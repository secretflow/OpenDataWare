package com.cec.example.Delivery.netty.controller;

import com.cec.deliver.netty.core.Controller;
import com.cec.deliver.netty.plugin.InnerMemoryDeliveryPlugin;
import com.cec.modeling.CombinatorialDataWare;

import java.util.Map;

public class CombinatorialDCController extends Controller{

	@Override
	public void process(Map<String, String> paras, String body) {
        //给定默认参数
        String combinatorialDataWareId = paras.get("dcid") == null ? "DCC": paras.get("dcid");
        String mainKey = paras.get("mk") == null ? "900000000000000000": paras.get("mk");
        //通过元件ID、主体标识查询模态元件
        CombinatorialDataWare combinatorialDataWare = InnerMemoryDeliveryPlugin.getCombinatorialDataWareByMainKey(combinatorialDataWareId, mainKey);
        renderJson(combinatorialDataWare);
	}
}
