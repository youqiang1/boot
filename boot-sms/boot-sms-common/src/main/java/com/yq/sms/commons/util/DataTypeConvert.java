package com.yq.sms.commons.util;

import com.yq.sms.commons.sms.packet.IHeadBodyMessage;

import java.nio.ByteBuffer;

public class DataTypeConvert {

	public static long byte2long(byte b[]) {
		return (long) b[7] & (long) 255 | ((long) b[6] & (long) 255) << 8 | ((long) b[5] & (long) 255) << 16 | ((long) b[4] & (long) 255) << 24 | ((long) b[3] & (long) 255) << 32
				| ((long) b[2] & (long) 255) << 40 | ((long) b[1] & (long) 255) << 48 | (long) b[0] << 56;
	}

	public static long[] getSMSSEQ(byte[] by) {
		long[] rr = new long[3];
		byte temp, im;
		temp = by[3];
		temp = (byte) (temp & 0xc0);
		temp = (byte) (temp >>> 6);

		im = (byte) (by[2] & 0x3f);
		im = (byte) (im << 2);
		temp = (byte) (im | temp);

		by[2] = (byte) (by[2] >>> 6);
		im = (byte) (by[1] & 0x3f);
		im = (byte) (im << 2);
		by[2] = (byte) (im | by[2]);

		by[1] = (byte) (by[1] >>> 6);
		im = (byte) (by[0] & 0x3f);
		im = (byte) (im << 2);
		by[1] = (byte) (im | by[1]);

		by[0] = (byte) (by[0] >>> 6);

		rr[0] = byte2long(new byte[] { 0, 0, 0, 0, by[0], by[1], by[2], temp });

		temp = (byte) (by[3] & 0x3f);
		rr[1] = byte2long(new byte[] { 0, 0, 0, 0, 0, temp, by[4], by[5] });

		rr[2] = byte2long(new byte[] { 0, 0, 0, 0, 0, 0, by[6], by[7] });
		return rr;
	}

	public static int byte2ToInt(byte[] b, int off) {
		// return (b[off + 1] & 0xFF00) + (b[off + 0] & 0xFF);
		return (short) (b[off + 1] & 0xff | (b[off + 0] & 0xff) << 8);
	}

	public static byte[] int2byte2(int n) {
		byte[] content = new byte[2];
		content[0] = (byte) (n >> 8);
		content[1] = (byte) n;
		return content;
	}

	public static byte[] IntToBytes2(int i) {
		byte mybytes[] = new byte[2];
		mybytes[0] = (byte) (i >> 24);
		mybytes[1] = (byte) (i >> 16);
		return mybytes;
	}

	public static String[] getSMGPSMSSEQ(byte[] msgId) {
		String[] seq = new String[3];
		seq[0] = convertBCD(msgId[0]) + convertBCD(msgId[1]) + convertBCD(msgId[2]);
		seq[1] = convertBCD(msgId[3]) + convertBCD(msgId[4]) + convertBCD(msgId[5]) + convertBCD(msgId[6]);
		seq[2] = convertBCD(msgId[7]) + convertBCD(msgId[8]) + convertBCD(msgId[9]);

		return seq;
	}

	public static String convertBCD(byte b) {
		byte tp = (byte) (b >>> 4);
		int tpi = (int) tp;
		String re = Integer.toString(tpi);
		tp = (byte) (b & 0x0f);
		tpi = (int) tp;
		re = re + "" + Integer.toString(tpi);
		return re;
	}

	public static String convertBCD(byte[] b) {
		String result = "";
		for (int i = 0; i < b.length; i++) {
			result += convertBCD(b[i]);
		}
		return result;
	}

	public static void putLong(byte[] b, int off, long val) {
		b[off + 7] = (byte) (val >>> 0);
		b[off + 6] = (byte) (val >>> 8);
		b[off + 5] = (byte) (val >>> 16);
		b[off + 4] = (byte) (val >>> 24);
		b[off + 3] = (byte) (val >>> 32);
		b[off + 2] = (byte) (val >>> 40);
		b[off + 1] = (byte) (val >>> 48);
		b[off + 0] = (byte) (val >>> 56);
	}

