package com.insilicogen.gdkm.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.insilicogen.gdkm.model.CodeGroup;

import egovframework.rte.psl.dataaccess.EgovAbstractMapper;
@Repository("CodeGroupDAO")
public class CodeGroupDAO extends EgovAbstractMapper{
	
	public int insertCodeGroup(CodeGroup codeGroup) throws Exception{
		return insert("CodeGroup.insert",codeGroup);
	}
	
	public CodeGroup selectCodeGroup(String codeGroupId) throws Exception{
		Map<String, Object> conditions = new HashMap<String, Object>();
		conditions.put("codeGroupId", codeGroupId);
		
		return selectOne("CodeGroup.get", codeGroupId);
	}
	
	public List<CodeGroup> selectCodeGroupList(Map<String, Object> params, boolean isBasicForm) throws Exception{
		String queryId = (isBasicForm) ? "CodeGroup.selectBasic" : "CodeGroup.select";
		return selectList(queryId, params);
	}
	
	public int getCodeGroupCount(Map<String, Object> params) throws Exception{
		return selectOne("CodeGroup.count", params);
	}
	
	public int updateCodeGroup(CodeGroup codeGroup) throws Exception{
		return update("CodeGroup.update", codeGroup);
	}
	
	public int deleteCodeGroup(String[] codeGroups) throws Exception{
		return delete("CodeGroup.delete", codeGroups);
	}

}
