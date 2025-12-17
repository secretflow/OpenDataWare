package com.cec.examine.desensitization;

import com.cec.examine.template.TableCellPojo;
import com.cec.examine.template.desensitization.DesensitizationConfig;
import com.cec.examine.template.desensitization.DesensitizationTemplate;
import com.cec.examine.util.MockDataSet;
import com.cec.examine.util.jsonparser.JSONParser;
import com.cec.examine.util.jsonparser.model.JsonArray;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;


/**
 * DesensitizationTemplate配套测试
 */
public class DesensitizationTemplateTest {

    private final static boolean coverAllEnum = false;
    private final static boolean coverEmptyValue = false;
    private final static boolean coverPeekValue = false;
    private static final String configJSON =
            "[" +
                    "{" +
                    "     \"colName\": \"id\"," +
                    "     \"colNameCn\": \"身份证\"," +
                    "     \"colType\": \"string\"," +
                    "     \"comment\": \"身份证\"," +
                    "     \"identifyLabel\": \"身份证\"," +
                    "     \"maskMethod\": \"GeneratorID\"," +
                    "     \"maskType\": \"固定仿真脱敏\"," +
                    "     \"maskTag\": \"a\"," +
                    "     \"coverAllEnum\": " + coverAllEnum + "," +
                    "     \"coverEmptyValue\": " + coverEmptyValue + "," +
                    "     \"coverPeekValue\": " + coverPeekValue + "," +
                    "     \"sensitivePersonalInformationStatus\": 1," +
                    "     \"explorationJson\":{\"cnt\":3000, \"emp\":0.1}" +
                    "}," +
                    "{" +
                    "     \"colName\": \"age\"," +
                    "     \"colNameCn\": \"年龄\"," +
                    "     \"colType\": \"int(3)\"," +
                    "     \"comment\": \"年龄\"," +
                    "     \"identifyLabel\": \"年龄\"," +
                    "     \"maskMethod\": \"GeneralEmulation\"," +
                    "     \"maskType\": \"通用仿真脱敏\"," +
                    "     \"maskTag\": \"a\"," +
                    "     \"coverAllEnum\": " + coverAllEnum + "," +
                    "     \"coverEmptyValue\": " + coverEmptyValue + "," +
                    "     \"coverPeekValue\": " + coverPeekValue + "," +
                    "     \"sensitivePersonalInformationStatus\": 1," +
                    "     \"explorationJson\":{\"cnt\":3000,\"emp\":0.1,\"p1\":5,\"p25\":25,\"p50\":40,\"p75\":50,\"p99\":81,\"min\":0,\"max\":110}" +
                    "}," +
                    "{" +
                    "     \"colName\": \"gender\"," +
                    "     \"colNameCn\": \"性别\"," +
                    "     \"colType\": \"VARCHAR(2)\"," +
                    "     \"comment\": \"性别\"," +
                    "     \"identifyLabel\": \"性别\"," +
                    "     \"maskMethod\": \"GeneralEmulation\"," +
                    "     \"maskType\": \"通用仿真脱敏\"," +
                    "     \"maskTag\": \"a\"," +
                    "     \"coverAllEnum\": " + coverAllEnum + "," +
                    "     \"coverEmptyValue\": " + coverEmptyValue + "," +
                    "     \"coverPeekValue\": " + coverPeekValue + "," +
                    "     \"sensitivePersonalInformationStatus\": 1," +
                    "     \"explorationJson\":{\"cnt\":3000,\"emp\":0.1,\"M\":0.8,\"F\":0.1}" +
                    "}," +
                    "{"  +
                    "     \"colName\": \"phone\"," +
                    "     \"colNameCn\": \"手机号\"," +
                    "     \"colType\": \"VARCHAR(15)\"," +
                    "     \"comment\": \"手机号\"," +
                    "     \"identifyLabel\": \"手机号\"," +
                    "     \"maskMethod\": \"GeneralMask\"," +
                    "     \"maskType\": \"通用遮蔽脱敏\"," +
                    "     \"maskTag\": \"a\"," +
                    "     \"coverAllEnum\": " + coverAllEnum + "," +
                    "     \"coverEmptyValue\": " + coverEmptyValue + "," +
                    "     \"coverPeekValue\": " + coverPeekValue + "," +
                    "     \"sensitivePersonalInformationStatus\": 1," +
                    "     \"explorationJson\":{\"cnt\":3000, \"emp\":0}," +
                    "     \"maskMethodDetail\": {" +
                    "       \"mode\": \"xToY\"," +
                    "       \"beginIndex\": 2," +
                    "       \"endAfter\": 8," +
                    "       \"maskChar\": \"*\"" +
                            "}" +
                    "}," +
                    "{"  +
                    "     \"colName\": \"name\"," +
                    "     \"colNameCn\": \"姓名\"," +
                    "     \"colType\": \"VARCHAR(5)\"," +
                    "     \"comment\": \"姓名\"," +
                    "     \"identifyLabel\": \"姓名\"," +
                    "     \"maskMethod\": \"GeneratorName\"," +
                    "     \"maskType\": \"固定仿真脱敏\"," +
                    "     \"maskTag\": \"a\"," +
                    "     \"coverAllEnum\": " + coverAllEnum + "," +
                    "     \"coverEmptyValue\": " + coverEmptyValue + "," +
                    "     \"coverPeekValue\": " + coverPeekValue + "," +
                    "     \"sensitivePersonalInformationStatus\": 1," +
                    "     \"explorationJson\":{\"cnt\":3000, \"emp\":0.1 }" +
                    "}," +
                    "{" +
                    "     \"colName\": \"md5\"," +
                    "     \"colNameCn\": \"哈希值\"," +
                    "     \"colType\": \"VARCHAR(32)\"," +
                    "     \"comment\": \"哈希值\"," +
                    "     \"identifyLabel\": \"哈希值\"," +
                    "     \"maskMethod\": \"GeneralHash\"," +
                    "     \"maskType\": \"通用哈希脱敏\"," +
                    "     \"maskTag\": \"a\"," +
                    "     \"coverAllEnum\": " + coverAllEnum + "," +
                    "     \"coverEmptyValue\": " + coverEmptyValue + "," +
                    "     \"coverPeekValue\": " + coverPeekValue + "," +
                    "     \"sensitivePersonalInformationStatus\": 1," +
                    "     \"explorationJson\":{\"cnt\":3000, \"emp\":0.5 }" +
                    "}" +
        "]";

    @Test
    public void testPrintJson() {
        System.out.println(configJSON);
    }

    @Test
    public void testJsonParse() {
        JSONParser jsonParser = new JSONParser();
        try {
            JsonArray jsonArray = (JsonArray) jsonParser.fromJSON(configJSON);
            for (int i = 0; i < jsonArray.size(); i++) {
                //脱敏配置
                DesensitizationConfig desensitizationConfig = new DesensitizationConfig(jsonArray.getJsonObject(i));
                //不为空的字段
                assertNotNull(desensitizationConfig.getColName());
                assertNotNull(desensitizationConfig.getColType());
                assertNotNull(desensitizationConfig.getExplorationJson());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDesensitizationTemplate() {
        MockDataSet mockDataSet = new MockDataSet(MockDataSet.getExampleBuilder());
        List<Map<String, TableCellPojo>> inputData =  mockDataSet.mockListMap(200);
        MockDataSet.print(inputData);

        DesensitizationTemplate desensitizationTemplate = new DesensitizationTemplate(configJSON);
        List<Map<String, TableCellPojo>> result = desensitizationTemplate.de(inputData);
        MockDataSet.print(result);
    }
}
