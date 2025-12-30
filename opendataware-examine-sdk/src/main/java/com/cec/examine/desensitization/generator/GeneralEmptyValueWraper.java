package com.cec.examine.desensitization.generator;
import com.cec.examine.desensitization.SensitivePattern;
import com.cec.examine.util.jsonparser.JSONParser;
import com.cec.examine.util.jsonparser.model.JsonObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.TreeSet;

import static com.cec.examine.desensitization.generator.Generator.generateProbabilityIntervalLable;

/**
 * 通用的空值生成器，可以包装一个SensitivePattern子类，然后按照一定的概率生成空值
 */
public class GeneralEmptyValueWraper extends SensitivePattern {

    private final TreeSet<Integer> probabilitySet = new TreeSet<>();
    private final HashMap<Integer, String> probabilityMap = new HashMap<Integer, String>();
    private final SensitivePattern sensitivePattern;

    public GeneralEmptyValueWraper(String exploreJson, SensitivePattern sensitivePattern) {
        JSONParser jsonParser = new JSONParser();
        Double empValue = null;
        try {
            JsonObject jsonObject = (JsonObject) jsonParser.fromJSON(exploreJson);
            empValue = Double.parseDouble(String.valueOf(jsonObject.get("emp")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Integer empPercentValue = empValue == null ? 0: (int)(empValue * 100);
        this.probabilityMap.put(empPercentValue, "emp");
        this.probabilityMap.put(100, "gen");
        this.probabilitySet.add(empPercentValue);
        this.probabilitySet.add(100);
        this.sensitivePattern = sensitivePattern;
    }

    public GeneralEmptyValueWraper(Integer empPercentValue, SensitivePattern sensitivePattern) {
        this.probabilityMap.put(empPercentValue, "emp");
        this.probabilityMap.put(100, "gen");
        this.probabilitySet.add(empPercentValue);
        this.probabilitySet.add(100);
        this.sensitivePattern = sensitivePattern;
    }

    @Override
    public String desensitive(String text, boolean longText) {
        Integer interval = generateProbabilityIntervalLable(text, probabilitySet);
        String n =  probabilityMap.get(interval);
        if("emp".equals(n)){
            return null;
        } else {
            return sensitivePattern.desensitive(text, longText);
        }
    }
}
