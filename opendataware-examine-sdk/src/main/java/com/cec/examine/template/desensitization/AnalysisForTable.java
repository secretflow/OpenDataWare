package com.cec.examine.template.desensitization;

import com.cec.examine.template.TableCellPojo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 分析一个小表
 */
public class AnalysisForTable {

    /**
     * 将这张样本表的所有分析结果返回
     * @return
     */

    private List<ColumnResult> columnResultList = new ArrayList<>();

    public List<ColumnResult> getTableResultList() {
        return columnResultList;
    }

    public AnalysisForTable(List<Map<String, TableCellPojo>> data) {
        if(data!=null && !data.isEmpty()){
            Map<String, TableCellPojo> firstLine = data.get(0);
            for(String columnName : firstLine.keySet()){
                //按照列遍历
                String dataType = firstLine.get(columnName).getDataType().replaceAll("\\(.*?\\)", "");
                if (!isNumeric(dataType)) {
                    //字符串类型
                    List<String> colListData = new ArrayList<>();
                    for (Map<String, TableCellPojo> datum : data) {
                        TableCellPojo tableCellPojo = datum.get(columnName);
                        String value = tableCellPojo.getValue() != null ? String.valueOf(tableCellPojo.getValue()) : null;
                        colListData.add(value);
                    }
                    //分析字符串
                    AnalysisResultForEnum analysisResultForEnum = new AnalysisResultForEnum(colListData);
                    columnResultList.add(new ColumnResult(columnName,false, analysisResultForEnum));
                } else {
                    //分析数字
                    AnalysisResultForNumeric<?> analysisResultForNumeric = null;
                    switch (dataType.toLowerCase()) {
                        case "integer":
                        case "int":
                        case "short":
                            List<Integer> colListDataInteger = new ArrayList<>();
                            for (Map<String, TableCellPojo> datum : data) {
                                TableCellPojo tableCellPojo = datum.get(columnName);
                                Integer value = (Integer) tableCellPojo.getValue();
                                colListDataInteger.add(value);
                            }
                            analysisResultForNumeric = new AnalysisResultForNumeric<Integer>(colListDataInteger, Integer.class);
                            break;
                        case "float":
                            List<Float> colListDataFloat = new ArrayList<>();
                            for (Map<String, TableCellPojo> datum : data) {
                                TableCellPojo tableCellPojo = datum.get(columnName);
                                Float value = (Float) tableCellPojo.getValue();
                                colListDataFloat.add(value);
                            }
                            analysisResultForNumeric = new AnalysisResultForNumeric<Float>(colListDataFloat, Float.class);
                            break;
                        case "long":
                        case "unsigned":
                            List<Long> colListDataLong = new ArrayList<>();
                            for (Map<String, TableCellPojo> datum : data) {
                                TableCellPojo tableCellPojo = datum.get(columnName);
                                Long value = (Long) tableCellPojo.getValue();
                                colListDataLong.add(value);
                            }
                            analysisResultForNumeric = new AnalysisResultForNumeric<Long>(colListDataLong,  Long.class);
                            break;
                        case "byte":
                            List<Byte> colListDataByte = new ArrayList<>();
                            for (Map<String, TableCellPojo> datum : data) {
                                TableCellPojo tableCellPojo = datum.get(columnName);
                                Byte value = (Byte) tableCellPojo.getValue();
                                colListDataByte.add(value);
                            }
                            analysisResultForNumeric = new AnalysisResultForNumeric<Byte>(colListDataByte, Byte.class);
                            break;
                        default:
                            List<Double> colListDataDouble = new ArrayList<>();
                            for (Map<String, TableCellPojo> datum : data) {
                                TableCellPojo tableCellPojo = datum.get(columnName);
                                Double value = (Double) tableCellPojo.getValue();
                                colListDataDouble.add(value);
                            }
                            analysisResultForNumeric = new AnalysisResultForNumeric<Double>(colListDataDouble, Double.class);
                            break;
                    }
                    columnResultList.add(new ColumnResult(columnName,true, analysisResultForNumeric));
                }
            }
        }
    }

    public static boolean isNumeric(String dataType) {
        //去掉所有括号部分
        if(dataType != null) {
            dataType = dataType.replaceAll("\\(.*?\\)", "");
            return dataType.equalsIgnoreCase("number")
                    || dataType.equalsIgnoreCase("integer")
                    || dataType.equalsIgnoreCase("float")
                    || dataType.equalsIgnoreCase("double")
                    || dataType.equalsIgnoreCase("long")
                    || dataType.equalsIgnoreCase("int")
                    || dataType.equalsIgnoreCase("decimal")
                    || dataType.equalsIgnoreCase("unsigned")
                    || dataType.equalsIgnoreCase("short")
                    || dataType.equalsIgnoreCase("byte");
        } else return false;
    }

    /**
     * 装载列级别的结果
     */
    public static class ColumnResult {
        public String getColumnName() {
            return columnName;
        }

        public boolean isNumeric() {
            return isNumeric;
        }

        public AnalysisResult getAnalysisResult() {
            return analysisResult;
        }

        private final String columnName;
        private final boolean isNumeric;
        private final AnalysisResult analysisResult;
        public ColumnResult(String columnName, boolean isNumeric, AnalysisResult analysisResult) {
            this.columnName = columnName;
            this.isNumeric = isNumeric;
            this.analysisResult = analysisResult;
        }
    }
}
