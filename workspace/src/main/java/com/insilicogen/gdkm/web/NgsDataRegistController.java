package com.insilicogen.gdkm.web;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.insilicogen.gdkm.Globals;
import com.insilicogen.gdkm.exception.NgsDataException;
import com.insilicogen.gdkm.exception.NgsDataNotFoundException;
import com.insilicogen.gdkm.model.AbstractIdObject;
import com.insilicogen.gdkm.model.NgsDataAchive;
import com.insilicogen.gdkm.model.NgsDataRegist;
import com.insilicogen.gdkm.model.PageableList;
import com.insilicogen.gdkm.model.Taxonomy;
import com.insilicogen.gdkm.model.User;
import com.insilicogen.gdkm.service.NgsDataAchiveService;
import com.insilicogen.gdkm.service.NgsDataRegistService;
import com.insilicogen.gdkm.service.NgsFileProcessService;
import com.insilicogen.gdkm.service.TaxonomyService;
import com.insilicogen.gdkm.util.FileDownloadUtil;
import com.insilicogen.gdkm.util.RegistUtils;

@RestController
public class NgsDataRegistController extends AbstractController {
	
	static Logger logger = LoggerFactory.getLogger(NgsDataRegistController.class);
	
	@Resource(name="messageSource")
	private MessageSource messageSource;
	
	@Resource(name="TaxonomyService")
	TaxonomyService taxonomyService;
	
	@Resource(name="NgsFileProcessService")
	NgsFileProcessService processService;
	
	@Resource(name="NgsDataRegistService")
	NgsDataRegistService ngsDataRegistService;
	
	@Resource(name="NgsDataAchiveService")
	NgsDataAchiveService ngsDataAchiveService;
	
