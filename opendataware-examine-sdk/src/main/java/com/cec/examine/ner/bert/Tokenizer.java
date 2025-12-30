package com.cec.examine.ner.bert;

import java.util.List;

public interface Tokenizer {
    public List<String> tokenize(String text);
}
