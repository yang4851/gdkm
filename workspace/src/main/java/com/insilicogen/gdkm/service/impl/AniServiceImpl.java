package com.insilicogen.gdkm.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.Executor;
import org.apache.commons.exec.PumpStreamHandler;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.insilicogen.gdkm.Globals;
import com.insilicogen.gdkm.dao.AniDAO;
import com.insilicogen.gdkm.model.AniFile;
import com.insilicogen.gdkm.model.AniParameter;
import com.insilicogen.gdkm.model.AniResult;
import com.insilicogen.gdkm.model.NgsDataAchive;
import com.insilicogen.gdkm.model.NgsFileSeqQuantity;
import com.insilicogen.gdkm.service.AniService;
import com.insilicogen.gdkm.service.NgsDataAchiveService;
import com.insilicogen.gdkm.util.CommandExecuteUtils;
import com.insilicogen.gdkm.util.NgsFileUtils;

@Service("AniService")
public class AniServiceImpl implements AniService{
	
	@Resource(name="AniDAO")
	private AniDAO aniDAO;

	@Override
	public int run(OutputStream outputStream, OutputStream errorStream, AniParameter parameter) throws Exception {
		
		if(outputStream == null || errorStream == null)
			throw new IllegalArgumentException("필수 인자가 비었습니다.");
		
		String jarPath = "/tools/gdkm-ani-cli.jar";
		
		CommandLine command = new CommandLine("java");
		
		command.addArgument("-jar");
		command.addArgument(jarPath);
		command.addArgument("-i");
		command.addArgument(parameter.getInputFileDir());
		command.addArgument("-m");
		command.addArgument(parameter.getMethod());
		
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
			System.out.println("outputStream");
			System.out.println(outputStream);
			System.out.println("errorStream");
			System.out.println(errorStream);
			throw e;
		}
	}
	
	@Override
	public AniResult resultTabFileParse(File target) throws Exception {
		
		if(!target.exists() || !target.isFile())
			throw new Exception("결과 파일이 존재하지 않습니다.");
		
		AniResult aniResult = new AniResult();
		List<String> sources = new ArrayList<String>();
		List<List<String>> targets = new ArrayList<List<String>>();
		
		BufferedReader resultFileReader = new BufferedReader(new FileReader(target));
		String line;
		while((line = resultFileReader.readLine()) != null){
			
			if(line.startsWith("\t")) {
				String[] sourceArr = line.split("\t");
				for(String sourceStr : sourceArr) {
					if(!sourceStr.trim().isEmpty())
						sources.add(sourceStr);
				}
			} else {
				String[] targetArr = line.split("\t");
				List<String> row = new ArrayList<String>();
				for(String targetStr : targetArr) {
					if(!targetStr.trim().isEmpty())
						row.add(targetStr);
				}
				targets.add(row);
			}
			aniResult.setSources(sources);
			aniResult.setTargets(targets);
			
		}
		resultFileReader.close();
		return aniResult;
	}
	
	@Override
	public List<AniFile> selectAchiveOfFasta(Map<String, Object> params) throws Exception {
		return aniDAO.selectAchiveOfFasta(params);
	}
	
	@Override
	public Integer countAchiveOfFasta(Map<String, Object> params) throws Exception {
		return aniDAO.countAchiveOfFasta(params);
	}
	
	@Override
	public NgsFileSeqQuantity executeClcAssemblyCell(File target) {

		System.out.println("CLCAssemblyCell 실행 시작 : " + target.getAbsolutePath());

		File executable = NgsFileUtils.getClcAssemblyCellFile();
		CommandLine command = new CommandLine(executable);
		command.addArgument("-n");
		command.addArgument("-r");
		command.addArgument(target.getAbsolutePath());

		try {
			String output = CommandExecuteUtils.executeStdOut(command);
			NgsFileSeqQuantity quantity = NgsFileProcessServiceImpl.parseSequenceInfo(output);

			System.out.println("CLCAssemblyCell 실행 완료 : " + target.getAbsolutePath());
			System.out.println(output);
			return quantity; 
		} catch (Exception e) {
			System.out.println("CLCAssemblyCell 실행 실패 : " + target.getAbsolutePath() + "-" + e.getMessage());
			throw new RuntimeException();
		}
		
	}
}
