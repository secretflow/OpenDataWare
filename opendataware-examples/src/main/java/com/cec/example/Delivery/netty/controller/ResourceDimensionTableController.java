package com.cec.example.Delivery.netty.controller;

import com.alibaba.fastjson.JSONObject;
import com.cec.deliver.netty.core.Controller;
import com.cec.utils.FileUtilityUtil;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ResourceDimensionTableController extends Controller{

    private static final HashMap<String, JSONObject> data = new HashMap<>();
    static {
        InputStream inputStream = ResourceDimensionTableController.class.getResourceAsStream("/ResourceDataDemo/ResourceDimensionTableDemo.csv");
        FileUtilityUtil fileUtilityUtil = FileUtilityUtil.getFileReader(inputStream);
        String [] s = fileUtilityUtil.getAll().split("\n");
        fileUtilityUtil.close();
        for(int i = 1; i < s.length; i++) {
            String line = s[i];
            String [] lineArr = line.split(",");
            JSONObject lineData = new JSONObject();
            lineData.put("enterprise_code", lineArr[0]);
            lineData.put("city",  lineArr[1]);
            data.put(lineArr[0], lineData);
        }
    }

	@Override
	public void process(Map<String, String> paras, String body) {
        JSONObject response = new JSONObject();
        response.put("code", "400");
        response.put("msg", "OK");
        response.put("data", data.get(paras.get("enterprise_code")));
        renderJson(response);
	}
}
