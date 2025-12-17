package com.cec.examine.util.encryption;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import com.cec.examine.util.ByteUtil;
import com.cec.examine.util.CompressUtil;
import com.cec.examine.util.key.Key;

/** 
 * AES加密解密工具类
 * 加密: 只能用当前最新版本密钥进行加密
 * 解密: 会根据数据对应的密钥版本进行解密
 *@author qiuwenyi
 */  
public class EncryptionUtil {

	private static Key key = new Key();
	private static int BYTES_THRESHHOLD = 4194304;//<压缩阈值 4MB
	//private static int BYTES_THRESHHOLD = 10;//<压缩阈值

	/**
	 * 加密字节数组
	 * @param data
	 * @param uniqueId
	 * @return
	 */
    public static byte[] encrypt(byte [] data, String uniqueId) {
    	short version = key.getKeyVersion();
    	return encrypt(data, uniqueId,version, false);
    }
	/**
	 * 加密字节数组
	 * @param data
	 * @param uniqueId
	 * @param version
	 * @return
	 */
    public static byte[] encrypt(byte [] data, String uniqueId, short version) {
    	return encrypt(data, uniqueId,version, false);
    }
    /**
     * 加密字节数组归档方法
     * @param data
     * @param uniqueId
     * @return
     */
    public static byte[] encryptArchive(byte [] data, String uniqueId) {
    	short version = key.getKeyVersion();
    	return encrypt(data, uniqueId, version, true);
    }
    /**
     * 加密字节数组归档方法
     * @param data
     * @param uniqueId
     * @return
     */
    public static byte[] encryptArchive(byte [] data, String uniqueId, short version) {
    	return encrypt(data, uniqueId, version, true);
    }
    /**
     * 根据指定版本加密字节数组
     * @param data
     * @param uniqueId
     * @param version
     * @return
     */
    private static byte[] encrypt(byte [] data, String uniqueId, short version, boolean isAchive) {
    	//压缩数据
    	data = compressData(data, BYTES_THRESHHOLD);
    	//加密压缩后的数据
    	String subKey = isAchive ? key.getSubKeyArchive(uniqueId, version) : key.getSubKey(uniqueId, version);
    	byte [] result = AESUtil.encrypt(data, subKey);
    	if(version >= 0) {
    		byte [] head = key.getKeyVersionMagicBytes(version);
    		byte [] finalResult = new byte[head.length + result.length];
    		//先拷贝头
    		System.arraycopy(head, 0, finalResult, 0, head.length);
    		//在拷贝数据
    		System.arraycopy(result, 0, finalResult, head.length, result.length);
    		return finalResult;
    	} else {
    		return result;
    	}
    }
    
    /**
     * 压缩转化
     * @param data
     * @param bytesThreshhold >= 这个阈值就开启压缩
     * @return
     */
    private static byte[] compressData(byte [] data, int bytesThreshhold) {
    	byte [] r = data;
    	if(data.length >= bytesThreshhold) {
    		//压缩
    		r = CompressUtil.compress(data);
    	}
    	return r;
    }
    
    /**
     * 解压转化
     * @param data
     * @param data >= 这个阈值就开启压缩
     * @return
     */
    private static byte[] decompressData(byte [] data) {
    	byte [] r = data;
    	if(CompressUtil.isGZipped(data)) {
    		//说明这段数据是压缩的
    		r = CompressUtil.decompress(data);
    	}
    	return r;
    }
    
    /**
     * 解密
     * 
     * @param data
     *            AES加密过过的内容
     * @param uniqueId
     *            加密时的密码
     * @return 明文
     */
    public static byte[] decrypt(byte[] data, String uniqueId) {
    	short version = key.getDataKeyVersion(data);
    	return decrypt(data, uniqueId, version, false);
    }
    /**
     * 解密字节数组归档方法
     * 
     * @param data
     *            AES加密过过的内容
     * @param uniqueId
     *            加密时的密码
     * @return 明文
     */
    public static byte[] decryptArchive(byte[] data, String uniqueId) {
    	short version = key.getDataKeyVersion(data);
    	return decrypt(data, uniqueId, version, true);
    }
    /**
     * 根据版本解密字节数组
     * @param data
     * @param uniqueId
     * @param version
     * @return
     */
    private static byte[] decrypt(byte[] data, String uniqueId, short version, boolean isAchive) {
    	byte [] r = null;
    	String subKey = isAchive ? key.getSubKeyArchive(uniqueId, version) : key.getSubKey(uniqueId, version);
    	//解密前去掉头
    	if(version >= 0) {
        	byte [] rawData = new byte[data.length - Key.HEAD_LENGTH];
        	System.arraycopy(data, Key.HEAD_LENGTH, rawData, 0, data.length - Key.HEAD_LENGTH);
        	r = AESUtil.decrypt(rawData, subKey);
    	} else {
    		r = AESUtil.decrypt(data, subKey);
    	}
    	return decompressData(r);
    }
    


