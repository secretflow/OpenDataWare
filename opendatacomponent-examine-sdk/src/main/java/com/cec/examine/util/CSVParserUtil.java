package com.cec.examine.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CSVParserUtil {
    public static void main(String[] args) {
        URL resourceUrl = CSVParserUtil.class.getClassLoader().getResource("adcodes.csv");
        File vocabFile = new File(resourceUrl.getFile());
//        String csvFilePath = "your_csv_file.csv"; // 替换成你的CSV文件路径
        List<String> data = parseCSV();

        if (data != null) {
            for (String cell : data) {
                System.out.print(cell + "\t");
            }
            System.out.println(); // 换行显示下一行数据

        }
    }

    public static List<String> parseCSV() {
        URL resourceUrl = CSVParserUtil.class.getClassLoader().getResource("adcodes.csv");
        File vocabFile = new File(resourceUrl.getFile());
        List<String> data = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(vocabFile.getAbsolutePath()))) {
            String line;
            while ((line = reader.readLine()) != null) {
//                String[] row = line.split(","); // 根据CSV文件的分隔符分割数据
                data.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }
}
