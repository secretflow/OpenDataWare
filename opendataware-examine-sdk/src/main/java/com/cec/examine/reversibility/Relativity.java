package com.cec.examine.reversibility;
import com.cec.examine.template.TableCellPojo;
import com.cec.examine.util.MapCounter;

import java.util.*;

/**
 * 相关性审核，目前只加载结果表
 */
public class Relativity extends Reversibility {

    private LinkedHashMap<MainIdKV, String> resultData = new LinkedHashMap<MainIdKV, String>();

    /**
     * 添加元件结果的每一行
     */
    @Override
    public void addLineOfResult(Map<String, TableCellPojo> lineData) {
        if(this.getMainIdColumns() == null ) return;//如果主体字段没设置，则直接退出即可。
        String featureText = "";
        Object mainId = null;
        TreeMap<String, Object> mainIds = new TreeMap<>();
        for(String colum: lineData.keySet()) {
            if(this.getMainIdColumnSet().contains(colum)) {
                mainId = lineData.get(colum).getValue();
                mainIds.put(colum,mainId);
            } else {
                featureText += lineData.get(colum);
            }
        }
        if(mainIds.size() > 0) {
            resultData.put(new MainIdKV(mainIds), featureText);
        }
    }


    @Override
    public void addLineOfResource(Map<String, TableCellPojo> lineData) {

    }

    public List<RelativityResult> check() {
        List<RelativityResult> results = new ArrayList<RelativityResult>();
        if(this.getMainIdColumns() == null) return results;//如果没设置主键直接退出
        MapCounter<String> kValueMapCounter = new MapCounter<String>();
        LinkedHashMap<String, List<MainIdKV>> antiMap = new LinkedHashMap<String, List<MainIdKV>>();
        for(MainIdKV mainId: resultData.keySet()) {
            String featureText = resultData.get(mainId);
            List<MainIdKV> listMainId = antiMap.get(featureText);
            if(listMainId == null) listMainId = new ArrayList<MainIdKV>();
            listMainId.add(mainId);
            antiMap.put(featureText, listMainId);
            kValueMapCounter.incr(featureText);
        }

        for(String f: kValueMapCounter.keySet()) {
            if(kValueMapCounter.get(f) <= 1) {
                //说明K值=1，需要罗列出对应的主体标识ID
                List<MainIdKV> mainIdList = antiMap.get(f);
                for(MainIdKV mainId: mainIdList) {
                    RelativityResult relativityResult = new RelativityResult();
                    relativityResult.setMainIdValue(mainId.getMap());
                    relativityResult.setMainIdCloumName(this.getMainIdColumns());
                    results.add(relativityResult);
                }
            }
        }
        return results;
    }

}
