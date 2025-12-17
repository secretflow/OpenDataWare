package com.cec.deliver.netty.plugin;
import com.cec.deliver.InnerMemory.DataComponentDeliveryInnerMemory;
import com.cec.modeling.CombinatorialDataComponent;
import com.cec.modeling.ComposedDataComponent;
import com.cec.modeling.DataComponent;
import com.cec.modeling.ModalDataComponent;
import java.util.List;

/**
 * 元件交付存储内存版本的插件
 * @author koala
 */
public class InnerMemoryDeliveryPlugin {

    public static DataComponentDeliveryInnerMemory dataComponentDelivery = new DataComponentDeliveryInnerMemory();
    /**
     * 批量加载模态元件
     * @param modalDataComponentId 模态元件ID
     * @param modalDataComponents 模态元件对象列表
     */
    public static void loadModalDataComponents(String modalDataComponentId, List<ModalDataComponent> modalDataComponents) {
        dataComponentDelivery.loadModalDataComponents(modalDataComponentId, modalDataComponents);
    }
    /**
     * 批量加载组态元件接口
     * @param composedDataComponentId 组态元件ID
     * @param composedDataComponents 组态元件对象列表
     */
    public static void loadComposedDataComponents(String composedDataComponentId, List<ComposedDataComponent> composedDataComponents) {
        dataComponentDelivery.loadComposedDataComponents(composedDataComponentId, composedDataComponents);
    }
    /**
     * 批量加载组合态元件接口
     * @param combinatorialDataComponentId 组合态元件ID
     * @param combinatorialDataComponents 组合态元件对象列表
     */
    public static void loadCombinatorialDataComponents(String combinatorialDataComponentId, List<CombinatorialDataComponent> combinatorialDataComponents) {
        dataComponentDelivery.loadCombinatorialDataComponents(combinatorialDataComponentId, combinatorialDataComponents);
    }
    /**
     * 模态元件调用
     * @param dataComponentId 元件ID
     * @param mainKey 主体字段
     * @return 一个模态元件对象
     */
    public static ModalDataComponent getModalDataComponent(String dataComponentId, String mainKey){
        return dataComponentDelivery.getModalDataComponent(dataComponentId, mainKey);
    }
    /**
     * 组态元件调用
     * @param dataComponentId 元件ID
     * @param index 元件记录的起始索引
     * @param size 返回元件记录数量
     * @return 组态元件列表
     */
    public static List<ComposedDataComponent> getComposedDataComponent(String dataComponentId, int index, int size){
        return dataComponentDelivery.getComposedDataComponent(dataComponentId, index, size);
    }
    /**
     * 组合态元件调用
     * @param dataComponentId 元件ID
     * @param mainKey 主体字段
     * @return 一个组合态元件
     */
    public static CombinatorialDataComponent getCombinatorialDataComponentByMainKey(String dataComponentId, String mainKey){
        return dataComponentDelivery.getCombinatorialDataComponentByMainKey(dataComponentId, mainKey);
    }
    /**
     * 批量元件调用
     * @param dataComponentId 元件ID
     * @param index 元件记录的起始索引
     * @param size 返回元件记录数量
     * @return 元件列表
     */
    public static List<DataComponent> getDataComponentBath(String dataComponentId, int index, int size) {
        return dataComponentDelivery.getDataComponentBath(dataComponentId, index, size);
    }
}
