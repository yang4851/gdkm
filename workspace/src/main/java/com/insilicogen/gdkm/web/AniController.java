package com.insilicogen.gdkm.web;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.Executor;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.insilicogen.gdkm.Globals;
import com.insilicogen.gdkm.model.AniFile;
import com.insilicogen.gdkm.model.AniParameter;
import com.insilicogen.gdkm.model.AniRequest;
import com.insilicogen.gdkm.model.AniResult;
import com.insilicogen.gdkm.model.NgsFileSeqQuantity;
import com.insilicogen.gdkm.model.PageableList;
import com.insilicogen.gdkm.service.AniService;
import com.insilicogen.gdkm.util.EgovProperties;

@RestController
@RequestMapping("/ani")
public class AniController extends AbstractController {
	
	@Resource(name="AniService")
	AniService aniService;
	
	@RequestMapping(value="/fileList", method=RequestMethod.GET)
	public PageableList<AniFile> getFastaFileList(@RequestParam Map<String, Object> params) throws Exception {
		
		int page = getCurrentPage(params.get(Globals.PARAM_PAGE));
		int rowSize = getRowSize(params.get(Globals.PARAM_ROW_SIZE));
		int firstIndex = (page-1) * rowSize;
		
		params.put(Globals.PARAM_FIRST_INDEX, firstIndex);
		params.put(Globals.PARAM_ROW_SIZE, rowSize);
		String searchId = (String) params.get("searchId");
		params.put(searchId, params.get("searchContent"));
		
		// 파일리스트 불러오기
		List<AniFile> results = aniService.selectAchiveOfFasta(params);
		// 파일리스트 count
		Integer totalCount = aniService.countAchiveOfFasta(params);
		
		PageableList<AniFile> pageableList = new PageableList<AniFile>();
		pageableList.setPage(page);
		pageableList.setRowSize(rowSize);
		pageableList.setList(results);
		pageableList.setTotal(totalCount);
		
		return pageableList;
	}
	
