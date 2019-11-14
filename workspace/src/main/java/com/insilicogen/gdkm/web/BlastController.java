package com.insilicogen.gdkm.web;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.io.FilenameUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.insilicogen.gdkm.Globals;
import com.insilicogen.gdkm.model.AchiveBlastSequence;
import com.insilicogen.gdkm.model.AchiveHeaderXref;
import com.insilicogen.gdkm.model.BlastAchiveLink;
import com.insilicogen.gdkm.model.BlastEvent;
import com.insilicogen.gdkm.model.BlastRequest;
import com.insilicogen.gdkm.model.BlastResult;
import com.insilicogen.gdkm.model.NgsDataAchive;
import com.insilicogen.gdkm.model.NgsDataFeatures;
import com.insilicogen.gdkm.model.NgsDataFeaturesHeader;
import com.insilicogen.gdkm.model.NgsDataRegist;
import com.insilicogen.gdkm.model.PageableList;
import com.insilicogen.gdkm.service.BlastService;
import com.insilicogen.gdkm.service.NgsDataAchiveService;
import com.insilicogen.gdkm.util.EgovProperties;
import com.insilicogen.gdkm.util.NgsFileUtils;

@RestController
@RequestMapping("/blast")
public class BlastController extends AbstractController {

	@Resource(name = "BlastService")
	private BlastService blastService;

	@Resource(name = "NgsDataAchiveService")
	private NgsDataAchiveService achiveService;

	@RequestMapping(value = { "/run" }, method = RequestMethod.POST)
	public BlastEvent run(@RequestBody BlastRequest request) throws Exception {
		System.out.println(request);

		// 1. input file 생성
		File file = new File(EgovProperties.getProperty("TOOL.BLAST.INPUT.DIR") + "/" + System.currentTimeMillis() + "_"
				+ request.getToolName() + ".fasta");
		System.out.println(" ## INPUT FILE: " + file.getAbsolutePath());

		blastService.fileWrite(request.getUploadString(), file);

		// 2. custom db 생성
		String tool = request.getToolName();
		File customDb = null;
		if ("blastn".equals(tool) || "tblastx".equals(tool) || "tblastn".equals(tool))
			customDb = new File(EgovProperties.getProperty("TOOL.BLAST.DB.DIR") + "/" + System.currentTimeMillis() + "_"
					+ request.getToolName() + "_custom.nal");
		else if ("blastx".equals(tool) || "blastp".equals(tool))
			customDb = new File(EgovProperties.getProperty("TOOL.BLAST.DB.DIR") + "/" + System.currentTimeMillis() + "_"
					+ request.getToolName() + "_custom.pal");

		System.out.println(request.getDbNames());
		String content = blastService.FormatDbParse(request.getDbNames());
		blastService.fileWrite(content, customDb);

		BlastEvent event = new BlastEvent();
		event.setBlastDb(customDb.getAbsolutePath());
		event.setBlastTool(request.getToolName());
		event.setExpect(request.getExpect());
		event.setInputFile(file);
		event.setFilter(request.getFilter());

		ByteArrayOutputStream assemblyCellOs = new ByteArrayOutputStream();
		ByteArrayOutputStream assemblyCellEs = new ByteArrayOutputStream();

		// 3. blast 실행
		blastService.run(assemblyCellOs, assemblyCellEs, event);
		System.out.println(assemblyCellOs);
		System.out.println(assemblyCellEs);

		event.setFileName(file.getName());
		return event;
	}

	@RequestMapping(value = "/dblist/{type}", method = RequestMethod.GET)
	public Set<String> getDbList(@PathVariable("type") String type) throws Exception {
		File dbListDir = new File(EgovProperties.getProperty("TOOL.BLAST.DB.DIR"));

		if (!dbListDir.exists() || !dbListDir.isDirectory())
			throw new Exception(" ## 디렉토리가 존재하지 않습니다.");

		Set<String> fileNames = new HashSet<String>();
		for (File file : dbListDir.listFiles()) {
			String fileName = file.getName().replace("." + FilenameUtils.getExtension(file.getName()), "");
			String extension = FilenameUtils.getExtension(file.getName());

			if ("dna".equals(type) && ("nhr".equals(extension) || "nin".equals(extension) || "nsq".equals(extension)))
				fileNames.add(fileName);
			else if ("protein".equals(type)
					&& ("phr".equals(extension) || "pin".equals(extension) || "psq".equals(extension)))
				fileNames.add(fileName);
		}

		return fileNames;
	}

