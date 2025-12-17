package com.cec.example.DataComponentStandard;
import com.alibaba.fastjson.JSON;
import com.cec.modeling.DataComponent;
import com.cec.utils.FileUtilityUtil;
import java.io.InputStream;
import java.util.List;

/**
 * 加载样例数据父类
 */
public abstract class DCExample<T extends DataComponent> {

    protected String resourceFileName;

    protected String fileContent;

    public DCExample(String resourceFileName) {
        this.resourceFileName = "/" + resourceFileName;
        this.fileContent = readFile();
    }

    //元件类可以复用json格式的方法
    public String getJSON() {
        return JSON.toJSONString(getDataComponents());
    }

    //获取元件对象
    public abstract List<T> getDataComponents();

    //只适合小数据量的文件，放在resource目录下
    private String readFile() {
        InputStream inputStream = DCExample.class.getResourceAsStream(resourceFileName);
        FileUtilityUtil fileUtilityUtil = FileUtilityUtil.getFileReader(inputStream);
        String s = fileUtilityUtil.getAll();
        fileUtilityUtil.close();
        return s;
    }

    public static void main(String[] args) {
        ModalDataComponentExample modalDataComponentExample = new ModalDataComponentExample("DataComponentDemo/ModalDataComponentDemo.csv");
        System.out.println("这是一个模态数据元件的数据部分：" + modalDataComponentExample.getJSON());
        ComposedDataComponentExample composedDataComponentExample = new ComposedDataComponentExample("DataComponentDemo/ComposedDataComponentDemo.csv");
        System.out.println("这是一个组态数据元件的数据部分：" + composedDataComponentExample.getJSON());
        CombinatorialDataComponentExample combinatorialDataComponentExample = new CombinatorialDataComponentExample("DataComponentDemo/CombinatorialDataComponentDemo.csv");
        System.out.println("这是一个组合态数据元件的数据部分：" + combinatorialDataComponentExample.getJSON());
    }
}