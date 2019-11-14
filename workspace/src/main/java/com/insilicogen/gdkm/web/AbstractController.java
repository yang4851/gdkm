package com.insilicogen.gdkm.web;

import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;

import com.insilicogen.gdkm.Globals;
import com.insilicogen.gdkm.exception.UserException;
import com.insilicogen.gdkm.model.User;

public abstract class AbstractController {
	
	@Resource(name="messageSource")
	private MessageSource messageSource;

	protected User checkAuthorization(HttpSession session) {
		User loginUser = (User) session.getAttribute(Globals.LOGIN_USER);
		if(loginUser == null) 
			throw new UserException(messageSource.getMessage("ERR001", null, Locale.US), HttpStatus.UNAUTHORIZED);
		return loginUser;
	}
	
	protected static int getCurrentPage(Object page) {
		try {
			return Integer.valueOf(page.toString());
		} catch(Exception e) {
			return 1;
		}
	}
	
	protected static int getCurrentPage(Map<String, Object> params) {
		return getCurrentPage(params.get(Globals.PARAM_PAGE));
	}
	
	protected static int getRowSize(Object rowSize) {
		try {
			return Integer.valueOf(rowSize.toString());
		} catch(Exception e) {
			return 1000;
		}
	}
	
	protected static int getRowSize(Map<String, Object> params) {
		return getRowSize(params.get(Globals.PARAM_ROW_SIZE));
	}
	
	protected static String getSection(Map<String, Object> params) {
		return getSection(params.get(Globals.PARAM_SECTION));
	}
	
	protected static String getSection(Object section) {
		if(section == null)
			return "";
		
		return section.toString();
	}
	
	protected static Boolean getBasicMode(Object basicMode) {
		try {
			return Boolean.valueOf(basicMode.toString());
		} catch(Exception e) {
			return false;
		}
	}
	
	protected static boolean isAppendRequest(Map<String, Object> params) {
		if(params == null || params.isEmpty())
			return false;
		
		String action = (String)params.get(Globals.PARAM_ACTION);
		return StringUtils.equalsIgnoreCase(Globals.PARAM_ACTION_APPEND, action);
	}
	
	protected static boolean isRemoveRequest(Map<String, Object> params) {
		if(params == null || params.isEmpty())
			return false;
		
		String action = (String)params.get(Globals.PARAM_ACTION);
		return StringUtils.equalsIgnoreCase(Globals.PARAM_ACTION_REMOVE, action);
	}
	
	protected static boolean isOpenRequest(Map<String, Object> params) {
		if(params == null || params.isEmpty())
			return false;
		
		String action = (String)params.get(Globals.PARAM_ACTION);
		return StringUtils.equalsIgnoreCase(Globals.PARAM_ACTION_OPEN, action);
	}
}
