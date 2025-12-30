package com.cec.examine.desensitization.generator;

/**
 * @author ZGH
 * @version 1.0.0
 * @ClassName MD5Generator.java
 * @Description 固定生成32位数字
 * @createTime 2023/11/17
 */
public class MD5Generator extends Generator{
    @Override
    public String generateFixed(String seed) {
        return this.generateFixedLengthNumber(seed,32);
    }
}
