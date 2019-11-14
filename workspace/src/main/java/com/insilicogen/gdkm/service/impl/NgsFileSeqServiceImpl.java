package com.insilicogen.gdkm.service.impl;

import java.util.Locale;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.insilicogen.exception.RestTransferException;
import com.insilicogen.gdkm.Globals;
import com.insilicogen.gdkm.dao.NgsFileSeqQuantityDAO;
import com.insilicogen.gdkm.exception.NgsDataException;
import com.insilicogen.gdkm.exception.NgsDataNotFoundException;
import com.insilicogen.gdkm.model.NgsDataAchive;
import com.insilicogen.gdkm.model.NgsFileSeqQuantity;
import com.insilicogen.gdkm.service.NgsFileSeqService;

@Service("NgsFileSeqService")
public class NgsFileSeqServiceImpl implements NgsFileSeqService {

	static Logger logger = LoggerFactory.getLogger(NgsFileSeqServiceImpl.class);
	
	static final Object Lock = new Object();
	
	@Resource(name="messageSource")
	private MessageSource messageSource;
	
	@Resource(name="NgsFileSeqQuantityDAO")
	NgsFileSeqQuantityDAO quantityDAO;
	
	@Override
	public NgsFileSeqQuantity createNgsFileSeqQuantity(NgsFileSeqQuantity quantity) throws Exception {
		if (quantity == null) {
			logger.error("NGS 정량정보 등록 오류 - quantity: " + quantity );
			String args = messageSource.getMessage("ENTITY.SEQ_QUANTITY",null,Locale.US);
			String message = messageSource.getMessage("ERR002", new String[]{args}, Locale.US);
			throw new NgsDataNotFoundException(message, HttpStatus.INTERNAL_SERVER_ERROR);
//			throw new NgsDataException("Failed to create seq-quantity - Invalid seq-quantity!");
		}
		
		if(!Globals.isValidModel(quantity.getAchive())) {
			logger.error("NGS 정량정보 등록 오류 - achive: " + quantity.getAchive());
			String args = messageSource.getMessage("ENTITY.SEQ_QUANTITY",null,Locale.US);
			String message = messageSource.getMessage("ERR002", new String[]{args}, Locale.US);
			throw new NgsDataNotFoundException(message, HttpStatus.INTERNAL_SERVER_ERROR);
//			throw new NgsDataException("Failed to create seq-quantity - Invalid data file!");
		}
		
		if(!Globals.isValidUser(quantity.getRegistUser())) {
			logger.error("NGS 정량정보 등록 오류 - registUser: " + quantity.getRegistUser());
			String args = messageSource.getMessage("ENTITY.SEQ_QUANTITY",null,Locale.US);
			String message = messageSource.getMessage("ERR002", new String[]{args}, Locale.US);
			throw new NgsDataNotFoundException(message, HttpStatus.INTERNAL_SERVER_ERROR);
//			throw new NgsDataException("Failed to create seq-quantity - Invalid user!");
		}
		
		synchronized (Lock) {
			if(quantityDAO.insertNgsFileSeqQuantity(quantity) != 1) {
				logger.error("NGS 정량정보 등록 실패 - " + quantity);
				String args = messageSource.getMessage("ENTITY.SEQ_QUANTITY",null,Locale.US);
				String message = messageSource.getMessage("ERR002", new String[]{args}, Locale.US);
				throw new NgsDataNotFoundException(message, HttpStatus.INTERNAL_SERVER_ERROR);
//				throw new NgsDataException("Failed to create seq-quantity");
			}	
		}
		
		return quantityDAO.selectNgsFileSeqQuantity(quantity.getId());
	}

	@Override
	public NgsFileSeqQuantity changeNgsFileSeqQuantity(NgsFileSeqQuantity quantity) throws Exception {
		if(!Globals.isValidModel(quantity)) {
			logger.error("NGS 정량정보 변경 오류 - quantity: " + quantity );
			String args = messageSource.getMessage("ENTITY.SEQ_QUANTITY",null,Locale.US);
			String message = messageSource.getMessage("ERR003", new String[]{args}, Locale.US);
			throw new NgsDataNotFoundException(message, HttpStatus.INTERNAL_SERVER_ERROR);
//			throw new NgsDataException("Failed to change seq-quantity - Invalid seq-quantity!");
		}
		
		if(!Globals.isValidModel(quantity.getAchive())) {
			logger.error("NGS 정량정보 변경 오류 - achive: " + quantity.getAchive());
			String args = messageSource.getMessage("ENTITY.SEQ_QUANTITY",null,Locale.US);
			String message = messageSource.getMessage("ERR003", new String[]{args}, Locale.US);
			throw new NgsDataNotFoundException(message, HttpStatus.INTERNAL_SERVER_ERROR);
//			throw new NgsDataException("Failed to change seq-quantity - Invalid data file!");
		}
		
		synchronized(Lock) {
			if(quantityDAO.updateNgsFileSeqQuantity(quantity) != 1) {
				logger.error("NGS 정량정보 변경 실패 - " + quantity);
				String args = messageSource.getMessage("ENTITY.SEQ_QUANTITY",null,Locale.US);
				String message = messageSource.getMessage("ERR003", new String[]{args}, Locale.US);
				throw new NgsDataNotFoundException(message, HttpStatus.INTERNAL_SERVER_ERROR);
//				throw new NgsDataException("Failed to change seq-quantity");
			}
		}
		
		return quantityDAO.selectNgsFileSeqQuantity(quantity.getId());
	}
	
