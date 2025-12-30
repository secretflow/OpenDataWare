package com.cec.examine.util.jsonparser;


import com.cec.examine.util.jsonparser.model.JsonArray;
import com.cec.examine.util.jsonparser.model.JsonObject;
import com.cec.examine.util.jsonparser.parser.Parser;
import com.cec.examine.util.jsonparser.tokenizer.CharReader;
import com.cec.examine.util.jsonparser.tokenizer.TokenList;
import com.cec.examine.util.jsonparser.tokenizer.Tokenizer;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.Map;
/**
 * Created by code4wt on 17/9/1.
 */
public class JSONParser {

    private Tokenizer tokenizer = new Tokenizer();

    private Parser parser = new Parser();

    public Object fromJSON(String json) throws IOException {
        json = json.replaceAll("[\r\n]","");
        CharReader charReader = new CharReader(new StringReader(json));
        TokenList tokens = tokenizer.tokenize(charReader);
        return parser.parse(tokens);
    }

    public JsonArray fromList(List list) {
        JsonArray jsonArray = new JsonArray();
        jsonArray.addAll(list);
        return jsonArray;
    }

    public JsonObject fromMap(Map<String, Object> map) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.putAll(map);
        return jsonObject;
    }


}
