package com.cec.examine.template.quality;
import com.cec.examine.qi.*;
import com.cec.examine.template.ConfigFactory;
import com.cec.examine.template.TableCellPojo;
import com.cec.examine.template.recognition.RecognitionPair;

import java.util.*;

public class QiTemplate {
    private List<QiConfig> qiConfigs;//质检配置

    private Map<String, List<QiRuleBindingResult>> bindQiConfig;//在列上绑定多个质检规则

    /**
     * 将绑定到列的规则添加到全局map
     * @param column
     * @param qiRuleBindingResult
     */
    private void addRule(String column, QiRuleBindingResult qiRuleBindingResult) {
        List<QiRuleBindingResult> qiRuleBindingResults = bindQiConfig.get(column);
        if(qiRuleBindingResults == null) qiRuleBindingResults = new ArrayList<>();
        qiRuleBindingResults.add(qiRuleBindingResult);
        bindQiConfig.put(column, qiRuleBindingResults);
    }


    /**
     * 累加计算规则对应的列结果
     * @param qiOperator 质检算子对象
     * @param qiResultMap 质检结果集合
     * @param qiColumnResult 质检编码对应的单一列的质检结果
     */
    private void addQiColumnResult(QiOperator qiOperator, Map<String, QiResult> qiResultMap, QiColumnResult qiColumnResult, Integer sizeOfData) {
        QiResult qiResult = qiResultMap.get(qiOperator.getQiCode());
        if(qiResult == null) qiResult = new QiResult();
        //合并逻辑
        qiResult.addQiColumnResults(qiColumnResult);
        qiResult.addProblemNum(qiColumnResult.getProblemNum());
        qiResult.addQiDataTotal(qiColumnResult.getQiDataNum());
        qiResult.setTableDataCount(sizeOfData);
        qiResult.maxQiDataNum(qiColumnResult.getQiDataNum());
        qiResult.setQiCode(qiOperator.getQiCode());
        qiResult.setQiName(qiOperator.getQiName());
        qiResult.setQiType(qiOperator.getQiType());
        qiResultMap.put(qiOperator.getQiCode(), qiResult);
    }


    public QiTemplate() {
        ConfigFactory<QiConfigLoader> qiConfigLoaderConfigFactory = new ConfigFactory<>();
        qiConfigs = qiConfigLoaderConfigFactory.loadConfigs(ConfigFactory.RESOURCE_FILE, QiConfigLoader.class);
    }

