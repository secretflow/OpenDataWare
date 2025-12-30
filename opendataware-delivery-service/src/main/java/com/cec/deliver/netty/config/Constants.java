package com.cec.deliver.netty.config;

/**
 * 框架默认常熟
 * @author koala
 *
 */
public class Constants {
	
	public static int S_PORT = 8088;
	public static int S_BOSS_GROUP_EVENT_LOOPS = 10;
	public static int S_WORKER_GROUP_EVENT_LOOPS = 100;
	public static int S_POJO_QUEUE_SIZE = 500;
	public static int S_POJO_QUEUE_WORKERS = 20;

	public static String D_NOT_FOUND = "404 NOT FOUND";
	public static String D_BAD_REQUEST = "400 BAD REQUEST";
	public static String D_INTERNAL_SERVER_ERROR = "500 INTERNAL SERVER ERROR";
	
	public static String C_S_PORT = "server.port";
	public static String C_S_BOSS_GROUP_EVENT_LOOPS = "server.bloops";
	public static String C_S_WORKER_GROUP_EVENT_LOOPS = "server.wloops";
	public static String C_S_POJO_QUEUE_SIZE = "server.queue.size";
	public static String C_S_POJO_QUEUE_WORKERS = "server.queue.workers";
	
	public static String C_D_NOT_FOUND = "response.msg.404";
	public static String C_D_BAD_REQUEST = "response.msg.400";
	public static String C_D_INTERNAL_SERVER_ERROR = "response.msg.500";
}
