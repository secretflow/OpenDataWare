package com.cec.examine.desensitization.hash;

import com.cec.examine.desensitization.SensitivePatternRegex;
import com.cec.examine.util.encryption.MD5Util;

import java.util.regex.Matcher;

/**
 * 学历授予学校
 *^：表示匹配字符串的开头。
 * [\u4E00-\u9FA5]{2,10}：表示匹配2到10个汉字，用于匹配学校的中文名称。根据实际情况，您可以调整数字范围以适应不同学校名称的长度。
 * (大学|学院|中学)：表示匹配 "大学"、"学院" 或 "中学" 之一，用于匹配学校类型的关键词。您可以根据需要添加或修改这些关键词。
 * $：表示匹配字符串的结尾。
 **/
public class DegreeGrantingSchoolMD5 extends SensitivePatternRegex {

    public DegreeGrantingSchoolMD5(){

    }
    @Override
    public String pattern() {
        String pattern =  "^[\\u4E00-\\u9FA5]{2,10}(学校|大学|学院|中学|小学)$";
        return pattern;
    }

    @Override
    public String target(String source) {
        return MD5Util.textToMD5L32(getSaltedValue(source));
    }
}
