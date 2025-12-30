package com.cec.examine.template;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * 规则=识别模式 + 脱敏算法
 */
public class Rule<T> implements Serializable {


    private String id;//规则ID
    private T recognize;//识别对象
    private String denseFunctionClass;//脱敏算法
    private List<Config> configs;//脱敏算法配置

    public String getDenseFunctionClass() {
        return denseFunctionClass;
    }

    public void setDenseFunctionClass(String denseFunctionClass) {
        this.denseFunctionClass = denseFunctionClass;
    }
    public T getRecognize() {
        return recognize;
    }

    public void setRecognize(T recognize) {
        this.recognize = recognize;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rule rule = (Rule) o;
        return Objects.equals(id, rule.id) & Objects.equals(configs, rule.configs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, configs);
    }

    @Override
    public String toString() {
        return "Rule{" +
                "id=\"" + id + '\"' +
                ", configs=\"" + configs +
                "\", recognition=\"" + recognize + "\"}";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Config> getConfigs() {
        return configs;
    }

    public void setConfigs(List<Config> configs) {
        this.configs = configs;
    }

    public Rule() {
    }

    public Rule(String id, T recognize, String denseFunctionClass, List<Config> configs) {
        this.id = id;
        this.configs = configs;
        this.denseFunctionClass = denseFunctionClass;
        this.recognize = recognize;
    }
}