    /**
     * 获取质检报告
     * @param data listMap 表数据
     * @param mainIdColumn 主键ID
     * @return 规则编码->质检结果
     */
    public List<QiResult> qi(String [] mainIdColumn, List<Map<String, TableCellPojo>> data) {
        Map<String, QiResult> qiResultMap = new LinkedHashMap<>();
        Map<String, List<QiOperator>> qiOperatorsForColumn = new HashMap<>();//列的所有算子集合
        Set<String> columns = new HashSet<>();//记录所有的列名称
        //加载表级别的算子
        List<QiOperator> operatorsTable = new ArrayList<>();
        List<QiRuleBindingResult> qiRuleBindingResultsForTable = bindQiConfig.get(QiRuleBindingResult.TABLE_LEVEL);
        for (QiRuleBindingResult qiRuleBindingResult : qiRuleBindingResultsForTable) {
            QiOperator qiOperator = getQiOperatorFromBindRule(qiRuleBindingResult);
            operatorsTable.add(qiOperator);
        }
        //获取面向所有列的质检规则
        List<QiOperator> operatorsAC = new ArrayList<>();//面向所有列的算子加载进来
        List<QiRuleBindingResult> qiRuleBindingResultsForAllColumns = bindQiConfig.get(QiRuleBindingResult.ALL_COLUMN);
        for (QiRuleBindingResult qiRuleBindingResult : qiRuleBindingResultsForAllColumns) {
            QiOperator qiOperator = getQiOperatorFromBindRule(qiRuleBindingResult);
            operatorsAC.add(qiOperator);
        }
        //给算子加载数据
        for (int i = 0; data != null && i < data.size(); i++) {
            //遍历表的行数据
            Map<String,TableCellPojo> map = data.get(i);//获取列信息

            for(String column: map.keySet()) {
                columns.add(column);//记录列名
                List<QiOperator> operators = qiOperatorsForColumn.get(column);//该列所有的面向列的算子
                //开始给每一个列加载质检算子
                if(operators == null) {
                    //说明这个列第一次初始化算子。则需要调用直接配置来初始化算子
                    operators = new ArrayList<>();//初始化算子列表对象
                    List<QiRuleBindingResult> qiRuleBindingResults = bindQiConfig.get(column);//筛选该列绑定的所有质检规则
                    if(qiRuleBindingResults != null){
                        for (QiRuleBindingResult qiRuleBindingResult : qiRuleBindingResults) {
                            //遍历该列绑定的所有的质检规则，生成对应的算子
                            QiOperator qiOperator = getQiOperatorFromBindRule(qiRuleBindingResult);
                            operators.add(qiOperator);
                        }
                    }
                    qiOperatorsForColumn.put(column,operators);
                    //添加面向所有列的算子
                    operators.addAll(operatorsAC);
                }
                //加载数据逻辑
                for(QiOperator qiOperator: operators) {
                    qiOperator.put(column, map);
                }
            }
            //表级别的算子加载行数据
            for(QiOperator qiOperator: operatorsTable) {
                qiOperator.put(map);
            }
            //触发所有算子结果
            for(String column: columns) {
                //执行质检算子结果
                List<QiOperator> qiOperators = qiOperatorsForColumn.get(column);
                for(QiOperator qiOperator: qiOperators) {
                    qiOperator.execute();
                    QiColumnResult qiColumnResult = new QiColumnResult();
                    qiColumnResult.setColChName(column);
                    qiColumnResult.setColEnName(column);
                    qiColumnResult.setProblemNum(qiOperator.getCountOfProblems());
                    qiColumnResult.setQiDataNum(qiOperator.getCountOfCells());
                    qiColumnResult.setProblemRate(String.valueOf((float) qiOperator.getCountOfProblems() * 100 / (float) qiOperator.getCountOfCells()) + "%");
                    addQiColumnResult(qiOperator, qiResultMap, qiColumnResult, data.size());
                }
            }
            //触发表级别的算子
            for(QiOperator qiOperator: operatorsTable) {
                qiOperator.execute();
                QiResult qiResult = qiResultMap.get(qiOperator.getQiCode());
                if(qiResult == null) {
                    qiResult = new QiResult();
                }
                //合并逻辑
                qiResult.setTableLevel(true);//设置为表级别的结果
                qiResult.setProblemNum(qiOperator.getCountOfProblems());
                qiResult.addQiDataTotal(qiOperator.getCountOfCells());
                qiResult.setTableDataCount(qiOperator.getCountOfCells());
                qiResult.maxQiDataNum(qiOperator.getCountOfCells());
                qiResult.setQiName(qiOperator.getQiName());
                qiResult.setQiType(qiOperator.getQiType());
                qiResult.setQiCode(qiOperator.getQiCode());
                qiResultMap.put(qiOperator.getQiCode(), qiResult);
            }
        }
        //将质检结果的以qiCode为主键的Map结构转化为list
        List<QiResult> qiResults = new ArrayList<>();
        for(String qiCode: qiResultMap.keySet()) {
            QiResult qiResult = qiResultMap.get(qiCode);
            //这个是为了将Map类型的列级别结果转化为list形式，方便应用调用使用
            qiResult.calculateQiColumnResultDetailDTO();
            qiResult.calculateQiTableResultDetailDTO();
            qiResults.add(qiResult);
        }
        return qiResults;
    }


    /**
     * 通过绑定的质检规则产生特定的算子，规则与算子的映射函数
     * @param qiRuleBindingResult
     * @return
     */
    private QiOperator getQiOperatorFromBindRule(QiRuleBindingResult qiRuleBindingResult) {
        QiOperator qiOperator = null;
        String qiType = qiRuleBindingResult.getQiConfig().getQiType();
        String qiCode = qiRuleBindingResult.getQiConfig().getQiCode();
        String qiName = qiRuleBindingResult.getQiConfig().getQiName();

        if("值域有效性".equals(qiType)) {
            Set<String> words = qiRuleBindingResult.getQiConfig().getValues();
            qiOperator = new QiOperatorEnumValue(new ArrayList<>(words));
        } else if ("值域准确性".equals(qiType)) {
            qiOperator = new QiOperatorValueRight(QiRuleBindingResult.getBasicLabelNameToColumnName());
        } else if ("格式规范性".equals(qiType)) {
            qiOperator = new QiOperatorFormatCheck(qiRuleBindingResult.getQiConfig().getBasicLableName(), null);
        } else if ("空值检查".equals(qiType)) {
            qiOperator = new QiOperatorBlankValue();
        } else if ("主键唯一性".equals(qiType)) {
            qiOperator = new QiOperatorUnique();
        } else if ("非法格式".equals(qiType)) {
            qiOperator = new QiOperatorDataType();
        } else if ("数据唯一性".equals(qiType)) {
            //表级别
            qiOperator = new QiOperatorUnique();
        } else if ("属性完整性".equals(qiType)) {
            //表级别
            qiOperator = new QiOperatorAttributesIntegrity();
        } else if ("记录完整性".equals(qiType)) {
            //表级别
            qiOperator = new QiOperatorRecordsIntegrity();
        } else if ("更新时效性".equals(qiType)) {
            //表级别
            qiOperator = new QiOperatorUpdateTime();
        }
        //如果产生了质检算子对象，那么就需要设置一下质检编码
        if(qiOperator != null) {
            qiOperator.setQiCode(qiCode);
            qiOperator.setQiName(qiName);
            qiOperator.setQiType(qiType);
        }
        return qiOperator;
    }

