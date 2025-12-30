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
public class DataWareConvert {

    private TableResult tableResult;
    private List<String> columnNames;
    private List<DataType> dataTypes;

    /**
     * 把Flink的dataType转化为元件的dataType
     * @param dataType Flink dataType
     * @return 元件的dataType
     */
    public static DataWareDataType convertTocomponentDataType(DataType dataType) {
        if(dataType.toString().equals("STRING")){
            return DataWareDataType.STRING;
        } else if(dataType.toString().equals("INT")){
            return DataWareDataType.INT;
        } else if(dataType.toString().equals("FLOAT")){
            return DataWareDataType.FLOAT;
        } else if(dataType.toString().equals("BOOLEAN")){
            return DataWareDataType.BOOLEAN;
        } else if(dataType.toString().equals("DATE")){
            return DataWareDataType.DATE;
        } else if(dataType.toString().equals("TIME")){
            return DataWareDataType.TIME;
        } else if(dataType.toString().equals("TIMESTAMP")){
            return DataWareDataType.TIMESTAMP;
        } else if(dataType.toString().equals("CHAR")){
            return DataWareDataType.CHAR;
        } else if(dataType.toString().equals("VARCHAR")){
            return DataWareDataType.VARCHAR;
        } else if(dataType.toString().equals("ARRAY")){
            return DataWareDataType.ARRAY;
        } else if(dataType.toString().equals("MAP")){
            return DataWareDataType.MAP;
        } else if(dataType.toString().equals("ROW")){
            return DataWareDataType.ROW;
        } else if(dataType.toString().equals("DOUBLE")){
            return DataWareDataType.DOUBLE;
        } else if(dataType.toString().equals("BINARY")){
            return DataWareDataType.BINARY;
        } else if(dataType.toString().equals("BYTES")){
            return DataWareDataType.BYTES;
        } else if(dataType.toString().equals("DECIMAL")){
            return DataWareDataType.DECIMAL;
        } else if(dataType.toString().equals("INTEGER")){
            return DataWareDataType.INTEGER;
        } else if(dataType.toString().equals("NULL")){
            return DataWareDataType.NULL;
        } else if(dataType.toString().equals("SMALLINT")){
            return DataWareDataType.SMALLINT;
        } else {
            return null;
        }
    }

    /**
     * 用TableResult初始化
     * @param tableResult FLink TableResult
     */
    public DataWareConvert(TableResult tableResult) {
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
    public List<ModalDataWare> getModalDataWares(String mainKeyCol) {
        List<ModalDataWare> modalDataWares = new ArrayList<>();
        try (CloseableIterator<Row> iterator = tableResult.collect()) {
            while (iterator.hasNext()) {
                ModalDataWare  modalDataWare = new ModalDataWare();
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
                        modalDataWare.setMainKey(dataCell);
                    } else {
                        modalDataWare.setValue(dataCell);
                    }
                }
                modalDataWare.setValues(values);
                modalDataWares.add(modalDataWare);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return modalDataWares;
    }

    /**
     * 把TableResult结果转化为组态元件
     * @return 组态元件列表
     */
    public List<ComposedDataWare> getComposedDataWares() {
        List<ComposedDataWare> composedDataWares = new ArrayList<>();
        try (CloseableIterator<Row> iterator = tableResult.collect()) {
            int index = 0;
            while (iterator.hasNext()) {
                ComposedDataWare  composedDataWare = new ComposedDataWare();
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
                composedDataWare.setIndex(index++);
                composedDataWare.setValues(values);
                composedDataWares.add(composedDataWare);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return composedDataWares;
    }

    /**
     * 把TableResult结果转化为组合态元件
     * @param mainKeyCol 主体字段列名
     * @param queryColumns 查询列列表
     * @return 组合态元件列表
     */
    public List<CombinatorialDataWare> getCombinatorialDataWares(String mainKeyCol, List<String> queryColumns) {
        List<CombinatorialDataWare> combinatorialDataWares = new ArrayList<>();
        try (CloseableIterator<Row> iterator = tableResult.collect()) {
            int index = 0;
            while (iterator.hasNext()) {
                CombinatorialDataWare  combinatorialDataWare = new CombinatorialDataWare();
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
                        combinatorialDataWare.setMainKey(dataCell);
                    }
                }
                combinatorialDataWare.setIndex(index++);
                combinatorialDataWare.setQueryColumn(queryColumns);
                combinatorialDataWare.setValues(values);
                combinatorialDataWares.add(combinatorialDataWare);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return combinatorialDataWares;
    }
}
