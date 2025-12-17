package com.cec.examine.template;

import com.cec.examine.reversibility.*;
import com.cec.examine.template.PVT.PVTResult;
import com.cec.examine.template.PVT.PVTTemplate;
import com.cec.examine.template.quality.*;
import com.cec.examine.template.recognition.RecognitionPair;
import com.cec.examine.template.recognition.RecognitionTemplate;
import com.cec.examine.util.jsonparser.JSONParser;
import com.cec.examine.util.jsonparser.model.JsonArray;
import com.cec.modeling.ComposedDataComponent;
import com.cec.modeling.DataComponent;
import com.cec.modeling.ModalDataComponent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 生产复核流程
 * 模态：安全识别->元件质检->相关性检查->相似性审查->离群点审核<p/>
 * 组态：安全识别->元件质检->相关性检查->离群点审核<p/>
 * @author koala
 */
public class ComponentProductionCheckTemplate {

    //质检模板
    private static final QiTemplate qiTemplate = new QiTemplate();
    //安全识别模板
    private static final RecognitionTemplate recognitionTemplate = new RecognitionTemplate();
    //黄暴恐识别模版
    private static final PVTTemplate pvtTemplate = new PVTTemplate();

    //安全识别的阈值参数设置，0会考虑NaN类型，大于零才会考虑非NaN
    private static final float recognitionThreshold = 0.1F;

