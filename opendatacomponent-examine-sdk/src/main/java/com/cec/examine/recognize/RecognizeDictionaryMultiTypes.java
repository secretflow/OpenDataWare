package com.cec.examine.recognize;

import com.cec.examine.dict.AhoCorasickDoubleArrayTrie;
import com.cec.examine.dict.HIT;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author qiuwenyi
 * @version 1.0.0
 * @ClassName Recognize
 * @Description 字典识别，支持对类型返回
 * 在0.0.4版本中废弃使用
 * @createTime 2023/10/08
 */
@Deprecated
public class RecognizeDictionaryMultiTypes extends Recognize {

    private boolean exactly;
    /**
     * 初始化构造函数
     * @param wordsToType 词=>类型对
     * @param exactly 如果为true，那么需要精确匹配，如果为false，则为长文本匹配
     */
    public RecognizeDictionaryMultiTypes(Map<String,String> wordsToType, boolean exactly) {
        if(wordsToType.size() > 0) {
            dictSet = new AhoCorasickDoubleArrayTrie<String>();
            dictSet.build(wordsToType);
            this.exactly = exactly;
        }
    }

    /**
     * 获取识别结果
     * @param text
     * @return
     */
    @Override
    public List<HIT> getHitResult(String text) {
        if(text == null || "".equals(text) || dictSet == null) return null;
        List<HIT> res = new ArrayList<HIT>();
        List<AhoCorasickDoubleArrayTrie.Hit<String>> wordList = dictSet.parseText(text);
        TreeMap<Integer,Integer> statusMap = new TreeMap<Integer,Integer>();

        if(!this.exactly) {
            //走长文本匹配模式
            //建表
            for (AhoCorasickDoubleArrayTrie.Hit<String> word : wordList) {
                for (int index = word.begin; index <= word.end; index++) {
                    Integer length = statusMap.get(index);
                    if (length == null) length = 0;
                    int wordLen = word.end - word.begin;
                    if (length < wordLen) {
                        statusMap.put(index, wordLen);
                    }
                }
            }
            //查表
            for (AhoCorasickDoubleArrayTrie.Hit<String> word : wordList) {
                for (int index = word.begin; index <= word.end; index++) {
                    Integer length = statusMap.get(index);
                    if (length == null) length = 0;
                    int wordLen = word.end - word.begin;
                    if (length == wordLen) {
                        //说明匹配，保留当前词
                        HIT h = new HIT();
                        h.start = word.begin;
                        h.end = word.end;
                        h.source = text.substring(word.begin, word.end);
                        h.target = "?";
                        h.type = word.value;
                        res.add(h);
                        break;
                    }
                }
            }
            return res;
        } else {
            //走精确匹配模式
            for (AhoCorasickDoubleArrayTrie.Hit<String> word : wordList) {
                if(word.begin == 0 && word.end == text.length()) {
                    HIT h = new HIT();
                    h.start = word.begin;
                    h.end = word.end;
                    h.source = text.substring(word.begin, word.end);
                    h.target = "?";
                    h.type = word.value;
                    res.add(h);
                }
            }
            return res;
        }
    }
    //这个字典树是多识别规则共享
    protected AhoCorasickDoubleArrayTrie<String> dictSet;

    @Override
    public String getSingleType(String text) {
        if(text == null || "".equals(text) || dictSet == null) return null;
        List<HIT> wordList = getHitResult(text);
        return wordList != null && wordList.size() > 0 ?  wordList.get(0).getType(): null;
    }

    @Override
    public List<String> getTypeOfLongText(String text) {
        List<String> types = new ArrayList<String>();
        String type = getSingleType(text);
        if(type != null) types.add(type);
        return types;
    }

}
