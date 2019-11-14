package com.insilicogen.gdkm.web;

import java.io.File;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.insilicogen.gdkm.Globals;
import com.insilicogen.gdkm.exception.NgsDataException;
import com.insilicogen.gdkm.model.NgsDataAchive;
import com.insilicogen.gdkm.model.NgsDataFeatures;
import com.insilicogen.gdkm.model.NgsDataFeaturesHeader;
import com.insilicogen.gdkm.model.PageableList;
import com.insilicogen.gdkm.service.NgsDataAchiveService;
import com.insilicogen.gdkm.service.NgsDataFeaturesHeaderService;
import com.insilicogen.gdkm.service.NgsDataFeaturesService;
import com.insilicogen.gdkm.service.NgsFileProcessService;
import com.insilicogen.gdkm.util.NgsFileUtils;

@RestController
public class NgsDataFeaturesController extends AbstractController {
	
	static Logger logger = LoggerFactory.getLogger(NgsDataFeaturesController.class);
	
	static int jbrowseSeq = 0;
	
	@Resource(name="NgsDataAchiveService")
	NgsDataAchiveService achiveService;
	
	@Resource(name="NgsDataFeaturesService")
	NgsDataFeaturesService featuresService;
	
	@Resource(name="NgsDataFeaturesHeaderService")
	NgsDataFeaturesHeaderService headerService;
	
	@Resource(name="NgsFileProcessService")
	NgsFileProcessService processService;
	
