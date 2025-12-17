package com.cec.example.Delivery.netty.controller;
import com.cec.deliver.netty.core.Controller;
import com.cec.flink.ProductionManager;
import java.util.HashMap;
import java.util.Map;

/**
 * 调用即生产Controller实现
 */
public class CallAsProductionController extends Controller{

	@Override
	public void process(Map<String, String> paras, String body) {
        //为了测试方便设置默认值
        String enterprise_code =  paras.get("enterprise_code") == null ? "900000000000000000" : paras.get("enterprise_code");
        String dc_id =  paras.get("dc_id") == null ? "001" : paras.get("dc_id");
        //以下是通用代码
        Map<String, Object> sqlParaValues = new HashMap<>();
        //透传body String
        sqlParaValues.put("$b", body);
        //透传query String Map
        paras.remove("__prv");//先去掉框架自带的参数
        sqlParaValues.put("$q", paras);
        //请求的信息增加到Message
        sqlParaValues.put("$1", enterprise_code);
        String result = "{}";
        try {
            long start = System.currentTimeMillis();
            result = ProductionManager.getDC(dc_id, sqlParaValues);
            long end = System.currentTimeMillis();
            System.out.println("返回结果时间：" + (end - start) + "ms");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        renderText(result);
    }
}
