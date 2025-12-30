package com.cec.example.DataWareStandard;
import com.alibaba.fastjson.JSON;
import com.cec.modeling.DataWare;
import com.cec.utils.FileUtilityUtil;
import java.io.InputStream;
import java.util.List;

/**
 * 加载样例数据父类
 */
public abstract class DCExample<T extends DataWare> {

    protected String resourceFileName;

    protected String fileContent;

    public DCExample(String resourceFileName) {
        this.resourceFileName = "/" + resourceFileName;
        this.fileContent = readFile();
    }

    //元件类可以复用json格式的方法
    public String getJSON() {
        return JSON.toJSONString(getDataWares());
    }

    //获取元件对象
    public abstract List<T> getDataWares();

    //只适合小数据量的文件，放在resource目录下
    private String readFile() {
        InputStream inputStream = DCExample.class.getResourceAsStream(resourceFileName);
        FileUtilityUtil fileUtilityUtil = FileUtilityUtil.getFileReader(inputStream);
        String s = fileUtilityUtil.getAll();
        fileUtilityUtil.close();
        return s;
    }

    public static void main(String[] args) {
        ModalDataWareExample modalDataWareExample = new ModalDataWareExample("DataWareDemo/ModalDataWareDemo.csv");
        System.out.println("这是一个模态数据元件的数据部分：" + modalDataWareExample.getJSON());
        ComposedDataWareExample composedDataWareExample = new ComposedDataWareExample("DataWareDemo/ComposedDataWareDemo.csv");
        System.out.println("这是一个组态数据元件的数据部分：" + composedDataWareExample.getJSON());
        CombinatorialDataWareExample combinatorialDataWareExample = new CombinatorialDataWareExample("DataWareDemo/CombinatorialDataWareDemo.csv");
        System.out.println("这是一个组合态数据元件的数据部分：" + combinatorialDataWareExample.getJSON());
    }
}