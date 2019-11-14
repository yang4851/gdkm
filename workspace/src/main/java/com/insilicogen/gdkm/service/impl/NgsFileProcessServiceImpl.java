package com.insilicogen.gdkm.service.impl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.biojava.nbio.core.sequence.DNASequence;
import org.biojava.nbio.core.sequence.compound.NucleotideCompound;
import org.biojava.nbio.core.sequence.features.FeatureInterface;
import org.biojava.nbio.core.sequence.io.GenbankReaderHelper;
import org.biojava.nbio.core.sequence.template.AbstractSequence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.insilicogen.gdkm.Globals;
import com.insilicogen.gdkm.exception.NgsDataException;
import com.insilicogen.gdkm.exception.NgsFileProcessException;
import com.insilicogen.gdkm.model.AchiveFeaturesXref;
import com.insilicogen.gdkm.model.AchiveHeaderXref;
import com.insilicogen.gdkm.model.NgsDataAchive;
import com.insilicogen.gdkm.model.NgsDataAchiveLog;
import com.insilicogen.gdkm.model.NgsDataFeatures;
import com.insilicogen.gdkm.model.NgsDataFeaturesHeader;
import com.insilicogen.gdkm.model.NgsDataFeaturesXref;
import com.insilicogen.gdkm.model.NgsDataRegist;
import com.insilicogen.gdkm.model.NgsFileAnnotation;
import com.insilicogen.gdkm.model.NgsFileQcResult;
import com.insilicogen.gdkm.model.NgsFileQcSummary;
import com.insilicogen.gdkm.model.NgsFileSeqQuantity;
import com.insilicogen.gdkm.service.NgsDataAchiveLogService;
import com.insilicogen.gdkm.service.NgsDataAchiveService;
import com.insilicogen.gdkm.service.NgsDataFeaturesHeaderService;
import com.insilicogen.gdkm.service.NgsDataFeaturesService;
import com.insilicogen.gdkm.service.NgsDataFeaturesXrefService;
import com.insilicogen.gdkm.service.NgsDataRegistService;
import com.insilicogen.gdkm.service.NgsFileAnnotationService;
import com.insilicogen.gdkm.service.NgsFileProcessService;
import com.insilicogen.gdkm.service.NgsFileQcService;
import com.insilicogen.gdkm.service.NgsFileSeqService;
import com.insilicogen.gdkm.util.CommandExecuteUtils;
import com.insilicogen.gdkm.util.EgovProperties;
import com.insilicogen.gdkm.util.NgsFileUtils;
import com.insilicogen.gdkm.util.ZipFileUtils;

@Service("NgsFileProcessService")
public class NgsFileProcessServiceImpl implements NgsFileProcessService {

	static Logger logger = LoggerFactory.getLogger(NgsFileProcessServiceImpl.class);

	@Resource(name = "messageSource")
	private MessageSource messageSource;

	@Resource(name = "NgsDataRegistService")
	private NgsDataRegistService registService;

	@Resource(name = "NgsDataAchiveService")
	private NgsDataAchiveService achiveService;

	@Resource(name = "NgsDataAchiveLogService")
	private NgsDataAchiveLogService logService;

	@Resource(name = "NgsFileQcService")
	private NgsFileQcService qcService;

	@Resource(name = "NgsFileSeqService")
	private NgsFileSeqService seqService;

	@Resource(name = "NgsFileAnnotationService")
	private NgsFileAnnotationService annotationService;

	@Resource(name = "NgsDataFeaturesHeaderService")
	private NgsDataFeaturesHeaderService headerService;

	@Resource(name = "NgsDataFeaturesService")
	private NgsDataFeaturesService featuresService;

	@Resource(name = "NgsDataFeaturesXrefService")
	private NgsDataFeaturesXrefService xrefService;

	@Autowired
	PlatformTransactionManager txManager;

	protected TransactionStatus getTransaction() {
		DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
		definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);

