package com.cec.examine.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static com.cec.examine.comm.Constants.E_APPID;
import static com.cec.examine.comm.Constants.E_NONCE;
import static com.cec.examine.comm.Constants.E_SIGN;
import static com.cec.examine.comm.Constants.E_TS;

/**
 * 与远程通信的加密字符串
 */
public class SecretUtil {

    //    appid={AppID}&ts={TimeStamp}&nonce={RandomStr}&secret={AppSecretKey}
    public static Map<String, String> getSecretInMap(String appId, String secretKey) {


        String ets = String.valueOf(Instant.now().getEpochSecond());
        String eNonce = LocalDateTime.now().toString();

        Map<String, String> map = new HashMap<>(16);
        map.put(E_TS, ets);
        map.put(E_NONCE, eNonce);
        map.put(E_APPID, appId);

        String signStr = new StringBuilder()
                .append("appid=").append(appId)
                .append("&")
                .append("ts=").append(ets)
                .append("&")
                .append("nonce=").append(eNonce)
                .append("&")
                .append("secret=").append(secretKey)
                .toString();

        MessageDigest instance = null;
        try {
            instance = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        byte[] hash = new byte[0];
        try {
            hash = signStr.getBytes("UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        byte[] digest = instance.digest(hash);

        String sign = bytesToHex(digest);

        map.put(E_SIGN, sign);

        return map;
    }


    private static String bytesToHex(byte[] hash) {

        StringBuilder hexString = new StringBuilder(2 * hash.length);

        for (int i = 0; i < hash.length; i++) {

            String hex = Integer.toHexString(0xff & hash[i]);

            if (hex.length() == 1) {

                hexString.append('0');

            }

            hexString.append(hex);

        }
        return hexString.toString();
    }

}
