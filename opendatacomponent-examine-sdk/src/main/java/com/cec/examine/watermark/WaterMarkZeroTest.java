package com.cec.examine.watermark;

import com.cec.examine.util.FileUtilityUtil;

import java.text.DecimalFormat;
import java.util.List;

/**
 * 测试1kw条数据加水印和提取水印的速度
 */
public class WaterMarkZeroTest {


    public static void main(String [] args) {
        //选择水印类型
        WaterMark wm = new WaterMarkZeroWith();//不可见字符
        //测试样本文件
//        String sourceFilePath = "/Users/mac/Downloads/test/777.csv";
        String sourceFilePath = "E:\\work\\2025.03\\watermark\\a_waterzero_input.csv";
        String targetFilePath = "E:\\work\\2025.03\\watermark\\a_waterzero_output.csv";
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
        System.out.println("key:" + key + ", watermark: " + encodeWaterMark + ", watermark length: " + encodeWaterMark.length());

        //加水印
        long start = System.currentTimeMillis();
        List<String> result = wm.addBatchWaterMark(source,seprator, encodeWaterMark, 100);
        long end = System.currentTimeMillis();
        System.out.println("add watermark time cos: " + (end-start) + "ms, " + source.size() + " records.");

        Double costTime = (end-start)/1000.0;
        Double totalBytes = getTotalMegabytes(result);
        System.out.println("加水印共耗时:"+costTime+"s");
        System.out.println("加水印数据量:"+totalBytes+"M");
        Double speed = totalBytes/costTime;
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        System.out.println("加水印速度:"+decimalFormat.format(speed)+"M/s");
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

    public static double getTotalMegabytes(List<String> list) {
        long totalBytes = 0L;

        // 遍历列表中的每个字符串
        for (String str : list) {
            // 获取字符串的字节数组
            byte[] bytes = str.getBytes();
            // 累加字节数
            totalBytes += bytes.length;
        }

        // 将总字节数转换为兆字节 (MB)
        double totalMegabytes = (double) totalBytes / (1024 * 1024);

        return totalMegabytes;
    }
}
