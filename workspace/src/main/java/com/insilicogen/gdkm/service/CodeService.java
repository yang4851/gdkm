package com.insilicogen.gdkm.service;

import java.util.List;
import java.util.Map;

import com.insilicogen.gdkm.model.Code;

public interface CodeService {
	
	public static final Object Lock = new Object();

	public int createCode(Code code) throws Exception;
	
	public Code getCode(String code) throws Exception;
	
	public List<Code> getCodeList(Map<String, Object> params, boolean isBasicForm) throws Exception;
	
	public int getCodeCount(Map<String, Object> params) throws Exception;
	
	public int changeCode(Code code) throws Exception;
	
	public int deleteCode(String codes) throws Exception;

	
}
