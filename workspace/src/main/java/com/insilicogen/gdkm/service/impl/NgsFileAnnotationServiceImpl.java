package com.insilicogen.gdkm.service.impl;

import java.util.Locale;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.insilicogen.gdkm.Globals;
import com.insilicogen.gdkm.dao.NgsFileAnnotationDAO;
import com.insilicogen.gdkm.exception.NgsDataException;
import com.insilicogen.gdkm.exception.NgsDataNotFoundException;
import com.insilicogen.gdkm.model.NgsDataAchive;
import com.insilicogen.gdkm.model.NgsFileAnnotation;
import com.insilicogen.gdkm.service.NgsFileAnnotationService;

@Service("NgsFileAnnotationService")
public class NgsFileAnnotationServiceImpl implements NgsFileAnnotationService {

	static Logger logger = LoggerFactory.getLogger(NgsFileAnnotationServiceImpl.class);
	
	static final Object Lock = new Object();
	
	@Resource(name="messageSource")
	private MessageSource messageSource;
	
	@Resource(name="NgsFileAnnotationDAO")
	NgsFileAnnotationDAO annotationDAO;
	
	@Override
	public NgsFileAnnotation createNgsFileAnnotation(NgsFileAnnotation annotation) throws Exception {
		if (annotation == null) {
			logger.error("NGS 주석정보 등록 오류 - annotation: " + annotation );
			String args = messageSource.getMessage("ENTITY.PROCESSED",null,Locale.US);
			String message = messageSource.getMessage("ERR002", new String[]{args}, Locale.US);
			throw new NgsDataException(message);
//			throw new NgsDataException("Failed to create annotation - Invalid annotation!");
		}
		
		if(!Globals.isValidModel(annotation.getAchive())) {
			logger.error("NGS 주석정보 등록 오류 - achive: " + annotation.getAchive());
			String args = messageSource.getMessage("ENTITY.PROCESSED",null,Locale.US);
			String message = messageSource.getMessage("ERR002", new String[]{args}, Locale.US);
			throw new NgsDataException(message);
//			throw new NgsDataException("Failed to create annotation - Invalid data file!");
		}
		
		if(!Globals.isValidUser(annotation.getRegistUser())) {
			logger.error("NGS 주석정보 등록 오류 - registUser: " + annotation.getRegistUser());
			String args = messageSource.getMessage("ENTITY.PROCESSED",null,Locale.US);
			String message = messageSource.getMessage("ERR002", new String[]{args}, Locale.US);
			throw new NgsDataException(message);
//			throw new NgsDataException("Failed to create annotation - Invalid user!");
		}
		
		synchronized (Lock) {
			if(annotationDAO.insertNgsFileAnnotation(annotation) != 1) {
				logger.error("NGS 주석정보 등록 실패 - " + annotation);
				String args = messageSource.getMessage("ENTITY.PROCESSED",null,Locale.US);
				String message = messageSource.getMessage("ERR002", new String[]{args}, Locale.US);
				throw new NgsDataException(message);
//				throw new NgsDataException("Failed to create annotation");
			}	
		}
		
		return annotationDAO.selectNgsFileAnnotation(annotation.getId());
	}

