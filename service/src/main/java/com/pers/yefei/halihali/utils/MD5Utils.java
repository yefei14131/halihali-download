package com.pers.yefei.halihali.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 *  
 * MD5工具类 
 * @author jiankang
 * @date 2013-11-25
 * @version 1.0
 */
public class MD5Utils {
	
	public static String getFileMD5String(File file) throws IOException,
			NoSuchAlgorithmException {
		MessageDigest messagedigest = MessageDigest.getInstance("MD5");
		InputStream fis;
		fis = new FileInputStream(file);
		byte[] buffer = new byte[4096];
		int numRead = 0;
		while ((numRead = fis.read(buffer)) > 0) {
			messagedigest.update(buffer, 0, numRead);
		}
		fis.close();
		return BytesUtil.bytes2Hex(messagedigest.digest());
	}

	
	
	public static String getIdMD5Digest(long id , byte[] random ) {
		try{
			MessageDigest messagedigest = MessageDigest.getInstance("MD5");
			messagedigest.update(random, 0, random.length);
			byte[] data = BytesUtil.long2Bytes(id);
			messagedigest.update(data, 0, data.length);
			return BytesUtil.bytes2Hex(messagedigest.digest());
		}catch(Exception ex){
			throw new RuntimeException(ex);
		}
	}

	public static String getMD5Digest(byte[] random ) {
		try{
			MessageDigest messagedigest = MessageDigest.getInstance("MD5");
			messagedigest.update(random, 0, random.length);
			return BytesUtil.bytes2Hex(messagedigest.digest());
		}catch(Exception ex){
			throw new RuntimeException(ex);
		}
	}	
	
	public static String getMD5Digest(long id ,byte[] token, byte[] key ) {
		try{
			MessageDigest messagedigest = MessageDigest.getInstance("MD5");
			byte[] data = BytesUtil.long2Bytes(id);
			messagedigest.update(data, 0, data.length);
			messagedigest.update(token, 0, token.length);
			messagedigest.update(key, 0, key.length);
			return BytesUtil.bytes2Hex(messagedigest.digest());
		}catch(Exception ex){
			throw new RuntimeException(ex);
		}
	}
	/*** 
     * MD5加密 生成32位md5码
     * @param 待加密字符串
     * @return 返回32位md5码
     */
    public static String md5Encode(String inStr) throws Exception {
        MessageDigest md5 = null;
		md5 = MessageDigest.getInstance("MD5");

        byte[] byteArray = inStr.getBytes("UTF-8");
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }
}
