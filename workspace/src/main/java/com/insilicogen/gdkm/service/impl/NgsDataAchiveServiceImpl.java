



package com.insilicogen.gdkm.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.insilicogen.gdkm.Globals;
import com.insilicogen.gdkm.dao.NgsDataAchiveDAO;
import com.insilicogen.gdkm.exception.NgsDataException;
import com.insilicogen.gdkm.model.NgsDataAchive;
import com.insilicogen.gdkm.model.NgsDataRegist;
import com.insilicogen.gdkm.service.NgsDataAchiveLogService;
import com.insilicogen.gdkm.service.NgsDataAchiveService;
import com.insilicogen.gdkm.service.NgsDataFeaturesHeaderService;
import com.insilicogen.gdkm.service.NgsDataFeaturesService;
import com.insilicogen.gdkm.service.NgsFileAnnotationService;
import com.insilicogen.gdkm.service.NgsFileQcService;
import com.insilicogen.gdkm.service.NgsFileSeqService;
import com.insilicogen.gdkm.util.NgsFileUtils;

@Service("NgsDataAchiveService")
public class NgsDataAchiveServiceImpl implements NgsDataAchiveService{
	
	static Logger logger = LoggerFactory.getLogger(NgsDataAchiveServiceImpl.class);
	
	static final int BUFF_SIZE = 4096;
	
	@Resource(name="messageSource")
	private MessageSource messageSource;
	
	@Resource(name="NgsDataAchiveDAO")
	NgsDataAchiveDAO achiveDAO;
	
	@Resource(name="NgsFileSeqService")
	NgsFileSeqService seqService;
	
	@Resource(name="NgsFileQcService")
	NgsFileQcService qcService;
	
	@Resource(name="NgsFileAnnotationService")
	NgsFileAnnotationService annotationService;
	
	@Resource(name="NgsDataAchiveLogService")
	NgsDataAchiveLogService logService;
	
	/* GBK 파일 파싱정보 처리를 위한 서비스 선언 */

	@Resource(name = "NgsDataFeaturesService")
	private NgsDataFeaturesService featuresService;
	
	@Resource(name = "NgsDataFeaturesHeaderService")
	private NgsDataFeaturesHeaderService headerService;
	
	@Override
	public NgsDataAchive createNgsDataAchive(NgsDataAchive achive) throws Exception {
		
		if(!Globals.isValidUser(achive.getRegistUser())) {
			String args = messageSource.getMessage("ENTITY.NGSDATA",null,Locale.US);
			String message = messageSource.getMessage("ERR002", new String[]{args}, Locale.US);
			throw new NgsDataException(message+" - Invalid user information!", HttpStatus.BAD_REQUEST);
//			throw new NgsDataException("Failed to create data file - Invalid user information!", HttpStatus.BAD_REQUEST);
		}
		
		if(StringUtils.isBlank(achive.getFileName())) {
			String args = messageSource.getMessage("ENTITY.NGSDATA",null,Locale.US);
			String message = messageSource.getMessage("ERR002", new String[]{args}, Locale.US);
			throw new NgsDataException(message+" - Invalid file name!", HttpStatus.BAD_REQUEST);
//			throw new NgsDataException("Failed to create data file - Invalid file name!", HttpStatus.BAD_REQUEST);
		}
		
		if(achive.getFileSize() == 0) {
			String args = messageSource.getMessage("ENTITY.NGSDATA",null,Locale.US);
			String message = messageSource.getMessage("ERR002", new String[]{args}, Locale.US);
			throw new NgsDataException(message+" - Invalid file size!", HttpStatus.BAD_REQUEST);
//			throw new NgsDataException("Failed to create data file - Invalid file size!", HttpStatus.BAD_REQUEST);
		}
		
		
		achive.setFileType(NgsFileUtils.getFileType(achive.getFileName()));
		if(achiveDAO.insertNgsDataAchive(achive) != 1) {
			logger.error("NGS 데이터 파일 저장 실패  - " + achive);
			String args = messageSource.getMessage("ENTITY.NGSDATA",null,Locale.US);
			String message = messageSource.getMessage("ERR002", new String[]{args}, Locale.US);
			throw new NgsDataException(message, HttpStatus.BAD_REQUEST);
//			throw new NgsDataException("Failed to create data file", HttpStatus.BAD_REQUEST);
		}
		
		return achiveDAO.selectNgsDataAchive(achive.getId());
	}
	