	@RequestMapping(value = "/output/tab/{fileName}", method = RequestMethod.GET)
	public List<BlastAchiveLink> getOutputFileTab(@PathVariable("fileName") String fileName) throws Exception {
		System.out.println(" ## fileName: " + fileName);

		// 1. output file 파싱
		File outputTabFile = new File(
				EgovProperties.getProperty("TOOL.BLAST.OUTPUT.DIR") + "/" + fileName + ".fasta/output.tab");
		System.out.println(" ## OUTPUT FILE PATH: " + outputTabFile.getAbsolutePath());

		if (!outputTabFile.exists() || !outputTabFile.isFile())
			throw new Exception(" ## 파일이 존재하지 않습니다.");

		List<BlastResult> results = blastService.blastResultTabFileParse(outputTabFile);

		// 2. pairwise file parse
		File outputPairwiseFile = new File(
				EgovProperties.getProperty("TOOL.BLAST.OUTPUT.DIR") + "/" + fileName + ".fasta/output.pairwise");
		System.out.println(" ## OUTPUT FILE PATH: " + outputPairwiseFile.getAbsolutePath());

		if (!outputPairwiseFile.exists() || !outputPairwiseFile.isFile())
			throw new Exception(" ## 파일이 존재하지 않습니다.");

		Map<String, String> resultPairwise = blastService.blastResultPairwiseParse(outputPairwiseFile);

		Set<String> keySet = resultPairwise.keySet();
		Iterator<String> iterator = keySet.iterator();
		List<String> keyList = new ArrayList<>();
		
		while (iterator.hasNext()) {
			keyList.add((String) iterator.next());
		}
		
		Map<Integer, BlastAchiveLink> linkMap = new HashMap<>();
		for (String keyStr : keyList) {
			
			String key = keyStr;
			String sequenceName = keyStr;
			if(key.contains(" "))
				key = key.split(" ")[0].trim();
			
			if(sequenceName.contains(","))
				sequenceName = sequenceName.split(",")[0].trim();
			
			// tab 과 pairwise 비교해서 넣기..
			System.out.println("		## key:  " + key + ", keyStr:  " + keyStr);
			BlastAchiveLink blastAchiveLink = new BlastAchiveLink();
			for(BlastResult result: results) {
				if (key.equals(result.getSubjectId())) {
					blastAchiveLink.setPairwise(resultPairwise.get(keyStr));
					blastAchiveLink.setResults(result);
				}
			}
			
			// achive 찾기
			AchiveBlastSequence achiveBlastSeq = blastService.selectAchiveIdBySequenceName(sequenceName);
			
			NgsDataAchive achive = new NgsDataAchive();
			if(achiveBlastSeq != null)
				achive = blastService.selectAchiveById(achiveBlastSeq.getAchiveId());
			else 
				achive.setId(0);
			System.out.println("	## achiveBlastSeq : " + achiveBlastSeq + ", achive :  " + achive);
			
			blastAchiveLink.setAchive(achive);
			
			if(linkMap.containsKey(achive.getId())) {
				BlastAchiveLink saved = new BlastAchiveLink();
				saved = linkMap.get(achive.getId());
				saved.setResults(blastAchiveLink.getResults());
			} else {
				linkMap.put(achive.getId(), blastAchiveLink);
			}
			
		}
		
		List<BlastAchiveLink> links = new ArrayList<BlastAchiveLink>(linkMap.values());
		return links;
	}

	@RequestMapping(value = "/features/{subjectId}", method = RequestMethod.GET)
	public PageableList<NgsDataFeatures> getNgsDataFeatures(@PathVariable("subjectId") String subjectId,
			@RequestParam Map<String, Object> params) throws Exception {

		System.out.println("subject id: " + subjectId);
 		System.out.println("start: " + params.get("subjectStart") + ", end :  "+ params.get("subjectEnd"));
		int page = getCurrentPage(params.get(Globals.PARAM_PAGE));
		int rowSize = getRowSize(params.get(Globals.PARAM_ROW_SIZE));
		int firstIndex = (page - 1) * rowSize;

		params.put(Globals.PARAM_FIRST_INDEX, firstIndex);
		params.put(Globals.PARAM_ROW_SIZE, rowSize);

		List<NgsDataFeaturesHeader> headers = blastService.selectHeaderByLocus(subjectId);
		List<NgsDataFeatures> features = new ArrayList<NgsDataFeatures>();
		PageableList<NgsDataFeatures> pageableList = new PageableList<NgsDataFeatures>();
		int totalCount = 0;

		if (!headers.isEmpty()) {
			List<Integer> headerIds = new ArrayList<>();
			for (NgsDataFeaturesHeader header : headers) {
				headerIds.add(header.getHeaderId());
			}

			params.put("headers", headerIds);
			features.addAll(blastService.selectFeaturesByHeaderIds(params));
			System.out.println(features.size());

			totalCount = blastService.countFeaturesByHeaderIds(params);
		}

		pageableList.setList(features);
		pageableList.setTotal(totalCount);
		pageableList.setPage(page);
		pageableList.setRowSize(rowSize);

		return pageableList;
	}
	
