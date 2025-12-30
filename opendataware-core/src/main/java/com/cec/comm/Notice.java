package com.cec.comm;

import java.lang.annotation.*;

/**
 * 定义元件元数据的注释注解。
 * @author koala
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Notice {
    /**
     * @return 中文名称
     */
    String name();
    /**
     * @return 中文描述说明
     */
    String description() default "";
    /**
     * @return 元件所处的阶段
     */
    String stage() default "";
}