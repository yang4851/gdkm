package com.insilicogen.gdkm.service;

import java.util.List;
import java.util.Map;

import com.insilicogen.gdkm.model.CodeGroup;

public interface CodeGroupService {
	
	public static final Object Lock = new Object();
	
	public int createCodeGroup(CodeGroup codeGroup) throws Exception;
	
	public CodeGroup getCodeGroup(String codeGroupId) throws Exception;
	
	public List<CodeGroup> getCodeGroupList(Map<String, Object> params, boolean isBasicForm) throws Exception;
	
	public int getCodeGroupCount(Map<String, Object> params) throws Exception;
	
	public int changeCodeGroup(CodeGroup codeGroup) throws Exception;
	
	public int deleteCodeGroup(String codeGroups) throws Exception;


}
