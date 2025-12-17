package com.cec.examine.template;

import java.io.Serializable;
import java.util.List;

public class TemplateBean implements Serializable {

    private String id;

    private List<Rule> rules;

    private String version;

    private int state;

    @Override
    public String toString() {
        return "TemplateBean{" +
                "id='" + id + '\'' +
                ", rules=" + rules +
                ", version='" + version + '\'' +
                ", state=" + state +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Rule> getRules() {
        return rules;
    }

    public void setRules(List<Rule> rules) {
        this.rules = rules;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public TemplateBean() {
    }

    public TemplateBean(String id, List<Rule> rules, String version, int state) {
        this.id = id;
        this.rules = rules;
        this.version = version;
        this.state = state;
    }
}

