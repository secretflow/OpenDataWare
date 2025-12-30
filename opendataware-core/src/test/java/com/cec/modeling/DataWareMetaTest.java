package com.cec.modeling;

import com.cec.comm.Notice;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 数据元件业务元数据包装类测试
 * @author koala
 */
public class DataWareMetaTest {

    @Test
    public void testDataWareMetaGetNotion() {
        String targetValue = "dc001";
        Class<?> clazz = DataWareMeta.class;
        DataWareMeta DataWareMeta = new DataWareMeta();
        DataWareMeta.setComponentId(targetValue);
        // 获取所有声明的字段（包括private和protected）
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Notice.class)) {
                Notice annotation = field.getAnnotation(Notice.class);
                if("Definition".equals(annotation.stage()) && field.getName().equals("componentId")) {
                    field.setAccessible(true);
                    try {
                        assertEquals(targetValue, field.get(DataWareMeta));
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

    @Test
    public void testDataWareMetaSetNotion() {
        String targetValue = "dc001";
        Class<?> clazz = DataWareMeta.class;
        DataWareMeta DataWareMeta = new DataWareMeta();
        // 获取所有声明的字段（包括private和protected）
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Notice.class)) {
                Notice annotation = field.getAnnotation(Notice.class);
                if("Definition".equals(annotation.stage()) && field.getName().equals("componentId")) {
                    field.setAccessible(true);
                    try {
                        field.set(DataWareMeta, targetValue);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        assertEquals(targetValue, DataWareMeta.getComponentId());
    }
}