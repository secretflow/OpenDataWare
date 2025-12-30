package com.cec.deliver.netty.core;

import com.cec.deliver.netty.config.*;
import io.netty.handler.codec.http.FullHttpRequest;

import java.util.Map;
/**
 * EasyNettyConfig.
 * <p>
 * Config order: configConstant(), configRoute(), configPlugin(), configInterceptor(), configHandler()
 */
public abstract class EasyNettyConfig {
	
	private Routes routes = new Routes();
	private Config conf = new Config();

	/**
	 * 初始化配置文件
	 * @param
	 */
	public abstract void configProp(Config config);

	/**
	 * 初始化路由器
	 * @param
	 */
	public abstract void configRoute(Routes routes);
	
	/**
	 * 利用配置去初始化第三方的插件
	 * @param conf
	 */
	public abstract void configPlugin(final Config conf);

	private void initProp() {
		conf.set(Constants.C_S_PORT, Constants.S_PORT);
		conf.set(Constants.C_S_BOSS_GROUP_EVENT_LOOPS, Constants.S_BOSS_GROUP_EVENT_LOOPS);
		conf.set(Constants.C_S_WORKER_GROUP_EVENT_LOOPS, Constants.S_WORKER_GROUP_EVENT_LOOPS);
		conf.set(Constants.C_S_POJO_QUEUE_SIZE, Constants.S_POJO_QUEUE_SIZE);
		conf.set(Constants.C_S_POJO_QUEUE_WORKERS, Constants.S_POJO_QUEUE_WORKERS);
		conf.set(Constants.C_D_NOT_FOUND, Constants.D_NOT_FOUND);
		conf.set(Constants.C_D_BAD_REQUEST, Constants.D_BAD_REQUEST);
		conf.set(Constants.C_D_INTERNAL_SERVER_ERROR, Constants.D_INTERNAL_SERVER_ERROR);
	}
	
	/**
	 * 决定了初始化顺序
	 */
	public void startup() {
		initProp();// 初始化配置文件
		configProp(conf); // 可以覆盖配置
		configRoute(routes);
		configPlugin(conf);
		// TODO override default configuration
		new HttpServer(this).run();
	}

	public String getProp(String name) {
		return conf.get(name);
	}
	
	public Integer getPropInt(String name) {
		return conf.getInt(name);
	}
	
	private Controller getController(String uri) {
		Class<? extends Controller> clazz = routes.getControllerClass(uri);
		Controller controller = null;
		if (clazz != null) {
			try {
				controller = clazz.newInstance();
				controller.setConfig(this);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return controller;
	}
	
	private Controller getDefaultController() {
		Controller c = new DefaultController();
		c.setConfig(this);
		return c;
	}

	/**
	 * 处理请求
	 * @param
	 * @return
	 */
	public Controller process(String path, Map<String, String> paras, String body, FullHttpRequest request) {
		Controller c = getController(path);
		if (c == null) {
			// 没找到
			c = getDefaultController();
			c.render404();
		} else if (paras == null || body == null) {
			// 请求有问题
			c.render400();
		} else {
			try {
				c.processWithAnnotation(paras, body, request);
			} catch (Exception e) {
				e.printStackTrace();
				c.render500();
			}
		}
		return c;
	}
}