		return txManager.getTransaction(definition);
	}
	
	protected void rollbackTransaction(TransactionStatus txStatus) {
		try {
			txManager.rollback(txStatus);
		} catch(Exception e) {
			logger.error(e.getMessage());
		}
	}

	@Override
	@Async("RegistValidator")
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void validateNgsData(List<NgsDataRegist> registList) throws Exception {
		for(NgsDataRegist regist : registList) {
			logger.info("NGS 데이터 검증 시작 - " + regist);
			changeRegistStatus(regist, Globals.STATUS_REGIST_VALIDATING);
	
			try {
				List<NgsDataAchive> achiveList = achiveService.getNgsDataAchiveList(regist);
				if (achiveList.size() > 0) {
					for (NgsDataAchive achive : achiveList) {
						validateAchiveFile(achive);
					}
				} else {
					logger.warn("NGS 데이터 검증 파일 없음 - " + regist);
					changeRegistStatus(regist, Globals.STATUS_REGIST_READY);
					String dataType = messageSource.getMessage("ENTITY.NGSDATAFILE", null, Locale.US);
					String message = messageSource.getMessage("ERR009", new String[] { dataType }, Locale.US); 
					throw new NgsFileProcessException(message, HttpStatus.NOT_FOUND);
				}
	
				setRegistDataStatus(regist);
				logger.info("NGS 데이터 검증 완료 - " + regist);
			} catch (Exception e) {
				changeRegistStatus(regist, Globals.STATUS_REGIST_ERROR);
				logger.info("NGS 데이터 검증 오류 - " + e.getMessage());
			}
		}
	}
	
	/**
	 * 성과물 파일에 대한 검증완료 후 등록정보 상태를 변경 
	 * 모든 성과물 파일에 대한 검증 상태정보를 확인하고 오류가 하나라도 있는 경우는 오류 상태가 되어야 하고, 
	 * 검증 중인 상태의 것이 하나라도 있는 경우는 검증 중 상태가 됨. 
	 * 성공한 파일이 전혀 없는 경우는 준비 상태로 변경.
	 *  
	 * @param regist
	 */
	private void setRegistDataStatus(NgsDataRegist regist) {
		try {
			int errorCount = 0; 
			int successCount = 0;
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
				} else {
					successCount++;
				}
			}
			
			String registStatus = (errorCount > 0) ? Globals.STATUS_REGIST_ERROR : 
				(runningCount > 0) ? Globals.STATUS_REGIST_VALIDATING : 
					(readyCount > 0) ? Globals.STATUS_REGIST_READY : 
						(successCount > 0) ? Globals.STATUS_REGIST_VALIDATED : Globals.STATUS_REGIST_READY; 
			changeRegistStatus(regist, registStatus);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	@Override
	@Async("AchiveValidator")
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void validateAchiveFile(NgsDataAchive achive) {
		File realFile = NgsFileUtils.getPhysicalFile(achive);
		if (!realFile.exists()) {
			achive.setVerifiError(String.format("NGS file not found(%s)", realFile.getName()));
			changeVerifyStatus(achive, Globals.STATUS_VERIFY_FAILED);
			logger.error("NGS 파일 검증 실패(파일 검색 실패) - " + achive);
			String dataType = messageSource.getMessage("ENTITY.NGSDATAFILE", null, Locale.US);
			String message = messageSource.getMessage("ERR009", new String[] { dataType }, Locale.US); 
			throw new NgsFileProcessException(message, HttpStatus.NOT_FOUND);
		}

		changeVerifyStatus(achive, Globals.STATUS_VERIFY_RUNNING);
		
		if (NgsFileUtils.isZippedFile(realFile)) {
			List<NgsDataAchive> unzippedList = unzipAchiveFile(achive);
			for (NgsDataAchive child : unzippedList) {
				validateAchiveFile(child);
			}

			// TODO 완료되면 성과물 정보를 삭제해야 하는지 정책 결정 필요
		} else {
			if (NgsFileUtils.isFastaFile(realFile) || NgsFileUtils.isFastqFile(realFile)) {
				executeClcAssemblyCell(achive);
			}

			if (NgsFileUtils.isFastqFile(realFile)) {
				executeFastQC(achive);
			}

			if (NgsFileUtils.isGenbankFile(realFile)) {
				parseGenbankFile(achive);
			}

			if (NgsFileUtils.isGffFile(realFile)) {
				buildBrowserTrack(achive);
			}

			if (NgsFileUtils.isH5File(realFile) || !NgsFileUtils.isNgsAchiveFile(realFile)) {
				changeVerifyStatus(achive, Globals.STATUS_VERIFY_SUCCESS);
			}
		}
	}

	/**
	 * 압축파일 압축 해제 작업 수행
	 * 압축이 풀리면 해당 압축파일 정보는 삭제 
	 */
	@Override
	@Async("UnzipExecutor")
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public List<NgsDataAchive> unzipAchiveFile(NgsDataAchive achive) {
		File zippedFile = NgsFileUtils.getPhysicalFile(achive);
		File destDir = NgsFileUtils.getUnzippedDir(achive);
		List<NgsDataAchive> childList = new ArrayList<NgsDataAchive>();

		logger.info("압축파일 해제 시작 : " + zippedFile.getAbsolutePath() + " => " + destDir.getAbsolutePath());
		changeVerifyStatus(achive, Globals.STATUS_VERIFY_RUNNING);
		createProcessLog(achive, Globals.PROCESS_TYPE_UNZIP, Globals.STATUS_VERIFY_RUNNING);

		try {
			List<File> unzippedList = ZipFileUtils.unzip(zippedFile, destDir);

			for (File file : unzippedList) {
				/* 압축 해제 파일 중 성과물로 등록할 수 있는 것만 등록하려 하였으나 확장자를 마음대로 변경할 수 있기 때문에 조건문 변경   
				if (!NgsFileUtils.isNgsAchiveFile(file))
					continue;
				*/
				TransactionStatus txStatus = getTransaction();
				try {
					NgsDataAchive newAchive = new NgsDataAchive();
					newAchive.setDataRegist(achive.getDataRegist());
					newAchive.setFileName(file.getName());
					newAchive.setFileSize(file.length());
					newAchive.setFileType(NgsFileUtils.getFileType(file));
					newAchive.setChecksum(NgsFileUtils.getMd5Checksum(file));
					newAchive.setRegistUser(achive.getRegistUser());

					childList.add(achiveService.createNgsDataAchive(newAchive, file));
					txManager.commit(txStatus);
				} catch (Exception e) {
					logger.error(e.getMessage());
					rollbackTransaction(txStatus);
				}
			}

			logger.info("압축파일 해제 완료 : " + zippedFile.getAbsolutePath());
			changeVerifyStatus(achive, Globals.STATUS_VERIFY_SUCCESS);
			createProcessLog(achive, Globals.PROCESS_TYPE_UNZIP, Globals.STATUS_VERIFY_SUCCESS);
			
			try {
				achiveService.deleteNgsDataAchive(achive);
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		} catch (IOException e) {
			logger.info("압축파일 해제 실패 : " + zippedFile.getAbsolutePath() + "-" + e.getMessage());
			achive.setVerifiError("Failed decompress file - " + e.getMessage());
			changeVerifyStatus(achive, Globals.STATUS_VERIFY_FAILED);
			createProcessLog(achive, Globals.PROCESS_TYPE_UNZIP, Globals.STATUS_VERIFY_FAILED);
		}

		return childList;
	}

	@Override
	@Async("FastQcExecutor")
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void executeFastQC(NgsDataAchive achive) {
		File fastqFile = NgsFileUtils.getPhysicalFile(achive);
		File destDir = NgsFileUtils.getFastQcOutputDir(achive);

		logger.info("FastQC 실행 시작 : " + fastqFile.getAbsolutePath());
		changeVerifyStatus(achive, Globals.STATUS_VERIFY_RUNNING);
		createProcessLog(achive, Globals.PROCESS_TYPE_FASTQC, Globals.STATUS_VERIFY_RUNNING);

		File executable = NgsFileUtils.getFastQcFile();
		CommandLine command = new CommandLine(executable);
		if (Globals.isWindows()) {
			command = new CommandLine("perl");
			command.addArgument(executable.getAbsolutePath());
		}
		command.addArgument(fastqFile.getAbsolutePath());
		command.addArgument("-o");
		command.addArgument(destDir.getAbsolutePath());

		TransactionStatus txStatus = null;
		try {
			String output = CommandExecuteUtils.executeStdOut(command);
			String dataType = messageSource.getMessage("ENTITY.QCDATA", null, Locale.US);
			
			if (output.indexOf("complete") < 0) {
				throw new RuntimeException(output);
			}

			synchronized(NgsDataAchiveService.Lock) {
				txStatus = getTransaction();
				qcService.deleteNgsFileQcResult(achive);
				List<NgsFileQcResult> resultList = readAndCreateFastQcResult(achive);
				if (resultList.size() == 0) {
					String message = messageSource.getMessage("ERR002", new String[] { dataType }, Locale.US); 
					throw new RuntimeException(message);
				}
				txManager.commit(txStatus);
			}
			
			txStatus = getTransaction();
			qcService.deleteNgsFileQcSummary(achive);
			List<NgsFileQcSummary> summaryList = readAndCreateFastQcSummary(achive);
			if (summaryList.size() == 0) {
				dataType = messageSource.getMessage("ENTITY.QC_SUMMARY", null, Locale.US);
				String message = messageSource.getMessage("ERR002", new String[] { dataType }, Locale.US); 
				throw new RuntimeException(message);
			}
			txManager.commit(txStatus);

			logger.info("FastQC 실행 완료 : " + fastqFile.getAbsolutePath());
			changeVerifyStatus(achive, Globals.STATUS_VERIFY_SUCCESS);
			createProcessLog(achive, Globals.PROCESS_TYPE_FASTQC, Globals.STATUS_VERIFY_SUCCESS);
		} catch (Exception e) {
			rollbackTransaction(txStatus);
			
			logger.info("FastQC 실행 실패 : " + fastqFile.getAbsolutePath() + "-" + e.getMessage());
			achive.setVerifiError("Failed decompress file - " + e.getMessage());
			changeVerifyStatus(achive, Globals.STATUS_VERIFY_FAILED);
			createProcessLog(achive, Globals.PROCESS_TYPE_FASTQC, Globals.STATUS_VERIFY_FAILED);
			
			String dataType = "FastQC";
			String message = messageSource.getMessage("ERR010", new String[] { dataType }, Locale.US); 
			throw new RuntimeException(message);
		}
	}

	@Override
	@Async("ClcAssemblyCellExecutor")
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void executeClcAssemblyCell(NgsDataAchive achive) {
		File ngsFile = NgsFileUtils.getPhysicalFile(achive);

		logger.info("CLCAssemblyCell 실행 시작 : " + ngsFile.getAbsolutePath());
		changeVerifyStatus(achive, Globals.STATUS_VERIFY_RUNNING);
		createProcessLog(achive, Globals.PROCESS_TYPE_SEQINFO, Globals.STATUS_VERIFY_RUNNING);

		File executable = NgsFileUtils.getClcAssemblyCellFile();
		CommandLine command = new CommandLine(executable);
		command.addArgument("-n");
		command.addArgument("-r");
		command.addArgument(ngsFile.getAbsolutePath());

		TransactionStatus txStatus = null;

		try {
			String output = CommandExecuteUtils.executeStdOut(command);
			NgsFileSeqQuantity quantity = parseSequenceInfo(output);
			quantity.setAchive(achive);
			quantity.setRegistUser(achive.getUpdateUser());

			synchronized(NgsDataAchiveService.Lock) {
				txStatus = getTransaction();
				seqService.deleteNgsFileSeqQuantity(achive);
				seqService.createNgsFileSeqQuantity(quantity);
				txManager.commit(txStatus);
			}
			
			logger.info("CLCAssemblyCell 실행 완료 : " + ngsFile.getAbsolutePath());
			changeVerifyStatus(achive, Globals.STATUS_VERIFY_SUCCESS);
			createProcessLog(achive, Globals.PROCESS_TYPE_SEQINFO, Globals.STATUS_VERIFY_SUCCESS);
		} catch (Exception e) {
			rollbackTransaction(txStatus);
			
			txStatus.flush();

			logger.info("CLCAssemblyCell 실행 실패 : " + ngsFile.getAbsolutePath() + "-" + e.getMessage());
			achive.setVerifiError("Failed to decompress file - " + e.getMessage());
			changeVerifyStatus(achive, Globals.STATUS_VERIFY_FAILED);
			createProcessLog(achive, Globals.PROCESS_TYPE_SEQINFO, Globals.STATUS_VERIFY_FAILED);
			
			String dataType = "CLCAssemblyCell";
			String message = messageSource.getMessage("ERR010", new String[] { dataType }, Locale.US); 
			throw new RuntimeException(message);
		}
	}

	/**
	 * TODO .gbk파일에 대한 parsing이 이루어 지는 곳..?
	 */
	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void parseGenbankFile(NgsDataAchive achive) {
		File realFile = NgsFileUtils.getPhysicalFile(achive);

		logger.info("gbk 파일 파싱 시작 : " + realFile.getAbsolutePath());
		changeVerifyStatus(achive, Globals.STATUS_VERIFY_RUNNING);
		createProcessLog(achive, Globals.PROCESS_TYPE_GENBANK, Globals.STATUS_VERIFY_RUNNING);

		TransactionStatus txStatus = null;
		try {
			txStatus = getTransaction();
			
			LinkedHashMap<String, DNASequence> dnaSequences = GenbankReaderHelper.readGenbankDNASequence(realFile, true);
			AchiveHeaderXref ahxref = new AchiveHeaderXref();
			NgsDataFeaturesHeader header = new NgsDataFeaturesHeader();

			for (DNASequence sequence : dnaSequences.values()) {
				header.setAccession(sequence.getAccession().getIdentifier());
				header.setOrganism(sequence.getDescription());
				header.setStart(sequence.getBioBegin());
				header.setEnd(sequence.getBioEnd());
				header.setDate(achive.getRegistDate());

				String[] temp = sequence.getOriginalHeader().replaceAll("\\s{2,}", " ").split(" ");
				header.setLocus(temp[0]);

				if (headerService.createHeader(header) > 0) {

					ahxref.setAchiveId(achive.getId());
					ahxref.setHeaderId(header.getHeaderId());
					headerService.createAchiveHeaderXref(ahxref);

					Iterator<FeatureInterface<AbstractSequence<NucleotideCompound>, NucleotideCompound>> it = sequence.getFeatures().iterator();
					int geneCount = 0;
					int cdsCount = 0;

					while (it.hasNext()) {
						FeatureInterface<AbstractSequence<NucleotideCompound>, NucleotideCompound> ft = it.next();

						NgsDataFeatures features = new NgsDataFeatures();
						NgsDataFeaturesXref xref = new NgsDataFeaturesXref();
						AchiveFeaturesXref afxref = new AchiveFeaturesXref();
						features.setType(ft.getType());

						if (ft.getType().equalsIgnoreCase("CDS")) {
							cdsCount++;
						} else if (ft.getType().equalsIgnoreCase("gene")) {
							geneCount++;
						}

						features.setStart(Integer.parseInt(ft.getLocations().getStart().toString()));
						features.setEnd(Integer.parseInt(ft.getLocations().getEnd().toString()));
						features.setStrand(ft.getLocations().getStrand().toString());

						if (ft.getQualifiers().containsKey("gene")) {
							features.setGene(ft.getQualifiers().get("gene").get(0).getValue());
						}

						if (ft.getQualifiers().containsKey("product")) {
							features.setProduct(ft.getQualifiers().get("product").get(0).getValue());
						}

						if (ft.getQualifiers().containsKey("translation")) {
							features.setSequence(ft.getQualifiers().get("translation").get(0).getValue());
						}

						if (ft.getQualifiers().containsKey("codon_start")) {
							features.setCodonStart(
									Integer.parseInt(ft.getQualifiers().get("codon_start").get(0).getValue()));
						}

						if (ft.getQualifiers().containsKey("EC_number")) {
							features.setEcNumber(ft.getQualifiers().get("EC_number").get(0).getValue());
						}

						if (ft.getQualifiers().containsKey("protein_id")) {
							features.setProteinId(ft.getQualifiers().get("protein_id").get(0).getValue());
						}

						if (ft.getQualifiers().containsKey("transl_table")) {
							features.setTranslTable(
									Integer.parseInt(ft.getQualifiers().get("transl_table").get(0).getValue()));
						}

						if (ft.getQualifiers().containsKey("locus_tag")) {
							features.setLocusTag(ft.getQualifiers().get("locus_tag").get(0).getValue());
						}

						features.setHeaderId(header.getHeaderId());

						// features insert
						int featuresResult = featuresService.createFeatures(features);

						afxref.setAchiveId(achive.getId());
						afxref.setFeaturesId(features.getFeaturesId());

						// afxref insert
						featuresService.createAchiveFeaturesXref(afxref);

						if (featuresResult > 0 && ft.getQualifiers().containsKey("db_xref")) {

							for (int j = 0; j < ft.getQualifiers().get("db_xref").size(); j++) {
								String[] db = ft.getQualifiers().get("db_xref").get(j).toString().split(":");
								xref.setType(db[0]);
								xref.setValue(db[1]);
								xref.setFeaturesId(features.getFeaturesId());

								// xref insert
								if (xrefService.createXref(xref) == 0) {
									break;
								}
							}
						}

					}

					NgsFileAnnotation annotation = new NgsFileAnnotation();
					annotation.setAchive(achive);
					annotation.setCdsCount(cdsCount);
					annotation.setGeneCount(geneCount);
					annotation.setRegistDate(achive.getRegistDate());
					annotation.setRegistUser(achive.getRegistUser());

					annotationService.createNgsFileAnnotation(annotation);
				}
			}

			txManager.commit(txStatus);

			logger.info("gbk 파일 파싱 완료 : " + realFile.getAbsolutePath());
			changeVerifyStatus(achive, Globals.STATUS_VERIFY_SUCCESS);
			createProcessLog(achive, Globals.PROCESS_TYPE_GENBANK, Globals.STATUS_VERIFY_SUCCESS);
		} catch (Exception e) {
			rollbackTransaction(txStatus);

			logger.info("gbk 파일 파싱 실패 : " + realFile.getAbsolutePath() + "-" + e.getMessage());
			achive.setVerifiError("Failed to parse gbk file - " + e.getMessage());
			changeVerifyStatus(achive, Globals.STATUS_VERIFY_FAILED);
			createProcessLog(achive, Globals.PROCESS_TYPE_GENBANK, Globals.STATUS_VERIFY_FAILED);
		}
	}

	/**
	 * 1. .gff 파일안에 fasta형식이 포함되어 있는지 확인
	 * 2. gview생성을 위한 gff파일 생성 (fasta를 포함하지 않은 gff파일)
	 */
	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void buildBrowserTrack(NgsDataAchive achive) {
		File realFile = NgsFileUtils.getPhysicalFile(achive);

		logger.info("gff 파일 처리 시작 : " + realFile.getAbsolutePath());
		changeVerifyStatus(achive, Globals.STATUS_VERIFY_RUNNING);
		createProcessLog(achive, Globals.PROCESS_TYPE_SEQINFO, Globals.STATUS_VERIFY_RUNNING);

		TransactionStatus txStatus = null;

		try {
			txStatus = getTransaction();

			List<String> lines = FileUtils.readLines(realFile, Charset.defaultCharset());
			if (!isContigIncluded(lines)){
				String args = messageSource.getMessage("ENTITY.NGSDATAFILE",null,Locale.US);
				String message = messageSource.getMessage("ERR028", new String[]{args}, Locale.US);
				throw new NgsDataException(message, HttpStatus.NOT_FOUND);
			}

			// gview에 사용할 gff파일 생성
			File targetFile = new File(NgsFileUtils.getGviewInputDir(achive), realFile.getName());
			try (FileWriter fw = new FileWriter(targetFile);) {
				for (String line : lines) {
					if (line.startsWith(">")) {
						break;
					}
					line += System.lineSeparator();
					fw.write(line, 0, line.length());
				}

				fw.close();
			}
			
			txManager.commit(txStatus);

			logger.info("gff 파일 처리 완료 : " + realFile.getAbsolutePath());
			changeVerifyStatus(achive, Globals.STATUS_VERIFY_SUCCESS);
			createProcessLog(achive, Globals.PROCESS_TYPE_GENBANK, Globals.STATUS_VERIFY_SUCCESS);
		} catch (Exception e) {
			rollbackTransaction(txStatus);

			logger.info("gff 파일 처리 실패 : " + realFile.getAbsolutePath() + "-" + e.getMessage());
			achive.setVerifiError(e.getMessage());
			changeVerifyStatus(achive, Globals.STATUS_VERIFY_FAILED);
			createProcessLog(achive, Globals.PROCESS_TYPE_GFF, Globals.STATUS_VERIFY_FAILED);
		}

	}
	
	// .gff파일 안에 파스타형식이 있는지 확인 
	private boolean isContigIncluded(List<String> lines) {
		for (String line : lines) {
			if (line.startsWith(">")) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void createJbrowse(NgsDataAchive achive) {
		createJbrowse(achive, NgsFileUtils.getJBrowserTrackFolder(achive));
	}
	
	/**
	 * jbrowse의 data생성
	 */
	@Override
	@Async("JBrowseCellExecutor")
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void createJbrowse(NgsDataAchive achive, File outputDir) {
		File realFile = NgsFileUtils.getPhysicalFile(achive);
		// gff 파일인지 확인
		if (NgsFileUtils.isGffFile(realFile)) {
			File trackFolder = outputDir;
			// 파일 존재여부
			if (!trackFolder.isDirectory()) {
				try {
					logger.info("JBrowser 트랙 생성 실행 : " + realFile.getAbsolutePath());

					// seq파일 생성
					File executable = NgsFileUtils.getPerpareRefSeqPerl();
					CommandLine command = new CommandLine(executable);
					if (Globals.isWindows()) {
						command = new CommandLine("perl");
						command.addArgument(executable.getAbsolutePath());
					}

					command.addArgument("-gff");
					command.addArgument(realFile.getAbsolutePath());
					command.addArgument("--out");
					command.addArgument(trackFolder.getAbsolutePath());

					CommandExecuteUtils.executeStdOut(command);

					// track 생성
					executable = NgsFileUtils.getFlatFileToJsonPerl();
					command = new CommandLine(executable);
					if (Globals.isWindows()) {
						command = new CommandLine("perl");
						command.addArgument(executable.getAbsolutePath());
					}

					command.addArgument("--gff");
					command.addArgument(realFile.getAbsolutePath());
					command.addArgument("--trackLabel");
					command.addArgument(achive.getRegistNo());
					command.addArgument("--out");
					command.addArgument(trackFolder.getAbsolutePath());

					CommandExecuteUtils.executeStdOut(command);

					// names 생성
					executable = NgsFileUtils.getGenerateNamesPerl();
					command = new CommandLine(executable);
					if (Globals.isWindows()) {
						command = new CommandLine("perl");
						command.addArgument(executable.getAbsolutePath());
					}

					command.addArgument("-workdir");
					command.addArgument(trackFolder.getAbsolutePath() + "/names");
					command.addArgument("--out");
					command.addArgument(trackFolder.getAbsolutePath());

					CommandExecuteUtils.executeStdOut(command);

					logger.info("JBrowser 트랙 생성 완료  : " + realFile.getAbsolutePath());
				} catch (Exception e) {
					logger.info("JBrowser 트랙 생성 실패 - " + e.getMessage());
				}
			}
		}else{
			logger.info(".gff형식의 파일이 아닙니다." + realFile.getName());
		}
	}

	private List<NgsFileQcSummary> readAndCreateFastQcSummary(NgsDataAchive achive) throws Exception {
		File summaryFile = NgsFileUtils.getFastQcSummaryFile(achive);
		List<String> lines = FileUtils.readLines(summaryFile, Charset.defaultCharset());

		List<NgsFileQcSummary> summaryList = new ArrayList<NgsFileQcSummary>();

		for (String line : lines) {
			String[] buf = line.split("\t");
			if (buf.length < 3)
				continue;
			NgsFileQcSummary summary = new NgsFileQcSummary();
			summary.setAchive(achive);
			summary.setStatus(buf[0]);
			summary.setSummary(buf[1]);
			summary.setRegistUser(achive.getUpdateUser());

			summaryList.add(qcService.createNgsFileQcSummary(summary));
		}

		return summaryList;
	}

	private List<NgsFileQcResult> readAndCreateFastQcResult(NgsDataAchive achive) throws Exception {
		File destDir = NgsFileUtils.getFastQcOutputDir(achive);
		File[] results = destDir.listFiles();

		List<NgsFileQcResult> resultList = new ArrayList<NgsFileQcResult>();

		for (int i = 0; i < results.length; i++) {
			if (!results[i].isFile())
				continue;

			NgsFileQcResult result = new NgsFileQcResult();
			result.setAchive(achive);
			result.setFileName(results[i].getName());
			result.setFilePath(results[i].getAbsolutePath());
			result.setFileType(FilenameUtils.getExtension(results[i].getName()));
			result.setFileSize(results[i].length());
			result.setSummary(results[i].getName());
			result.setStatus(Globals.STATUS_REGIST_SUCCESS);
			result.setRegistUser(achive.getRegistUser());

			resultList.add(qcService.createNgsFileQcResult(result));

			if (NgsFileUtils.isZippedFile(results[i])) {
				ZipFileUtils.unzip(results[i]);
			}
		}

		return resultList;
	}

	@Override
	public void changeRegistStatus(NgsDataRegist regist, String registStatus) throws Exception {
		regist = registService.getNgsDataRegist(regist.getId());
		regist.setRegistStatus(registStatus);

		synchronized (NgsDataRegistService.Lock) {
			TransactionStatus txStatus = getTransaction();
			registService.changeNgsDataRegist(regist);
			txManager.commit(txStatus);
		}
	}
	
	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void changeVerifyStatus(NgsDataAchive achive1, String verifyStatus) {
		try {
			NgsDataAchive target = achiveService.getNgsDataAchive(achive1.getId());
			if (Globals.STATUS_VERIFY_FAILED.equals(verifyStatus)) {
				target.setRegistStatus(Globals.STATUS_REGIST_FAILED);
				target.setVerifiError(achive1.getVerifiError());
			} else if (Globals.STATUS_VERIFY_SUCCESS.equals(verifyStatus)) {
				target.setRegistStatus(Globals.STATUS_REGIST_VALIDATED);
				target.setVerifiError(null);
			} else if (Globals.STATUS_VERIFY_WARN.equals(verifyStatus)) {
				target.setRegistStatus(Globals.STATUS_REGIST_VALIDATED);
				target.setVerifiError(null);
			} else {
				target.setRegistStatus(Globals.STATUS_REGIST_VALIDATING);
				target.setVerifiError(null);
			}
			target.setVerifiStatus(verifyStatus);
			
			synchronized (NgsDataAchiveService.Lock) {
				TransactionStatus txStatus = getTransaction();
				achiveService.changeNgsDataAchive(target);
				txManager.commit(txStatus);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void createProcessLog(NgsDataAchive achive, String type, String status) {
		try {
			String pattern = "[{3}] {0} to execute {1} {2}.";
			String msg = MessageFormat.format(pattern, status, type, achive.getFileName(), achive.getRegistNo());

			NgsDataAchiveLog log = new NgsDataAchiveLog();
			log.setAchiveId(achive.getId());
			log.setProcessType(type);
			log.setProcessStatus(status);
			log.setProcessLog(msg);
			log.setRegistUser(achive.getUpdateUser());

			synchronized (NgsDataAchiveService.Lock) {
				TransactionStatus txStatus = getTransaction();
				logService.createNgsDataAchiveLog(log);
				txManager.commit(txStatus);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	static NgsFileSeqQuantity parseSequenceInfo(String output) {
		NgsFileSeqQuantity quantity = new NgsFileSeqQuantity();

		String[] lines = output.split(System.getProperty("line.separator"));
		for (int i = 0; i < lines.length; i++) {
			if (lines[i].contains("Number of sequences")) {
				String value = lines[i].replace("Number of sequences", "").trim();
				quantity.setReadCount(Integer.valueOf(value));
			} else if (lines[i].contains("Number of A's")) {
				String[] values = lines[i].replace("Number of A's", "").trim().split("\\s+");
				quantity.setNumberOfA(Integer.valueOf(values[0]));
			} else if (lines[i].contains("Number of C's")) {
				String[] values = lines[i].replace("Number of C's", "").trim().split("\\s+");
				quantity.setNumberOfC(Integer.valueOf(values[0]));
			} else if (lines[i].contains("Number of G's")) {
				String[] values = lines[i].replace("Number of G's", "").trim().split("\\s+");
				quantity.setNumberOfG(Integer.valueOf(values[0]));
			} else if (lines[i].contains("Number of T's")) {
				String[] values = lines[i].replace("Number of T's", "").trim().split("\\s+");
				quantity.setNumberOfT(Integer.valueOf(values[0]));
			} else if (lines[i].contains("Number of N's")) {
				String[] values = lines[i].replace("Number of N's", "").trim().split("\\s+");
				quantity.setNumberOfN(Integer.valueOf(values[0]));
			} else if (lines[i].contains("Total")) {
				String value = lines[i].replace("Total", "").trim();
				quantity.setTotalLength(Integer.parseInt(value));
			} else if (lines[i].contains("Minimum")) {
				// String value = lines[i].replace("Minimum", "").trim();
			} else if (lines[i].contains("Maximum")) {
				// String value = lines[i].replace("Maximum", "").trim();
			} else if (lines[i].contains("Average")) {
				String value = lines[i].replace("Average", "").trim();
				quantity.setAverageLength(Float.parseFloat(value));
			} else if (lines[i].contains("N50")) {
				String value = lines[i].replace("N50", "").trim();
				quantity.setN50(Integer.parseInt(value));
			} else if (lines[i].contains("N80")) {
				// String value = lines[i].replace("N80", "").trim();
				// dose.setN50(Integer.parseInt(value));
			} else if (lines[i].contains("N90")) {
				// String value = lines[i].replace("N90", "").trim();
				// dose.setN50(Integer.parseInt(value));
			}
		}

		return quantity;
	}
	
	/**
	 * jbrowse data파일경로 삭제
	 */
	@Override
	public void startFileDelete() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd, hh:mm:ss a"); 
		System.out.println("JBrowse Track data 파일 삭제 시작 !! - " + sdf.format(new Date()).toString());
		deleteDirectory(new File(EgovProperties.getProperty("TOOL.JBROWSER.PATH")+"/gdkm"));
	}
	
	public boolean deleteDirectory(File path) { 
		if(!path.exists()) { 
			return false; 
		} 
		File[] files = path.listFiles(); 
		for (File file : files) { 
			if (file.isDirectory()) { 
				deleteDirectory(file); 
			} else { 
				file.delete(); 
			} 
		} 
		return path.delete(); 
	}
}
