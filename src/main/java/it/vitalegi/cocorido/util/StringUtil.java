package it.vitalegi.cocorido.util;

public class StringUtil {

	public static boolean isNullOrEmpty(String str) {
		return str == null || str.trim().equals("");
	}

	public static String leftPadding(String str, int length) {
		while (str.length() < length) {
			str = " " + str;
		}
		return str;
	}

	public static String rightPadding(String str, int length) {
		while (str.length() < length) {
			str = str + " ";
		}
		return str;
	}
}
