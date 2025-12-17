package com.cec.examine.desensitization.generator;

import com.cec.examine.desensitization.SensitivePatternRegex;

import java.util.Map;

/**
 * 仿真脱敏包装类
 */
public class GeneralStringWraper extends SensitivePatternRegex {

    private final GeneratorGeneralString generatorGeneralString;
    public GeneralStringWraper(Map<String, Double> map,  Generator generator) {
        generatorGeneralString = new GeneratorGeneralString(map, generator, true, true);
    }

    @Override
    public String pattern() {
        return null;
    }

    @Override
    public String target(String source) {
        return generatorGeneralString.generateFixed(source);
    }
}
