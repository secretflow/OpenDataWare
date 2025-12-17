package com.cec.example.Delivery.netty.controller;

import com.alibaba.fastjson2.JSON;
import com.cec.deliver.DataComponentDelivery;
import com.cec.deliver.InnerMemory.DataComponentDeliveryInnerMemory;
import com.cec.deliver.netty.core.Controller;
import com.cec.deliver.netty.plugin.InnerMemoryDeliveryPlugin;
import com.cec.modeling.ModalDataComponent;

import java.util.Map;

public class ModalDCController extends Controller{

	@Override
	public void process(Map<String, String> paras, String body) {
        //给定默认参数
        String modalDataComponentId = paras.get("dcid") == null ? "DCA": paras.get("dcid");
        String mainKey = paras.get("mk") == null ? "900000000000000000": paras.get("mk");
        //通过元件ID、主体标识查询模态元件
        ModalDataComponent modalDataComponent = InnerMemoryDeliveryPlugin.getModalDataComponent(modalDataComponentId, mainKey);
        renderJson(modalDataComponent);
	}
}
