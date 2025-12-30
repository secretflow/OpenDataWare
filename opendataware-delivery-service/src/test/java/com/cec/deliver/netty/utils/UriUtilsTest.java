package com.cec.deliver.netty.utils;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 用于解析url中的页面路由，query String等参数
 * @author koala
 */
public class UriUtilsTest {

    @Test
    public void testUriUtils() {
        Map<String,String> map = UriUtils.getPara("http://localhost:8080/a?p1=1&p2=2");
        assertEquals("1", map.get("p1"));
        assertEquals("2", map.get("p2"));
        assertEquals("http://localhost:8080/a", map.get("__prv"));
    }
}
