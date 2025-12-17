package com.cec.deliver.InnerMemory;

import com.cec.deliver.AbstractDataComponentDelivery;
import com.cec.deliver.DataComponentDelivery;
import com.cec.modeling.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 内置内存元件交付接口。
 * <p/>
 * 主要用于测试使用，无法用于生产。
 * @author koala
 * @see com.cec.deliver.DataComponentDelivery
 * @see com.cec.deliver.AbstractDataComponentDelivery
 */
public class DataComponentDeliveryInnerMemory extends AbstractDataComponentDelivery implements DataComponentDelivery {

    /**
     * 获取内置内存对象
     * @return 内置内存对象
     */
    public InnerMemoryDB getInnerMemoryDB() {
        return innerMemoryDB;
    }

    /**
     * 设置内置内存对象
     * @param innerMemoryDB 内置内存对象
     */
    public void setInnerMemoryDB(InnerMemoryDB innerMemoryDB) {
        this.innerMemoryDB = innerMemoryDB;
    }

    /**
     * 内置内存对象
     */
    private InnerMemoryDB innerMemoryDB;

    /**
     * 内置内存元件交付接口类构造函数
     */
    public DataComponentDeliveryInnerMemory() {
        this.innerMemoryDB = new InnerMemoryDB();
    }

    /**
     * 模态元件调用
     * @param dataComponentId 元件ID
     * @param mainKey 主体字段
     * @return 一个模态元件对象
     */
    @Override
    public ModalDataComponent getModalDataComponent(String dataComponentId, String mainKey) {
        List<DataCell> r = innerMemoryDB.getDataCells(dataComponentId + mainKey);
        if(r != null && !r.isEmpty()){
            ModalDataComponent  modalDataComponent = new ModalDataComponent();
            modalDataComponent.setValue(r.get(1));
            modalDataComponent.setMainKey(r.get(0));
            return modalDataComponent;
        } else return null;
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
        List<ComposedDataComponent>  composedDataComponents = new ArrayList<>();
        for(int i = index; i < index + size; i++) {
            ComposedDataComponent  composedDataComponent = new ComposedDataComponent();
            List<DataCell> r = innerMemoryDB.getDataCells(dataComponentId + i);
            composedDataComponent.setValues(r);
            composedDataComponents.add(composedDataComponent);
        }
        return composedDataComponents;
    }
    /**
     * 组合态元件调用
     * @param dataComponentId 元件ID
     * @param mainKey 主体字段
     * @return 一个组合态元件
     */
    @Override
    public CombinatorialDataComponent getCombinatorialDataComponentByMainKey(String dataComponentId, String mainKey) {
        List<DataCell> r = innerMemoryDB.getDataCells(dataComponentId + mainKey);
        if(r != null && !r.isEmpty()){
            CombinatorialDataComponent  combinatorialDataComponent = new CombinatorialDataComponent();
            combinatorialDataComponent.setValues(r);
            combinatorialDataComponent.setIndex(0);
            return combinatorialDataComponent;
        } else return null;
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
        List<DataComponent>  dataComponents = new ArrayList<>();
        for(int i = index; i < index + size; i++) {
            DataComponent  dataComponent = new DataComponent();
            List<DataCell> r = innerMemoryDB.getDataCells(dataComponentId + i);
            dataComponent.setValues(r);
            dataComponents.add(dataComponent);
        }
        return dataComponents;
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
    public void loadModalDataComponent(String modalDataComponentId, ModalDataComponent modalDataComponents) {
        String mainKey = (String) modalDataComponents.getMainKey().getValue();
        innerMemoryDB.putDataCell(modalDataComponentId + mainKey, modalDataComponents.getValues());
        innerMemoryDB.putDataCell(modalDataComponentId + 0, modalDataComponents.getValues());
    }
    /**
     * 加载组态元件接口
     * @param composedDataComponentId 组态元件ID
     * @param composedDataComponents 组态元件对象
     */
    public void loadComposedDataComponent(String composedDataComponentId, ComposedDataComponent composedDataComponents) {
        innerMemoryDB.putDataCell(composedDataComponentId + 0, composedDataComponents.getValues());
    }
    /**
     * 加载组合态元件接口
     * @param combinatorialDataComponentId 组合态元件ID
     * @param combinatorialDataComponents 组合态元件对象
     */
    public void loadCombinatorialDataComponent(String combinatorialDataComponentId, CombinatorialDataComponent combinatorialDataComponents) {
        String mainKey = (String) combinatorialDataComponents.getMainKey().getValue();
        innerMemoryDB.putDataCell(combinatorialDataComponentId + mainKey, combinatorialDataComponents.getValues());
        innerMemoryDB.putDataCell(combinatorialDataComponentId + 0, combinatorialDataComponents.getValues());
    }

}
