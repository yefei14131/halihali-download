package com.pers.yefei.halihali.utils;

public class BytesUtil {

	private static char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6',
		'7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	public static byte[] short2Bytes(short sNum) {
		byte[] bytesRet = new byte[2];
		bytesRet[0] = (byte) ((sNum >> 8) & 0xFF);
		bytesRet[1] = (byte) (sNum & 0xFF);
		return bytesRet;
	}
	
	public static byte[] long2Bytes(long l) {
		byte[] bytesRet = new byte[8];
		int offset=0;
		bytesRet[offset++] = (byte)((l >> 56) & 0xff);
		bytesRet[offset++] = (byte)((l >> 48) & 0xff);
		bytesRet[offset++] = (byte)((l >> 40) & 0xff);
		bytesRet[offset++] = (byte)((l >> 32) & 0xff);
		bytesRet[offset++] = (byte)((l >> 24) & 0xff);
		bytesRet[offset++] = (byte)((l >> 16) & 0xff);
		bytesRet[offset++] = (byte)((l >> 8) & 0xff);
		bytesRet[offset] = (byte)(l & 0xff);
		return bytesRet;
	}
	
	public static byte[] int2Bytes(int nNum) {
		byte[] bytesRet = new byte[4];
		bytesRet[0] = (byte) ((nNum >> 24) & 0xFF);
		bytesRet[1] = (byte) ((nNum >> 16) & 0xFF);
		bytesRet[2] = (byte) ((nNum >> 8) & 0xFF);
		bytesRet[3] = (byte) (nNum & 0xFF);
		return bytesRet;
	}
	

	public static short bytes2Short(byte[] bytesShort) {
		return bytes2Short(bytesShort, 0);
	}

	public static short bytes2Short(byte[] bytesShort, int nStartPos) {
		short sRet = 0;
		sRet += (bytesShort[nStartPos++] & 0xFF) << 8;
		sRet += bytesShort[nStartPos] & 0xFF;
		return sRet;
	}

	

	public static int bytes2Int(byte[] bNum) {
		return bytes2Int(bNum, 0);
	}

	public static int bytes2Int(byte[] bNum, int nStartPos) {
		int retInt = 0;
		retInt = ((bNum[nStartPos++] & 0xFF) << 24);
		retInt += (bNum[nStartPos++] & 0xFF) << 16;
		retInt += (bNum[nStartPos++] & 0xFF) << 8;
		retInt += bNum[nStartPos] & 0xFF;
		return retInt;
	}

	
	public static long bytes2Long(byte[] bNum, int nStartPos) {
		return ((((long) bNum[nStartPos + 0] & 0xff) << 56)
				| (((long) bNum[nStartPos + 1] & 0xff) << 48)
				| (((long) bNum[nStartPos + 2] & 0xff) << 40)
				| (((long) bNum[nStartPos + 3] & 0xff) << 32)
				| (((long) bNum[nStartPos + 4] & 0xff) << 24)
				| (((long) bNum[nStartPos + 5] & 0xff) << 16)
				| (((long) bNum[nStartPos + 6] & 0xff) << 8) | (((long) bNum[nStartPos + 7] & 0xff) << 0));
	}

	
	public static String bytes2Hex(byte bytes[]) {
		return buffer2Hex(bytes, 0, bytes.length);
	}

	public static String buffer2Hex(byte bytes[], int m, int n) {
		StringBuilder strB = new StringBuilder(2 * n);
		int k = m + n;
		for (int ind = m; ind < k; ind++) {
			char c0 = hexDigits[(bytes[ind] & 0xf0) >> 4];// 取字节中高 4 位的数字转换
			// 为逻辑右移，将符号位一起右移,此处未发现两种符号有何不同
			char c1 = hexDigits[bytes[ind] & 0xf];// 取字节中低 4 位的数字转换
			strB.append(c0);
			strB.append(c1);
		}
		return strB.toString();
	}

	public static int byteArray2Int(byte[] data, int offset) {
		int r = data[offset++];
		r = (r << 8) | ((int)(data[offset++]) & 0xff);
		r = (r << 8) | ((int)(data[offset++]) & 0xff);
		r = (r << 8) | ((int)(data[offset++]) & 0xff);
		return r;
	}

	public static long byteArray2Long(byte[] data, int offset) {
		long r = data[offset++];
		r = (r << 8) | ((long)(data[offset++]) & 0xff);
		r = (r << 8) | ((long)(data[offset++]) & 0xff);
		r = (r << 8) | ((long)(data[offset++]) & 0xff);
		r = (r << 8) | ((long)(data[offset++]) & 0xff);
		r = (r << 8) | ((long)(data[offset++]) & 0xff);
		r = (r << 8) | ((long)(data[offset++]) & 0xff);
		r = (r << 8) | ((long)(data[offset++]) & 0xff);
		return r;
	}

	public static int int2ByteArray(int i, byte[] data, int offset) {
		data[offset++] = (byte)((i >> 24) & 0xff);
		data[offset++] = (byte)((i >> 16) & 0xff);
		data[offset++] = (byte)((i >> 8) & 0xff);
		data[offset] = (byte)(i & 0xff);
		return 4;
	}

	public static String short2Hex(short keyNum) {
		byte[] buf = short2Bytes(keyNum);
		return bytes2Hex(buf);
	}


	public static short hex2Short(String str) {
		if( str.length() > 4)
			throw new RuntimeException("str length != 4 fro to short");
		int val =0;
		for( int i=0; i< 4 ;i++ ){
			char ch = str.charAt(i);
			if( ch >='0' && ch <='9'){
				val = (val <<4) | ( (ch-'0')&0xf);
			}else if( ch >='a' && ch <='f'  ){
				val = (val <<4) | ( (ch-87)&0xf);
			}else if( ch >='A' && ch <='F'  ){
				val = (val <<4) | ( (ch-55)&0xf);
			}else {
				throw new  NumberFormatException(""+ch);
			}
		}
		return (short)val;
		
	}

	public static int hex2Int(String str) {
		if( str.length() > 8)
			throw new RuntimeException("str length != 4 fro to short");
		int val =0;
		for( int i=0; i< 8 ;i++ ){
			char ch = str.charAt(i);
			if( ch >='0' && ch <='9'){
				val = (val <<4) | ( (ch-'0')&0xf);
			}else if( ch >='a' && ch <='f'  ){
				val = (val <<4) | ( (ch-87)&0xf);
			}else if( ch >='A' && ch <='F'  ){
				val = (val <<4) | ( (ch-55)&0xf);
			}else {
				throw new  NumberFormatException(""+ch);
			}
		}
		return val;
		
	}	
}
