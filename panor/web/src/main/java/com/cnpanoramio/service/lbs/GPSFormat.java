package com.cnpanoramio.service.lbs;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GPSFormat {

	public static Double convert(String gps) {
		Double l = null;
		
		Pattern pattern = Pattern.compile("[0-9]+\\.[0-9]*");
	    // in case you would like to ignore case sensitivity,
	    // you could use this statement:
	    // Pattern pattern = Pattern.compile("\\s+", Pattern.CASE_INSENSITIVE);
	    Matcher matcher = pattern.matcher(gps);
	    String degree = null, minute = null, second = null;
	    int count = 0;	    
	    // check all occurance
	    while (matcher.find()) {
	    	count++;
	    	switch (count) {
			case 1:
				degree = matcher.group();
				break;
			case 2:
				minute = matcher.group();
				break;
			case 3:
				second = matcher.group();
				break;
			default:
				break;
			}
	    }
	    if(null != degree && null != minute && null != second) {
	    	l = Double.parseDouble(degree) + ( Double.parseDouble(minute) + Double.parseDouble(second) / 60 ) / 60;	    	
	    }
		return l;
	}
}
