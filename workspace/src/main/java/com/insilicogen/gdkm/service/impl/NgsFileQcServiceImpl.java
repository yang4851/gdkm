package com.insilicogen.gdkm.service.impl;

import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.insilicogen.gdkm.Globals;
import com.insilicogen.gdkm.dao.NgsFileQcResultDAO;
import com.insilicogen.gdkm.dao.NgsFileQcSummaryDAO;
import com.insilicogen.gdkm.exception.NgsDataException;
import com.insilicogen.gdkm.exception.NgsDataNotFoundException;
import com.insilicogen.gdkm.model.NgsDataAchive;
import com.insilicogen.gdkm.model.NgsFileQcResult;
import com.insilicogen.gdkm.model.NgsFileQcSummary;
import com.insilicogen.gdkm.service.NgsFileQcService;

@Service("NgsFileQcService")
public class NgsFileQcServiceImpl implements NgsFileQcService {
	
	static Logger logger = LoggerFactory.getLogger(NgsFileQcServiceImpl.class);
	
	static final Object ResultLock = new Object();
	
	static final Object SummaryLock = new Object();
	
	@Resource(name="messageSource")
	private MessageSource messageSource;
	
	@Resource(name="NgsFileQcResultDAO")
	NgsFileQcResultDAO resultDAO;
	
	@Resource(name="NgsFileQcSummaryDAO")
	NgsFileQcSummaryDAO summaryDAO;
	
	@Override
	public NgsFileQcResult createNgsFileQcResult(NgsFileQcResult result) throws Exception {
		if (result == null) {
			logger.error("QC 결과파일 등록 오류 - result: " + result );
			String args = messageSource.getMessage("ENTITY.QCDATA",null,Locale.US);
			String message = messageSource.getMessage("ERR002", new String[]{args}, Locale.US);
			throw new NgsDataNotFoundException(message);
//			throw new NgsDataException("Failed to create QC result file - Invalid qc-result!");
		}
		
		if(!Globals.isValidModel(result.getAchive())) {
			logger.error("QC 결과파일 등록 오류 - achive: " + result.getAchive());
			String args = messageSource.getMessage("ENTITY.QCDATA",null,Locale.US);
			String message = messageSource.getMessage("ERR002", new String[]{args}, Locale.US);
			throw new NgsDataNotFoundException(message);
//			throw new NgsDataException("Failed to create QC result file - Invalid data file!");
		}
		
		if(!Globals.isValidUser(result.getRegistUser())) {
			logger.error("QC 결과파일 등록 오류 - registUser: " + result.getRegistUser());
			String args = messageSource.getMessage("ENTITY.QCDATA",null,Locale.US);
			String message = messageSource.getMessage("ERR002", new String[]{args}, Locale.US);
			throw new NgsDataNotFoundException(message);
//			throw new NgsDataException("Failed to create QC result file - Invalid user!");
		}
		
		synchronized(ResultLock) {
			if(resultDAO.insertNgsFileQcResult(result) != 1) {
				logger.error("QC 결과파일 등록 실패 - " + result);
				String args = messageSource.getMessage("ENTITY.QCDATA",null,Locale.US);
				String message = messageSource.getMessage("ERR002", new String[]{args}, Locale.US);
				throw new NgsDataNotFoundException(message);
//				throw new NgsDataException("Failed to create QC result file");
			}
		}
		
		return resultDAO.selectNgsFileQcResult(result.getId());
	}

	@Override
	public NgsFileQcResult changeNgsFileQcResult(NgsFileQcResult result) throws Exception {
		if(!Globals.isValidModel(result)) {
			logger.error("QC 결과파일 변경 오류 - result: " + result );
			String args = messageSource.getMessage("ENTITY.QCDATA",null,Locale.US);
			String message = messageSource.getMessage("ERR003", new String[]{args}, Locale.US);
			throw new NgsDataNotFoundException(message);
//			throw new NgsDataException("Failed to change QC result file - Invalid qc-result!");
		}
		
		if(!Globals.isValidModel(result.getAchive())) {
			logger.error("QC 결과파일 변경 오류 - achive: " + result.getAchive());
			String args = messageSource.getMessage("ENTITY.QCDATA",null,Locale.US);
			String message = messageSource.getMessage("ERR003", new String[]{args}, Locale.US);
			throw new NgsDataNotFoundException(message);
//			throw new NgsDataException("Failed to change QC result file - Invalid data file!");
		}
		
		synchronized(ResultLock) {
			if(resultDAO.updateNgsFileQcResult(result) != 1) {
				logger.error("QC 결과파일 변경 실패 - " + result);
				String args = messageSource.getMessage("ENTITY.QCDATA",null,Locale.US);
				String message = messageSource.getMessage("ERR003", new String[]{args}, Locale.US);
				throw new NgsDataNotFoundException(message);
//				throw new NgsDataException("Failed to change QC result file");
			}
		}
		
		return resultDAO.selectNgsFileQcResult(result.getId());
	}

