package com.cec.examine.recognize;

import com.cec.examine.dict.AhoCorasickDoubleArrayTrie;
import com.cec.examine.dict.HIT;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * @author qiuwenyi
 * @version 1.0.0
 * @ClassName Recognize
 * @Description 字典识别，多词对应一个类型，适用于词匹配场景。例如黄暴恐
 * @createTime 2023/10/08
 */
public class RecognizeDictionary extends Recognize {

    public RecognizeDictionary(String code, String name, List<String> words) {
        this.setName(name);
        this.setCode(code);
        this.loadWords(words);
    }

    public RecognizeDictionary() {
    }

    /**
     * 获取识别结果
     * @param text
     * @return
     */
    @Override
    public List<HIT> getHitResult(String text) {
        if(text == null || "".equals(text)) return null;
        List<HIT> res = new ArrayList<HIT>();
        List<AhoCorasickDoubleArrayTrie.Hit<String>> wordList = dictSet.parseText(text);
        TreeMap<Integer,Integer> statusMap = new TreeMap<Integer,Integer>();

        //建表
        for(AhoCorasickDoubleArrayTrie.Hit<String> word: wordList) {
            for(int index = word.begin; index <= word.end; index++) {
                Integer length = statusMap.get(index);
                if(length == null) length = 0;
                int wordLen = word.end - word.begin;
                if(length < wordLen) {
                    statusMap.put(index, wordLen);
                }
            }
        }
        //查表
        for(AhoCorasickDoubleArrayTrie.Hit<String> word: wordList) {
            for(int index = word.begin; index <= word.end; index++) {
                Integer length = statusMap.get(index);
                if(length == null) length = 0;
                int wordLen = word.end - word.begin;
                if(length == wordLen) {
                    //说明匹配，保留当前词
                    HIT h = new HIT();
                    h.start = word.begin;
                    h.end = word.end;
                    h.source = text.substring(word.begin, word.end);
                    h.target = "?";
                    h.type = this.getName();
                    res.add(h);
                    break;
                }
            }
        }
        return res;
    }
    //这个字典树是多识别规则共享
    protected AhoCorasickDoubleArrayTrie<String> dictSet = new AhoCorasickDoubleArrayTrie<String>();

    public void loadWords(List<String> words) {
        if(words.size() == 0) {
            return;
        }
        TreeMap<String,String> map = new TreeMap<>();
        for(String word : words) {
            map.put(word,this.getName());
        }
        dictSet.build(map);
    }

    @Override
    public String getSingleType(String text) {
        if(text == null || "".equals(text)) return null;
        List<HIT> res = new ArrayList<HIT>();
        List<AhoCorasickDoubleArrayTrie.Hit<String>> wordList = dictSet.parseText(text);
        TreeMap<Integer,Integer> statusMap = new TreeMap<Integer,Integer>();

        //建表
        for(AhoCorasickDoubleArrayTrie.Hit<String> word: wordList) {
            for(int index = word.begin; index <= word.end; index++) {
                Integer length = statusMap.get(index);
                if(length == null) length = 0;
                int wordLen = word.end - word.begin;
                if(length < wordLen) {
                    statusMap.put(index, wordLen);
                }
            }
        }
        //查表
        for(AhoCorasickDoubleArrayTrie.Hit<String> word: wordList) {
            for(int index = word.begin; index <= word.end; index++) {
                Integer length = statusMap.get(index);
                if(length == null) length = 0;
                int wordLen = word.end - word.begin;
                if(length == wordLen  && this.getName().equals(word.value)) {
                    //说明匹配，保留当前词
                   return this.getName();
                }
            }
        }
        return null;
    }

    @Override
    public List<String> getTypeOfLongText(String text) {
        List<String> types = new ArrayList<String>();
        String type = getSingleType(text);
        if(type != null) types.add(type);
        return types;
    }

}
