package com.cec.examine.template.recognition;

import com.cec.examine.template.AbstractConfigLoader;
import com.cec.examine.util.FileUtilityUtil;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class RecognitionCofigLoader extends AbstractConfigLoader {

    public RecognitionCofigLoader() {
        this.resourcePath = "/template/recognition_config.csv";
        this.remoteURL = "";
    }

    @Override
    public List<RecognitionCofig> loadFromResourceFile() {
        List<RecognitionCofig> r = new ArrayList<RecognitionCofig>();
        String line = null;
        InputStream inputStream = RecognitionCofigLoader.class.getResourceAsStream(this.resourcePath);
        FileUtilityUtil fu = FileUtilityUtil.getFileReader(inputStream);
        while (true) {
            String[] cells = fu.getCSVLineCells(",");
            if (cells == null || cells.length < 15) break;//目前配置表为15列
            if (cells[0].indexOf("信息项") >= 0 || cells[0].indexOf("code") >= 0) continue;//去掉表头
            //System.out.println(FileUtilityUtil.join(cells,","));
            RecognitionCofig recogitionCofig = new RecognitionCofig();
            recogitionCofig.setCode(cells[0]);
            recogitionCofig.setCategory(cells[1]);
            recogitionCofig.setClassification(cells[2]);
            recogitionCofig.setLableName(cells[3]);
            recogitionCofig.setPriority(Integer.parseInt(cells[4]));
            recogitionCofig.setEnglishColumName(cells[5]);
            recogitionCofig.setChineseColumName(cells[6]);
            recogitionCofig.setRegexPattern(cells[7]);
            recogitionCofig.setNerCode(cells[8]);
            recogitionCofig.setJavaFunctionName(cells[9]);
            recogitionCofig.setStrategy(cells[10]);
            recogitionCofig.setPersonal("1".equals(cells[11]) ? true : false);
            if ("1".equals(cells[13])) {
                recogitionCofig.setLevel("一般");
            } else if ("2".equals(cells[13])) {
                recogitionCofig.setLevel("重要");
            } else if ("3".equals(cells[13])) {
                recogitionCofig.setLevel("核心");
            }
            recogitionCofig.setBasicLableName(cells[14]);
            r.add(recogitionCofig);
        }
        return r;
    }

    @Override
    public List<RecognitionCofig> loadFromRemote() {
        //后续可以支持从远程调用字典库
        return null;
    }
}
