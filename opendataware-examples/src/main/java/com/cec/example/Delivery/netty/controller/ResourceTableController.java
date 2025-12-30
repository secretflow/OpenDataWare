package com.cec.example.Delivery.netty.controller;

import com.alibaba.fastjson.JSONObject;
import com.cec.deliver.netty.core.Controller;
import com.cec.utils.FileUtilityUtil;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ResourceTableController extends Controller{

    private static final HashMap<String, JSONObject> data = new HashMap<>();
    static {
        InputStream inputStream = ResourceTableController.class.getResourceAsStream("/ResourceDataDemo/ResourceTableDemo.csv");
        FileUtilityUtil fileUtilityUtil = FileUtilityUtil.getFileReader(inputStream);
        String [] s = fileUtilityUtil.getAll().split("\n");
        fileUtilityUtil.close();
        for(int i = 1; i < s.length; i++) {
            String line = s[i];
            String [] lineArr = line.split(",");
            JSONObject lineData = new JSONObject();
            lineData.put("enterprise_code", lineArr[0]);
            lineData.put("revenue_of_recycle", Double.parseDouble(lineArr[1]));
            lineData.put("measurement", lineArr[2]);
            lineData.put("update_time", lineArr[3]);
            lineData.put("scale", Integer.parseInt(lineArr[4]));
            lineData.put("registered_assets", Double.parseDouble(lineArr[5]));
            lineData.put("revenue_of_total", Double.parseDouble(lineArr[6]));
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
