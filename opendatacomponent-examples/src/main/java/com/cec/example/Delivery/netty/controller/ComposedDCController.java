package com.cec.example.Delivery.netty.controller;

import com.alibaba.fastjson2.JSON;
import com.cec.deliver.netty.core.Controller;
import com.cec.deliver.netty.plugin.InnerMemoryDeliveryPlugin;
import com.cec.modeling.ComposedDataComponent;

import java.util.List;
import java.util.Map;

public class ComposedDCController extends Controller{

	@Override
	public void process(Map<String, String> paras, String body) {
        //给定默认参数
        String composedDataComponentId = paras.get("dcid") == null ? "DCB": paras.get("dcid");
        String index = paras.get("index") == null ? "0": paras.get("index");
        String size = paras.get("size") == null ? "1": paras.get("size");

        //通过元件ID、主体标识查询模态元件
        List<ComposedDataComponent> composedDataComponent = InnerMemoryDeliveryPlugin.getComposedDataComponent(composedDataComponentId, Integer.parseInt(index), Integer.parseInt(size));
        renderJson(composedDataComponent);
	}
}
