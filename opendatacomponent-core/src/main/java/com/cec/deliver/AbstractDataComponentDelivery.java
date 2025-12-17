package com.cec.deliver;

import com.cec.modeling.CombinatorialDataComponent;
import com.cec.modeling.ComposedDataComponent;
import com.cec.modeling.ModalDataComponent;

import java.util.List;

/**
 * 元件交付抽象类
 * @author koala
 */
public abstract class AbstractDataComponentDelivery implements DataComponentDelivery {

    /**
     * 批量加载模态元件
     * @param modalDataComponentId 模态元件ID
     * @param modalDataComponents 模态元件对象列表
     */
    public void loadModalDataComponents(String modalDataComponentId, List<ModalDataComponent> modalDataComponents){
        for(ModalDataComponent modalDataComponent : modalDataComponents){
            loadModalDataComponent(modalDataComponentId, modalDataComponent);
        }
    }
    /**
     * 批量加载组态元件接口
     * @param composedDataComponentId 组态元件ID
     * @param composedDataComponents 组态元件对象列表
     */
    public void loadComposedDataComponents(String composedDataComponentId, List<ComposedDataComponent> composedDataComponents) {
        for(ComposedDataComponent composedDataComponent : composedDataComponents){
            loadComposedDataComponent(composedDataComponentId, composedDataComponent);
        }
    }
    /**
     * 批量加载组合态元件接口
     * @param combinatorialDataComponentId 组合态元件ID
     * @param combinatorialDataComponents 组合态元件对象列表
     */
    public void loadCombinatorialDataComponents(String combinatorialDataComponentId, List<CombinatorialDataComponent> combinatorialDataComponents) {
        for(CombinatorialDataComponent combinatorialDataComponent : combinatorialDataComponents){
            loadCombinatorialDataComponent(combinatorialDataComponentId, combinatorialDataComponent);
        }
    }
}
