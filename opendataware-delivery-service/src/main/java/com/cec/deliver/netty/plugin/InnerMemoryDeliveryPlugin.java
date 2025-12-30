package com.cec.deliver.netty.plugin;
import com.cec.deliver.InnerMemory.DataWareDeliveryInnerMemory;
import com.cec.modeling.CombinatorialDataWare;
import com.cec.modeling.ComposedDataWare;
import com.cec.modeling.DataWare;
import com.cec.modeling.ModalDataWare;
import java.util.List;

/**
 * 元件交付存储内存版本的插件
 * @author koala
 */
public class InnerMemoryDeliveryPlugin {

    public static DataWareDeliveryInnerMemory DataWareDelivery = new DataWareDeliveryInnerMemory();
    /**
     * 批量加载模态元件
     * @param modalDataWareId 模态元件ID
     * @param modalDataWares 模态元件对象列表
     */
    public static void loadModalDataWares(String modalDataWareId, List<ModalDataWare> modalDataWares) {
        DataWareDelivery.loadModalDataWares(modalDataWareId, modalDataWares);
    }
    /**
     * 批量加载组态元件接口
     * @param composedDataWareId 组态元件ID
     * @param composedDataWares 组态元件对象列表
     */
    public static void loadComposedDataWares(String composedDataWareId, List<ComposedDataWare> composedDataWares) {
        DataWareDelivery.loadComposedDataWares(composedDataWareId, composedDataWares);
    }
    /**
     * 批量加载组合态元件接口
     * @param combinatorialDataWareId 组合态元件ID
     * @param combinatorialDataWares 组合态元件对象列表
     */
    public static void loadCombinatorialDataWares(String combinatorialDataWareId, List<CombinatorialDataWare> combinatorialDataWares) {
        DataWareDelivery.loadCombinatorialDataWares(combinatorialDataWareId, combinatorialDataWares);
    }
    /**
     * 模态元件调用
     * @param DataWareId 元件ID
     * @param mainKey 主体字段
     * @return 一个模态元件对象
     */
    public static ModalDataWare getModalDataWare(String DataWareId, String mainKey){
        return DataWareDelivery.getModalDataWare(DataWareId, mainKey);
    }
    /**
     * 组态元件调用
     * @param DataWareId 元件ID
     * @param index 元件记录的起始索引
     * @param size 返回元件记录数量
     * @return 组态元件列表
     */
    public static List<ComposedDataWare> getComposedDataWare(String DataWareId, int index, int size){
        return DataWareDelivery.getComposedDataWare(DataWareId, index, size);
    }
    /**
     * 组合态元件调用
     * @param DataWareId 元件ID
     * @param mainKey 主体字段
     * @return 一个组合态元件
     */
    public static CombinatorialDataWare getCombinatorialDataWareByMainKey(String DataWareId, String mainKey){
        return DataWareDelivery.getCombinatorialDataWareByMainKey(DataWareId, mainKey);
    }
    /**
     * 批量元件调用
     * @param DataWareId 元件ID
     * @param index 元件记录的起始索引
     * @param size 返回元件记录数量
     * @return 元件列表
     */
    public static List<DataWare> getDataWareBath(String DataWareId, int index, int size) {
        return DataWareDelivery.getDataWareBath(DataWareId, index, size);
    }
}
