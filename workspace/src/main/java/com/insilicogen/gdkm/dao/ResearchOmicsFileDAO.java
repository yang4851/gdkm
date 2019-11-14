package com.insilicogen.gdkm.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.insilicogen.gdkm.model.Code;
import com.insilicogen.gdkm.model.Research;
import com.insilicogen.gdkm.model.ResearchOmicsFile;

import egovframework.rte.psl.dataaccess.EgovAbstractMapper;

@Repository("ResearchOmicsFileDAO")
public class ResearchOmicsFileDAO extends EgovAbstractMapper{

	public int insertOmicsFile(ResearchOmicsFile omicsFile) throws Exception {
		if(omicsFile == null)
			throw new NullPointerException("Omics data is null.");
		
		if(omicsFile.getResearch() == null || omicsFile.getResearch().getId() < 1)
			throw new NullPointerException("Research data of omics is null");
		
		return insert("ResearchOmicsFile.insert", omicsFile);
	}
	
	public ResearchOmicsFile selectOmicsFile(int omicsFileId) throws Exception {
		if(omicsFileId < 1)
			throw new IllegalArgumentException("Invalid omics ID-" + omicsFileId);
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("omicsFileId", omicsFileId);
		
		return selectOne("ResearchOmicsFile.get", params);
	}
	
	public int updateOmicsFile(ResearchOmicsFile omicsFile) throws Exception {
		if(omicsFile == null)
			throw new NullPointerException("Omics data is null.");
		
		if(omicsFile.getId() < 1)
			throw new IllegalArgumentException("Invalid omics ID-" + omicsFile.getId());
		
		return update("ResearchOmicsFile.update", omicsFile);
	}
	
	public List<ResearchOmicsFile> selectOmicsFileList(Research research) throws Exception {
		if(research == null || research.getId() < 1) 
			throw new IllegalArgumentException("Invalid research data");
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("researchId", research.getId());
		
		return selectOmicsFileList(params);
	}
	
	public List<ResearchOmicsFile> selectOmicsFileList(Research research, Code category) throws Exception {
		if(research == null || research.getId() < 1) 
			throw new IllegalArgumentException("Invalid research data");
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("researchId", research.getId());
		
		if(category != null && StringUtils.isNotBlank(category.getCode()))
			params.put("categoryCd", category.getCode());
		
		return selectOmicsFileList(params);
	}
	
	public List<ResearchOmicsFile> selectOmicsFileList(Map<String, Object> params) throws Exception {
		if(params == null)
			params = new HashMap<String, Object>();
		
		return selectList("ResearchOmicsFile.select", params);
	}
	
	public int selectOmicsFileCount(Research research) throws Exception {
		if(research == null || research.getId() < 1) 
			throw new IllegalArgumentException("Invalid research data");
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("researchId", research.getId());
		
		return selectOmicsFileCount(params);
	}
	
	public int selectOmicsFileCount(Research research, Code category) throws Exception {
		if(research == null || research.getId() < 1) 
			throw new IllegalArgumentException("Invalid research data");
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("researchId", research.getId());
		
		if(category != null && StringUtils.isNotBlank(category.getCode()))
			params.put("categoryCd", category.getCode());
		
		return selectOmicsFileCount(params);
	}
	
	public int selectOmicsFileCount(Map<String, Object> params) throws Exception {
		if(params == null)
			params = new HashMap<String, Object>();
		
		return selectOne("ResearchOmicsFile.count", params);
	}
	
	public int deleteOmicsFile(ResearchOmicsFile omicsFile) throws Exception {
		if(omicsFile == null)
			throw new NullPointerException("Omics data is null.");
		
		if(omicsFile.getId() < 1)
			throw new IllegalArgumentException("Invalid omics ID-" + omicsFile.getId());
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("omicsFileId", omicsFile.getId());
		
		return deleteOmicsFile(params);
	}
	
	public int deleteOmicsFile(Research research) throws Exception {
		if(research == null)
			throw new NullPointerException("Research data is null.");
		
		if(research.getId() < 1)
			throw new IllegalArgumentException("Invalid research data ID-" + research.getId());
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("researchId", research.getId());
		
		return deleteOmicsFile(params);
	}
	
	public int deleteOmicsFile(Map<String, Object> params) throws Exception {
		if(params == null || params.isEmpty())
			throw new IllegalArgumentException("Invalid remove omics options.");
		
		return delete("ResearchOmicsFile.delete", params);
	}
}
