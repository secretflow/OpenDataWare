package com.cec.examine.reversibility;

import com.cec.examine.util.jsonparser.ObjectToJSON;

public class ReversibilityResult {


    public String toString() {
        return ObjectToJSON.toJSONString(this);
    }

    public Object toJSONObject() {
        return ObjectToJSON.toJSON(this);
    }
}
