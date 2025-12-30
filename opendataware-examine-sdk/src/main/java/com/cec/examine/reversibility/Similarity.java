package com.cec.examine.reversibility;

import com.cec.examine.dict.AhoCorasickDoubleArrayTrie;
import com.cec.examine.template.TableCellPojo;
import com.cec.examine.util.MapCounter;

import java.util.*;

/**
 * 相似性审核
 */
public class Similarity extends Reversibility {

    private AhoCorasickDoubleArrayTrie<String> datResource = new AhoCorasickDoubleArrayTrie<String>();
    private AhoCorasickDoubleArrayTrie<String> datResult = new AhoCorasickDoubleArrayTrie<String>();
    private LinkedHashMap<String,String> mapResource = new LinkedHashMap<String,String>();
    private LinkedHashMap<String,String> mapResult = new LinkedHashMap<String,String>();
    private LinkedHashMap<String,String> queryResource = new LinkedHashMap<String,String>();
    private LinkedHashMap<String,String> queryResult = new LinkedHashMap<String,String>();


    //资源表字段平均长度
    private MapCounter<String> avgLengthResource = new MapCounter<String>();
    //结果表平均长度
    private MapCounter<String> avgLengthResult = new MapCounter<String>();

    private MapCounter<String> cntLengthResource = new MapCounter<String>();
    //结果表平均长度
    private MapCounter<String> cntLengthResult = new MapCounter<String>();

    private Long sizeResult = 0l;
    private Long sizeResource = 0l;

    /**
     * 添加元件结果的每一行
     */
    @Override
    public void addLineOfResult(Map<String, TableCellPojo> lineData) {
        for(String colum: lineData.keySet()) {
            Object objVal = lineData.get(colum).getValue();
            String queryTxt = queryResult.get(colum);
            if(objVal != null) {
                mapResult.put(String.valueOf(objVal), colum);
                if(queryTxt == null) queryTxt = "";
                queryTxt += String.valueOf(objVal);
                queryResult.put(colum, queryTxt);
                //长度记数
                avgLengthResult.incr(colum,(long)String.valueOf(objVal).length());
                cntLengthResult.incr(colum,1l);
            }
        }
    }
    /**
     * 添加元件资源的每一行
     */
    @Override
    public void addLineOfResource(Map<String, TableCellPojo> lineData) {
        for(String colum: lineData.keySet()) {
            Object objVal = lineData.get(colum).getValue();
            String queryTxt = queryResource.get(colum);
            if (objVal != null) {
                String sortString = sortString(String.valueOf(objVal));//先排序备用
                mapResource.put(String.valueOf(objVal), colum);
                mapResource.put(sortString, colum);//增加对局部排序的字符串
                if (queryTxt == null) queryTxt = "";
                queryTxt += String.valueOf(objVal);
                queryTxt += sortString;//增加对局部排序的字符串
                queryResource.put(colum, queryTxt);
                //长度记数
                avgLengthResource.incr(colum, (long) String.valueOf(objVal).length());
                cntLengthResource.incr(colum, (long) String.valueOf(objVal).length());
            }
        }
    }

    private String getFinalColumName(List<AhoCorasickDoubleArrayTrie.Hit<String>> r, MapCounter<String> avgLengthMap) {
        MapCounter<String> counter = new MapCounter<>();
        MapCounter<String> counterOfkey = new MapCounter<>();
        for(AhoCorasickDoubleArrayTrie.Hit<String> hit: r) {
            counter.incr(hit.value, (long) (hit.end-hit.begin));
            counterOfkey.incr(hit.value,1L);
        }
        //挑选最小差距的列名称
        String minLenColumName = null;
        long min = Long.MAX_VALUE;
        for(String c: counter.keySet()) {
            long avgLenOfHits = Math.abs(counter.get(c)/counterOfkey.get(c) - avgLengthMap.get(c));
            if(min > avgLenOfHits) {
                min = avgLenOfHits;
                minLenColumName = c;
            }
        }
        return minLenColumName;
    }

    public List<SimilarityResult> check() {
        //刷新资源表、结果表的平均长度
        for(String c: avgLengthResource.keySet()) {
            if(cntLengthResource.get(c) == 0) {
                avgLengthResource.put(c, 0L);
                continue;
            }
            avgLengthResource.put(c, avgLengthResource.get(c)/cntLengthResource.get(c));
        }
        for(String c: avgLengthResult.keySet()) {
            if(cntLengthResult.get(c) == 0) {
                avgLengthResult.put(c, 0L);
                continue;
            }
            avgLengthResult.put(c, avgLengthResult.get(c)/cntLengthResult.get(c));
        }

        List<SimilarityResult> results = new ArrayList<SimilarityResult>();
        datResource.build(mapResource);
        datResult.build(mapResult);
        for(String columName: queryResource.keySet()) {
            String queryTxt = queryResource.get(columName);
            if (queryTxt != null) {
                List<AhoCorasickDoubleArrayTrie.Hit<String>> r = datResult.parseText(queryTxt);
                Integer score = (int)(100d * Math.exp(r.size()) / (1 + Math.exp(r.size())));
                if (r.size() > 0) {
                    String resultCloumName = getFinalColumName(r, avgLengthResult);
                    String resouceCloumName = columName;
                    SimilarityResult similarityResult = new SimilarityResult();
                    similarityResult.setResultColumName(resultCloumName);
                    similarityResult.setResourceColumName(resouceCloumName);
                    similarityResult.setScore(score);
                    results.add(similarityResult);
                }
            }
        }
        for(String columName: queryResult.keySet()) {
            String queryTxt = queryResult.get(columName);
            if(queryTxt!=null) {
                List<AhoCorasickDoubleArrayTrie.Hit<String>> r = datResource.parseText(queryTxt);
                if(r.size() == 0 ) {
                    //如果正向未匹配那么反向尝试匹配
                    r = datResource.parseText(reverse(queryTxt));
                }

                if(r.size() == 0 ) {
                    //如果还未匹配尝试排序后匹配
                    r = datResource.parseText(sortString(queryTxt));
                }
                Integer score = (int)(100d * Math.exp(r.size()) / (1 + Math.exp(r.size())));
                if (r.size() > 0) {
                    String resultCloumName = getFinalColumName(r, avgLengthResource);
                    String resouceCloumName = columName;
                    SimilarityResult similarityResult = new SimilarityResult();
                    similarityResult.setResultColumName(resultCloumName);
                    similarityResult.setResourceColumName(resouceCloumName);
                    similarityResult.setScore(score);
                    results.add(similarityResult);
                }
            }
        }
        return results;
    }

    /**
     * 字符串反转倒排
     * @param str
     * @return
     */
    public static String reverse(String str) {
        char[] charArray = str.toCharArray();
        int left = 0;
        int right = charArray.length - 1;

        while (left < right) {
            // 交换左右两边的字符
            char temp = charArray[left];
            charArray[left] = charArray[right];
            charArray[right] = temp;

            // 移动指针
            left++;
            right--;
        }

        return new String(charArray);
    }


    /**
     * 字符排序
     * @param str
     * @return
     */
    public static String sortString(String str) {
        // 将字符串转换为字符数组
        char[] charArray = str.toCharArray();
        // 对字符数组进行排序
        Arrays.sort(charArray);
        // 将排序后的字符数组转换为字符串
        return new String(charArray);
    }
}
