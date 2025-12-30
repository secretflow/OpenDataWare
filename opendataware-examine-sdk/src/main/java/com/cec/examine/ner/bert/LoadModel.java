package com.cec.examine.ner.bert;
public class LoadModel {}
/*
import ai.onnxruntime.OrtEnvironment;
import ai.onnxruntime.OrtException;
import ai.onnxruntime.OrtSession;
import com.cec.examine.ner.util.PropertiesReader;
import com.cec.examine.util.FileUtilityUtil;

import java.util.Optional;

public class LoadModel {

    public static OrtSession session;
    public static OrtEnvironment env;
    /**
     * load onnx model
     * @throws OrtException
     *//*
    public static void loadOnnxModel() throws OrtException {
        //System.out.println("load onnx model...");
        String userDir = System.getProperty("user.dir");
        String onnxPath = userDir + "/double_loss_no_crf_onnx_aa.onnx";
        //模型加载到本地
        FileUtilityUtil.convertInputStreamToFileIFNotExist(LoadModel.class.getClassLoader().getResourceAsStream(PropertiesReader.get("onnx_model_path")),onnxPath);
        env = OrtEnvironment.getEnvironment();
        session = env.createSession(onnxPath, new OrtSession.SessionOptions());
    }

    /**
     * close onnx model
     *//*
    public static void closeOnnxModel() {
        if (Optional.of(session).isPresent()) {
            try {
                session.close();
            } catch (OrtException e) {
                e.printStackTrace();
            }
        }
        if(Optional.of(env).isPresent()) {
            env.close();
        }
    }

}*/