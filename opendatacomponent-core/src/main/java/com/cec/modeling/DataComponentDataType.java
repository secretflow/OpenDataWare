package com.cec.modeling;

import java.util.Arrays;
import java.util.List;

/**
 * 元件的数据类型枚举
 * @author koala
 */
public enum DataComponentDataType {

    // 字符串类型
    CHAR(DataTypeFamily.STRING, "固定长度字符串"),
    VARCHAR(DataTypeFamily.STRING, "可变长度字符串"),
    STRING(DataTypeFamily.STRING, "字符串（VARCHAR(2147483647) 的同义词）"),

    // 二进制类型
    BINARY(DataTypeFamily.BINARY, "固定长度二进制字符串"),
    VARBINARY(DataTypeFamily.BINARY, "可变长度二进制字符串"),
    BYTES(DataTypeFamily.BINARY, "字节序列（VARBINARY(2147483647) 的同义词）"),

    // 精确数值类型
    DECIMAL(DataTypeFamily.NUMERIC, "固定精度和比例的十进制数"),
    TINYINT(DataTypeFamily.NUMERIC, "1字节有符号整数（-128 到 127）"),
    SMALLINT(DataTypeFamily.NUMERIC, "2字节有符号整数（-32768 到 32767）"),
    INT(DataTypeFamily.NUMERIC, "4字节有符号整数"),
    INTEGER(DataTypeFamily.NUMERIC, "INT 的同义词"),
    BIGINT(DataTypeFamily.NUMERIC, "8字节有符号整数"),

    // 近似数值类型
    FLOAT(DataTypeFamily.NUMERIC, "4字节单精度浮点数"),
    DOUBLE(DataTypeFamily.NUMERIC, "8字节双精度浮点数"),

    // 日期和时间类型
    DATE(DataTypeFamily.DATETIME, "日期（年-月-日）"),
    TIME(DataTypeFamily.DATETIME, "时间（无时区）"),
    TIME_WITH_PRECISION(DataTypeFamily.DATETIME, "带精度的无时区时间"),
    TIMESTAMP(DataTypeFamily.DATETIME, "时间戳（无时区）"),
    TIMESTAMP_WITH_TIME_ZONE(DataTypeFamily.DATETIME, "带时区的时间戳"),
    TIMESTAMP_LTZ(DataTypeFamily.DATETIME, "本地时区时间戳"),

    // 复杂类型
    ARRAY(DataTypeFamily.COLLECTION, "相同子类型元素的数组"),
    MAP(DataTypeFamily.COLLECTION, "键值对映射"),
    MULTISET(DataTypeFamily.COLLECTION, "多重集合"),
    ROW(DataTypeFamily.STRUCTURED, "字段序列"),

    // 其他类型
    BOOLEAN(DataTypeFamily.OTHER, "布尔值"),
    NULL(DataTypeFamily.OTHER, "空值"),
    RAW(DataTypeFamily.OTHER, "原始类型");

    private final DataTypeFamily family;
    private final String description;

    // 构造函数
    DataComponentDataType(DataTypeFamily family, String description) {
        this.family = family;
        this.description = description;
    }

    public DataTypeFamily getFamily() {
        return family;
    }

    public String getDescription() {
        return description;
    }

    /**
     * 获取所有数据类型的列表
     */
    public static List<DataComponentDataType> getAllDataTypes() {
        return Arrays.asList(DataComponentDataType.values());
    }

    /**
     * 按类型族筛选
     */
    public static List<DataComponentDataType> getByFamily(DataTypeFamily family) {
        return Arrays.stream(DataComponentDataType.values())
                .filter(dt -> dt.family == family).toList();
    }

    /**
     * 数据类型分类枚举
     */
    public enum DataTypeFamily {
        STRING,      // 字符串类型
        BINARY,      // 二进制类型
        NUMERIC,     // 数值类型
        DATETIME,    // 日期时间类型
        COLLECTION,  // 集合类型
        STRUCTURED,  // 结构类型
        OTHER        // 其他类型
    }
}
