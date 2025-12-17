package com.cec.examine.desensitization.generator;

import java.util.ArrayList;
import java.util.List;

/**
 * 邮箱固定仿真
 */
public class GeneratorEmail extends Generator{
    // 可以根据需要修改邮箱的域名部分
    private String[] domains = {
    "@gmail.com", "@yahoo.com", "@msn.com", "@hotmail.com", "@qq.com", "@163.com","@163.net", "@googlemail.com","@mail.com"
    };
    @Override
    public String generateFixed(String seed) {
        List<String> headsArr = new ArrayList<String>();
        for(String domain:domains) {
            headsArr.add(domain);
        }
        String HEAD = this.generateFixedEnumWord(seed, headsArr);
        String LAST = this.generateFixedLengthNumber(seed, 10);
        return LAST+HEAD;
    }
}
