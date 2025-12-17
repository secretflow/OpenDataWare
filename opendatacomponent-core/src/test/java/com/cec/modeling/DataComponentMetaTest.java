package com.cec.modeling;

import com.cec.comm.Notice;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 数据元件业务元数据包装类测试
 * @author koala
 */
public class DataComponentMetaTest {

    @Test
    public void testDataComponentMetaGetNotion() {
        String targetValue = "dc001";
        Class<?> clazz = DataComponentMeta.class;
        DataComponentMeta dataComponentMeta = new DataComponentMeta();
        dataComponentMeta.setComponentId(targetValue);
        // 获取所有声明的字段（包括private和protected）
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Notice.class)) {
                Notice annotation = field.getAnnotation(Notice.class);
                if("Definition".equals(annotation.stage()) && field.getName().equals("componentId")) {
                    field.setAccessible(true);
                    try {
                        assertEquals(targetValue, field.get(dataComponentMeta));
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

    @Test
    public void testDataComponentMetaSetNotion() {
        String targetValue = "dc001";
        Class<?> clazz = DataComponentMeta.class;
        DataComponentMeta dataComponentMeta = new DataComponentMeta();
        // 获取所有声明的字段（包括private和protected）
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Notice.class)) {
                Notice annotation = field.getAnnotation(Notice.class);
                if("Definition".equals(annotation.stage()) && field.getName().equals("componentId")) {
                    field.setAccessible(true);
                    try {
                        field.set(dataComponentMeta, targetValue);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        assertEquals(targetValue, dataComponentMeta.getComponentId());
    }
}