	@Override
	public NgsFileAnnotation changeNgsFileAnnotation(NgsFileAnnotation annotation) throws Exception {
		if(!Globals.isValidModel(annotation)) {
			logger.error("NGS 주석정보 변경 오류 - annotation: " + annotation );
			String args = messageSource.getMessage("ENTITY.PROCESSED",null,Locale.US);
			String message = messageSource.getMessage("ERR003", new String[]{args}, Locale.US);
			throw new NgsDataException(message);
//			throw new NgsDataException("Failed to change annotation - Invalid annotation!");
		}
		
		if(!Globals.isValidModel(annotation.getAchive())) {
			logger.error("NGS 주석정보 변경 오류 - achive: " + annotation.getAchive());
			String args = messageSource.getMessage("ENTITY.PROCESSED",null,Locale.US);
			String message = messageSource.getMessage("ERR003", new String[]{args}, Locale.US);
			throw new NgsDataException(message);
//			throw new NgsDataException("Failed to change annotation - Invalid data file!");
		}
		
		synchronized(Lock) {
			if(annotationDAO.updateNgsFileAnnotation(annotation) != 1) {
				logger.error("NGS 주석정보 변경 실패 - " + annotation);
				String args = messageSource.getMessage("ENTITY.PROCESSED",null,Locale.US);
				String message = messageSource.getMessage("ERR003", new String[]{args}, Locale.US);
				throw new NgsDataException(message);
//				throw new NgsDataException("Failed to change annotation");
			}
		}
		
		return annotationDAO.selectNgsFileAnnotation(annotation.getId());
	}

	@Override
	public NgsFileAnnotation getNgsFileAnnotation(Integer annotationId) throws Exception {
		if(annotationId == null || annotationId < 1) {
			logger.error(String.format("NGS 주석정보 호출 오류 (ID=%s)", annotationId));
			String args = messageSource.getMessage("ENTITY.PROCESSED",null,Locale.US);
			String message = messageSource.getMessage("ERR006", new String[]{args}, Locale.US);
			throw new NgsDataException(message);
//			throw new NgsDataException(String.format("Invalid annotation id(%s)", annotationId));
		}
		
		NgsFileAnnotation annotation = annotationDAO.selectNgsFileAnnotation(annotationId);
		if(!Globals.isValidModel(annotation)) { 
			String args = messageSource.getMessage("ENTITY.PROCESSED",null,Locale.US);
			String message = messageSource.getMessage("ERR009", new String[]{args}, Locale.US);
			throw new NgsDataException(message);
//			throw new NgsDataNotFoundException(String.format("Could not found annotation (ID=%s)", annotationId));
		}
		
		return annotation;
	}

	@Override
	public NgsFileAnnotation getNgsFileAnnotation(NgsDataAchive achive) throws Exception {
		if(!Globals.isValidModel(achive)) {
			String args = messageSource.getMessage("ENTITY.NGSDATA",null,Locale.US);
			String message = messageSource.getMessage("ERR006", new String[]{args}, Locale.US);
			throw new NgsDataException(message);
//			throw new NgsDataException("Invalid data file");
		}
		
		NgsFileAnnotation annotation = annotationDAO.selectNgsFileAnnotation(achive);
		if(!Globals.isValidModel(annotation)) { 
			String args = messageSource.getMessage("ENTITY.PROCESSED",null,Locale.US);
			String message = messageSource.getMessage("ERR009", new String[]{args}, Locale.US);
			throw new NgsDataException(message);
//			throw new NgsDataNotFoundException(String.format("Could not found annotation (file=%s)", achive.getRegistNo()));
		}
		
		return annotation;
	}

	@Override
	public int deleteNgsFileAnnotation(NgsFileAnnotation annotation) throws Exception {
		if(!Globals.isValidModel(annotation)) {
			String args = messageSource.getMessage("ENTITY.PROCESSED",null,Locale.US);
			String message = messageSource.getMessage("ERR006", new String[]{args}, Locale.US);
			throw new NgsDataException(message);
//			throw new NgsDataException("Invalid annotation");
		}
		
		synchronized(Lock) {
			return annotationDAO.deleteNgsFileAnnotation(annotation);
		}
	}

	@Override
	public int deleteNgsFileAnnotation(NgsDataAchive achive) throws Exception {
		if(!Globals.isValidModel(achive)) {
			String args = messageSource.getMessage("ENTITY.NGSDATA",null,Locale.US);
			String message = messageSource.getMessage("ERR006", new String[]{args}, Locale.US);
			throw new NgsDataException(message);
//			throw new NgsDataException("Invalid data file");
		}
		
		synchronized(Lock) {
			return annotationDAO.deleteNgsFileAnnotation(achive);
		}
	}
}
