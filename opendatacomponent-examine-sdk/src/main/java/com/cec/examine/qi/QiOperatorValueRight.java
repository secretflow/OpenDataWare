package com.cec.examine.qi;

import com.cec.examine.template.TableCellPojo;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * 值域准确性，目前只有身份证校验
 */
public class QiOperatorValueRight extends QiOperator {

    private String regexPatern;//如果有信息项对应的正则，那么加进来

    public String getRegexPatern() {
        return regexPatern;
    }

    public void setRegexPatern(String regexPatern) {
        this.regexPatern = regexPatern;
    }

    private Map<String, String> basicLabelNameToColumnName;

    public QiOperatorValueRight(Map<String, String> basicLabelNameToColumnName) {
        //这个算子主要考察性别、年龄与身份证的匹配关系
        this.basicLabelNameToColumnName = basicLabelNameToColumnName;
    }


    @Override
    public void put(String column, Map<String, TableCellPojo> line) {
        String cell = String.valueOf(line.get(column).getValue());
        if(basicLabelNameToColumnName != null) {
            String genderCol = basicLabelNameToColumnName.get("性别");
            String ageCol = basicLabelNameToColumnName.get("年龄");
            if (genderCol != null && line.containsKey(genderCol)) {
                String gender = String.valueOf(line.get(genderCol).getValue());
                if ("M".equals(gender.toUpperCase()) || "MALE".equals(gender.toUpperCase()) || "MEN".equals(gender.toUpperCase()))
                    gender = "M";
                if ("F".equals(gender.toUpperCase()) || "FEMALE".equals(gender.toUpperCase()) || "WOMEN".equals(gender.toUpperCase()))
                    gender = "F";
                if (!gender.equals(getGenderFromId(cell))) {
                    this.countOfProblems++;
                }
            }
            if (ageCol != null && line.containsKey(ageCol)) {
                String age = String.valueOf(line.get(ageCol).getValue());
                if (!age.equals(String.valueOf(calculateAgeFromIdNumber(cell)))) {
                    this.countOfProblems++;
                }
            }
            this.countOfCells++;
        }
    }

    @Override
    public void execute() {

    }
    @Override
    public void put(Map<String, TableCellPojo> line) {

    }

    private static String getGenderFromId(String id) {
        if (id == null || id.length() != 18) {
            //无效的身份证号码
            return null;
        }
        char genderChar = id.charAt(16);
        int genderValue = Integer.parseInt(String.valueOf(genderChar));
        if (genderValue % 2 == 0) {
            return "F";
        } else {
            return "M";
        }
    }

    public int calculateAgeFromIdNumber(String idNumber) {
        if (idNumber == null || idNumber.length() != 18) {
            throw new IllegalArgumentException("身份证号码不正确");
        }
        // 获取出生年月日
        String birthdayStr = idNumber.substring(6, 14);
        // 将出生年月日转换为日期格式
        LocalDate birthday = LocalDate.parse(birthdayStr, DateTimeFormatter.ofPattern("yyyyMMdd"));
        // 获取当前日期
        LocalDate today = LocalDate.now();
        // 计算年龄
        return Period.between(birthday, today).getYears();
    }
}
