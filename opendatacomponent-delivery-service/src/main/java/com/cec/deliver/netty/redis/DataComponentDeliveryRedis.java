package com.cec.deliver.netty.redis;

import com.cec.deliver.DataComponentDelivery;
import com.cec.modeling.*;

import java.util.List;
import java.util.Map;

/**
 * 元件交付接口类,Redis实现类
 * <p/>
 * 可用于生产，暂未实现后续版本实现。
 * @author koala
 * @see com.cec.deliver.DataComponentDelivery
 * @see com.cec.deliver.AbstractDataComponentDelivery
 */
public class DataComponentDeliveryRedis implements DataComponentDelivery {

    /**
     * Redis元件交付接口类构造函数
     */
    public DataComponentDeliveryRedis() {
    }
    /**
     * 模态元件调用
     * @param dataComponentId 元件ID
     * @param mainKey 主体字段
     * @return 一个模态元件对象
     */
    @Override
    public ModalDataComponent getModalDataComponent(String dataComponentId, String mainKey) {
        return null;
    }
    /**
     * 组态元件调用
     * @param dataComponentId 元件ID
     * @param index 元件记录的起始索引
     * @param size 返回元件记录数量
     * @return 组态元件列表
     */
    @Override
    public List<ComposedDataComponent> getComposedDataComponent(String dataComponentId, int index, int size) {
        return null;
    }
    /**
     * 组合态元件调用
     * @param dataComponentId 元件ID
     * @param mainKey 主体字段
     * @return 一个组合态元件
     */
    @Override
    public CombinatorialDataComponent getCombinatorialDataComponentByMainKey(String dataComponentId, String mainKey) {
        return null;
    }
    /**
     * 组合态元件调用
     * @param dataComponentId 元件ID
     * @param QueryColumn 查询字段键值对
     * @param index 元件记录的起始索引
     * @param size 返回元件记录数量
     * @return 组合态元件列表
     */
    @Override
    public List<CombinatorialDataComponent> getCombinatorialDataComponentByQueryColumns(String dataComponentId, Map<String, String> QueryColumn, int index, int size) {
        //内存版本的数据库仅仅是演示，按照列查询逻辑不实现。
        return null;
    }
    /**
     * 批量元件调用
     * @param dataComponentId 元件ID
     * @param index 元件记录的起始索引
     * @param size 返回元件记录数量
     * @return 元件列表
     */
    @Override
    public List<DataComponent> getDataComponentBath(String dataComponentId, int index, int size) {
        return null;
    }
    /**
     * 交付接口的配置方法
     * @param options 配置项
     */
    @Override
    public void configure(Map<String, String> options) {

    }
    /**
     * 加载模态元件接口
     * @param modalDataComponentId 模态元件ID
     * @param modalDataComponents 模态元件对象
     */
    @Override
    public void loadModalDataComponent(String modalDataComponentId, ModalDataComponent modalDataComponents) {

    }
    /**
     * 加载组态元件接口
     * @param composedDataComponentId 组态元件ID
     * @param composedDataComponents 组态元件对象
     */
    @Override
    public void loadComposedDataComponent(String composedDataComponentId, ComposedDataComponent composedDataComponents) {

    }
    /**
     * 加载组合态元件接口
     * @param combinatorialDataComponentId 组合态元件ID
     * @param combinatorialDataComponents 组合态元件对象
     */
    @Override
    public void loadCombinatorialDataComponent(String combinatorialDataComponentId, CombinatorialDataComponent combinatorialDataComponents) {

    }

}
