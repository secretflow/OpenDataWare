package com.cec.examine.desensitization.generator;

import com.cec.examine.desensitization.SensitivePatternRegex;
import com.cec.examine.template.desensitization.AnalysisForTable;
import com.cec.examine.template.desensitization.AnalysisResultForEnum;
import com.cec.examine.template.desensitization.AnalysisResultForNumeric;

/**
 * 仿真脱敏/通用仿真脱敏
 */
public class GeneralEmulation extends SensitivePatternRegex {

    private Generator generator;

    private void init(String dataType, String explorationResultJSONString, boolean coverEmptyValue, boolean coverPeekValue, boolean coverAllEnum) {
        if(AnalysisForTable.isNumeric(dataType)){
            //数字
            switch (dataType = dataType.toLowerCase().replaceAll("\\(.*?\\)", "")) {
                case "integer":
                case "int":
                case "short":
                    generator = new GeneratorGeneralNumeric<Integer>(new AnalysisResultForNumeric.NumericResult<Integer>(explorationResultJSONString), Integer.class);
                    break;
                case "float":
                    generator = new GeneratorGeneralNumeric<Float>(new AnalysisResultForNumeric.NumericResult<Float>(explorationResultJSONString), Float.class);
                    break;
                case "long":
                case "unsigned":
                    generator = new GeneratorGeneralNumeric<Long>(new AnalysisResultForNumeric.NumericResult<Long>(explorationResultJSONString), Long.class);
                    break;
                case "byte":
                    generator = new GeneratorGeneralNumeric<Byte>(new AnalysisResultForNumeric.NumericResult<Byte>(explorationResultJSONString), Byte.class);
                    break;
                default:
                    generator = new GeneratorGeneralNumeric<Double>(new AnalysisResultForNumeric.NumericResult<Double>(explorationResultJSONString), Double.class);
                    break;
            }
            //覆盖空值，极值设置
            ((GeneratorGeneralNumeric<?>) generator).setCoverEmptyValue(coverEmptyValue);
            ((GeneratorGeneralNumeric<?>) generator).setCoverPeekValue(coverPeekValue);
        } else {
            //字符串
            generator = new GeneratorGeneralString(new AnalysisResultForEnum(null).getResult(explorationResultJSONString), new GeneratorCopy(), coverAllEnum, coverEmptyValue);
        }
    }

    public GeneralEmulation(String dataType, String explorationResultJSONString, boolean coverEmptyValue, boolean coverPeekValue, boolean coverAllEnum) {
        init(dataType,explorationResultJSONString, coverEmptyValue, coverPeekValue, coverAllEnum);
    }
    public GeneralEmulation(String dataType, String explorationResultJSONString) {
        init(dataType,explorationResultJSONString, true, true, true);
    }

    @Override
    public String pattern() {
        return null;
    }

    @Override
    public String target(String source) {
        return generator.generateFixed(source);
    }
}
