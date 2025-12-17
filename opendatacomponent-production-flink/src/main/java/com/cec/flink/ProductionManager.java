package com.cec.flink;

import com.cec.flink.connectors.http.CallAsProductionUtil;
import com.cec.flink.connectors.http.CallEventListener;
import com.cec.production.ProductionModel;

import java.util.*;

/**
 * 元件生产模型管理类
 * @author koala
 */
public class ProductionManager extends ProductionModel {

    private static Map<String, ProductionModel> dcModels = new HashMap<>();

    /**
     * 通过元件ID获取对应的模型
     * @param DC_ID 元件ID
     * @return 元件生产模型
     */
    public static  ProductionModel getDcModel(String DC_ID) {
        return dcModels.get(DC_ID);
    }

    /**
     * 加载元件模型
     * @param DC_ID 元件ID
     * @param dcModel 元件生产模型
     */
    public static void putDcModel(String DC_ID, ProductionModel dcModel) {
        dcModels.put(DC_ID, dcModel);
    }

    /**
     * 启动所有元件模型
     */
    public static void startAll() {
        for(String DC_ID : dcModels.keySet()){
            start(DC_ID);
        }
    }

    static {
        //必须初始化的代码
        CallAsProductionUtil.configEnv();
    }

    /**
     * 启动元件生产
     * @param DC_ID 元件ID
     */
    public static void start(String DC_ID) {
        ProductionModel dcModel = dcModels.get(DC_ID);
        if(dcModel != null) {
            CallAsProductionUtil.configEnv();
            List<String> cataLogs = new ArrayList<>();
            Map<String, String> sourceTableCataLogs = dcModel.getSourceTableCataLogs();
            for(String tableName : sourceTableCataLogs.keySet()){
                cataLogs.add(sourceTableCataLogs.get(tableName));
            }
            cataLogs.add(dcModel.getSinkTableCataLogs());
            CallAsProductionUtil.createCatalogs(cataLogs);
            //两种提交方式均可
            //CallAsProductionUtil.submitSql(dcModel.getSql());
            CallAsProductionUtil.submitSql(dcModel.getSql(), dcModel.getSinkTableName());
        }
    }

    /**
     * 获取元件结果
     * @param DC_ID 元件ID
     * @param sqlParaValues where条件中的元件查询参数
     */
    public static String getDC(String DC_ID, Map<String, Object> sqlParaValues) throws InterruptedException {
        ProductionModel dcModel = dcModels.get(DC_ID);
        if(dcModel == null) return null;
        String requestId = String.valueOf(UUID.randomUUID());
        CallEventListener.Message message = new CallEventListener.Message(requestId, sqlParaValues);
        for(String tableName : dcModel.getSourceTableCataLogs().keySet()){
            CallEventListener.put(tableName, message);
        }
        return CallEventListener.getResultJSONString(requestId);
    }

}
