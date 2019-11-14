package com.insilicogen.gdkm.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

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

import com.insilicogen.gdkm.Globals;
import com.insilicogen.gdkm.exception.ResearchException;
import com.insilicogen.gdkm.model.NgsDataRegist;
import com.insilicogen.gdkm.model.PageableList;
import com.insilicogen.gdkm.model.Research;
import com.insilicogen.gdkm.model.User;
import com.insilicogen.gdkm.service.NgsDataRegistService;
import com.insilicogen.gdkm.service.ResearchNgsDataService;
import com.insilicogen.gdkm.service.ResearchService;

@RestController
public class ResearchNgsDataController extends AbstractController {
	
	static Logger logger = LoggerFactory.getLogger(ResearchOmicsController.class);
	
	@Resource(name="ResearchService")
	ResearchService researchService;

	@Resource(name="ResearchNgsDataService")
	ResearchNgsDataService linkedService;
	
	@Resource(name="NgsDataRegistService")
	NgsDataRegistService registService;
	
	@Resource(name = "messageSource")
	MessageSource messageSource;
	
	@RequestMapping(value={"/research/{research_id}/ngs"}, method=RequestMethod.GET)
	public PageableList<NgsDataRegist> getRelatedList(
			@PathVariable("research_id") Integer researchId,
			@RequestParam Map<String, Object> params,
			HttpSession session) throws Exception {
		
		try {
			int page = getCurrentPage(params);
			int rowSize = getRowSize(params);
			int firstIndex = (page-1) * rowSize;
			
			params.put(Globals.PARAM_FIRST_INDEX, firstIndex);
			params.put(Globals.PARAM_ROW_SIZE, rowSize);
			
			if(session.getAttribute(Globals.LOGIN_USER) == null)
				params.put(Globals.PARAM_OPEN_YN, Globals.STATUS_OPEN_Y);
			
			Research research = researchService.getResearch(researchId);
			List<NgsDataRegist> ngsList = linkedService.getLinkedDataList(research, params);
			int total = linkedService.getLinkedDataCount(research, params);
			
			PageableList<NgsDataRegist> pageableList = new PageableList<NgsDataRegist>();
			pageableList.setTotal(total);
			pageableList.setPage(page);
			pageableList.setRowSize(rowSize);
			pageableList.setList(ngsList);
			
			return pageableList;
		} catch (ResearchException e) {
			throw e;
		} catch(Exception e) {
			throw new ResearchException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value={"/research/{research_id}/ngs"}, method=RequestMethod.POST)
	public NgsDataRegist createRelatedData(
			@PathVariable("research_id") Integer researchId,
			@RequestBody NgsDataRegist data, 
			HttpSession session) throws Exception {
		
		checkAuthorization(session);
		Research research = researchService.getResearch(researchId);
		NgsDataRegist regist = registService.getNgsDataRegist(data.getId());
		
		try {
			return linkedService.appendLinkedData(research, regist);
		} catch (ResearchException e) {
			throw e;
		} catch(Exception e) {
			String dataType = messageSource.getMessage("ENTITY.RESEARCH.ATTACHMENT", null, Locale.US);
			String message = messageSource.getMessage("ERR022", new String[]{ dataType, e.getMessage() }, Locale.US);
			
			throw new ResearchException(message, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value={"/research/{research_id}/ngs/{regist_id}"}, method=RequestMethod.DELETE)
	public NgsDataRegist setOmicsFile(
			@PathVariable("research_id") Integer researchId,
			@PathVariable("regist_id") Integer registId,
			HttpSession session) throws Exception {
		
		User loginUser = checkAuthorization(session);
		Research research = researchService.getResearch(researchId);
		NgsDataRegist regist = registService.getNgsDataRegist(registId);
		
		if(loginUser == null && !Globals.isOpenData(research)) 
			throw new ResearchException("Not permission", HttpStatus.UNAUTHORIZED);
		
		try {
			return linkedService.deleteLinkedData(research, regist);  
		} catch (ResearchException e) {
			throw e;
		} catch(Exception e) {
			String dataType = messageSource.getMessage("ENTITY.RESEARCH.ATTACHMENT", null, Locale.US);
			String message = messageSource.getMessage("ERR022", new String[]{ dataType, e.getMessage() }, Locale.US);
			
			throw new ResearchException(message, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value={"/research/{research_id}/ngs"}, method=RequestMethod.PUT)
	public PageableList<NgsDataRegist> doOmicsFileBatchJob(
			@PathVariable("research_id") Integer researchId,
			@RequestParam Map<String, Object> params,
			@RequestBody List<NgsDataRegist> dataList, 
			HttpSession session) throws Exception {
		
		checkAuthorization(session);
		Research research = researchService.getResearch(researchId);
		
		if(Globals.isOpenData(research)) 
			throw new ResearchException("Public data can not be modified.");
		
		List<NgsDataRegist> processedList = new ArrayList<NgsDataRegist>();
		
		for(NgsDataRegist data : dataList) {
			try { 
				if(isRemoveRequest(params)) {
					processedList.add(linkedService.deleteLinkedData(research, data));
				} else if(isAppendRequest(params)) {
					processedList.add(linkedService.appendLinkedData(research, data));
				}
			} catch(Exception e) {
				logger.error(e.getMessage());
			}
		}
		
		PageableList<NgsDataRegist> pageableList = new PageableList<NgsDataRegist>();
		pageableList.setTotal(dataList.size());
		pageableList.setRowSize(processedList.size());
		pageableList.setList(processedList);
		
		return pageableList;
	}
}
