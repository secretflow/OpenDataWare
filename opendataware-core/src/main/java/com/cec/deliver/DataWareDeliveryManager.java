package com.cec.deliver;
import java.util.HashMap;
import java.util.Map;

/**
 * 元件交付接口类管理类
 * @author koala
 */
public class DataWareDeliveryManager {

    public static Map<String, DataWareDelivery> DataWareDeliveryMap = new HashMap<String, DataWareDelivery>();

    /**
     * 获取数据库对象
     * @param dataBaseName 数据库名称
     * @return 元件交付接口实现对象
     */
    public static DataWareDelivery get(String dataBaseName) {
        return DataWareDeliveryMap.get(dataBaseName);
    }

    /**
     * 获取，如果不存在则创建
     * @param dataBaseName 数据库名称
     */
    public static DataWareDelivery getOrCreateDataBase(String dataBaseName, String deliveryClassName, Map<String, String> dcDeliveryOptions) {
        DataWareDelivery DataWareDelivery = get(dataBaseName);
        if (DataWareDelivery == null) {
            try {
                Class<?> clazz1 = Class.forName(deliveryClassName);
                DataWareDelivery = (DataWareDelivery) clazz1.newInstance();
                DataWareDelivery.configure(dcDeliveryOptions);
                DataWareDeliveryMap.put(dataBaseName, DataWareDelivery);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return DataWareDelivery;
    }
}
