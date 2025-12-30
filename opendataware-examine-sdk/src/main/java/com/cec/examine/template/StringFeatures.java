package com.cec.examine.template;

import com.cec.examine.util.jsonparser.ObjectToJSON;

/**
 * 字符串特征提取
 */
public class StringFeatures {
    public String toString() {
        return ObjectToJSON.toJSONString(this);
    }

    public Object toJSONObject() {
        return ObjectToJSON.toJSON(this);
    }

    public static StringFeatures getStringFeatures(String s) {
        StringFeatures stringFeatures = new StringFeatures();
        int cntOfNumber = 0;
        int cntOfEnglish = 0;
        for(int i=0;i<s.length();i++) {
            int n = (int)s.charAt(i);
            if(n >= 48 && n <= 57) {
                stringFeatures.setHasNumber(true);
                cntOfNumber++;
            }
            else if((n >= 65 && n <= 90) || (n >= 97 && n <= 122)) {
                stringFeatures.setHasEnglish(true);
                cntOfEnglish++;
            }
        }
        stringFeatures.setLength(s.length());
        if(cntOfNumber == s.length()) stringFeatures.setAllNumber(true);
        else if(cntOfEnglish == s.length()) stringFeatures.setAllEnglish(true);
        return stringFeatures;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public boolean isHasEnglish() {
        return hasEnglish;
    }

    public void setHasEnglish(boolean hasEnglish) {
        this.hasEnglish = hasEnglish;
        if(hasEnglish) this.binCode[1] = '1';
    }

    public boolean isHasNumber() {
        return hasNumber;
    }

    public void setHasNumber(boolean hasNumber) {
        this.hasNumber = hasNumber;
        if(hasNumber) this.binCode[0] = '1';
    }

    public boolean isAllEnglish() {
        return allEnglish;
    }

    public void setAllEnglish(boolean allEnglish) {
        this.allEnglish = allEnglish;
        if(allEnglish) this.binCode[3] = '1';
    }

    public boolean isAllNumber() {
        return allNumber;
    }

    public void setAllNumber(boolean allNumber) {
        this.allNumber = allNumber;
        if(allNumber) this.binCode[2] = '1';
    }

    private int length = 0;
    private boolean hasEnglish = false;
    private boolean hasNumber = false;
    private boolean allEnglish = false;
    private boolean allNumber = false;
    private char [] binCode = {'0','0','0','0'};

    public String getBinCode() {
        return new String(binCode);
    }
}
