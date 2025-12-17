package com.cec.examine.desensitization.generator;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

/**
 * 通用的枚举类型生成器
 */
public class GeneratorGeneralString extends Generator {

    private final HashMap<Integer, String> probabilityMap = new HashMap<>();
    private final TreeSet<Integer> probabilitySet = new TreeSet<>();
    private Generator generator = null;

    public boolean isCoverEmptyValue() {
        return coverEmptyValue;
    }

    public void setCoverEmptyValue(boolean coverEmptyValue) {
        this.coverEmptyValue = coverEmptyValue;
    }

    public boolean isCoverAllEnum() {
        return coverAllEnum;
    }

    public void setCoverAllEnum(boolean coverAllEnum) {
        this.coverAllEnum = coverAllEnum;
    }

    private boolean coverEmptyValue = true;
    private boolean coverAllEnum = true;
    /**
     * 按照枚举分布进行生成
     * @param result
     */
    public GeneratorGeneralString(Map<String, Double> result, boolean coverEmptyValue, boolean coverAllEnum) {
        int interval = 0;
        for(String word: result.keySet()){
            if("cnt".equals(word)) continue;
            //如果设置为不覆盖空值，并且遇到空值时那么选择跳过
            if(!coverEmptyValue && "emp".equals(word)) continue;
            interval += (int)Math.ceil(result.get(word) * 100);
            probabilityMap.put(interval,word);
            probabilitySet.add(interval);
        }
    }
    /**
     * 如果可枚举，则generator不生效，按照分布生成
     * 按照如果不可枚举，可以指定generator函数生成
     * result中至少包含cnt和emp
     * @param result
     */
    public GeneratorGeneralString(Map<String, Double> result, Generator generator,boolean coverEmptyValue, boolean coverAllEnum) {
        if(result != null && result.size() >= 3) {
            int interval = 0;
            for(String word: result.keySet()){
                if("cnt".equals(word)) continue;
                //如果设置为不覆盖空值，并且遇到空值时那么选择跳过
                if(!coverEmptyValue && "emp".equals(word)) continue;
                interval += (int)Math.ceil(result.get(word) * 100);
                probabilityMap.put(interval,word);
                probabilitySet.add(interval);
            }
        } else {
            this.generator = generator;
            Double pEmp = result!=null? result.get("emp"): null;
            Integer intervalEmp = pEmp == null ? 0: (int)(pEmp * 100);
            if(coverEmptyValue) {
                //如果覆盖空值则写入emp
                probabilityMap.put(intervalEmp, "emp");
                probabilitySet.add(intervalEmp);
            }
            probabilityMap.put(100, "gen");
            probabilitySet.add(100);
        }
    }

    @Override
    public String generateFixed(String seed) {
        Integer interval = generateProbabilityIntervalLable(seed, probabilitySet);
        String n =  probabilityMap.get(interval);
        if("emp".equals(n)) {
            //如果是emp，那么需要产生空值
            return null;
        } else if("gen".equals(n)) {
            //如果是gen，那么需要使用初始化进来的generator生成
            return this.generator.generateFixed(seed);
        } else {
            //如果都不是则直接返回n
            return n;
        }
    }
}
