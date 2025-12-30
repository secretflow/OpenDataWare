package com.cec.example.Delivery.netty.controller;

import com.cec.deliver.netty.core.Controller;
import java.util.Map;

public class JSONController extends Controller{

	@Override
	public void process(Map<String, String> paras, String body) {
		renderJson("{}");
	}
}
