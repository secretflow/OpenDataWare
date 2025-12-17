package com.cec.deliver.netty.core;

import com.cec.deliver.netty.config.Constants;
import com.cec.deliver.netty.handler.*;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;


/**
 * 启动service并且启动队列worker
 * @author koala
 *
 */
public class HttpServer {

	public static ChannelPromise promise;
	
	private int port;
	private int pgnum;
	private int cgnum;
	
	private EasyNettyConfig serverConfig;

	public HttpServer(EasyNettyConfig serverConfig) {
		this.serverConfig = serverConfig;
		this.port = serverConfig.getPropInt(Constants.C_S_PORT);
		this.pgnum = serverConfig.getPropInt(Constants.C_S_BOSS_GROUP_EVENT_LOOPS);
		this.cgnum = serverConfig.getPropInt(Constants.C_S_WORKER_GROUP_EVENT_LOOPS);

	}
	
	public void run() {
		// 初始化前端线程池
		NioEventLoopGroup bossGroup = new NioEventLoopGroup(pgnum);
		NioEventLoopGroup workGroup = new NioEventLoopGroup(cgnum);
		try {
			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap.group(bossGroup, workGroup);
			bootstrap.channel(NioServerSocketChannel.class)
			.option(ChannelOption.SO_BACKLOG, 100);
			bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
			@Override
				protected void initChannel(SocketChannel ch) {
					ChannelPipeline pipeline = ch.pipeline();
					pipeline.addLast("decoder", new HttpRequestDecoder());
					pipeline.addLast("aggregator", new HttpObjectAggregator(65535));
					pipeline.addLast("encoder", new HttpResponseEncoder());
					pipeline.addLast("handler", new HttpQueueServerHandler(serverConfig));
				}
			})
			.childOption(ChannelOption.TCP_NODELAY, true)
			.childOption(ChannelOption.SO_RCVBUF, 65535)
			.childOption(ChannelOption.SO_KEEPALIVE, true);
			ChannelFuture future = bootstrap.bind(port).sync();
			System.out.println("EasyNetty Server started. Listen port: " + port + " :)");
			future.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			bossGroup.shutdownGracefully();
			workGroup.shutdownGracefully();
		}
	}
}