package com.insilicogen.gdkm.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.insilicogen.gdkm.model.NgsDataRegist;
import com.insilicogen.gdkm.model.Research;

import egovframework.rte.psl.dataaccess.EgovAbstractMapper;

@Repository("ResearchNgsDataDAO")
public class ResearchNgsDataDAO extends EgovAbstractMapper{

	public int insertNgsData(Research research, NgsDataRegist data) {
		if(research == null || research.getId() < 1) 
			throw new IllegalArgumentException("Research data is null.");
		
		if(data == null || data.getId() < 1)
			throw new IllegalArgumentException("Ngs data is null.");
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("research", research);
		params.put("data", data);
		
		return insert("ResearchNgsDataRegist.insert", params);
	}
	
	public List<NgsDataRegist> selectNgsDataList(Research research) throws Exception {
		if(research == null || research.getId() < 1) 
			throw new IllegalArgumentException("Research data is null.");
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("researchId", research.getId());
		
		return selectNgsDataList(params);
	}
	
	public List<NgsDataRegist> selectNgsDataList(Research research, NgsDataRegist data) throws Exception {
		if(research == null || research.getId() < 1) 
			throw new IllegalArgumentException("Research data is null.");
		
		if(data == null || data.getId() < 1)
			throw new IllegalArgumentException("Ngs data is null.");
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("researchId", research.getId());
		params.put("dataId", data.getId());
		
		return selectNgsDataList(params);
	}
	
	public List<NgsDataRegist> selectNgsDataList(Map<String, Object> params) throws Exception {
		return selectList("ResearchNgsDataRegist.select", params);
	}
	
	public int selectNgsDataCount(Research research) throws Exception {
		if(research == null || research.getId() < 1) 
			throw new IllegalArgumentException("Research data is null.");
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("researchId", research.getId());
		
		return selectNgsDataCount(params);
	}
	
	public int selectNgsDataCount(Research research, NgsDataRegist data) throws Exception {
		if(research == null || research.getId() < 1) 
			throw new IllegalArgumentException("Research data is null.");
		
		if(data == null || data.getId() < 1)
			throw new IllegalArgumentException("Ngs data is null.");
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("researchId", research.getId());
		params.put("dataId", data.getId());
		
		return selectNgsDataCount(params);
	}
	
	public int selectNgsDataCount(Map<String, Object> params) throws Exception {
		return selectOne("ResearchNgsDataRegist.count", params);
	}
	
	public int deleteNgsData(Research research) throws Exception {
		if(research == null || research.getId() < 1) 
			throw new IllegalArgumentException("Research data is null.");
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("researchId", research.getId());
		
		return deleteNgsData(params);
	}
	
	public int deleteNgsData(Research research, NgsDataRegist data) throws Exception {
		if(research == null || research.getId() < 1) 
			throw new IllegalArgumentException("Research data is null.");
		
		if(data == null || data.getId() < 1)
			throw new IllegalArgumentException("Ngs data is null.");
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("researchId", research.getId());
		params.put("dataId", data.getId());
		
		return deleteNgsData(params);
	}
	
	public int deleteNgsData(Map<String, Object> params) throws Exception {
		if(params == null || params.isEmpty())
			throw new IllegalArgumentException("Invalid remove options.");
		
		return delete("ResearchNgsDataRegist.delete", params);
	}
}
