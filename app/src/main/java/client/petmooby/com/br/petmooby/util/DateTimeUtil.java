package client.petmooby.com.br.petmooby.util;

import android.support.annotation.IdRes;
import android.support.annotation.StringRes;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import client.petmooby.com.br.petmooby.R;
import client.petmooby.com.br.petmooby.application.Application;


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

	public static String formatDateTime(Date date, @StringRes int resId) {
		String formatOut = Application.Companion.getInstance().getString(resId);
		return new SimpleDateFormat(formatOut).format(date);
	}

	public static String formatDateTime(Date date) {
		if(date == null)return "";
		String formatOut = Application.Companion.getInstance().getString(R.string.formatDate);
		return new SimpleDateFormat(formatOut).format(date);
	}

	public static Date getDate(int year, int month, int day){
		Calendar calendar = Calendar.getInstance();
		calendar.set(year,month,day);
		return calendar.getTime() ;
	}



	
}