	@RequestMapping(value={"/regist-data"}, method=RequestMethod.POST)
	public NgsDataRegist createNgsDataRegist(
			@RequestBody NgsDataRegist dataRegist,
			HttpSession session) throws Exception{
		User loginUser = (User)session.getAttribute(Globals.LOGIN_USER);
		if(loginUser == null) 
			throw new NgsDataException(messageSource.getMessage("ERR001", null, Locale.US), HttpStatus.UNAUTHORIZED);
				
		try{
			if(!Globals.isValidUser(dataRegist.getRegistUser())) {
				dataRegist.setRegistUser(loginUser);
				dataRegist.setUpdateUser(loginUser);
			}
			synchronized(NgsDataRegistService.Lock){
				return ngsDataRegistService.createNgsDataRegist(dataRegist);
			}
		} catch(NgsDataException e) {
			throw e;
		} catch(Exception e){
			String dataType = messageSource.getMessage("ENTITY.NGSDATA", null, Locale.US);
			String message = messageSource.getMessage("ERR022", new String[]{ dataType, e.getMessage() }, Locale.US);
			throw new NgsDataException(message, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 *
	 * 기존에 사용하던 [[[ @RequestParam("registId") String registId ]]] 방식으로 parameter를 여러개 지정하지 않고, 
	 * [[[ @RequestParam Map<String,Object> params ]]]으로 한줄만 작성하면, 
	 * parameter를 자동으로 map에 담아 넘겨 받을 수 있다.
	 * (참고 : https://gs.saro.me/#!m=elec&jn=878)
	 * @param params
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value={"/regist-data"}, method=RequestMethod.GET)
	public PageableList<NgsDataRegist> getNgsDataRegistList(
			@RequestParam Map<String, Object> params,
			HttpSession session) throws Exception {

		try{
			int page = getCurrentPage(params.get(Globals.PARAM_PAGE));
			int rowSize = getRowSize(params.get(Globals.PARAM_ROW_SIZE));
			int firstIndex = (page-1) * rowSize;
			
			params.put(Globals.PARAM_FIRST_INDEX, firstIndex);
			params.put(Globals.PARAM_ROW_SIZE, rowSize);
			
			if(session.getAttribute(Globals.LOGIN_USER) == null)
				params.put(Globals.PARAM_OPEN_YN, Globals.STATUS_OPEN_Y);
			
			List<NgsDataRegist> registList = ngsDataRegistService.getNgsDataRegistList(params);
			int totalCount = ngsDataRegistService.getNgsDataRegistCount(params);
			
			PageableList<NgsDataRegist> pageableList = new PageableList<NgsDataRegist>();
			pageableList.setRowSize(rowSize);
			pageableList.setPage(page);
			pageableList.setList(registList);
			pageableList.setTotal(totalCount);
			
			return pageableList;
		} catch(NgsDataException e) {
			throw e;
		} catch(Exception e){
			throw new NgsDataException("", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value={"/regist-data/{data_id}"}, method=RequestMethod.PUT)
	public NgsDataRegist changeNgsDataRegist(
				@PathVariable("data_id") String registId,
				@RequestBody NgsDataRegist dataRegist,
				HttpSession session) throws Exception{
		User loginUser = checkAuthorization(session);
		try{
			if(!Globals.isValidUser(dataRegist.getUpdateUser())) {
				dataRegist.setUpdateUser(loginUser);
			}
			synchronized(NgsDataRegistService.Lock){
				return ngsDataRegistService.changeNgsDataRegist(dataRegist);
			}
		} catch(NgsDataNotFoundException e) {
			throw e;
		} catch(NgsDataException e) {
			throw e;
		} catch(Exception e){
			String dataType = messageSource.getMessage("ENTITY.NGSDATA", null, Locale.US);
			String message = messageSource.getMessage("ERR003", new String[]{ dataType }, Locale.US);
			throw new NgsDataException(message, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value={"/regist-data/{data_id}"}, method=RequestMethod.GET)
	public NgsDataRegist getNgsDataRegist(
			@PathVariable("data_id") Integer dataId,
			HttpSession session) throws Exception{
		
		try{
			NgsDataRegist regist = ngsDataRegistService.getNgsDataRegist(dataId);
			if(!Globals.isOpenData(regist)) {
				checkAuthorization(session);
			}
			
			return regist;
		} catch(NgsDataNotFoundException e) {
			throw e;
		} catch(NgsDataException e) {
			throw e;
		} catch(Exception e){
			String dataType = messageSource.getMessage("ENTITY.NGSDATA", null, Locale.US);
			String message = messageSource.getMessage("ERR024", new String[]{ dataType, e.getMessage() }, Locale.US);
			throw new NgsDataException(message, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value={"/regist-data/{data_id}/linked-data"}, method=RequestMethod.GET)
	public List<NgsDataRegist> getNgsDataLinkedList(@PathVariable("data_id") Integer registId) throws Exception{
		try{
			NgsDataRegist regist = ngsDataRegistService.getNgsDataRegist(registId);
			return ngsDataRegistService.getNgsDataLinkedList(regist);
		} catch(NgsDataException e) {
			throw e;
		} catch(Exception e) {
			throw new NgsDataException("NGS data search error - " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value={"/regist-data/confirm-status"}, method=RequestMethod.PUT)
	public List<NgsDataRegist> changeConfirmStatus(
				@RequestBody List<NgsDataRegist> registList,
				HttpSession session) throws Exception{
		
		User loginUser = checkAuthorization(session);
		List<NgsDataRegist> changedList = new ArrayList<NgsDataRegist>();
		boolean errorCheck = false;
		boolean exceptionCheck = false;
		try{
			for(NgsDataRegist source : registList) {
				NgsDataRegist target = ngsDataRegistService.getNgsDataRegist(source.getId());
				
				// 파일 검증이 success인지 확인하기 위해 전체 파일목록 조회
				List<NgsDataAchive> achiveList = ngsDataAchiveService.getNgsDataAchiveList(target);
				
				// 파일이 regist_status가  validated가 아니거나 verifi_status가 success가 아니면 error 
				for(NgsDataAchive achive : achiveList){
					if(!Globals.STATUS_VERIFY_SUCCESS.equals(achive.getVerifiStatus())
							|| !achive.getRegistStatus().equals(Globals.STATUS_REGIST_VALIDATED)){
						errorCheck = true; // target의 상태를 error로 바꾸기 위한 check
						exceptionCheck = true; // exception발생을 위한 check 
//						throw new NgsDataException(messageSource.getMessage("ERR012", null, Locale.US), HttpStatus.BAD_REQUEST);
					}
				}
				
				if(!target.getRegistStatus().equals(Globals.STATUS_REGIST_SUBMIT)){
					errorCheck = true;
					exceptionCheck = true;
//					throw new NgsDataException(messageSource.getMessage("ERR012", null, Locale.US), HttpStatus.BAD_REQUEST);
				}
				
				target.setApprovalStatus(source.getApprovalStatus());
				target.setApprovalDate(new Date());
				target.setApprovalUser(loginUser);
				// 파일이 에러가 아닐때
				if(!errorCheck){
					target.setRegistStatus(Globals.isAcceptedData(source)? Globals.STATUS_REGIST_SUCCESS : Globals.STATUS_REGIST_FAILED);
					errorCheck = false;
				}else{
					target.setRegistStatus(Globals.STATUS_REGIST_ERROR);
				}
				target.setUpdateUser(loginUser);
				synchronized(NgsDataRegistService.Lock){
					changedList.add(ngsDataRegistService.changeNgsDataRegist(target));
				}
			}
			// 에러가 났을 때
			if(exceptionCheck){
				throw new NgsDataException(messageSource.getMessage("ERR012", null, Locale.US), HttpStatus.BAD_REQUEST);
			}
		}catch(NullPointerException e){
			String dataType = messageSource.getMessage("ENTITY.NGSDATAFILE", null, Locale.US);
			String message = messageSource.getMessage("ERR006", new String[]{ dataType }, Locale.US);
			throw new NgsDataException(message, HttpStatus.NOT_FOUND);
		}catch(NgsDataException e){
			throw e;
		}catch(Exception e){
			String dataType = messageSource.getMessage("ENTITY.NGSDATA", null, Locale.US);
			String message = messageSource.getMessage("ERR023", new String[]{ dataType, e.getMessage() }, Locale.US);
			throw new NgsDataException(message, HttpStatus.INTERNAL_SERVER_ERROR);
		}
			
		return changedList;
	}
		
	@RequestMapping(value={"/regist-data/open-status"}, method=RequestMethod.PUT)
	public List<NgsDataRegist> changeOpenStatus(
			@RequestBody List<NgsDataRegist> registList,
			HttpSession session) throws Exception {
		User loginUser = checkAuthorization(session);
		List<NgsDataRegist> changedList = new ArrayList<NgsDataRegist>();
		
		boolean errorCheck = false;
		boolean exceptionCheck = false;
		
		try {
			for(NgsDataRegist source : registList) {
				NgsDataRegist target = ngsDataRegistService.getNgsDataRegist(source.getId());
				
				// 파일 검증이 success인지 확인하기 위해 전체 파일목록 조회
				List<NgsDataAchive> achiveList = ngsDataAchiveService.getNgsDataAchiveList(target);
				
				// 파일이 regist_status가  validated가 아니거나 verifi_status가 success가 아니면 error 
				for(NgsDataAchive achive : achiveList){
					if(!achive.getVerifiStatus().equals(Globals.STATUS_VERIFY_SUCCESS)
							|| !achive.getRegistStatus().equals(Globals.STATUS_REGIST_VALIDATED)){
						errorCheck = true; // target의 상태를 error로 바꾸기 위한 check
						exceptionCheck = true; // exception발생을 위한 check 
					}
				}
				// regist상태가 success가 아니거나 accept 되지 않았다면 error
				if(!target.getRegistStatus().equals(Globals.STATUS_REGIST_SUCCESS)
						|| !target.getApprovalStatus().equals(Globals.STATUS_APPROVAL_ACCEPT)){
					errorCheck = true; // target의 상태를 error로 바꾸기 위한 check
					exceptionCheck = true;
				}
				
				// 상태가 accept이고 errorCheck가 false일때 
				if(Globals.isAcceptedData(target) && !errorCheck) {
					target.setOpenYn(source.getOpenYn());
					target.setOpenDate(new Date());
					target.setUpdateUser(loginUser);
					errorCheck = false;
				}else{
					target.setRegistStatus(Globals.STATUS_REGIST_ERROR);
				}
				
				synchronized(NgsDataRegistService.Lock){
					changedList.add(ngsDataRegistService.changeNgsDataRegist(target));
				}
				
			}
			// 에러가 났을 때
			if(exceptionCheck){
				String dataType1 = messageSource.getMessage("ENTITY.OPEN", null, Locale.US);
				String dataType2 = messageSource.getMessage("ENTITY.RS6/8", null, Locale.US); 
				throw new NgsDataException(messageSource.getMessage("ERR013", new String[]{ dataType1, dataType2 }, Locale.US), HttpStatus.BAD_REQUEST);
			}
		} catch(NullPointerException e){
			String dataType = messageSource.getMessage("ENTITY.NGSDATA", null, Locale.US);
			String message = messageSource.getMessage("ERR006", new String[]{ dataType }, Locale.US);
			throw new NgsDataException(message, HttpStatus.NOT_FOUND);
		} catch(NgsDataException e){
			throw e;
		} catch (Exception e) {
			logger.error(e.getMessage());
			String dataType = messageSource.getMessage("ENTITY.NGSDATA", null, Locale.US);
			String message = messageSource.getMessage("ERR023", new String[]{ dataType, e.getMessage() }, Locale.US);
			throw new NgsDataException(message, HttpStatus.INTERNAL_SERVER_ERROR);
		}
			
		return changedList;
	}
	
	@RequestMapping(value={"/regist-data/regist-status"}, method=RequestMethod.PUT)
	public List<NgsDataRegist> changeRegistStatus(
			@RequestBody List<NgsDataRegist> registList,
			HttpSession session) throws Exception{
		User loginUser = checkAuthorization(session);
		List<NgsDataRegist> changedList = new ArrayList<NgsDataRegist>();
		for(NgsDataRegist source : registList) {
			try {
				synchronized(NgsDataRegistService.Lock){
					NgsDataRegist target = ngsDataRegistService.getNgsDataRegist(source.getId());
					target.setRegistStatus(getValidRegistStatus(source));
					target.setUpdateUser(loginUser);
					
					changedList.add(ngsDataRegistService.changeNgsDataRegist(target));
				}
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		}
			
		return changedList;
	}
	
	public String getValidRegistStatus(NgsDataRegist regist) {
		try {
			if(!RegistUtils.isValidatedData(regist))
				return regist.getRegistStatus();
			
			List<NgsDataAchive> achiveList = ngsDataAchiveService.getNgsDataAchiveList(regist);
			int successCnt = 0;
			int failedCnt = 0;
			int runningCnt = 0;
			int readyCnt = 0;
			
			for(NgsDataAchive file : achiveList) {
				if(Globals.STATUS_VERIFY_SUCCESS.equals(file.getVerifiStatus())) {
					successCnt++;
				} else if(Globals.STATUS_VERIFY_FAILED.equals(file.getVerifiStatus())) {
					failedCnt++;
				} else if(Globals.STATUS_VERIFY_RUNNING.equals(file.getVerifiStatus())) {
					runningCnt++;
				} else if(Globals.STATUS_VERIFY_WARN.equals(file.getVerifiStatus())) {
					successCnt++;
				} else {
					readyCnt++;
				}
			}
			
			if(runningCnt > 0) {
				return Globals.STATUS_REGIST_VALIDATING;
			} else if(failedCnt > 0) {
				return Globals.STATUS_REGIST_ERROR;
			} else if(readyCnt > 0 || achiveList.size() == 0) {
				if(Globals.REGIST_STATUS_LIST.indexOf(regist.getRegistStatus()) > 0) {
					return Globals.STATUS_REGIST_READY;
				}
			} else if(successCnt == achiveList.size()) {
				if(Globals.REGIST_STATUS_LIST.indexOf(regist.getRegistStatus()) > 2) {
					return Globals.STATUS_REGIST_VALIDATED;
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return regist.getRegistStatus();
	}
	
	/**
	 * 검증실행 요청 처리 메소드
	 * 
	 * @param registList
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value={"/regist-data/validations"}, method=RequestMethod.PUT)
	public List<NgsDataRegist> validateNgsDataRegists(
			@RequestBody List<NgsDataRegist> registList,
			HttpSession session) throws Exception {
		User loginUser = checkAuthorization(session);
		List<NgsDataRegist> pendingList = new ArrayList<NgsDataRegist>();
//		String dataType = messageSource.getMessage("ENTITY.NGSDATA", null, Locale.US);
		String errorMessage = null;
		
		for(NgsDataRegist source : registList) {
			NgsDataRegist target = ngsDataRegistService.getNgsDataRegist(source.getId());
			
			if(Globals.isVerifiRunnable(target)) {
				target.setUpdateUser(loginUser);
				target.setRegistStatus(Globals.STATUS_REGIST_VALIDATING);
				
				ngsDataRegistService.changeNgsDataRegist(target);
				
				pendingList.add(target);
			}else{
				errorMessage = messageSource.getMessage("ERR027", new String[] { 
						target.getRegistNo(), target.getRegistStatus() },
						Locale.US);
			}
		}
		
		if(pendingList.size() > 0) {
			processService.validateNgsData(pendingList);
		} else {
			errorMessage = messageSource.getMessage("ERR026", null, Locale.US);
		}
		
		if(StringUtils.isNotBlank(errorMessage)) {
			throw new NgsDataException(errorMessage, HttpStatus.BAD_REQUEST);
		}
		
		return pendingList;
	}
	
	@RequestMapping(value={"/taxonomies/{taxonId}/tree-nodes"}, method=RequestMethod.GET)
	public List<NgsDataRegist> getTreeNodeListOfTaxonomy(
			@PathVariable("taxonId") Integer taxonId,
			@RequestParam Map<String, Object> params) throws Exception{
		
		try {
			Taxonomy taxonomy = taxonomyService.getTaxonomy(taxonId);
			params.put("taxonId", taxonomy.getTaxonId());
			
			params.put("dataType", Globals.REGIST_DATA_RAWDATA);
			List<NgsDataRegist> nodeList = ngsDataRegistService.getNgsDataRegistList(params);
			
			params.put("dataType", Globals.REGIST_DATA_PROCESSEDDATA);
			nodeList.addAll(ngsDataRegistService.getNgsDataRegistList(params));
			
			return nodeList;
		} catch(NgsDataException e){
			throw e;
		} catch (Exception e) {
			throw new NgsDataException(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value={"/regist-data/{dataId}/tree-nodes"}, method=RequestMethod.GET)
	public List<AbstractIdObject> getProcessedNode(
			@PathVariable("dataId") Integer dataId,
			@RequestParam Map<String, Object> params) throws Exception{
		try {
			NgsDataRegist data = ngsDataRegistService.getNgsDataRegist(dataId);
			if(Globals.REGIST_DATA_RAWDATA.equals(data.getDataType())) {
				params.put("rawRegistId", data.getId());
			} else {
				params.put("processedRegistId", data.getId());
			}
			
			List<NgsDataRegist> dataList = ngsDataRegistService.getNgsDataRegistList(params);
			List<NgsDataAchive> achiveList = ngsDataAchiveService.getNgsDataAchiveList(data);
			
			List<AbstractIdObject> nodeList = new ArrayList<AbstractIdObject>();
			nodeList.addAll(dataList);
			nodeList.addAll(achiveList);
			
			return nodeList;
		} catch(NgsDataException e){
			throw e;
		} catch (Exception e) {
			throw new NgsDataException(e.getMessage(), HttpStatus.BAD_REQUEST);
		}	
	}
	
	@RequestMapping(value={"/regist-data/{data_id}"}, method=RequestMethod.DELETE)
	public NgsDataRegist deleteNgsDataRegist(
			@PathVariable("data_id") Integer registId,
			HttpSession session) throws Exception {
		checkAuthorization(session);
		try{
			NgsDataRegist regist = ngsDataRegistService.getNgsDataRegist(registId);
			synchronized(NgsDataRegistService.Lock){
				if(ngsDataRegistService.deleteNgsDataRegist(regist) > 0) {
					return regist;
				}
			}
			String message = messageSource.getMessage("ERR005", new String[]{ regist.getRegistNo() }, Locale.US);
			throw new NgsDataException(message, HttpStatus.BAD_REQUEST);
		} catch(NgsDataNotFoundException e) {
			throw e;
		} catch(NgsDataException e) {
			throw e;
		} catch(Exception e){
			throw new NgsDataException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value="/regist-data/{data_type}/list-down", method = RequestMethod.GET)
	public void downloadExcel(
			@RequestParam Map<String, Object> params,
			@PathVariable("data_type") String dataType,
			ModelMap model,
			HttpServletRequest request,
			HttpServletResponse response,
			HttpSession session
			)throws Exception{
		if(params.get("openYn")==null){
			checkAuthorization(session);
		}
		try{
			//1. 파일 생성
			File file = ngsDataRegistService.downloadRegistDataExcelFile(params);
			
			//2. 브라우저에 파일다운로드 진행완료적용
			FileDownloadUtil fileView=new FileDownloadUtil();
			String section = params.get("section")==null ? "Integrated" : (String) params.get("section");
			String fileName = params.get("dataType")+"_"+section+".xls";
			model.addAttribute("file", file);
			model.addAttribute("fileName", fileName);

			fileView.render((Map) model, request, response);
			
		} catch(NgsDataException e) {
			e.printStackTrace();
			throw e;
		} catch(Exception e){
			e.printStackTrace();
			throw new NgsDataException("Fail", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
