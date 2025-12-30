package com.cec.examine.util.key;

  
import javax.crypto.KeyAgreement;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
 
public class ECDSAUtil {
	
    private static byte[] decodeHex(String hexStr) {  
        if (hexStr.length() < 1)  
                return null;  
        byte[] result = new byte[hexStr.length()/2];  
        for (int i = 0;i< hexStr.length()/2; i++) {  
                int high = Integer.parseInt(hexStr.substring(i*2, i*2+1), 16);  
                int low = Integer.parseInt(hexStr.substring(i*2+1, i*2+2), 16);  
                result[i] = (byte) (high * 16 + low);  
        }  
        return result;  
}
    private static String encodeHexString(byte buf[]) {  
        StringBuffer sb = new StringBuffer();  
        for (int i = 0; i < buf.length; i++) {  
                String hex = Integer.toHexString(buf[i] & 0xFF);  
                if (hex.length() == 1) {  
                        hex = '0' + hex;  
                }  
                sb.append(hex.toUpperCase());
        }  
        return sb.toString();
}
	
    public static void main(String[] args) throws Exception {
//        加签验签
    	String data = "ecdsa security";
        KeyPair keyPair = getKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();
        String sign = signECDSA(privateKey, data);
        verifyECDSA(publicKey,sign,data);
 
//        生成公钥私钥1
        KeyPair keyPair1 = getKeyPair();
        PublicKey publicKey1 = keyPair1.getPublic();
        PrivateKey privateKey1 = keyPair1.getPrivate();
        System.out.println(encodeHexString(publicKey1.getEncoded()));
        System.out.println(encodeHexString(privateKey1.getEncoded()));
 
//        生成公钥私钥2
        KeyPair keyPair2 = getKeyPair();
        PublicKey publicKey2 = keyPair2.getPublic();
        PrivateKey privateKey2 = keyPair2.getPrivate();
        System.out.println(encodeHexString(publicKey2.getEncoded()));
        System.out.println(encodeHexString(privateKey2.getEncoded()));
 

        String sharedKey1 = genSharedKey(publicKey1, privateKey2);
        String sharedKey2 = genSharedKey(publicKey2, privateKey1);
        System.out.println(sharedKey1);
        System.out.println(sharedKey2);
 
    }
 
    //加签
    public static String signECDSA(PrivateKey privateKey, String message) {
        String result = "";
        try {
            //执行签名
            Signature signature = Signature.getInstance("SHA1withECDSA");
            signature.initSign(privateKey);
            signature.update(message.getBytes());
            byte[] sign = signature.sign();
            return encodeHexString(sign);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
 
    //验签
    public static boolean verifyECDSA(PublicKey publicKey, String signed, String message) {
        try {
            //验证签名
            Signature signature = Signature.getInstance("SHA1withECDSA");
            signature.initVerify(publicKey);
            signature.update(message.getBytes());
 
            byte[] hex = decodeHex(signed);
            boolean bool = signature.verify(hex);
            return bool;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
 
    /**
     * 从string转private key
     */
    public static PrivateKey getPrivateKey(String key) throws Exception {
        byte[] bytes = decodeHex(key);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(bytes);
        KeyFactory keyFactory = KeyFactory.getInstance("EC");
        return keyFactory.generatePrivate(keySpec);
    }
 
    /**
     * 从string转public key
     */
    public static PublicKey getPublicKey(String key) throws Exception {
        byte[] bytes = decodeHex(key);
 
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(bytes);
        KeyFactory keyFactory = KeyFactory.getInstance("EC");
        return keyFactory.generatePublic(keySpec);
    }
 
 
    /**
     * 生成 share key
     *
     * @param publicStr  公钥字符串
     * @param privateStr 私钥字符串
     * @return
     */
    public static String genSharedKey(String publicStr, String privateStr) {
        try {
            return genSharedKey(getPublicKey(publicStr), getPrivateKey(privateStr));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
 
    /**
     * 生成 share key
     *
     * @param publicKey  公钥
     * @param privateKey 私钥
     * @return
     */
    public static String genSharedKey(PublicKey publicKey, PrivateKey privateKey) {
        String sharedKey = "";
        try {
            KeyAgreement ka = KeyAgreement.getInstance("ECDH");
            ka.init(privateKey);
            ka.doPhase(publicKey, true);
            sharedKey = encodeHexString(ka.generateSecret());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sharedKey;
    }
 
    //生成KeyPair
    public static KeyPair getKeyPair() throws Exception {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("EC");
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        keyGen.initialize(256, random);
        return keyGen.generateKeyPair();
    }
}
 
 
