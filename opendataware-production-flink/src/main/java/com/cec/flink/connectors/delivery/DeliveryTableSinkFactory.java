package com.cec.flink.connectors.delivery;
import com.cec.deliver.DataWareDelivery;
import com.cec.deliver.DataWareDeliveryManager;
import com.cec.modeling.*;
import org.apache.flink.annotation.Internal;
import org.apache.flink.api.common.functions.OpenContext;
import org.apache.flink.configuration.ConfigOption;
import org.apache.flink.configuration.ReadableConfig;
import org.apache.flink.streaming.api.functions.sink.legacy.RichSinkFunction;
import org.apache.flink.streaming.api.operators.StreamingRuntimeContext;
import org.apache.flink.table.catalog.ResolvedSchema;
import org.apache.flink.table.connector.ChangelogMode;
import org.apache.flink.table.connector.sink.DynamicTableSink;
import org.apache.flink.table.connector.sink.abilities.SupportsPartitioning;
import org.apache.flink.table.connector.sink.legacy.SinkFunctionProvider;
import org.apache.flink.table.data.GenericRowData;
import org.apache.flink.table.data.RowData;
import org.apache.flink.table.factories.DynamicTableSinkFactory;
import org.apache.flink.table.factories.FactoryUtil;
import org.apache.flink.table.types.DataType;
import java.util.*;

import static com.cec.flink.DataWareConvert.convertTocomponentDataType;
/**
 * 元件交付连接算子实现类
 * @author koala
 */
@Internal
public class DeliveryTableSinkFactory implements DynamicTableSinkFactory {
    public static final String IDENTIFIER = "delivery";

    public String factoryIdentifier() {
        return IDENTIFIER;
    }

    public Set<ConfigOption<?>> requiredOptions() {
        Set<ConfigOption<?>> options = new HashSet<>();
        options.add(DeliveryConnectorOptions.DB_NAME);
        options.add(DeliveryConnectorOptions.DC_TYPE);
        options.add(DeliveryConnectorOptions.DC_DELIVERY_CLASS);
        options.add(DeliveryConnectorOptions.DC_MAIN_KEY);
        options.add(DeliveryConnectorOptions.DC_ID);
        return options;
    }

    public Set<ConfigOption<?>> optionalOptions() {
        Set<ConfigOption<?>> options = new HashSet<>();
        options.add(FactoryUtil.SINK_PARALLELISM);
        options.add(DeliveryConnectorOptions.DC_DELIVERY_PROP);
        return options;
    }

    public DynamicTableSink createDynamicTableSink(Context context) {
        FactoryUtil.TableFactoryHelper helper = FactoryUtil.createTableFactoryHelper(this, context);
        helper.validate();
        ReadableConfig options = helper.getOptions();
        return new DeliverySink(context.getCatalogTable().getResolvedSchema(), context.getCatalogTable().getPartitionKeys(), options);
    }

    private static class DeliverySink implements DynamicTableSink, SupportsPartitioning {
        private final ResolvedSchema schema;
        private final List<String> partitionKeys;
        private final ReadableConfig options;

        private DeliverySink(ResolvedSchema schema, List<String> partitionKeys, ReadableConfig options) {
            this.schema = schema;
            this.partitionKeys = partitionKeys;
            this.options = options;
        }

        public ChangelogMode getChangelogMode(ChangelogMode requestedMode) {
            return requestedMode;
        }

        public SinkRuntimeProvider getSinkRuntimeProvider(Context context) {
            DataStructureConverter converter = context.createDataStructureConverter(this.schema.toPhysicalRowDataType());
            return SinkFunctionProvider.of(new RowDataDeliveryFunction(this.options, converter, this.schema.toPhysicalRowDataType(), this.schema.getColumnNames()));
        }

        public DynamicTableSink copy() {
            return new DeliverySink(this.schema, this.partitionKeys, this.options);
        }

        public String asSummaryString() {
            return "DeliverySink";
        }

