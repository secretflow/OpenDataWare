package com.cec.examine.desensitization.generator;

/**
 * @author ZGH
 * @version 1.0.0
 * @ClassName RegisterID.java
 * @Description
 * @createTime 2023/11/17
 */
public class RegisterID extends Generator{
    @Override
    public String generateFixed(String seed) {
        return this.generateFixedLengthNumber(seed,20);
    }
}