    /**
     * 获取质检落标结果
     * @param recognitionResult 安全识别信息项
     * @return
     */
    public Map<String, List<QiRuleBindingResult>> bind(String [] mainIdColumns, List<RecognitionPair> recognitionResult) {
        bindQiConfig = new LinkedHashMap<>();
        Map<String, String> recognitionResultMap = new LinkedHashMap<>();
        Map<String, String> recognitionResultMapReg = new LinkedHashMap<>();
        for(RecognitionPair recognitionPair: recognitionResult) {
            String basicLableName = recognitionPair.getBasicLableName();
            if(basicLableName == null) continue;//没有识别到标签则直接跳过
            String columName = recognitionPair.getColumName();
            String pattern = recognitionPair.getPattern();
            //日期字段需要绑定：非法格式,QIC20220830100061,非法格式,null,,null （日期、时间、数值类型字段是否以字符串类型进行填写）
            if(basicLableName.contains("日期") || basicLableName.contains("时间") || "数值".equals(basicLableName) || "数量".equals(basicLableName)) {
                QiRuleBindingResult qiRuleBindingResult = new QiRuleBindingResult();
                qiRuleBindingResult.setColumnName(columName);
                QiConfig badFormatRule = new QiConfig();
                badFormatRule.setBasicLableName(basicLableName);
                badFormatRule.setQiCode("QIC20220830100061");
                badFormatRule.setQiName("非法格式");
                badFormatRule.setStandardCode("");
                badFormatRule.setQiType("非法格式");
                badFormatRule.setValues(null);
                qiRuleBindingResult.setQiConfig(badFormatRule);
                addRule(columName, qiRuleBindingResult);
            }
            recognitionResultMap.put(basicLableName, columName);
            recognitionResultMapReg.put(basicLableName,pattern);
        }
        //通过识别信息项绑定规则
        Map<String, String> basicLabelNameToColumnName = new LinkedHashMap<>();
        for(QiConfig qiConfig: qiConfigs) {
            String basicLableName = qiConfig.getBasicLableName();
            String columName = recognitionResultMap.get(basicLableName);
            if(columName == null) continue;
            basicLabelNameToColumnName.put(basicLableName, columName);
            QiRuleBindingResult qiRuleBindingResult = new QiRuleBindingResult();
            qiRuleBindingResult.setColumnName(columName);
            qiRuleBindingResult.setQiConfig(qiConfig);
            if("格式规范性".equals(qiConfig.getQiType())) {
                //如果是格式规范性绑定配置，则需要获取安全识别对应的识别对象配置进来
                //TODO
            }
            addRule(columName, qiRuleBindingResult);
        }
        //统一设置好全局的BasicLabelNameToColumnName映射表
        QiRuleBindingResult.setBasicLabelNameToColumnName(basicLabelNameToColumnName);

        /*补充质检规则
        空值检查,QIC20220830100056,空值检查,null,,null
        属性完整性,QIC20220830100057,属性完整性,null,,null 不检测
        记录完整性,QIC20220830100058,记录完整性,null,,null 不检测
        主键唯一性,QIC20221230100044,主键唯一性,null,,null
        值域准确性,QIC20220830100060,值域准确性,公民身份号码,,null
        数据唯一性,QIC20220830100062,数据唯一性,null,,null
        更新时效性,QIC20221230100045,更新时效性,null,,null 不检测
        */

        //空值检查,QIC20220830100056,空值检查,null,,null
        QiRuleBindingResult blankValueRule = new QiRuleBindingResult();
        blankValueRule.setColumnName(QiRuleBindingResult.ALL_COLUMN);//代表所有的列都绑定
        QiConfig blankValueConfig = new QiConfig();
        blankValueConfig.setBasicLableName("");
        blankValueConfig.setQiCode("QIC20220830100056");
        blankValueConfig.setQiName("空值检查");
        blankValueConfig.setStandardCode("");
        blankValueConfig.setQiType("空值检查");
        blankValueRule.setQiConfig(blankValueConfig);
        addRule(QiRuleBindingResult.ALL_COLUMN, blankValueRule);
        //主键唯一性,QIC20221230100044,主键唯一性,null,,null 绑定在表级别
        QiRuleBindingResult mainIDRule = new QiRuleBindingResult();
        if(mainIdColumns == null) {
            mainIDRule.setColumnName(null);
        } else {
            //如果发现传了主键，那么在绑定到列上一次
            for (String mainIdColumn : mainIdColumns) {
                if (mainIdColumn != null) {
                    mainIDRule.setColumnName(mainIdColumn);//代表所有的列都绑定
                }
            }
        }
        QiConfig mainIDRuleConfig = new QiConfig();
        mainIDRuleConfig.setBasicLableName("");
        mainIDRuleConfig.setQiCode("QIC20221230100044");
        mainIDRuleConfig.setQiName("主键唯一性");
        mainIDRuleConfig.setStandardCode("");
        mainIDRuleConfig.setQiType("主键唯一性");
        mainIDRule.setQiConfig(mainIDRuleConfig);
        addRule(QiRuleBindingResult.TABLE_LEVEL, mainIDRule);
        //数据唯一性,QIC20220830100062,数据唯一性,null,,null
        QiRuleBindingResult uniqueRule = new QiRuleBindingResult();
        uniqueRule.setColumnName(QiRuleBindingResult.ALL_COLUMN);//代表所有的列都绑定
        QiConfig uniqueRuleConfig = new QiConfig();
        uniqueRuleConfig.setBasicLableName("");
        uniqueRuleConfig.setQiCode("QIC20220830100062");
        uniqueRuleConfig.setQiName("数据唯一性");
        uniqueRuleConfig.setStandardCode("");
        uniqueRuleConfig.setQiType("数据唯一性");
        uniqueRule.setQiConfig(uniqueRuleConfig);
        addRule(QiRuleBindingResult.TABLE_LEVEL, uniqueRule);
        //值域准确性
        QiRuleBindingResult valueRightRule = new QiRuleBindingResult();
        valueRightRule.setColumnName(QiRuleBindingResult.ALL_COLUMN);//代表所有的列都绑定
        QiConfig valueRightRuleConfig = new QiConfig();
        valueRightRuleConfig.setBasicLableName("");
        valueRightRuleConfig.setQiCode("QIC20220830100060");
        valueRightRuleConfig.setQiName("值域准确性");
        valueRightRuleConfig.setStandardCode("");
        valueRightRuleConfig.setQiType("值域准确性");
        valueRightRule.setQiConfig(valueRightRuleConfig);
        addRule(QiRuleBindingResult.ALL_COLUMN, valueRightRule);
        //属性完整性
        QiRuleBindingResult attributesIntegrityRule = new QiRuleBindingResult();
        attributesIntegrityRule.setColumnName(QiRuleBindingResult.ALL_COLUMN);//代表所有的列都绑定
        QiConfig attributesIntegrityRuleConfig = new QiConfig();
        attributesIntegrityRuleConfig.setBasicLableName("");
        attributesIntegrityRuleConfig.setQiCode("QIC20220830100057");
        attributesIntegrityRuleConfig.setQiName("属性完整性");
        attributesIntegrityRuleConfig.setStandardCode("");
        attributesIntegrityRuleConfig.setQiType("属性完整性");
        attributesIntegrityRule.setQiConfig(attributesIntegrityRuleConfig);
        addRule(QiRuleBindingResult.TABLE_LEVEL, attributesIntegrityRule);
        //记录完整性
        QiRuleBindingResult recordsIntegrityRule = new QiRuleBindingResult();
        recordsIntegrityRule.setColumnName(QiRuleBindingResult.ALL_COLUMN);//代表所有的列都绑定
        QiConfig recordsIntegrityRuleConfig = new QiConfig();
        recordsIntegrityRuleConfig.setBasicLableName("");
        recordsIntegrityRuleConfig.setQiCode("QIC20220830100058");
        recordsIntegrityRuleConfig.setQiName("记录完整性");
        recordsIntegrityRuleConfig.setStandardCode("");
        recordsIntegrityRuleConfig.setQiType("记录完整性");
        recordsIntegrityRule.setQiConfig(recordsIntegrityRuleConfig);
        addRule(QiRuleBindingResult.TABLE_LEVEL, recordsIntegrityRule);
        //更新时效性
        QiRuleBindingResult updateTimeRule = new QiRuleBindingResult();
        updateTimeRule.setColumnName(QiRuleBindingResult.ALL_COLUMN);//代表所有的列都绑定
        QiConfig updateTimeRuleConfig = new QiConfig();
        updateTimeRuleConfig.setBasicLableName("");
        updateTimeRuleConfig.setQiCode("QIC20221230100045");
        updateTimeRuleConfig.setQiName("更新时效性");
        updateTimeRuleConfig.setStandardCode("");
        updateTimeRuleConfig.setQiType("更新时效性");
        updateTimeRule.setQiConfig(updateTimeRuleConfig);
        addRule(QiRuleBindingResult.TABLE_LEVEL, updateTimeRule);
        return bindQiConfig;
    }
}
