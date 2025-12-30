package com.cec.deliver;

import com.cec.modeling.CombinatorialDataWare;
import com.cec.modeling.ComposedDataWare;
import com.cec.modeling.ModalDataWare;

import java.util.List;

/**
 * 元件交付抽象类
 * @author koala
 */
public abstract class AbstractDataWareDelivery implements DataWareDelivery {

    /**
     * 批量加载模态元件
     * @param modalDataWareId 模态元件ID
     * @param modalDataWares 模态元件对象列表
     */
    public void loadModalDataWares(String modalDataWareId, List<ModalDataWare> modalDataWares){
        for(ModalDataWare modalDataWare : modalDataWares){
            loadModalDataWare(modalDataWareId, modalDataWare);
        }
    }
    /**
     * 批量加载组态元件接口
     * @param composedDataWareId 组态元件ID
     * @param composedDataWares 组态元件对象列表
     */
    public void loadComposedDataWares(String composedDataWareId, List<ComposedDataWare> composedDataWares) {
        for(ComposedDataWare composedDataWare : composedDataWares){
            loadComposedDataWare(composedDataWareId, composedDataWare);
        }
    }
    /**
     * 批量加载组合态元件接口
     * @param combinatorialDataWareId 组合态元件ID
     * @param combinatorialDataWares 组合态元件对象列表
     */
    public void loadCombinatorialDataWares(String combinatorialDataWareId, List<CombinatorialDataWare> combinatorialDataWares) {
        for(CombinatorialDataWare combinatorialDataWare : combinatorialDataWares){
            loadCombinatorialDataWare(combinatorialDataWareId, combinatorialDataWare);
        }
    }
}
