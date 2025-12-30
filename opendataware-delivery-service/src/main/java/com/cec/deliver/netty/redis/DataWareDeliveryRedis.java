package com.cec.deliver.netty.redis;

import com.cec.deliver.DataWareDelivery;
import com.cec.modeling.*;

import java.util.List;
import java.util.Map;

/**
 * 元件交付接口类,Redis实现类
 * <p/>
 * 可用于生产，暂未实现后续版本实现。
 * @author koala
 * @see com.cec.deliver.DataWareDelivery
 * @see com.cec.deliver.AbstractDataWareDelivery
 */
public class DataWareDeliveryRedis implements DataWareDelivery {

    /**
     * Redis元件交付接口类构造函数
     */
    public DataWareDeliveryRedis() {
    }
    /**
     * 模态元件调用
     * @param DataWareId 元件ID
     * @param mainKey 主体字段
     * @return 一个模态元件对象
     */
    @Override
    public ModalDataWare getModalDataWare(String DataWareId, String mainKey) {
        return null;
    }
    /**
     * 组态元件调用
     * @param DataWareId 元件ID
     * @param index 元件记录的起始索引
     * @param size 返回元件记录数量
     * @return 组态元件列表
     */
    @Override
    public List<ComposedDataWare> getComposedDataWare(String DataWareId, int index, int size) {
        return null;
    }
    /**
     * 组合态元件调用
     * @param DataWareId 元件ID
     * @param mainKey 主体字段
     * @return 一个组合态元件
     */
    @Override
    public CombinatorialDataWare getCombinatorialDataWareByMainKey(String DataWareId, String mainKey) {
        return null;
    }
    /**
     * 组合态元件调用
     * @param DataWareId 元件ID
     * @param QueryColumn 查询字段键值对
     * @param index 元件记录的起始索引
     * @param size 返回元件记录数量
     * @return 组合态元件列表
     */
    @Override
    public List<CombinatorialDataWare> getCombinatorialDataWareByQueryColumns(String DataWareId, Map<String, String> QueryColumn, int index, int size) {
        //内存版本的数据库仅仅是演示，按照列查询逻辑不实现。
        return null;
    }
    /**
     * 批量元件调用
     * @param DataWareId 元件ID
     * @param index 元件记录的起始索引
     * @param size 返回元件记录数量
     * @return 元件列表
     */
    @Override
    public List<DataWare> getDataWareBath(String DataWareId, int index, int size) {
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
     * @param modalDataWareId 模态元件ID
     * @param modalDataWares 模态元件对象
     */
    @Override
    public void loadModalDataWare(String modalDataWareId, ModalDataWare modalDataWares) {

    }
    /**
     * 加载组态元件接口
     * @param composedDataWareId 组态元件ID
     * @param composedDataWares 组态元件对象
     */
    @Override
    public void loadComposedDataWare(String composedDataWareId, ComposedDataWare composedDataWares) {

    }
    /**
     * 加载组合态元件接口
     * @param combinatorialDataWareId 组合态元件ID
     * @param combinatorialDataWares 组合态元件对象
     */
    @Override
    public void loadCombinatorialDataWare(String combinatorialDataWareId, CombinatorialDataWare combinatorialDataWares) {

    }

}
