package com.cec.examine.desensitization.generator;

public class GeneratorHeight extends Generator {

    @Override
    public String generateFixed(String seed) {
        return this.generateFixedRangeNumber(seed,155,185);
    }

}
