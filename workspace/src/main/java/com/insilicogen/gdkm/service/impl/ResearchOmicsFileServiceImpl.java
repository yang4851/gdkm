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

import com.insilicogen.gdkm.dao.ResearchOmicsFileDAO;
import com.insilicogen.gdkm.exception.ResearchException;
import com.insilicogen.gdkm.exception.ResearchNotFoundException;
import com.insilicogen.gdkm.model.Research;
import com.insilicogen.gdkm.model.ResearchOmicsFile;
import com.insilicogen.gdkm.service.ResearchOmicsFileService;
import com.insilicogen.gdkm.util.NgsFileUtils;
import com.insilicogen.gdkm.util.UploadFileUtils;

@Service("ResearchOmicsFileService")
public class ResearchOmicsFileServiceImpl implements ResearchOmicsFileService {

	static Logger logger = LoggerFactory.getLogger(ResearchOmicsFileServiceImpl.class);
	
	@Resource(name="messageSource")
	private MessageSource messageSource;
	
	@Resource(name="ResearchOmicsFileDAO")
	private ResearchOmicsFileDAO omicsFileDAO;
	
	@Override
	public ResearchOmicsFile createOmicsFile(ResearchOmicsFile omicsFile) throws Exception {
		String dataType = messageSource.getMessage("ENTITY.RESEARCH.OMICSFILE", null, Locale.US);
		String message = messageSource.getMessage("ERR002", new String[]{ dataType }, Locale.US);
		
		if(omicsFile == null) 
			throw new ResearchException(message + " - data is empty.");
		
		if(StringUtils.isBlank(omicsFile.getName()))
			throw new ResearchException(message + " - Invalid file name.");
		
		if(StringUtils.isBlank(omicsFile.getType()))
			throw new ResearchException(message + " - file type is empty.");
		
		if(omicsFile.getCategory() == null || StringUtils.isBlank(omicsFile.getCategory().getCode()))
			throw new ResearchException(message + " - Invalid omics category.");
		
		if(omicsFile.getRegistUser() == null || StringUtils.isBlank(omicsFile.getRegistUser().getUserId()))
			throw new ResearchException(message + " - Invalid owner information");
		
		File srcFile = new File(UploadFileUtils.getTempDir(), omicsFile.getPath());
		if(!srcFile.exists())
			throw new ResearchNotFoundException("File '" + omicsFile.getName() + "' does not exist.");
		
		synchronized(Lock) {
			if(omicsFileDAO.insertOmicsFile(omicsFile) == 0) {
				logger.error("성과물 오믹스 파일 등록 실패 - " + omicsFile);
				throw new ResearchException(message);
			} 
		}
		
		omicsFile = omicsFileDAO.selectOmicsFile(omicsFile.getId());
		File destFile = NgsFileUtils.getPhysicalFile(omicsFile);
		FileUtils.moveFile(srcFile, destFile);
		
		return omicsFile;
	}

	@Override
	public ResearchOmicsFile changeOmicsFile(ResearchOmicsFile omicsFile) throws Exception {
		String dataType = messageSource.getMessage("ENTITY.RESEARCH.OMICSFILE", null, Locale.US);
		String message = messageSource.getMessage("ERR003", new String[]{ dataType }, Locale.US);
		
		if(omicsFile == null || omicsFile.getId() < 1) 
			throw new ResearchException(message + " - data is empty.");
		
		synchronized(Lock) {
			if(omicsFileDAO.updateOmicsFile(omicsFile) == 0) {
				logger.error("성과물 오믹스 파일 변경 실패 - " + omicsFile);
				throw new ResearchException(message);
			}	
		}
		
		return omicsFileDAO.selectOmicsFile(omicsFile.getId());
	}

	@Override
	public ResearchOmicsFile getOmicsFile(Integer omicsFileId) throws Exception {
		String dataType = messageSource.getMessage("ENTITY.RESEARCH.OMICSFILE",null, Locale.US);
		
		if(omicsFileId == null || omicsFileId < 1) {
			String message = messageSource.getMessage("ERR004", new String[]{ dataType }, Locale.US);
			logger.error("성과물 오믹스 파일 식별번호 오류 - " + omicsFileId);
			throw new ResearchNotFoundException(message + " - Invalid ID(" + omicsFileId + ")");
		}
		
		ResearchOmicsFile omicsFile = omicsFileDAO.selectOmicsFile(omicsFileId);
		if(omicsFile == null) {
			String message = messageSource.getMessage("ERR006", new String[]{ dataType }, Locale.US);
			throw new ResearchNotFoundException(message);
		}
		
		return omicsFile;
	}

