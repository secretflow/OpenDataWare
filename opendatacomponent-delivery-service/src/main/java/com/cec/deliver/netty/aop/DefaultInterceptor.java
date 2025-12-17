package com.cec.deliver.netty.aop;

public class DefaultInterceptor implements Interceptor {

	public void intercept(ActionInvocation ai) {
		ai.invoke();
	}

}