	@RequestMapping(value = "/sequence/{achiveId}", method = RequestMethod.GET)
	public ResponseEntity<Object> getFastaSequence(@PathVariable("achiveId") Integer achiveId,
			@RequestParam Map<String, Object> params) throws Exception {
		
		System.out.println("achiveId: " + achiveId);
		System.out.println("start: " + params.get("subjectStart") + ", end :  "+ params.get("subjectEnd"));
		String subjectId = (String) params.get("subjectId");
		Integer subjectStart = Integer.parseInt(((String)params.get("subjectStart")).trim());
		Integer subjectEnd = Integer.parseInt(((String)params.get("subjectEnd")).trim());
		// 1. fasta 파일 물리파일 갖구오기
		NgsDataAchive achive = blastService.selectAchiveById(achiveId);
		File fastaFile = NgsFileUtils.getPhysicalFile(achive);
		
		// 2. fasta 파일 존재 유무
		if(!fastaFile.exists() || !fastaFile.isFile())
			throw new Exception("파일이 존재하지 않습니다. -  " + fastaFile.getAbsolutePath());
		
		BufferedReader br = new BufferedReader(new FileReader(fastaFile));
		String line = null;
		boolean lineStart = false;
		boolean seqStart = false;
		int count = 0;
		String seq = "";
		// 3. fasta파일 읽어서 >로 subjectId위치 찾기
		while ((line = br.readLine()) != null) {
			if(!lineStart && line.trim().startsWith(">") && line.trim().contains(subjectId)) {
				lineStart = true;
				continue;
			}
			if(lineStart && !line.trim().startsWith(">")) {
				String seqLine = line.replace(" ", "").trim();
				count += seqLine.length();
				
				// 4. 읽은 다음 start위치 ~ end 위치 찾아서 리턴
				if(!seqStart && count >= subjectStart) {
					seqStart = true;
					if(count >= subjectEnd)
						seq += seqLine.substring(seqLine.length() - (count-subjectStart+1), seqLine.length() - (count-subjectEnd+1));
					else 
						seq += seqLine.substring(seqLine.length() - (count-subjectStart+1), seqLine.length());
					
					continue;
				}
				
				if(seqStart && count >= subjectEnd) {
					seqStart = false;
					seq += seqLine.substring(0, seqLine.length() - (count-subjectEnd));
					break;
				}
				
				if(seqStart)
					seq += seqLine;
			}
		}
		
		List<String> resultList = new ArrayList<String>();
		resultList.add(seq);

		return new ResponseEntity<Object>(resultList, HttpStatus.OK);
	}

	@RequestMapping(value = "/ngs-file/{dataType}/download/{registId}/{locus}", method = RequestMethod.POST)
	public ResponseEntity<Object> downloadGbkFile(@PathVariable("dataType") String dataType,
			@PathVariable("registId") Integer registId, @PathVariable("locus") String locus,
			@RequestBody List<NgsDataFeatures> request) throws Exception {
		System.out.println("요청 파라메터: " + request);

		StringBuilder result = new StringBuilder();
		try {
			NgsDataAchive achive = achiveService.getNgsDataAchive(registId);
//			File output = NgsFileUtils.getPhysicalFile(achive);
			List<NgsDataAchive> achives = achiveService.getNgsDataAchiveList(achive.getDataRegist());
			
			File output = null;
			for(NgsDataAchive source : achives) {
				if(source.getFileType().equals("gbk"))
					output = NgsFileUtils.getPhysicalFile(source);
			}
				
			if(output == null)
				throw new Exception ("gbk 파일이 존재하지 않습니다.");
			
			System.out.println("output: " + output.getAbsolutePath());

			// blastService.ngsFileParse(output);

			BufferedReader br = new BufferedReader(new FileReader(output));

			String line = null;
			boolean featuresStart = false;
			boolean headerStart = false;
			boolean check = false;
			while ((line = br.readLine()) != null) {

				NgsDataFeatures remove = null;
				
				if (line.contains("LOCUS")) {
					headerStart = false;
					
					if(line.contains(locus))
						headerStart = true;
				}
				
				// header 부분 추가
				if (headerStart && line.trim().equals("FEATURES             Location/Qualifiers")) {
					result.append(line + System.lineSeparator());
					featuresStart = true;
					continue;
				}

				if (!featuresStart && headerStart) {
					result.append(line + System.lineSeparator());
				} else {
					if (!line.trim().startsWith("/") && !line.contains("\"") && line.contains("..")) {
						check = false;

						for (NgsDataFeatures feature : request) {
							if (line.contains(feature.getStart() + ".." + feature.getEnd())
									&& line.contains(feature.getType())) {
								result.append(line + System.lineSeparator());
								check = true;
								remove = feature;
								break;
							}
						}

						if (remove != null)
							request.remove(remove);

					} else if (check && (line.trim().startsWith("/") || line.contains("\"") || !line.contains("..")))
						result.append(line + System.lineSeparator());
				}
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
	
	@RequestMapping(value = "/ngs-data/{achiveRegistNo}", method = RequestMethod.GET)
	public NgsDataRegist getDataRegist(@PathVariable("achiveRegistNo") String achiveRegistNo) throws Exception {
		System.out.println("요청 파라미터:  " + achiveRegistNo);
		
		NgsDataAchive achive = blastService.selectAchiveByRegistNo(achiveRegistNo);
		
		if(achive == null)
			throw new Exception("파일 정보가 존재하지 않습니다.-  " + achiveRegistNo);
		
		return achive.getDataRegist();
	}
	
}
