package com.cec.examine.comm;

/**
 * 对分隔符的处理
 */
public enum SymbolOperator {


    SPLIT("split"),
    SKIP("skip"),
    PASS("pass");


    private final String code;

    SymbolOperator(String code) {
        this.code = code;
    }

    public static SymbolOperator forcode(String code) {
        for (SymbolOperator v : values()) {
            if (code.equals(v.getCode())) {
                return v;
            }
        }
        return PASS;
    }

    public String getCode() {
        return code;
    }
}
