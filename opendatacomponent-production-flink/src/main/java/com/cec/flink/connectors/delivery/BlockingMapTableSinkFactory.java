package com.cec.flink.connectors.delivery;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.alibaba.fastjson.JSONObject;
import com.cec.flink.connectors.http.CallEventListener;
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

/**
 * 阻塞Map的连接算子实现类
 * @author koala
 */
@Internal
public class BlockingMapTableSinkFactory implements DynamicTableSinkFactory {
    public static final String IDENTIFIER = "blockingMap";

    public String factoryIdentifier() {
        return IDENTIFIER;
    }

    public Set<ConfigOption<?>> requiredOptions() {
        return new HashSet<>();
    }

    public Set<ConfigOption<?>> optionalOptions() {
        Set<ConfigOption<?>> options = new HashSet<>();
        options.add(FactoryUtil.SINK_PARALLELISM);
        return options;
    }

    public DynamicTableSink createDynamicTableSink(Context context) {
        FactoryUtil.TableFactoryHelper helper = FactoryUtil.createTableFactoryHelper(this, context);
        helper.validate();
        ReadableConfig options = helper.getOptions();
        return new BlockingMapSink(context.getCatalogTable().getResolvedSchema(), context.getCatalogTable().getPartitionKeys());
    }

    private static class BlockingMapSink implements DynamicTableSink, SupportsPartitioning {
        private final ResolvedSchema schema;
        private final List<String> partitionKeys;

        private BlockingMapSink(ResolvedSchema schema, List<String> partitionKeys) {
            this.schema = schema;
            this.partitionKeys = partitionKeys;
        }

        public ChangelogMode getChangelogMode(ChangelogMode requestedMode) {
            return requestedMode;
        }

        public SinkRuntimeProvider getSinkRuntimeProvider(Context context) {
            DataStructureConverter converter = context.createDataStructureConverter(this.schema.toPhysicalRowDataType());
            return SinkFunctionProvider.of(new RowDataBlockingMapFunction(converter, this.schema.toPhysicalRowDataType(), this.schema.getColumnNames()));
        }

        public DynamicTableSink copy() {
            return new BlockingMapSink(this.schema, this.partitionKeys);
        }

        public String asSummaryString() {
            return "BlockingMapSink";
        }

        //分区处理
        public void applyStaticPartition(Map<String, String> partition) {
            for(String partitionCol : this.partitionKeys) {
            }
        }
    }

    private static class RowDataBlockingMapFunction extends RichSinkFunction<RowData> {
        private final DynamicTableSink.DataStructureConverter converter;
        private final DataType type;
        private final List<String> columnNames;
        private RowDataBlockingMapFunction(DynamicTableSink.DataStructureConverter converter, DataType type, List<String> columnNames) {
            this.converter = converter;
            this.type = type;
            this.columnNames = columnNames;
        }

        public void open(OpenContext openContext) throws Exception {
            super.open(openContext);
            StreamingRuntimeContext context = (StreamingRuntimeContext)this.getRuntimeContext();
        }

        public void invoke(RowData value, Context context) {
            String requestId = null;
            JSONObject resultJSON = new  JSONObject();
            for (int i = 0; i < value.getArity(); i++) {
                if (value instanceof GenericRowData) {
                    String colName = this.columnNames.get(i);
                    String cell = ((GenericRowData) value).getField(i).toString();
                    if (colName.equals("requestId")) {
                        requestId = cell;
                    }
                    resultJSON.put(colName, cell);
                }
            }
            //先清空在写入
            CallEventListener.clearResultJSONStrings();
            try {
                CallEventListener.putResultJSONString(requestId, resultJSON.toString());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