    /**
     * 元件复核总方法
     * @param resourceJSON 资源数据的JSON
     * @param resultJSON 结果数据的JSON
     * @return 检查结果
     */
    public static Map<String,Object> check(String resourceJSON, String resultJSON, String [] mainIDColumn) {
        JSONParser jsonParser = new JSONParser();
        Map<String, Object> result = new HashMap<>();
        try {
            //解析资源JSON
            JsonArray resourceJSONArray = (JsonArray) jsonParser.fromJSON(resourceJSON);
            //解析结果JSON
            JsonArray resultJSONArray = (JsonArray) jsonParser.fromJSON(resultJSON);
            //定义识别数据集合
            List<Map<String, TableCellPojo>> data = new ArrayList<>();
            //定义离群点实例
            Outlier outlier = new Outlier();
            //定义相关性实例
           // Relativity relativity = new Relativity();
            //定义相似性实例
            //Similarity similarity = new Similarity();
            //设置主体字段
            outlier.setMainIdColumn(mainIDColumn);
            //relativity.setMainIdColumn(mainIDColumn);
           // similarity.setMainIdColumn(mainIDColumn);
            //加载资源数据
            for(int i = 0; i < resourceJSONArray.size(); i++) {
                Map<String, TableCellPojo> map = TableCellPojo.tableCellPojoMapWrapper(resourceJSONArray.getJsonObject(i));
                outlier.addLineOfResource(map);
               // relativity.addLineOfResource(map);
               // similarity.addLineOfResource(map);
            }
            //加载结果数据
            for(int i = 0; i < resultJSONArray.size(); i++) {
                Map<String, TableCellPojo> map =  TableCellPojo.tableCellPojoMapWrapper(resultJSONArray.getJsonObject(i));
                data.add(map);
                outlier.addLineOfResult(map);
                //relativity.addLineOfResult(map);
               // similarity.addLineOfResult(map);
            }
            //黄暴恐识别
            List<PVTResult> pvtResults = pvtTemplate.pvt(data);
            //安全识别计算
            List<RecognitionPair> recognitionPairs = recognitionTemplate.recognise(data, recognitionThreshold);
            //质检落标和计算
            Map<String, List<QiRuleBindingResult>> qiRuleBindingResults = qiTemplate.bind(mainIDColumn, recognitionPairs);
            //获取质检结果，主键是规则，一个规则对应一个质检结果
            List<QiResult> qiResults = qiTemplate.qi(mainIDColumn, data);
            //计算离群点
            long start = System.currentTimeMillis();
            List<OutlierResult> outlierResults = outlier.check();
            JsonArray outlierResultsJson = jsonParser.fromList(outlierResults);
            //计算相关性
           // List<RelativityResult> relativityResults = relativity.check();
           // JsonArray relativityResultsJson = jsonParser.fromList(relativityResults);
            //计算相似性
            //List<SimilarityResult> similarityResults = similarity.check();
            //JsonArray similarityResultsJson = jsonParser.fromList(similarityResults);
            long end = System.currentTimeMillis();
            System.out.println("cost: " + (end - start) + "ms.");
            //组装结果
            result.put("pvt", pvtResults);
            result.put("recognition", recognitionPairs);
            result.put("qiBinding", qiRuleBindingResults);
            result.put("qi", qiResults);
            result.put("outlier", outlierResultsJson);
            //result.put("relativity", relativityResultsJson);
            //result.put("similarity", similarityResultsJson);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 检查模态元件，模态元件要求输入主体字段，支持多个主体字段
     * 可逆性算子：检查相似性、相关性、离群点
     * 合规算子：黄暴恐
     * 通用算子：信息项识别，质检
     * @param dataComponents 元件数据
     * @return 审核结果
     */
    public static Map<String, Object> checkModalDataComponents(List<ModalDataComponent> dataComponents) {
        String [] mainIDColumn = { dataComponents.get(0).getMainKey().getValueColumn()};
        Map<String, Object> result = new HashMap<>();
        //定义识别数据集合
        List<Map<String, TableCellPojo>> data = new ArrayList<>();
        //定义离群点实例
        Outlier outlier = new Outlier();
        //定义相关性实例
        Relativity relativity = new Relativity();
        //定义相似性实例
        //Similarity similarity = new Similarity();
        //设置主体字段
        outlier.setMainIdColumn(mainIDColumn);
        relativity.setMainIdColumn(mainIDColumn);
        // similarity.setMainIdColumn(mainIDColumn);
        //加载结果数据
        for (int i = 0; i < dataComponents.size(); i++) {
            Map<String, TableCellPojo> map = TableCellPojo.tableCellPojoMapWrapper((DataComponent) dataComponents.get(i));
            data.add(map);
            outlier.addLineOfResult(map);
            relativity.addLineOfResult(map);
        }
        //黄暴恐识别
        List<PVTResult> pvtResults = pvtTemplate.pvt(data);
        //安全识别计算
        List<RecognitionPair> recognitionPairs = recognitionTemplate.recognise(data, recognitionThreshold);
        //质检落标和计算
        Map<String, List<QiRuleBindingResult>> qiRuleBindingResults = qiTemplate.bind(mainIDColumn, recognitionPairs);
        //获取质检结果，主键是规则，一个规则对应一个质检结果
        List<QiResult> qiResults = qiTemplate.qi(mainIDColumn, data);
        //计算离群点
        List<OutlierResult> outlierResults = outlier.check();
        //计算相关性
        List<RelativityResult> relativityResults = relativity.check();
        //组装结果
        result.put("pvt", pvtResults);
        result.put("recognition", recognitionPairs);
        result.put("qiBinding", qiRuleBindingResults);
        result.put("qi", qiResults);
        result.put("outlier", outlierResults);
        result.put("relativity", relativityResults);
        return result;
    }

    /**
     * 检查组态元件，
     * 可逆性算子：检查相关性、离群点
     * 合规算子：黄暴恐
     * 通用算子：信息项识别、主体标识识别、质检
     * @param dataComponents 元件数据
     * @return 审核结果
     */
    public static Map<String, Object> checkComposedDataComponents(List<ComposedDataComponent> dataComponents) {
        Map<String, Object> result = new HashMap<>();
        String [] mainIdColumn = new String[0];
        //定义识别数据集合
        List<Map<String, TableCellPojo>> data = new ArrayList<>();
        //定义离群点实例
        Outlier outlier = new Outlier();
        //定义相关性实例
        Relativity relativity = new Relativity();
        //定义相似性实例
        //Similarity similarity = new Similarity();
        //设置主体字段
        outlier.setMainIdColumn(mainIdColumn);
        relativity.setMainIdColumn(mainIdColumn);
        // similarity.setMainIdColumn(mainIDColumn);
        //加载结果数据
        for (int i = 0; i < dataComponents.size(); i++) {
            Map<String, TableCellPojo> map = TableCellPojo.tableCellPojoMapWrapper((DataComponent) dataComponents.get(i));
            data.add(map);
            outlier.addLineOfResult(map);
            relativity.addLineOfResult(map);
        }
        //黄暴恐识别
        List<PVTResult> pvtResults = pvtTemplate.pvt(data);
        //安全识别计算
        List<RecognitionPair> recognitionPairs = recognitionTemplate.recognise(data, recognitionThreshold);
        //质检落标和计算
        Map<String, List<QiRuleBindingResult>> qiRuleBindingResults = qiTemplate.bind(mainIdColumn, recognitionPairs);
        //获取质检结果，主键是规则，一个规则对应一个质检结果
        List<QiResult> qiResults = qiTemplate.qi(mainIdColumn, data);
        //计算离群点
        List<OutlierResult> outlierResults = outlier.check();
        //计算相关性
        List<RelativityResult> relativityResults = relativity.check();
        //组装结果
        result.put("pvt", pvtResults);
        result.put("recognition", recognitionPairs);
        result.put("qiBinding", qiRuleBindingResults);
        result.put("qi", qiResults);
        result.put("outlier", outlierResults);
        result.put("relativity", relativityResults);
        return result;
    }
}
