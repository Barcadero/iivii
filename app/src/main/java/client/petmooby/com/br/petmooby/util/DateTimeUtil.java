package client.petmooby.com.br.petmooby.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class DateTimeUtil {
	public static final String APPLICATION_FORMAT_INPUT = "ddMMyyyy";


	
	public static String formatDateTime(String date, String formatIn, String formatOut) {
		try {
			Date dateToFormat 		= new SimpleDateFormat(formatIn).parse(date);
			String formattedDate 	= new SimpleDateFormat(formatOut).format(dateToFormat);
			return formattedDate;
		} catch (ParseException e) {
			return "";
		}	
	}
	
	public static String formatDateTime(Date date, String formatOut) {
		return new SimpleDateFormat(formatOut).format(date);
	}

	public static Date getDate(int year, int month, int day){
		Calendar calendar = Calendar.getInstance();
		calendar.set(year,month,day);
		return calendar.getTime() ;
	}



	
}
