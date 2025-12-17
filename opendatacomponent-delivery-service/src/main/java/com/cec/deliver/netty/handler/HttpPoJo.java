package com.cec.deliver.netty.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

public class HttpPoJo {
	ChannelHandlerContext ctx;
	String uri;
	String body;
	FullHttpRequest request;
	
	public HttpPoJo(ChannelHandlerContext ctx, String uri, String body, FullHttpRequest request) {
		this.ctx = ctx;
		this.uri = uri;
		this.body = body;
		this.request = request;
	}
	
	public void setContext(ChannelHandlerContext ctx, String uri, String body, FullHttpRequest request) {
		this.ctx = ctx;
		this.uri = uri;
		this.body = body;
		this.request = request;
	}
	
	public ChannelHandlerContext getCtx() {
		return this.ctx;
	}
	
	public String getBody() {
		return this.body;
	}
	
	public String getUri() {
		return this.uri;
	}
	
	public FullHttpRequest getFullHttpRequest() {
		return this.request;
	}
}
