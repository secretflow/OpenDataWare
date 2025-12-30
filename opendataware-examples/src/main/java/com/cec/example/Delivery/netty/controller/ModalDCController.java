package com.cec.example.Delivery.netty.controller;

import com.alibaba.fastjson2.JSON;
import com.cec.deliver.DataWareDelivery;
import com.cec.deliver.InnerMemory.DataWareDeliveryInnerMemory;
import com.cec.deliver.netty.core.Controller;
import com.cec.deliver.netty.plugin.InnerMemoryDeliveryPlugin;
import com.cec.modeling.ModalDataWare;

import java.util.Map;

public class ModalDCController extends Controller{

	@Override
	public void process(Map<String, String> paras, String body) {
        //给定默认参数
        String modalDataWareId = paras.get("dcid") == null ? "DCA": paras.get("dcid");
        String mainKey = paras.get("mk") == null ? "900000000000000000": paras.get("mk");
        //通过元件ID、主体标识查询模态元件
        ModalDataWare modalDataWare = InnerMemoryDeliveryPlugin.getModalDataWare(modalDataWareId, mainKey);
        renderJson(modalDataWare);
	}
}
