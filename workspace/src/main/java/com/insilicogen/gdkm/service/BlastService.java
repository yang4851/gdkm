package com.insilicogen.gdkm.service;

import java.io.File;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import com.insilicogen.gdkm.model.AchiveBlastSequence;
import com.insilicogen.gdkm.model.AchiveHeaderXref;
import com.insilicogen.gdkm.model.BlastEvent;
import com.insilicogen.gdkm.model.BlastResult;
import com.insilicogen.gdkm.model.NgsDataAchive;
import com.insilicogen.gdkm.model.NgsDataFeatures;
import com.insilicogen.gdkm.model.NgsDataFeaturesHeader;
import com.insilicogen.gdkm.model.NgsDataRegist;

public interface BlastService {
	
	public int run(OutputStream outputStream, OutputStream errorStream, BlastEvent event) throws Exception;
	
	public String FormatDbParse(String[] target) throws Exception;
	
	public void fileWrite(String content, File file);
	
	public List<BlastResult> blastResultTabFileParse(File outputFile) throws Exception;
	
	public Map<String, String> blastResultPairwiseParse(File outputFile) throws Exception;
	
	public List<NgsDataAchive> selectAchiveByIds(List<AchiveHeaderXref> xref) throws Exception;
	public List<NgsDataFeaturesHeader> selectHeaderByLocus(String locus);
	public List<AchiveHeaderXref> selectAchiveHeaderXrefByHeaderId(List<NgsDataFeaturesHeader> headers);
	public List<NgsDataFeatures> selectFeaturesByHeaderIds(Map<String, Object> params);
	public Integer countFeaturesByHeaderIds(Map<String, Object> params);
	
	public AchiveBlastSequence selectAchiveIdBySequenceName(String name);
	public NgsDataAchive selectAchiveById(Integer achiveId);
	public NgsDataAchive selectAchiveByRegistNo(String registNo);
	
}
