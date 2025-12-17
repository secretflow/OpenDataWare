package com.cec.example.Delivery.netty.controller;

import com.cec.deliver.netty.core.Controller;
import com.cec.deliver.netty.plugin.InnerMemoryDeliveryPlugin;
import com.cec.modeling.CombinatorialDataComponent;

import java.util.Map;

public class CombinatorialDCController extends Controller{

	@Override
	public void process(Map<String, String> paras, String body) {
        //给定默认参数
        String combinatorialDataComponentId = paras.get("dcid") == null ? "DCC": paras.get("dcid");
        String mainKey = paras.get("mk") == null ? "900000000000000000": paras.get("mk");
        //通过元件ID、主体标识查询模态元件
        CombinatorialDataComponent combinatorialDataComponent = InnerMemoryDeliveryPlugin.getCombinatorialDataComponentByMainKey(combinatorialDataComponentId, mainKey);
        renderJson(combinatorialDataComponent);
	}
}
