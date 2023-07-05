package com.github.rok.utils;

/*
 * @author Rok, Pedro Lucas N M Machado created on 05/07/2023
 */
public class TimeUtils {
	public static String getTime(long time) {
		final long variacao = time,
				seconds = variacao / 1000 % 60,
				minutes = variacao / 60000 % 60,
				hours = variacao / 3600000 % 24,
				days = variacao / 86400000 % 30;

		String duration = (days == 0 ? "" : days + " dias,") + (hours == 0 ? "" : hours + " horas, ")
				                  + (minutes < 10 ? "0"+minutes+":" : minutes + ":") + (seconds < 10 ? "0" +seconds : seconds);

		duration = replaceLast(duration, ", ", "");

		return replaceLast(duration, ",", " e");

	}
	private static String replaceLast(String text, String regex, String replacement) {
		return text.replaceFirst("(?s)(.*)" + regex, "$1" + replacement);
	}

}