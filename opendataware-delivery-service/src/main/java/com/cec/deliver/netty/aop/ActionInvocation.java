package com.cec.deliver.netty.aop;

import com.cec.deliver.netty.core.Controller;
import io.netty.handler.codec.http.FullHttpResponse;

import java.util.Map;

public class ActionInvocation {

	public Map<String, String> getParas() {
		return paras;
	}

	public String getBody() {
		return body;
	}

	public Controller getController() {
		return controller;
	}

	private Map<String,String> paras;
	private String body;
	private Controller controller;
	
	@SuppressWarnings("unused")
	private ActionInvocation () {}
	
	public ActionInvocation(Controller controller, Map<String,String> paras, String body) {
		this.paras = paras;
		this.controller = controller;
		this.body = body;
	}
	
	/**
	 * 直接返回给controller去处理
	 */
	public void invoke() {
		this.controller.process(paras, body);
	}
	
	/** 
	 * 交付结果
	 * @return
	 */
	public FullHttpResponse get() {
		return this.controller.getResponse();
	}
		
}
