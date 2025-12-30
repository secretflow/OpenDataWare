package com.cec.deliver.netty.aop;

public interface Interceptor {
	void intercept(ActionInvocation ai);
}
