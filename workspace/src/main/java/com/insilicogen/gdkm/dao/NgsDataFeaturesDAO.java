package com.insilicogen.gdkm.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.insilicogen.gdkm.model.AchiveFeaturesXref;
import com.insilicogen.gdkm.model.NgsDataAchive;
import com.insilicogen.gdkm.model.NgsDataFeatures;
import com.insilicogen.gdkm.model.NgsDataFeaturesHeader;

import egovframework.rte.psl.dataaccess.EgovAbstractMapper;

@Repository("NgsDataFeaturesDAO")
public class NgsDataFeaturesDAO extends EgovAbstractMapper{
	
	public int insertFeatures(NgsDataFeatures features){
		return insert("NgsDataFeatures.insert", features);
	}
	
	public int deleteFeatures(int features_id){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("featuresId", features_id);
		
		return delete("NgsDataFeatures.delete", params);
	}
	
	public int deleteFeatures(NgsDataFeaturesHeader header){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("headerId", header.getHeaderId());
		
		return delete("NgsDataFeatures.delete", params);
	}
	
	public int deleteFeatures(List<Integer> featureIdList){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("featureIdList", featureIdList);
		
		return delete("NgsDataFeatures.delete", params);
	}
	
	public int updateFeatures(NgsDataFeatures features){
		return update("NgsDataFeatures.update", features);
	}
	
	public NgsDataFeatures selectOneFeatures(int features_id){
		return selectOne("NgsDataFeatures.selectOne", features_id);
	}
	
	public List<NgsDataFeatures> selectListFeatures(Map<String, Object> params){
		return selectList("NgsDataFeatures.selectList", params);
	}
	
	public int selectCountFeatures(Map<String, Object> params){
		return selectOne("NgsDataFeatures.selectCount", params);
	}
	
	public List<NgsDataFeatures> selectListHeaderFeatures(int header_id){
		return selectList("NgsDataFeatures.selectList_header", header_id);
	}
	
	public int insertAchiveFeaturesXref(AchiveFeaturesXref afxref){
		return insert("AchiveFeaturesXref.insert", afxref);
	}
	
	public int deleteAchiveFeaturesXref(int afxref_id){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("afxrefId", afxref_id);
		
		return delete("AchiveFeaturesXref.delete", params);
	}
	
	public int deleteAchiveFeaturesXref(NgsDataAchive achive){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("achiveId", achive.getId());
		
		return delete("AchiveFeaturesXref.delete", params);
	}
	
	public int deleteAchiveFeaturesXref(NgsDataFeatures feature){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("featuresId", feature.getFeaturesId());
		
		return delete("AchiveFeaturesXref.delete", params);
	}
	
	public int updateAchiveFeaturesXref(AchiveFeaturesXref afxref){
		return update("AchiveFeaturesXref.update", afxref);
	}
	
	public AchiveFeaturesXref selectOneAchiveFeaturesXref(int afxref_id){
		return selectOne("AchiveFeaturesXref.selectOne", afxref_id);
	}
	
	public List<AchiveFeaturesXref> selectListAchiveFeaturesXref(){
		return selectList("AchiveFeaturesXref.selectList");
	}
	
	public List<Integer> selectFeatureIdList(NgsDataAchive achive) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("achiveId", achive.getId());
		
		return selectList("AchiveFeaturesXref.selectIdList", params);
	}
	
	public List<Integer> selectFeatureIdList(NgsDataFeaturesHeader header) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("headerId", header.getHeaderId());
		
		return selectList("NgsDataFeatures.selectIdList", params);
	}
}
