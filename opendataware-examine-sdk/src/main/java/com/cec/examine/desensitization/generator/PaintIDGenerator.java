package com.cec.examine.desensitization.generator;

/**
 * @author ZGH
 * @version 1.0.0
 * @ClassName paintCodeGenerator.java
 * @Description 病人id生成规则
 * @createTime 2023/11/17
 */
public class PaintIDGenerator extends Generator{
    @Override
    public String generateFixed(String seed) {
        return this.generateFixedLengthNumber(seed,13);
    }
}
