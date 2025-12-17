package com.cec.examine.template;


import com.cec.examine.comm.OpType;
import com.cec.examine.comm.SymbolOperator;

import java.io.Serializable;
import java.util.Objects;

public class Config implements Serializable {

    private int order;

    private String placeHolder;

    private int start;

    private int end;

    // 是否需要跳过特殊符号
    private SymbolOperator symbotOp;

    // 特殊符号
    private String symbol;

    private OpType op;


    public Config() {


    }

    @Override
    public String toString() {
        return "Config{" +
                "order=" + order +
                ", placeHolder='" + placeHolder + '\'' +
                ", start=" + start +
                ", end=" + end +
                ", symbotOp=" + symbotOp +
                ", symbol='" + symbol + '\'' +
                ", op=" + op +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Config config = (Config) o;
        return order == config.order && start == config.start && end == config.end && Objects.equals(placeHolder, config.placeHolder) && symbotOp == config.symbotOp && Objects.equals(symbol, config.symbol) && op == config.op;
    }

    @Override
    public int hashCode() {
        return Objects.hash(order, placeHolder, start, end, symbotOp, symbol, op);
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getPlaceHolder() {
        return placeHolder;
    }

    public void setPlaceHolder(String placeHolder) {
        this.placeHolder = placeHolder;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end+1;
    }

    public SymbolOperator getSymbotOp() {
        return symbotOp;
    }

    public void setSymbotOp(SymbolOperator symbotOp) {
        this.symbotOp = symbotOp;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public OpType getOp() {
        return op;
    }

    public void setOp(OpType op) {
        this.op = op;
    }

    public Config(int order, String placeHolder, int start, int end, SymbolOperator symbotOp, String symbol, OpType op) {
        this.order = order;
        this.placeHolder = placeHolder;
        this.start = start;
        this.end = end;
        this.symbotOp = symbotOp;
        this.symbol = symbol;
        this.op = op;
    }
}

