package org.opensrp.batch.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.springframework.stereotype.Component;

@Component
public class DateUtils {
	
	public static String getDateAsYYYYMMddHHMMSS(String date) {
		Date formatedDate;
		String convertedDate = "";
		try {
			formatedDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).parse(date);
			
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			convertedDate = simpleDateFormat.format(formatedDate);
			
		}
		catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return convertedDate;
	}
	
}
