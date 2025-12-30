package com.cec.examine.desensitization.generator;

import com.cec.examine.util.name.FamilyNames;

/**
 * 企业名称固定映射
 */
public class GeneratorEnterpriseName extends Generator{
    private String[] names = {
            "集团", "有限责任公司", "测试分公司"
    };
    @Override
    public String generateFixed(String seed) {
        return FamilyNames.getLikelyFullName(seed)+"有限责任公司";

    }
}
