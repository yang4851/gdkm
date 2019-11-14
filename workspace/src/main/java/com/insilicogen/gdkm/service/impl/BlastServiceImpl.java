package com.insilicogen.gdkm.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.Executor;
import org.apache.commons.exec.PumpStreamHandler;
import org.springframework.stereotype.Service;

import com.insilicogen.gdkm.dao.BlastDAO;
import com.insilicogen.gdkm.model.AchiveBlastSequence;
import com.insilicogen.gdkm.model.AchiveHeaderXref;
import com.insilicogen.gdkm.model.BlastEvent;
import com.insilicogen.gdkm.model.BlastResult;
import com.insilicogen.gdkm.model.NgsDataAchive;
import com.insilicogen.gdkm.model.NgsDataFeatures;
import com.insilicogen.gdkm.model.NgsDataFeaturesHeader;
import com.insilicogen.gdkm.model.NgsDataRegist;
import com.insilicogen.gdkm.service.BlastService;
import com.insilicogen.gdkm.util.EgovProperties;

@Service("BlastService")
public class BlastServiceImpl implements BlastService {

	@Resource(name = "BlastDAO")
	BlastDAO blastDAO;

	@Override
	public int run(OutputStream outputStream, OutputStream errorStream, BlastEvent event) throws Exception {

		if (outputStream == null || errorStream == null)
			throw new IllegalArgumentException("필수 인자가 비었습니다.");

		CommandLine command = new CommandLine("java");

		command.addArgument("-jar");
		command.addArgument(EgovProperties.getProperty("TOOL.BLAST.JAR.PATH"));
		command.addArgument("-i");
		command.addArgument(event.getInputFile().getAbsolutePath());
		command.addArgument("-d");
		command.addArgument(event.getBlastDb());
		command.addArgument("-t");
		command.addArgument(event.getBlastTool());
		command.addArgument("-e");
		command.addArgument(event.getExpect());
		command.addArgument("-f");
		command.addArgument(event.getFilter());

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

	@Override
	public String FormatDbParse(String[] target) throws Exception {
		String content = "#" + System.lineSeparator();
		content += "# Alias file created "
				+ new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date(System.currentTimeMillis()))
				+ System.lineSeparator();
		content += "#" + System.lineSeparator() + System.lineSeparator();
		content += "DBLIST ";

		for (String db : target) {
			content += "\"" + db + "\" ";
		}

		return content;
	}

	@Override
	public void fileWrite(String content, File file) {

		FileWriter writer = null;
		try {
			writer = new FileWriter(file, false);
			writer.write(content);
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (writer != null)
					writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public List<BlastResult> blastResultTabFileParse(File outputFile) throws Exception {
		if (!outputFile.exists() || !outputFile.isFile())
			throw new Exception("결과 파일이 존재하지 않습니다.");

		BufferedReader resultFileReader = new BufferedReader(new FileReader(outputFile));
		List<BlastResult> results = new ArrayList<BlastResult>();
		String line;
		int count = 0;
		while ((line = resultFileReader.readLine()) != null) {

			if (!line.contains("#")) {
				String[] columns = line.split("\t");
				BlastResult result = new BlastResult();
				result.setId(count++);
				result.setQueryId(columns[0]);
				result.setSubjectId(columns[1]);
				result.setIdentity(Float.parseFloat(columns[2]));
				result.setLength(Integer.parseInt(columns[3]));
				result.setMismatches(Integer.parseInt(columns[4]));
				result.setGapOpenings(Integer.parseInt(columns[5]));
				result.setQueryStart(Integer.parseInt(columns[6]));
				result.setQueryEnd(Integer.parseInt(columns[7]));
				result.setSubjectStart(Integer.parseInt(columns[8]));
				result.setSubjectEnd(Integer.parseInt(columns[9]));
				result.seteValue(columns[10]);
				result.setBitScore(Float.parseFloat(columns[11]));

				results.add(result);
			}

		}
		resultFileReader.close();

		return results;
	}

	@Override
	public Map<String, String> blastResultPairwiseParse(File outputFile) throws Exception {

		if (!outputFile.exists() || !outputFile.isFile())
			throw new Exception("결과 파일이 존재하지 않습니다.");

		BufferedReader resultFileReader = new BufferedReader(new FileReader(outputFile));
		// List<String> results = new ArrayList<String>();
		Map<String, String> results = new HashMap<String, String>();
		String line;
		String content = null;
		String subjectId = null;
		while ((line = resultFileReader.readLine()) != null) {

			if(line.contains("Lambda")) {
				if(content != null) {
					if(!results.containsKey(subjectId))
						results.put(subjectId, content);
					else {
						String saved = results.get(subjectId);
						results.put(subjectId, saved + content);
					}
				}
				
				content = null;
			}
			
			if(line.startsWith(">")) {
				
				if(subjectId == null)
					subjectId = line.replace(">", "").trim();
				
				if(content != null) {
					if(!results.containsKey(subjectId)) {
						results.put(subjectId, content);
					} else {
						String saved = results.get(subjectId);
						results.put(subjectId, saved + content);
					}
				}
				subjectId = line.replace(">", "").trim();
				content = line + System.lineSeparator();
			}else if(content != null) {
				content += line + System.lineSeparator();
			}

			// 마지막꺼...
		}
		resultFileReader.close();

		return results;
	}

	@Override
	public List<NgsDataAchive> selectAchiveByIds(List<AchiveHeaderXref> xref) throws Exception {
		return blastDAO.selectAchiveByIds(xref);
	}

	@Override
	public List<NgsDataFeaturesHeader> selectHeaderByLocus(String locus) {
		return blastDAO.selectHeaderByLocus(locus);
	}

	@Override
	public List<AchiveHeaderXref> selectAchiveHeaderXrefByHeaderId(List<NgsDataFeaturesHeader> headers) {
		return blastDAO.selectAchiveHeaderXrefByHeaderId(headers);
	}

	@Override
	public List<NgsDataFeatures> selectFeaturesByHeaderIds(Map<String, Object> params) {
		return blastDAO.selectFeaturesByHeaderIds(params);
	}

	@Override
	public Integer countFeaturesByHeaderIds(Map<String, Object> params) {
		return blastDAO.countFeaturesByHeaderIds(params);
	}

	@Override
	public AchiveBlastSequence selectAchiveIdBySequenceName(String name) {
		return blastDAO.selectAchiveIdBySequenceName(name);
	}

	@Override
	public NgsDataAchive selectAchiveById(Integer achiveId) {
		return blastDAO.selectAchiveById(achiveId);
	}

	@Override
	public NgsDataAchive selectAchiveByRegistNo(String registNo) {
		return blastDAO.selectAchiveByRegistNo(registNo);
	}

}
