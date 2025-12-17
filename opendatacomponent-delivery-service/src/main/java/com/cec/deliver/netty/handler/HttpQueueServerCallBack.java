package com.cec.deliver.netty.handler;

import com.cec.deliver.netty.core.Controller;
import com.cec.deliver.netty.core.EasyNettyConfig;
import com.cec.deliver.netty.utils.UriUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;

public class HttpQueueServerCallBack implements Runnable {
	
	private EasyNettyConfig serverConfig;
	private ArrayBlockingQueue<HttpPoJo> HttpPoJoQueue;
	
	public HttpQueueServerCallBack(EasyNettyConfig serverConfig, ArrayBlockingQueue<HttpPoJo> HttpPoJoQueue) {
		this.serverConfig = serverConfig;
		this.HttpPoJoQueue = HttpPoJoQueue;
	}
	
	public void run() {
		while(true) {
			// 获取消息
			HttpPoJo httppojo = null;
			ChannelHandlerContext ctx = null;
			try {
				httppojo = HttpPoJoQueue.take();
				ctx = httppojo.getCtx();
			} catch (Exception e1) {
				e1.printStackTrace();
				break;
			}
			String uri = null;
			String body = null;
			FullHttpRequest request = null;
			
			uri = httppojo.getUri();
			body = httppojo.getBody();
			request = httppojo.getFullHttpRequest();
			
			Map<String, String> paras = UriUtils.getPara(uri);
			
			String path = paras.get("__prv");
						
			Controller controller = serverConfig.process(path, paras, body, request);
			
			redner(ctx, controller);
		}
	}

	private void redner(ChannelHandlerContext ctx, Controller controller) {
		// 写消息
		if (ctx.channel().isWritable()) {
			ctx.write(controller.getResponse());
		}
		ctx.flush();
	}
}
