package com.cec.examine.recognize;
public class RecognizeNERLocal{}
/*
import ai.onnxruntime.*;
import com.cec.examine.dict.HIT;
import com.cec.examine.ner.BertNerTypeCode;
import com.cec.examine.ner.bert.BertTokenizer;
import com.cec.examine.ner.bert.LoadModel;
import com.cec.examine.util.ChineseUtil;

import java.util.*;

/**
 * @author qiuwenyi
 * @version 1.0.0
 * @ClassName Recognize
 * @Description NER识别类本地版本
 * @createTime 2023/10/08
 */
/*
public class RecognizeNERLocal extends Recognize {
    /**
     * B：开头、I：中间，S：单体，0是开头，PAD是结束
     * typeArr 是需要支持的NER识别实体类型
     */
/*   public RecognizeNERLocal(String [] typeArr) {
        this.typeArr = typeArr;
        id2label.put("0", "O");
        id2label.put("1", "B-address");
        id2label.put("2", "I-address");
        id2label.put("3", "B-book");
        id2label.put("4", "I-book");
        id2label.put("5", "B-company");
        id2label.put("6", "I-company");
        id2label.put("7", "B-game");
        id2label.put("8", "I-game");
        id2label.put("9", "B-government");
        id2label.put("10", "I-government");
        id2label.put("11", "B-movie");
        id2label.put("12", "I-movie");
        id2label.put("13", "B-name");
        id2label.put("14", "I-name");
        id2label.put("15", "B-organization");
        id2label.put("16", "I-organization");
        id2label.put("17", "B-position");
        id2label.put("18", "I-position");
        id2label.put("19", "B-scene");
        id2label.put("20", "I-scene");
        id2label.put("21", "S-address");
        id2label.put("22", "S-book");
        id2label.put("23", "S-company");
        id2label.put("24", "S-game");
        id2label.put("25", "S-government");
        id2label.put("26", "S-movie");
        id2label.put("27", "S-name");
        id2label.put("28", "S-organization");
        id2label.put("29", "S-position");
        id2label.put("30", "S-scene");
        id2label.put("31", "[PAD]");

        try {
            LoadModel.loadOnnxModel();
        } catch (OrtException e) {
            e.printStackTrace();
        }
    }

    private String[] typeArr;//需要识别的类型数组
    private Map<String, String> id2label = new HashMap<>();
    private BertTokenizer tokenizer = new BertTokenizer();
    @Override
    public String getSingleType(String text) {
        List<HIT> hits = getHitResult(text);
        if(hits.size() > 0 ) {
            String aliasType = hits.get(0).type;
            //无论address是否使用了别名，都进入该逻辑
            if(aliasType.equals(getTypeAlias(BertNerTypeCode.ADDRESS))) {
                //籍贯增加逻辑，如果NER识别是ADDRESS类型，那么需要使用正则再次识别是否是省市自治区，如果是则返回是籍贯类型
                Recognize recognizeReg = new RecognizeRegularExpression("[^省]+省|.+自治区|[^澳门]+澳门|北京|重庆|上海|天津|台湾|[^香港]+香港|[^市]+市");
                recognizeReg.setCode(BertNerTypeCode.NATIVE_PLACE);
                recognizeReg.setName("籍贯");
                List<HIT> r = recognizeReg.getHitResult(text);
                if(r.size() > 0) {
                    aliasType = getTypeAlias(BertNerTypeCode.NATIVE_PLACE);
                }
            }
            return aliasType;
        } else return null;
    }

    @Override
    public List<String> getTypeOfLongText(String text) {
        List<HIT> hits = getHitResult(text);
        if(hits.size() > 0 ) {
            Set<String> typeSet = new HashSet<String>();
            for(HIT hit: hits) {
                typeSet.add(hit.type);
            }
            return new ArrayList<String>(typeSet);
        } else return  new ArrayList<String>();
    }

    @Override
    public List<HIT> getHitResult(String text) {
        List<HIT> result = new ArrayList<HIT>();//定义结果对象
        //如果不包含汉字，则退出匹配
        if(!ChineseUtil.containChineseCharacter(text)) return result;
        Map<String, OnnxTensor> data = parseInputText(text);
        List<String> tokens = tokenizer.tokenize(text);
        try {
            OrtSession session = LoadModel.session;
            OrtSession.Result probabilities = session.run(data);
//            System.out.println(probabilities);
            OnnxValue onnxValue = probabilities.get(0);
//            System.out.println(onnxValue.getValue());
            float[][][] labels = (float[][][]) onnxValue.getValue();
            for (int idx = 1; idx < labels[0].length - 1; idx++) {
                if("[SEP]".equals( tokens.get(idx))) continue;//跳过结尾符号
                float[] labelsIdx = labels[0][idx];
                double maxValue = Math.abs(labelsIdx[0]);
                int index = 0;
                for (int i = 1; i < labelsIdx.length - 1; i++) {
                    if (Math.abs(labelsIdx[i]) > maxValue) {
                        maxValue = Math.abs(labelsIdx[i]);
                        index = i;
                    }
                }
                String label = id2label.get(String.valueOf(index));
                for (int k = 0; k < this.typeArr.length; k++) {
                    if (label != null) {
                        String [] l = label.split("-", -1);
                        if (l.length >= 2) {
                            if (this.typeArr[k].equals(l[1])) {
                                if ("B".equals(l[0]) || "S".equals(l[0]) || ("I".equals(l[0]) && result.size() == 0) ) {
                                    //实体的起始位置
                                    HIT hit = new HIT();
                                    hit.start =  idx - 1;
                                    hit.end = idx;
                                    hit.target = "?";
                                    hit.source = tokens.get(idx);
                                    //如果存在别名设置，则直接显示别名即可
                                    hit.type = getTypeAlias(l[1]);
                                    result.add(hit);
                                } else if ("I".equals(l[0])) {
                                    //上一个实体连续
                                    HIT hit = result.get(result.size() - 1);
                                    hit.end++;
                                    hit.source = hit.source + tokens.get(idx);
                                }
                                break;
                            }
                        }
                    }
                }
            }
        } catch (OrtException e) {
            e.printStackTrace();
        }
        return result;
    }

    private Map<String, OnnxTensor> parseInputText(String text) {
        try {
            OrtEnvironment env = LoadModel.env;
            List<String> tokens = tokenizer.tokenize(text);
            List<Integer> tokenIds = tokenizer.convert_tokens_to_ids(tokens);
            int max_shape=64;
            long[] inputIds = new long[max_shape];
            for (int index = 0; index < tokenIds.size(); index++) {
                inputIds[index] = tokenIds.get(index);
            }//这里应该补到长度为128
            long[] shape = new long[]{1, inputIds.length};//(1,128)
            Object ObjInputIds = OrtUtil.reshape(inputIds, shape);
            OnnxTensor input_ids = OnnxTensor.createTensor(env, ObjInputIds);
            Map<String, OnnxTensor> input_data = new HashMap<>();//(1,64)
            input_data.put("input", input_ids);
            return input_data;
        } catch (OrtException e) {
            e.printStackTrace();
        }
        return null;
    }
}*/