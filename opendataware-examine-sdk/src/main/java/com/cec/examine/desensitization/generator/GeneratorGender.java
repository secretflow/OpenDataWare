package com.cec.examine.desensitization.generator;

import com.cec.examine.util.IDCardUtil;

public class GeneratorGender extends Generator {

    @Override
    public String generateFixed(String seed) {
        Generator generatorID = new GeneratorID();
        String ID = generatorID.generateFixed(seed);
        return IDCardUtil.getGender(ID);
    }

}
