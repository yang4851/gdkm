package com.insilicogen.gdkm.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.insilicogen.gdkm.Globals;
import com.insilicogen.gdkm.dao.NgsDataAchiveDAO;
import com.insilicogen.gdkm.dao.NgsDataLinkDAO;
import com.insilicogen.gdkm.dao.NgsDataRegistDAO;
import com.insilicogen.gdkm.dao.NgsProcessedDataDAO;
import com.insilicogen.gdkm.dao.NgsRawDataDAO;
import com.insilicogen.gdkm.exception.NgsDataException;
import com.insilicogen.gdkm.exception.NgsDataNotFoundException;
import com.insilicogen.gdkm.exception.ProcessedDataException;
import com.insilicogen.gdkm.exception.RawDataException;
import com.insilicogen.gdkm.model.NgsDataAchive;
import com.insilicogen.gdkm.model.NgsDataLink;
import com.insilicogen.gdkm.model.NgsDataRegist;
import com.insilicogen.gdkm.service.NgsDataAchiveService;
import com.insilicogen.gdkm.service.NgsDataRegistService;
import com.insilicogen.gdkm.util.EgovStringUtil;

import egovframework.rte.fdl.property.EgovPropertyService;

@Service("NgsDataRegistService")
public class NgsDataRegistServiceImpl implements NgsDataRegistService{
	
	static Logger logger = LoggerFactory.getLogger(NgsDataRegistServiceImpl.class);
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	@Resource(name="NgsDataAchiveService")
	NgsDataAchiveService achiveService;
	
	@Resource(name="NgsDataAchiveDAO")
	NgsDataAchiveDAO achiveDAO;
	
	@Resource(name="NgsDataRegistDAO")
	NgsDataRegistDAO registDAO;
	
	@Resource(name="NgsRawDataDAO")
	NgsRawDataDAO rawdataDAO;
	
	@Resource(name="NgsProcessedDataDAO")
	NgsProcessedDataDAO processedDAO;
	
	@Resource(name="NgsDataLinkDAO")
	NgsDataLinkDAO ngsDataLinkDAO;
	
	@Resource(name="NgsDataAchiveDAO")
	NgsDataAchiveDAO ngsDataAchiveDAO;
	
	@Resource(name="messageSource")
	private MessageSource messageSource;
	
    @Resource(name = "propertiesService")
    protected EgovPropertyService propertiesService;

	@Override
	public NgsDataRegist createNgsDataRegist(NgsDataRegist regist) throws Exception {
		String dataType = messageSource.getMessage("ENTITY.NGSDATA", null, Locale.US);
		String message = messageSource.getMessage("ERR002", new String[]{ dataType }, Locale.US);
		if(registDAO.insertNgsDataRegist(regist) == 0) {
			logger.error("NGS 데이터 등록 정보 저장 실패 - " + regist);
			throw new NgsDataException(message, HttpStatus.BAD_REQUEST);
		} 
			
		dataType = messageSource.getMessage("ENTITY." + regist.getDataType().toUpperCase(), null, Locale.US);
		message = messageSource.getMessage("ERR002", new String[]{ dataType }, Locale.US);
		if(Globals.isRawData(regist)){
			regist.getRawData().setRegistId(regist.getId());
			if(rawdataDAO.insertNgsRawData(regist.getRawData()) == 0) {
				logger.error("NGS 원천데이터 생성 실패 - " + regist);
				throw new RawDataException(message, HttpStatus.BAD_REQUEST); 
			}
		}else if(Globals.isProcessedData(regist)){
			regist.getProcessedData().setRegistId(regist.getId());
			
			List<NgsDataRegist> linkedList = regist.getLinkedList();
//			if(linkedList == null || linkedList.size() == 0) {
//				logger.error("NGS 가데이터 생성 실패 - 원시데이터 미등록: " + regist);
//				throw new NgsDataException("Failed to create processed data - linked data is empty.", HttpStatus.BAD_REQUEST);
//			}
//			
//			regist.setTaxonomy(linkedList.get(0).getTaxonomy());
//			regist.setStrain(linkedList.get(0).getStrain());
			
			if(processedDAO.insertNgsProcessedData(regist.getProcessedData()) == 0) {
				logger.error("NGS 가공데이터 생성 실패 - " + regist);
				throw new ProcessedDataException(message, HttpStatus.BAD_REQUEST); 
			}
			
			if(linkedList != null) {
				for(NgsDataRegist rawData : linkedList) {
					NgsDataLink link = new NgsDataLink();
					link.setRelation("processed");
					link.setRawData(rawData);
					link.setProcessedData(regist);
					
					ngsDataLinkDAO.insertNgsDataLink(link);
				}
			}
		} else {
			dataType = messageSource.getMessage("ENTITY.NGSDATA", null, Locale.US);
			message = messageSource.getMessage("ERR006", new String[]{ dataType }, Locale.US);
			throw new NgsDataNotFoundException(message, HttpStatus.NOT_FOUND);
		}
		
		return registDAO.selectNgsDataRegist(regist.getId());
	}

