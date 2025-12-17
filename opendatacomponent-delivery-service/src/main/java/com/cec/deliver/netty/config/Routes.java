package com.cec.deliver.netty.config;

import com.cec.deliver.netty.core.Controller;

import java.util.HashMap;
import java.util.Map;

public class Routes {
	
	private final Map<String, Class<? extends Controller> > ControllerMap = new HashMap<String, Class<? extends Controller>>();
	
	/**
	 * 添加成功返回1， 不成功返回0
	 * @param uri
	 * @param controller
	 * @return
	 */
	public int add(String uri, Class<? extends Controller> controller) {
		int c = 0;
		if(!ControllerMap.containsKey(uri)) {
			c++;
			ControllerMap.put(uri, controller);
		}
		return c;
	}

	public Class<? extends Controller> getControllerClass(String uri) {
		return ControllerMap.get(uri);
	}

}
