package com.cec.examine.desensitization.generator;

/**
 *卫生机构（组织）代码由22位数字（或英文字母）组成,包括9位组织机构代码和13位机构属性代码。
 * 机构属性代码由行政区划代码（6位）、经济类型代码（2位）、卫生机构（组织）类别代码（4位）和
 * 机构分类管理代码（1位）四部分组成
 **/
public class MedicalInstitutionCodeGenerator extends Generator {

    @Override
    public String generateFixed(String seed) {
        // 生成9位组织机构代码
        String orgCode = this.generateFixedLengthNumber(seed,22);
        // 生成机构属性代码

        return orgCode;
    }
}