	@Override
	public NgsDataRegist changeNgsDataRegist(NgsDataRegist regist) throws Exception {
		String dataType = messageSource.getMessage("ENTITY." + regist.getDataType().toUpperCase(),null, Locale.US);
		String message = messageSource.getMessage("ERR003", new String[]{ dataType }, Locale.US);
		if(Globals.isRawData(regist)){
			if(rawdataDAO.updateNgsRawData(regist.getRawData()) == 0) {
				logger.error("NGS 원천데이터 변경 실패 - " + regist);
				throw new RawDataException(message, HttpStatus.BAD_REQUEST);
			}
		}else if(Globals.isProcessedData(regist)){
			List<NgsDataRegist> linkedList = regist.getLinkedList();
			
			// 상태정보 변경이 아닌 경우는 연계 데이터 정보를 초기화 시킴
			if(linkedList != null) {
//				if(linkedList.size() == 0) {
//					logger.error("NGS 가데이터 변경 실패 - 원시데이터 미등록: " + regist);
//					throw new NgsDataException("Failed to change processed data - raw data is empty.", HttpStatus.BAD_REQUEST);
//				}
				
				ngsDataLinkDAO.deleteNgsDataLinkOfProcessedData(regist);
				
				for(NgsDataRegist rawData : linkedList) {
					NgsDataLink link = new NgsDataLink();
					link.setRelation("processed");
					link.setRawData(rawData);
					link.setProcessedData(regist);
					ngsDataLinkDAO.insertNgsDataLink(link);
					
					regist.setTaxonomy(rawData.getTaxonomy());
					regist.setStrain(rawData.getStrain());
				}
			}
			
			if(processedDAO.updateNgsProcessedData(regist.getProcessedData()) == 0) {
				logger.error("NGS 가공데이터 변경 실패 - " + regist);
				throw new ProcessedDataException(message, HttpStatus.BAD_REQUEST);
			}
		} else {
			throw new NgsDataNotFoundException(messageSource.getMessage("ERR006", new String[]{ dataType }, Locale.US), HttpStatus.NOT_FOUND);
		}
		
		setRegistDataStatus(regist);
		
		if(registDAO.updateNgsDataRegist(regist) == 0) {
			logger.error("NGS 데이터 등록정보 변경 실패 - " + regist);
			throw new NgsDataException(message, HttpStatus.BAD_REQUEST);
		}
		
		return registDAO.selectNgsDataRegist(regist.getId());
	}
	
