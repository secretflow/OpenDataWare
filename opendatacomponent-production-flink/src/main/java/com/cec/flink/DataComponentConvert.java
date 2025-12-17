package com.cec.flink;

import com.cec.modeling.*;
import org.apache.flink.table.api.TableResult;
import org.apache.flink.table.catalog.ResolvedSchema;
import org.apache.flink.table.types.DataType;
import org.apache.flink.types.Row;
import org.apache.flink.util.CloseableIterator;

import java.util.ArrayList;
import java.util.List;

/**
 * 这是一个工具类，可以提取Flink计算后的数据转化为元件格式
 * @author koala
 */
public class DataComponentConvert {

    private TableResult tableResult;
    private List<String> columnNames;
    private List<DataType> dataTypes;

    /**
     * 把Flink的dataType转化为元件的dataType
     * @param dataType Flink dataType
     * @return 元件的dataType
     */
    public static DataComponentDataType convertTocomponentDataType(DataType dataType) {
        if(dataType.toString().equals("STRING")){
            return DataComponentDataType.STRING;
        } else if(dataType.toString().equals("INT")){
            return DataComponentDataType.INT;
        } else if(dataType.toString().equals("FLOAT")){
            return DataComponentDataType.FLOAT;
        } else if(dataType.toString().equals("BOOLEAN")){
            return DataComponentDataType.BOOLEAN;
        } else if(dataType.toString().equals("DATE")){
            return DataComponentDataType.DATE;
        } else if(dataType.toString().equals("TIME")){
            return DataComponentDataType.TIME;
        } else if(dataType.toString().equals("TIMESTAMP")){
            return DataComponentDataType.TIMESTAMP;
        } else if(dataType.toString().equals("CHAR")){
            return DataComponentDataType.CHAR;
        } else if(dataType.toString().equals("VARCHAR")){
            return DataComponentDataType.VARCHAR;
        } else if(dataType.toString().equals("ARRAY")){
            return DataComponentDataType.ARRAY;
        } else if(dataType.toString().equals("MAP")){
            return DataComponentDataType.MAP;
        } else if(dataType.toString().equals("ROW")){
            return DataComponentDataType.ROW;
        } else if(dataType.toString().equals("DOUBLE")){
            return DataComponentDataType.DOUBLE;
        } else if(dataType.toString().equals("BINARY")){
            return DataComponentDataType.BINARY;
        } else if(dataType.toString().equals("BYTES")){
            return DataComponentDataType.BYTES;
        } else if(dataType.toString().equals("DECIMAL")){
            return DataComponentDataType.DECIMAL;
        } else if(dataType.toString().equals("INTEGER")){
            return DataComponentDataType.INTEGER;
        } else if(dataType.toString().equals("NULL")){
            return DataComponentDataType.NULL;
        } else if(dataType.toString().equals("SMALLINT")){
            return DataComponentDataType.SMALLINT;
        } else {
            return null;
        }
    }

    /**
     * 用TableResult初始化
     * @param tableResult FLink TableResult
     */
    public DataComponentConvert(TableResult tableResult) {
        this.tableResult = tableResult;
        ResolvedSchema schema = tableResult.getResolvedSchema();
        this.columnNames = schema.getColumnNames();
        this.dataTypes = schema.getColumnDataTypes();

    }

    /**
     * 把TableResult结果转化为模态元件
     * @param mainKeyCol 主体字段列名
     * @return 模态元件列表
     */
    public List<ModalDataComponent> getModalDataComponents(String mainKeyCol) {
        List<ModalDataComponent> modalDataComponents = new ArrayList<>();
        try (CloseableIterator<Row> iterator = tableResult.collect()) {
            while (iterator.hasNext()) {
                ModalDataComponent  modalDataComponent = new ModalDataComponent();
                List<DataCell> values = new  ArrayList<>();
                Row row = iterator.next();
                // 在这里处理每一行数据，例如获取字段值
                for( int i = 0; i < columnNames.size(); i++ ) {
                    String columnName = columnNames.get(i);
                    Object value = row.getFieldAs(columnNames.get(i));
                    DataCell dataCell = new DataCell();
                    dataCell.setValueColumn(columnName);
                    dataCell.setValue(value);
                    dataCell.setValueDataType(convertTocomponentDataType(dataTypes.get(i)));
                    values.add(dataCell);
                    if(mainKeyCol != null && mainKeyCol.equals(columnNames.get(i))) {
                        modalDataComponent.setMainKey(dataCell);
                    } else {
                        modalDataComponent.setValue(dataCell);
                    }
                }
                modalDataComponent.setValues(values);
                modalDataComponents.add(modalDataComponent);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return modalDataComponents;
    }

    /**
     * 把TableResult结果转化为组态元件
     * @return 组态元件列表
     */
    public List<ComposedDataComponent> getComposedDataComponents() {
        List<ComposedDataComponent> composedDataComponents = new ArrayList<>();
        try (CloseableIterator<Row> iterator = tableResult.collect()) {
            int index = 0;
            while (iterator.hasNext()) {
                ComposedDataComponent  composedDataComponent = new ComposedDataComponent();
                List<DataCell> values = new  ArrayList<>();
                Row row = iterator.next();
                // 在这里处理每一行数据，例如获取字段值
                for( int i = 0; i < columnNames.size(); i++ ) {
                    String columnName = columnNames.get(i);
                    Object value = row.getFieldAs(columnNames.get(i));
                    DataCell dataCell = new DataCell();
                    dataCell.setValueColumn(columnName);
                    dataCell.setValue(value);
                    dataCell.setValueDataType(convertTocomponentDataType(dataTypes.get(i)));
                    values.add(dataCell);
                }
                composedDataComponent.setIndex(index++);
                composedDataComponent.setValues(values);
                composedDataComponents.add(composedDataComponent);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return composedDataComponents;
    }

    /**
     * 把TableResult结果转化为组合态元件
     * @param mainKeyCol 主体字段列名
     * @param queryColumns 查询列列表
     * @return 组合态元件列表
     */
    public List<CombinatorialDataComponent> getCombinatorialDataComponents(String mainKeyCol, List<String> queryColumns) {
        List<CombinatorialDataComponent> combinatorialDataComponents = new ArrayList<>();
        try (CloseableIterator<Row> iterator = tableResult.collect()) {
            int index = 0;
            while (iterator.hasNext()) {
                CombinatorialDataComponent  combinatorialDataComponent = new CombinatorialDataComponent();
                List<DataCell> values = new  ArrayList<>();
                Row row = iterator.next();
                // 在这里处理每一行数据，例如获取字段值
                for( int i = 0; i < columnNames.size(); i++ ) {
                    String columnName = columnNames.get(i);
                    Object value = row.getFieldAs(columnNames.get(i));
                    DataCell dataCell = new DataCell();
                    dataCell.setValueColumn(columnName);
                    dataCell.setValue(value);
                    dataCell.setValueDataType(convertTocomponentDataType(dataTypes.get(i)));
                    values.add(dataCell);
                    if(mainKeyCol != null && mainKeyCol.equals(columnNames.get(i))) {
                        combinatorialDataComponent.setMainKey(dataCell);
                    }
                }
                combinatorialDataComponent.setIndex(index++);
                combinatorialDataComponent.setQueryColumn(queryColumns);
                combinatorialDataComponent.setValues(values);
                combinatorialDataComponents.add(combinatorialDataComponent);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return combinatorialDataComponents;
    }
}
