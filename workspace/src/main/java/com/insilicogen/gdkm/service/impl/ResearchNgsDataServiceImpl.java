package com.insilicogen.gdkm.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.insilicogen.gdkm.dao.NgsDataRegistDAO;
import com.insilicogen.gdkm.dao.ResearchDAO;
import com.insilicogen.gdkm.dao.ResearchNgsDataDAO;
import com.insilicogen.gdkm.exception.ResearchException;
import com.insilicogen.gdkm.model.NgsDataRegist;
import com.insilicogen.gdkm.model.Research;
import com.insilicogen.gdkm.service.ResearchNgsDataService;

@Service("ResearchNgsDataService")
public class ResearchNgsDataServiceImpl implements ResearchNgsDataService {

	static Logger logger = LoggerFactory.getLogger(ResearchNgsDataServiceImpl.class);
	
	@Resource(name="messageSource")
	private MessageSource messageSource;
	
	@Resource(name="ResearchNgsDataDAO")
	ResearchNgsDataDAO linkedDAO;
	
	@Resource(name="ResearchDAO")
	ResearchDAO researchDAO;
	
	@Resource(name="NgsDataRegistDAO")
	NgsDataRegistDAO registDAO;
	
	@Override
	public NgsDataRegist appendLinkedData(Research research, NgsDataRegist data) throws Exception {
		String dataType = messageSource.getMessage("ENTITY.RESEARCH.LINKEDDATA",null, Locale.US);
		String message = messageSource.getMessage("ERR002", new String[]{ dataType }, Locale.US);
		
		if(research == null || research.getId() < 1) 
			throw new ResearchException(message + " - Research data is empty.");
		
		if(data == null || data.getId() < 1) 
			throw new ResearchException(message + " - NGS data is empty.");
		
		synchronized(Lock) {
			if(linkedDAO.insertNgsData(research, data) == 0) {
				logger.error("성과물 연계 데이터 등록 실패 - " + research + "," + data);
				throw new ResearchException(message);
			}
		}
		
		return data;
	}

	@Override
	public boolean hasLinkedData(Research research, NgsDataRegist data) throws Exception {
		String dataType = messageSource.getMessage("ENTITY.RESEARCH.OMICSFILE",null, Locale.US);
		String message = messageSource.getMessage("ERR004", new String[]{ dataType }, Locale.US);
		
		if(research == null || research.getId() < 1) 
			throw new ResearchException(message + " - Research data is empty.");
		
		if(data == null || data.getId() < 1) 
			throw new ResearchException(message + " - NGS data is empty.");
		
		return (linkedDAO.selectNgsDataCount(research, data) > 0);
	}
	
	@Override
	public List<NgsDataRegist> setLinkedData(Research research, List<NgsDataRegist> dataList) throws Exception {
		String dataType = messageSource.getMessage("ENTITY.RESEARCH.LINKEDDATA",null, Locale.US);
		String message = messageSource.getMessage("ERR002", new String[]{ dataType }, Locale.US);
		
		if(research == null || research.getId() < 1) 
			throw new ResearchException(message + " - Research data is empty.");
		
		linkedDAO.deleteNgsData(research);
		
		List<NgsDataRegist> linkedList = new ArrayList<NgsDataRegist>();
		for(NgsDataRegist data : dataList) {
			try {
				appendLinkedData(research, data);
				linkedList.add(data);
			} catch(Exception e) {
				logger.error(e.getMessage());
			}
		}
		
		return linkedList;
	}

	@Override
	public List<NgsDataRegist> getLinkedDataList(Research research) throws Exception {
		if(research == null || research.getId() < 1) {
			String dataType = messageSource.getMessage("ENTITY.RESEARCH.LINKEDDATA",null, Locale.US); 
			String message = messageSource.getMessage("ERR006", new String[]{ dataType }, Locale.US);
			throw new ResearchException(message);
		}
		
		return linkedDAO.selectNgsDataList(research);
	}

	@Override
	public List<NgsDataRegist> getLinkedDataList(Map<String, Object> params) throws Exception {
		return linkedDAO.selectNgsDataList(params);
	}
	
