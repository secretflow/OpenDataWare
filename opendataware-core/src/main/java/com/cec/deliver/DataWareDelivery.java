package com.cec.deliver;

import com.cec.modeling.CombinatorialDataWare;
import com.cec.modeling.ComposedDataWare;
import com.cec.modeling.DataWare;
import com.cec.modeling.ModalDataWare;

import java.util.List;
import java.util.Map;

/**
 * 元件交付接口。
 * <p/>
 * 其他类型的元件交付接口可以实现该接口。
 * @author koala
 */
public interface DataWareDelivery {

    /**
     * 模态元件调用
     * @param DataWareId 元件ID
     * @param mainKey 主体字段
     * @return 一个模态元件对象
     */
    public ModalDataWare getModalDataWare(String DataWareId, String mainKey);
    /**
     * 组态元件调用
     * @param DataWareId 元件ID
     * @param index 元件记录的起始索引
     * @param size 返回元件记录数量
     * @return 组态元件列表
     */
    public List<ComposedDataWare> getComposedDataWare(String DataWareId, int index, int size);
    /**
     * 组合态元件调用
     * @param DataWareId 元件ID
     * @param mainKey 主体字段
     * @return 一个组合态元件
     */
    public CombinatorialDataWare getCombinatorialDataWareByMainKey(String DataWareId, String mainKey);

    /**
     * 组合态元件调用
     * @param DataWareId 元件ID
     * @param QueryColumn 查询字段键值对
     * @param index 元件记录的起始索引
     * @param size 返回元件记录数量
     * @return 组合态元件列表
     */
    public List<CombinatorialDataWare> getCombinatorialDataWareByQueryColumns(String DataWareId, Map<String,String> QueryColumn, int index, int size);
    /**
     * 批量元件调用
     * @param DataWareId 元件ID
     * @param index 元件记录的起始索引
     * @param size 返回元件记录数量
     * @return 元件列表
     */
    public List<DataWare> getDataWareBath(String DataWareId, int index, int size);

    /**
     * 交付接口的配置方法
     * @param options 配置项
     */
    public void configure(Map<String,String> options);

    /**
     * 加载模态元件接口
     * @param modalDataWareId 模态元件ID
     * @param modalDataWares 模态元件对象
     */
    public void loadModalDataWare(String modalDataWareId, ModalDataWare modalDataWares);

    /**
     * 加载组态元件接口
     * @param composedDataWareId 组态元件ID
     * @param composedDataWares 组态元件对象
     */
    public void loadComposedDataWare(String composedDataWareId, ComposedDataWare composedDataWares);

    /**
     * 加载组合态元件接口
     * @param combinatorialDataWareId 组合态元件ID
     * @param combinatorialDataWares 组合态元件对象
     */
    public void loadCombinatorialDataWare(String combinatorialDataWareId, CombinatorialDataWare combinatorialDataWares);
}
