package com.insilicogen.gdkm.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.insilicogen.gdkm.Globals;
import com.insilicogen.gdkm.exception.NgsDataException;
import com.insilicogen.gdkm.model.NgsDataAchive;
import com.insilicogen.gdkm.model.NgsDataRegist;
import com.insilicogen.gdkm.model.NgsFileQcResult;
import com.insilicogen.gdkm.model.NgsFileQcSummary;
import com.insilicogen.gdkm.model.NgsFileSeqQuantity;
import com.insilicogen.gdkm.model.PageableList;
import com.insilicogen.gdkm.model.User;
import com.insilicogen.gdkm.service.NgsDataAchiveService;
import com.insilicogen.gdkm.service.NgsDataRegistService;
import com.insilicogen.gdkm.service.NgsFileProcessService;
import com.insilicogen.gdkm.service.NgsFileQcService;
import com.insilicogen.gdkm.service.NgsFileSeqService;
import com.insilicogen.gdkm.util.EgovStringUtil;
import com.insilicogen.gdkm.util.NgsFileUtils;

import net.sf.json.JSONObject;

@RestController
public class NgsDataAchiveController extends AbstractController {

	static Logger logger = LoggerFactory.getLogger(NgsDataAchiveController.class);
	
	@Resource(name="NgsDataRegistService")
	NgsDataRegistService registService;
	
	@Resource(name="NgsDataAchiveService")
	NgsDataAchiveService achiveService;
	
	@Resource(name="NgsFileProcessService")
	NgsFileProcessService processService;
	
	@Resource(name="NgsFileSeqService")
	NgsFileSeqService quantityService;
	
	@Resource(name="NgsFileQcService")
	NgsFileQcService qcService;
	
	@Resource(name = "messageSource")
	private MessageSource messageSource;
	