	@Override
	public NgsFileQcResult getNgsFileQcResult(Integer resultId) throws Exception {
		if(resultId == null || resultId < 1) {
			logger.error(String.format("QC 결과파일 호출 오류 (ID=%s)", resultId));
			String args = messageSource.getMessage("ENTITY.QCDATA",null,Locale.US);
			String message = messageSource.getMessage("ERR006", new String[]{args}, Locale.US);
			throw new NgsDataNotFoundException(message);
//			throw new NgsDataException(String.format("Invalid QC result file id(%s)", resultId));
		}
		
		NgsFileQcResult result = resultDAO.selectNgsFileQcResult(resultId);
		if(!Globals.isValidModel(result)) { 
			String args = messageSource.getMessage("ENTITY.QCDATA",null,Locale.US);
			String message = messageSource.getMessage("ERR009", new String[]{args}, Locale.US);
			throw new NgsDataNotFoundException(message);
//			throw new NgsDataNotFoundException(String.format("Could not found QC result file (ID=%s)", resultId));
		}
		
		return result;
	}

	@Override
	public List<NgsFileQcResult> getNgsFileQcResultList(NgsDataAchive achive) throws Exception {
		if(!Globals.isValidModel(achive)) {
			String args = messageSource.getMessage("ENTITY.NGSDATA",null,Locale.US);
			String message = messageSource.getMessage("ERR006", new String[]{args}, Locale.US);
			throw new NgsDataNotFoundException(message);
//			throw new NgsDataException("Invalid data file");
		}
		
		return resultDAO.selectNgsFileQcResultList(achive);
	}

	@Override
	public int getNgsFileQcResultCount(NgsDataAchive achive) throws Exception {
		if(!Globals.isValidModel(achive)) {
			String args = messageSource.getMessage("ENTITY.NGSDATA",null,Locale.US);
			String message = messageSource.getMessage("ERR006", new String[]{args}, Locale.US);
			throw new NgsDataNotFoundException(message);
//			throw new NgsDataException("Invalid data file");
		}
		
		return resultDAO.selectNgsFileQcResultCount(achive);
	}

	@Override
	public int deleteNgsFileQcResult(NgsFileQcResult result) throws Exception {
		if(!Globals.isValidModel(result)) {
			String args = messageSource.getMessage("ENTITY.QCDATA",null,Locale.US);
			String message = messageSource.getMessage("ERR006", new String[]{args}, Locale.US);
			throw new NgsDataNotFoundException(message);
//			throw new NgsDataException("Invalid QC result file");
		}
		
		synchronized(ResultLock) {
			return resultDAO.deleteNgsFileQcResult(result);
		}
	}
	
	@Override
	public int deleteNgsFileQcResult(NgsDataAchive achive) throws Exception {
		if(!Globals.isValidModel(achive)) {
			String args = messageSource.getMessage("ENTITY.NGSDATA",null,Locale.US);
			String message = messageSource.getMessage("ERR006", new String[]{args}, Locale.US);
			throw new NgsDataNotFoundException(message);
//			throw new NgsDataException("Invalid data file");
		}
		
		synchronized(ResultLock) {
			return resultDAO.deleteNgsFileQcResult(achive);
		}
	}

	@Override
	public NgsFileQcSummary createNgsFileQcSummary(NgsFileQcSummary summary) throws Exception {
		if (summary == null) {
			logger.error("QC 요약정보 등록 오류 - summary: " + summary);
			String args = messageSource.getMessage("ENTITY.QC_SUMMARY",null,Locale.US);
			String message = messageSource.getMessage("ERR002", new String[]{args}, Locale.US);
			throw new NgsDataNotFoundException(message);
//			throw new NgsDataException("Failed to create QC summary - Invalid qc-summary!");
		}
		
		if(!Globals.isValidModel(summary.getAchive())) {
			logger.error("QC 요약정보 등록 오류 - achive: " + summary.getAchive());
			String args = messageSource.getMessage("ENTITY.QC_SUMMARY",null,Locale.US);
			String message = messageSource.getMessage("ERR002", new String[]{args}, Locale.US);
			throw new NgsDataNotFoundException(message);
//			throw new NgsDataException("Failed to create QC summary - Invalid data file!");
		}
		
		if(!Globals.isValidUser(summary.getRegistUser())) {
			logger.error("QC 요약정보 등록 오류 - registUser: " + summary.getRegistUser());
			String args = messageSource.getMessage("ENTITY.QC_SUMMARY",null,Locale.US);
			String message = messageSource.getMessage("ERR002", new String[]{args}, Locale.US);
			throw new NgsDataNotFoundException(message);
//			throw new NgsDataException("Failed to create QC summary - Invalid user!");
		}
		
		synchronized(SummaryLock) {
			if(summaryDAO.insertNgsFileQcSummary(summary) != 1) {
				logger.error("QC 요약정보 등록 실패 - " + summary);
				String args = messageSource.getMessage("ENTITY.QC_SUMMARY",null,Locale.US);
				String message = messageSource.getMessage("ERR002", new String[]{args}, Locale.US);
				throw new NgsDataNotFoundException(message);
//				throw new NgsDataException("Failed to create QC summary");
			}
		}
		
		return summaryDAO.selectNgsFileQcSummary(summary.getId());
	}

