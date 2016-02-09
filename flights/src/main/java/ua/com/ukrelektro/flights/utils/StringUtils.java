package ua.com.ukrelektro.flights.utils;

public class StringUtils {
	public static boolean isNullOrEmpty(String str) {

		if (str != null)
			return str.length() == 0;
		else
			return true;
	}
}
