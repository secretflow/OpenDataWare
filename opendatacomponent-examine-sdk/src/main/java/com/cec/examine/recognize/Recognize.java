package com.cec.examine.recognize;

import com.cec.examine.dict.HIT;
import com.cec.examine.util.jsonparser.ObjectToJSON;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author qiuwenyi
 * @version 1.0.0
 * @ClassName Recognize
 * @Description 识别模式对象基类
 * @createTime 2023/10/08
 */
public abstract class Recognize {
    private String code;//唯一标识码
    private String name;//识别类型名称
    public abstract List<HIT> getHitResult(String text);

    protected LinkedHashMap<String, String> typeNameAliasMap = new LinkedHashMap<String,String>();

    /**
     * 结构化单体实体词，只存在一个类型
     * @param text
     * @return
     */
    public abstract String getSingleType(String text);

    /**
     * 长文本，可以识别出多个类型
     * @param text
     * @param longText
     * @return
     */
    public abstract List<String> getTypeOfLongText(String text);

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String toString() {
        return ObjectToJSON.toJSONString(this);
    }

    /**
     * 设置类型的别名，方便直接返回第三方定义的类型编码
     * @param text
     * @return
     */
    public void setTypeAlias(String typeName, String aliasTypeName) {
        typeNameAliasMap.put(typeName, aliasTypeName);
    }

    /**
     * 返回设置后的别名，没设置返回原始类型值
     * @param typeName
     * @return
     */
    public String getTypeAlias(String typeName) {
        String typeAlias = typeNameAliasMap.get(typeName);
        return typeAlias != null ? typeAlias :  typeName;
    }


}
