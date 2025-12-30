package com.cec.examine.template.recognition;

import com.cec.examine.ner.BertNerTypeCode;
import com.cec.examine.recognize.*;
import com.cec.examine.template.ConfigFactory;
import com.cec.examine.template.StringPatternIndex;
import com.cec.examine.template.TableCellPojo;
import com.cec.examine.template.recognition.functions.*;
import com.cec.examine.util.MapCounter;
import com.cec.examine.util.StringUtil;
import com.cec.examine.util.jsonparser.ObjectToJSON;

import java.util.*;
import java.util.concurrent.*;

public class RecognitionTemplate {

    public static String NaN = "NaN";
    private List<RecognitionCofig> recognitionConfigs;//安全识别配置

    private Recognize columnRecognizes;//列字典识别
    private Recognize contentRecognizes;//内容字典识别

    private List<Recognize> contentRegExpRecognizes;//内容正则识别

    private Map<String, String> typeToRegMap;//通过信息项类型找到正则

    private Map<String, String> javaFunctionToType;//通过java函数名称找code
    private Map<String, String> nerTypeToType;//通过java函数名称找code

    private Recognize nerRecognize = new RecognizeNERRuleBased();//ner专门的识别对象

    private Map<String, RecognitionCofig> codeToRecognitionCofig;//通过code获取RecognitionCofig
    private StringPatternIndex stringPatternIndex;//多模态模式索引

    /**
     * 3.5版本支持的行业：
     * 公共数据：政务、公共安全、其他
     * 企业数据：能源、金融 - 证券、金融 - 保险、其他
     * 个人信息是必选项
     */
    public static enum ProductType {
        DataFactory ,//数据要素化产品全部

        DataFactoryGovernment  ,//数据要素化产品-政务
        DataFactoryEnergy  ,//数据要素化产品-能源
        DataFactoryFinancialSecurity  ,//数据要素化产品-金融/证券
        DataFactoryFinancialInsurance  ,//数据要素化产品-金融/保险

        DataFactoryFinancialFinance  ,//数据要素化产品-金融/财政
        DataFactoryPublicSafety  ,//数据要素化产品-公共安全
        DataFactoryOthers  ,//数据要素化产品-其他
        ClassificationAndGrading, //分类分级产品安全识别
        DataFactorySensitive, //数据要素化产品敏感识别
    }
    public RecognitionTemplate() {
        init(ProductType.DataFactory);
    }

