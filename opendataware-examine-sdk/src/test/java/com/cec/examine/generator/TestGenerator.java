package com.cec.examine.generator;

import com.cec.examine.desensitization.SensitivePattern;
import com.cec.examine.desensitization.generator.*;
import com.cec.examine.template.desensitization.AnalysisResultForEnum;
import com.cec.examine.template.desensitization.AnalysisResultForNumeric;
import com.cec.examine.util.MockDataSet;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * 固定仿真测试
 */
public class TestGenerator {
    /**
     * 座机号固定仿真
     */
    @Test
    public void testGeneratorLandLineNum(){
        GeneratorLandlineNumber generatorLandlineNumber = new GeneratorLandlineNumber();
        for (int i = 0; i <10; i++) {
            String landLineNum = generatorLandlineNumber.generateFixed("座机号"+i);
            System.out.println("生成固定的座机号码:"+landLineNum);

        }
    }

    /**
     * 固定邮箱生成
     */
    @Test
    public void testGeneratorEmail(){
        GeneratorEmail generatorEmail = new GeneratorEmail();
        for (int i = 0; i < 10; i++) {
            String email = generatorEmail.generateFixed("邮箱"+1);
            System.out.println("生成的固定邮箱是:"+email);
        }
    }

    /**
     * 姓名固定生成
     */
    @Test
    public void testGeneratorName(){
        GeneratorName generatorName = new GeneratorName();
        for (int i = 0; i < 10; i++) {
            String name = generatorName.generateFixed("姓名"+1);
            System.out.println("生成的名字是:"+name);

        }
    }

    /**
     * 企业名称固定生成
     */
    @Test
    public void testGeneratorEnterpriseName(){
        GeneratorEnterpriseName generatorEnterpriseName = new GeneratorEnterpriseName();
        for (int i = 0; i < 10; i++) {
            String name = generatorEnterpriseName.generateFixed("企业名称"+i);
            System.out.println("生成的企业名称是:"+name);

        }
    }

    /**
     * 手机号固定仿真
     */

    @Test
    public void testGeneratoPhone(){
        Generator generator = new GeneratorPhone();
        for (int i = 0; i < 10; i++) {
            String name = generator.generateFixed("手机号");
            System.out.println("生成的固定手机号是:"+name);

        }
    }
    @Test
    public void testMockDataUtil(){
        System.out.println(new MockDataSet(MockDataSet.getExampleBuilder()).dataMockJSON(10));
    }

    /**
     * 可枚举的生成
     */
    @Test
    public void testGeneratorGeneralString(){
        AnalysisResultForEnum analysisResultForEnum = new AnalysisResultForEnum(Arrays.asList(null, "NULL", "", null, "A",  "C", "B", "B", "B", "B"));
        GeneratorGeneralString generatorGeneralString = new GeneratorGeneralString(analysisResultForEnum.getResult(), true, true);
        int cnt = 0;
        for (int i = 0; i < 100; i++) {
            String n = generatorGeneralString.generateFixed(String.valueOf(i));
            if( n == null) {
                cnt++;
            }
        }
        assertTrue(cnt > 0);
    }

    /**
     * 性别可枚举
     */
    @Test
    public void testGeneratorGeneralStringGender(){
        AnalysisResultForEnum analysisResultForEnum = new AnalysisResultForEnum(Arrays.asList(null, "M", "F", "M", "M",  "M", "M", "M", "M", "M"));
        GeneratorGeneralString generatorGeneralString = new GeneratorGeneralString(analysisResultForEnum.getResult(), true, true);
        int cnt = 0;
        for (int i = 0; i < 100; i++) {
            String n = generatorGeneralString.generateFixed(String.valueOf(i));
            System.out.println(n);
            if( n == null) {
                cnt++;
            }
        }
    }

    /**
     * 不可枚举的生成
     */
    @Test
    public void testGeneratorGeneralStringUnEnum(){
        List<String> ids = new ArrayList<>();
        Generator generator = new GeneratorID();
        for(int i=0;i<200;i++){
            ids.add(generator.generateFixed(String.valueOf(i)));
            if(i > 180) ids.add(null);
        }
        AnalysisResultForEnum analysisResultForEnum = new AnalysisResultForEnum(ids);
        GeneratorGeneralString generatorGeneralString = new GeneratorGeneralString(analysisResultForEnum.getResult(), generator, true, true);
        int cnt = 0;
        for (int i = 0; i < 100; i++) {
            String n = generatorGeneralString.generateFixed(String.valueOf(i));
            if( n == null) {
                cnt++;
            }
        }
        assertTrue(cnt > 0);
    }

    @Test
    public void testGeneratorGeneralNumeric(){
        AnalysisResultForNumeric<Integer> analysisResultForNumeric = new AnalysisResultForNumeric<Integer>(Arrays.asList(null, -1, 0, null, 1,  4, 6, 5, 8, 9), Integer.class);
        GeneratorGeneralNumeric<Integer> generatorGeneralNumeric = new GeneratorGeneralNumeric<Integer>(analysisResultForNumeric.getResult(), Integer.class);
        for (int i = 0; i < 100; i++) {
            generatorGeneralNumeric.generateFixed(String.valueOf(i));
        }
    }

    /**
     * 测试通用脱敏算子
     */
    @Test
    public void testGeneralEmulation(){
        List<String> ids = new ArrayList<>();
        Generator generator = new GeneratorID();
        for(int i=0;i<200;i++){
            ids.add(generator.generateFixed(String.valueOf(i)));
            if(i > 180) ids.add(null);
        }
        AnalysisResultForEnum analysisResultForEnum = new AnalysisResultForEnum(ids);
        System.out.println(analysisResultForEnum.toString());
        SensitivePattern generalEmulation = new GeneralEmulation("varchar(18)", analysisResultForEnum.toString());
        int cnt = 0;
        for(String id: ids) {
            String id2 = generalEmulation.desensitive(id, false);
            if(id2 == null) cnt++;
        }
        assertTrue(cnt > 0);
    }
}
