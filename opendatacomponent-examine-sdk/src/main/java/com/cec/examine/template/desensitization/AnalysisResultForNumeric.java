package com.cec.examine.template.desensitization;
import com.cec.examine.util.jsonparser.JSONParser;
import com.cec.examine.util.jsonparser.model.JsonObject;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 分析一个小表
 */
public class AnalysisResultForNumeric<T extends Number> extends AnalysisResult{
    private final List<T> sortedList;
    private NumericResult<T> result = null;


    public Class<T> getType() {
        return type;
    }

    private Class<T> type;

    /**
     * 强制转换
     * @param dataListObj
     * @param force
     */
    public AnalysisResultForNumeric(List<Object> dataListObj, Class<T> type, boolean force) {
        this.type = type;
        this.totalSize = dataListObj.size();
        List<T> dataList = new ArrayList<>();
        for(Object dataObj : dataListObj){
            if(Objects.isNull(dataObj)){
                emptyValuePercent++;
                continue;
            }
            dataList.add((T)dataObj);
        }
        Objects.requireNonNull(dataList, "数据列表不能为null");
        this.sortedList = dataList.stream()
                .sorted()
                .collect(Collectors.toList());

        emptyValuePercent = emptyValuePercent/dataListObj.size();
    }

    public AnalysisResultForNumeric(List<T> dataListObj, Class<T> type) {
        this.type = type;
        this.totalSize = dataListObj.size();
        List<T> dataList = new ArrayList<>();
        for(T dataObj : dataListObj){
            if(Objects.isNull(dataObj)){
                emptyValuePercent++;
                continue;
            }
            dataList.add(dataObj);
        }
        Objects.requireNonNull(dataList, "数据列表不能为null");
        this.sortedList = dataList.stream()
                .sorted()
                .collect(Collectors.toList());
        emptyValuePercent = emptyValuePercent/dataListObj.size();
    }

    /**
     * 获取最大值
     */
    public T getMax() {
        return sortedList.get(sortedList.size()-1);
    }

    /**
     * 获取最小值
     */
    public T getMin() {
        return sortedList.get(0);
    }

    /**
     * 获取平均值
     */
    public double getAverage() {
        return sortedList.stream().mapToDouble(Number::doubleValue).average().getAsDouble();
    }

    /**
     * 计算指定分位数
     * 采用线性插值法，适用于P1, P25, P50, P75, P99等
     *
     * @param percentile 分位点，取值范围 [0, 100]，例如50表示中位数P50
     */
    public T getPercentile(double percentile) {
        checkDataNotEmpty();
        if (percentile < 0 || percentile > 100) {
            throw new IllegalArgumentException("分位点必须在0到100之间");
        }

        // 计算分位点位置
        double position = (percentile / 100.0) * (sortedList.size() - 1);
        int index = (int) Math.floor(position);
        double fraction = position - index;

        // 处理边界情况
        if (index == sortedList.size() - 1) {
            return sortedList.get(sortedList.size() - 1);
        }

        // 线性插值
        T lowerValue = sortedList.get(index);
        return lowerValue;
    }

    /**
     * 便捷方法：获取P1分位数
     */
    public T getP1() {
        return getPercentile(1);
    }

    /**
     * 便捷方法：获取P25分位数
     */
    public T getP25() {
        return getPercentile(25);
    }

    /**
     * 便捷方法：获取P50分位数（中位数）
     */
    public T getP50() {
        return getPercentile(50);
    }

    /**
     * 便捷方法：获取P75分位数
     */
    public T getP75() {
        return getPercentile(75);
    }

    /**
     * 便捷方法：获取P99分位数
     */
    public T getP99() {
        return getPercentile(99);
    }

    /**
     * 生成完整的统计摘要报告
     */
    public String toString() {
       StringBuilder sb = new StringBuilder();
        sb.append("{").append("\"cnt\":").append(getCount()).append(",")
                .append("\"min\":").append(getMin()).append(",")
                .append("\"max\":").append(getMax()).append(",")
                .append("\"avg\":").append(getAverage()).append(",")
                .append("\"p1\":").append(getP1()).append(",")
                .append("\"p25\":").append(getP25()).append(",")
                .append("\"p50\":").append(getP50()).append(",")
                .append("\"p75\":").append(getP75()).append(",")
                .append("\"p99\":").append(getP99()).append(",")
                .append("\"emp\":").append(getEmptyValuePercent()).append("}");
        return sb.toString();
    }

    public NumericResult<T> getResult() {
        if(result == null){
            result = new NumericResult<T>();
            result.setCnt(getCount());
            result.setEmp(getEmptyValuePercent());
            result.setAvg(getAverage());
            result.setP1(getP1());
            result.setP25(getP25());
            result.setP50(getP50());
            result.setP75(getP75());
            result.setP99(getP99());
        }
        return result;
    }

    public static class NumericResult<T extends Number> {

        public NumericResult() {}

        /**
         * 通过JSON String初始化
         * @param fromJson
         */
        public NumericResult(String fromJson) {
            JSONParser jsonParser = new JSONParser();
            try {
                JsonObject explorationResult = (JsonObject)jsonParser.fromJSON(fromJson);
                this.cnt = Long.parseLong(String.valueOf(explorationResult.get("cnt")));
                this.emp = (double)explorationResult.get("emp");
                this.avg = explorationResult.get("avg") == null ? 0D: (double) explorationResult.get("avg");
                this.p1 = (T)explorationResult.get("p1");
                this.p25 = (T)explorationResult.get("p25");
                this.p50 = (T)explorationResult.get("p50");
                this.p75 = (T)explorationResult.get("p75");
                this.p99 = (T)explorationResult.get("p99");
                this.min = (T)explorationResult.get("min");
                this.max = (T)explorationResult.get("max");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        private Long cnt;
        private Double emp;

        public Long getCnt() {
            return cnt;
        }

        public void setCnt(Long cnt) {
            this.cnt = cnt;
        }

        public Double getEmp() {
            return emp;
        }

        public void setEmp(Double emp) {
            this.emp = emp;
        }

        public T getMin() {
            return min;
        }

        public void setMin(T min) {
            this.min = min;
        }

        public T getMax() {
            return max;
        }

        public void setMax(T max) {
            this.max = max;
        }

        public double getAvg() {
            return avg;
        }

        public void setAvg(double avg) {
            this.avg = avg;
        }

        public T getP1() {
            return p1;
        }

        public void setP1(T p1) {
            this.p1 = p1;
        }

        public T getP25() {
            return p25;
        }

        public void setP25(T p25) {
            this.p25 = p25;
        }

        public T getP50() {
            return p50;
        }

        public void setP50(T p50) {
            this.p50 = p50;
        }

        public T getP75() {
            return p75;
        }

        public void setP75(T p75) {
            this.p75 = p75;
        }

        public T getP99() {
            return p99;
        }

        public void setP99(T p99) {
            this.p99 = p99;
        }

        private T min;
        private T max;
        private double avg;
        private T p1;
        private T p25;
        private T p50;
        private T p75;
        private T p99;
    }

    private void checkDataNotEmpty() {
        if (sortedList.isEmpty()) {
            throw new IllegalStateException("数据列表为空，无法计算统计量");
        }
    }
}