	@RequestMapping(value = { "/upload/{method}" }, method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Object> uploadAniInputFile(
			@PathVariable("method") String method,
			HttpServletRequest request, 
			HttpServletResponse response, 
			HttpSession session) throws Exception {
		
		if (!ServletFileUpload.isMultipartContent(request)) {
			System.out.println("MultipartContent 없음");
			return new ResponseEntity<Object>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		Map<String, MultipartFile> files = ((MultipartHttpServletRequest) request).getFileMap();
		Iterator<Entry<String, MultipartFile>> itr = files.entrySet().iterator();
		
		AniRequest output = new AniRequest();
		while(itr.hasNext()) {
			Entry<String, MultipartFile> entry = itr.next();
			MultipartFile file = entry.getValue();
			File target = new File(EgovProperties.getProperty("TOOL.ANI.INPUT.DIR") + "/" + System.currentTimeMillis()+"_" + method + "/" + file.getOriginalFilename().replaceAll("\\s+", "_"));
			System.out.println("INPUT FILE DIR: " + target.getAbsolutePath());
			
			if (!target.exists()) {
				File parent = new File(target.getParent());
				if (!parent.isDirectory()) {
					parent.mkdirs();
				}
			}
			
			if(StringUtils.isBlank(file.getOriginalFilename()))
				continue;
			
			try {
				BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()));
				FileWriter inputFileWriter = new FileWriter(target);
				String line = null;
				while ((line = br.readLine()) != null)
					inputFileWriter.write(line + System.lineSeparator());
				
				// input파일 생성: 파일읽어서 넣기
				inputFileWriter.flush();
				inputFileWriter.close();
				br.close();
				// input파일 권한 변경
				if (target.exists()) {
					target.setReadable(true);
					target.setExecutable(true);
					target.setWritable(true);
				}
				output.setOutputDir(target.getParent());
				System.out.println("업로드 완료: " + target.getAbsolutePath());
				
				//clc 프로그램 돌리기
				NgsFileSeqQuantity seqQuantity = aniService.executeClcAssemblyCell(target);
				System.out.println(seqQuantity);
				output.setSeqQuantity(seqQuantity);
				System.out.println(output);
			} catch (IOException e) {
				e.printStackTrace();
				return new ResponseEntity<Object>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		return new ResponseEntity<Object>(output, HttpStatus.OK);
	}
	
	@RequestMapping(value = { "/run" }, method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Object> runAni(
			@RequestBody AniRequest aniEvent, 
			HttpServletResponse response, 
			HttpSession session) throws Exception {
		System.out.println(aniEvent);
		String rootPath = "/data/gdkm";
		String labelLine = "";
		
		// 1. input file dir 생성
		File aniInputDir = new File(EgovProperties.getProperty("TOOL.ANI.INPUT.DIR") + "/" + System.currentTimeMillis()+"_" + aniEvent.getMethod());
		if(aniEvent.getOutputDir() != null)
			aniInputDir = new File(aniEvent.getOutputDir());
		
		System.out.println("	## ANI INPUT DIR: " + aniInputDir.getAbsolutePath());
		if(!aniInputDir.exists() || !aniInputDir.isDirectory())
			aniInputDir.mkdirs();
		
		// 2. 파일 위치 확인, 파일 복사
		for(AniFile selectedFile: aniEvent.getSelectedFiles()) {
			
			labelLine += selectedFile.getAchiveRegistNo() + "\t" + selectedFile.getAniLabel() + System.lineSeparator();
			
			// 업로드 파일은 이미 올렸으므로
			if(selectedFile.getAchiveId() == null)
				continue;
			
			String registType = selectedFile.getDataType();
			String realName = selectedFile.getFileName().trim();
			realName = selectedFile.getAchiveRegistNo() + "_" + realName.replaceAll("\\s+", "_");
			File file = new File(rootPath , registType + "/" + realName);
			
			// 2-1. 복사할 파일 존재 확인 
			if(!file.exists() || !file.isFile()) {
				System.out.println("파일이 존재하지 않습니다.- " + file.getAbsolutePath());
				return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
			}
			
			// 2-2. 파일 복사
			ByteArrayOutputStream assemblyCellOs = new ByteArrayOutputStream();
			ByteArrayOutputStream assemblyCellEs = new ByteArrayOutputStream();
			fileCopy(assemblyCellOs, assemblyCellEs, file, aniInputDir.getAbsolutePath());
			
		}
		// 3. label.txt 파일 생성
		File labelFile = new File(aniInputDir.getAbsoluteFile() + "/labels.tab");
		fileWrite(labelLine, labelFile);
		
		// 5. cli parameter 생성
		AniParameter parameter = new AniParameter();
		parameter.setInputFileDir(aniInputDir.getAbsolutePath());
		parameter.setMethod(aniEvent.getMethod());
		
		System.out.println("ANI PARAMETER: " + parameter);
		
		// 6. cli 실행 > parameter: method, input dir
		ByteArrayOutputStream aniCliOs = new ByteArrayOutputStream();
		ByteArrayOutputStream aniCliEs = new ByteArrayOutputStream();
		aniService.run(aniCliOs, aniCliEs, parameter);
		System.out.println(aniCliOs);
		System.out.println(aniCliEs);
		
		aniEvent.setOutputDir(EgovProperties.getProperty("TOOL.ANI.OUTPUT.DIR") + "/" + aniInputDir.getName());
		System.out.println("ANI EVENT: " + aniEvent);
		return new ResponseEntity<Object>(aniEvent, HttpStatus.OK);
	}
	
	@RequestMapping(value="/output/file", method=RequestMethod.GET)
	public ResponseEntity<Object> getAniOutputFile(@RequestParam Map<String, Object> params) throws Exception {
		System.out.println("요청 파라메터: " + params);
		System.out.println("outputDir: " + params.get("outputDir") + ", method: " + params.get("method"));
		
		File outputDir = new File((String) params.get("outputDir"));
		
		System.out.println(outputDir.getAbsolutePath());
		
		if(!outputDir.exists() || !outputDir.isDirectory())
			throw new Exception(" ## 디렉토리가 존재하지 않습니다.");
		
		for(File file: outputDir.listFiles()) {
			System.out.println(file.getName());
		}
		
		Map<String, AniResult> results = new HashMap<String, AniResult>();
		
		for(File outputFile : outputDir.listFiles()) {
			if(FilenameUtils.isExtension(outputFile.getName(), "tab")) {
				AniResult result = aniService.resultTabFileParse(outputFile);
				results.put(outputFile.getName(), result);
			}
		}
		
		System.out.println(results);
		
		return new ResponseEntity<Object>(results, HttpStatus.OK);
	}
	
	@RequestMapping(value="/output/image", method=RequestMethod.GET)
	public void getAniOutputImage(
			HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println("outputDir: " + request.getParameter("outputDir") + ", method: " + request.getParameter("method"));
		
		String outputDir = (String) request.getParameter("outputDir");
		String name = (String) request.getParameter("name");
		String method = request.getParameter("method");
		
		File imgFile = new File(outputDir + "/" + method + "_" + name + ".png");
		System.out.println("imgFile: " + imgFile.getAbsolutePath());
		
		FileInputStream ifo = new FileInputStream(imgFile);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buf = new byte[1024];
		int readlength = 0;
		
		while((readlength = ifo.read(buf)) != -1)
			baos.write(buf, 0, readlength);
		
		byte[] imgbuf = null;
		imgbuf = baos.toByteArray();
		baos.close();
		ifo.close();
		
		int length = imgbuf.length;
		OutputStream out = response.getOutputStream();
		out.write(imgbuf, 0, length);
		out.close();
	}
	
	@RequestMapping(value="/output/download", method=RequestMethod.GET)
	public ResponseEntity<Object> downloadAniOutputFile(@RequestParam Map<String, Object> params) throws Exception {
		
		StringBuilder result = new StringBuilder();
		
		String path = (String) params.get("outputDir");
		String name = (String) params.get("name");
		
		try {
			File output = new File(path + "/" + name);
			System.out.println("output: " + output.getAbsolutePath());
			BufferedReader br = new BufferedReader(new FileReader(output));
			
			String line = null;
			while((line = br.readLine()) != null) {
				result.append(line + System.lineSeparator());
				System.out.println(line);
			}
			
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Object>("file not found", HttpStatus.NOT_FOUND);
		}
		List<String> resultList = new ArrayList<String>();
		resultList.add(result.toString());
		
		return new ResponseEntity<Object>(resultList, HttpStatus.OK);
	}
	
	@RequestMapping(value="/upload", method=RequestMethod.DELETE)
	public ResponseEntity<Object> deleteUploadFile(@RequestParam Map<String, Object> params) throws Exception {
		
		System.out.println("요청 파라메터 : " + params.get("file"));
		
		File removeFile = new File((String) params.get("output") + "/" + params.get("fileName") );
		
		if(!removeFile.exists() || !removeFile.isFile())
			throw new Exception ("파일이 존재하지 않습니다. -  " + removeFile.getAbsolutePath());
		
		removeFile.delete();
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	private int fileCopy(OutputStream outputStream, OutputStream errorStream, File file, String inputFileDir) throws Exception {

		CommandLine command = new CommandLine("cp");
		command.addArgument(file.getAbsolutePath());
		
		String extension = FilenameUtils.getExtension(file.getName());
		
		if(!extension.equals("png"))
			command.addArgument(inputFileDir + "/" + file.getName().substring(0, 18) + "." + extension);
		else 
			command.addArgument(inputFileDir);
			
		Executor executor = new DefaultExecutor();
		
		PumpStreamHandler streamHandler;
		
		streamHandler = new PumpStreamHandler(outputStream, errorStream);
		executor.setStreamHandler(streamHandler);
		System.out.println("  ## COMMAND: " + String.join(" ", command.toStrings()));
		try {
			int result = executor.execute(command);
			outputStream.close();
			errorStream.close();
			return result;
		} catch (Exception e) { 
			throw e;
		}
	}
	
	private void fileWrite(String content, File file) {
		
		FileWriter writer = null;
		try {
			writer = new FileWriter(file, false);
			writer.write(content);
			writer.flush();
			
			// input파일 권한 변경
			if (file.exists()) {
				file.setReadable(true);
				file.setExecutable(true);
				file.setWritable(true);
			}
		} catch(IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(writer != null) writer.close();
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
}
