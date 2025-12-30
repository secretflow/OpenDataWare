package com.cec.example.Delivery.netty.controller;

import com.cec.deliver.netty.aop.Before;
import com.cec.deliver.netty.core.Controller;
import com.cec.example.Delivery.netty.interceptor.AuthInterceptor;

import java.util.Map;

@Before(AuthInterceptor.class)
public class ParasController extends Controller {

	@Override
	public void process(Map<String, String> paras, String body) {
		renderText(paras.get("m") == null ? "Nil": paras.get("m"));
	}

}