	public static long getLong(byte[] b, int off) {
		return ((b[off + 7] & 0xFFL) << 0) + ((b[off + 6] & 0xFFL) << 8) + ((b[off + 5] & 0xFFL) << 16) + ((b[off + 4] & 0xFFL) << 24) + ((b[off + 3] & 0xFFL) << 32) + ((b[off + 2] & 0xFFL) << 40)
				+ ((b[off + 1] & 0xFFL) << 48) + ((b[off + 0] & 0xFFL) << 56);
	}

	public static int Bytes4ToInt(byte mybytes[]) {
		return (0xff & mybytes[0]) << 24 | (0xff & mybytes[1]) << 16 | (0xff & mybytes[2]) << 8 | 0xff & mybytes[3];
	}

	public static int Bytes3ToInt(byte mybytes[]) {
		return (0xff & mybytes[0]) << 16 | (0xff & mybytes[1]) << 8 | (0xff & mybytes[2]) << 0;
	}

	public static String bcdToString(byte[] bcd) {
		StringBuilder s = new StringBuilder(bcd.length * 2);
		char c = ' ';
		for (byte b : bcd) {
			switch ((b >> 4) & 0xF) {
			case 0:
				c = '0';
				break;
			case 1:
				c = '1';
				break;
			case 2:
				c = '2';
				break;
			case 3:
				c = '3';
				break;
			case 4:
				c = '4';
				break;
			case 5:
				c = '5';
				break;
			case 6:
				c = '6';
				break;
			case 7:
				c = '7';
				break;
			case 8:
				c = '8';
				break;
			case 9:
				c = '9';
				break;
			default:
				throw new IllegalArgumentException("Bad Decimal: " + ((b >> 4) & 0xF));
			}
			s.append(c);
			switch (b & 0xF) {
			case 0:
				c = '0';
				break;
			case 1:
				c = '1';
				break;
			case 2:
				c = '2';
				break;
			case 3:
				c = '3';
				break;
			case 4:
				c = '4';
				break;
			case 5:
				c = '5';
				break;
			case 6:
				c = '6';
				break;
			case 7:
				c = '7';
				break;
			case 8:
				c = '8';
				break;
			case 9:
				c = '9';
				break;
			default:
				throw new IllegalArgumentException("Bad Decimal: " + (b & 0xF));
			}
			s.append(c);
		}
		return s.toString();
	}

	public static byte[] LongToBytes4(long i) {
		byte mybytes[] = new byte[4];
		mybytes[3] = (byte) (int) ((long) 255 & i);
		mybytes[2] = (byte) (int) (((long) 65280 & i) >> 8);
		mybytes[1] = (byte) (int) (((long) 0xff0000 & i) >> 16);
		mybytes[0] = (byte) (int) (((long) 0xff000000 & i) >> 24);
		return mybytes;
	}

	public static byte[] IntToBytes4(int i) {
		byte mybytes[] = new byte[4];
		mybytes[3] = (byte) (0xff & i);
		mybytes[2] = (byte) ((0xff00 & i) >> 8);
		mybytes[1] = (byte) ((0xff0000 & i) >> 16);
		mybytes[0] = (byte) ((0xff000000 & i) >> 24);
		return mybytes;
	}

	public static int Bytes4ToInt(byte mybytes[], int startIndex) {
		return (0xff & mybytes[0 + startIndex]) << 24 | (0xff & mybytes[1 + startIndex]) << 16 | (0xff & mybytes[2 + startIndex]) << 8 | 0xff & mybytes[3 + startIndex];
	}

	public static long Bytes4ToLong(byte mybytes[], int startIndex) {
		return ((long) 255 & (long) mybytes[0 + startIndex]) << 24 | ((long) 255 & (long) mybytes[1 + startIndex]) << 16 | ((long) 255 & (long) mybytes[2 + startIndex]) << 8 | (long) 255
				& (long) mybytes[3 + startIndex];
	}

