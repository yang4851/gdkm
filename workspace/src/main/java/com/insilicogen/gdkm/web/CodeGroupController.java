package com.insilicogen.gdkm.web;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.insilicogen.exception.RestTransferException;
import com.insilicogen.exception.ZeroCountException;
import com.insilicogen.gdkm.Globals;
import com.insilicogen.gdkm.model.Code;
import com.insilicogen.gdkm.model.CodeGroup;
import com.insilicogen.gdkm.model.PageableList;
import com.insilicogen.gdkm.service.CodeGroupService;
import com.insilicogen.gdkm.service.CodeService;

@RestController
public class CodeGroupController  extends AbstractController{
	
	@Resource(name="CodeGroupService")
	CodeGroupService codeGroupService;
	
	@Resource(name="CodeService")
	CodeService codeService;
	
	@Resource(name = "messageSource")
	private MessageSource messageSource;

	@RequestMapping(value="/codeGroups", method=RequestMethod.POST)
	public void createCodeGroup(
			HttpSession session
			,@RequestBody CodeGroup codeGroup
			) throws Exception{
		 checkAuthorization(session);
		try{
			synchronized(CodeGroupService.Lock){
				String useYn = codeGroup.getUseYn().toUpperCase();
				if(!useYn.equals("Y") && !useYn.equals("N")){
					throw new Exception("사용 유무는 Y or N 만 가능합니다.");
				}
				codeGroupService.createCodeGroup(codeGroup);
			}
		}catch(Exception e){
			String args = messageSource.getMessage("ENTITY.GROUPCODE",null,Locale.US);
			String message = messageSource.getMessage("ERR002", new String[]{args}, Locale.US);
			throw new RestTransferException(message, HttpStatus.INTERNAL_SERVER_ERROR);
//			throw new RestTransferException("코드그룹 등록 오류 - " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value="/codeGroups/{codeGroup_id}", method=RequestMethod.PUT)
	public void changeCodeGroup(@PathVariable("codeGroup_id") String codeGroupId
			,@RequestBody CodeGroup codeGroup
			,HttpSession session
			) throws Exception{
		checkAuthorization(session);
		try{
			synchronized(CodeGroupService.Lock){
				String useYn = codeGroup.getUseYn().toUpperCase();
				if(!useYn.equals("Y") && !useYn.equals("N")){
					throw new Exception("사용 유무는 Y or N 만 가능합니다.");
				}
				int changeCnt = codeGroupService.changeCodeGroup(codeGroup);
				if(changeCnt == 0){
					throw new ZeroCountException();
				}
			}
		}catch(Exception e){
			String args = messageSource.getMessage("ENTITY.GROUPCODE",null,Locale.US);
			String message = messageSource.getMessage("ERR003", new String[]{args}, Locale.US);
			throw new RestTransferException(message+ " - "+e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//			throw new RestTransferException("코드그룹 수정 오류 - " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value="/codeGroups/{codeGroup_id}", method=RequestMethod.GET)
	public CodeGroup getCodeGroup(
			@PathVariable("codeGroup_id") String codeGroupId
			,HttpSession session
			){
		checkAuthorization(session);
		try{
			
			CodeGroup codeGroup = codeGroupService.getCodeGroup(codeGroupId);
			if(codeGroup==null){
				String args = messageSource.getMessage("ENTITY.GROUPCODE",null,Locale.US);
				String message = messageSource.getMessage("ERR006", new String[]{args}, Locale.US);
				throw new RestTransferException(message, HttpStatus.NOT_FOUND);
//				throw new ZeroCountException();
			}
			return codeGroup;
		}catch(Exception e){
			String args = messageSource.getMessage("ENTITY.GROUPCODE",null,Locale.US);
			String message = messageSource.getMessage("ERR004", new String[]{args}, Locale.US);
			throw new RestTransferException(message+ " - "+e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//			throw new RestTransferException("코드그룹 조회 오류 - " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value="/codeGroups", method=RequestMethod.GET)
	public PageableList<CodeGroup> getCodeGroupList(
			@RequestParam Map<String, Object> params
			,HttpSession session
			){
		try{
			int page = getCurrentPage(params.get(Globals.PARAM_PAGE));
			int rowSize = getRowSize(params.get(Globals.PARAM_ROW_SIZE));
			int firstIndex = (page-1) * rowSize;
			
			params.put("firstIndex", firstIndex);
			params.put("rowSize", rowSize);
			
			Boolean isBasicForm = getBasicMode(params.get(Globals.PARAM_IS_BASICFORM));
			List<CodeGroup> codeGroupList = codeGroupService.getCodeGroupList(params, isBasicForm);
			int totalCount = codeGroupService.getCodeGroupCount(params);
			
			PageableList<CodeGroup> codeGroups = new PageableList<CodeGroup>();
			codeGroups.setRowSize(rowSize);
			codeGroups.setPage(page);
			codeGroups.setList(codeGroupList);
			codeGroups.setTotal(totalCount);
			
			return codeGroups;
		}catch(Exception e){
			String args = messageSource.getMessage("ENTITY.GROUPCODE",null,Locale.US);
			String message = messageSource.getMessage("ERR004", new String[]{args}, Locale.US);
			throw new RestTransferException(message + " - "+e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//			throw new RestTransferException("코드그룹 목록 조회 오류 - " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value="/codeGroups/{codeGroup_ids}", method=RequestMethod.DELETE)
	public void deleteCodeGroup(
			@PathVariable("codeGroup_ids") String codeGroupIds
			,HttpSession session
			) throws Exception{
		checkAuthorization(session);
		try{
			synchronized(CodeGroupService.Lock){
				int deleteCnt = codeGroupService.deleteCodeGroup(codeGroupIds);
				if(deleteCnt == 0){
					throw new ZeroCountException();
				}
			}
		}catch(DataIntegrityViolationException integrityEx){
			// 코드그룹을 사용하고 있는 코드 리스트
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("group", codeGroupIds);
			params.put("USE_YN", "Y");
			List<Code> codeList = codeService.getCodeList(params, true);
			
			String args = codeGroupIds +" : ";
			for(Code code : codeList){
				args += code.getCode()+" ";
			}
			
			String message = messageSource.getMessage("ERR018", new String[]{args}, Locale.US);
			throw new RestTransferException(message, HttpStatus.INTERNAL_SERVER_ERROR);
//			throw new RestTransferException("해당 코드그룹를 사용하는 코드가 존재하므로 삭제가 불가능 합니다.", HttpStatus.INTERNAL_SERVER_ERROR);
		}catch(Exception e){
			String args = messageSource.getMessage("ENTITY.GROUPCODE",null,Locale.US);
			String message = messageSource.getMessage("ERR005", new String[]{args}, Locale.US);
			throw new RestTransferException(message+ " - "+e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//			throw new RestTransferException("코드그룹 삭제 오류 - " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value="/codeGroups/check/id", method=RequestMethod.GET)
	public void getCodeIdCheck(
			@RequestParam("codeGroupId") String codeGroupId
			)throws Exception{
		CodeGroup codeGroup = codeGroupService.getCodeGroup(codeGroupId);
		if(codeGroup==null){
			
			throw new RestTransferException("사용가능 " , HttpStatus.OK);
		}else{
			String args = messageSource.getMessage("ENTITY.GROUPCODE",null,Locale.US);
			String message = messageSource.getMessage("ERR017", new String[]{args}, Locale.US);
			throw new RestTransferException(message, HttpStatus.INTERNAL_SERVER_ERROR);
//			throw new RestTransferException("사용불가" , HttpStatus.BAD_REQUEST);
		}
	}
}