    public RecognitionTemplate(ProductType productType) {
        init(productType);
    }
    public RecognitionTemplate(String industryType) {
        ProductType productType = ProductType.DataFactoryOthers;
        if("政务".equals(industryType)) {
            productType = ProductType.DataFactoryGovernment;
        } else if ("能源".equals(industryType)) {
            productType = ProductType.DataFactoryEnergy;
        } else if ("财政".equals(industryType)) {
            //财政用个人敏感信息
            productType = ProductType.DataFactorySensitive;
        } else if ("金融/证券".equals(industryType) || "证券".equals(industryType)) {
            productType = ProductType.DataFactoryFinancialSecurity;
        } else if ("金融/保险".equals(industryType) || "保险".equals(industryType)) {
            productType = ProductType.DataFactoryFinancialInsurance;
        } else if ("公共安全".equals(industryType)) {
            productType = ProductType.DataFactoryPublicSafety;
        }
        init(productType);
    }
    private void init(ProductType productType) {
        //初始化识别对象
        ConfigFactory<RecognitionCofigLoader> recognitionConfigLoaderConfigFactory = new ConfigFactory<>();
        recognitionConfigs = recognitionConfigLoaderConfigFactory.loadConfigs(ConfigFactory.RESOURCE_FILE, RecognitionCofigLoader.class);
        contentRegExpRecognizes = new ArrayList<>();
        typeToRegMap = new LinkedHashMap<>();
        javaFunctionToType = new LinkedHashMap<>();
        nerTypeToType = new LinkedHashMap<>();
        codeToRecognitionCofig = new LinkedHashMap<>();
        stringPatternIndex = new StringPatternIndex();
        LinkedHashMap<String,String> wordsToTypesForColumn = new LinkedHashMap<>();
        LinkedHashMap<String,String> wordsToTypesForContent = new LinkedHashMap<>();
        for(RecognitionCofig recognitionCofig: recognitionConfigs) {
            if (productType == ProductType.DataFactory && "CEC司库测试知识库".equals(recognitionCofig.getCategory())) continue;//要素识别不需要
            if (productType == ProductType.DataFactorySensitive && ("CEC司库测试知识库".equals(recognitionCofig.getCategory()) || !recognitionCofig.getClassification().equals("个人主体标识"))) continue;//要素敏感识别算子不需要
            if (productType == ProductType.DataFactoryEnergy && !"个人信息".equals(recognitionCofig.getCategory())) continue;
            if (productType == ProductType.DataFactoryOthers && !"个人信息".equals(recognitionCofig.getCategory())) continue;
            if (productType == ProductType.DataFactoryFinancialInsurance && !"个人信息".equals(recognitionCofig.getCategory()) && (recognitionCofig.getClassification() == null || recognitionCofig.getClassification().indexOf("保险") != 0)) continue;
            if (productType == ProductType.DataFactoryFinancialSecurity && !"个人信息".equals(recognitionCofig.getCategory()) && (recognitionCofig.getClassification() == null || recognitionCofig.getClassification().indexOf("证券") != 0)) continue;
            if (productType == ProductType.DataFactoryFinancialFinance && !"个人信息".equals(recognitionCofig.getCategory()) && (recognitionCofig.getClassification() == null || recognitionCofig.getClassification().indexOf("财政") != 0)) continue;
            if (productType == ProductType.DataFactoryGovernment && !"政务".equals(recognitionCofig.getCategory()) && !"个人信息".equals(recognitionCofig.getCategory())) continue;
            if (productType == ProductType.DataFactoryPublicSafety && !"公共安全".equals(recognitionCofig.getCategory()) && !"个人信息".equals(recognitionCofig.getCategory())) continue;
            codeToRecognitionCofig.put(recognitionCofig.getCode(),recognitionCofig);
            //列识别采用字典识别模块
            String englishColumName = recognitionCofig.getEnglishColumName().replaceAll("，",",").replaceAll("、",",");
            String chineseColumName = recognitionCofig.getChineseColumName().replaceAll("，",",").replaceAll("、",",");
            List<String> words = new ArrayList<>();
            words.addAll(Arrays.asList(englishColumName.split(",")));
            words.addAll(Arrays.asList(chineseColumName.split(",")));
            if(productType != ProductType.DataFactorySensitive)
            for(String word: words) {
                if( word == null || "".equals(word.trim())) continue;
                wordsToTypesForColumn.put(word, recognitionCofig.getCode());
            }
            //内容识别采用正则模块,加载正则配置
            String regexPattern = recognitionCofig.getRegexPattern();
            if(recognitionCofig.getJavaFunctionName() != null && !"".equals(recognitionCofig.getJavaFunctionName())) javaFunctionToType.put(recognitionCofig.getJavaFunctionName(), recognitionCofig.getCode());//建表
            if(recognitionCofig.getNerCode() != null && !"".equals(recognitionCofig.getNerCode())) nerTypeToType.put(recognitionCofig.getNerCode(), recognitionCofig.getCode());
            if(regexPattern != null && !"".equals(regexPattern.trim())) {
                typeToRegMap.put(recognitionCofig.getCode(), regexPattern);//建表
                //优化逻辑，某些正则表达式是词典类型匹配，所以直接换成TrieTree结构更加高效
                if(isDictionaryTypePattern(regexPattern)) {
                    String [] wordsOfRegexPattern = getWordsFromPattern(regexPattern);
                    for(String word: wordsOfRegexPattern) {
                        if(word == null || "".equals(word.trim())) continue;
                        wordsToTypesForContent.put(word, recognitionCofig.getCode());
                    }
                } else {
                    Recognize regexpRecogize = new RecognizeRegularExpression(regexPattern);
                    regexpRecogize.setCode(recognitionCofig.getCode());
                    regexpRecogize.setName(recognitionCofig.getBasicLableName());
                    contentRegExpRecognizes.add(regexpRecogize);
                }
            }
        }
        columnRecognizes = new RecognizeDictionaryMultiTypes(wordsToTypesForColumn, true);
        //内容识别加入到正则识别集合中去，来替换原来的正则识别
        contentRecognizes = new RecognizeDictionaryMultiTypes(wordsToTypesForContent, true);
        contentRecognizes.setName("正则字典");
        contentRecognizes.setCode("dict");
        contentRegExpRecognizes.add(contentRecognizes);
    }

