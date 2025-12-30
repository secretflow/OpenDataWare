package com.cec.examine.desensitization;


import com.cec.examine.dict.HIT;
import com.cec.examine.recognize.Recognize;
import com.cec.examine.recognize.RecognizeDictionary;
import com.cec.examine.recognize.RecognizeRegularExpression;
import com.cec.examine.template.Config;
import com.cec.examine.template.Rule;

import java.util.ArrayList;
import java.util.List;

public class Template {
    public static enum TPL_CODE {
        /**
         *默认固定映射
         **/
        DEFAULT_FAKE,
        /**
         *默认遮盖算法
         **/
        DEFAULT_MASK,
        ID_FAKE, PHONE_FAKE, IPV4_FAKE, LAND_LINE_FAKE, BANK_CARD_FAKE,
        ID_MASK, PHONE_MASK, IPV4_MASK, LAND_LINE_MASK, BANK_CARD_MASK,
        EMAIL_MD5, EMAIL_MASK, EMPTY
    }

    private TPL_CODE tplCode;


    private String tplId;
    private List<Rule<?>> listRule;

    public Template(TPL_CODE tplCode) {
        this.tplCode = tplCode;
    }


    public Template(String tplId, List<Rule<?>> listRule) {
        this.tplId = tplId;
        this.listRule = listRule;
    }


    public String desensitive(String text) {
        return null;
    }


    public String target(String text) {
        if(null!=tplCode) {
            switch (tplCode) {
                case DEFAULT_FAKE:
                    listRule = FAKE_DEFAULT();
                    break;
                case DEFAULT_MASK:
                    listRule = MASK_DEFAULT();
                    break;
                default:


            }
        }
        String result = text;
        //先识别出类型，然后在脱敏
        for(Rule<?> rule: listRule) {
            Recognize recognize = (Recognize) rule.getRecognize();
            List<HIT> hints = recognize.getHitResult(text);
            if(hints != null && hints.size() > 0) {
                SensitiveAdaptor sensitiveAdaptor = new SensitiveAdaptor(rule);
                result = sensitiveAdaptor.desensitive(text);
                break;
            }
        }
        return result;
    }
    public String recognize(String text) {
        String result = text;
        //先识别出类型，然后在脱敏
        String name = null;
        for(Rule<?> rule: listRule) {
            Recognize recognize = (Recognize) rule.getRecognize();
            List<HIT> hints = recognize.getHitResult(text);
            if(hints != null && hints.size() > 0) {
                SensitiveAdaptor sensitiveAdaptor = new SensitiveAdaptor(rule);
                result = sensitiveAdaptor.desensitive(text);
                name = recognize.getSingleType(text);
                break;
            }
        }
        return name;
    }


    private static List<Rule<?>> FAKE_DEFAULT() {
        List<Rule<?>> listRules = new ArrayList<Rule<?>>();
        List<Config> configs = null;
        RecognizeRegularExpression r1 = new RecognizeRegularExpression();
        r1.setPattern("(?<=\\D|\\b)(\\d{16,19})(?=\\D|\\b)");
        r1.setCode("FIXED_FAKE001");
        r1.setName("银行卡号");
        Rule rule1 = new Rule("rule1", r1, "com.cec.examine.desensitization.fake.fixedfake.BankCardFixedFake", configs);
        RecognizeRegularExpression r2 = new RecognizeRegularExpression();
        r2.setPattern("(?<=\\D|\\b)([1-9])(\\d{5})(\\d{8})(\\d{3}[0-9Xx])(?=\\D|\\b)");
        r2.setCode("FIXED_FAKE002");
        r2.setName("身份证号码");
        Rule rule2 = new Rule("rule2",r2,"com.cec.examine.desensitization.fake.fixedfake.IDFixedFake",configs);
        RecognizeRegularExpression r3 = new RecognizeRegularExpression();

        r3.setPattern("(?<=(\\b|\\D))(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)(\\.)(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)(\\.)(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)(\\.)(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)(?=(\\b|\\D))");
        r3.setCode("FIXED_FAKE003");
        r3.setName("IPV4");
        Rule rule3 = new Rule("rule3",r3,"com.cec.examine.desensitization.fake.fixedfake.IPv4FixedFake",configs);
        RecognizeRegularExpression r4 = new RecognizeRegularExpression();

        r4.setPattern("(\\d{3,4}-\\d{7,8})");
        r4.setCode("FIXED_FAKE004");
        r4.setName("座机号");
        Rule rule4 = new Rule("rule4",r4,"com.cec.examine.desensitization.fake.fixedfake.LandLineFixedFake",configs);
        RecognizeRegularExpression r5 = new RecognizeRegularExpression();

        r5.setPattern("(?<=\\D|\\b)([1])([3][0-9]|[4][5-9]|[5][0-3,5-9]|[6][5,6]|[7][0-8]|[8][0-9]|[9][1,8,9])([0-9]{4})([0-9]{4})(?=\\D|\\b)");
        r5.setCode("FIXED_FAKE005");
        r5.setName("手机号");
        Rule rule5 = new Rule("rule5",r5,"com.cec.examine.desensitization.fake.fixedfake.PhoneFixedFake",configs);
        RecognizeRegularExpression r6 = new RecognizeRegularExpression();

        r6.setPattern("^[0-9]\\d{5}$");
        r6.setCode("FIXED_FAKE006");
        r6.setName("邮编");
        Rule rule6 = new Rule("rule6",r6,"com.cec.examine.desensitization.fake.fixedfake.PostalCodeFixedFake",configs);

        listRules.add(rule1);
        listRules.add(rule2);
        listRules.add(rule3);
        listRules.add(rule4);
        listRules.add(rule5);
        listRules.add(rule6);
        return listRules;
    }

