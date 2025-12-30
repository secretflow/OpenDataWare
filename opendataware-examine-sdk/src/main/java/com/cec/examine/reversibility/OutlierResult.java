package com.cec.examine.reversibility;

import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;

/**
 * 离群点审核
 */
public class OutlierResult extends ReversibilityResult {
    private TreeMap<String,Object> mainIdValue = new TreeMap<>();

    private String CloumName;

    public String getCloumName() {
        return CloumName;
    }

    public void setCloumName(String cloumName) {
        CloumName = cloumName;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    private Object value;


    public TreeMap<String,Object> getMainIdValue() {
        return mainIdValue;
    }

    public void setMainIdValue(TreeMap<String,Object> mainIdValue) {
        this.mainIdValue = mainIdValue;
    }


    private List<String> mainIdCloumNames;
    public void setMainIdCloumName(String [] mainIdCloumNames) {
        if(mainIdCloumNames != null)
            this.mainIdCloumNames = Arrays.asList(mainIdCloumNames);
    }

    public List<String> getMainIdCloumName() {
        return this.mainIdCloumNames;
    }
}
