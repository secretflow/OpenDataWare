package com.cec.examine.recognize;
import com.cec.examine.dict.HIT;
import com.cec.examine.ner.BertNerTypeCode;
import com.cec.examine.util.FileUtilityUtil;
import com.cec.examine.util.name.FamilyNames;

import java.io.InputStream;
import java.util.*;

/**
 * @author qiuwenyi
 * @version 1.0.0
 * @ClassName Recognize
 * @Description 基于规则的NER识别类本地版本，不需要加载模型，不需要走serving服务
 * 方法缺陷：不支持长文本识别，只支持实体文本
 * @createTime 2023/10/08
 */
public class RecognizeNERRuleBased extends Recognize {

    private Recognize dictRecognize = null;

    private Recognize addressRegExpRecognize = null;
    public RecognizeNERRuleBased() {
        //加载姓氏
        Map<String, String> datas = new HashMap<>();
        for(int i = 0; i < FamilyNames.getFamilyNames().length; i++) {
            datas.put(FamilyNames.getFamilyNames()[i], BertNerTypeCode.NAME);
        }
        //加载行政区
        InputStream inputStream = RecognizeNERRuleBased.class.getClassLoader().getResourceAsStream("provinces.csv");
        FileUtilityUtil fileUtilityUtil = FileUtilityUtil.getFileReader(inputStream);
        while (true) {
            String line = fileUtilityUtil.getLine();
            if(line == null) break;
            datas.put(line, BertNerTypeCode.NATIVE_PLACE);
        }
        dictRecognize = new RecognizeDictionaryMultiTypes(datas, true);
        //地址正则识别
        String pattern = "([\\u4e00-\\u9fa5]{2,5}(?:省|自治区|市))([\\u4e00-\\u9fa5]{2,7}?(?:市|区|县|州)){0,1}([\\u4e00-\\u9fa5]{2,7}?(?:路|街|巷|道|弄|号)){0,1}";
        addressRegExpRecognize = new RecognizeRegularExpression(pattern);
        addressRegExpRecognize.setName(BertNerTypeCode.ADDRESS);
    }
    @Override
    public String getSingleType(String text) {
        String type = null;
        if (text.length() >=2 && text.length() <= 8 && type == null) {
            type = dictRecognize.getSingleType(text);
        }
        if(text.length() >=2 && text.length() <=3 && type == null) {
            type = dictRecognize.getSingleType(text.substring(0,1));
        }
        if (text.length() >=3 && text.length() <=4 && type == null) {
            type = dictRecognize.getSingleType(text.substring(0,2));
        }
        if (text.length() >= 2 && type == null) {
            type = addressRegExpRecognize.getSingleType(text);
        }
        String aliasType = getTypeAlias(type);
        return aliasType;
    }

    @Override
    public List<String> getTypeOfLongText(String text) {
        List<String> r = new ArrayList<>();
        String type = getSingleType(text);
        if(type != null) {
            r.add(type);
        }
        return r;
    }

    @Override
    public List<HIT> getHitResult(String text) {
        List<HIT> result = new ArrayList<HIT>();
        String type = getSingleType(text);
        if(type != null) {
            HIT hit = new HIT();
            hit.start = 0;
            hit.end = text.length();
            hit.target = "?";
            hit.source = text;
            hit.type = type;
            result.add(hit);
        }
        return result;
    }
}