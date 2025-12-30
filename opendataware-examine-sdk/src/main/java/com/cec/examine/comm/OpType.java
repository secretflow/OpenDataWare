package com.cec.examine.comm;

/**
 * 脱敏的数据类型
 */
public enum OpType {


    AND("and"),
    OR("or");

    private final String code;

    OpType(String code) {
        this.code = code;
    }

    public static OpType forcode(String code) {
        for (OpType v : values()) {
            if (code.equals(v.getCode())) {
                return v;
            }
        }
        return null;
    }

    public String getCode() {
        return code;
    }
}
