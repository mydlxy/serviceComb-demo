package com.ca.mfd.prc.common.utils.yidiantong;

/**
 * 字符串工具类。
 *
 */
public abstract class StringUtils {

	private StringUtils() {}

	/**
	 * 检查指定的字符串是否为空。
	 * <ul>
	 * <li>SysUtils.isEmpty(null) = true</li>
	 * <li>SysUtils.isEmpty("") = true</li>
	 * <li>SysUtils.isEmpty("   ") = true</li>
	 * <li>SysUtils.isEmpty("abc") = false</li>
	 * </ul>
	 *
	 * @param value 待检查的字符串
	 * @return true/false
	 */
	public static boolean isEmpty(String value) {
		int strLen;
		if (value == null || (strLen = value.length()) == 0) {
			return true;
		}
		for (int i = 0; i < strLen; i++) {
			if ((Character.isWhitespace(value.charAt(i)) == false)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 检查指定的字符串列表是否不为空。
	 */
	public static boolean areNotEmpty(String... values) {
		boolean result = true;
		if (values == null || values.length == 0) {
			result = false;
		} else {
			for (String value : values) {
				result &= !isEmpty(value);
			}
		}
		return result;
	}

	public static String leftPad(String str, int size, char padChar) {
		if (str == null) {
			return null;
		} else {
			int pads = size - str.length();
			if (pads <= 0) {
				return str;
			} else {
				return pads > 8192 ? leftPad(str, size, String.valueOf(padChar)) : padding(pads, padChar).concat(str);
			}
		}
	}

	public static String leftPad(String str, int size, String padStr) {
		if (str == null) {
			return null;
		} else {
			if (isEmpty(padStr)) {
				padStr = " ";
			}

			int padLen = padStr.length();
			int strLen = str.length();
			int pads = size - strLen;
			if (pads <= 0) {
				return str;
			} else if (padLen == 1 && pads <= 8192) {
				return leftPad(str, size, padStr.charAt(0));
			} else if (pads == padLen) {
				return padStr.concat(str);
			} else if (pads < padLen) {
				return padStr.substring(0, pads).concat(str);
			} else {
				char[] padding = new char[pads];
				char[] padChars = padStr.toCharArray();

				for(int i = 0; i < pads; ++i) {
					padding[i] = padChars[i % padLen];
				}

				return (new String(padding)).concat(str);
			}
		}
	}

	private static String padding(int repeat, char padChar) throws IndexOutOfBoundsException {
		if (repeat < 0) {
			throw new IndexOutOfBoundsException("Cannot pad a negative amount: " + repeat);
		} else {
			char[] buf = new char[repeat];

			for(int i = 0; i < buf.length; ++i) {
				buf[i] = padChar;
			}

			return new String(buf);
		}
	}
}
