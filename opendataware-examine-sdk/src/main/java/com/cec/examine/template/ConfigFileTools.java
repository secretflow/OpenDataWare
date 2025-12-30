package com.cec.examine.template;

import com.cec.examine.util.FileUtilityUtil;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 这个类单独使用
 * 适用于目前安全系统、质检系统导出的规则表的excel-csv文件处理合并
 * 其中
 * recognition_config.csv 为安全识别的配置文件
 * DIC_INTELLIGENT_TO_STANDARD.csv 为质检信息项目与质检算子落标的配置表
 * spt_standard_standard_range_code_doc.csv 为质检值域检查算子的配置表/码表
 * 这个类的目的在于，直接将落标配置与值域表进行整合，剔除掉不会落标的信息项
 * 整合后的配置表为 data_qi_config.csv：
 * 质检算子类型，质检算子编码，质检算子名称，安全信息项名称，标准编码，标准值域数组
 */
public class ConfigFileTools {

    public static void main(String [] args) {


        //安全识别信息项与规则ID绑定关系
        FileUtilityUtil DIC_INTELLIGENT_TO_STANDARD = FileUtilityUtil.getFileReader("C:\\Users\\admin\\Desktop\\test\\DIC_INTELLIGENT_TO_STANDARD.csv");
        //规则编码到安全基础信息项的映射表
        LinkedHashMap<String, String> qicCodeToBasicLabelName = new LinkedHashMap<>();
        while(true) {
            String [] cells = DIC_INTELLIGENT_TO_STANDARD.getCSVLineCells(",");
            if(cells == null) break;
            //基础信息项名称
            if(cells.length < 7) continue;
            String basicLabelName = cells[2];
            if(basicLabelName.equals("BASE_LABEL_NAME")) continue;
            String qiCodeList = cells[6];
            if("NULL".equals(cells[6])) continue;
            String [] qiCodeArr = qiCodeList.split(",");
            for(String qiCode: qiCodeArr) {
                qicCodeToBasicLabelName.put(qiCode,  basicLabelName);
            }

        }
        //值域标准码表
        FileUtilityUtil spt_standard_standard_range_code_doc = FileUtilityUtil.getFileReader("C:\\Users\\admin\\Desktop\\test\\spt_standard_standard_range_code_doc.csv");
        //标准值域表规则
        Map<String,String> standardCodeTable = new LinkedHashMap<>();
        char secondSep = '\001';
        while(true) {
            String [] cells = spt_standard_standard_range_code_doc.getCSVLineCells(",");
            if(cells == null) break;
            String standardCode = cells[2];
            String standardName = cells[3];
            String value1 = cells[4];
            String value2 = cells[9];
            String value3 = cells[10];
            String values = standardCodeTable.get(standardCode);
            if(values == null) {
                values="";
            }
            values+=value1 + secondSep + value2 + secondSep + value3 + secondSep;
            standardCodeTable.put(standardCode, values);
        }
        /*
        for(String key: standardCodeTable.keySet()) {
            System.out.println(key + "," + standardCodeTable.get(key).trim());
        }*/

        //写质检规则配置文件
        FileUtilityUtil fileUtilityUtil = FileUtilityUtil.getFileWriter("C:\\Users\\admin\\Desktop\\test\\data_qi_config.csv");
        //所有规则，以及规则与值域码表绑定关系
        FileUtilityUtil rule_band_standcode = FileUtilityUtil.getFileReader("C:\\Users\\admin\\Desktop\\test\\rule_band_standcode.csv");
        while(true) {
            String[] cells = rule_band_standcode.getCSVLineCells(",");
            if(cells == null) break;
            String type = cells[1];
            String qiCode = cells[3];
            String qiName = cells[4];
            String standcode = cells[11];
            String standardCodeValues = standardCodeTable.get(standcode);
            if(standardCodeValues != null) {
                standardCodeValues = standardCodeValues.trim();
            }
            String writeLine = type + "," + qiCode + "," + qiName + "," + qicCodeToBasicLabelName.get(qiCode) + "," + standcode + "," + standardCodeValues;
            //System.out.println(writeLine);
            if(qiCode.indexOf("QIC") != 0) continue;
            fileUtilityUtil.writeLine(writeLine);
        }
        fileUtilityUtil.close();
    }
}