	@Override
	public NgsDataAchive createNgsDataAchive(NgsDataRegist regist, MultipartFile sourceFile) throws Exception {
		NgsDataAchive achive = new NgsDataAchive();
		achive.setDataRegist(regist);
		achive.setFileName(sourceFile.getOriginalFilename());
		achive.setFileType(NgsFileUtils.getFileType(sourceFile.getOriginalFilename()));
		achive.setFileSize(sourceFile.getSize());
		achive.setRegistUser(regist.getRegistUser());
		
		achive = createNgsDataAchive(achive);
		
		File targetFile = NgsFileUtils.getPhysicalFile(achive);
		writeStreamIntoFile(sourceFile, targetFile);
		
		try {
			achive.setChecksum(NgsFileUtils.getMd5Checksum(targetFile));
			achiveDAO.updateNgsDataAchive(achive);
		} catch (IOException e) {
			achive.setVerifiError(e.getMessage());
			achive.setVerifiStatus(Globals.STATUS_VERIFY_FAILED);
			achive.setRegistStatus(Globals.STATUS_REGIST_FAILED);
				
			achiveDAO.updateNgsDataAchive(achive);
		}
		
		return achive;
	}
	
	
	@Override
	public NgsDataAchive createNgsDataAchive(NgsDataAchive achive, File sourceFile) throws Exception {
		achive = createNgsDataAchive(achive);
		
		if(sourceFile.exists()) {
			FileUtils.moveFile(sourceFile, NgsFileUtils.getPhysicalFile(achive));			
		} else {
			achive.setRegistStatus(Globals.STATUS_REGIST_FAILED);
			changeNgsDataAchive(achive);
		}
		
		return achive;
	}
	
	@Override
	public NgsDataAchive changeNgsDataAchive(NgsDataAchive achive) throws Exception {
		synchronized(Lock) {
			if(achiveDAO.updateNgsDataAchive(achive) == 0) {
				logger.error("NGS 데이터 파일 정보 변경 실패 - " + achive);
				String args = messageSource.getMessage("ENTITY.NGSDATAFILE",null,Locale.US);
				String message = messageSource.getMessage("ERR003", new String[]{args}, Locale.US);
				throw new NgsDataException(message, HttpStatus.BAD_REQUEST);
//				throw new NgsDataException("Failed to change data file information.", HttpStatus.BAD_REQUEST);
			}
		}
		
		return achiveDAO.selectNgsDataAchive(achive.getId());
	}
	
	@Override
	public NgsDataAchive getNgsDataAchive(Integer achiveId) throws Exception{
		if(achiveId == null || achiveId < 1) {
			logger.error("NGS 데이터 파일 호출 ID 오류 - achiveId=" + achiveId );
			String args = messageSource.getMessage("ENTITY.NGSDATAFILE",null,Locale.US);
			String message = messageSource.getMessage("ERR006", new String[]{args}, Locale.US);
			throw new NgsDataException(message+" - achiveId = "+achiveId, HttpStatus.BAD_REQUEST);			
//			throw new NgsDataException("Invalid data file id: " + achiveId , HttpStatus.BAD_REQUEST);
		}
		
		return achiveDAO.selectNgsDataAchive(achiveId);
	}

	@Override
	public List<NgsDataAchive> getNgsDataAchiveList(NgsDataRegist regist) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("registId", regist.getId());
		
