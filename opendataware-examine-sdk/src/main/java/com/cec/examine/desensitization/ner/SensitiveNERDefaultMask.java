package com.cec.examine.desensitization.ner;

import com.cec.examine.desensitization.SensitivePatternNER;

/**
 * @author ZGH
 * @version 1.0.0
 * @Description 住址遮盖
 * @createTime 2023/07/12
 */
public class SensitiveNERDefaultMask extends SensitivePatternNER {

    @Override
    public String target(String s) {
        if(s != null && !s.equals("")) {
            return s.substring(0,1) + fill(s.length()-1, '*');
        } else return s;
    }

    private String fill(int n, char c) {
        String s= "";
        for (int i=0;i<n;i++) {
            s+=c;
        }
        return s;
    }
}
