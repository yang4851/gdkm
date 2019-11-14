package com.insilicogen.gdkm;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.insilicogen.gdkm.model.Code;
import com.insilicogen.gdkm.model.NgsDataAchive;
import com.insilicogen.gdkm.model.NgsDataRegist;
import com.insilicogen.gdkm.model.Taxonomy;

public class MybatisHelper {

	public static boolean isEmpty(Object obj) {
		if(obj instanceof String) { 
			return StringUtils.isBlank((String)obj);
		} else if(obj instanceof List) {
			return (obj == null) ? true : ((List<?>)obj).isEmpty();
		} else if(obj instanceof Map) {
			return (obj == null) ? true : ((Map<?,?>)obj).isEmpty();
		} else if(obj instanceof Object[]) {
			return (obj == null) ? true : Array.getLength(obj) == 0;
		}
		
		return (obj == null);
	}
	
	public static boolean isNotEmpty(Object... obj) {
		for(int i=0; i < obj.length ; i++) {
			if(!isEmpty(obj[i]))
				return true;
		}
		
		return false;
	}
	
	public static boolean isExactMatch(Object obj) {
		return "exact".equals(obj);
	}
	
	public static boolean isCode(Object obj) {
		if(obj instanceof Code) {
			return StringUtils.isNotBlank(((Code)obj).getCode());
		}
		
		return false;
	}
	
	public static boolean isTaxonomy(Object obj) {
		if(obj instanceof Taxonomy) {
			return ((Taxonomy)obj).getTaxonId() > 0;
		}
		
		return false;
	}

	public static boolean isDataRegist(Object obj) {
		if(obj instanceof NgsDataRegist)
			return ((NgsDataRegist)obj).getId() > 0;
			
		return false;
	}
	
	public static boolean isDataAchive(Object obj) {
		if(obj instanceof NgsDataAchive)
			return ((NgsDataAchive)obj).getId() > 0;
			
		return false;
	}
	
	public static boolean isRawData(String dataType) {
		return Globals.REGIST_DATA_RAWDATA.equals(dataType);
	}
	
	public static boolean isProcessedData(String dataType) {
		return Globals.REGIST_DATA_PROCESSEDDATA.equals(dataType);
	}
	
	public static boolean isSearchField(Object field, String target) {
		if(field == null)
			return false;
		
		if(field instanceof String) {
			if(StringUtils.isBlank(field.toString()))
				return true;
			
			return field.toString().equalsIgnoreCase(target);
		} else if (field instanceof Collection) {
			@SuppressWarnings("unchecked")
			Iterator<String> fields = ((Collection<String>)field).iterator();
			while(fields.hasNext()) {
				if(StringUtils.equalsIgnoreCase(fields.next(), target))
					return true;
			}
		} else if (field instanceof String[]) {
			String[] fields = (String[])field;
			for(int i=0; i < fields.length ; i++) {
				if(StringUtils.equalsIgnoreCase(fields[i], target))
					return true;
			}
		}
		
		return false;
	}
	
	public static boolean hasPaging(Object firstIndex, Object rowSize) {
		try {
			Integer.valueOf(firstIndex.toString());
			Integer.valueOf(rowSize.toString());
			
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public static boolean isZero(Integer number){
		return number.equals(0);
	}
	
	public static boolean isNotZero(Integer number){
		return !number.equals(0);
	}
	
}
