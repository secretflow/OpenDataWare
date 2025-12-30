package com.cec.example.DataWareStandard;

import com.cec.comm.Notice;
import com.cec.examine.util.jsonparser.JSONParser;
import com.cec.examine.util.jsonparser.model.JsonObject;
import com.cec.modeling.DataWare;
import com.cec.modeling.DataWareMeta;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

public class DataWareMetaExample  extends DCExample {

    public DataWareMetaExample(String resourceFileName) {
        super(resourceFileName);
    }

    @Override
    public List<DataWare> getDataWares() {
        return null;
    }
    //获取定义阶段的元件元信息
    public DataWareMeta getDataWareDefinitionMeta() throws IOException {
        DataWareMeta DataWareMeta = new DataWareMeta();
        JSONParser jsonParser = new JSONParser();
        JsonObject jsonObject = (JsonObject)jsonParser.fromJSON(this.fileContent);
        //遍历DCMeta类的所有字段，找到定义阶段的字段
        Class<?> clazz = DataWareMeta.class;
        // 获取所有声明的字段（包括private和protected）
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Notice.class)) {
                Notice annotation = field.getAnnotation(Notice.class);
                if("Definition".equals(annotation.stage())) {
                    //如果是定义阶段的字段，那么可以赋值
                    field.setAccessible(true);
                    try {
                        field.set(DataWareMeta, jsonObject.get(field.getName()));
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return DataWareMeta;
    }
    //获取生产阶段的元件元信息
    public DataWareMeta getDataWareProductionMeta() throws IOException {
        DataWareMeta DataWareMeta = new DataWareMeta();
        JSONParser jsonParser = new JSONParser();
        JsonObject jsonObject = (JsonObject)jsonParser.fromJSON(this.fileContent);
        //遍历DCMeta类的所有字段，找到定义阶段的字段
        Class<?> clazz = DataWareMeta.class;
        // 获取所有声明的字段（包括private和protected）
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Notice.class)) {
                field.setAccessible(true);
                try {
                    field.set(DataWareMeta, jsonObject.get(field.getName()));
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return DataWareMeta;
    }
}
