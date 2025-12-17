package com.cec.examine.util;

import com.cec.examine.desensitization.SensitivePatternRegex;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import static com.cec.examine.util.ImplementationFinder.findImplementationsOfAbstractClass;

public class DesensitiveAllinOneUtil {
	public static String desensitiveAllInOne( String text ) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {

		//依次脱敏
		String packageName = "com.cec.examine.desensitization.mask"; // Replace with your package name
		Class<?> abstractClass = SensitivePatternRegex.class; // Replace with your abstract class name

		List<Class<?>> implementations = findImplementationsOfAbstractClass(packageName, abstractClass);

		System.out.println("Implementations of " + abstractClass.getName() + ":");
		for (Class<?> implementation : implementations) {
//			text = (String) ReflectionUtils.invokeMethod(implementation, "desensitive", String.class.getClasses(), text) ;
			Method method = implementation.getSuperclass().getDeclaredMethod("desensitive",String.class);
			method.setAccessible(true);
			SensitivePatternRegex instance = (SensitivePatternRegex) implementation.newInstance();
			text = (String)method.invoke(instance,text);
		}


		return text;
	}

	public static void main(String[] args) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, InstantiationException {
		String text = "{\"phone\":  \"13029380032\" , \"name\":\"周杰伦\", \"content\":\"13029380032@刘老根 @老根 我的邮箱是：ztz@youdao.com ，身份证号：130202194505309921\", \"ID\":\"130202194505309921\"} ";
		System.out.println(desensitiveAllInOne(text));
	}
	

}
