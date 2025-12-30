package com.cec.examine.watermark;

import com.cec.examine.util.FileUtilityUtil;

import java.util.List;

public class WaterMarkLSBDemo {


    public static void main(String [] args) {
        //选择水印类型
        WaterMark wm = new WaterMarkLSB();//零宽字符水印
        //测试样本文件
        String sourceFilePath = "/Users/mac/Downloads/test/A.csv";
        String targetFilePath = "/Users/mac/Downloads/test/C.csv";
        if(args.length > 0) {
            sourceFilePath = args[0];
            targetFilePath = args[1];
        }
        String seprator = ",";
        //读取文件
        FileUtilityUtil fileReader = FileUtilityUtil.getFileReader(sourceFilePath);
        //把文件加载到内存
        List<String> source = fileReader.getCSVAllList();
        System.out.println("load file into memory: " + source.size() + " records.");
        //生成水印,长度太长引起性能问题，建议10以下，碰撞概率较低
        String key = "6OL0Mkg47ShKSKhjzrGAMWIzCMfuIdDvQ69ONHPlcsQv1bfUrHdIMjA4vWYkbxLg";
        String encodeWaterMark = WaterMarkUtils.encodeWaterMark(key);
        System.out.println("key:" + key + ", watermark: " + encodeWaterMark);
        //加水印
        long start = System.currentTimeMillis();
        List<String> result = wm.addBatchWaterMark(source,seprator, encodeWaterMark, 100);
        long end = System.currentTimeMillis();
        System.out.println("add watermark time cos: " + (end-start) + "ms, " + source.size() + " records.");
        //输出结果
        FileUtilityUtil fileWriter = FileUtilityUtil.getFileWriter(targetFilePath);
        for(String line: result) {
            fileWriter.writeLine(line);
        }
        fileWriter.close();
        //提取水印
        long extStartTime = System.currentTimeMillis();
        String waterMark = WaterMarkUtils.extractBatchWaterMark(result, seprator, null);
        long extEndTime = System.currentTimeMillis();

        System.out.println("extract waterMark is: " + waterMark + " time cos: " + (extEndTime - extStartTime) + "ms");
    }
}
