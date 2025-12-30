package com.cec.example.Delivery.netty;
import com.cec.deliver.netty.config.*;
import com.cec.deliver.netty.core.EasyNettyConfig;
import com.cec.example.Delivery.netty.controller.*;
import com.cec.example.Delivery.netty.service.ProductionDemo;
public class DemoServer extends EasyNettyConfig {

	private static boolean isServer = false;
	private static String confPath = "config.properties";
	private static String log4jPath = "log4j.properties";

	/**
	 * 路由表定义
	 */
	@Override
	public void configRoute(Routes me) {
        //添加你的Controller列表
		me.add("/", JSONController.class);
		me.add("/json", JSONController.class);
		me.add("/paras", ParasController.class);
        me.add("/mdc", ModalDCController.class);
        me.add("/cb", CombinatorialDCController.class);
        me.add("/cp", ComposedDCController.class);
        me.add("/batch", BatchDCController.class);
        me.add("/r", ResourceTableController.class);
        me.add("/rd", ResourceDimensionTableController.class);
        me.add("/cap", CallAsProductionController.class);
	}

	/**
	 * 初始化配置文件
	 */
	@Override
	public void configProp(Config me) {
		me.loadFromConfFile(confPath, isServer);
		me.initLog4j(log4jPath, isServer);
	}
	
	/**
	 * 配置第三方插件
	 */
	@Override
	public void configPlugin(Config conf) {
        //初始化内存交付数据
        ProductionDemo.initInMemoryDelivery();
        //初始化监听调用的Flink任务
        ProductionDemo.initCallAsProduction();
    }
	
	/**
	 * 程序入口
	 * @param args
	 */
	public static void main(String [] args) {
		if(args.length > 0) {
			confPath = args[0];
			log4jPath = args[1];
			isServer = true;
		}
		new DemoServer().startup();
	}
}
