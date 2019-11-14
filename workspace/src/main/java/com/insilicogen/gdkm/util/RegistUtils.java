package com.insilicogen.gdkm.util;

import java.util.Calendar;
import java.util.Date;

import com.insilicogen.gdkm.Globals;
import com.insilicogen.gdkm.model.NgsDataRegist;

public class RegistUtils {

	static final long MILLISECON_365DAY = 365L*24L*60L*60L*1000L;
	
	public static boolean isSubmittedData(NgsDataRegist regist) {
		if(regist == null || regist.getId() < 1)
			return false;
		
		return Globals.STATUS_REGIST_SUCCESS.equals(regist.getRegistStatus());
	}
	
	public static boolean isValidatedData(NgsDataRegist regist) {
		if(regist == null || regist.getId() < 1)
			return false;
		
		return Globals.STATUS_REGIST_VALIDATED.equals(regist.getRegistStatus());
	}
	
	public static Date getDefaultOpenDate() {
		Calendar nextYear = Calendar.getInstance();
		nextYear.setTime(new Date(System.currentTimeMillis() + MILLISECON_365DAY));
		nextYear.set(Calendar.MILLISECOND, 999);
		nextYear.set(Calendar.SECOND, 59);
		nextYear.set(Calendar.MINUTE, 59);
		nextYear.set(Calendar.HOUR_OF_DAY, 23);
		
		return nextYear.getTime();
	}
}
