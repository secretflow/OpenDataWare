package com.cec.deliver.netty.utils;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * 用于解析url中的页面路由，query String等参数
 * @author koala
 */
public class UriUtils {

    /**
     * 解析参数
     * @param uri 资源路径标识
     * @return 请求参数
     */
	public static Map<String,String> getPara(String uri) {
		
		if(uri == null) return null;
		
		if('/' == uri.charAt(uri.length()-1) && uri.length() > 1) 
			uri = uri.substring(0, uri.length() - 1 );// 去掉最后字符是/的
		
		Map<String,String> paras = new HashMap<String,String>();
		
		int index = 0;
		if( (index = uri.indexOf("?")) >= 0 ) {
			paras.put("__prv", uri.substring(0, index));
			uri = uri.substring(index+1);
			String [] para = uri.split("&");
			for(String p: para) {
				String [] t = p.split("=",-1);
				try {
					if(t.length > 1)
						t[1] = java.net.URLDecoder.decode(t[1], "utf-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				if(t.length == 2) paras.put(t[0], t[1]);
			}
		} else {
			paras.put("__prv", uri);
		}
		return paras;
	}
}
