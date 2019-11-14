package com.insilicogen.gdkm.service;

import java.util.List;

import com.insilicogen.gdkm.model.NgsDataAchive;
import com.insilicogen.gdkm.model.NgsFileQcResult;
import com.insilicogen.gdkm.model.NgsFileQcSummary;

/**
 * NGS 시퀀싱 파일(fastq)파일 정량정보 관리 서비스 
 */
public interface NgsFileQcService {

	public NgsFileQcResult createNgsFileQcResult(NgsFileQcResult result) throws Exception;
	
	public NgsFileQcResult changeNgsFileQcResult(NgsFileQcResult result) throws Exception;
	
	public NgsFileQcResult getNgsFileQcResult(Integer resultId) throws Exception;
	
	public List<NgsFileQcResult> getNgsFileQcResultList(NgsDataAchive achive) throws Exception;
	
	public int getNgsFileQcResultCount(NgsDataAchive achive) throws Exception;
	
	public int deleteNgsFileQcResult(NgsFileQcResult result) throws Exception;
	
	public int deleteNgsFileQcResult(NgsDataAchive achive) throws Exception;
	
	public NgsFileQcSummary createNgsFileQcSummary(NgsFileQcSummary summary) throws Exception;
	
	public NgsFileQcSummary changeNgsFileQcSummary(NgsFileQcSummary summary) throws Exception;
	
	public NgsFileQcSummary getNgsFileQcSummary(Integer summaryId) throws Exception;
	
	public List<NgsFileQcSummary> getNgsFileQcSummaryList(NgsDataAchive achive) throws Exception;
	
	public int getNgsFileQcSummaryCount(NgsDataAchive achive) throws Exception;
	
	public int deleteNgsFileQcSummary(NgsFileQcSummary summary) throws Exception;
	
	public int deleteNgsFileQcSummary(NgsDataAchive achive) throws Exception;
	
}
