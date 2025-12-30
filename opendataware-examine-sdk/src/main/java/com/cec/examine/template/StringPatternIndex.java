package com.cec.examine.template;

import com.cec.examine.template.recognition.RecognitionCofigLoader;
import com.cec.examine.util.FileUtilityUtil;
import com.cec.examine.util.jsonparser.JSONParser;

import java.io.InputStream;
import java.util.*;

/**
 * 字符串特征提取
 */
public class StringPatternIndex {

    private static class IndexFeature {
        public Set<Integer> lengths;
        public String binCode;
    }

    private HashMap<String, List<IndexFeature>> codeToIndexFeatures = new HashMap<>();
    public StringPatternIndex() {
        InputStream inputStream = RecognitionCofigLoader.class.getResourceAsStream("/template/recognition_pattern_index");
        FileUtilityUtil fu = FileUtilityUtil.getFileReader(inputStream);
        while (true) {
            String [] lines = fu.getCSVLineCells(",");
            if(lines == null) break;
            String code = lines[0];
            String [] strLens = lines[2].split("\\|");
            String binCode = lines[3];
            Set<Integer> lengths = new HashSet<Integer>();
            for(String len:strLens) {
                lengths.add(Integer.parseInt(len));
            }
            IndexFeature indexFeature = new IndexFeature();
            indexFeature.binCode = binCode;
            indexFeature.lengths = lengths;
            List<IndexFeature> indexFeatures = codeToIndexFeatures.get(code);
            if(indexFeatures == null) indexFeatures = new ArrayList<>();
            indexFeatures.add(indexFeature);
            codeToIndexFeatures.put(code, indexFeatures);
        }
    }

    public boolean filter(String code, String text) {
        if(code == null || text == null) return false;
        if("PE000033".equals(code)) {
            if(text.indexOf("@") > 0) return true;
            else return false;
        }//专门给邮箱打补丁
        List<IndexFeature> indexFeatures = codeToIndexFeatures.get(code);
        if(indexFeatures == null) return true;//这些属于索引为覆盖的情况，不做判断直接返回true

        for(IndexFeature indexFeature: indexFeatures) {
            Set<Integer> lengths = indexFeature.lengths;
            String binCode = indexFeature.binCode;
            int length = text.length();
            if (lengths.contains(length)
                    && StringFeatures.getStringFeatures(text).getBinCode().equals(binCode)) {
                //这个条件必须按照这种写法，有助于性能提升，与计算可以短路掉慢计算。
                return true;
            }
        }
        //如果一个也不满足，跳出了循环，直接返回false
        return false;
    }
}
