package com.cec.examine.reversibility;

import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;

/**
 * 相关性审核，目前只加载结果表
 */
public class RelativityResult extends ReversibilityResult {

    private TreeMap<String, Object> mainIdValue;

    private List<String> mainIdCloumName;

    public Object getMainIdValue() {
        return mainIdValue;
    }

    public void setMainIdValue(TreeMap<String, Object> mainIdValue) {
        this.mainIdValue = mainIdValue;
    }

    public List<String> getMainIdCloumName() {
        return mainIdCloumName;
    }

    public void setMainIdCloumName(String [] mainIdCloumName) {
        this.mainIdCloumName = Arrays.asList(mainIdCloumName);
    }
}