        //分区处理
        public void applyStaticPartition(Map<String, String> partition) {
            for(String partitionCol : this.partitionKeys) {
            }
        }
    }

    private static class RowDataDeliveryFunction extends RichSinkFunction<RowData> {
        private final DynamicTableSink.DataStructureConverter converter;
        private final DataType type;
        private final List<String> columnNames;
        private final ReadableConfig options;
        private RowDataDeliveryFunction(ReadableConfig options, DynamicTableSink.DataStructureConverter converter, DataType type, List<String> columnNames) {
            this.converter = converter;
            this.type = type;
            this.columnNames = columnNames;
            this.options = options;
        }

        public void open(OpenContext openContext) throws Exception {
            super.open(openContext);
            StreamingRuntimeContext context = (StreamingRuntimeContext)this.getRuntimeContext();
        }

        public void invoke(RowData value, Context context) {
            String dataBaseName = options.get(DeliveryConnectorOptions.DB_NAME);
            String dcType = options.get(DeliveryConnectorOptions.DC_TYPE);
            String dcId = options.get(DeliveryConnectorOptions.DC_ID);
            String mainKeyCol = options.get(DeliveryConnectorOptions.DC_MAIN_KEY);
            String deliveryClassName = options.get(DeliveryConnectorOptions.DC_DELIVERY_CLASS);
            Map<String,String> dcDeliveryOptions = options.get(DeliveryConnectorOptions.DC_DELIVERY_PROP);
            DataWareDelivery DataWareDelivery =  DataWareDeliveryManager.getOrCreateDataBase(dataBaseName, deliveryClassName, dcDeliveryOptions);
            DataWare DataWare = null;
            List<DataCell> values = new ArrayList<>();
            for (int i = 0; i < value.getArity(); i++) {
               String columnName = this.columnNames.get(i);
               Object dataCellValue = ((GenericRowData) value).getField(i);
               DataType dataType = this.type.getChildren().get(i);
               DataCell dataCell = new DataCell();
               dataCell.setValueColumn(columnName);
               dataCell.setValue(dataCellValue);
               dataCell.setValueDataType(convertTocomponentDataType(dataType));
               values.add(dataCell);
               if(dcType.toUpperCase().equals(DataWareType.MODAL)) {
                   if(DataWare == null) DataWare = new ModalDataWare();
                   if(mainKeyCol != null && mainKeyCol.equals(columnNames.get(i))) {
                       ((ModalDataWare)DataWare).setMainKey(dataCell);
                   } else {
                       ((ModalDataWare)DataWare).setValue(dataCell);
                   }
               } else if (dcType.toUpperCase().equals(DataWareType.COMPOSED)) {
                   if(DataWare == null) DataWare = new ComposedDataWare();
                   ((ComposedDataWare)DataWare).setIndex(0);
               } else if (dcType.toUpperCase().equals(DataWareType.COMBINATORIAL)) {
                   if(DataWare == null) DataWare = new CombinatorialDataWare();
                   ((CombinatorialDataWare)DataWare).setIndex(0);
                   if(mainKeyCol != null && mainKeyCol.equals(columnNames.get(i))) {
                       ((CombinatorialDataWare)DataWare).setMainKey(dataCell);
                   }
               }
            }//for
            DataWare.setValues(values);
            if(dcType.toUpperCase().equals(DataWareType.MODAL)) {
                if(DataWare == null) DataWare = new ModalDataWare();
                DataWareDelivery.loadModalDataWare(dcId, (ModalDataWare)DataWare);
            } else if (dcType.toUpperCase().equals(DataWareType.COMPOSED)) {
                if(DataWare == null) DataWare = new ComposedDataWare();
                DataWareDelivery.loadComposedDataWare(dcId, (ComposedDataWare)DataWare);
            } else if (dcType.toUpperCase().equals(DataWareType.COMBINATORIAL)) {
                if(DataWare == null) DataWare = new CombinatorialDataWare();
                DataWareDelivery.loadCombinatorialDataWare(dcId, (CombinatorialDataWare)DataWare);
            }
        }
    }
}
