package com.cec.examine.desensitization.generator;

import java.util.ArrayList;
import java.util.List;

public class GeneratorPhone extends Generator {

    private String [] heads = {"139","138","137","136","134","135","147","150",
            "151","152","157","158","159","172","178","182","183","184","187",
            "188","195","197","198","130","131","132","140","145","146","155",
            "156","166","185","186","175","176","196","133","149","153","177",
            "173","180","181","189","190","191","193","199","192","162","165",
            "167","170","171"};

    @Override
    public String generateFixed(String seed) {
        List<String> headsArr = new ArrayList<String>();
        for(String head:heads) {
            headsArr.add(head);
        }
        String HEAD = this.generateFixedEnumWord(seed, headsArr);
        String LAST = this.generateFixedLengthNumber(seed, 8);
        return HEAD + LAST;
    }

}
