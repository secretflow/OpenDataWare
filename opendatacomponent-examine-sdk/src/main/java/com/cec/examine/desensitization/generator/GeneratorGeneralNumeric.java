package com.cec.examine.desensitization.generator;

import com.cec.examine.template.desensitization.AnalysisResultForNumeric;
import java.util.*;

/**
 * 通用的数值分布生成器
 */
public class GeneratorGeneralNumeric<T extends Number> extends Generator {

    private Class<T> type;
    private final T max;
    private final T min;
    private final Map<Integer, List<T>> fromToMap = new HashMap<>();
    private final TreeSet<Integer> fromToSet = new TreeSet<>();
    private final TreeSet<Integer> empAndNormal;

    public boolean isCoverPeekValue() {
        return coverPeekValue;
    }

    public void setCoverPeekValue(boolean coverPeekValue) {
        this.coverPeekValue = coverPeekValue;
    }

    public boolean isCoverEmptyValue() {
        return coverEmptyValue;
    }

    public void setCoverEmptyValue(boolean coverEmptyValue) {
        this.coverEmptyValue = coverEmptyValue;
    }

    private boolean coverEmptyValue = true;
    private boolean coverPeekValue = true;

    public GeneratorGeneralNumeric(AnalysisResultForNumeric.NumericResult<T> numericResult, Class<T> type) {
        this.type = type;
        //最大最小赋值
        max = numericResult.getMax();
        min = numericResult.getMin();
        //空值与正常值分割区间
        empAndNormal = new TreeSet<>();
        empAndNormal.add((int)Math.ceil(numericResult.getEmp() * 100));
        empAndNormal.add(100);
        //正常值数值分布分割区间
        List<T> p1 = new ArrayList<>(Arrays.asList(numericResult.getMin(), numericResult.getP1()));
        List<T> p25 = new ArrayList<>(Arrays.asList(numericResult.getP1(), numericResult.getP25()));
        List<T> p50 = new ArrayList<>(Arrays.asList(numericResult.getP25(), numericResult.getP50()));
        List<T> p75 = new ArrayList<>(Arrays.asList(numericResult.getP50(), numericResult.getP75()));
        List<T> p99 = new ArrayList<>(Arrays.asList(numericResult.getP75(), numericResult.getP99()));
        List<T> p100 = new ArrayList<>(Arrays.asList(numericResult.getP99(), numericResult.getMax()));
        fromToMap.put(1, p1);
        fromToMap.put(25, p25);
        fromToMap.put(50, p50);
        fromToMap.put(75, p75);
        fromToMap.put(99, p99);
        fromToMap.put(100, p100);
        fromToSet.add(1);
        fromToSet.add(25);
        fromToSet.add(50);
        fromToSet.add(75);
        fromToSet.add(99);
        fromToSet.add(100);
    }

    @Override
    public String generateFixed(String seed) {
        counter++;
        //优先保证出现最大最小值
        if(counter == 1 && coverPeekValue) return String.valueOf(min);
        else if(counter == 2 && coverPeekValue) return String.valueOf(max);
        else {
            //保证出现异常值
            int empAndNorInterval = generateProbabilityIntervalLable(seed, empAndNormal);
            if(empAndNorInterval < 100 && coverEmptyValue) return null;
            else {
                //正常值切割区间
                int fromToInterval = generateProbabilityIntervalLable(seed, fromToSet);
                List<T> fromTo =  fromToMap.get(fromToInterval);
                double r = generateRandomDouble(fromTo.get(0), fromTo.get(1));
                if(Integer.class.equals(type)) {
                    return String.valueOf((int)r);
                } else if (Long.class.equals(type)) {
                    return String.valueOf((long)r);
                } else if(Float.class.equals(type)) {
                    return String.valueOf((float)r);
                } else if(Short.class.equals(type)) {
                    return String.valueOf((short)r);
                } else if(Byte.class.equals(type)) {
                    return String.valueOf((byte)r);
                } else {
                    return String.valueOf(r);
                }

            }
        }
    }

    /**
     * 生成范围内的double数值
     * @param min
     * @param max
     * @return
     */
    public double generateRandomDouble(T min, T max) {
        double minDouble = Double.parseDouble(String.valueOf(min));
        double maxDouble = Double.parseDouble(String.valueOf(max));
        if (minDouble > maxDouble) {
            throw new IllegalArgumentException("min must be less than or equal to max");
        }
        Random random = new Random();
        double range = maxDouble - minDouble + 1;
        return minDouble + random.nextDouble() * range;
    }
}
