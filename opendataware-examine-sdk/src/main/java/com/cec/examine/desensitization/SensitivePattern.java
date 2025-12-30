package com.cec.examine.desensitization;

import com.cec.examine.dict.HIT;
import com.cec.examine.template.Config;
import java.util.List;
import java.util.Optional;

/**
 * 虚基类，定义了模式匹配结果和组装逻辑
 * 定义了脱敏接口
 *
 * @author wenyiqiu
 */
public abstract class SensitivePattern {

    protected String text;
    protected List<Config> configs;
    // 盐值
    protected String salt;

    public static Integer getIsDegeneration() {
        return isDegeneration;
    }

    public static void setIsDegeneration(Integer isDegeneration) {
        SensitivePattern.isDegeneration = isDegeneration;
    }

    protected static Integer isDegeneration;

    //长文本脱敏模式默认true为开启，false为关闭
    protected boolean defaultLongTextMode = true;

    public String desensitive(String text) {
        return desensitive(text, this.defaultLongTextMode);
    }
    public abstract String desensitive(String text, boolean longText);



    public void cleanConfigs() {
        this.configs = null;
    }

    public void cleanSalt() {
        this.salt = null;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public void setConfigs(List<Config> configs) {
        this.configs = configs;
    }


    /**
     * 组装
     *
     * @param res  模式匹配结果
     * @param text 操作文本
     * @return
     */
    public static String processHIT(List<HIT> res, String text) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (HIT h : res) {
            if (i < h.start) {
                sb.append(text.subSequence(i, h.start));
                i = h.start;
            }
            if (i == h.start) {
                sb.append(h.target);
                i = h.end;
            }
        }
        if (i < text.length()&& null ==isDegeneration) {
            sb.append(text.subSequence(i, text.length()));
        }
        return sb.toString();
    }


    protected String getSaltedValue(String source) {
        return Optional.ofNullable(this.salt)
                .filter(s -> !s.isEmpty())
                .map(t -> source + t)
                .orElse(source);
    }

}