	@Override
	public List<ResearchOmicsFile> getOmicsFileList(Research research) throws Exception {
		if(research == null || research.getId() < 1) {
			String dataType = messageSource.getMessage("ENTITY.RESEARCH",null, Locale.US); 
			String message = messageSource.getMessage("ERR006", new String[]{ dataType }, Locale.US);
			throw new ResearchException(message);
		}
		
		return omicsFileDAO.selectOmicsFileList(research);
	}

	@Override
	public List<ResearchOmicsFile> getOmicsFileList(Map<String, Object> params) throws Exception {
		return omicsFileDAO.selectOmicsFileList(params);
	}
	
	@Override
	public List<ResearchOmicsFile> getOmicsFileList(Research research, Map<String, Object> params) throws Exception {
		if(research == null || research.getId() < 1) {
			String dataType = messageSource.getMessage("ENTITY.RESEARCH",null, Locale.US); 
			String message = messageSource.getMessage("ERR006", new String[]{ dataType }, Locale.US);
			throw new ResearchException(message);
		}
		
		if(params == null || params.isEmpty())
			return omicsFileDAO.selectOmicsFileList(research);
		
		params.put("researchId", research.getId());
		return omicsFileDAO.selectOmicsFileList(params);
	}

	@Override
	public int getOmicsFileCount(Research research) throws Exception {
		if(research == null || research.getId() < 1) {
			String dataType = messageSource.getMessage("ENTITY.RESEARCH",null, Locale.US); 
			String message = messageSource.getMessage("ERR006", new String[]{ dataType }, Locale.US);
			throw new ResearchException(message);
		}
		
		return omicsFileDAO.selectOmicsFileCount(research);
	}

	@Override
	public int getOmicsFileCount(Map<String, Object> params) throws Exception {
		return omicsFileDAO.selectOmicsFileCount(params);
	}

	@Override
	public int getOmicsFileCount(Research research, Map<String, Object> params) throws Exception {
		if(research == null || research.getId() < 1) {
			String dataType = messageSource.getMessage("ENTITY.RESEARCH",null, Locale.US); 
			String message = messageSource.getMessage("ERR006", new String[]{ dataType }, Locale.US);
			throw new ResearchException(message);
		}
		
		if(params == null || params.isEmpty())
			return omicsFileDAO.selectOmicsFileCount(research);
		
		params.put("researchId", research.getId());
		return omicsFileDAO.selectOmicsFileCount(params);
	}
	
	@Override
	public ResearchOmicsFile deleteOmicsFile(ResearchOmicsFile omicsFile) throws Exception {
		String dataType = messageSource.getMessage("ENTITY.RESEARCH.OMICSFILE",null, Locale.US);
		String message = messageSource.getMessage("ERR005", new String[]{ dataType }, Locale.US);
		
		if(omicsFile == null || omicsFile.getId() < 1) {
			throw new ResearchException(message + " - data is empty.");
		}
		
		synchronized(Lock) {
			if(omicsFileDAO.deleteOmicsFile(omicsFile) == 0) {
				logger.error("성과물 오믹스 파일 삭제 실패 - " + omicsFile);
				throw new ResearchException(message);
			}
		}
		
		try {
			File file = NgsFileUtils.getPhysicalFile(omicsFile);
			if(file.exists())
				file.delete();
		} catch(Exception e) {
			logger.error("성과물 오믹스 물리 파일 삭제 실패 - " + e.getMessage());
		}
		
		return omicsFile;
	}

	@Override
	public List<ResearchOmicsFile> deleteOmicsFile(Research research) throws Exception {
		List<ResearchOmicsFile> fileList = getOmicsFileList(research);
		List<ResearchOmicsFile> removedList = new ArrayList<ResearchOmicsFile>(); 
		
		for(ResearchOmicsFile omicsFile : fileList) {
			try {
				deleteOmicsFile(omicsFile);
				removedList.add(omicsFile);
			} catch(Exception e) {
				logger.error(e.getMessage());
			}
		}
		
		return removedList;
	}
}