		return getNgsDataAchiveList(params);
	}
	
	@Override
	public List<NgsDataAchive> getNgsDataAchiveList(Map<String, Object> params) throws Exception{
		return achiveDAO.selectNgsDataAchiveList(params);
	}
	
	@Override
	public int getNgsDataAchiveCount(Map<String, Object> params) throws Exception{
		return achiveDAO.selectNgsDataAchiveCount(params);
	}
	
	@Override
	public List<NgsDataAchive> deleteNgsDataAchive(String registId) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("registId", registId);
		List<NgsDataAchive> achiveList = achiveDAO.selectNgsDataAchiveList(params);
		
		List<NgsDataAchive> successAchives = new ArrayList<NgsDataAchive>();
		List<NgsDataAchive> failAchives = new ArrayList<NgsDataAchive>();
		for(NgsDataAchive achive : achiveList){
			try{
				if(deleteNgsDataAchive(achive) > 0) {
					successAchives.add(achive);
				} else {
					failAchives.add(achive);
				}
			}catch(Exception e){
				e.printStackTrace();
				failAchives.add(achive);
			}
		}
		
		if(failAchives.size()>0){
			String args = messageSource.getMessage("ENTITY.NGSDATAFILE",null,Locale.US);
			String message = messageSource.getMessage("ERR005", new String[]{args}, Locale.US);
			throw new NgsDataException(message, HttpStatus.BAD_REQUEST);		
//			throw new Exception("Failed to delete NgsDataAchiveId - " + failAchives);
		}else{
			return successAchives;
		}
	}

	/**
	 * TODO 삭제요청에 따른 실제 파일 삭제
	 * 
	 *  - FastQC 실행결과 DB 삭제(T_NGS_FILE_QC_RESULT, T_NGS_FILE_QC_SUMMARY)
	 *  - CLC Assembly Cell 실행결과 DB 삭제 (T_NGS_FILE_SEQ_QUANTITY)
	 *  - GBK 파일 분석 정보 DB 삭제 (T_NGS_DATA_EATURES, T_NGS_NGS_DATA_FEATURES_HEADER, T_NGS_DATA_FEATURES_XREF, T_ACHIVE_FEATURES_XREF, T_ACHIVE_HEADER_XREF)
	 *  - 서열 Annotation 정보 DB 삭제 (T_NGS_FILE_ANNOTATION)
	 *  - 등록 파일 로그 정보 DB 삭제 (T_NGS_DATA_ACHIVE_LOG)
	 *  
	 *  실제 파일의 삭제는 완전히 삭제 되지 않아도 DB 정보만 삭제되면 삭제된 것으로 처리 
	 *  - 실제 파일 정보 삭제 : {@link NgsFileUtils#getPhysicalFile(NgsDataAchive)} 
	 *  - QC 실행결과 폴더 삭제 : {@link NgsFileUtils#getFastQcOutputDir(NgsDataAchive)}
	 */
	@Override
	public int deleteNgsDataAchive(NgsDataAchive achive) throws Exception {
		if(!Globals.isValidModel(achive)) {
			String args = messageSource.getMessage("ENTITY.NGSDATA",null,Locale.US);
			String message = messageSource.getMessage("ERR006", new String[]{args}, Locale.US);
			throw new NgsDataException(message, HttpStatus.BAD_REQUEST);		
//			throw new NgsDataException("Invalid data file");
		}
		
		qcService.deleteNgsFileQcResult(achive);
		qcService.deleteNgsFileQcSummary(achive);
		seqService.deleteNgsFileSeqQuantity(achive);
		logService.deleteNgsDataAchiveLog(achive);
		featuresService.deleteFeatures(achive);
		headerService.deleteHeader(achive);
		annotationService.deleteNgsFileAnnotation(achive);
		
		int result = achiveDAO.deleteNgsDataAchive(achive.getId());
		if(result > 0) {
			try {
				File file = NgsFileUtils.getPhysicalFile(achive);
				if(file.exists())
					FileUtils.forceDelete(file);
				
				// QC 실행결과 삭제
				File dir = NgsFileUtils.getFastQcOutputDir(achive);
				if(dir.exists()) {
					FileUtils.forceDelete(dir);
				}
			} catch(Exception e) {
				logger.error(e.getMessage());
			}
		}
		
		return result;
	}

	static void writeStreamIntoFile(MultipartFile sourceFile, File targetFile) throws IOException {
		InputStream stream = null;
		OutputStream bos = null;
		
		try {
			stream = sourceFile.getInputStream();
			bos = new FileOutputStream(targetFile);

			int bytesRead = 0;
			byte[] buffer = new byte[BUFF_SIZE];

			while ((bytesRead = stream.read(buffer, 0, BUFF_SIZE)) != -1) {
				bos.write(buffer, 0, bytesRead);
			}
		} finally {	
			try {
				bos.close();
			} catch (Exception ignore) {
				logger.error("IGNORED: " + ignore.getMessage());
			}
		
			try {
				stream.close();
			} catch (Exception ignore) {
				logger.error("IGNORED: " + ignore.getMessage());
			}	
		}
	}

	@Override
	public List<Map<String, String>> getAchiveNode(Map<String, Object> params) throws Exception {
		return achiveDAO.selectAchiveNode(params);
	}
}