    /**
     * 加密字符串
     * @param data
     * @param uniqueId
     * @return
     */
    public static String encryptStr(String data, String uniqueId) {
    	short version = key.getKeyVersion();
    	return encryptStr(data, uniqueId, version, false);
    }
    /**
     * 加密字符串
     * @param data
     * @param uniqueId
     * @return
     */
    public static String encryptStr(String data, String uniqueId, short version) {
    	return encryptStr(data, uniqueId, version, false);
    }
    /**
     * 使用最新密钥加密归档方法
     * @param data
     * @param uniqueId
     * @return
     */
    public static String encryptStrArchive(String data, String uniqueId) {   
    	short version = key.getKeyVersion();
    	return encryptStr(data, uniqueId, version, true);
    }
    /**
     * 指定版本使用最新密钥加密归档方法
     * @param data
     * @param uniqueId
     * @return
     */
    public static String encryptStrArchive(String data, String uniqueId, short version) {   
    	return encryptStr(data, uniqueId, version, true);
    }
    /**
     * 根据指定版本加密字符串
     * 加密流程：string -> byte[] -> compress -> byte[] -> encrypt -> byte [] -> hex string
     * @param data
     * @param uniqueId
     * @param version
     * @return
     */
    private static String encryptStr(String data, String uniqueId, short version, boolean isAchive) {
    	//string ->bytes
    	byte [] byteData = data.getBytes(StandardCharsets.UTF_8);
    	//encrypt
    	byte [] encryptData = encrypt(byteData, uniqueId, version, isAchive);
    	//byte [] -> hex string
    	String encryptStr = ByteUtil.bytesToHexString(encryptData);
    	//返回版本头 + 加密数据
    	//String head = key.getKeyVersionAntiHex(version);
    	//return head + encryptStr;
    	return encryptStr;
    }
    /**
     * 解密字符串
     * @param data
     * @param uniqueId
     * @return
     */
    public static String decryptStr(String data, String uniqueId) {
    	short version = key.getDataKeyVersion(data);
    	return decryptStr(data, uniqueId, version, false);
    }
    
    /**
     * 密钥解密字符串归档方法
     * @param data
     * @param uniqueId
     * @return
     */
    public static String decryptStrArchive(String data, String uniqueId) {
    	short version = key.getDataKeyVersion(data);
    	return decryptStr(data, uniqueId, version, true);
    }
    /**
     * 根据指定版本解密字符串
     * 解密流程：hex string -> byte[] -> decrypt -> byte[] -> decompress -> byte[] -> string
     * @param data
     * @param uniqueId
     * @param version
     * @return
     */
    private static String decryptStr(String data, String uniqueId, short version, boolean isAchive) {
    	//解密前去掉头
    	//if(version >= 0) {
        //	String head = key.getKeyVersionAntiHex(version);
        //	data = data.replaceAll(head, "");
    	//}
    	//hex string -> byte []
    	byte [] encyptedData = ByteUtil.hexStringToBytes(data);
    	//decrypt
    	byte [] decryptedData = decrypt(encyptedData, uniqueId, version, isAchive);
    	return new String(decryptedData , StandardCharsets.UTF_8);
    }
   
    /**
     * 解密流
     * @param is
     * @param os
     * @param uniqueId
     * @throws IOException
     */
    public static void decryptStream(InputStream is , OutputStream os, String uniqueId) throws IOException {
    	BufferedInputStream bis = new BufferedInputStream(is);//使用BufferedInputStream包装一下，主要为了控制游标
    	byte [] head = new byte[Key.HEAD_LENGTH];
    	bis.mark(0);
    	bis.read(head);
    	short version = key.getDataKeyVersion(head);//获取密钥版本
    	String subKey = key.getSubKey(uniqueId);//派生子密钥
    	//解密前去掉头
    	if(version >= 0) {
        	AESUtil.decryptStream(bis, os, subKey);
    	} else {
    		bis.reset();//如果是创世版本的密钥，则重置到标记点0
    		AESUtil.decryptStream(bis, os, subKey);
    	}
    }
    
    /**
     * 加密流
     * @param is
     * @param os
     * @param uniqueId
     * @throws IOException
     */
    public static void encryptStream(InputStream is , OutputStream os, String uniqueId) throws IOException {
    	short version = key.getKeyVersion();
    	encryptStream(is,os,uniqueId,version);
    }
    /**
     * 指定版本的加密流
     * @param is
     * @param os
     * @param uniqueId
     * @param version
     * @throws IOException
     */
    public static void encryptStream(InputStream is , OutputStream os, String uniqueId, short version) throws IOException {
    	String subKey = key.getSubKey(uniqueId, version);
    	if(version >= 0) {
    		byte [] head = key.getKeyVersionMagicBytes(version);
    		os.write(head, 0, head.length);
    	}
    	AESUtil.encryptStream(is, os, subKey);
    }
    
    /**
     * 解密流归档方法
     * @param is
     * @param os
     * @param uniqueId
     * @throws IOException
     */
    public static void decryptStreamArchive(InputStream is , OutputStream os, String uniqueId) throws IOException {
    	BufferedInputStream bis = new BufferedInputStream(is);//使用BufferedInputStream包装一下，主要为了控制游标
    	byte [] head = new byte[Key.HEAD_LENGTH];
    	bis.mark(0);
    	bis.read(head);
    	short version = key.getDataKeyVersion(head);//获取密钥版本
    	String subKey = key.getSubKeyArchive(uniqueId);//派生子密钥
    	//解密前去掉头
    	if(version >= 0) {
        	AESUtil.decryptStream(bis, os, subKey);
    	} else {
    		bis.reset();//如果是创世版本的密钥，则重置到标记点0
    		AESUtil.decryptStream(bis, os, subKey);
    	}
    }
    
