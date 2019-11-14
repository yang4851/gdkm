package com.insilicogen.gdkm.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.insilicogen.gdkm.model.NgsDataFeatures;
import com.insilicogen.gdkm.model.NgsDataFeaturesXref;

import egovframework.rte.psl.dataaccess.EgovAbstractMapper;


@Repository("NgsDataFeaturesXrefDAO")
public class NgsDataFeaturesXrefDAO extends EgovAbstractMapper{
	
	public int insertXref(NgsDataFeaturesXref xref){
		return insert("NgsDataFeaturesXref.insert", xref);
	}
	
	public int deleteXref(int xref_id){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("xrefId", xref_id);
		
		return delete("NgsDataFeaturesXref.delete", params);
	}
	
	public int deleteXref(NgsDataFeatures feature){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("featuresId", feature.getFeaturesId());
		
		return delete("NgsDataFeaturesXref.delete", params);
	}
	
	public int deleteXref(List<Integer> featureIdList){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("featureIdList", featureIdList);
		
		return delete("NgsDataFeaturesXref.delete", params);
	}
	
	public int updateXref(NgsDataFeaturesXref xref){
		return update("NgsDataFeaturesXref.update", xref);
	}
	
	public NgsDataFeaturesXref selectOneXref(int xref_id){
		return selectOne("NgsDataFeaturesXref.selectOne", xref_id);
	}
	
	public List<NgsDataFeaturesXref> selectListXref(){
		return selectList("NgsDataFeaturesXref.selectList");
	}
}