	public static void BytesCopy(byte source[], byte dest[], int sourcebegin, int sourceend, int destbegin) {
		int j = 0;
		for (int i = sourcebegin; i <= sourceend; i++) {
			dest[destbegin + j] = source[i];
			j++;
		}
	}

	public static byte[] GetSendContent(IHeadBodyMessage HeadMessage, IHeadBodyMessage BodyMessage) {
		byte[] SendContent = new byte[HeadMessage.bitContent.length + BodyMessage.bitContent.length];
		int indexOf = 0;
		System.arraycopy(HeadMessage.bitContent, 0, SendContent, indexOf, HeadMessage.bitContent.length);
		indexOf += HeadMessage.bitContent.length;
		System.arraycopy(BodyMessage.bitContent, 0, SendContent, indexOf, BodyMessage.bitContent.length);
		return SendContent;
	}

	public static void GetSendContent(byte[] SendContent, int indexOf, IHeadBodyMessage HeadMessage, IHeadBodyMessage BodyMessage) {
		System.arraycopy(HeadMessage.bitContent, 0, SendContent, indexOf, HeadMessage.bitContent.length);
		indexOf += HeadMessage.bitContent.length;
		System.arraycopy(BodyMessage.bitContent, 0, SendContent, indexOf, BodyMessage.bitContent.length);
	}

	public static void bytesCopy(byte[] dest, byte[] src) {
		int len = 0;
		if (dest.length > src.length)
			len = src.length;
		else
			len = dest.length;
		for (int i = 0; i < len; i++) {
			dest[i] = src[i];
		}
	}

	/**
	 * 将int转为低字节在前，高字节在后的byte数组
	 * 
	 * @param n
	 *            int
	 * @return byte[]
	 */
	public static byte[] toLH(int n) {
		byte[] b = new byte[4];
		b[0] = (byte) (n & 0xff);
		b[1] = (byte) (n >> 8 & 0xff);
		b[2] = (byte) (n >> 16 & 0xff);
		b[3] = (byte) (n >> 24 & 0xff);
		return b;
	}

	/**
	 * 将int转为高字节在前，低字节在后的byte数组
	 * 
	 * @param n
	 *            int
	 * @return byte[]
	 */
	public static byte[] toHH(int n) {
		byte[] b = new byte[4];
		b[3] = (byte) (n & 0xff);
		b[2] = (byte) (n >> 8 & 0xff);
		b[1] = (byte) (n >> 16 & 0xff);
		b[0] = (byte) (n >> 24 & 0xff);
		return b;
	}

	public static byte[] HexStringToByteArray(String s) {
		byte[] b = new byte[s.length() / 2];
		for (int i = 0; i < b.length; i++) {
			int index = i * 2;
			int v = Integer.parseInt(s.substring(index, index + 2), 16);
			b[i] = (byte) v;
		}
		return b;
	}

	public static String bytesToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}

	public static String bytesToHexString(byte[] src, int indexOf, int length) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = indexOf; i < indexOf + length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}

	public static String bytesToHexString(byte[] src, int length) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}



	public static byte[] long2byte(long n)
	{
		byte b[] = new byte[8];
		b[0] = (byte)(int)(n >> 56);
		b[1] = (byte)(int)(n >> 48);
		b[2] = (byte)(int)(n >> 40);
		b[3] = (byte)(int)(n >> 32);
		b[4] = (byte)(int)(n >> 24);
		b[5] = (byte)(int)(n >> 16);
		b[6] = (byte)(int)(n >> 8);
		b[7] = (byte)(int)n;
		return b;
	}

	public static void SetSleep(ByteBuffer _ByteBuffer, byte[] TempByte) {
		_ByteBuffer.put(TempByte);
		if (TempByte.length > 1) {
			_ByteBuffer.put((byte) 0);
		}
	}

	public static int searchByte(int start, byte[] Content) {
		int indexOf = 0;
		for (int i = start; i < Content.length; i++) {
			if (Content[i] == 0) {
				indexOf = i;
				break;
			}
		}
		return indexOf;
	}
}
