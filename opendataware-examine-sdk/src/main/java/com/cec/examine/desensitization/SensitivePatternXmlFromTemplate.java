package com.cec.examine.desensitization;
import java.util.regex.Matcher;

public class SensitivePatternXmlFromTemplate extends SensitivePatternRegex {

    private Template tpl;
    public SensitivePatternXmlFromTemplate(Template tpl){
        this.tpl = tpl;
    }

	@Override
    public String pattern() {
        String pattern = "(\\>)([\\/A-Za-z0-9-_.:\\s]*)(\\</)";
        return pattern;
    }

    @Override
    public String target(String source) {
        return source;
    }

    /**
     * 重新父类方法
     * @param m
     * @return
     */
    @Override
    public String target(Matcher m) {
        String target = m.group(0);
        String result;
        int index;
        int lastIndex;

        index = target.indexOf(">");
        lastIndex = target.lastIndexOf("/");
        result = target.substring(index+1, lastIndex-1);

        if (!(result == null || result.trim().length() == 0)) {
            target = ">" + tpl.target(m.group(3)) + "</";
        }
        return target;
    }

    /**
     * 模板处理的子类，需要重新desensitive方法，因为模板只能是走长文本处理
     * @param text 短文本，默认text就是脱敏对象本身
     * @return
     */
    @Override
    public String desensitive(String text) {
        return desensitive(text, true);
    }

}
