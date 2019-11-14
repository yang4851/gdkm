package com.insilicogen.gdkm.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.insilicogen.gdkm.model.AchiveHeaderXref;
import com.insilicogen.gdkm.model.NgsDataAchive;
import com.insilicogen.gdkm.model.NgsDataFeaturesHeader;

import egovframework.rte.psl.dataaccess.EgovAbstractMapper;

@Repository("NgsDataFeaturesHeaderDAO")
public class NgsDataFeaturesHeaderDAO extends EgovAbstractMapper{
	
	public int insertHeader(NgsDataFeaturesHeader header){
		return insert("NgsDataFeaturesHeader.insert", header);
	}
	
	public int deleteHeader(int header_id){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("headerId", header_id);
		
		return delete("NgsDataFeaturesHeader.delete", params);
	}
	
	public int deleteHeader(List<Integer> headerIdList){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("headerIdList", headerIdList);
		
		return delete("NgsDataFeaturesHeader.delete", params);
	}
	
	public int updateHeader(NgsDataFeaturesHeader header){
		return update("NgsDataFeaturesHeader.update", header);
	}
	
	public NgsDataFeaturesHeader selectOneHeader(int header_id){
		return selectOne("NgsDataFeaturesHeader.selectOne", header_id);
	}
	
	public List<NgsDataFeaturesHeader> selectOneHeaderOfAchive(int achive_id){
		return selectList("NgsDataFeaturesHeader.selectOneOfAchive", achive_id);
	}
	
	public List<NgsDataFeaturesHeader> selectListHeader(){
		return selectList("NgsDataFeaturesHeader.selectList");
	}
	
	public int insertAchiveHeaderXref(AchiveHeaderXref ahxref){
		return insert("AchiveHeaderXref.insert", ahxref);
	}
	
	public int deleteAchiveHeaderXref(int ahxrefId){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ahxrefId", ahxrefId);
		
		return delete("AchiveHeaderXref.delete", params);
	}
	
	public int deleteAchiveHeaderXref(NgsDataAchive achive){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("achiveId", achive.getId());
		
		return delete("AchiveHeaderXref.delete", params);
	}
	
	public int deleteAchiveHeaderXref(NgsDataFeaturesHeader header){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("headerId", header.getHeaderId());
		
		return delete("AchiveHeaderXref.delete", params);
	}
	
	public  int updateAchiveHeaderXref(AchiveHeaderXref ahxref){
		return update("AchiveHeaderXref.update", ahxref);
	}
	
	public AchiveHeaderXref selectOneAchiveHeaderXref(int ahxref_id){
		return selectOne("AchiveHeaderXref.selectOne", ahxref_id);
	}
	
	public List<AchiveHeaderXref> selectListAchiveHeaderXref(){
		return selectList("AchiveHeaderXref.selectList");
	}
	
	public List<Integer> selectHeaderIdList(NgsDataAchive achive) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("achiveId", achive.getId());
		
		return selectList("AchiveHeaderXref.selectIdList", params);
	}
}
