package com.cec.examine.desensitization.fake.randomfake;

import com.cec.examine.desensitization.SensitivePatternRegex;

import java.time.LocalDate;
import java.util.Random;
import java.util.regex.Matcher;

/**
 * @author ZGH
 * @version 1.0.0
 * @ClassName SensitivePatternRealEstateOwnershipCertificateFake.java
 * @Description 不动产权证 随机映射
 * 8位数字构成  前边四位是年份 后边四位是流水号
 * @createTime 2023/07/13
 */
public class RealEstateOwnershipCertificateRandomFake extends SensitivePatternRegex {
    public RealEstateOwnershipCertificateRandomFake(){

    }
    @Override
    public String pattern() {
        String pattern = "^\\d{4}\\d{4}$";
        return pattern;
    }

    @Override
    public String target(String source) {
        return generateRandomCertificateNumber();
    }

    public static String generateRandomCertificateNumber() {
        int currentYear = LocalDate.now().getYear();
        int randomYear = generateRandomNumber(1978, currentYear);

        // 生成随机的流水号
        int randomSerialNumber = generateRandomNumber(0, 9999);

        // 拼接年份和流水号
        String certificateNumber = String.format("%04d%04d", randomYear, randomSerialNumber);
        return certificateNumber;
    }

    public static int generateRandomNumber(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min + 1) + min;
    }
}
