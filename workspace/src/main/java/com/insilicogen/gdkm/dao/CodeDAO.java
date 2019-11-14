package com.insilicogen.gdkm.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.insilicogen.gdkm.model.Code;

import egovframework.rte.psl.dataaccess.EgovAbstractMapper;

@Repository("CodeDAO")
public class CodeDAO extends EgovAbstractMapper{
	
	public int insertCode(Code code) throws Exception{
		return insert("Code.insert",code);
	}
	
	public Code selectCode(String code) throws Exception{
		Map<String, Object> conditions = new HashMap<String, Object>();
		conditions.put("code", code);
		
		return selectOne("Code.get", conditions);
	}
	
	public List<Code> selectList(Map<String, Object> params, boolean isBasicForm) throws Exception{
		String queryId = (isBasicForm) ? "Code.selectBasic" : "Code.select";
		return selectList(queryId, params);
	}
	
	public int getCodeCount(Map<String, Object> params) throws Exception{
		return selectOne("Code.count", params);
	}
	
	public int updateCode(Code code) throws Exception{
		return update("Code.update", code);
	}
	
	public int deleteCode(String[] codes) throws Exception{
		return delete("Code.delete", codes);
	}

}
