package com.cec.examine.desensitization.generator;

import com.cec.examine.util.IDCardUtil;

public class GeneratorAge extends Generator {

    @Override
    public String generateFixed(String seed) {
        Generator generatorID = new GeneratorID();
        String ID = generatorID.generateFixed(seed);
        return String.valueOf(IDCardUtil.getAge(ID));
    }

}
