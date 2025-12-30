package com.cec.examine.template.quality;

import com.cec.examine.template.AbstractConfigLoader;
import com.cec.examine.util.FileUtilityUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class QiConfigLoader extends AbstractConfigLoader {

    public QiConfigLoader() {
        this.resourcePath = "/template/data_qi_config.csv";
        this.remoteURL = "";
    }

    @Override
    public List<QiConfig> loadFromResourceFile() {
        List<QiConfig> r = new ArrayList<QiConfig>();
        InputStream inputStream = QiConfigLoader.class.getResourceAsStream(this.resourcePath);
        FileUtilityUtil fu = FileUtilityUtil.getFileReader(inputStream);
        while (true) {
            String[] cells = fu.getCSVLineCells(",");
            if (cells == null) break;
            if (cells[1].indexOf("QIC") == -1) continue;//去掉表头行

            QiConfig qiConfig = new QiConfig();
            qiConfig.setQiType(cells[0]);
            qiConfig.setQiCode(cells[1]);
            qiConfig.setQiName(cells[2]);
            qiConfig.setBasicLableName(cells[3]);
            qiConfig.setQiProblemDesc(cells[2] + "有误");
            qiConfig.setStandardCode(cells[4]);
            if (cells[5] != null) {
                String[] valueArray = cells[5].split("\001");
                Set<String> valueSet = new HashSet<String>();
                for (int i = 0; i < valueArray.length; i++) {
                    //剔除没有值或者为null的值域值
                    if ("".equals(valueArray[i]) || "null".equals(valueArray[i])) continue;
                    valueSet.add(valueArray[i]);
                }
                qiConfig.setValues(valueSet);
            }
            r.add(qiConfig);
        }

        return r;
    }

    @Override
    public List<QiConfig> loadFromRemote() {
        //后续可以支持从远程调用字典库
        return null;
    }
}
