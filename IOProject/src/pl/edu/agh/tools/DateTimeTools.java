package pl.edu.agh.tools;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class DateTimeTools {

	private static SimpleDateFormat fullDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault());
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
	private static SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
	private static final String DATE_SEPARATOR = ".";
	private static final String TIME_SEPARATOR = ":";
	
	/**
	 * Format: yyyy:MM:dd HH:mm:ss
	 * @param fullDate
	 */
	public static Date convertStringToFullDate(String fullDate) {
		String[] date = fullDate.split(" ")[0].split("\\" + DATE_SEPARATOR);
		Calendar calendar = new GregorianCalendar();
		calendar.set(Calendar.YEAR, Integer.valueOf(date[0]));
		calendar.set(Calendar.MONTH, Integer.valueOf(date[1]) - 1);
		calendar.set(Calendar.DAY_OF_MONTH, Integer.valueOf(date[2]));
		String[] time = fullDate.split(" ")[1].split(TIME_SEPARATOR);
		calendar.set(Calendar.HOUR_OF_DAY, Integer.valueOf(time[0]));
		calendar.set(Calendar.MINUTE, Integer.valueOf(time[1]));
		calendar.set(Calendar.SECOND, Integer.valueOf(time[2]));
		return calendar.getTime();
	}
	
	/**
	 * Format: yyyy:MM:dd + time is 00:00:00
	 * @param date
	 */
	public static Date convertStringToDate(String dateString) {
		String[] date = dateString.split("\\" + DATE_SEPARATOR);
		Calendar calendar = new GregorianCalendar();
		calendar.set(Calendar.YEAR, Integer.valueOf(date[0]));
		calendar.set(Calendar.MONTH, Integer.valueOf(date[1]) - 1);
		calendar.set(Calendar.DAY_OF_MONTH, Integer.valueOf(date[2]));
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		return calendar.getTime();
	}
	
	/**
	 * Format: HH:mm:ss + date is set on today's date
	 * @param timeOnly
	 * @return
	 */
	public static Date convertStringToTime(String timeOnly) {
		String[] date = timeOnly.split(TIME_SEPARATOR);
		Calendar calendar = new GregorianCalendar();
		calendar.set(Calendar.HOUR_OF_DAY, Integer.valueOf(date[0]));
		calendar.set(Calendar.MINUTE, Integer.valueOf(date[1]) - 1);
		calendar.set(Calendar.SECOND, Integer.valueOf(date[2]));
		return calendar.getTime();
	}
	
	public static String convertFullDateToString(Calendar calendar) {
		return fullDateFormat.format(calendar.getTime());
	}
	
	public static String convertFullDateToString(Date date) {
		return fullDateFormat.format(date);
	}
	
	public static String convertDateToString(Calendar calendar) {
		return dateFormat.format(calendar.getTime());
	}
	
	public static String convertDateToString(Date date) {
		return dateFormat.format(date);
	}
	
	public static String convertTimeToString(Calendar calendar) {
		return timeFormat.format(calendar.getTime());
	}
	
	public static String convertTimeToString(Date date) {
		return timeFormat.format(date);
	}

}
