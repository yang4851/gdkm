package com.insilicogen.gdkm.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.insilicogen.exception.RestTransferException;
import com.insilicogen.gdkm.Globals;
import com.insilicogen.gdkm.exception.ResearchException;
import com.insilicogen.gdkm.exception.UserException;
import com.insilicogen.gdkm.model.PageableList;
import com.insilicogen.gdkm.model.Research;
import com.insilicogen.gdkm.model.User;
import com.insilicogen.gdkm.service.ResearchAttachmentService;
import com.insilicogen.gdkm.service.ResearchNgsDataService;
import com.insilicogen.gdkm.service.ResearchOmicsFileService;
import com.insilicogen.gdkm.service.ResearchService;
import com.insilicogen.gdkm.util.RegistUtils;

@RestController
public class ResearchController extends AbstractController {

	static Logger logger = LoggerFactory.getLogger(ResearchController.class);
	
	@Resource(name="ResearchService")
	ResearchService researchService;
	
	@Resource(name="ResearchAttachmentService")
	ResearchAttachmentService attachmentService;
	
	@Resource(name="ResearchOmicsFileService")
	ResearchOmicsFileService omicsService;
	
	@Resource(name="ResearchNgsDataService")
	ResearchNgsDataService linkedService;
	
	@Resource(name = "messageSource")
	MessageSource messageSource;

	@RequestMapping(value={"/research"}, method=RequestMethod.GET)
	public PageableList<Research> getResearchList(
			@RequestParam Map<String, Object> params,
			HttpSession session) throws Exception {
		
		int page = getCurrentPage(params);
		int rowSize = getRowSize(params);
		int firstIndex = (page-1) * rowSize;
		
		params.put(Globals.PARAM_FIRST_INDEX, firstIndex);
		params.put(Globals.PARAM_ROW_SIZE, rowSize);
		
		if(session.getAttribute(Globals.LOGIN_USER) == null) {
			if(StringUtils.equalsIgnoreCase(Globals.PARAM_SECTION_REGIST, getSection(params)) ||
					StringUtils.equalsIgnoreCase(Globals.PARAM_SECTION_SYSTEM, getSection(params)))
				throw new UserException(messageSource.getMessage("ERR001", null, Locale.US), HttpStatus.UNAUTHORIZED);
					
			params.put(Globals.PARAM_OPEN_YN, Globals.STATUS_OPEN_Y);
		}
		
		try {
			List<Research> researchList = researchService.getResearchList(params);
			int total = researchService.getResearchCount(params);
			
			PageableList<Research> pageableList = new PageableList<Research>();
			pageableList.setRowSize(rowSize);
			pageableList.setPage(page);
			pageableList.setTotal(total);
			pageableList.setList(researchList);
			
			return pageableList;
		} catch (RestTransferException e) {
			throw e;
		} catch(Exception e) {
			throw new ResearchException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value={"/research"}, method=RequestMethod.POST)
	public Research createResearch(@RequestBody Research research, 
			HttpSession session) throws Exception {
		
		User loginUser = checkAuthorization(session);
		research.setRegistUser(loginUser);
		research.setUpdateUser(loginUser);
		
		try {
			research = researchService.createResearch(research);
		} catch (ResearchException e) {
			throw e;
		} catch(Exception e) {
			String dataType = messageSource.getMessage("ENTITY.RESEARCH", null, Locale.US);
			String message = messageSource.getMessage("ERR022", new String[]{ dataType, e.getMessage() }, Locale.US);
			
			throw new ResearchException(message, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return research;
	}
	
	@RequestMapping(value={"/research/{research_id}"}, method=RequestMethod.PUT)
	public Research changeResearch(
			@PathVariable("research_id") Integer researchId, 
			@RequestBody Research research, 
			HttpSession session) throws Exception {
		
		User loginUser = checkAuthorization(session);
		try {
			research.setUpdateUser(loginUser);
			return researchService.changeResearch(research);
		} catch (ResearchException e) {
			throw e;
		} catch(Exception e) {
			String dataType = messageSource.getMessage("ENTITY.RESEARCH", null, Locale.US);
			String message = messageSource.getMessage("ERR003", new String[]{ dataType }, Locale.US);
			throw new ResearchException(message, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value={"/research/{research_id}"}, method=RequestMethod.GET)
	public Research getResearch(
			@PathVariable("research_id") Integer researchId,
			HttpSession session) throws Exception {
		try {
			Research research = researchService.getResearch(researchId);
			if(!Globals.isOpenData(research)) 
				checkAuthorization(session);
			
			research.setAttachmentList(attachmentService.getAttachmentList(research));
			research.setOmicsList(omicsService.getOmicsFileList(research));
			research.setLinkedList(linkedService.getLinkedDataList(research));
			
			return research;
		} catch (ResearchException e) {
			throw e;
		} catch(Exception e) {
			String dataType = messageSource.getMessage("ENTITY.RESEARCH", null, Locale.US);
			String message = messageSource.getMessage("ERR024", new String[]{ dataType, e.getMessage() }, Locale.US);
			throw new ResearchException(message, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value={"/research/{research_id}"}, method=RequestMethod.DELETE)
	public Research deleteResearch(
			@PathVariable("research_id") Integer researchId,
			HttpSession session) throws Exception {
		
		checkAuthorization(session);
		Research research = researchService.getResearch(researchId);
		if(Globals.isOpenData(research))
			throw new ResearchException("Could not remove opened data");
		
		return researchService.deleteResearch(research);	
	}
	
	@RequestMapping(value={"/research}"}, method=RequestMethod.DELETE)
	public List<Research> deleteResearch() {
		return null;
	}
	
	/**
	 * 목록에서 선택된 연구성과물 일괄처리 요청 실행 
	 * 매개변수로 action을 받아 open, remove 에 따라 동작 따라 실행
	 * 
	 * @param params
	 * @param researchList
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value={"/research"}, method=RequestMethod.PUT)
	public PageableList<Research> doResearchBatchJob(
			@RequestParam Map<String, Object> params,
			@RequestBody List<Research> researchList,
			HttpSession session) throws Exception {
		
		List<Research> processedList = new ArrayList<Research>();
		User loginUser = checkAuthorization(session);
		
		for(Research research : researchList) {
			try {
				Research source = researchService.getResearch(research.getId());
				if(isRemoveRequest(params)) {
					if(!Globals.isOpenData(source)) {
						processedList.add(researchService.deleteResearch(research));
					}
				} else if (isOpenRequest(params)) {
					source.setUpdateUser(loginUser);
					if(StringUtils.equalsIgnoreCase(Globals.STATUS_OPEN_Y, source.getOpenYn())) {
						source.setRegistStatus(Globals.STATUS_REGIST_READY);
						source.setOpenYn(Globals.STATUS_OPEN_N);
						source.setOpenDate(RegistUtils.getDefaultOpenDate());
					} else {
						source.setRegistStatus(Globals.STATUS_REGIST_SUCCESS);
						source.setOpenYn(Globals.STATUS_OPEN_Y);
						source.setOpenDate(new Date(System.currentTimeMillis()));
					}
					
					processedList.add(researchService.changeResearch(source));
				}
			} catch(Exception e) {
				logger.error(e.getMessage());
			}
		}
		
		PageableList<Research> pageableList = new PageableList<Research>();
		pageableList.setTotal(researchList.size());
		pageableList.setRowSize(processedList.size());
		pageableList.setList(processedList);
		
		return pageableList;
	}
	
}
