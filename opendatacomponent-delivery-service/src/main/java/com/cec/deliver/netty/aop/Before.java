package com.cec.deliver.netty.aop;

import java.lang.annotation.*;

/**
 * Before is used to configure Interceptor or Validator.
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Before {
	Class<? extends Interceptor>[] value();
}
