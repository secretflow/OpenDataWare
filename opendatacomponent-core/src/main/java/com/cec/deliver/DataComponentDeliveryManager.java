package com.cec.deliver;
import java.util.HashMap;
import java.util.Map;

/**
 * 元件交付接口类管理类
 * @author koala
 */
public class DataComponentDeliveryManager {

    public static Map<String, DataComponentDelivery> dataComponentDeliveryMap = new HashMap<String, DataComponentDelivery>();

    /**
     * 获取数据库对象
     * @param dataBaseName 数据库名称
     * @return 元件交付接口实现对象
     */
    public static DataComponentDelivery get(String dataBaseName) {
        return dataComponentDeliveryMap.get(dataBaseName);
    }

    /**
     * 获取，如果不存在则创建
     * @param dataBaseName 数据库名称
     */
    public static DataComponentDelivery getOrCreateDataBase(String dataBaseName, String deliveryClassName, Map<String, String> dcDeliveryOptions) {
        DataComponentDelivery dataComponentDelivery = get(dataBaseName);
        if (dataComponentDelivery == null) {
            try {
                Class<?> clazz1 = Class.forName(deliveryClassName);
                dataComponentDelivery = (DataComponentDelivery) clazz1.newInstance();
                dataComponentDelivery.configure(dcDeliveryOptions);
                dataComponentDeliveryMap.put(dataBaseName, dataComponentDelivery);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return dataComponentDelivery;
    }
}
