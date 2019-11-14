package com.insilicogen.gdkm.service.impl;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.insilicogen.gdkm.Globals;
import com.insilicogen.gdkm.dao.ResearchAttachmentDAO;
import com.insilicogen.gdkm.dao.ResearchDAO;
import com.insilicogen.gdkm.dao.ResearchNgsDataDAO;
import com.insilicogen.gdkm.dao.ResearchOmicsFileDAO;
import com.insilicogen.gdkm.exception.ResearchException;
import com.insilicogen.gdkm.exception.ResearchNotFoundException;
import com.insilicogen.gdkm.model.NgsDataRegist;
import com.insilicogen.gdkm.model.Research;
import com.insilicogen.gdkm.service.ResearchService;

@Service("ResearchService")
public class ResearchServiceImpl implements ResearchService {

	static Logger logger = LoggerFactory.getLogger(ResearchServiceImpl.class);

	@Resource(name="ResearchDAO")
	ResearchDAO researchDAO;
	
	@Resource(name="ResearchAttachmentDAO")
	ResearchAttachmentDAO attachmentDAO;
	
	@Resource(name="ResearchOmicsFileDAO")
	ResearchOmicsFileDAO omicsFileDAO;
	
	@Resource(name="ResearchNgsDataDAO")
	ResearchNgsDataDAO relationDAO;
	
	@Resource(name="messageSource")
	private MessageSource messageSource;
	
	@Override
	public Research createResearch(Research research) throws Exception {
		
		String dataType = messageSource.getMessage("ENTITY.RESEARCH", null, Locale.US);
		String message = messageSource.getMessage("ERR002", new String[]{ dataType }, Locale.US);
		
		if(research == null) 
			throw new ResearchException(message + " - data is empty.");
		
		if(StringUtils.isBlank(research.getTitle()))
			throw new ResearchException(message + " - Invalid title");
		
		if(StringUtils.isBlank(research.getSubmitter()))
			throw new ResearchException(message + " - Invalid submitter");
		
		if(StringUtils.isBlank(research.getSubmitOrgan()))
			throw new ResearchException(message + " - Invalid submit organization");
		
		if(research.getRegistUser() == null || StringUtils.isBlank(research.getRegistUser().getUserId()))
			throw new ResearchException(message + " - Invalid owner information");
		
		research.setRegistStatus(Globals.STATUS_REGIST_READY);
		
		synchronized(Lock) {
			if(researchDAO.insertResearch(research) == 0) {
				logger.error("성과물 데이터 등록 실패 - " + research);
				throw new ResearchException(message);
			}
		}
		
		return researchDAO.selectResearch(research.getId());
	}

	@Override
	public Research changeResearch(Research research) throws Exception {
		String dataType = messageSource.getMessage("ENTITY.RESEARCH", null, Locale.US);
		String message = messageSource.getMessage("ERR003", new String[]{ dataType }, Locale.US);
		
		if(research == null || research.getId() < 1) 
			throw new ResearchException(message + " - data is empty.");
		
		synchronized(Lock) {
			if(researchDAO.updateResearch(research) == 0) {
				logger.error("성과물 데이터 변경 실패 - " + research);
				throw new ResearchException(message);
			}
		}
		
		return researchDAO.selectResearch(research.getId());
	}

	@Override
	public Research getResearch(Integer researchId) throws Exception {
		String dataType = messageSource.getMessage("ENTITY.RESEARCH",null, Locale.US);
		
		if(researchId == null || researchId < 1) {
			String message = messageSource.getMessage("ERR004", new String[]{ dataType }, Locale.US);
			
			logger.error("성과물 데이터 등록번호 오류 - " + researchId);
			throw new ResearchNotFoundException(message + " - Invalid ID(" + researchId + ")");
		}
		
		Research research = researchDAO.selectResearch(researchId);
		if(research == null) {
			String message = messageSource.getMessage("ERR006", new String[]{ dataType }, Locale.US);
			throw new ResearchNotFoundException(message);
		}
			
		return research;
	}

	@Override
	public List<Research> getResearchList(NgsDataRegist regist) throws Exception {
		if(regist == null || regist.getId() < 1) {
			String message = messageSource.getMessage("ERR006", new String[]{ "NGS data" }, Locale.US);
			throw new ResearchException(message);
		}
		
		return researchDAO.selectResearchList(regist);
	}

	@Override
	public List<Research> getResearchList(Map<String, Object> params) throws Exception {
		return researchDAO.selectResearchList(params);
	}

	@Override
	public int getResearchCount(NgsDataRegist regist) throws Exception {
		if(regist == null || regist.getId() < 1) {
			String message = messageSource.getMessage("ERR006", new String[]{ "NGS data" }, Locale.US);
			throw new ResearchException(message);
		}
		
		return researchDAO.selectResearchCount(regist);
	}

	@Override
	public int getResearchCount(Map<String, Object> params) throws Exception {
		synchronized(Lock) {
			return researchDAO.selectResearchCount(params);
		}
	}

	@Override
	public Research deleteResearch(Research research) throws Exception {
		String dataType = messageSource.getMessage("ENTITY.RESEARCH",null, Locale.US);
		String message = messageSource.getMessage("ERR005", new String[]{ dataType }, Locale.US);
		
		if(research == null || research.getId() < 1) {
			throw new ResearchException(message + " - data is empty.");
		}
		
		synchronized(Lock) {
			// TODO 연관 데이터 삭제가 우선되어야 함.
			if(researchDAO.deleteResearch(research) == 0) {
				throw new ResearchException(message);
			}
		}
		
		return research;
	}

}
