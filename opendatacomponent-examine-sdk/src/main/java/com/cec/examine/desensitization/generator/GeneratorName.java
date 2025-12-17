package com.cec.examine.desensitization.generator;

import com.cec.examine.util.name.FamilyNames;

public class GeneratorName extends Generator {

    @Override
    public String generateFixed(String seed) {
        return FamilyNames.getLikelyFullName(seed);
    }

}
