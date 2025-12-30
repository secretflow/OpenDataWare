package com.cec.examine.util;

import com.cec.examine.desensitization.generator.*;
import com.cec.examine.template.TableCellPojo;

import java.util.*;

/**
 * 方针数据生成工具类
 */
public class MockDataSet {

    Random random = new Random();
    Builder builder;
    public MockDataSet(Builder builder) {
        this.builder = builder;
    }

    public static Builder getExampleBuilder(){
        Builder builder = new Builder();
        return builder
                .addColumn(new ColumnBuilder()
                    .setEnglishColumnName("id")
                    .setChineseColumnName("身份证号")
                    .setDataType("STRING")
                    .setValue(new GeneratorID()))
                .addColumn(new ColumnBuilder()
                        .setEnglishColumnName("age")
                        .setChineseColumnName("年龄")
                        .setDataType("INT(3)")
                        .setValue(new GeneratorAge()))
                .addColumn(new ColumnBuilder()
                        .setEnglishColumnName("gender")
                        .setChineseColumnName("性别")
                        .setDataType("VARCHAR(2)")
                        .setValue(new GeneratorGender()))
                .addColumn(new ColumnBuilder()
                        .setEnglishColumnName("phone")
                        .setChineseColumnName("手机号")
                        .setDataType("VARCHAR(15)")
                        .setValue(new GeneratorPhone()))
                .addColumn(new ColumnBuilder()
                        .setEnglishColumnName("name")
                        .setChineseColumnName("姓名")
                        .setDataType("VARCHAR(5)")
                        .setValue(new GeneratorName()))
                .addColumn(new ColumnBuilder()
                        .setEnglishColumnName("md5")
                        .setChineseColumnName("哈希值")
                        .setDataType("VARCHAR(32)")
                        .setValue(new MD5Generator()));
    }

    public List<Map<String, TableCellPojo>> mockListMap(Integer rows) {
        List<Map<String, TableCellPojo>> data = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            Map<String, TableCellPojo> map = new HashMap<>();
            for(ColumnBuilder columnBuilder : builder.columnBuilders){
                TableCellPojo tableCellPojo = new TableCellPojo();
                tableCellPojo.setDataType(columnBuilder.dataType);
                tableCellPojo.setValue((i+random.nextInt(30)) % 30 == 0 ? null : columnBuilder.value.generateFixed(String.valueOf(i)));
                tableCellPojo.setEnglishColumnName(columnBuilder.englishColumnName);
                tableCellPojo.setChineseColumnName(columnBuilder.chineseColumnName);
                map.put(columnBuilder.englishColumnName, tableCellPojo);
            }
            data.add(map);
        }
        return data;
    }

    public String dataMockJSON(Integer rows) {
        StringBuilder sb = new StringBuilder();
        List<Map<String, TableCellPojo>> listMap =  mockListMap(rows);
        sb.append("[");
        for (Map<String, TableCellPojo> map : listMap) {
            sb.append("{");
            for (Map.Entry<String, TableCellPojo> entry : map.entrySet()) {
                sb.append("\"").append(entry.getKey()).append("\":\"").append(entry.getValue().getValue()).append("\",");
            }
            sb.deleteCharAt(sb.length() - 1);
            sb.append("},");
        }
        if(!listMap.isEmpty()) sb.deleteCharAt(sb.length() - 1);
        sb.append("]");
        return sb.toString();
    }

    public static void print(List<Map<String, TableCellPojo>> data) {
        if(data != null && !data.isEmpty()) {
            Map<String, Integer> colMaxLengthmap = new HashMap<>();
            Set<String> colNames = data.get(0).keySet();
            for(String colName : colNames) {
                int maxLen = colName.length();
                for(Map<String, TableCellPojo> line: data) {
                    int len = String.valueOf(line.get(colName).getValue()).length();
                    if(maxLen < len) maxLen = len;
                }
                colMaxLengthmap.put(colName, maxLen + 3);
            }
            //打印表头
            for(String colName : colNames) {
                System.out.printf("%-" + colMaxLengthmap.get(colName) + "s\t|", colName);
            }
            System.out.println();
            //打印内容
            for(Map<String, TableCellPojo> line: data) {
                for(String colName : colNames) {
                    String value = String.valueOf(line.get(colName).getValue());
                    if("peak".equals(line.get(colName).getComments())) value = "[P]" + value;
                    else if("outlier".equals(line.get(colName).getComments())) value = "[O]" + value;
                    System.out.printf("%-" + colMaxLengthmap.get(colName) + "s\t|", value);
                }
                System.out.println();
            }
            System.out.println();
        }
    }

    /**
     * 构造器
     */
    public static class Builder {

        List<ColumnBuilder> columnBuilders = new ArrayList<>();
        public Builder addColumn(ColumnBuilder columnBuilder) {
            columnBuilders.add(columnBuilder);
            return this;
        }

        public MockDataSet build() {
            return new MockDataSet(this);
        }

    }

    public static class ColumnBuilder {
        public String getComment() {
            return comment;
        }

        public ColumnBuilder setComment(String comment) {
            this.comment = comment;
            return this;
        }

        public String getChineseColumnName() {
            return chineseColumnName;
        }

        public ColumnBuilder setChineseColumnName(String chineseColumnName) {
            this.chineseColumnName = chineseColumnName;
            return this;
        }

        public String getEnglishColumnName() {
            return englishColumnName;
        }

        public ColumnBuilder setEnglishColumnName(String englishColumnName) {
            this.englishColumnName = englishColumnName;
            return this;
        }

        public Generator getValue() {
            return value;
        }

        public ColumnBuilder setValue(Generator value) {
            this.value = value;
            return this;
        }

        public String getDataType() {
            return dataType;
        }

        public ColumnBuilder setDataType(String dataType) {
            this.dataType = dataType;
            return this;
        }

        private String dataType;
        private Generator value;
        private String englishColumnName;
        private String chineseColumnName;
        private String comment;
    }

}
