package com.cec.deliver.InnerMemory;

import com.cec.deliver.AbstractDataWareDelivery;
import com.cec.deliver.DataWareDelivery;
import com.cec.modeling.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 内置内存元件交付接口。
 * <p/>
 * 主要用于测试使用，无法用于生产。
 * @author koala
 * @see com.cec.deliver.DataWareDelivery
 * @see com.cec.deliver.AbstractDataWareDelivery
 */
public class DataWareDeliveryInnerMemory extends AbstractDataWareDelivery implements DataWareDelivery {

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
    public DataWareDeliveryInnerMemory() {
        this.innerMemoryDB = new InnerMemoryDB();
    }

    /**
     * 模态元件调用
     * @param DataWareId 元件ID
     * @param mainKey 主体字段
     * @return 一个模态元件对象
     */
    @Override
    public ModalDataWare getModalDataWare(String DataWareId, String mainKey) {
        List<DataCell> r = innerMemoryDB.getDataCells(DataWareId + mainKey);
        if(r != null && !r.isEmpty()){
            ModalDataWare  modalDataWare = new ModalDataWare();
            modalDataWare.setValue(r.get(1));
            modalDataWare.setMainKey(r.get(0));
            return modalDataWare;
        } else return null;
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
        List<ComposedDataWare>  composedDataWares = new ArrayList<>();
        for(int i = index; i < index + size; i++) {
            ComposedDataWare  composedDataWare = new ComposedDataWare();
            List<DataCell> r = innerMemoryDB.getDataCells(DataWareId + i);
            composedDataWare.setValues(r);
            composedDataWares.add(composedDataWare);
        }
        return composedDataWares;
    }
    /**
     * 组合态元件调用
     * @param DataWareId 元件ID
     * @param mainKey 主体字段
     * @return 一个组合态元件
     */
    @Override
    public CombinatorialDataWare getCombinatorialDataWareByMainKey(String DataWareId, String mainKey) {
        List<DataCell> r = innerMemoryDB.getDataCells(DataWareId + mainKey);
        if(r != null && !r.isEmpty()){
            CombinatorialDataWare  combinatorialDataWare = new CombinatorialDataWare();
            combinatorialDataWare.setValues(r);
            combinatorialDataWare.setIndex(0);
            return combinatorialDataWare;
        } else return null;
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
        List<DataWare>  DataWares = new ArrayList<>();
        for(int i = index; i < index + size; i++) {
            DataWare  DataWare = new DataWare();
            List<DataCell> r = innerMemoryDB.getDataCells(DataWareId + i);
            DataWare.setValues(r);
            DataWares.add(DataWare);
        }
        return DataWares;
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
    public void loadModalDataWare(String modalDataWareId, ModalDataWare modalDataWares) {
        String mainKey = (String) modalDataWares.getMainKey().getValue();
        innerMemoryDB.putDataCell(modalDataWareId + mainKey, modalDataWares.getValues());
        innerMemoryDB.putDataCell(modalDataWareId + 0, modalDataWares.getValues());
    }
    /**
     * 加载组态元件接口
     * @param composedDataWareId 组态元件ID
     * @param composedDataWares 组态元件对象
     */
    public void loadComposedDataWare(String composedDataWareId, ComposedDataWare composedDataWares) {
        innerMemoryDB.putDataCell(composedDataWareId + 0, composedDataWares.getValues());
    }
    /**
     * 加载组合态元件接口
     * @param combinatorialDataWareId 组合态元件ID
     * @param combinatorialDataWares 组合态元件对象
     */
    public void loadCombinatorialDataWare(String combinatorialDataWareId, CombinatorialDataWare combinatorialDataWares) {
        String mainKey = (String) combinatorialDataWares.getMainKey().getValue();
        innerMemoryDB.putDataCell(combinatorialDataWareId + mainKey, combinatorialDataWares.getValues());
        innerMemoryDB.putDataCell(combinatorialDataWareId + 0, combinatorialDataWares.getValues());
    }

}
