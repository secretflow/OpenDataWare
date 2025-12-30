package com.cec.examine.comm.bean;

import java.util.Objects;

public class Tuple2<T, U> {


    private T f0;

    private U f1;

    public void f0(T f0) {
        this.f0 = f0;
    }

    public void f1(U f1) {
        this.f1 = f1;
    }

    public Tuple2() {
    }


    public T f0() {
        return f0;
    }

    public U f1() {
        return f1;
    }

    public Tuple2(T f0, U f1) {
        this.f0 = f0;
        this.f1 = f1;
    }

    @Override
    public String toString() {
        return "Tuple2{" +
                "f0=" + f0 +
                ", f1=" + f1 +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (o == null || getClass() != o.getClass()) { return false;}
        Tuple2<?, ?> tuple2 = (Tuple2<?, ?>) o;
        return Objects.equals(f0, tuple2.f0) &&
                Objects.equals(f1, tuple2.f1);
    }

    @Override
    public int hashCode() {
        return Objects.hash(f0, f1);
    }
}
