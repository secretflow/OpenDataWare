package com.cec.example.DataComponentStandard;

import com.cec.comm.Notice;
import com.cec.examine.util.jsonparser.JSONParser;
import com.cec.examine.util.jsonparser.model.JsonObject;
import com.cec.modeling.DataComponent;
import com.cec.modeling.DataComponentMeta;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

public class DataComponentMetaExample  extends DCExample {

    public DataComponentMetaExample(String resourceFileName) {
        super(resourceFileName);
    }

    @Override
    public List<DataComponent> getDataComponents() {
        return null;
    }
    //获取定义阶段的元件元信息
    public DataComponentMeta getDataComponentDefinitionMeta() throws IOException {
        DataComponentMeta dataComponentMeta = new DataComponentMeta();
        JSONParser jsonParser = new JSONParser();
        JsonObject jsonObject = (JsonObject)jsonParser.fromJSON(this.fileContent);
        //遍历DCMeta类的所有字段，找到定义阶段的字段
        Class<?> clazz = DataComponentMeta.class;
        // 获取所有声明的字段（包括private和protected）
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Notice.class)) {
                Notice annotation = field.getAnnotation(Notice.class);
                if("Definition".equals(annotation.stage())) {
                    //如果是定义阶段的字段，那么可以赋值
                    field.setAccessible(true);
                    try {
                        field.set(dataComponentMeta, jsonObject.get(field.getName()));
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return dataComponentMeta;
    }
    //获取生产阶段的元件元信息
    public DataComponentMeta getDataComponentProductionMeta() throws IOException {
        DataComponentMeta dataComponentMeta = new DataComponentMeta();
        JSONParser jsonParser = new JSONParser();
        JsonObject jsonObject = (JsonObject)jsonParser.fromJSON(this.fileContent);
        //遍历DCMeta类的所有字段，找到定义阶段的字段
        Class<?> clazz = DataComponentMeta.class;
        // 获取所有声明的字段（包括private和protected）
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Notice.class)) {
                field.setAccessible(true);
                try {
                    field.set(dataComponentMeta, jsonObject.get(field.getName()));
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return dataComponentMeta;
    }
}
