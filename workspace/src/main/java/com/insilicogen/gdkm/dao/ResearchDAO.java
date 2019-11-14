package com.insilicogen.gdkm.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.insilicogen.gdkm.model.NgsDataRegist;
import com.insilicogen.gdkm.model.Research;

import egovframework.rte.psl.dataaccess.EgovAbstractMapper;

@Repository("ResearchDAO")
public class ResearchDAO extends EgovAbstractMapper{

	public int insertResearch(Research research) throws Exception {
		if(research == null) 
			throw new NullPointerException("Research data is null.");
		
		return insert("Research.insert", research);
	}
	
	public Research selectResearch(int researchId) throws Exception {
		if(researchId < 1)
			throw new IllegalArgumentException("Invalid research ID-" + researchId);
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("researchId", researchId);
		
		return selectOne("Research.get", params);
	}
	
	public int updateResearch(Research research) throws Exception {
		if(research == null) 
			throw new NullPointerException("Research data is null.");
		
		return update("Research.update", research);
	}
	
	public int deleteResearch(Research research) throws Exception {
		if(research == null) 
			throw new NullPointerException("Research data is null.");
		
		if(research.getId() < 1)
			throw new IllegalArgumentException("Invalid research data - ID is " + research.getId());
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("researchId", research.getId());
		
		return delete("Research.delete", params);
	}
	
	public List<Research> selectResearchList(NgsDataRegist regist) throws Exception {
		if(regist == null || regist.getId() < 1) 
			throw new IllegalArgumentException("NGS data is null.");
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("registId", regist.getId());
		
		return selectResearchList(params);
	}
	
	public List<Research> selectResearchList(Map<String, Object> params) throws Exception {
		if(params == null) 
			params = new HashMap<String, Object>();
		
		return selectList("Research.select", params);
	}
	
	public int selectResearchCount(NgsDataRegist regist) throws Exception {
		if(regist == null || regist.getId() < 1) 
			throw new IllegalArgumentException("NGS data is null.");
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("registId", regist.getId());
		
		return selectResearchCount(params);
	}
	
	public int selectResearchCount(Map<String, Object> params) throws Exception {
		if(params == null) 
			params = new HashMap<String, Object>();
		
		return selectOne("Research.count", params);
	}
}
