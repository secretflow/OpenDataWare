package com.cec.examine.reversibility;
import com.cec.examine.template.TableCellPojo;
import com.cec.examine.util.StringUtil;

import java.util.*;

/**
 * 离群点审核
 */
public class Outlier extends Reversibility {

    private LinkedHashMap<String, List<TableCellPojo>> datas = new LinkedHashMap<String, List<TableCellPojo>>();

    private List<TreeMap<String, Object>> mainIDlist = new ArrayList<>();//多主键结构，所以是个treemap，列->值

    private double alpha = 0.05; // 5% 的置信水平

    private int maxSample = 100;// 异常样本最大数量
    //主体字段

    /**
     * 添加元件结果的每一行
     */
    @Override
    public void addLineOfResult(Map<String, TableCellPojo> lineData) {
        TreeMap<String,Object> mainIDCell = new TreeMap<>();//一行中的主键值，多列
        int currentSize = 0;
        for(String colum: lineData.keySet()) {
            //若存在主体字段，则跳过离群点审核
            if(this.getMainIdColumns() != null && this.getMainIdColumnSet().contains(colum)) {
                mainIDCell.put(colum, lineData.get(colum).getValue());
                continue;
            }
            List<TableCellPojo> list = datas.get(colum);
            if(list == null) list = new ArrayList<TableCellPojo>();
            TableCellPojo val = lineData.get(colum);
            list.add(val);
            datas.put(colum, list);
            currentSize = list.size();//记录一下当前数据的记录数
        }
        //如果没有设置主键则填充一个默认的行号
        if(mainIDCell.keySet().size() == 0) {
            mainIDCell.put("defaultLineNumber",  currentSize - 1);
        }
        mainIDlist.add(mainIDCell);//最后将多列主键添加到集合中
    }

    /**
     * 对象转化double
     * @param val
     * @return
     */
    private double getDouble(Object val) {
        Double valDouble = 0.0D;
        if(val instanceof Integer || val instanceof Float || val instanceof Double) {
            valDouble = Double.valueOf(String.valueOf(val));
        } else if( val instanceof String) {
            //如果string类型发现可以转为数字，则转为数字
            if(((String) val).length() < 24 && StringUtil.isNumerical((String)val)) {
                //如果是纯数字
                valDouble = Double.valueOf((String) val);
            } else if(StringUtil.hasNumerical((String)val)) {
                //如果仅是包含数字,那么去掉非数字截取前24位
                String s = ((String) val).replaceAll("[^0-9]","");
                if(s.length() < 24)
                    valDouble = Double.valueOf(s);
                else
                    valDouble = Double.valueOf(s.substring(0,23));
            }
            else {
                valDouble = (double) val.hashCode();
            }
        }
        return valDouble;
    }

    @Override
    public void addLineOfResource(Map<String, TableCellPojo> lineData) {
        //不需要加载资源表，空置
    }

    public List<OutlierResult> check() {
        List<OutlierResult> results = new ArrayList<OutlierResult>();
        for(String colunm: this.datas.keySet()) {
            List<TableCellPojo> originData = datas.get(colunm);
            double [] data = toDoubleArray(originData);
            double [] interval = calculateBidirectionalInterval(data, alpha);
            //System.out.println("下边界置信区间: " + interval[0]);
            //System.out.println("上边界置信区间: " + interval[1]);
            for (int i = 0; i < data.length && results.size() <= maxSample ; i++ ) {
                //i代表行号
                if(data[i] < interval[0] || data[i] > interval[1]) {
                    OutlierResult outlierResult = new OutlierResult();
                    outlierResult.setValue(originData.get(i));
                    outlierResult.setCloumName(colunm);
                    outlierResult.setMainIdCloumName(this.getMainIdColumns());
                    //找到对应的队列主键值添加到result
                    outlierResult.setMainIdValue(mainIDlist.get(i));
                    //System.out.println(data[i] + ","+ i + "," + originData.get(i) + "," + colunm);
                    results.add(outlierResult);
                }
            }
        }
        return results;
    }

    private double [] toDoubleArray(List<TableCellPojo> data) {
        double [] doubleArray = new double[data.size()];
        for (int i = 0; i < data.size(); i++ ) {
            doubleArray[i] = getDouble(data.get(i).getValue());
        }
        return doubleArray;
    }

    // 计算双侧置信区间的方法
    public static double[] calculateBidirectionalInterval(double[] data, double alpha) {
        int n = data.length;
        double[] sortedData = data.clone();
        Arrays.sort(sortedData);

        // 计算Q1和Q3四分位数
        double q1 = calculateQuantile(sortedData, 0.25, n);
        double q3 = calculateQuantile(sortedData, 0.75, n);
        double iqr = q3 - q1; // 四分位数间距
        double h = 1.5 * iqr; // 由四分位数间距计算的宽度

        // 计算下边界
        double lowerBound = Math.max(q1 - 1.5 * iqr, sortedData[0]);
        double upperBound = Math.min(q3 + 1.5 * iqr, sortedData[n - 1]);
        // 计算下边界的置信区间
        //double lowerConfidenceLevel = lowerBound - h / Math.sqrt(n) * calculateZValue(1 - alpha / 2);
        //double upperConfidenceLevel = upperBound + h / Math.sqrt(n) * calculateZValue(1 - alpha / 2);
        double lowerConfidenceLevel = lowerBound;
        double upperConfidenceLevel = upperBound;
        return new double[]{lowerConfidenceLevel, upperConfidenceLevel};
    }

    // 计算Z分数
    private static double calculateZValue(double p) {
        return NormalDistribution.inverseCumulativeProbability(1 - p);
    }

    // 计算四分位数
    private static double calculateQuantile(double[] sortedData, double p, int n) {
        if (n == 0) {
            return 0.0; // 处理空数组的情况
        }
        
        int index = (int) Math.floor(p * (n + 1));
        if (index <= 0) {
            return sortedData[0];
        } else if (index >= n) {
            return sortedData[n - 1];
        } else {
            double lower = sortedData[index - 1];
            double upper = sortedData[index];
            return lower + (upper - lower) * (p * (n + 1) - index);
        }
    }
}
