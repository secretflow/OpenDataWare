package com.cec.deliver;

import com.cec.modeling.CombinatorialDataComponent;
import com.cec.modeling.ComposedDataComponent;
import com.cec.modeling.DataComponent;
import com.cec.modeling.ModalDataComponent;

import java.util.List;
import java.util.Map;

/**
 * 元件交付接口。
 * <p/>
 * 其他类型的元件交付接口可以实现该接口。
 * @author koala
 */
public interface DataComponentDelivery {

    /**
     * 模态元件调用
     * @param dataComponentId 元件ID
     * @param mainKey 主体字段
     * @return 一个模态元件对象
     */
    public ModalDataComponent getModalDataComponent(String dataComponentId, String mainKey);
    /**
     * 组态元件调用
     * @param dataComponentId 元件ID
     * @param index 元件记录的起始索引
     * @param size 返回元件记录数量
     * @return 组态元件列表
     */
    public List<ComposedDataComponent> getComposedDataComponent(String dataComponentId, int index, int size);
    /**
     * 组合态元件调用
     * @param dataComponentId 元件ID
     * @param mainKey 主体字段
     * @return 一个组合态元件
     */
    public CombinatorialDataComponent getCombinatorialDataComponentByMainKey(String dataComponentId, String mainKey);

    /**
     * 组合态元件调用
     * @param dataComponentId 元件ID
     * @param QueryColumn 查询字段键值对
     * @param index 元件记录的起始索引
     * @param size 返回元件记录数量
     * @return 组合态元件列表
     */
    public List<CombinatorialDataComponent> getCombinatorialDataComponentByQueryColumns(String dataComponentId, Map<String,String> QueryColumn, int index, int size);
    /**
     * 批量元件调用
     * @param dataComponentId 元件ID
     * @param index 元件记录的起始索引
     * @param size 返回元件记录数量
     * @return 元件列表
     */
    public List<DataComponent> getDataComponentBath(String dataComponentId, int index, int size);

    /**
     * 交付接口的配置方法
     * @param options 配置项
     */
    public void configure(Map<String,String> options);

    /**
     * 加载模态元件接口
     * @param modalDataComponentId 模态元件ID
     * @param modalDataComponents 模态元件对象
     */
    public void loadModalDataComponent(String modalDataComponentId, ModalDataComponent modalDataComponents);

    /**
     * 加载组态元件接口
     * @param composedDataComponentId 组态元件ID
     * @param composedDataComponents 组态元件对象
     */
    public void loadComposedDataComponent(String composedDataComponentId, ComposedDataComponent composedDataComponents);

    /**
     * 加载组合态元件接口
     * @param combinatorialDataComponentId 组合态元件ID
     * @param combinatorialDataComponents 组合态元件对象
     */
    public void loadCombinatorialDataComponent(String combinatorialDataComponentId, CombinatorialDataComponent combinatorialDataComponents);
}
