package com.cec.examine.recognize;

import com.cec.examine.dict.HIT;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author qiuwenyi
 * @version 1.0.0
 * @ClassName Recognize
 * @Description 正则识别模式对象, 此类识别对象仅能支持一个正则表达式，一种类型
 * @createTime 2023/10/08
 */
public class RecognizeRegularExpression extends Recognize {

    public RecognizeRegularExpression(String pattern) {
        this.pattern = pattern;
    }

    public RecognizeRegularExpression() {
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    private String pattern;


    /**
     * 获取识别结果
     * @param text
     * @return
     */
    @Override
    public List<HIT> getHitResult(String text) {
        if(text == null || "".equals(text)) return null;
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(text);
        List<HIT> res = new ArrayList<HIT>();
        while(m.find()) {
            String source = m.group(0);
            if("".equals(source)) continue;//排除空串干扰的问题，有些正则会将空串作为识别对象，本类不支持此类型
            //组装替换结果
            HIT hit = new HIT();
            hit.start = m.start();
            hit.end = m.end();
            hit.source = source;
            hit.target = "?";
            hit.type = this.getName();
            res.add(hit);
        }
        return res;
    }

    /**
     * 将字符串作为一个整体，匹配则返回类型，不匹配则直接返回null
     * @param text 需要匹配的字符串
     * @return
     */
    @Override
    public String getSingleType(String text) {
        if(text == null || "".equals(text)) return null;
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(text);
        if(m.matches()) return this.getName();
        else return null;
    }

    @Override
    public List<String> getTypeOfLongText(String text) {
        List<String> types = new ArrayList<String>();
        String type = getSingleType(text);
        if(type != null) types.add(type);
        return types;
    }

}