	@Override
	public NgsFileSeqQuantity getNgsFileSeqQuantity(Integer quantityId) throws Exception {
		if(quantityId == null || quantityId < 1) {
			logger.error(String.format("NGS 파일정보 호출 오류 (ID=%s)", quantityId));
			String args = messageSource.getMessage("ENTITY.SEQ_QUANTITY",null,Locale.US);
			String message = messageSource.getMessage("ERR006", new String[]{args}, Locale.US);
			throw new NgsDataNotFoundException(message, HttpStatus.INTERNAL_SERVER_ERROR);
//			throw new NgsDataException(String.format("Invalid seq-quantity id(%s)", quantityId));
		}
		
		NgsFileSeqQuantity quantity = quantityDAO.selectNgsFileSeqQuantity(quantityId);
		if(!Globals.isValidModel(quantity)) { 
			String args = messageSource.getMessage("ENTITY.SEQ_QUANTITY",null,Locale.US);
			String message = messageSource.getMessage("ERR009", new String[]{args}, Locale.US);
			throw new NgsDataNotFoundException(message, HttpStatus.INTERNAL_SERVER_ERROR);
//			throw new NgsDataNotFoundException(String.format("Could not found seq-quantity (ID=%s)", quantityId));
		}
		
		return quantity;
	}

	@Override
	public NgsFileSeqQuantity getNgsFileSeqQuantity(NgsDataAchive achive) throws Exception {
		if(!Globals.isValidModel(achive)) {
			String args = messageSource.getMessage("ENTITY.NGSDATA",null,Locale.US);
			String message = messageSource.getMessage("ERR006", new String[]{args}, Locale.US);
			throw new NgsDataNotFoundException(message, HttpStatus.INTERNAL_SERVER_ERROR);
//			throw new NgsDataException("Invalid data file");
		}
		
		NgsFileSeqQuantity quantity = quantityDAO.selectNgsFileSeqQuantity(achive);
		if(!Globals.isValidModel(quantity)) { 
			String args = messageSource.getMessage("ENTITY.SEQ_QUANTITY",null,Locale.US);
			String message = messageSource.getMessage("ERR009", new String[]{args}, Locale.US);
			throw new NgsDataNotFoundException(message, HttpStatus.INTERNAL_SERVER_ERROR);
//			throw new NgsDataNotFoundException(String.format("Could not found seq-quantity (file=%s)", achive.getRegistNo()));
		}
		
		return quantity;
	}

	@Override
	public int deleteNgsFileSeqQuantity(NgsFileSeqQuantity quantity) throws Exception {
		if(!Globals.isValidModel(quantity)) {
			String args = messageSource.getMessage("ENTITY.SEQ_QUANTITY",null,Locale.US);
			String message = messageSource.getMessage("ERR006", new String[]{args}, Locale.US);
			throw new NgsDataNotFoundException(message, HttpStatus.INTERNAL_SERVER_ERROR);
//			throw new NgsDataException("Invalid seq-quantity");
		}
		
		synchronized(Lock) {
			return quantityDAO.deleteNgsFileSeqQuantity(quantity);
		}
	}
	
	@Override
	public int deleteNgsFileSeqQuantity(NgsDataAchive achive) throws Exception {
		if(!Globals.isValidModel(achive)) {
			String args = messageSource.getMessage("ENTITY.SEQ_QUANTITY",null,Locale.US);
			String message = messageSource.getMessage("ERR006", new String[]{args}, Locale.US);
			throw new NgsDataNotFoundException(message, HttpStatus.INTERNAL_SERVER_ERROR);
//			throw new NgsDataException("Invalid data file");
		}
		
		synchronized(Lock) {
			return quantityDAO.deleteNgsFileSeqQuantity(achive);
		}
	}
}