	@RequestMapping(value = { "/regist-data/{registId}/achieves" }, method = RequestMethod.POST)
	@ResponseBody
	public List<NgsDataAchive> uploadAchiveFiles(
			@PathVariable("registId") Integer registId, 
			HttpServletRequest request, 
			HttpServletResponse response, 
			HttpSession session) throws Exception {
		
		if (!ServletFileUpload.isMultipartContent(request)) {
			String message = messageSource.getMessage("ERR021", new String[]{"multipart","multipart / form-data"}, Locale.US);
			throw new NgsDataException(message, HttpStatus.BAD_REQUEST);
		}
		checkAuthorization(session);

		try {
			NgsDataRegist regist = registService.getNgsDataRegist(registId);
			List<NgsDataAchive> achiveList = new ArrayList<NgsDataAchive>();
			
			Map<String, MultipartFile> files = ((MultipartHttpServletRequest) request).getFileMap();
			Iterator<Entry<String, MultipartFile>> itr = files.entrySet().iterator();
			while(itr.hasNext()) {
				Entry<String, MultipartFile> entry = itr.next();
				MultipartFile file = entry.getValue();
				if(StringUtils.isBlank(file.getOriginalFilename()))
					continue;
				
				synchronized(NgsDataAchiveService.Lock) {
					NgsDataAchive achive = achiveService.createNgsDataAchive(regist, file);
					if(NgsFileUtils.isZippedFile(achive.getFileName())) {
						processService.unzipAchiveFile(achive);
						achive.setRegistStatus(Globals.STATUS_VERIFY_RUNNING);
					}
					achiveList.add(achive);
				}
				
				// 새로운 파일이 업로드 되면 상태를 검증 이전 상태로 변경
				regist.setRegistStatus(Globals.STATUS_REGIST_READY);
				synchronized(NgsDataRegistService.Lock){
					registService.changeNgsDataRegist(regist);
				}
			}
		
			return achiveList;
		} catch (Exception e) {
			String args = messageSource.getMessage("ENTITY.NGSDATAFILE",null,Locale.US);
			String message = messageSource.getMessage("ERR002", new String[]{args}, Locale.US);
			throw new NgsDataException(message+" - "+e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = { "/regist-data/{registId}/achieves" }, method = RequestMethod.GET)
	@ResponseBody
	public List<NgsDataAchive> getAchiveFileList(
			@PathVariable("registId") Integer registId, 
			HttpSession session) throws Exception {
		try{
			NgsDataRegist regist = registService.getNgsDataRegist(registId);
			return achiveService.getNgsDataAchiveList(regist);
		} catch(Exception e){
			String args = messageSource.getMessage("ENTITY.NGSDATAFILE",null,Locale.US);
			String message = messageSource.getMessage("ERR004", new String[]{args}, Locale.US);
			throw new NgsDataException(message+" - "+e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}
	
	/**
	 * 요청 media type 이 json 인 경우는 NGS 데이터 파일의 기본 등록정보를 반환하고 
	 * 그렇지 않은 경우는 파일을 다운로드 하도록 처리  
	 * 
	 * response.getContentType()이 angularJS의 http 에서 GET 메소드로 호출하는 경우 
	 * 헤더가 정의되지 않으면 "application/json" 으로 설정되기 때문에 상세정보 호출로 인식하고 
	 * 그렇지 않은 경우는 파일 다운로드 요청으로 인식한다.  
	 * 
	 * HttpServletResponse.setContentLengthLong() 메소드는 Servlet 3.1 부터 지원 됨. 
	 * Apache Tomcat의 경우 8버전 이후 부터 Servlet 3.1이 지원됨. 
	 *  
	 * 미생물 데이터의 경우 NGS 데이터 파일의 크기는 1GB가 넘지 않을 것이기에 Long 크기까지 필요하지 않을 것으로 보임
	 * 
	 * @param achiveId
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = { "/achieves/{achiveId}" }, method = RequestMethod.GET)
	public void getNgsDataAchive(@PathVariable("achiveId") Integer achiveId,
			HttpServletRequest request, HttpServletResponse response) throws Exception{
		try{
			NgsDataAchive achive = achiveService.getNgsDataAchive(achiveId);
			if(achive == null) {
				String args = messageSource.getMessage("ENTITY.NGSDATA",null,Locale.US);
				String message = messageSource.getMessage("ERR006", new String[]{args}, Locale.US);
				throw new NgsDataException(message, HttpStatus.NOT_FOUND);
			}
			
			MediaType mediaType = MediaType.APPLICATION_OCTET_STREAM;
			try {
				mediaType = MediaType.parseMediaType(request.getContentType());
			} catch(Exception e) {
				logger.error(e.getMessage());
			}
			
			if(MediaType.APPLICATION_JSON.includes(mediaType)) {
				JSONObject json = JSONObject.fromObject(achive);
				response.setContentType(request.getContentType());
				response.setCharacterEncoding(request.getCharacterEncoding());
				response.setContentLength(json.toString().getBytes().length);
				response.getOutputStream().write(json.toString().getBytes());
			} else {
				File file = NgsFileUtils.getPhysicalFile(achive);
				if(!file.exists() || !file.isFile()){
					String args = messageSource.getMessage("ENTITY.NGSDATAFILE",null,Locale.US);
					String message = messageSource.getMessage("ERR006", new String[]{args}, Locale.US);
					
					response.sendError(HttpServletResponse.SC_NOT_FOUND, message);
				} else {
					FileInputStream input = new FileInputStream(file); 
					
					String filename= URLEncoder.encode(achive.getFileName(), "UTF-8").replaceAll("\\+", " ");
					response.setContentType(MediaType.APPLICATION_OCTET_STREAM.toString());
					response.setContentLengthLong(file.length());
					response.setHeader( "Content-Disposition", "attachment; filename=\"" + filename + "\"" );
					
					OutputStream output = response.getOutputStream();
					
					byte[] buffer = new byte[8192];
					int bytes = -1;
					while((bytes = input.read(buffer)) > -1) {
						output.write(buffer, 0, bytes);
					}
					
					input.close();
					output.close();
				}
			}
		} catch(NgsDataException e) {
			throw e;
		} catch(Exception e){
			String args = messageSource.getMessage("ENTITY.NGSDATAFILE",null,Locale.US);
			String message = messageSource.getMessage("ERR005", new String[]{args}, Locale.US);
			throw new NgsDataException(message, HttpStatus.NOT_FOUND);
		}
	}
	
	@RequestMapping(value = { "/achieves" }, method = RequestMethod.GET)
	@ResponseBody
	public PageableList<NgsDataAchive> getAchiveFileList(
			@RequestParam Map<String, Object> params, 
			HttpSession session) throws Exception {
		try{
			User loginUser = (User)session.getAttribute(Globals.LOGIN_USER);
			if(loginUser == null) 
				params.put("openYn", "Y");
			
			int page = getCurrentPage(params.get(Globals.PARAM_PAGE));
			int rowSize = getRowSize(params.get(Globals.PARAM_ROW_SIZE));
			int firstIndex = (page-1) * rowSize;
			System.err.println(page+","+rowSize);
			params.put(Globals.PARAM_FIRST_INDEX, firstIndex);
			params.put(Globals.PARAM_ROW_SIZE, rowSize);
			
			List<NgsDataAchive> achiveList = achiveService.getNgsDataAchiveList(params);
			int totalCount = achiveService.getNgsDataAchiveCount(params);
			PageableList<NgsDataAchive> pageableList = new PageableList<NgsDataAchive>();
			pageableList.setPage(page);
			pageableList.setRowSize(rowSize);
			pageableList.setList(achiveList);
			pageableList.setTotal(totalCount);
			
			return pageableList;
		} catch(NgsDataException e) {
			throw e;
		} catch(Exception e){
			String args = messageSource.getMessage("ENTITY.NGSDATAFILE",null,Locale.US);
			String message = messageSource.getMessage("ERR005", new String[]{args}, Locale.US);
			throw new NgsDataException(message, HttpStatus.NOT_FOUND);
		}
	}

	@RequestMapping(value = { "/achieves/{ids}" }, method = RequestMethod.DELETE)
	@ResponseBody
	public List<NgsDataAchive> deleteAchiveFile(
			@PathVariable("ids") String ids, 
			HttpSession session) throws Exception {
		checkAuthorization(session);
		try{
			List<NgsDataAchive> removedList = new ArrayList<NgsDataAchive>();
			int[] idList = EgovStringUtil.splitStringToIntegerArray(ids);
			
			synchronized(NgsDataAchiveService.Lock) {
				for(int i=0; i < idList.length ; i++) {
					try {
						NgsDataAchive achive = achiveService.getNgsDataAchive(idList[i]);
						if(achiveService.deleteNgsDataAchive(achive) == 1) {
							removedList.add(achive);
						}
					} catch(Exception e) {
						logger.error(e.getMessage());
						String args = messageSource.getMessage("ENTITY.NGSDATAFILE",null,Locale.US);
						String message = messageSource.getMessage("ERR005", new String[]{args}, Locale.US);
						throw new NgsDataException(message, HttpStatus.INTERNAL_SERVER_ERROR);
					}
				}
			}
			
			// 등록정보 상태정보 확인
			for(NgsDataAchive achive : removedList) {
				checkRegistStatusOfNgsAchive(achive);
			}
			
			return removedList;
		} catch(NgsDataException e) {
			throw e;
		} catch(Exception e){
			String args = messageSource.getMessage("ENTITY.NGSDATA",null,Locale.US);
			String message = messageSource.getMessage("ERR005", new String[]{args}, Locale.US);
			throw new NgsDataException(message, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	private void checkRegistStatusOfNgsAchive(NgsDataAchive achive) {
		try {
			NgsDataRegist regist = registService.getNgsDataRegist(achive.getDataRegist().getId());
			List<NgsDataAchive> achiveList = achiveService.getNgsDataAchiveList(regist);
			
			int successCnt = 0;
			int failedCnt = 0;
			int runningCnt = 0;
			int readyCnt = 0;
			
			for(NgsDataAchive file : achiveList) {
				if(Globals.STATUS_VERIFY_SUCCESS.equals(file.getVerifiStatus())) {
					successCnt++;
				} else if(Globals.STATUS_VERIFY_FAILED.equals(file.getVerifiStatus())) {
					failedCnt++;
				} else if(Globals.STATUS_VERIFY_RUNNING.equals(file.getVerifiStatus())) {
					runningCnt++;
				} else if(Globals.STATUS_VERIFY_WARN.equals(file.getVerifiStatus())) {
					successCnt++;
				} else {
					readyCnt++;
				}
			}
			
			if(runningCnt > 0) {
				regist.setRegistStatus(Globals.STATUS_REGIST_VALIDATING);
				registService.changeNgsDataRegist(regist);
			} else if(failedCnt > 0) {
				regist.setRegistStatus(Globals.STATUS_REGIST_ERROR);
				registService.changeNgsDataRegist(regist);
			} else if(readyCnt > 0 || achiveList.size() == 0) {
				if(Globals.REGIST_STATUS_LIST.indexOf(regist.getRegistStatus()) > 0) {
					regist.setRegistStatus(Globals.STATUS_REGIST_READY);
					registService.changeNgsDataRegist(regist);
				}
			} else if(successCnt == achiveList.size()) {
				if(Globals.REGIST_STATUS_LIST.indexOf(regist.getRegistStatus()) > 2) {
					regist.setRegistStatus(Globals.STATUS_REGIST_VALIDATED);
					registService.changeNgsDataRegist(regist);
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value = { "/achieves/{achiveId}/quantity" }, method = RequestMethod.GET)
	@ResponseBody
	public NgsFileSeqQuantity getNgsFileSeqQuantity(@PathVariable("achiveId") Integer achiveId) throws Exception {
		try{
			NgsDataAchive achive = achiveService.getNgsDataAchive(achiveId);
			NgsFileSeqQuantity quantity = quantityService.getNgsFileSeqQuantity(achive);
			if(quantity == null) {
				String args = messageSource.getMessage("ENTITY.NGSDATA",null,Locale.US);
				String message = messageSource.getMessage("ERR006", new String[]{args}, Locale.US);
				throw new NgsDataException(message, HttpStatus.NOT_FOUND);
			}
			
			return quantity;
		} catch(NgsDataException e) {
			throw e;
		} catch(Exception e){
			String args = messageSource.getMessage("ENTITY.NGSDATA",null,Locale.US);
			String message = messageSource.getMessage("ERR006", new String[]{args}, Locale.US);
			throw new NgsDataException(message, HttpStatus.NOT_FOUND);
		}
	}
	
	@RequestMapping(value = { "/achieves/{achiveId}/qcResults" }, method = RequestMethod.GET)
	@ResponseBody
	public List<NgsFileQcResult> getNgsFileQcResultList(@PathVariable("achiveId") Integer achiveId) throws Exception {
		try{
			NgsDataAchive achive = achiveService.getNgsDataAchive(achiveId);
			return qcService.getNgsFileQcResultList(achive);
		} catch(NgsDataException e) {
			throw e;
		} catch(Exception e){
			String args = messageSource.getMessage("ENTITY.NGSDATAFILE",null,Locale.US);
			String message = messageSource.getMessage("ERR006", new String[]{args}, Locale.US);
			throw new NgsDataException(message, HttpStatus.NOT_FOUND);
		}
	}
	
	@RequestMapping(value = { "/achieves/{achiveId}/qcSummary" }, method = RequestMethod.GET)
	@ResponseBody
	public List<NgsFileQcSummary> getNgsFileQcSummaryList(@PathVariable("achiveId") Integer achiveId) throws Exception {
		try{
			NgsDataAchive achive = achiveService.getNgsDataAchive(achiveId);
			return qcService.getNgsFileQcSummaryList(achive);
		} catch(NgsDataException e) {
			throw e;
		} catch(Exception e){
			String args = messageSource.getMessage("ENTITY.NGSDATAFILE",null,Locale.US);
			String message = messageSource.getMessage("ERR006", new String[]{args}, Locale.US);
			throw new NgsDataException(message, HttpStatus.NOT_FOUND);
		}
	}
	
	@RequestMapping(value = { "/achieves/{achiveId}/qcReport" }, method = RequestMethod.GET)
	@ResponseBody
	public void getNgsFileQcReport(@PathVariable("achiveId") Integer achiveId, 
			HttpServletResponse response) throws Exception {
		try{
			NgsDataAchive achive = achiveService.getNgsDataAchive(achiveId);
			List<NgsFileQcResult> qcList = qcService.getNgsFileQcResultList(achive);
			
			NgsFileQcResult reportHtml = null;
			for(NgsFileQcResult qc : qcList) {
				if(qc.getFileType().matches("(html|htm)")) {
					reportHtml = qc;
					break;
				}
			}
			
			if(reportHtml == null) {
				String args = messageSource.getMessage("ENTITY.NGSDATAFILE",null,Locale.US);
				String message = messageSource.getMessage("ERR006", new String[]{args}, Locale.US);
				response.sendError(HttpServletResponse.SC_NOT_FOUND, message);
			}
			
			File dir = NgsFileUtils.getFastQcOutputDir(achive);
			File html = new File(dir, reportHtml.getFileName());
			if(!html.exists()) {
				String args = messageSource.getMessage("ENTITY.NGSDATAFILE",null,Locale.US);
				String message = messageSource.getMessage("ERR006", new String[]{args}, Locale.US);
				response.sendError(HttpServletResponse.SC_NOT_FOUND, message);
			}
			
			response.setContentType( MediaType.TEXT_HTML.toString() );
			response.setCharacterEncoding("euc-kr");
			response.getOutputStream().write(FileUtils.readFileToByteArray(html));
		} catch(NgsDataException e) {
			throw e;
		} catch(Exception e){
			String args = messageSource.getMessage("ENTITY.NGSDATAFILE",null,Locale.US);
			String message = messageSource.getMessage("ERR006", new String[]{args}, Locale.US);
			throw new HttpClientErrorException(HttpStatus.NOT_FOUND, message);
		}
	}
	
	@RequestMapping(value={"/achieves/achiveNode"}, method=RequestMethod.GET)
	public List<Map<String, String>> getAchiveNode(
			@RequestParam Map<String, Object> params
			)throws Exception{
		return achiveService.getAchiveNode(params);
	}
	
}