	@Resource(name = "messageSource")
	private MessageSource messageSource;
	/**
	 * 유전체 구성 특성정보 목록 호출 
	 * @param title
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/achieves/{achiveId}/features", method = RequestMethod.GET)
	public PageableList<NgsDataFeatures> getFeature(@PathVariable("achiveId") Integer achiveId, 
			@RequestParam Map<String, Object> params) throws Exception {
		
		try {
			NgsDataAchive achive = achiveService.getNgsDataAchive(achiveId);
			if(achive == null) {
				String args = messageSource.getMessage("ENTITY.NGSDATAFILE",null,Locale.US);
				String message = messageSource.getMessage("ERR006", new String[]{args}, Locale.US);
				throw new NgsDataException(message, HttpStatus.NOT_FOUND);
			}
			
			int page = getCurrentPage(params.get(Globals.PARAM_PAGE));
			int rowSize = getRowSize(params.get(Globals.PARAM_ROW_SIZE));
			int firstIndex = (page-1) * rowSize;
			
			params.put(Globals.PARAM_FIRST_INDEX, firstIndex);
			params.put(Globals.PARAM_ROW_SIZE, rowSize);
			
			List<NgsDataFeatures> featureList = featuresService.getFeaturesList(params);
			int totalCount = featuresService.getFeaturesCount(params);
			
			PageableList<NgsDataFeatures> pageableList = new PageableList<NgsDataFeatures>();
			pageableList.setPage(page);
			pageableList.setRowSize(rowSize);
			pageableList.setList(featureList);
			pageableList.setTotal(totalCount);
			
			return pageableList;
		} catch (NgsDataException e) {
			throw e;
		} catch (Exception e) {
			String args = messageSource.getMessage("ENTITY.NGSDATAFILE",null,Locale.US);
			String message = messageSource.getMessage("ERR004", new String[]{args}, Locale.US);
			throw new NgsDataException(message, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/achieves/{achiveId}/feature-header", method = RequestMethod.GET)
	public List<NgsDataFeaturesHeader> getFeatureHeader(@PathVariable("achiveId") Integer achiveId) throws Exception {
		
		try {
			NgsDataAchive achive = achiveService.getNgsDataAchive(achiveId);
			if(achive == null) {
				String args = messageSource.getMessage("ENTITY.NGSDATA",null,Locale.US);
				String message = messageSource.getMessage("ERR006", new String[]{args}, Locale.US);
				throw new NgsDataException(message, HttpStatus.NOT_FOUND);
			}
			return headerService.selectOneHeader(achive);
		} catch (NgsDataException e) {
			throw e;
		} catch (Exception e) {
			String args = messageSource.getMessage("ENTITY.NGSDATAFILE",null,Locale.US);
			String message = messageSource.getMessage("ERR004", new String[]{args}, Locale.US);
			throw new NgsDataException(message, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/achieves/{achiveId}/gbk", method = RequestMethod.GET)
	public String getGenebankFileAsText(@PathVariable("achiveId") Integer achiveId, 
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		try{
			NgsDataAchive achive = achiveService.getNgsDataAchive(achiveId);
			if(achive == null) {
				String args = messageSource.getMessage("ENTITY.NGSDATA",null,Locale.US);
				String message = messageSource.getMessage("ERR006", new String[]{args}, Locale.US);
				throw new NgsDataException(message, HttpStatus.NOT_FOUND);
			}
			
			File file = NgsFileUtils.getPhysicalFile(achive);
			if(!file.exists() || !file.isFile()){
				String args = achive.getFileName();
				String message = messageSource.getMessage("ERR009", new String[]{args}, Locale.US);
				throw new NgsDataException(message, HttpStatus.NOT_FOUND);
			}
			
			return FileUtils.readFileToString(file, Charset.defaultCharset());
		} catch(NgsDataException e) {
			throw e;
		} catch(Exception e){
			String args = messageSource.getMessage("ENTITY.NGSDATA",null,Locale.US);
			String message = messageSource.getMessage("ERR006", new String[]{args}, Locale.US);
			throw new NgsDataException(message, HttpStatus.NOT_FOUND);
		}
	}
	
	@RequestMapping(value = "/achieves/{achiveId}/gview.gff", method = RequestMethod.GET)
	public String getGffFileAsText(@PathVariable("achiveId") int achiveId, 
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		try{
			NgsDataAchive achive = achiveService.getNgsDataAchive(achiveId);
			if(achive == null) {
				String args = messageSource.getMessage("ENTITY.NGSDATA",null,Locale.US);
				String message = messageSource.getMessage("ERR006", new String[]{args}, Locale.US);
				throw new NgsDataException(message, HttpStatus.NOT_FOUND);
			}
			
			File dir = NgsFileUtils.getGviewInputDir(achive);
			File file = NgsFileUtils.getPhysicalFile(achive);
			file = new File(dir, file.getName());
			
			if(!file.exists() || !file.isFile()){
				String args = achive.getFileName();
				String message = messageSource.getMessage("ERR009", new String[]{args}, Locale.US);
				throw new NgsDataException(message, HttpStatus.NOT_FOUND);
			}
			
			return FileUtils.readFileToString(file, Charset.defaultCharset());
		} catch(NgsDataException e) {
			throw e;
		} catch(Exception e){
			String args = messageSource.getMessage("ENTITY.NGSDATA",null,Locale.US);
			String message = messageSource.getMessage("ERR004", new String[]{args}, Locale.US);
			throw new NgsDataException(message, HttpStatus.NOT_FOUND);
		}
	}
	
	@RequestMapping(value = {"/achieves/Jbrowse"}, method=RequestMethod.POST)
	public Map<String, String> createJbrowse(@RequestBody List<NgsDataAchive> achiveList, 
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		try{
			File outputDir = new File(NgsFileUtils.getJBrowserDir(), "/gdkm");
			outputDir = new File(outputDir, String.format("%03d", ++jbrowseSeq));
			String track = "";
			int i=0;
			for(NgsDataAchive temp : achiveList){
				i++;
				NgsDataAchive achive = achiveService.getNgsDataAchive(temp.getId());
				track += temp.getRegistNo();
				if(i!=achiveList.size()){
					track += "%2C";
				}
				if(achive == null){
					String args = messageSource.getMessage("ENTITY.NGSDATA",null,Locale.US);
					String message = messageSource.getMessage("ERR006", new String[]{args}, Locale.US);
					throw new NgsDataException(message, HttpStatus.NOT_FOUND);
				}
				synchronized(NgsFileProcessService.Lock){
					processService.createJbrowse(achive, outputDir);
				}
			}
			
			Map<String, String> params = new HashMap<String, String>();
			params.put("path", outputDir.getName());
			params.put("track", track);
			return params;
		}catch(NgsDataException e){
			throw e;
		}
	}
	
}