    /**
     *随机映射
     **/
    private static List<Rule<?>> FAKE_RANDOM_DEFAULT() {
        List<Rule<?>> listRules = new ArrayList<Rule<?>>();
        List<Config> configs = null;
        RecognizeRegularExpression r = new RecognizeRegularExpression();
        Rule rule1 = new Rule("rule1", r, "com.cec.examine.desensitization.mask.IDMask", configs);
        RecognizeRegularExpression r2 = new RecognizeRegularExpression();
        Rule rule2 = new Rule("rule2",r2,"com.cec.examine.desensitization.fake.randomfake.AgeRandomFake",configs);
        listRules.add(rule1);
        listRules.add(rule2);
        return listRules;
    }

    /**
     *遮盖算法
     **/
    private static List<Rule<?>> MASK_DEFAULT() {
        List<Rule<?>> listRules = new ArrayList<Rule<?>>();
        List<Config> configs = null;
        RecognizeRegularExpression r1 = new RecognizeRegularExpression();
        r1.setPattern("^[\\u4e00-\\u9fa5省市市区县]+[\\u4e00-\\u9fa5a-zA-Z0-9]+[路街巷道弄号]?[\\u4e00-\\u9fa5a-zA-Z0-9]*号?$");
        r1.setCode("MASK001");
        r1.setName("住址遮盖");
        Rule rule1 = new Rule("rule1", r1, "com.cec.examine.desensitization.mask.AddressMask", configs);

        RecognizeRegularExpression r2 = new RecognizeRegularExpression();
        r2.setPattern("(?<=\\D|\\b)(\\d{16,19})(?=\\D|\\b)");
        r2.setCode("MASK002");
        r2.setName("银行卡遮盖");
        Rule rule2 = new Rule("rule2",r2,"com.cec.examine.desensitization.mask.BankCardMask",configs);

        RecognizeRegularExpression r3 = new RecognizeRegularExpression();
        r3.setPattern("(([A-Za-z0-9]{1,20}[-_\\.]){0,5}[a-zA-Z0-9\\.-]{1,20})@(([a-zA-Z0-9_-]{1,20})(\\.[a-zA-Z0-9_-]{1,20}){1,3})");
        r3.setCode("MASK003");
        r3.setName("邮箱遮盖");
        Rule rule3 = new Rule("rule3",r3,"com.cec.examine.desensitization.mask.EmailMask",configs);

        RecognizeRegularExpression r4 = new RecognizeRegularExpression();
        r4.setPattern("(?<=\\D|\\b)([1-9])(\\d{5})(\\d{8})(\\d{3}[0-9Xx])(?=\\D|\\b)");
        r4.setCode("MASK004");
        r4.setName("身份证号遮盖");
        Rule rule4 = new Rule("rule4",r4,"com.cec.examine.desensitization.mask.IDMask",configs);

        RecognizeRegularExpression r5 = new RecognizeRegularExpression();
        r5.setPattern("(?<=\\D|\\b)([1])([3][0-9]|[4][5-9]|[5][0-3,5-9]|[6][5,6]|[7][0-8]|[8][0-9]|[9][1,8,9])([0-9]{4})([0-9]{4})(?=\\D|\\b)");
        r5.setCode("MASK005");
        r5.setName("手机号遮盖");
        Rule rule5 = new Rule("rule5",r5,"com.cec.examine.desensitization.mask.PhoneMask",configs);
        listRules.add(rule1);
        listRules.add(rule2);
        listRules.add(rule3);
        listRules.add(rule4);
        listRules.add(rule5);
        return listRules;
    }




    }
