package com.cec.examine.desensitization.generator;

public class HealthOrganizationCodeGenerator {

    public static String generateHealthOrganizationCode(String tag) {
        Integer input = tag.hashCode();
        // 根据规则，机构属性代码包括行政区划代码（6位）、经济类型代码（2位）、卫生机构（组织）类别代码（4位）和机构分类管理代码（1位）
        String administrativeCode = String.format("%06d", input % 1000000);
        String economicTypeCode = String.format("%02d", (input / 1000000) % 100);
        String organizationTypeCode = String.format("%04d", (input / 100000000) % 10000);
        String managementCode = String.format("%1d", (input / 10000000000L) % 10);

        // 组合生成卫生机构代码
        String organizationCode = administrativeCode + economicTypeCode + organizationTypeCode + managementCode;

        // 检查生成的卫生机构代码是否符合规则
        if (organizationCode.length() != 13) {
            throw new IllegalArgumentException("生成的机构属性代码不符合规则");
        }

        // 这里可以添加更多逻辑以生成组织机构代码的剩余部分

        String orgCode = String.format("%09d", (input / 1000000000L) % 10);
        return organizationCode + orgCode; // 假设这里添加了9位组织机构代码
    }

    public static void main(String[] args) {
        String tag = "name1"; // 替换为您希望的整数输入
        String organizationCode = generateHealthOrganizationCode(tag);
        System.out.println("生成的卫生机构代码: " + organizationCode);
    }
}
