package com.insilicogen.gdkm.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.insilicogen.gdkm.dao.ResearchAttachmentDAO;
import com.insilicogen.gdkm.exception.ResearchException;
import com.insilicogen.gdkm.exception.ResearchNotFoundException;
import com.insilicogen.gdkm.model.Research;
import com.insilicogen.gdkm.model.ResearchAttachment;
import com.insilicogen.gdkm.service.ResearchAttachmentService;
import com.insilicogen.gdkm.util.NgsFileUtils;
import com.insilicogen.gdkm.util.UploadFileUtils;

@Service("ResearchAttachmentService")
public class ResearchAttachmentServiceImpl implements ResearchAttachmentService {

	static Logger logger = LoggerFactory.getLogger(ResearchOmicsFileServiceImpl.class);
	
	@Resource(name="messageSource")
	private MessageSource messageSource;
	
	@Resource(name="ResearchAttachmentDAO")
	private ResearchAttachmentDAO attachmentDAO;
	
	@Override
	public ResearchAttachment createAttachment(ResearchAttachment attachment) throws Exception {
		String dataType = messageSource.getMessage("ENTITY.RESEARCH.ATTACHMENT", null, Locale.US);
		String message = messageSource.getMessage("ERR002", new String[]{ dataType }, Locale.US);
		
		if(attachment == null) 
			throw new ResearchException(message + " - data is empty.");
		
		if(StringUtils.isBlank(attachment.getName()))
			throw new ResearchException(message + " - Invalid file name.");
		
		if(StringUtils.isBlank(attachment.getType()))
			throw new ResearchException(message + " - file type is empty.");
		
		if(attachment.getRegistUser() == null || StringUtils.isBlank(attachment.getRegistUser().getUserId()))
			throw new ResearchException(message + " - Invalid owner information");
		
		File srcFile = new File(UploadFileUtils.getTempDir(), attachment.getPath());
		if(!srcFile.exists())
			throw new ResearchNotFoundException("File '" + attachment.getName() + "' does not exist.");
		
		synchronized(Lock) {
			if(attachmentDAO.insertAttachment(attachment) == 0) {
				logger.error("성과물 첨부파일 등록 실패 - " + attachment);
				throw new ResearchException(message);
			}
		}
		
		attachment = attachmentDAO.selectAttachment(attachment.getId());
		File destFile = NgsFileUtils.getPhysicalFile(attachment);
		FileUtils.moveFile(srcFile, destFile);
		
		return attachment;
	}

	@Override
	public ResearchAttachment changeAttachment(ResearchAttachment attachment) throws Exception {
		String dataType = messageSource.getMessage("ENTITY.RESEARCH.ATTACHMENT", null, Locale.US);
		String message = messageSource.getMessage("ERR003", new String[]{ dataType }, Locale.US);
		
		if(attachment == null || attachment.getId() < 1) 
			throw new ResearchException(message + " - data is empty.");
		
		synchronized(Lock) {
			if(attachmentDAO.updateAttachment(attachment) == 0) {
				logger.error("성과물 첨부파일 변경 실패 - " + attachment);
				throw new ResearchException(message);
			}
		}
		
		return attachmentDAO.selectAttachment(attachment.getId());
	}

	@Override
	public ResearchAttachment getAttachment(Integer attachmentId) throws Exception {
		String dataType = messageSource.getMessage("ENTITY.RESEARCH.ATTACHMENT",null, Locale.US);
		
		if(attachmentId == null || attachmentId < 1) {
			String message = messageSource.getMessage("ERR004", new String[]{ dataType }, Locale.US);
			logger.error("성과물 첨부파일 식별번호 오류 - " + attachmentId);
			throw new ResearchNotFoundException(message + " - Invalid ID(" + attachmentId + ")");
		}
		
		ResearchAttachment attachment = attachmentDAO.selectAttachment(attachmentId);
		if(attachment == null) {
			String message = messageSource.getMessage("ERR006", new String[]{ dataType }, Locale.US);
			throw new ResearchNotFoundException(message);
		}
		
		return attachment;
	}

