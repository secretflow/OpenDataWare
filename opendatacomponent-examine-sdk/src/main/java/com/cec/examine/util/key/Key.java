package com.cec.examine.util.key;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.cec.examine.util.ByteUtil;
import com.cec.examine.util.encryption.AESUtil;
import com.cec.examine.util.encryption.MD5Util;
import com.cec.examine.util.jsonparser.JSONParser;
import com.cec.examine.util.jsonparser.model.JsonArray;
import com.cec.examine.util.jsonparser.model.JsonObject;
import com.cec.examine.util.SecretUtil;

public class Key {

	private String key;//<当前最新密钥
	private short version = -1;//当前最新版本, 32767,-32768
	private static short magicNum = 3920;//<用于加密文件和字节的头
	private static byte [] magicBytes = null;
	public static int HEAD_LENGTH = 4;//<版本头信息长度是4
	private List<Short> versions = new ArrayList<Short>();
	private TreeMap<Short, String> keyMap = new TreeMap<Short, String>();
	public Key() {
		//创世密钥
        String sec = "F2A78100341B01A102232CB979A45DF6631FD7DBD7699102404B7349CADA99EB60C71781E5B24CD92FB740AC596E05E5";
		String initkey = AESUtil.decryptStr(sec, "1");
		keyMap.put((short)-1, initkey);
		//从服务获取所有版本的密钥对
		//String result = Httpj.sendGet(url);
		String result = "{}";//默认密钥列表是空
		int timeout = 3000;

	    	System.err.println("下载密钥，超时设置" + timeout + "ms");

			//String appKey = ProperUtil.getProperty(APP_KEY);
			//String appSecret = ProperUtil.getProperty(APP_SECRET);

			//String remoteServerAlias = ProperUtil.getProperty(REMOTE_SERVER_NAME);

			String appKey = null;
			String appSecret = null;

			String remoteServerAlias = null;

			Map<String, String> secretInMap = SecretUtil.getSecretInMap(appKey, appSecret);

			result = "";


		//String result = "{\"status\": 200,\"result\": [{\"st\": \"e81f6e49943a128f3f710be274f3b546e8b3601bb4317edf7eae184b79845a6c\",\"id\": 8,\"status\": \"SUCCEED\"}],\"message\": \"OK\",\"code\": \"OK\" }";
        try {
    		JSONParser jsonParser = new JSONParser();
			JsonObject jsonObject = (JsonObject) jsonParser.fromJSON(result);
			String code = String.valueOf(jsonObject.get("code"));
			if("OK".equals(code)) {
				JsonArray keysArr = jsonObject.getJsonArray("result");
				for(int i = 0; i < keysArr.size() ; i++) {
					String st = String.valueOf(keysArr.getJsonObject(i).get("st"));
					Short v = Short.parseShort((String.valueOf(keysArr.getJsonObject(i).get("id"))));
					keyMap.put(v, st);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		//keyMap.put(1, "testKey1");
		//keyMap.put(2, "testKey2");
		//获取最新版本
		for(short v :keyMap.keySet()) {
			versions.add(v);
			if(v > version) version = v;
		}
		key = keyMap.get(version);//最新Key
		//魔力数字
		magicBytes = ByteUtil.shortToBytes(magicNum);
	}

	/**
	 * 获取当前最新密钥的版本号
	 * @return
	 */
	public short getKeyVersion() {
		return version;
	}
	/**
	 * 获取指定版本假HEX版本
	 * @param version
	 * @return
	 */
	public String getKeyVersionAntiHex(int version) {
		if(version < 0) return "";
		else return numToAntiHexString(version);
	}
	
	/**
	 * 获取指定版本魔力字节头，前2个是MagicBytes，后2个是version short型数字
	 * @param version
	 * @return
	 */
	public byte [] getKeyVersionMagicBytes(short version) {
		if(version < 0) return new byte[0];
		byte [] magicBytesHead = new byte[HEAD_LENGTH];
		for(int i = 0; i < magicBytes.length; i++) {
			magicBytesHead[i] = magicBytes[i];
		}
		byte [] versionBytes = ByteUtil.shortToBytes(version);
		for(int i = 0; i < versionBytes.length; i++) {
			magicBytesHead[i + magicBytes.length] = versionBytes[i];
		}
		return magicBytesHead;
	}
	

	/**
	 * 获取最新版本派生密钥
	 * @return
	 */
	public String getSubKey(String uniqueId) {
    	String subKey = MD5Util.textToMD5U32(key + uniqueId);
    	return subKey;
	}
	
	/**
	 * 获取最新版本归档用的派生密钥
	 * @param uniqueId
	 * @return
	 */
	public String getSubKeyArchive(String uniqueId) {
    	String subKey = MD5Util.textToMD5L32(key + uniqueId);
    	return subKey;
	}
	
	/**
	 * 根据版本获取派生密钥
	 * @return
	 */
	public String getSubKey(String uniqueId, Short version ) {
		String key = keyMap.get(version);
		if(key == null) return null;
    	String subKey = MD5Util.textToMD5U32(keyMap.get(version) + uniqueId);
    	return subKey;
	}
	
	/**
	 * 根据版本获取归档用的派生密钥
	 * @param uniqueId
	 * @return
	 */
	public String getSubKeyArchive(String uniqueId, Short version) {
		String key = keyMap.get(version);
		if(key == null) return null;
    	String subKey = MD5Util.textToMD5L32(keyMap.get(version) + uniqueId);
    	return subKey;
	}
	
	/**
	 * 获取加密数据的Key版本
	 * @param data
	 * @return
	 */
	public Short getDataKeyVersion(String data) {
		return getDataKeyVersion(ByteUtil.hexStringToBytes(data));
	}
	
	/**
	 * 获取加密数据的版本
	 * @param data
	 * @return
	 */
	public Short getDataKeyVersion(byte [] data) {
		Short version = -1;
		if(data != null && data.length >= HEAD_LENGTH && ByteUtil.bytesEquals(magicBytes, data, 0, 2)) {
			version = ByteUtil.bytesToShort(data, 2);
		}
		return version;
	}
	
    /**
     * 转化为非16进制字符串
     * @param
     * @return
     */
    public String numToAntiHexString(int num) {
    	String s = Long.toHexString(num);
    	StringBuilder sb = new StringBuilder();
    	for(int i = 0; i < s.length() ; i++) {
    		int ascii = s.charAt(i);
    		if(ascii >= 48 && ascii <= 57) sb.append((char)(ascii + 23));
    		else if (ascii >= 97 && ascii <= 102) sb.append((char)(ascii - 16));
    	}
    	return sb.toString();
    }
    
    /**
     * 转化为非16进制字符串
     * @param
     * @return
     */
    public Integer antiHexStringToNum(String antiHexString) {
    	StringBuilder sb = new StringBuilder();
    	antiHexString = antiHexString.trim();
    	for(int i = 0; i < antiHexString.length() ; i++) {
    		int ascii = antiHexString.charAt(i);
    		if(ascii >= 71 && ascii <= 80) sb.append((char)(ascii - 23));
    		else if (ascii >= 81 && ascii <= 86) sb.append((char)(ascii + 16));
    	}
    	return Integer.parseInt(sb.toString(),16);
    }
    
    /**
     * 获取所有可用密钥版本和密钥的散列值
     * @return
     */
    public Map<Short, String> getKeyVersions() {
    	TreeMap<Short, String> r = new TreeMap<Short, String>();
    	for(Short version: keyMap.keySet()) {
    		r.put(version, MD5Util.textToMD5L32(keyMap.get(version)));
    	}
    	return r;
    }
}