	@Override
	public List<NgsDataRegist> getLinkedDataList(Research research, Map<String, Object> params) throws Exception {
		if(research == null || research.getId() < 1) {
			String dataType = messageSource.getMessage("ENTITY.RESEARCH.LINKEDDATA",null, Locale.US); 
			String message = messageSource.getMessage("ERR006", new String[]{ dataType }, Locale.US);
			throw new ResearchException(message);
		}
		
		if(params == null || params.isEmpty())
			return linkedDAO.selectNgsDataList(research);
		
		params.put("researchId", research.getId());
		return linkedDAO.selectNgsDataList(params);
	}

	@Override
	public int getLinkedDataCount(Research research) throws Exception {
		if(research == null || research.getId() < 1) {
			String dataType = messageSource.getMessage("ENTITY.RESEARCH.LINKEDDATA",null, Locale.US); 
			String message = messageSource.getMessage("ERR006", new String[]{ dataType }, Locale.US);
			throw new ResearchException(message);
		}
		
		return linkedDAO.selectNgsDataCount(research);
	}

	@Override
	public int getLinkedDataCount(Map<String, Object> params) throws Exception {
		return linkedDAO.selectNgsDataCount(params);
	}
	
	@Override
	public int getLinkedDataCount(Research research, Map<String, Object> params) throws Exception {
		if(research == null || research.getId() < 1) {
			String dataType = messageSource.getMessage("ENTITY.RESEARCH.LINKEDDATA",null, Locale.US); 
			String message = messageSource.getMessage("ERR006", new String[]{ dataType }, Locale.US);
			throw new ResearchException(message);
		}
		
		if(params == null || params.isEmpty())
			return linkedDAO.selectNgsDataCount(research);
		
		params.put("researchId", research.getId());
		return linkedDAO.selectNgsDataCount(params);
	}

	@Override
	public List<NgsDataRegist> deleteLinkedData(Research research) throws Exception {
		String dataType = messageSource.getMessage("ENTITY.RESEARCH.LINKEDDATA",null, Locale.US);
		String message = messageSource.getMessage("ERR005", new String[]{ dataType }, Locale.US);
		
		if(research == null || research.getId() < 1) {
			throw new ResearchException(message + " - Research data is empty.");
		}
		
		List<NgsDataRegist> dataList = linkedDAO.selectNgsDataList(research);
		
		synchronized(Lock) {
			if(linkedDAO.deleteNgsData(research) != dataList.size()) { 
				logger.error("성과물 연계 데이터 삭제 실패 - " + research);
				throw new ResearchException(message);
			}
		}
		
		return dataList; 
	}

	@Override
	public int deleteLinkedData(NgsDataRegist regist) throws Exception {
		String dataType = messageSource.getMessage("ENTITY.RESEARCH.LINKEDDATA",null, Locale.US);
		String message = messageSource.getMessage("ERR005", new String[]{ dataType }, Locale.US);
		
		if(regist == null || regist.getId() < 1) {
			throw new ResearchException(message + " - NGS data is empty.");
		}
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("dataId", regist.getId());
		
		synchronized(Lock) {
			return linkedDAO.deleteNgsData(params);
		}
	}

	@Override
	public NgsDataRegist deleteLinkedData(Research research, NgsDataRegist data) throws Exception {
		String dataType = messageSource.getMessage("ENTITY.RESEARCH.LINKEDDATA",null, Locale.US);
		String message = messageSource.getMessage("ERR005", new String[]{ dataType }, Locale.US);
		
		if(research == null || research.getId() < 1) {
			throw new ResearchException(message + " - Research data is empty.");
		}
		
		if(data == null || data.getId() < 1) {
			throw new ResearchException(message + " - NGS data is empty.");
		}
		
		synchronized(Lock) {
			if(linkedDAO.deleteNgsData(research, data) == 0) {
				logger.error("성과물 연계 데이터 삭제 실패 - " + research + ", " + data);
				throw new ResearchException(message);
			}
		}
		
		return data;
	}
}
