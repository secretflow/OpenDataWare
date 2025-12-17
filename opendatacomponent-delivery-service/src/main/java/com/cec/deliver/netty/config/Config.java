package com.cec.deliver.netty.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
/*
 * NettyProxyConf
 * @author qiuwenyi
 */
public class Config {
	private Properties prop = new Properties();
	
	public void loadFromConfFile(String fn, boolean isServer) {
		try {
			fn = isServer ? fn : System.getProperty("user.dir") + "/src/main/resources/" + fn;
			File file = new File(fn);
			if (file.exists() && file.isFile()) {
				InputStream in = new FileInputStream(fn);
				prop.load(in);
			} else {
				System.err.println("config file " + fn + " not avalible.");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void initLog4j(String fn, boolean isServer) {
		fn = isServer ? fn : System.getProperty("user.dir") + "/src/main/resources/" + fn;
		try {
			//PropertyConfigurator.configure(fn);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String get(String key) {
		return prop.getProperty(key);
	}
	
	public String get(String key, String defaultName) {
		return prop.getProperty(key,defaultName);
	}
	
	public int getInt(String key) {
		return Integer.parseInt(prop.getProperty(key));
	}
	
	public int getInt(String key, String defaultName) {
		return Integer.parseInt(prop.getProperty(key,defaultName));
	}
	
	public void set(String key, Object value) {
		prop.setProperty(key, String.valueOf(value));
	}
}
