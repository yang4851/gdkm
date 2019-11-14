package com.insilicogen.gdkm.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.insilicogen.gdkm.model.AchiveBlastSequence;
import com.insilicogen.gdkm.model.AchiveHeaderXref;
import com.insilicogen.gdkm.model.NgsDataAchive;
import com.insilicogen.gdkm.model.NgsDataFeatures;
import com.insilicogen.gdkm.model.NgsDataFeaturesHeader;

import egovframework.rte.psl.dataaccess.EgovAbstractMapper;

@Repository("BlastDAO")
public class BlastDAO extends EgovAbstractMapper{
	
	public List<NgsDataAchive> selectAchiveByIds(List<AchiveHeaderXref> xref) throws Exception {
		Map<String, List<AchiveHeaderXref>> param = new HashMap<String, List<AchiveHeaderXref>>();
		param.put("achives", xref);
		
		List<NgsDataAchive> achives = selectList("Tools.selectAchiveByIds", param);
		return achives;
	}
	
	public List<NgsDataFeatures> selectFeaturesByHeaderIds(Map<String, Object> params) {
		List<NgsDataFeatures> features = selectList("Tools.selectFeaturesByHeaderIds", params);
		
		return features;
	} 
	
	public Integer countFeaturesByHeaderIds(Map<String, Object> params) {
		Integer total = selectOne("Tools.countFeaturesByHeaderIds", params);
		return total;
	} 
	
	public List<NgsDataFeaturesHeader> selectHeaderByLocus(String locus) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("locus", locus);
		
		return selectList("Tools.selectHeaderByLocus", params);
	}
	
	public List<AchiveHeaderXref> selectAchiveHeaderXrefByHeaderId(List<NgsDataFeaturesHeader> headers) {
		Map<String, List<NgsDataFeaturesHeader>> params = new HashMap<String, List<NgsDataFeaturesHeader>>();
		params.put("headers", headers);
		
		return selectList("Tools.selectAchiveHeaderXrefByHeaderId", params);
	}
	
	public AchiveBlastSequence selectAchiveIdBySequenceName(String name) {
		Map<String, String> params = new HashMap<>();
		params.put("sequenceName", name);
		
		return selectOne("Tools.selectAchiveIdBySequenceName", params);
	}
	
	public NgsDataAchive selectAchiveById(Integer achiveId) {
		Map<String, Integer> params = new HashMap<String, Integer>();
		params.put("achiveId", achiveId);
		
		NgsDataAchive achive = selectOne("Tools.selectAchiveById", params);
		return achive;
	}
	
	public NgsDataAchive selectAchiveByRegistNo(String registNo) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("registNo", registNo);
		
		NgsDataAchive achive = selectOne("Tools.selectAchiveByRegistNo", params);
		return achive;
	}
}
