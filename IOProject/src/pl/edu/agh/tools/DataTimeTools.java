package pl.edu.agh.tools;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DataTimeTools {

	public static String parseDateFromCalendar(Calendar calendar) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
		return simpleDateFormat.format(calendar.getTime());
	}
	
	public static String parseTimeFromCalendar(Calendar calendar) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
		return simpleDateFormat.format(calendar.getTime());
	}
	
	public static String parseDateFromDate(Date date) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
		return simpleDateFormat.format(date);
	}
	
	public static String parseTimeFromDate(Date date) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
		return simpleDateFormat.format(date);
	}

}