    /**
     * 加密流归档方法
     * @param is
     * @param os
     * @param uniqueId
     * @throws IOException
     */
    public static void encryptStreamArchive(InputStream is , OutputStream os, String uniqueId) throws IOException {
    	short version = key.getKeyVersion();
    	encryptStreamArchive(is,os,uniqueId,version);
    }
    /**
     * 指定版本的加密流归档方法
     * @param is
     * @param os
     * @param uniqueId
     * @param version
     * @throws IOException
     */
    public static void encryptStreamArchive(InputStream is , OutputStream os, String uniqueId, short version) throws IOException {
    	String subKey = key.getSubKeyArchive(uniqueId, version);
    	if(version >= 0) {
    		byte [] head = key.getKeyVersionMagicBytes(version);
    		os.write(head, 0, head.length);
    	}
    	AESUtil.encryptStream(is, os, subKey);
    }
    
    /**
     * 获取随机字符串
     * @param size
     * @return
     */
    public static String getRandomStr(int size) {
    	Random r = new Random(System.currentTimeMillis());
    	char [] c = new char[size];
    	for(int i = 0 ; i < size; i++) {
    		c[i] = (char) (33 + (Math.abs(r.nextInt()) % 94));
    	}
    	return String.valueOf(c);
    }
    /**
     * 文件加密
     * @param srcFilePath
     * @param encryptedFilePath
     * @param uniqueId
     */
    public static void encryptFile(String srcFilePath, String encryptedFilePath, String uniqueId) {
    	short version = key.getKeyVersion();
    	encryptFile(srcFilePath, encryptedFilePath, uniqueId, version, false);
    }
    /**
     * 归档文件加密
     * @param srcFilePath
     * @param encryptedFilePath
     * @param uniqueId
     */
    public static void encryptFileArchive(String srcFilePath, String encryptedFilePath, String uniqueId) {
    	short version = key.getKeyVersion();
    	encryptFile(srcFilePath, encryptedFilePath, uniqueId, version, true);
    }
    
    /**
     * 指定密钥版本文件加密
     * @param srcFilePath
     * @param encryptedFilePath
     * @param uniqueId
     */
    public static void encryptFile(String srcFilePath, String encryptedFilePath, String uniqueId, short version) {
    	encryptFile(srcFilePath, encryptedFilePath, uniqueId, version, false);
    }
    /**
     * 指定密钥版本归档文件加密
     * @param srcFilePath
     * @param encryptedFilePath
     * @param uniqueId
     */
    public static void encryptFileArchive(String srcFilePath, String encryptedFilePath, String uniqueId, short version) {
    	encryptFile(srcFilePath, encryptedFilePath, uniqueId, version, true);
    }
    
