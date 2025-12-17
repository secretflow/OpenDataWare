package com.cec.examine.template.desensitization;
import com.cec.examine.desensitization.SensitivePattern;
import com.cec.examine.desensitization.generator.GeneralEmptyValueWraper;
import com.cec.examine.desensitization.generator.GeneralStringWraper;
import com.cec.examine.desensitization.generator.Generator;
import com.cec.examine.template.TableCellPojo;
import com.cec.examine.util.jsonparser.JSONParser;
import com.cec.examine.util.jsonparser.model.JsonArray;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.*;

/**
 * 样本生成模板类
 */
public class DesensitizationTemplate {

    private final Map<String, String> maskTagMap = new  HashMap<>();
    private final Map<String, DesensitizationConfig> configMap = new  HashMap<>();

    private final Map<String,SensitivePattern> sensitivePatternMap;
    /**
     * @param fromJsonConfig JSON例子见：DesensitizationTemplateTest
     */
    public DesensitizationTemplate(String fromJsonConfig) {
        JSONParser jsonParser = new JSONParser();
        sensitivePatternMap = new HashMap<>();
        try {
            JsonArray jsonArray = (JsonArray) jsonParser.fromJSON(fromJsonConfig);
            for(int i=0;i<jsonArray.size();i++){
                //脱敏配置
                DesensitizationConfig desensitizationConfig = new DesensitizationConfig(jsonArray.getJsonObject(i));
                //列名
                String colName = desensitizationConfig.getColName();
                configMap.put(colName,desensitizationConfig);
                //方法名
                SensitivePattern sensitivePattern = getDeFunction(desensitizationConfig);
                //将仿真脱敏的算子设置maskTag，因为仿真的随机性依赖于seed，而不在意原始值，如果使用原始值作为seed则会导致无法按照概率分布生成。
                //1. 固定仿真脱敏的需要加入maskTag
                //2. 是通用仿真脱敏的，使用分布生成的也需要加入maskTag，maskTag取值为null即可。
                if("固定仿真脱敏".equals(desensitizationConfig.getMaskType())
                        ||"GeneralEmulation".equals(desensitizationConfig.getMaskMethod())) {
                    maskTagMap.put(colName,String.valueOf(desensitizationConfig.getMaskTag()));
                }
                sensitivePatternMap.put(colName,sensitivePattern);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 通过脱敏配置初始化脱敏算子
     * @param desensitizationConfig 脱敏配置
     * @return 脱敏算子实例
     */
    public SensitivePattern getDeFunction(DesensitizationConfig desensitizationConfig) {
        String maskMethod = desensitizationConfig.getMaskMethod();
        String maskType = desensitizationConfig.getMaskType();
        SensitivePattern sensitivePattern = null;
        //反射类
        try {
            Class<?> clazz = null;
            Object instance = null;
            if ("GeneralMask".equals(maskMethod)) {
                //通用遮盖
                clazz = Class.forName("com.cec.examine.desensitization.mask." + maskMethod);
                Constructor<?> constructor = clazz.getConstructor(int.class,int.class,char.class);
                instance = constructor.newInstance(desensitizationConfig.getMaskMethodDetail().getBeginIndex(), desensitizationConfig.getMaskMethodDetail().getEndAfter(),desensitizationConfig.getMaskMethodDetail().getMaskChar());
                sensitivePattern = (SensitivePattern) instance;
                if(desensitizationConfig.isCoverEmptyValue()) sensitivePattern = new GeneralEmptyValueWraper(desensitizationConfig.getExplorationJson(),sensitivePattern);
            } else if ("GeneralHash".equals(maskMethod)) {
                //通用hash
                clazz = Class.forName("com.cec.examine.desensitization.hash." + maskMethod);
                Constructor<?> constructor = clazz.getConstructor();
                instance = constructor.newInstance();
                sensitivePattern = (SensitivePattern) instance;
                if(desensitizationConfig.isCoverEmptyValue()) sensitivePattern = new GeneralEmptyValueWraper(desensitizationConfig.getExplorationJson(),sensitivePattern);
            } else if ("GeneralEmulation".equals(maskMethod)) {
                //通用仿真
                clazz = Class.forName("com.cec.examine.desensitization.generator." + maskMethod);
                Constructor<?> constructor = clazz.getConstructor(String.class,String.class, boolean.class, boolean.class,boolean.class);
                instance = constructor.newInstance(
                        desensitizationConfig.getColType(),
                        desensitizationConfig.getExplorationJson(),
                        desensitizationConfig.isCoverEmptyValue(),
                        desensitizationConfig.isCoverPeekValue(),
                        desensitizationConfig.isCoverAllEnum()
                );
                sensitivePattern = (SensitivePattern) instance;
            } else {
                //非通用
                if ("固定仿真脱敏".equals(maskType)) {
                    //Generator系列
                    clazz = Class.forName("com.cec.examine.desensitization.generator." + maskMethod);
                    Constructor<?> constructor = clazz.getConstructor();
                    instance = constructor.newInstance();
                    if(AnalysisForTable.isNumeric(desensitizationConfig.getColType())) {
                        sensitivePattern = (SensitivePattern) instance;
                        if(desensitizationConfig.isCoverEmptyValue()) sensitivePattern = new GeneralEmptyValueWraper(desensitizationConfig.getExplorationJson(),sensitivePattern);
                    } else {
                        sensitivePattern = new GeneralStringWraper(new AnalysisResultForEnum(null).getResult(desensitizationConfig.getExplorationJson()), (Generator) instance);
                    }
                } else if("随机仿真脱敏".equals(maskType)) {
                    clazz = Class.forName("com.cec.examine.desensitization.fake.randomfake." + maskMethod);
                    Constructor<?> constructor = clazz.getConstructor();
                    instance = constructor.newInstance();
                    sensitivePattern = (SensitivePattern) instance;
                    if(desensitizationConfig.isCoverEmptyValue()) sensitivePattern = new GeneralEmptyValueWraper(desensitizationConfig.getExplorationJson(),sensitivePattern);
                } else if("遮盖脱敏".equals(maskType)) {
                    clazz = Class.forName("com.cec.examine.desensitization.mask." + maskMethod);
                    Constructor<?> constructor = clazz.getConstructor();
                    instance = constructor.newInstance();
                    sensitivePattern = (SensitivePattern) instance;
                    if(desensitizationConfig.isCoverEmptyValue()) sensitivePattern = new GeneralEmptyValueWraper(desensitizationConfig.getExplorationJson(),sensitivePattern);
                } else if("哈希脱敏".equals(maskType)) {
                    clazz = Class.forName("com.cec.examine.desensitization.hash." + maskMethod);
                    Constructor<?> constructor = clazz.getConstructor();
                    instance = constructor.newInstance();
                    sensitivePattern = (SensitivePattern) instance;
                    if(desensitizationConfig.isCoverEmptyValue()) sensitivePattern = new GeneralEmptyValueWraper(desensitizationConfig.getExplorationJson(),sensitivePattern);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return sensitivePattern;
    }

    /**
     * TableCellPojo中的comment会提示是否是异常值
     * @param data 样本数据
     * @return 脱敏结果
     */
    public List<Map<String, TableCellPojo>> de(List<Map<String, TableCellPojo>> data) {
        List<Map<String, TableCellPojo>> result = new ArrayList<>();
        int lineNo = 0;
        for (Map<String, TableCellPojo> map : data) {
            Map<String, TableCellPojo> demap = new HashMap<>();
            for(String colName : map.keySet()) {
                SensitivePattern sensitivePattern = sensitivePatternMap.get(colName);
                TableCellPojo tableCellPojo = map.get(colName);
                TableCellPojo deTableCellPojo = desensitization(lineNo++ , tableCellPojo, sensitivePattern);
                demap.put(colName, deTableCellPojo);
            }
            result.add(demap);
        }
        return result;
    }

    /**
     * 根据配置脱敏返回
     * @param tableCellPojo 原始值
     * @param sensitivePattern 脱敏算子
     */
    public TableCellPojo desensitization(int lineNo, TableCellPojo tableCellPojo, SensitivePattern sensitivePattern) {
        TableCellPojo r = new  TableCellPojo();
        //先判断一下这个列是否存在maskTag
        String source = maskTagMap.containsKey(tableCellPojo.getEnglishColumnName()) ?
                maskTagMap.get(tableCellPojo.getEnglishColumnName()) + lineNo :
                String.valueOf(tableCellPojo.getValue());
        String target = sensitivePattern.desensitive(source, false);
        //异常值判断
        DesensitizationConfig desensitizationConfig = configMap.get(tableCellPojo.getEnglishColumnName());
        if(desensitizationConfig.isCoverEmptyValue()
                && (target == null || target.isEmpty() || "NULL".equalsIgnoreCase(target))) {
            //这三种情况下为异常值
            r.setComments("outlier");
        } else if(AnalysisForTable.isNumeric(desensitizationConfig.getColType())) {
            //极值判断
            AnalysisResultForNumeric.NumericResult<Double> numericResult = new AnalysisResultForNumeric.NumericResult<Double>(desensitizationConfig.getExplorationJson());
            if(desensitizationConfig.isCoverPeekValue() &&
                    (Double.parseDouble(String.valueOf(target)) <= Double.parseDouble(String.valueOf(numericResult.getP1())) ||
                    Double.parseDouble(String.valueOf(target)) >= Double.parseDouble(String.valueOf(numericResult.getP99())))) {
                r.setComments("peak");
            }
        }
        r.setValue(target);
        r.setEnglishColumnName(tableCellPojo.getEnglishColumnName());
        r.setChineseColumnName(tableCellPojo.getChineseColumnName());
        r.setDataType(tableCellPojo.getDataType());
        return r;
    }
}
