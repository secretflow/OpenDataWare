package com.cec.deliver.netty.core;

import com.alibaba.fastjson.JSON;
import com.cec.deliver.netty.aop.ActionInvocation;
import com.cec.deliver.netty.aop.Before;
import com.cec.deliver.netty.aop.DefaultInterceptor;
import com.cec.deliver.netty.aop.Interceptor;
import com.cec.deliver.netty.config.Constants;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.util.Map;

import static io.netty.buffer.Unpooled.copiedBuffer;

public abstract class Controller {
	
	public abstract void process(Map<String,String> paras, String body);
	
	private FullHttpResponse response = null;
	private FullHttpRequest request = null;
	
	private EasyNettyConfig config = null;// 所属的conf
	
	public void setConfig(EasyNettyConfig config) {
		this.config = config;
	}
	
	public EasyNettyConfig getConfig() {
		return this.config;
	}
	
	public void renderJson(Object object) {
		String r = JSON.toJSONString(object);
		renderText(r);
	}
	
	public void renderText(String msg) {
		render(msg, HttpResponseStatus.OK);
	}
	
	public void render(String msg, HttpResponseStatus s) {
		ByteBuf buf = copiedBuffer(msg, CharsetUtil.UTF_8);
		response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buf);
		response.headers().setInt(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
		response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json; charset=utf-8");
		response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
	}
	
	public void render(FullHttpResponse response) {
		this.response = response;
	}
	
	public void render404() {
		render404(config.getProp(Constants.C_D_NOT_FOUND));
	}
	
	public void render400() {
		render400(config.getProp(Constants.C_D_BAD_REQUEST));
	}
	
	public void render500() {
		render500(config.getProp(Constants.C_D_INTERNAL_SERVER_ERROR));
	}
	
	public void render404(String msg) {
		render(msg, HttpResponseStatus.NOT_FOUND);
	}
	
	public void render400(String msg) {
		render(msg, HttpResponseStatus.BAD_REQUEST);
	}
	
	public void render500(String msg) {
		render(msg, HttpResponseStatus.INTERNAL_SERVER_ERROR);
	}
	
	public FullHttpResponse getResponse() {
		return response;
	}
	
	public FullHttpRequest getRquest() {
		return request;
	}
	
	/**
	 * 带注解的处理
	 * @param paras
	 * @param body
	 * @return
	 */
	public void processWithAnnotation(Map<String,String> paras, String body, FullHttpRequest request) {
		this.request = request;
		ActionInvocation ai = new ActionInvocation(this, paras, body);
		Before before = this.getClass().getAnnotation(Before.class);
		Interceptor i = new DefaultInterceptor();
		if (before != null) {
			Class<? extends Interceptor>[] ic = before.value();
			if (ic.length > 0) {
				try {
					i = (Interceptor) ic[0].newInstance();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		i.intercept(ai);
	}
}
