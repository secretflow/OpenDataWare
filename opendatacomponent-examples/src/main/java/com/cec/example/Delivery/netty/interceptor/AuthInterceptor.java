package com.cec.example.Delivery.netty.interceptor;

import com.cec.deliver.netty.aop.ActionInvocation;
import com.cec.deliver.netty.aop.Interceptor;

public class AuthInterceptor implements Interceptor{

	public void intercept(ActionInvocation ai) {
		System.out.println("this is an intercept.");
		ai.invoke();
	}
}
