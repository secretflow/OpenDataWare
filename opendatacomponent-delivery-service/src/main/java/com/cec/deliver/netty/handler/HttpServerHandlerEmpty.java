package com.cec.deliver.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import static io.netty.buffer.Unpooled.copiedBuffer;

public class HttpServerHandlerEmpty extends SimpleChannelInboundHandler<FullHttpRequest> {

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
		if (ctx.channel().isWritable()) {
			ByteBuf buf = copiedBuffer("OK", CharsetUtil.UTF_8);
			FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buf);
		    response.headers().setInt(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
		    response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json; charset=utf-8");
		    response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
		    ctx.writeAndFlush(response);
		}
	}
}