	@Override
	public List<ResearchAttachment> getAttachmentList(Research research) throws Exception {
		if(research == null || research.getId() < 1) {
			String dataType = messageSource.getMessage("ENTITY.RESEARCH",null, Locale.US); 
			String message = messageSource.getMessage("ERR006", new String[]{ dataType }, Locale.US);
			throw new ResearchException(message);
		}
		
		return attachmentDAO.selectAttachmentList(research);
	}

	@Override
	public List<ResearchAttachment> getAttachmentList(Map<String, Object> params) throws Exception {
		return attachmentDAO.selectAttachmentList(params);
	}

	public List<ResearchAttachment> getAttachmentList(Research research, Map<String, Object> params) throws Exception {
		if(research == null || research.getId() < 1) {
			String dataType = messageSource.getMessage("ENTITY.RESEARCH",null, Locale.US); 
			String message = messageSource.getMessage("ERR006", new String[]{ dataType }, Locale.US);
			throw new ResearchException(message);
		}
		
		if(params == null || params.isEmpty()) {
			return attachmentDAO.selectAttachmentList(research);
		} else {
			params.put("researchId", research.getId());
			return attachmentDAO.selectAttachmentList(params);
		}
	}
	
	@Override
	public int getAttachmentCount(Research research) throws Exception {
		if(research == null || research.getId() < 1) {
			String dataType = messageSource.getMessage("ENTITY.RESEARCH",null, Locale.US); 
			String message = messageSource.getMessage("ERR006", new String[]{ dataType }, Locale.US);
			throw new ResearchException(message);
		}
		
		return attachmentDAO.selectAttachmentCount(research);
	}

	@Override
	public int getAttachmentCount(Map<String, Object> params) throws Exception {
		return attachmentDAO.selectAttachmentCount(params);
	}
	
	@Override
	public int getAttachmentCount(Research research, Map<String, Object> params) throws Exception {
		if(research == null || research.getId() < 1) {
			String dataType = messageSource.getMessage("ENTITY.RESEARCH",null, Locale.US); 
			String message = messageSource.getMessage("ERR006", new String[]{ dataType }, Locale.US);
			throw new ResearchException(message);
		}
		
		if(params == null || params.isEmpty()) {
			return attachmentDAO.selectAttachmentCount(research);
		} else {
			params.put("researchId", research.getId());
			return attachmentDAO.selectAttachmentCount(params);
		}
	}

	@Override
	public ResearchAttachment deleteAttachment(ResearchAttachment attachment) throws Exception {
		String dataType = messageSource.getMessage("ENTITY.RESEARCH.OMICSFILE",null, Locale.US);
		String message = messageSource.getMessage("ERR005", new String[]{ dataType }, Locale.US);
		
		if(attachment == null || attachment.getId() < 1) 
			throw new ResearchException(message + " - data is empty.");
		
		synchronized(Lock) {
			if(attachmentDAO.deleteAttachment(attachment) == 0) {
				logger.error("성과물 첨부파일 삭제 실패 - " + attachment);
				throw new ResearchException(message);
			}
		}
		
		try {
			File file = NgsFileUtils.getPhysicalFile(attachment);
			if(file.exists())
				file.delete();
		} catch(Exception e) {
			logger.error("성과물 오믹스 물리 파일 삭제 실패 - " + e.getMessage());
		}
		
		return attachment;
	}

	@Override
	public List<ResearchAttachment> deleteAttachment(Research research) throws Exception {
		List<ResearchAttachment> attachmentList = getAttachmentList(research);
		List<ResearchAttachment> removedList = new ArrayList<ResearchAttachment>();
		
		for(ResearchAttachment attachment : attachmentList) {
			try {
				deleteAttachment(attachment);
				removedList.add(attachment);
			} catch(Exception e) {
				logger.error(e.getMessage());
			}
		}
		
		return removedList;
	}

}
