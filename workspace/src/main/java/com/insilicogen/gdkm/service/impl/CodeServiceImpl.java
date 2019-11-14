package com.insilicogen.gdkm.service.impl;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.insilicogen.exception.RestTransferException;
import com.insilicogen.gdkm.dao.CodeDAO;
import com.insilicogen.gdkm.model.Code;
import com.insilicogen.gdkm.model.CodeGroup;
import com.insilicogen.gdkm.service.CodeGroupService;
import com.insilicogen.gdkm.service.CodeService;
import com.insilicogen.gdkm.util.EgovStringUtil;

@Service("CodeService")
public class CodeServiceImpl implements CodeService{
	
	@Resource(name="CodeDAO")
	CodeDAO codeDAO;
	
	@Resource(name="CodeGroupService")
	CodeGroupService codegroupService;

	@Override
	public int createCode(Code code) throws Exception {
		CodeGroup codeGroup = codegroupService.getCodeGroup(code.getGroup());
		if(codeGroup==null){
			throw new RestTransferException("존재하지 않는 그룹코드");
		}else{
			return codeDAO.insertCode(code);
		}
	}

	@Override
	public Code getCode(String code) throws Exception {
		return codeDAO.selectCode(code);
	}

	@Override
	public List<Code> getCodeList(Map<String, Object> params, boolean isBasicForm) throws Exception {
		return codeDAO.selectList(params, isBasicForm);
	}
	
	@Override
	public int getCodeCount(Map<String, Object> params) throws Exception {
		return codeDAO.getCodeCount(params);
	}

	@Override
	public int changeCode(Code code) throws Exception {
		return codeDAO.updateCode(code);
	}

	@Override
	public int deleteCode(String codes) throws Exception {
		String[] arrayCodes = EgovStringUtil.splitStringToStringArray(codes);
		return codeDAO.deleteCode(arrayCodes);
	}
}