    /**
     * 根据指定版本密钥进行文件加密
     * @param srcFilePath
     * @param encryptedFilePath
     * @param uniqueId
     * @param version
     */
    private static void encryptFile(String srcFilePath, String encryptedFilePath, String uniqueId, short version, boolean isArchive) {
		try {
			File srcFile = new File(srcFilePath);
			File encryptedFile = new File(encryptedFilePath);

			if (srcFile.exists()) {

				if (!encryptedFile.exists()) {
					encryptedFile.createNewFile();
				}

				//源文件
				InputStream srcFileIs = new FileInputStream(srcFile);
				//输出文件
				OutputStream encryptedFileOS = new FileOutputStream(encryptedFile);

				//源文件大于BYTES_THRESHHOLD 先压缩文件 再进行加密
				if (srcFile.length() >= BYTES_THRESHHOLD) {
					ByteArrayOutputStream compressTempBytesOs = new ByteArrayOutputStream();
					byte[] compressTempBytes = null;//承接压缩后的内存字节

					//压缩
					try {
						CompressUtil.compress(srcFileIs, compressTempBytesOs);
						compressTempBytes = compressTempBytesOs.toByteArray();
						compressTempBytesOs.flush();
						compressTempBytesOs.close();
						srcFileIs.close();
					} catch (Exception e) {
						e.printStackTrace();
					}

					//加密
					ByteArrayInputStream compressTempBytesIs = new ByteArrayInputStream(compressTempBytes);
					if(isArchive) encryptStreamArchive(compressTempBytesIs, encryptedFileOS, uniqueId, version);
					else encryptStream(compressTempBytesIs, encryptedFileOS, uniqueId, version);
				} else {
					if(isArchive) encryptStreamArchive(srcFileIs, encryptedFileOS, uniqueId, version);
					else encryptStream(srcFileIs, encryptedFileOS, uniqueId, version);
				}

			} else {
				System.err.println("文件不存在");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

    /**
     * 文件解密
     * @param encryptedFilePath
     * @param decodedFilePath
     * @param uniqueId
     */
    public static void decryptFile(String encryptedFilePath, String decodedFilePath, String uniqueId) {
    	decryptFile(encryptedFilePath, decodedFilePath, uniqueId, false) ;
    }
    /**
     * 归档文件解密
     * @param encryptedFilePath
     * @param decodedFilePath
     * @param uniqueId
     */
    public static void decryptFileArchive(String encryptedFilePath, String decodedFilePath, String uniqueId) {
    	decryptFile(encryptedFilePath, decodedFilePath, uniqueId, true) ;
    }
	/**
	 * 文件解密
	 *
	 * @param encryptedFilePath
	 * @param decodedFilePath
	 * @param uniqueId
	 */
	private static void decryptFile(String encryptedFilePath, String decodedFilePath, String uniqueId, boolean isArchive) {
		try {
			File encryptedFile = new File(encryptedFilePath);
			File decodedFile = new File(decodedFilePath);
			if (encryptedFile.exists()) {

				if (!decodedFile.exists()) {
					decodedFile.createNewFile();
				}

				InputStream encryptedFileIs = new FileInputStream(encryptedFile);
				OutputStream decodedFileOs = new FileOutputStream(decodedFile);

				ByteArrayOutputStream decryptTempBytesOs = new ByteArrayOutputStream();
				byte[] decryptTempBytes = null;//承接解密后的内存文件
				//解密
				if(isArchive) decryptStreamArchive(encryptedFileIs, decryptTempBytesOs, uniqueId);
				else decryptStream(encryptedFileIs, decryptTempBytesOs, uniqueId);
				
				decryptTempBytes = decryptTempBytesOs.toByteArray();
				InputStream decompressIs = new ByteArrayInputStream(decryptTempBytes);

				BufferedInputStream bis = new BufferedInputStream(decompressIs);
				bis.mark(0);
				//判断是否是压缩文件
				boolean flag = CompressUtil.isGZipped(bis);

				//解压
				if (flag) {
					bis.reset();
					//解压
					try {
						CompressUtil.decompress(bis, decodedFileOs);
						decodedFileOs.flush();
						decodedFileOs.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					decodedFileOs.write(decryptTempBytes);
				}
			} else {
				System.err.println("文件不存在");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
    public static void main(String[] args) {
        System.out.println("查看数据密钥版本：" + key.getDataKeyVersion("0F500008A47F51AF07D0C4F214761DC7A0F19FA1"));
        System.out.println("查看数据密钥版本：" + key.getDataKeyVersion("0F500008D30C7D55AC5CBC5DA2D1AE41794BB9A5"));
        System.out.println("当前最新密钥版本：" + key.getKeyVersion());
        System.out.println("当前最新密钥版本头：" + key.getKeyVersionAntiHex(key.getKeyVersion()));
        System.out.println("当前所有可用密钥版本：" + key.getKeyVersions());
        String data1 = "3DA779455E685AFE3D447171698EB29E4D5760FDB581ADD19BF4A994068C1DEF0480275DB554AE98FB7F2C654C7EB50FB9938D8A53E5BAC6E8EDB36D52DB315938D5EDFC9B7CD61EA01D4A4B5D2E20A8E45253BA989C5CDE27596BE74E0C52458ED5998E2EEA27D38C149F70E565D48A2FCE5E2C8535A6819629B59CA5AC6D292D297759EAB585BF84C06F249A518B588014386E2A9588612CDF022813E388815106A8A0A960AE948DD556AEE53BF9F59D4321A10067EE013137BBC73154CAE6F2BD244F695E6464AF632BFEFB2C1F402FD86F9A1FFF5729EE6B91392EE7C8F3484A67D61E5129FAF3FDE5739E5E2A26691669E3823020E24948490A495620C726D970EABB734F19EE6122D678EC1DF59C8DDBF71277F9575777ACCCE978AC6EA522638ECA31976A1A6AAB8F043C6203724F81504926A082C38ADA4049BCB69871F4F0EDB1DC9B7FC1E2F0C5F16630CEBC4644768D3528DB44763BF8199D29FF4DE4591C75FEB0DC94FC0E772C23A6AE6BA45BB4EFA9BBD781413CA9952E993BAEF118FF1919C0883A9A20FAE647EB7DED5ADC8B53A0E932E08199BAE47F4F40E2A7ADB81AB6F8D8572B89A067B1168F26D46B13BDDC39CF05787276712EBC8FAADEB86E2BED732ED8494F2B09AA4FEF66E3F6D4680E500DB58D661CF2E4710C7A55C03FE0F20BFE79FBFFCEB1D702C0D2DCD64DAE7DAC93C8B9B8BC3E198842A1034F6E92239A3B8DFE51E1B418C62BCD4885B85938EA39C29A2E73698147A174EE07ECEBC0AF873AA2F7CBD44947F64CC32DD715C7BDE34BA1408A315E4F3B5DAF6B4641153A73DEC932D4FB23F7E8C7C2F32C9324ACBCC01E43348E5C452F59F5D2709880F3A5E9F897011AFE1234762997868FEEDDC849D9AF4C5638A6EA8F4C96CC3F1028CD8314B78EEB30BABBFCBCB4C2A1F8B3E704601234FDA53D451021042CC9E5B2D21D5D1A5AB1D93F43990B288B5EF81577440010EDAEC462EF484C6D090E55F329796C4798E8E2E02C26C927FAADD8B965A28A8F6A8B876C25A1A52BD3377D0F9D32AA27B26456A1D7C91D0B22B82587984B70A9EA817611787EEAB612C096A197AF686AE14DE0A2B545A02B5BF4BDBDA59A802AF0B80D2D2EA635E6C282C6B9543A3CF68DF60917F607278F9671A8B22B151E1BEBA509EC0AD2A34E14F8D2CAC0C160C182F70EDE7B28071219A3FF35102A0AB84CEB48D4260D35D715586B71A42783FEDD64C33EE705A5BE8BD11FFB409CC02799EBFFEBD5067F2B9A1E4F87C5826EE339987C1AFFEFAFA361377AE619F840250D29D08BDC5D034DB48D7770CECF5FDD29EEADD62823056CCD50541B9B54CCE77655FB3076783D707C218FA13941AB3FA5086A49FC2E3BA34020D14CD87A828FD4E913B312940999F1FAC375F1549386F34A501AA26DC146E3DAEA051FA7734DC8FC0E975F121474D7E323C3DAB29EEFD851613B71B94AB47A073DED0F25B1C1285658868DBE975D8D2F280CF5AC76D7A100ECD7ED89BE2B28C660FC9956348D732899EBC8BA638ABAA5FDA55268B53986D369BFA494F854EADBF587BC860D4EFD74F6B5E0AE5FAD32F69355FDAEC0091A106EC20D526099C9112A9B8F0DA5576439A308D8336D2DB84E6B98AC287FF79F68AB63DBE04B5A8969DD6B7A292A0C1CF8A5E6210C21CC44CCC90E39662C93508AD5121716CE8ABCB7C440BFC0804E5985D3A1D527490D3C7126EC651362E08F05006B3FAB45EC3D885835E1C2D3FA92C246E23E69832271DE678CBFB277AB64858E3B2415499E75DDDE582A876955DCDB70E61EA0E0C9623BB06596A229D0017A6B93C2A94707D78827560B38B59630CBCBC9CFAD90C94CE2AFD224A0994DA37546A46AD54D69C425600F44149608FBAD292C517024E0B3BFCC52CC33D890F80191D1577B37F203430EB129235394723E917B52064D20B7403941582C6BE1B1E79CC6259402254E9A26915BF06AA02E6F0D7D8CA5B32CE57D4FD4F1E65DAA1AB5E665CBFB63DDA854646EEAD3A43B574320A112D7FEBA7552C8E22C875B9F587297C67ADE06751F76156CDF9F7DCEF8C01D89C9C94814FC23490053D3ADB599BDF79232B91687F77ED25A4BE71096203839E90FBAD4422F5DA8FF4E1D5D2216E2B80CC0B90D9C244C0F30A23FB59BA1B55C0A7F377413D0154A2BDFD06806009FC0C122775842EAA534C466815C36D43B88634BE65674F790FAB41324A79215CD7D9611CCD543AEBF2C04AEF89A8CE41237E3441F5478224D81B8EF005F2AC7F75E117E1AE1F915F9D458716ED2168CBCEE31C6B1A356893D067864CF3085C0FCC7B8E2F34427EF0129F5D9DCDB59710A58A1EB9C1414021E8F56C15A0E6693C853A0412768BDB9D38464D6F31106EC4DA0067D8EB4B4C2FCE774D2D6DF8F62A0F484617E0594167A0EE6CE905FF8B226C7C15DF9ABB0F555067F3A179A7F7024B89B28378DB99CD2C4E2EB8734F713AB5C4185E8057EC925E8DC31A4426E1BEC65F781FFEC015387B3592C93D7EA6D701766C104162121B748B44C604EFE94C89325E8397A09C066355D50D34C0A9BC4C436441915B6278865CC82ACA1DD9675CB0A9D39CF0D7569B4951BAB4FCEC9E1AEA0EE0AB62C008B6C8AFDCAE280E429195D08B574353A7DA41A896545799C2CC979F8F4D79491683C8B22351E09BC4D48F09F672E42662FD2439DA417019C735CDCEDD5A9B88DFA963D71649B9E99C43E0FF48A1E3579C6D4764584D13D6552B960301862A7D066A6DB790BA12FB7AB1BD557D07404DB3C4A63CC215D280CD1BFAA398F9B4B6C34D43DFD8C8E3E8BC5015CAD16D0E1949F1603FA64DE667CA1FBBF129E65D46BFBD6E189E96985570E996B721A1A5DA308C1D733B31298887ECD146BBD3AF8E91F5B17E39BD68E30114EBC9D3CCBACCC0EBE1B4E3B5B2E9E90F1F0B5E95D8A8F7BBD76B8DD20BEA3154CEC3F90D502FF9089A57EB6F9915B82177428FA59AA910AA9120E331A085F8B66F5D1FD87107AE9C2D1D3021350D5B7F53EEC472EF9C967DDE0F839D695CFD67FC9324A9FC80DF37E9BC8341F522B1CE5EBEA204AB5353112CDC07B518FA3445699F05FD387340E83C5FC30A30AAECAD0B2B520F88247F2AD00FA4F3F1E32F28251DF694EAB7AA69436F95B31BEB350B887C0A342EB92618EC30C38BCE0A8B95B966D3DA3EBDDDAEAC985BBE55060A1A6D638E1AAD6D79F4BA2C0D2ADC65A75F8A1E9DF1758B10F3D3EDACFE8EADB8FCFED8BBCB41511AA79B14E08D754B849814C70DD1655E240EBED0161ED56E778BE705056475EA66DAE798A61CD699335B88FFDE6141DC7EAAEF8732D6C02DF0DE00715B7949F2F3F402603AE7D8D18479136A8C780936BF6FC973428A699602A676196653E60F532881F42338C1D468C1027F7319994586A7BEAC3E32006D4345E4E85BA45083094283E02A94ADDE142793B17D988D46C72C674FAC22FABCE03E7722AEA3699EFD246513FAB6B7BF836537D02A9C4BC4DC6CED5A68D296FB2748278ECE4B6ED6E1F8DE8D33A532F952CA7EA452557C970748838E4EE825E7823EAF90304E4B57E1A320B4197F20797A4F5C2FA07787FDCA488702E4FA66E0EE4E25A578EC897E932621FC95F8006CE54301E3EF193DA2A85FAE1FC9890150B9C43244988FB12819CA3C3516D517A91AEB458A421C482E0C4CF8C2EE96FE78EF44BA1855719CAEA2531688545A0F27F66C92F5419422840C4FF6AB272B0C5C68FC469B7B30BA2C9F6B1ABA4DBAA8876B7CD33AF25C57B3566E3B2C4B992FBAD5F03AC4E37423BBDDB987F080E568F9D6CF06A0FB7671D87DD8F643A068A043BCE0E1C0D99EB3C779A8F5F4EB8591823AECC56F350857D5CBC43C726C411CA55509D3DC0B46BF802ADF4DF3D89985792BA76EB6F011E524DD51614C701AFDCF91A78B0BD1E662E97D36A57AF9E14ADA11F53DBCDC7BD4F817AA43ACDB2797C176095232D0085EE221B5EE451443F111897062C672C3693514B170A6538CF6E2E5571BC8C973C125A243523F356061900E2B0EDFB330C3826DC541FDC90EA2374ED8A93278B933D531A2634FA3D383D2044D2F3D01A20D3F1B35ED9B82C6738DFB60F5DA67A1EEBB65FC8F1C4E9CA9BE0E6DF5A0473FFF9064D71A077C1C235E1C7DCF03B6AD62F5279C31726F574EDAC0A5A75F5BD74A32118F4C5C342A8418F58A0DA04925012FBC861649BB33CBB48C27BA6592C1E1EDFC79A8055207772A952C86EA563853CC6F01A2772116024DBE054571EA4854BF375319668C970F1270D52A69CB40739044E1669AB894CABEC6F645505DE7ADE70494DAAC66922D278968EE875B4A1E55824DDB208BC91D03C89D934BA568B47322EF8895E7861E72422E17A95BEB3A5D93533A4F4A5C252771A7D428AB1F59639C5413467214060CA94B3E3DAD11E507E69635605855F6F4B59E3829016AD41D482B00F8D56C92FCE4529DF0FE57901B4ECEE896134FC81903DCEBD97A7AD6F6767B72CBA51BA6252D79CAB61D3C966C01ACB8D9FC15FE612A773E69D8DFB701A7300A56B772125C7A745EB20CF236604AAD400C6857FDB54BB5CF1BC27AF4BD4CA8363D7C6E66ED63618A6083AC28EB853606714A0710EF23CE58B23319790DB10B4F5788508AD5030C018E1CCAED53266C32D80035AD471B38A3DF85F1D7C32A947F2944711EE762938CB6BC117F8CE444BC743A025C6C0E24F1648E3A8F18CDFBAFAA1BB24E6BF782FFBA64299E869634445B97A2B8889C7D928FD62736CCB842C1EEDE718134BFBDD0962805EC2EC6FCBBC549CB4E2B47572B15971A60A6DF761DC56F6070F533FF5979354058B98C1CBCD6E54F4A844201C3AB31DF4687E13D2E40318D0866E115B85DF6B91D84CBFFA8C07E90DE6D9ED9A58499C8F38BD2805C257FD21A917C603E1E38E1A4A7D1E6BFB8B1CF48737429302503DD4F0209625D5859503DDBA164AD065A4A3C24F9165BFFE1AEBEE4CA07B4A1EDCC77FB1A8678B99427F6A24A7514E24E599A5A8CFBF3DDAB6AF8C3B9A3480A9D647678964C8A8BA1AEC583D3F5DE0C88AD9CDC5DBE2DFE24B3E418341E24962ABE98F2D14AF6EAA51C5DB8A31322EACB50677FB1D6B44778174A51BAC5A85134CD00C547C4A4E7419197E03E97AA58A173061E8378DFC4BF5D615FDFCC862FD8956D44FDCA2ECB9A8FBE69E5356D33E1C5DCEF3A7B3E1BE258AF13522745CEB3BD6B425A5B2AAB9ADB8340486D0B64211CA529069CD40F661248E78ECB132D22C533E62EED02284E69CA2688BA2A327997637D2690F94D6C6D4106AAECEA01705EF7F274B5E1E579E67687F61785BE283AFDF1330AEADBD10B6EBE185D71461B721F24F5D0A7157DBC78837E51168B79314A9CAB4B9131BD3D9F9A74A7F528B32B922A262BF1BC264D2165630BAD3E36DB522AF00AD4F5A794573F75115C8D0A756D1B108FB10E29FEBD148AA77E1D3973502CD13C20F37EF7662B19DEA8E46914C9A82CEFC400CDBB98D32E734CF4ABC3F775CB63863AEF3C1A7ECE3A132230423183F5BE1709D1101BECF625403349E165A041B289604DD64F86F909AD558CC2E2452B6777C64910927C201DEBDCF7C7EB11EC2133244E456FC299EAE4C960020FD9512C6A175A6A23684D64151273E086930EE15BA23DD40F614851CB9371EAE18A293E34E364052EE5041C3885B43AF9A213C20F14B5A10167AABF7A01CFA2A8F17FCD169B132577A481C4D638552B77B4EF077362F919C99983F77313D44FC065B86DB7BE74F98D8664111186AEE5BD01FD01B5CD107B059178DDDCE84BB04443C935BF864E379AE475AE14893E0058831BB754C44AAB3FD945A221C289BB702C1D100BE573931A7FF6DC5F8F87F82C9B1080C217619F12FF42D9298664815311117236524CE3E4B1D1DD86E0EFD87107AE9C2D1D3021350D5B7F53EEC472EF9C967DDE0F839D695CFD67FC9324A9FC80DF37E9BC8341F522B1CE5EBEA204AB5353112CDC07B518FA3445699F0ADFCDBC8BFB004AEEDABEC3928FA880A20F88247F2AD00FA4F3F1E32F28251DF694EAB7AA69436F95B31BEB350B887C0A342EB92618EC30C38BCE0A8B95B966D31CFE48C3EBDAED1A1B1667E48A9B12CEF4D91DB17C8FAB854FF463E4CC47646F0FF193F94A381BF88F54020A989384FEFE1E9906A3F924C749E0E4E78B9C67AEC25A61C4A04F88F6C29FC7004201180C72D4FA07F1C122D0ED315726148E9FA477B09C522563E3C82859A4ACBD7B2C8DEFD4B44A17F0A182331078A92306BBD53A577E80C9456BD6D81F9702036147675109643016102EB267FEF16C8E3C72975B052A4E72F2EA10F3BDD8C41B3BCF14E06BE1E87AE1EAD2E72AF342343C17F31CFE48C3EBDAED1A1B1667E48A9B12CEF4D91DB17C8FAB854FF463E4CC47646F0FF193F94A381BF88F54020A989384FEFE1E9906A3F924C749E0E4E78B9C67AEC25A61C4A04F88F6C29FC70042011809C1C2B145FA84D07B88D093747ABBA8DC69F8EFD7DC44A79F5545DD3062F2E68554CE10C20CB4B0D91FBA74B5782EBE0BCFDB7692C13F124950679921161DC143DCD8A3CA2F59C16150D28E414118F3C49C60D60F43F1BFAC63A68FC25EA6F6D9631C1FC2CE2B81689A2755A0E1CF3E22DDD51A70149A06C61203E9877BC9D729C7C5CA04EEBEC79DE741939FE4593E091F0C61BCE6DA66CD90BA642251856514430678B62A9082E86FAB915BC035EC5FF55810FFE59CB9AF00A5E060301F1082BD8702C6C4801D6482170C393834390F9D8D18B0F9681E1631493970E86481F784B9F6C45AF691CBFE0890EEBE0F220D2029068F66F60679A9A922877BFD74C27B70B7A8788EE0FBE3062DC1487FFDD0662CED20F716B711561EF2B34F0FF003B8B324DD06AB8ED08939011A5B6B99762765341FB22137F95F01D3BE9961C5D728F5CB054799125816656E4444792DF";
        String uniqueId = "67cd4361-604b-4d7c-879a-ad839b8635ae";
        System.out.println("归档解密：" + EncryptionUtil.decryptStrArchive(data1,uniqueId));
        String data = "hello";
        String data2 = "hello2";
        String dstr = EncryptionUtil.encryptStr(data, uniqueId);
        String dstr2 = EncryptionUtil.encryptStr(data2, uniqueId);
        String dstr3 = EncryptionUtil.encryptStr(data2, uniqueId,(short)-1);//使用非最新版本加密
        System.out.println(dstr + "," + dstr2 + "," + dstr3);
        String estr2 = EncryptionUtil.decryptStr(dstr2, uniqueId);
        String estr3 = EncryptionUtil.decryptStr(dstr3, uniqueId);
        System.out.println("不同版本解密结果：" + estr2 + "," + estr3);
        //字节加解密
        byte [] dataBytes = data.getBytes();
        System.out.println("打印原字节数组细节：" + ByteUtil.printBytes(dataBytes));
        byte [] ENCdataBytes = EncryptionUtil.encrypt(dataBytes, uniqueId);
        System.out.println("当前最新密钥的加密头字节" + ByteUtil.printBytes(key.getKeyVersionMagicBytes(key.getKeyVersion())));
        System.out.println("魔力数字的字节：" + ByteUtil.printBytes(ByteUtil.shortToBytes((short)3920)));
        System.out.println("Key的版本字节：" + ByteUtil.printBytes(ByteUtil.shortToBytes(key.getKeyVersion())));
        System.out.println("加密后的数据：" + ByteUtil.printBytes(ENCdataBytes));
        System.out.println("加密后的数据版本密钥：" + key.getDataKeyVersion(ENCdataBytes));
        byte [] DECdataBytes = EncryptionUtil.decrypt(ENCdataBytes, uniqueId);
        boolean result = ByteUtil.bytesEquals(dataBytes, DECdataBytes, 0, DECdataBytes.length);
        System.out.println("字节加解密结果：" + result);
        System.out.println("字节加解密结果：" + ByteUtil.bytesEquals(dataBytes, EncryptionUtil.encryptArchive(dataBytes, uniqueId)));
        //流加密测试
        try {
//        	String srcFilePath = "/Users/wenyiqiu/Downloads/name.txt";
//        	String encryptedFilePath = "/Users/wenyiqiu/Downloads/name.txt.enc";

        	//流加密
//        	File srcFile = new File(srcFilePath);//原文件等待被加密
//            File encryptedFile = new File(encryptedFilePath);//加密后的文件
//            if(!encryptedFile.exists()){
//            	System.out.println("encrypt file created");
//            	encryptedFile.createNewFile();
//            }
//			InputStream srcFileIs  = new FileInputStream(srcFile);
//	        OutputStream encryptedFileOS = new FileOutputStream(encryptedFile);
//	        EncryptionUtil.encryptStream(srcFileIs, encryptedFileOS, uniqueId);//完成文件加密过程
//
//	        //流解密
//	        File decryptedFile = new File("/Users/wenyiqiu/Downloads/name.txt.dec.txt");
//	        InputStream encryptedFileIs  = new FileInputStream(encryptedFile);
//	        OutputStream decryptedFileOS = new FileOutputStream(decryptedFile);
//            if(!decryptedFile.exists()){
//            	System.out.println("decrypt file created");
//            	decryptedFile.createNewFile();
//            }
//
//            EncryptionUtil.decryptStream(encryptedFileIs, decryptedFileOS, uniqueId);//完成文件解密过程
            
            //压缩测试
            String originData = "HeeeeeeeeeeeeeeeeeeeeeeeeelloooooooooooooooooooooooooooHeeeeeeeeeeeeeeeeeeeeeeeeelloooooooooooooooooooooooooooHeeeeeeeeeeeeeeeeeeeeeeeeelloooooooooooooooooooooooooooHeeeeeeeeeeeeeeeeeeeeeeeeelloooooooooooooooooooooooooooHeeeeeeeeeeeeeeeeeeeeeeeeelloooooooooooooooooooooooooooHeeeeeeeeeeeeeeeeeeeeeeeeelloooooooooooooooooooooooooooHeeeeeeeeeeeeeeeeeeeeeeeeelloooooooooooooooooooooooooooHeeeeeeeeeeeeeeeeeeeeeeeeelloooooooooooooooooooooooooooHeeeeeeeeeeeeeeeeeeeeeeeeelloooooooooooooooooooooooooooHeeeeeeeeeeeeeeeeeeeeeeeeelloooooooooooooooooooooooooooHeeeeeeeeeeeeeeeeeeeeeeeeelloooooooooooooooooooooooooooHeeeeeeeeeeeeeeeeeeeeeeeeelloooooooooooooooooooooooooooHeeeeeeeeeeeeeeeeeeeeeeeeelloooooooooooooooooooooooooooHeeeeeeeeeeeeeeeeeeeeeeeeelloooooooooooooooooooooooooooHeeeeeeeeeeeeeeeeeeeeeeeeelloooooooooooooooooooooooooooHeeeeeeeeeeeeeeeeeeeeeeeeelloooooooooooooooooooooooooooHeeeeeeeeeeeeeeeeeeeeeeeeelloooooooooooooooooooooooooooHeeeeeeeeeeeeeeeeeeeeeeeeelloooooooooooooooooooooooooooHeeeeeeeeeeeeeeeeeeeeeeeeelloooooooooooooooooooooooooooHeeeeeeeeeeeeeeeeeeeeeeeeelloooooooooooooooooooooooooooHeeeeeeeeeeeeeeeeeeeeeeeeelloooooooooooooooooooooooooooHeeeeeeeeeeeeeeeeeeeeeeeeelloooooooooooooooooooooooooooHeeeeeeeeeeeeeeeeeeeeeeeeelloooooooooooooooooooooooooooHeeeeeeeeeeeeeeeeeeeeeeeeellooooooooooooooooooooooooooo";
            byte [] eoriginData = EncryptionUtil.encrypt(originData.getBytes("UTF-8"), uniqueId);
            byte [] doriginData = EncryptionUtil.decrypt(eoriginData, uniqueId);
            System.out.println("原始数据大小/加密数据大小/解密数据大小" + originData.getBytes("UTF-8").length + "/" + eoriginData.length +  "/" + doriginData.length);
            System.out.println("解密还原是否相等：" + ByteUtil.bytesEquals(originData.getBytes("UTF-8"), doriginData));
            
            //文件加解密测试
        	String srcFilePath2 = "/Users/wenyiqiu/Downloads/name.txt.2";
        	String encryptedFilePath2 = "/Users/wenyiqiu/Downloads/name.txt.enc.2";
        	String decodedFilePath2 = "/Users/wenyiqiu/Downloads/name.txt.enc.dec.2";
            EncryptionUtil.encryptFile(srcFilePath2, encryptedFilePath2, uniqueId);
            EncryptionUtil.decryptFile(encryptedFilePath2, decodedFilePath2, uniqueId);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}
