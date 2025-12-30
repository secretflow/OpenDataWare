//package com.cec.examine.util;
//
//import org.springframework.beans.BeansException;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.ApplicationContextAware;
//import org.springframework.stereotype.Component;
//
//import java.util.Iterator;
//import java.util.Map;
//
//@Component
//public class SupportFactoryUtils implements ApplicationContextAware {
//    public static ApplicationContext applicationContext;
//
//    public SupportFactoryUtils() {
//    }
//
//    public static <T extends BeanSupport> T get(String supportType, Class<T> cls) {
//        Map<String, T> beanMap = applicationContext.getBeansOfType(cls);
//        Iterator i$ = beanMap.values().iterator();
//
//        BeanSupport t;
//        do {
//            if (!i$.hasNext()) {
//                return null;
//            }
//
//            t = (BeanSupport)i$.next();
//        } while(!t.support(supportType));
//
//        return t;
//    }
//
//    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
//        SupportFactoryUtils.applicationContext = applicationContext;
//    }
//}
//
