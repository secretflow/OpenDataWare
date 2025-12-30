package com.cec.examine.template.desensitization;
import com.cec.examine.util.jsonparser.JSONParser;
import com.cec.examine.util.jsonparser.model.JsonObject;

import java.io.IOException;
import java.util.*;

/**
 * 统计是否可枚举，但如果超过100个key则认为不可枚举
 */
public class AnalysisResultForEnum extends AnalysisResult{
    private final Map<String, Double> wordCount = new HashMap<>();
    public AnalysisResultForEnum(List<String> dataList) {
        if(dataList != null) {
            boolean countable = true;
            this.totalSize = dataList.size();
            for (String word : dataList) {
                if(word == null) {
                    this.emptyValuePercent++;
                } else if(countable) {
                    Double count = wordCount.get(word);
                    if (count == null) {
                        wordCount.put(word, 1D);
                    } else {
                        wordCount.put(word, ++count);
                    }
                    if (wordCount.size() > 100) {
                        //超过100认为不可枚举
                        wordCount.clear();
                        countable = false;
                    }
                }
            }
            this.emptyValuePercent = this.emptyValuePercent / totalSize;
        }
    }

    public boolean isEnumerable() {
        return !wordCount.isEmpty();
    }

    public Double getPercent(String word) {
        if(totalSize > 0) {
            Double count = wordCount.get(word);
            return count == null ? 0 : count/totalSize;
        } else return 0D;
    }

    public Integer getCount(String word) {
        Double count = wordCount.get(word);
        return (int) (count == null ? 0 : count);
    }

    /**
     * 生成完整的统计摘要报告
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"cnt\"").append(":").append(this.totalSize).append(",");
        for(String word : wordCount.keySet()){
            sb.append("\"").append(word).append("\"").append(":").append(wordCount.get(word)/totalSize).append(",");
        }
        sb.append("\"emp\"").append(":").append(this.emptyValuePercent).append("}");
        return sb.toString();
    }

    /**
     * 获取统计结果
     * @return 返回统计结果
     */
    public Map<String, Double> getResult() {
        Map<String, Double> map = new HashMap<>();
        map.put("cnt", (double)this.totalSize);
        map.put("emp", this.emptyValuePercent);
        for(String word : wordCount.keySet()){
            map.put(word, wordCount.get(word)/totalSize);
        }
        return map;
    }

    /**
     * 通过JSON获取结果
     * @param fromJson
     * @return
     */
    public  Map<String, Double> getResult(String fromJson) {
        Map<String, Double> map = new HashMap<>();
        JSONParser jsonParser = new JSONParser();
        try {
            JsonObject explorationResult = (JsonObject)jsonParser.fromJSON(fromJson);
            for(String key: explorationResult.keySet()) {
                map.put(key, Double.parseDouble(String.valueOf(explorationResult.get(key))));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return map;
    }
}
