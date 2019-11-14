package com.insilicogen.gdkm.web;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.context.MessageSource;
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
import com.insilicogen.gdkm.service.CodeService;

@RestController
public class CodeController  extends AbstractController{
	
	@Resource(name="CodeService")
	CodeService codeService;

	@Resource(name = "messageSource")
	private MessageSource messageSource;
	
	@RequestMapping(value="/codes", method=RequestMethod.POST)
	public void createCode(
			HttpSession session
			,@RequestBody Code code
			) throws Exception{
		 checkAuthorization(session);
		try{
			synchronized(CodeService.Lock){
				String useYn = code.getUseYn().toUpperCase();
				if(!useYn.equals("Y") && !useYn.equals("N")){
					throw new Exception("사용 유무는 Y or N 만 가능합니다.");
				}
				codeService.createCode(code);
			}
		}catch(Exception e){
			String args = messageSource.getMessage("ENTITY.CODE",null, Locale.US);
			String message = messageSource.getMessage("ERR002", new String[]{args}, Locale.US);
			throw new RestTransferException(message + " - "+e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//			throw new RestTransferException("그룹코드 등록 오류 - " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value="/codes/{code_id}", method=RequestMethod.PUT)
	public void changeCode(@PathVariable("code_id") String codeId
			,@RequestBody Code code
			,HttpSession session
			) throws Exception{
		checkAuthorization(session);
		try{
			synchronized(CodeService.Lock){
				String useYn = code.getUseYn().toUpperCase();
				if(!useYn.equals("Y") && !useYn.equals("N")){
					throw new Exception("사용 유무는 Y or N 만 가능합니다.");
				}
				int changeCnt = codeService.changeCode(code);
				if(changeCnt == 0){
					throw new ZeroCountException();
				}
			}
		}catch(Exception e){
			String args = messageSource.getMessage("ENTITY.CODE",null, Locale.US);
			String message = messageSource.getMessage("ERR003", new String[]{args}, Locale.US);
			throw new RestTransferException(message+" - "+e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//			throw new RestTransferException("그룹코드 수정 오류 - " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value="/codes/{code_id}", method=RequestMethod.GET)
	public Code getCode(
			@PathVariable("code_id") String codeId
			,HttpSession session
			){
		checkAuthorization(session);
		try{
			Code code = codeService.getCode(codeId);
			if(code==null){
				String args = messageSource.getMessage("ENTITY.CODE",null,Locale.US);
				String message = messageSource.getMessage("ERR006", new String[]{args}, Locale.US);
				throw new RestTransferException(message, HttpStatus.NOT_FOUND);
//				throw new ZeroCountException();
			}
			return code;
		}catch(Exception e){
			String args = messageSource.getMessage("ENTITY.CODE",null, Locale.US);
			String message = messageSource.getMessage("ERR004", new String[]{args}, Locale.US);
			throw new RestTransferException(message +" - "+e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//			throw new RestTransferException("그룹코드 조회 오류 - " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/**
	 * 공통코드 목록을 {@link PageableList} 객체로 반환
	 * 
	 * 검색조건(param)은 6개의 파라미터를 전달 받음 
	 *  1. 공통코드 그룹 : group
	 *  2. 공통코드 : code (어디에 사용되는지 모르겠음)
	 *  3. 코드이름 : name (이름으로 검색)
	 *  4. 사용여부 : useYn (Y|N)
	 *  5. 페이지 번호 : page (테이블 형식으로 검색하는 경우 해당 페이지 번호)
	 *  6. 페이지 크기 : rowSize (테이블 형식으로 검색하는 경우 테이블 레코드 개수)	 * 
	 * 
	 * @param session
	 * @param param 검색조건
	 * @return
	 */
	@RequestMapping(value="/codes", method=RequestMethod.GET)
	public PageableList<Code> getCodeList(@RequestParam Map<String, Object> params, HttpSession session) {
		try{
			int page = getCurrentPage(params.get(Globals.PARAM_PAGE));
			int rowSize = getRowSize(params.get(Globals.PARAM_ROW_SIZE));
			int firstIndex = (page-1) * rowSize;

			params.put("firstIndex", firstIndex);
			params.put("rowSize", rowSize);
			
			Boolean isBasicForm = getBasicMode(params.get(Globals.PARAM_IS_BASICFORM));
			List<Code> codeList = codeService.getCodeList(params, isBasicForm);
			int totalCount = codeService.getCodeCount(params);
			
			PageableList<Code> codes = new PageableList<Code>();
			codes.setRowSize(rowSize);
			codes.setPage(page);
			codes.setList(codeList);
			codes.setTotal(totalCount);
			
			return codes;
		}catch(Exception e){
			String args = messageSource.getMessage("ENTITY.CODE",null, Locale.US);
			String message = messageSource.getMessage("ERR004", new String[]{args}, Locale.US);
			throw new RestTransferException(message + " - "+e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//			throw new RestTransferException("그룹코드 목록 조회 오류 - " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value="/codes/{code_ids}", method=RequestMethod.DELETE)
	public void deleteCode(
			@PathVariable("code_ids") String codeIds
			,HttpSession session
			) throws Exception{
		checkAuthorization(session);
		try{
			synchronized(CodeService.Lock){
				int deleteCnt = codeService.deleteCode(codeIds);
				if(deleteCnt == 0){
					throw new ZeroCountException();
				}
			}
		}catch(Exception e){
			String args = messageSource.getMessage("ENTITY.CODE",null, Locale.US);
			String message = messageSource.getMessage("ERR005", new String[]{args}, Locale.US);
			throw new RestTransferException(message+" - "+e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//			throw new RestTransferException("그룹코드 삭제 오류 - " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value="/codes/check/id", method=RequestMethod.GET)
	public void getCodeIdCheck(
			@RequestParam("codeId") String codeId
			)throws Exception{
		Code code = codeService.getCode(codeId);
		if(code==null){
			
			throw new RestTransferException("사용가능 " , HttpStatus.OK);
		}else{
			String message = messageSource.getMessage("ERR019", null, Locale.US);
			throw new RestTransferException(message, HttpStatus.INTERNAL_SERVER_ERROR);
//			throw new RestTransferException("사용불가" , HttpStatus.BAD_REQUEST);
		}
	}
}