	@Override
	public NgsFileQcSummary changeNgsFileQcSummary(NgsFileQcSummary summary) throws Exception {
		if(!Globals.isValidModel(summary)) {
			logger.error("QC 요약정보 변경 오류 - summary: " + summary );
			String args = messageSource.getMessage("ENTITY.QC_SUMMARY",null,Locale.US);
			String message = messageSource.getMessage("ERR003", new String[]{args}, Locale.US);
			throw new NgsDataNotFoundException(message);
//			throw new NgsDataException("Failed to change QC summary - Invalid qc-summary!");
		}
		
		if(!Globals.isValidModel(summary.getAchive())) {
			logger.error("QC 요약정보 변경 오류 - achive: " + summary.getAchive());
			String args = messageSource.getMessage("ENTITY.QC_SUMMARY",null,Locale.US);
			String message = messageSource.getMessage("ERR003", new String[]{args}, Locale.US);
			throw new NgsDataNotFoundException(message);
//			throw new NgsDataException("Failed to change QC summary - Invalid data file!");
		}
		
		synchronized(SummaryLock) {
			if(summaryDAO.updateNgsFileQcSummary(summary) != 1) {
				logger.error("QC 요약정보 변경 실패 - " + summary);
				String args = messageSource.getMessage("ENTITY.QC_SUMMARY",null,Locale.US);
				String message = messageSource.getMessage("ERR003", new String[]{args}, Locale.US);
				throw new NgsDataNotFoundException(message);
//				throw new NgsDataException("Failed to change QC summary");
			}
		}
		
		return summaryDAO.selectNgsFileQcSummary(summary.getId());
	}

	@Override
	public NgsFileQcSummary getNgsFileQcSummary(Integer summaryId) throws Exception {
		if(summaryId == null || summaryId < 1) {
			logger.error(String.format("QC 요약정보 호출 오류 (ID=%s)", summaryId));
			String args = messageSource.getMessage("ENTITY.QC_SUMMARY",null,Locale.US);
			String message = messageSource.getMessage("ERR006", new String[]{args}, Locale.US);
			throw new NgsDataNotFoundException(message + " - id : "+summaryId);
//			throw new NgsDataException(String.format("Invalid QC summary id(%s)", summaryId));
		}
		
		NgsFileQcSummary summary = summaryDAO.selectNgsFileQcSummary(summaryId);
		if(!Globals.isValidModel(summary)) { 
			String args = messageSource.getMessage("ENTITY.QC_SUMMARY",null,Locale.US);
			String message = messageSource.getMessage("ERR009", new String[]{args}, Locale.US);
			throw new NgsDataNotFoundException(message + " - id : "+summaryId);
//			throw new NgsDataNotFoundException(String.format("Could not found QC summary (ID=%s)", summaryId));
		}
		
		return summary;
	}

	@Override
	public List<NgsFileQcSummary> getNgsFileQcSummaryList(NgsDataAchive achive) throws Exception {
		if(!Globals.isValidModel(achive)) {
			String args = messageSource.getMessage("ENTITY.NGSDATA",null,Locale.US);
			String message = messageSource.getMessage("ERR006", new String[]{args}, Locale.US);
			throw new NgsDataNotFoundException(message);
//			throw new NgsDataException("Invalid data file");
		}
		
		return summaryDAO.selectNgsFileQcSummaryList(achive);
	}

	@Override
	public int getNgsFileQcSummaryCount(NgsDataAchive achive) throws Exception {
		if(!Globals.isValidModel(achive)) {
			String args = messageSource.getMessage("ENTITY.NGSDATA",null,Locale.US);
			String message = messageSource.getMessage("ERR006", new String[]{args}, Locale.US);
			throw new NgsDataNotFoundException(message);
//			throw new NgsDataException("Invalid data file");
		}
		
		return summaryDAO.selectNgsFileQcSummaryCount(achive);
	}

	@Override
	public int deleteNgsFileQcSummary(NgsFileQcSummary summary) throws Exception {
		if(!Globals.isValidModel(summary)) {
			String args = messageSource.getMessage("ENTITY.QC_SUMMARY",null,Locale.US);
			String message = messageSource.getMessage("ERR006", new String[]{args}, Locale.US);
			throw new NgsDataNotFoundException(message);
//			throw new NgsDataException("Invalid QC summary");
		}
		
		synchronized(SummaryLock) {
			return summaryDAO.deleteNgsFileQcSummary(summary);
		}
	}

	@Override
	public int deleteNgsFileQcSummary(NgsDataAchive achive) throws Exception {
		if(!Globals.isValidModel(achive)) {
			String args = messageSource.getMessage("ENTITY.NGSDATA",null,Locale.US);
			String message = messageSource.getMessage("ERR006", new String[]{args}, Locale.US);
			throw new NgsDataNotFoundException(message);
//			throw new NgsDataException("Invalid data file");
		}
		
		synchronized(SummaryLock) {
			return summaryDAO.deleteNgsFileQcSummary(achive);
		}
	}
}
