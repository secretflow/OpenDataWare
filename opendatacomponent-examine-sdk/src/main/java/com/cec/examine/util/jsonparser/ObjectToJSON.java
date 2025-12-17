package com.cec.examine.util.jsonparser;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ObjectToJSON {
    private static char objectBegin = '{';
    private static char objectEnd = '}';
    private static char delimiter = ',';
    private static char arrayBegin = '[';
    private static char arrayEnd = ']';
    private static char quote = '"';
    private static char colon = ':'; // 冒号

    private static boolean isPrimitive(Class<?> clazz) {
        return clazz.isPrimitive() || clazz == Boolean.class || clazz == Character.class || clazz == Byte.class || clazz == Short.class || clazz == Integer.class || clazz == Long.class || clazz == Float.class || clazz == Double.class || clazz == BigInteger.class || clazz == java.util.Date.class || clazz == Date.class || clazz == Time.class || clazz == Timestamp.class || clazz.isEnum();
    }

    //获取类及父类参数
    private static void parserAllFieldToCache(Class<?> clazz, Map<String, Field> fieldCacheMap) {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (Modifier.isStatic(field.getModifiers())) {
                //排除静态变量
                continue;
            }
            String fieldName = field.getName();
            if (!fieldCacheMap.containsKey(fieldName)) {
                fieldCacheMap.put(fieldName, field);
            }
        }
        if (clazz.getSuperclass() != null && clazz.getSuperclass() != Object.class) {
            parserAllFieldToCache(clazz.getSuperclass(), fieldCacheMap);
        }
    }

    public static String toJSONString(Object object) {
        return String.valueOf(toJSON(object));
    }

    public static Object toJSON(Object o) {
        try {
            if (o == null) {
                return null;
            }
            if (o instanceof String) {
                return o;
            }
            Class<?> clazz = o.getClass();
            //八大原始类型原始类型封装类
            if (isPrimitive(clazz)) {
                return o;
            }
            StringBuilder sb = new StringBuilder();
            if (o instanceof Map) {
                sb.append(objectBegin);
                Map<Object, Object> map = (Map) o;
                Set<Map.Entry<Object, Object>> mapSet = map.entrySet();
                for (Map.Entry<Object, Object> entry : mapSet) {
                    Object key = entry.getKey();
                    Object mapValue = entry.getValue();
                    if (mapValue != null) {
                        Object value = toJSON(mapValue);
                        if (mapValue instanceof String) {
                            sb.append(quote).append(key).append(quote).append(colon).append(quote)
                                    .append(value).append(quote).append(delimiter);
                        } else {
                            sb.append(quote).append(key).append(quote).append(colon)
                                    .append(value).append(delimiter);
                        }
                    }
                }
                if (sb.length() >= 1 && sb.charAt(sb.length() - 1) == delimiter) {
                    sb.deleteCharAt(sb.length() - 1);
                }
                sb.append(objectEnd);
            } else if (o instanceof Collection) {
                sb.append(arrayBegin);
                Collection collection = (Collection) o;
                for (Object item : collection) {
                    if (item != null) {
                        Object value = toJSON(item);
                        if (item instanceof String) {
                            sb.append(quote).append(value).append(quote).append(delimiter);
                        } else {
                            sb.append(value).append(delimiter);
                        }
                    }
                }
                if (sb.length() >= 1 && sb.charAt(sb.length() - 1) == delimiter) {
                    sb.deleteCharAt(sb.length() - 1);
                }
                sb.append(arrayEnd);
            } else {
                //自定义POJO或其他类
                sb.append(objectBegin);
                Map<String, Field> fieldCacheMap = new HashMap<String, Field>();
                parserAllFieldToCache(clazz, fieldCacheMap);
                Set<Map.Entry<String, Field>> fieldSet = fieldCacheMap.entrySet();
                for (Map.Entry<String, Field> fieldMap : fieldSet) {
                    String fieldName = fieldMap.getKey();
                    Field field = fieldMap.getValue();
                    //private属性
                    field.setAccessible(true);
                    Object fieldValue = field.get(o);
                    if (fieldValue != null) {
                        Object value = toJSON(fieldValue);
                        if (fieldValue instanceof String) {
                            sb.append(quote)
                                    .append(fieldName).append(quote).append(colon).append(quote)
                                    .append(value).append(quote).append(delimiter);
                        } else {
                            sb.append(quote)
                                    .append(fieldName).append(quote).append(colon)
                                    .append(value).append(delimiter);
                        }
                    }
                }
                if (sb.length() >= 1 && sb.charAt(sb.length() - 1) == delimiter) {
                    sb.deleteCharAt(sb.length() - 1);
                }
                sb.append(objectEnd);
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}