	private void setRegistDataStatus(NgsDataRegist regist) {
		try {
			int errorCount = 0; 
			int runningCount = 0;
			int readyCount = 0;
			
			List<NgsDataAchive> achiveList = achiveService.getNgsDataAchiveList(regist);
			for (NgsDataAchive achive : achiveList) {
				if(Globals.STATUS_VERIFY_FAILED.equals(achive.getRegistStatus())) {
					errorCount++;
				} else if(Globals.STATUS_VERIFY_RUNNING.equals(achive.getRegistStatus())) {
					runningCount++;
				} else if(Globals.STATUS_VERIFY_READY.equals(achive.getRegistStatus())) {
					readyCount++;
				}
			}
			
			String registStatus = (errorCount > 0) ? Globals.STATUS_REGIST_ERROR : 
				(runningCount > 0) ? Globals.STATUS_REGIST_VALIDATING : 
					(readyCount > 0) ? Globals.STATUS_REGIST_READY : 
						(achiveList.size() > 0) ? regist.getRegistStatus() : Globals.STATUS_REGIST_READY;
			
			regist.setRegistStatus(registStatus);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	@Override
	public NgsDataRegist getNgsDataRegist(Integer registId) throws Exception {
		if(registId == null || registId < 1) {
			String dataType = messageSource.getMessage("ENTITY.NGSDATA",null, Locale.US);
			String message = messageSource.getMessage("ERR004", new String[]{ dataType }, Locale.US);
			logger.error("NGS 데이터 등록정보 호출 ID 오류 - registId=" + registId );
			throw new NgsDataException(message + registId , HttpStatus.BAD_REQUEST);
		}
		NgsDataRegist regist = registDAO.selectNgsDataRegist(registId);
		if(regist==null){
			String dataType = messageSource.getMessage("ENTITY.NGSDATA", null, Locale.US);
			String message = messageSource.getMessage("ERR006", new String[]{ dataType }, Locale.US);
			throw new NgsDataNotFoundException(message, HttpStatus.NOT_FOUND);
		}
		return regist;
	}
	
	@Override
	public List<NgsDataRegist> getNgsDataRegistList(Map<String, Object> params) throws Exception{
		return registDAO.selectNgsDataRegistList(params);
	}
	
	/**
	 * NGS 등록데이터와 연결된 원시데이터 혹은 가공데이터 목록을 반환 
	 */
	@Override
	public List<NgsDataRegist> getNgsDataLinkedList(NgsDataRegist regist) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		if(Globals.isProcessedData(regist)) {
			params.put("dataType", Globals.REGIST_DATA_RAWDATA);
			params.put("processedRegistId", regist.getId());
		} else {
			params.put("dataType", Globals.REGIST_DATA_PROCESSEDDATA);
			params.put("rawRegistId", regist.getId());
		}
		
		return getNgsDataRegistList(params);
	}

	@Override
	public int getNgsDataRegistCount(Map<String, Object> params) throws Exception{
		return registDAO.selectNgsDataRegistCount(params);
	}
	
	public List<Map<String, String>> getRawdataNode(Map<String, Object> params) throws Exception{
		return registDAO.selectRawdataNode(params);
	}
	
	public List<Map<String, String>> getProcessedNode(Map<String, Object> params) throws Exception{
		return registDAO.selectProcessedNode(params);
	}
	
	@Override
	public int deleteNgsDataRegist(NgsDataRegist regist) throws Exception {
		String dataType = null;
		switch(regist.getRegistStatus()){
			case Globals.STATUS_REGIST_VALIDATING : 
				dataType = Globals.STATUS_REGIST_VALIDATING;
			case Globals.STATUS_REGIST_SUBMIT : 
				dataType = Globals.STATUS_REGIST_SUBMIT;
			case Globals.STATUS_REGIST_SUCCESS : 
				dataType = Globals.STATUS_REGIST_SUCCESS;
		}
		if(dataType!=null){
			dataType = messageSource.getMessage("ENTITY." + regist.getDataType().toUpperCase() , null, Locale.US);
			String message = messageSource.getMessage("ERR008", new String[]{ dataType }, Locale.US);
			throw new NgsDataException(message, HttpStatus.BAD_REQUEST);
		}
		
		List<NgsDataAchive> achiveList = achiveService.getNgsDataAchiveList(regist);
		for(NgsDataAchive achive : achiveList) {
			achiveService.deleteNgsDataAchive(achive);
		}
		
		int rawLinkCnt = ngsDataLinkDAO.deleteNgsDataLinkOfRawData(regist);
		int proLinkCnt = ngsDataLinkDAO.deleteNgsDataLinkOfProcessedData(regist);
		int rawCnt = rawdataDAO.deleteNgsRawData(regist.getId());
		int proCnt = processedDAO.deleteNgsProcessedData(regist.getId());
		
		if((rawCnt + proCnt + rawLinkCnt + proLinkCnt)==0){
			String message = messageSource.getMessage("ERR006", new String[]{ regist.getRegistNo() }, Locale.US);
			throw new NgsDataNotFoundException(message, HttpStatus.NOT_FOUND);
		}
		
		return registDAO.deleteNgsDataRegist(regist.getId());
	}
	
	@Override
	public File downloadRegistDataExcelFile(Map<String, Object> params) throws Exception{
		
		List<NgsDataRegist> list = registDAO.selectNgsDataRegistList(params);
		String[] rawDataTitles = {"Raw data ID","Open status","NCBI taxonomy","Experiment type", "Registration status",
				"Taxonomy","Sample name","BioSample type","Sequencing platform","Strategy","Registration date"};
		String[] processedTitles = {"Annotation ID","Open status","NCBI taxonomy","Registration status",
				"Submitter","Submitting organization","Project","Assembly methods","Reference title","Registration date"};
		
		Workbook wb = new HSSFWorkbook();
		Sheet sheet;
		Cell cell;
		try {
			long start = System.currentTimeMillis();
			String section = params.get("section")==null ? "Integrated" : (String) params.get("section");
			sheet = wb.createSheet(params.get("dataType")+"_"+section);
			
			/* excel style */
			Font headFont = wb.createFont();
			headFont.setFontHeightInPoints((short) 11);
			headFont.setFontName("돋움");
			headFont.setBold(true);
			// 첫번째 로우 셀 스타일 설정
			CellStyle headStyle = wb.createCellStyle();
			headStyle.setFillForegroundColor(IndexedColors.GOLD.index);
			headStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			headStyle.setAlignment(HorizontalAlignment.CENTER);
			headStyle.setFont(headFont);
			headStyle.setBorderBottom(BorderStyle.THIN);
			headStyle.setBorderLeft(BorderStyle.THIN);
			headStyle.setBorderRight(BorderStyle.THIN);
			headStyle.setBorderTop(BorderStyle.THIN);
			
			// 바디 폰트 설정
			Font bodyFont = wb.createFont();
			bodyFont.setFontHeightInPoints((short) 9);
			bodyFont.setFontName("돋움");

			// 바디 스타일 설정
			CellStyle bodyStyle = wb.createCellStyle();
			bodyStyle.setFont(bodyFont);
			bodyStyle.setWrapText(true);
			bodyStyle.setBorderBottom(BorderStyle.THIN);
			bodyStyle.setBorderLeft(BorderStyle.THIN);
			bodyStyle.setBorderRight(BorderStyle.THIN);
			bodyStyle.setBorderTop(BorderStyle.THIN);

			CellStyle bodyStyleAlign = wb.createCellStyle();
			bodyStyleAlign.setFont(bodyFont);
			bodyStyleAlign.setWrapText(true);
			bodyStyleAlign.setBorderBottom(BorderStyle.THIN);
			bodyStyleAlign.setBorderLeft(BorderStyle.THIN);
			bodyStyleAlign.setBorderRight(BorderStyle.THIN);
			bodyStyleAlign.setBorderTop(BorderStyle.THIN);
			bodyStyleAlign.setAlignment(HorizontalAlignment.CENTER);
			
			/* excel input data */
			Row titleRow = sheet.createRow(0);
			System.out.println(params);
			String[] titles = {};
			if(params.get("dataType").equals("rawdata")){
				titles = rawDataTitles;
				for(int i = 0; i < titles.length; i++){
					cell = titleRow.createCell(i);
					cell.setCellStyle(headStyle);
					cell.setCellValue(titles[i]);
				}
				setRawDataList(list, sheet, bodyStyle);
			}else if(params.get("dataType").equals("processed")){
				titles = processedTitles;
				for(int i = 0; i < titles.length; i++){
					cell = titleRow.createCell(i);
					cell.setCellStyle(headStyle);
					cell.setCellValue(titles[i]);
				}
				setProcessedList(list, sheet, bodyStyle);
			}
			
			// 셀 와이드 설정
			for (int i = 0; i < titles.length; i++) {
				sheet.autoSizeColumn(i, true);
			}
			Random random = new Random();
			File file = new File("/file/");
			if(!file.exists()){
				//디렉토리 생성 메서드
				file.mkdirs();
				System.out.println("created directory successfully!");
			}
			
			String filePath = propertiesService.getString("fileUploadPath") +  "gdkm_ngsRawData_"+EgovStringUtil.getTimeStamp()+random.nextInt(100)+".xls";
	        file = new File(filePath);
			wb.write(new FileOutputStream(file));
			wb.close();
			
			long end = System.currentTimeMillis();
			System.out.println("writeHSSFWorkbook : " + (end - start));
			
			return file;
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
		}
		return null;
	}

	private void setRawDataList(List<NgsDataRegist> list, Sheet sheet, CellStyle bodyStyle) {
		Cell cell;

		for (int i = 0; i < list.size(); i++) {
			Row row = sheet.createRow(i+1);
			NgsDataRegist regist = list.get(i);
			cell = row.createCell(0);
			cell.setCellStyle(bodyStyle);
			cell.setCellValue(regist.getRegistNo()==null?"null":regist.getRegistNo());
			cell = row.createCell(1);
			cell.setCellStyle(bodyStyle);
			cell.setCellValue(regist.getOpenYn().equals("Y") ? "Open" : "Close");
			cell = row.createCell(2);
			cell.setCellStyle(bodyStyle);
			if(regist.getTaxonomy()==null){
				cell.setCellValue("-");
			}else{
				cell.setCellValue(regist.getTaxonomy().getName()==null?"-":regist.getTaxonomy().getName());
			}
			cell = row.createCell(3);
			cell.setCellStyle(bodyStyle);
			cell.setCellValue(regist.getRawData().getExprType().getName()==null?"null":regist.getRawData().getExprType().getName());
			cell = row.createCell(4);
			cell.setCellStyle(bodyStyle);
			cell.setCellValue(regist.getRegistStatus()==null?"null":regist.getRegistStatus());
			cell = row.createCell(5);
			cell.setCellStyle(bodyStyle);
			cell.setCellValue(regist.getTaxonomy()==null?0.0:regist.getTaxonomy().getNcbiTaxonId());
			cell = row.createCell(6);
			cell.setCellStyle(bodyStyle);
			cell.setCellValue(regist.getRawData().getSampleName()==null?"null":regist.getRawData().getSampleName());
			cell = row.createCell(7);
			cell.setCellStyle(bodyStyle);
			cell.setCellValue(regist.getRawData().getSampleType()==null?"null":regist.getRawData().getSampleType());
			cell = row.createCell(8);
			cell.setCellStyle(bodyStyle);
			cell.setCellValue(regist.getRawData().getSequencer()==null?"null":regist.getRawData().getSequencer());
			cell = row.createCell(9);
			cell.setCellStyle(bodyStyle);
			cell.setCellValue(regist.getRawData().getStrategy()==null?"null":regist.getRawData().getStrategy());
			cell = row.createCell(10);
			cell.setCellStyle(bodyStyle);
			cell.setCellValue(sdf.format(regist.getRegistDate()==null?"19999999":regist.getRegistDate()));
		}
	}

	private void setProcessedList(List<NgsDataRegist> list, Sheet sheet, CellStyle bodyStyle) {
		Cell cell;
		for (int i = 0; i < list.size(); i++) {
			NgsDataRegist regist = list.get(i);
			Row row = sheet.createRow(i+1);
			cell = row.createCell(0);
			cell.setCellStyle(bodyStyle);
			cell.setCellValue(regist.getRegistNo()==null?"null":regist.getRegistNo());
			cell = row.createCell(1);
			cell.setCellStyle(bodyStyle);
			cell.setCellValue(regist.getOpenYn().equals("Y") ? "Open" : "Close");
			cell = row.createCell(2);
			cell.setCellStyle(bodyStyle);
			if(regist.getTaxonomy()==null){
				cell.setCellValue("-");
			}else{
				cell.setCellValue(regist.getTaxonomy().getName()==null?"-":regist.getTaxonomy().getName());
			}
			cell = row.createCell(3);
			cell.setCellStyle(bodyStyle);
			cell.setCellValue(regist.getRegistStatus()==null?"null":regist.getRegistStatus());
			cell = row.createCell(4);
			cell.setCellStyle(bodyStyle);
			cell.setCellValue(regist.getProcessedData().getSubmitter()==null?"null":regist.getProcessedData().getSubmitter());
			cell = row.createCell(5);
			cell.setCellStyle(bodyStyle);
			cell.setCellValue(regist.getProcessedData().getSubmitOrgan()==null?"null":regist.getProcessedData().getSubmitOrgan());
			cell = row.createCell(6);
			cell.setCellStyle(bodyStyle);
			cell.setCellValue(regist.getProcessedData().getProject()==null?"null":regist.getProcessedData().getProject());
			cell = row.createCell(7);
			cell.setCellStyle(bodyStyle);
			cell.setCellValue(regist.getProcessedData().getAssemblyMethod()==null?"null":regist.getProcessedData().getAssemblyMethod());
			cell = row.createCell(8);
			cell.setCellStyle(bodyStyle);
			cell.setCellValue(regist.getProcessedData().getRefTitle()==null?"null":regist.getProcessedData().getRefTitle());
			cell = row.createCell(9);
			cell.setCellStyle(bodyStyle);
			cell.setCellValue(sdf.format(regist.getRegistDate()==null?"19999999":regist.getRegistDate()));
		}
	}

}
