package com.insilicogen.gdkm.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.insilicogen.gdkm.Globals;
import com.insilicogen.gdkm.dao.NgsDataAchiveLogDAO;
import com.insilicogen.gdkm.exception.NgsDataException;
import com.insilicogen.gdkm.model.NgsDataAchive;
import com.insilicogen.gdkm.model.NgsDataAchiveLog;
import com.insilicogen.gdkm.service.NgsDataAchiveLogService;

@Service("NgsDataAchiveLogService")
public class NgsDataAchvieLogServiceImpl implements NgsDataAchiveLogService {

	static Logger logger = LoggerFactory.getLogger(NgsDataAchvieLogServiceImpl.class);
	
	static final Object Lock = new Object();
	
	@Resource(name="messageSource")
	private MessageSource messageSource;
	
	@Resource(name="NgsDataAchiveLogDAO")
	NgsDataAchiveLogDAO achiveLogDAO;
	
	@Override
	public NgsDataAchiveLog createNgsDataAchiveLog(NgsDataAchiveLog log) throws Exception {
		if (log == null) {
			logger.error("데이터 처리 로그 등록 오류 - log: " + log );
			String args = messageSource.getMessage("ENTITY.LOG.ACHIVE",null,Locale.US);
			String message = messageSource.getMessage("ERR002", new String[]{args}, Locale.US);
			throw new NgsDataException(message+" - null");
//			throw new NgsDataException("Failed to create process log - null");
		}
		
		if(log.getAchiveId() < 1) {
			logger.error("데이터 처리 로그 등록 오류 - achiveId: " + log.getAchiveId());
			String args = messageSource.getMessage("ENTITY.LOG.ACHIVE",null,Locale.US);
			String message = messageSource.getMessage("ERR002", new String[]{args}, Locale.US);
			throw new NgsDataException(message+" - Invalid data file!");
//			throw new NgsDataException("Failed to create process log - Invalid data file!");
		}
		
		if(!Globals.isValidUser(log.getRegistUser())) {
			logger.error("데이터 처리 로그 등록 오류 - registUser: " + log.getRegistUser());
			String args = messageSource.getMessage("ENTITY.LOG.ACHIVE",null,Locale.US);
			String message = messageSource.getMessage("ERR002", new String[]{args}, Locale.US);
			throw new NgsDataException(message+" - Invalid user!");
//			throw new NgsDataException("Failed to create process log - Invalid user!");
		}
		
		synchronized(Lock) {
			if(achiveLogDAO.insertNgsDataAchiveLog(log) != 1) {
				logger.error("데이터 처리 로그 등록 실패 - " + log);
				String args = messageSource.getMessage("ENTITY.LOG.ACHIVE",null,Locale.US);
				String message = messageSource.getMessage("ERR002", new String[]{args}, Locale.US);
				throw new NgsDataException(message);
//				throw new NgsDataException("Failed to change process log");
			}
		}
		
		return achiveLogDAO.selectNgsDataAchiveLog(log.getId());
	}

	@Override
	public NgsDataAchiveLog getNgsDataAchiveLog(Integer logId) throws Exception {
		if(logId == null || logId < 1) {
			logger.error(String.format("데이터 처리 로그 호출 오류 (ID=%s)", logId));
			String args = messageSource.getMessage("ENTITY.LOG.ACHIVE",null,Locale.US);
			String message = messageSource.getMessage("ERR004", new String[]{args}, Locale.US);
			throw new NgsDataException(message);
//			throw new NgsDataException(String.format("Invalid process log id(%s)", logId));
		}
		
		return null;
	}

	@Override
	public List<NgsDataAchiveLog> getNgsDataAchiveLogList(NgsDataAchive achive) {
		try {
			if(!Globals.isValidModel(achive)){
				String args = messageSource.getMessage("ENTITY.NGSDATA",null,Locale.US);
				String message = messageSource.getMessage("ERR006", new String[]{args}, Locale.US);
				throw new NgsDataException(message);
//				throw new NgsDataException("Invalid data file");
			}
			
			return achiveLogDAO.selectNgsDataAchiveLogList(achive);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return Collections.emptyList();
	}

	@Override
	public int getNgsDataAchiveLogCount(NgsDataAchive achive) {
		try {
			if(!Globals.isValidModel(achive)){
				String args = messageSource.getMessage("ENTITY.NGSDATA",null,Locale.US);
				String message = messageSource.getMessage("ERR006", new String[]{args}, Locale.US);
				throw new NgsDataException(message);
//				throw new NgsDataException("Invalid data file");
			}
			
			return achiveLogDAO.selectNgsDataAchiveLogCount(achive);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return 0;
	}

	@Override
	public int deleteNgsDataAchvieLog(NgsDataAchiveLog log) throws Exception {
		if(!Globals.isValidModel(log)) {
			String args = messageSource.getMessage("ENTITY.LOG.ACHIVE",null,Locale.US);
			String message = messageSource.getMessage("ERR006", new String[]{args}, Locale.US);
			throw new NgsDataException(message);
//			throw new NgsDataException("Invalid process log");
		}
		
		return achiveLogDAO.deleteNgsDataAchiveLog(log);
	}

	@Override
	public int deleteNgsDataAchiveLog(NgsDataAchive achive) throws Exception {
		if(!Globals.isValidModel(achive)) {
			String args = messageSource.getMessage("ENTITY.NGSDATA",null,Locale.US);
			String message = messageSource.getMessage("ERR006", new String[]{args}, Locale.US);
			throw new NgsDataException(message);
//			throw new NgsDataException("Invalid data file");
		}
		
		synchronized(Lock) {
			return achiveLogDAO.deleteNgsDataAchiveLog(achive);
		}
	}


}
