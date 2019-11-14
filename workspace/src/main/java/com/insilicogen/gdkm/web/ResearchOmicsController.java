package com.insilicogen.gdkm.web;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.insilicogen.gdkm.Globals;
import com.insilicogen.gdkm.exception.ResearchException;
import com.insilicogen.gdkm.exception.ResearchNotFoundException;
import com.insilicogen.gdkm.model.PageableList;
import com.insilicogen.gdkm.model.Research;
import com.insilicogen.gdkm.model.ResearchOmicsFile;
import com.insilicogen.gdkm.model.User;
import com.insilicogen.gdkm.service.ResearchOmicsFileService;
import com.insilicogen.gdkm.service.ResearchService;
import com.insilicogen.gdkm.util.NgsFileUtils;

@RestController
public class ResearchOmicsController extends AbstractController {

	static Logger logger = LoggerFactory.getLogger(ResearchOmicsController.class);
	
	@Resource(name="ResearchService")
	ResearchService researchService;
	
	@Resource(name="ResearchOmicsFileService")
	ResearchOmicsFileService omicsService;
	
	@Resource(name = "messageSource")
	MessageSource messageSource;
	
	@RequestMapping(value={"/research/{research_id}/omics"}, method=RequestMethod.GET)
	public PageableList<ResearchOmicsFile> getOmicsFileList(
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
			List<ResearchOmicsFile> omicsList = omicsService.getOmicsFileList(research, params);
			int total = omicsService.getOmicsFileCount(research, params);
			
			PageableList<ResearchOmicsFile> pageableList = new PageableList<ResearchOmicsFile>();
			pageableList.setTotal(total);
			pageableList.setPage(page);
			pageableList.setRowSize(rowSize);
			pageableList.setList(omicsList);
			
			return pageableList;
		} catch (ResearchException e) {
			throw e;
		} catch(Exception e) {
			throw new ResearchException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value={"/research/{research_id}/omics"}, method=RequestMethod.POST)
	public ResearchOmicsFile createOmicsFile(
			@PathVariable("research_id") Integer researchId,
			@RequestBody ResearchOmicsFile omics, 
			HttpSession session) throws Exception {
		
		User loginUser = checkAuthorization(session);
		Research research = researchService.getResearch(researchId);
		
		omics.setResearch(research);
		omics.setRegistUser(loginUser);
		
		try {
			return omicsService.createOmicsFile(omics);
		} catch (ResearchException e) {
			throw e;
		} catch(Exception e) {
			String dataType = messageSource.getMessage("ENTITY.RESEARCH.ATTACHMENT", null, Locale.US);
			String message = messageSource.getMessage("ERR022", new String[]{ dataType, e.getMessage() }, Locale.US);
			
			throw new ResearchException(message, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value={"/research/{research_id}/omics/{omics_id}"}, method=RequestMethod.GET)
	public ResearchOmicsFile getOmicsFile(
			@PathVariable("research_id") Integer researchId,
			@PathVariable("omics_id") Integer omicsId, 
			HttpSession session) throws Exception {
		
		User loginUser = (User)session.getAttribute(Globals.LOGIN_USER);
		Research research = researchService.getResearch(researchId);
		
		if(loginUser == null && !Globals.isOpenData(research)) 
			throw new ResearchException("Not permission", HttpStatus.UNAUTHORIZED);
		
		try {
			return omicsService.getOmicsFile(omicsId);
		} catch (ResearchException e) {
			throw e;
		} catch(Exception e) {
			String dataType = messageSource.getMessage("ENTITY.RESEARCH.ATTACHMENT", null, Locale.US);
			String message = messageSource.getMessage("ERR022", new String[]{ dataType, e.getMessage() }, Locale.US);
			
			throw new ResearchException(message, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = {"/research/{research_id}/omics/{omics_id}" }, method=RequestMethod.GET, 
			produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public void downloadOmicsFile(
			@PathVariable("research_id") Integer researchId,
			@PathVariable("omics_id") Integer omicsId, 
			HttpServletRequest request, 
			HttpServletResponse response) throws Exception {
		
		User loginUser = (User)request.getSession().getAttribute(Globals.LOGIN_USER);
		Research research = researchService.getResearch(researchId);
		
		if(loginUser == null && !Globals.isOpenData(research)) 
			throw new ResearchException("Not permission", HttpStatus.UNAUTHORIZED);
		
		try {
			ResearchOmicsFile omics = omicsService.getOmicsFile(omicsId);
			File file = NgsFileUtils.getPhysicalFile(omics);
			if(!file.exists())
				throw new ResearchNotFoundException(omics.getName() + " not found.");
			
			response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
			response.setContentLengthLong(file.length());
			response.setHeader( "Content-Disposition", "attachment; filename=\"" + file.getName() + "\"" );
			
			response.getOutputStream().write(FileUtils.readFileToByteArray(file));
		} catch(Exception e) {
			throw new ResearchException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value={"/research/{research_id}/omics/{omics_id}"}, method=RequestMethod.PUT)
	public ResearchOmicsFile setOmicsFile(
			@PathVariable("research_id") Integer researchId,
			@PathVariable("omics_id") Integer omicsId,
			@RequestBody ResearchOmicsFile omics,
			HttpSession session) throws Exception {
		
		User loginUser = checkAuthorization(session);
		Research research = researchService.getResearch(researchId);
		
		if(loginUser == null && !Globals.isOpenData(research)) 
			throw new ResearchException("Not permission", HttpStatus.UNAUTHORIZED);
		
		try {
			ResearchOmicsFile source = omicsService.getOmicsFile(omicsId);
			source.setName(omics.getName());
			source.setCategory(omics.getCategory());
			source.setType(omics.getType());
			source.setSize(omics.getSize());
			source.setDescription(omics.getDescription());
			
			return omicsService.changeOmicsFile(source);
		} catch (ResearchException e) {
			throw e;
		} catch(Exception e) {
			String dataType = messageSource.getMessage("ENTITY.RESEARCH.ATTACHMENT", null, Locale.US);
			String message = messageSource.getMessage("ERR022", new String[]{ dataType, e.getMessage() }, Locale.US);
			
			throw new ResearchException(message, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value={"/research/{research_id}/omics/{omics_id}"}, method=RequestMethod.DELETE)
	public ResearchOmicsFile deleteOmicsFile(
			@PathVariable("research_id") Integer researchId,
			@PathVariable("omics_id") Integer omicsId,
			HttpSession session) throws Exception {
		
		User loginUser = checkAuthorization(session);
		Research research = researchService.getResearch(researchId);
		
		if(loginUser == null && !Globals.isOpenData(research)) 
			throw new ResearchException("Not permission", HttpStatus.UNAUTHORIZED);
		
		try {
			ResearchOmicsFile omics = omicsService.getOmicsFile(omicsId);
			return omicsService.changeOmicsFile(omics);
		} catch (ResearchException e) {
			throw e;
		} catch(Exception e) {
			String dataType = messageSource.getMessage("ENTITY.RESEARCH.ATTACHMENT", null, Locale.US);
			String message = messageSource.getMessage("ERR022", new String[]{ dataType, e.getMessage() }, Locale.US);
			
			throw new ResearchException(message, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value={"/research/{research_id}/omics"}, method=RequestMethod.PUT)
	public PageableList<ResearchOmicsFile> doOmicsFileBatchJob(
			@PathVariable("research_id") Integer researchId,
			@RequestParam Map<String, Object> params,
			@RequestBody List<ResearchOmicsFile> omicsList, 
			HttpSession session) throws Exception {
		
		checkAuthorization(session);
		Research research = researchService.getResearch(researchId);
		
		if(Globals.isOpenData(research)) 
			throw new ResearchException("Public data can not be deleted.");
		
		List<ResearchOmicsFile> processedList = new ArrayList<ResearchOmicsFile>();
		
		for(ResearchOmicsFile omics : omicsList) {
			try { 
				if(isRemoveRequest(params)) {
					processedList.add(omicsService.deleteOmicsFile(omics));
				}
			} catch(Exception e) {
				logger.error(e.getMessage());
			}
		}
		
		PageableList<ResearchOmicsFile> pageableList = new PageableList<ResearchOmicsFile>();
		pageableList.setTotal(omicsList.size());
		pageableList.setRowSize(processedList.size());
		pageableList.setList(processedList);
		
		return pageableList;
	}
}
