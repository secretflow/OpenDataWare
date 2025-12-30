package com.cec.examine.reversibility;

import com.cec.examine.template.TableCellPojo;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class Reversibility {

    public String [] getMainIdColumns() {
        return mainIdColumns;
    }

    private Set<String> mainIdColumnSet;
    public Set<String> getMainIdColumnSet() {
        return mainIdColumnSet;
    }

    public void setMainIdColumn(String [] mainIdColumns) {
        if(mainIdColumns != null && mainIdColumns.length > 0) {
            this.mainIdColumns = mainIdColumns;
            this.mainIdColumnSet = new HashSet<>();
            for (String mainIdColumn : mainIdColumns) {
                this.mainIdColumnSet.add(mainIdColumn);
            }
        }
    }

    private String [] mainIdColumns;

    public abstract void addLineOfResult(Map<String, TableCellPojo> lineData);
    /**
     * 添加元件资源的每一行
     */
    public abstract void addLineOfResource(Map<String, TableCellPojo> lineData);


}
