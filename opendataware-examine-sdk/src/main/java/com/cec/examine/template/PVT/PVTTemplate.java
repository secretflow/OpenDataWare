package com.cec.examine.template.PVT;

import com.cec.examine.dict.HIT;
import com.cec.examine.recognize.RecognizePornographyViolenceTerrorism;
import com.cec.examine.template.TableCellPojo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PVTTemplate {

    private static final RecognizePornographyViolenceTerrorism  recognizePornographyViolenceTerrorism = new RecognizePornographyViolenceTerrorism();

    public List<PVTResult> pvt(List<Map<String, TableCellPojo>> data) {
        List<PVTResult> results = new ArrayList<>();
        for (int i = 0; data != null && i < data.size(); i++) {
            //遍历表的行数据
            Map<String,TableCellPojo> map = data.get(i);//获取列信息
            for(String column: map.keySet()) {
                TableCellPojo tableCellPojo = map.get(column);
                if(i == 0) {
                    //这代表需要检查中文注释名
                    List<HIT> hitList = recognizePornographyViolenceTerrorism.getHitResult(tableCellPojo.getComments());
                    if(hitList != null && !hitList.isEmpty()) {
                        PVTResult pvtResult = new PVTResult();
                        pvtResult.setColumnName(column);
                        pvtResult.setLineNumber(-1L);//-1代表为列名称有问题
                        pvtResult.setHitWords(hitList.get(0).getSource());
                        results.add(pvtResult);
                        break;//直接跳出这一列即可。
                    }
                }
                //检查表格中的单元格内容
                List<HIT> hitList = recognizePornographyViolenceTerrorism.getHitResult(String.valueOf(tableCellPojo.getValue()));
                if(hitList != null && !hitList.isEmpty()) {
                    PVTResult pvtResult = new PVTResult();
                    pvtResult.setColumnName(column);
                    pvtResult.setLineNumber((long)i);//行号取数组下标
                    pvtResult.setHitWords(hitList.get(0).getSource());
                    results.add(pvtResult);
                    break;//直接跳出这一列即可。
                }
            }
        }
        return results;
    }
}
