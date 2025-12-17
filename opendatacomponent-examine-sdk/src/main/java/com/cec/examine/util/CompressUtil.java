package com.cec.examine.util;
import com.cec.examine.util.encryption.EncryptionUtil;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class CompressUtil {

	public static void main(String[] args) throws Exception {
		File file = new File("/Users/wenyiqiu/Downloads/outlook.txt");
		String uniqueId = "67cd4361-604b-4d7c-879a-ad839b8635ae";
		FileInputStream fis = new FileInputStream(file);
		byte [] s = new byte [fis.available()];
		fis.read(s, 0, s.length);
		String s1 = new String(s, "UTF-8");
		System.out.println("文件大小:" +  s.length);
		System.out.println("文件大小:" +  s1.length());
		byte [] e = EncryptionUtil.encrypt(s, uniqueId);
		String e1 = EncryptionUtil.encryptStr(s1, uniqueId);
		System.out.println("字节数组-加密后文件大小:" +  e.length);
		System.out.println("字符串-加密后文件大小:" +  e1.length());

		byte [] c = CompressUtil.compress(s);
		System.out.println("字节数组-压缩文件大小:" +  c.length);
		System.out.println("是否是压缩后的数据："  + isGZipped(c));
		System.out.println("是否是压缩后的数据："  + isGZipped(s));
		byte [] ec = EncryptionUtil.encrypt(c, uniqueId);
		System.out.println("[最优方案]字节数组-先压缩后加密文件大小:" +  ec.length);
		byte [] ce = CompressUtil.compress(e);
		System.out.println("[最差方案]字节数组-先加密后压缩文件大小:" +  ce.length);
		System.out.println("是否是压缩后的数据："  + isGZipped(e));
		System.out.println("是否是压缩后的数据："  + isGZipped(ce));
		fis.close();
	}
	
	public static byte [] compress(byte [] data) {
		ByteArrayInputStream bais = new ByteArrayInputStream(data);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] output = null;
		try {
			compress(bais, baos);
			output = baos.toByteArray();
			baos.flush();
			baos.close();
			bais.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return output;
	}
	
	public static byte [] decompress(byte [] data) {
		ByteArrayInputStream bais = new ByteArrayInputStream(data);  
        ByteArrayOutputStream baos = new ByteArrayOutputStream();  
        try {
        decompress(bais, baos);  
        data = baos.toByteArray();
        baos.flush();  
        baos.close();  
        bais.close(); 
        } catch (Exception e) {
        	e.printStackTrace();
        }
    	return data; 
	}
	
	/** 
	 * 数据压缩 
	 *  
	 * @param is 
	 * @param os 
	 * @throws Exception 
	 */  
	public static void compress(InputStream is, OutputStream os)  
	        throws Exception {  
	    GZIPOutputStream gos = new GZIPOutputStream(os);
	    int count;  
	    byte data[] = new byte[1048576];  
	    while ((count = is.read(data, 0, data.length)) != -1) {  
	        gos.write(data, 0, count);  
	    }  
	    gos.finish();
	    gos.flush();  
	    gos.close();  
	}
	/** 
	 * 数据解压缩 
	 *  
	 * @param is 
	 * @param os 
	 * @throws Exception 
	 */  
	public static void decompress(InputStream is, OutputStream os)  
	        throws Exception {  
	    GZIPInputStream gis = new GZIPInputStream(is);  
	    int count;
	    byte data[] = new byte[1048576];
	    while ((count = gis.read(data, 0, data.length)) != -1) {  
	        os.write(data, 0, count);
	    }
	    gis.close();  
	}
	
	/**
	 * 判断是否是GZIP压缩
	 * @param b
	 * @return
	 */
	public static boolean isGZipped(byte [] b) {
		int magic = 0;
		try {
			magic = b[0] & 0xff | ((b[1] << 8) & 0xff00);
		} catch (Throwable e) {
			e.printStackTrace(System.err);
		}
		return magic == GZIPInputStream.GZIP_MAGIC;
	}
	
    public static boolean isGZipped(InputStream is) {

        boolean flag = false;
        byte [] magic = new byte[2];
        BufferedInputStream bis = new BufferedInputStream(is);
        bis.mark(0);
        try {
            bis.read(magic);
            if( magic[0] == (byte) 0x1f && magic[1] == (byte) 0x8b ) {
                flag = true;
            }
            bis.reset();
        } catch (Throwable e) {
            e.printStackTrace(System.err);
        }
        return flag;
    }

}
