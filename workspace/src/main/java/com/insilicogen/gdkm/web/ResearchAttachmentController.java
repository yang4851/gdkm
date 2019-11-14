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
import com.insilicogen.gdkm.model.ResearchAttachment;
import com.insilicogen.gdkm.model.User;
import com.insilicogen.gdkm.service.ResearchAttachmentService;
import com.insilicogen.gdkm.service.ResearchService;
import com.insilicogen.gdkm.util.NgsFileUtils;

@RestController
public class ResearchAttachmentController extends AbstractController {

	static Logger logger = LoggerFactory.getLogger(ResearchAttachmentController.class);
	
	@Resource(name="ResearchService")
	ResearchService researchService;
	
	@Resource(name="ResearchAttachmentService")
	ResearchAttachmentService attachmentService;
	
	@Resource(name = "messageSource")
	MessageSource messageSource;
	
	@RequestMapping(value={"/research/{research_id}/attachments"}, method=RequestMethod.GET)
	public PageableList<ResearchAttachment> getAttachmentList(
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
			List<ResearchAttachment> attachmentList = attachmentService.getAttachmentList(research, params);
			int total = attachmentService.getAttachmentCount(research, params);
			
			PageableList<ResearchAttachment> pageableList = new PageableList<ResearchAttachment>();
			pageableList.setTotal(total);
			pageableList.setPage(page);
			pageableList.setRowSize(rowSize);
			pageableList.setList(attachmentList);
			
			return pageableList;
		} catch (ResearchException e) {
			throw e;
		} catch(Exception e) {
			throw new ResearchException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value={"/research/{research_id}/attachments"}, method=RequestMethod.POST)
	public ResearchAttachment createAttachment(
			@PathVariable("research_id") Integer researchId,
			@RequestBody ResearchAttachment attachment, 
			HttpSession session) throws Exception {
		
		User loginUser = checkAuthorization(session);
		Research research = researchService.getResearch(researchId);
		
		attachment.setResearch(research);
		attachment.setRegistUser(loginUser);
		
		try {
			return attachmentService.createAttachment(attachment);
		} catch (ResearchException e) {
			throw e;
		} catch(Exception e) {
			String dataType = messageSource.getMessage("ENTITY.RESEARCH.ATTACHMENT", null, Locale.US);
			String message = messageSource.getMessage("ERR022", new String[]{ dataType, e.getMessage() }, Locale.US);
			
			throw new ResearchException(message, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value={"/research/{research_id}/attachments/{attachment_id}"}, method=RequestMethod.GET)
	public ResearchAttachment getAttachment(
			@PathVariable("research_id") Integer researchId,
			@PathVariable("attachment_id") Integer attachmentId, 
			HttpSession session) throws Exception {
		
		User loginUser = (User)session.getAttribute(Globals.LOGIN_USER);
		Research research = researchService.getResearch(researchId);
		
		if(loginUser == null && !Globals.isOpenData(research)) 
			throw new ResearchException("Not permission", HttpStatus.UNAUTHORIZED);
		
		try {
			return attachmentService.getAttachment(attachmentId);
		} catch (ResearchException e) {
			throw e;
		} catch(Exception e) {
			String dataType = messageSource.getMessage("ENTITY.RESEARCH.ATTACHMENT", null, Locale.US);
			String message = messageSource.getMessage("ERR022", new String[]{ dataType, e.getMessage() }, Locale.US);
			
			throw new ResearchException(message, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = {"/research/{research_id}/attachments/{attachment_id}" }, method=RequestMethod.GET, 
			produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public void downloadAttachment(
			@PathVariable("research_id") Integer researchId,
			@PathVariable("attachment_id") Integer attachmentId, 
			HttpServletRequest request, 
			HttpServletResponse response) throws Exception {
		
		User loginUser = (User)request.getSession().getAttribute(Globals.LOGIN_USER);
		Research research = researchService.getResearch(researchId);
		
		if(loginUser == null && !Globals.isOpenData(research)) 
			throw new ResearchException("Not permission", HttpStatus.UNAUTHORIZED);
		
		try {
			ResearchAttachment attachment = attachmentService.getAttachment(attachmentId);
			File file = NgsFileUtils.getPhysicalFile(attachment);
			if(!file.exists())
				throw new ResearchNotFoundException(attachment.getName() + " not found.");
			
			response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
			response.setContentLengthLong(file.length());
			response.setHeader( "Content-Disposition", "attachment; filename=\"" + file.getName() + "\"" );
			
			response.getOutputStream().write(FileUtils.readFileToByteArray(file));
		} catch(Exception e) {
			throw new ResearchException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value={"/research/{research_id}/attachments/{attachment_id}"}, method=RequestMethod.PUT)
	public ResearchAttachment setAttachment(
			@PathVariable("research_id") Integer researchId,
			@PathVariable("attachment_id") Integer attachmentId,
			@RequestBody ResearchAttachment attachment,
			HttpSession session) throws Exception {
		
		User loginUser = checkAuthorization(session);
		Research research = researchService.getResearch(researchId);
		
		if(loginUser == null && !Globals.isOpenData(research)) 
			throw new ResearchException("Not permission", HttpStatus.UNAUTHORIZED);
		
		try {
			ResearchAttachment source = attachmentService.getAttachment(attachmentId);
			source.setName(attachment.getName());
			source.setType(attachment.getType());
			source.setSize(attachment.getSize());
			source.setDescription(attachment.getDescription());
			
			return attachmentService.changeAttachment(source);
		} catch (ResearchException e) {
			throw e;
		} catch(Exception e) {
			String dataType = messageSource.getMessage("ENTITY.RESEARCH.ATTACHMENT", null, Locale.US);
			String message = messageSource.getMessage("ERR022", new String[]{ dataType, e.getMessage() }, Locale.US);
			
			throw new ResearchException(message, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value={"/research/{research_id}/attachments/{attachment_id}"}, method=RequestMethod.DELETE)
	public ResearchAttachment deleteAttachment(
			@PathVariable("research_id") Integer researchId,
			@PathVariable("attachment_id") Integer attachmentId,
			HttpSession session) throws Exception {
		
		User loginUser = checkAuthorization(session);
		Research research = researchService.getResearch(researchId);
		
		if(loginUser == null && !Globals.isOpenData(research)) 
			throw new ResearchException("Not permission", HttpStatus.UNAUTHORIZED);
		
		try {
			ResearchAttachment attachment = attachmentService.getAttachment(attachmentId);
			return attachmentService.changeAttachment(attachment);
		} catch (ResearchException e) {
			throw e;
		} catch(Exception e) {
			String dataType = messageSource.getMessage("ENTITY.RESEARCH.ATTACHMENT", null, Locale.US);
			String message = messageSource.getMessage("ERR022", new String[]{ dataType, e.getMessage() }, Locale.US);
			
			throw new ResearchException(message, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value={"/research/{research_id}/attachments"}, method=RequestMethod.PUT)
	public PageableList<ResearchAttachment> doAttachmentBatchJob(
			@PathVariable("research_id") Integer researchId,
			@RequestParam Map<String, Object> params,
			@RequestBody List<ResearchAttachment> attachmentList, 
			HttpSession session) throws Exception {
		
		checkAuthorization(session);
		Research research = researchService.getResearch(researchId);
		
		if(Globals.isOpenData(research)) 
			throw new ResearchException("Public data can not be deleted.");
		
		List<ResearchAttachment> processedList = new ArrayList<ResearchAttachment>();
		
		for(ResearchAttachment attachment : attachmentList) {
			try { 
				if(isRemoveRequest(params)) {
					processedList.add(attachmentService.deleteAttachment(attachment));
				}
			} catch(Exception e) {
				logger.error(e.getMessage());
			}
		}
		
		PageableList<ResearchAttachment> pageableList = new PageableList<ResearchAttachment>();
		pageableList.setTotal(attachmentList.size());
		pageableList.setRowSize(processedList.size());
		pageableList.setList(processedList);
		
		return pageableList;
	}
}