    /**
     * 小工具，判断正则表达式是否是字典类型的
     * @param pattern
     * @return
     */
    public static boolean isDictionaryTypePattern(String pattern) {
        int b = pattern.indexOf("(?:");
        int c = pattern.indexOf("\\d");
        if(b == 0 && c < 0) return true;
        else return false;
    }

    /**
     * 把正则表达式切分为词
     * @param pattern
     * @return
     */
    public static String [] getWordsFromPattern(String pattern) {
        String s = pattern.substring(3, pattern.length()-1);
        return s.split("\\|");
    }
    public List<RecognitionPair> recognise(Map<String, TableCellPojo> map) {
        return recognise(map, null, null);
    }

    private boolean predict(Long countNan) {
        return countNan > 10 && countNan % (10 + countNan/ 8000 ) != 0;
        //return false;
    }
    /**
     * 识别单行，一般大数据算子需要这样调用
     * @param map 一行数据
     * @return
     */
    public List<RecognitionPair> recognise(Map<String, TableCellPojo> map, Set<String> removeSet, MapCounter counterOfColNan) {
        List<RecognitionPair> result = new ArrayList<>();
        for (String column : map.keySet()) {
            if(removeSet != null && removeSet.contains(column)) continue;
            RecognitionPair recognitionPair = new RecognitionPair();
            recognitionPair.setColumName(column);
            recognitionPair.setCountOfRecords(1L);//只有一行
            TableCellPojo tableCellPojo = map.get(column);
            recognitionPair.setComments(tableCellPojo.getComments());

            //1.先识别列
            String columnType = recogniseColumn(column);

            //2.如果有列中文名称也需要检查,二者取有类型的即可，若都无法识别类型，列类型即为null
            String chineseColumnType = recogniseColumn(tableCellPojo.getChineseColumnName());
            if(columnType == null) columnType = chineseColumnType;

            if(columnType == null) {
                //3.若列无法识别出类型，则进行内容识别，内容正则，java函数
                String content = String.valueOf(map.get(column).getValue());//获取表内容
                Long countNan = counterOfColNan != null ? counterOfColNan.get(column) : 0;
                String contentType = counterOfColNan != null && predict(countNan) ? null : recogniseContent(content);
                //String contentType = recogniseContent(content);
                if(contentType != null) {
                    recognitionPair.setCode(contentType);
                } else {
                    recognitionPair.setCode(NaN);
                    if(counterOfColNan != null) counterOfColNan.incr(column);//发现是NaN则+1
                }
                recognitionPair.setCountOfPositive(1L);
            } else {
                //如果列识别中了
                recognitionPair.setPattern(typeToRegMap.get(columnType));//识别中了才存储对应的正则
                recognitionPair.setCode(columnType);
                recognitionPair.setCountOfPositive(1L);
            }
            result.add(recognitionPair);
        }//for(String column: map.keySet())
        return result;
    }
    /**
     * 通过输入的数据输出识别结果，支持少量数据，建议在1000以内
     * @param data
     * @return
     */
    public List<RecognitionPair> recognise(List<Map<String, TableCellPojo>> data, float threshold) {
        List<RecognitionPair> collector = new ArrayList<>();//收集结果的对象
        for(Map<String, TableCellPojo> map: data) {
            List<RecognitionPair> recognitionPairs = recognise(map);
            collector.addAll(recognitionPairs);
        }
        return mergeRecognitionPair(collector, threshold);
    }

