package com.cec.examine.reversibility;

import com.cec.examine.util.jsonparser.JSONParser;

import java.util.TreeMap;

public class MainIdKV {

    private TreeMap<String, Object> map;

    public MainIdKV(TreeMap<String, Object> map) {
        this.map = map;
    }

    /**
     * 获取有序map展平的value值
     * @param map
     * @return
     */
    private String getFlatTreeMapValues(TreeMap<String, Object> map) {
        String line = "";
        for(String key: map.keySet()) {
            line += map.get(key) + ",";
        }
        return line;
    }
    public int hashCode() {
        String flatString = getFlatTreeMapValues(map);
        return flatString.hashCode();
    }

    public TreeMap<String, Object> getMap() {
        return this.map;
    }
}
