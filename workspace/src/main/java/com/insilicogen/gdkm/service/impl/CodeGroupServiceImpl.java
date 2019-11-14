package com.insilicogen.gdkm.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.insilicogen.gdkm.dao.CodeGroupDAO;
import com.insilicogen.gdkm.model.CodeGroup;
import com.insilicogen.gdkm.service.CodeGroupService;
import com.insilicogen.gdkm.util.EgovStringUtil;

@Service("CodeGroupService")
public class CodeGroupServiceImpl implements CodeGroupService{

	@Resource(name="CodeGroupDAO")
	CodeGroupDAO codeGroupDAO;
	
	@Override
	public int createCodeGroup(CodeGroup codeGroup) throws Exception {
		return codeGroupDAO.insertCodeGroup(codeGroup);
	}

	@Override
	public CodeGroup getCodeGroup(String codeGroupId) throws Exception {
		return codeGroupDAO.selectCodeGroup(codeGroupId);
	}

	@Override
	public List<CodeGroup> getCodeGroupList(Map<String, Object> params, boolean isBasicForm)
			throws Exception {
		return codeGroupDAO.selectCodeGroupList(params, isBasicForm);
	}
	
	@Override
	public int getCodeGroupCount(Map<String, Object> params) throws Exception{
		return codeGroupDAO.getCodeGroupCount(params);
	}

	@Override
	public int changeCodeGroup(CodeGroup codeGroup) throws Exception {
		return codeGroupDAO.updateCodeGroup(codeGroup);
	}

	@Override
	public int deleteCodeGroup(String codeGroups) throws Exception {
		String[] arrayCodes = EgovStringUtil.splitStringToStringArray(codeGroups);
		return codeGroupDAO.deleteCodeGroup(arrayCodes);
	}

}