    public static List<Map<String, TableCellPojo>> convertData(Map<String, List<TableCellPojo>> data) {
        List<Map<String, TableCellPojo>> data2 = new ArrayList<>();
        int maxLen = 0;
        for(String column: data.keySet()) {
            List<TableCellPojo> list = data.get(column);
            if(maxLen < list.size()) maxLen = list.size();
        }
        for(int i = 0; i < maxLen; i++ ) {
            Map<String, TableCellPojo> map = new HashMap<>();
            for(String column: data.keySet()) {
                if(i < data.get(column).size())
                    map.put(column, data.get(column).get(i));
            }
            data2.add(map);
        }
        return data2;
    }
    public List<RecognitionPair> recogniseFast(List<Map<String, TableCellPojo>> data, float threshold, int threadNum) {

        //多线程版本
        if(threadNum == 0) return null;
        ExecutorService executor = Executors.newFixedThreadPool(threadNum);
        List<Future> futures = new ArrayList<>();
        int step = data.size() / threadNum;
        for(int i = 0; i < threadNum; i++) {
            int start = i * step;
            int end = (i+1) * step;
            if( i == threadNum -1 ) end = data.size();
            Future<List<RecognitionPair>> future = executor.submit(new RecogniseTask(this,data,start,end,threshold));
            futures.add(future);
        }
        List<RecognitionPair> collector = new ArrayList<>();//收集结果的对象
        for(int i = 0; i < threadNum; i++) {
            try {
                List<RecognitionPair> recognitionPairs = (List<RecognitionPair>)futures.get(i).get();
                collector.addAll(recognitionPairs);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
        }
        executor.shutdown();
        return mergeRecognitionPair(collector, threshold);
    }

    /**
     * 单个线程识别的逻辑
     */
    private static class RecogniseTask implements Callable<List<RecognitionPair>> {
        private List<Map<String, TableCellPojo>> data;

        private RecognitionTemplate recognitionTemplate;
        private float threshold = 0;
        private int start = 0;
        private int end = 0;
        public RecogniseTask(RecognitionTemplate recognitionTemplate, List<Map<String, TableCellPojo>> data, int start, int end ,float threshold) {
            this.data = data;
            this.recognitionTemplate = recognitionTemplate;
            this.threshold = threshold;
            this.start = start;
            this.end = end;
        }
        @Override
        public List<RecognitionPair> call() throws Exception {
            Set<String> removeSet = new HashSet<>();
            MapCounter counterOfColNan = new MapCounter();
            List<RecognitionPair> collector = new ArrayList<>();//收集结果的对象
            for(int i = start; i < end; i++) {
                Map<String, TableCellPojo> map = data.get(i);
                List<RecognitionPair> recognitionPairs = this.recognitionTemplate.recognise(map,removeSet,counterOfColNan);
                for(RecognitionPair recognitionPair: recognitionPairs) {
                    if(!"NaN".equals(recognitionPair.getCode())) {
                        //如果已经识别出类型，则放在removeSet里面
                        removeSet.add(recognitionPair.getColumName());
                    }
                }
                collector.addAll(recognitionPairs);
            }
            return mergeRecognitionPair(collector, threshold);
        }
    }
    /**
     * 合并算子，reducer里面可以使用
     * 如果能够通过groupBy将列进行区分，保证同一个列的数据都在同一个reducer中，就可以直接使用这个工具方法进行merge
     * @param collector
     * @param threshold 如果该参数为0或者负数，那么合并计数器会选取一个最多的类别，包括无法分类的类别
     *                  如果该参数大于0，则会按照百分比取非空类别，只要达到阈值就可以返回类别
     * @return
     */
    public static List<RecognitionPair> mergeRecognitionPair(List<RecognitionPair> collector, float threshold) {
        List<RecognitionPair> result = new ArrayList<>();
        //合并逻辑 列=>code计数器
        Map<String, MapCounter<String>> mapperOfCouners = new LinkedHashMap<>();
        Map<String, String> columNameToComments = new HashMap<String,String>();
        //针对列的countOfRecords统计
        MapCounter<String> counterOfRecords = new MapCounter<>();
        MapCounter<String> counterOfNaN = new MapCounter<>();//记录各个列的NaN统计
        for (RecognitionPair recognitionPair: collector) {
            String code = recognitionPair.getCode();
            String columName = recognitionPair.getColumName();
            columNameToComments.put(columName, recognitionPair.getComments());
            Long countOfPositive = recognitionPair.getCountOfPositive();
            Long countOfRecords = recognitionPair.getCountOfRecords();
            MapCounter<String> codeCounter = mapperOfCouners.get(columName);
            if(codeCounter == null) {
                codeCounter = new MapCounter<>();
                mapperOfCouners.put(columName, codeCounter);
            }
            if(!NaN.equals(code)) {
                codeCounter.incr(code,countOfPositive);//累加信息项计数器
            }
            //保留记录各个列的NaN统计
            else if(NaN.equals(code)) counterOfNaN.incr(columName, countOfPositive);
            counterOfRecords.incr(columName, countOfRecords);//累加列下面的记录数
        }
        //组装结果
        for(String columName: mapperOfCouners.keySet()) {
            RecognitionPair recognitionPair = new RecognitionPair();
            Long countOfRecords = counterOfRecords.get(columName);
            recognitionPair.setCountOfRecords(countOfRecords);
            recognitionPair.setColumName(columName);
            recognitionPair.setComments(columNameToComments.get(columName));
            String code = mapperOfCouners.get(columName).getMaxKey();
            Long countOfPositive = 0L;
            //符合阈值设定则设置信息项，不符合阈值则直接置为NaN
            if( code != null && (float)(countOfPositive = mapperOfCouners.get(columName).get(code))/(float)countOfRecords >= threshold) {
                recognitionPair.setCode(code);
                recognitionPair.setCountOfPositive(countOfPositive);
            } else {
                recognitionPair.setCode(NaN);
                recognitionPair.setCountOfPositive(counterOfNaN.get(columName));
            }
            result.add(recognitionPair);
        }
        return result;
    }

    /**
     * 字段增强
     * @param result
     * @return
     */
    public List<RecognitionPair> enrichAttributes(List<RecognitionPair> result) {
        List<RecognitionPair> enrichedResult = new ArrayList<>();
        for (RecognitionPair recognitionPair: result) {
            //补全字段
            RecognitionCofig recognitionCofig = codeToRecognitionCofig.get(recognitionPair.getCode());
            if(recognitionCofig == null) {
                enrichedResult.add(recognitionPair);
                continue;
            }
            recognitionPair.setBasicLableName(recognitionCofig.getBasicLableName());
            String category = recognitionCofig.getCategory();
            if("公共安全".equals(category)) {
                recognitionPair.setDataType("1");
            } else if("个人信息".equals(category)) {
                recognitionPair.setTaskType("1");
            } else {
                recognitionPair.setDataType("2");
                recognitionPair.setTaskType("2");
            }
            recognitionPair.setIndustryTypeStr(category);
            recognitionPair.setLabelName(recognitionCofig.getLableName());
            recognitionPair.setParentNames(category + "/" + recognitionCofig.getClassification());
            if(recognitionCofig.isPersonal())
                recognitionPair.setSensitivePersonalInformationStatus("1");
            else
                recognitionPair.setSensitivePersonalInformationStatus("0");
            recognitionPair.setSecurityLevel(recognitionCofig.getLevel());
            enrichedResult.add(recognitionPair);
        }
        return enrichedResult;
    }

    /**
     * 识别列名
     * @param text
     * @return
     */
    public String recogniseColumn(String text) {
        return columnRecognizes.getSingleType(text);
    }

    /**
     * 识别内容
     * @param text
     * @return
     */
    public String recogniseContent(String text) {
        String code = null;
        //无内容就直接返回
        if(text == null || "".equals(text)) return code;
        //正则匹配逻辑
        for(Recognize recognize: contentRegExpRecognizes) {
            if(!stringPatternIndex.filter(recognize.getCode(), text)) continue;
            code = recognize.getSingleType(text);
            if(!recognize.getCode().equals("dict") && code != null) {
                //非字典匹配需要再转换一下
                code = recognize.getCode();
            }
            if(code != null) break;
        }
        //Java函数逻辑
        if(code == null) {
            String javaFunctionName = checkDataByJava(text);
            code = javaFunctionToType.get(javaFunctionName);
        }
        //NER逻辑
        if(code == null)
        if(StringUtil.isText(text)) {
            //非数字的字符串则需要走NER匹配
            //Recognize nerRecognize = new RecognizeNERLocal(new String[]{BertNerTypeCode.ADDRESS,BertNerTypeCode.NAME});
            nerRecognize.setTypeAlias(BertNerTypeCode.ADDRESS, "addresses");
            nerRecognize.setTypeAlias(BertNerTypeCode.NAME, "names");
            nerRecognize.setTypeAlias(BertNerTypeCode.NATIVE_PLACE, "provinces");
            String nerType = nerRecognize.getSingleType(text);
            if(nerType != null) code = nerTypeToType.get(nerType);
        }
        return code;
    }

    /**
     * 用java函数检查
     * @param content
     * @return
     */
    private String checkDataByJava(String content) {
        if(javaFunctionToType.containsKey("checkBankCard")
                && CheckBankCard.checkBankCard(content))
            return "checkBankCard";
        else if(javaFunctionToType.containsKey("checkIdCardNumber")
                && stringPatternIndex.filter(javaFunctionToType.get("checkIdCardNumber"), content)
                && CheckIdCardNumber.checkIdCardNumber(content))
            return "checkIdCardNumber";
        else if(javaFunctionToType.containsKey("checkDate")
                && stringPatternIndex.filter(javaFunctionToType.get("checkDate"), content)
                && CheckDate.checkDate(content))
            return "checkDate";
        else if(javaFunctionToType.containsKey("checkAge")
                && stringPatternIndex.filter(javaFunctionToType.get("checkAge"), content)
                && CheckAge.checkAge(content))
            return "checkAge";
        else if(javaFunctionToType.containsKey("checkGender") && CheckGender.checkGender(content))
            return "checkGender";
        else if(javaFunctionToType.containsKey("checkCreditCard") && CheckCreditCard.checkCreditCard(content))
            return "checkCreditCard";
        else     if(javaFunctionToType.containsKey("checkSchool") && CheckSchool.isSchool(content))
            return "checkSchool";
        else if(javaFunctionToType.containsKey("checkCollege") && CheckCollege.isCollege(content))
            return "checkCollege";
        else if(javaFunctionToType.containsKey("checkMajor") && CheckMajor.isMajor(content))
            return "checkMajor";
        else if(javaFunctionToType.containsKey("checkProfession") && CheckProfession.isProfession(content))
            return "checkProfession";
        else if(javaFunctionToType.containsKey("checkPosition") && CheckPosition.isPosition(content))
            return "checkPosition";
        else if(javaFunctionToType.containsKey("checkPassword") && CheckPassword.isPassword(content))
            return "checkPassword";
        else return null;
    }

    /**
     * 获取模式索引信息
     * @return
     */
    public String getStringPatternIndexJSON() {
        return ObjectToJSON.toJSONString(this.stringPatternIndex);
    }
}
