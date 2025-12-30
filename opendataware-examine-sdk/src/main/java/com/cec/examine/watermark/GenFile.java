package com.cec.examine.watermark;

import com.cec.examine.util.FileUtilityUtil;

import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;

public class GenFile {

    public static void main(String[] args) throws Exception {
        String sourceFilePath = "D:\\数据安全\\水印\\A.csv";
        String targetFilePath = "D:\\数据安全\\水印\\A300.csv";
        FileOutputStream fos = new FileOutputStream(targetFilePath);

        for(int i =0; i<300;i++) {
            FileUtilityUtil fu = FileUtilityUtil.getFileReader(sourceFilePath);
            int cnt = 0;
            while (true) {
                //一行一行的读取文件内容
                String line = fu.getCSVLine();
                if (line == null) break;
                cnt++;
                fos.write((line + "\n").getBytes(StandardCharsets.UTF_8));
            }
        }
        fos.flush();
        fos.close();
    }

}
