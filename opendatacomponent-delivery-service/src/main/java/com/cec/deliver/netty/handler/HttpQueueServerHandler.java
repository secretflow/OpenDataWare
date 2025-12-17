package com.cec.deliver.netty.handler;

import com.cec.deliver.netty.config.Constants;
import com.cec.deliver.netty.core.EasyNettyConfig;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * 用队列接收和分发请求
 * @author koala
 *
 */
public class HttpQueueServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
	
	/**
	 * 必须是单例，否则会导致内存泄漏
	 */
	private static ArrayBlockingQueue<HttpPoJo> HttpPoJoQueue = null;
	
    public HttpQueueServerHandler(EasyNettyConfig sc) {
    	int userQueueSize = sc.getPropInt(Constants.C_S_POJO_QUEUE_SIZE);
    	int workerThreadNum = sc.getPropInt(Constants.C_S_POJO_QUEUE_WORKERS);
		synchronized (HttpQueueServerHandler.class) {
			if (HttpPoJoQueue == null) {
				HttpPoJoQueue = new ArrayBlockingQueue<HttpPoJo>(userQueueSize);
				for (int i = 0; i < workerThreadNum; i++) {
					Thread t = new Thread(new HttpQueueServerCallBack(sc, HttpPoJoQueue));
					t.start();
				}
			}
		}
	}
    /**
     * 系统队列开放接口
     * @return
     */
    public static ArrayBlockingQueue<HttpPoJo> getSystemQueue() {
    	return HttpPoJoQueue;
    }
    
    @SuppressWarnings("unused")
	private HttpQueueServerHandler (){}

    @Override
	protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
		String uri = msg.uri();
		String body = msg.content().toString(io.netty.util.CharsetUtil.UTF_8);
		FullHttpRequest request = msg;
		HttpPoJoQueue.put(new HttpPoJo(ctx, uri, body, request));
	}

}