package com.insilicogen.gdkm.service;

import java.util.List;
import java.util.Map;

import com.insilicogen.gdkm.model.NgsDataRegist;
import com.insilicogen.gdkm.model.Research;

public interface ResearchService {

	public static final Object Lock = new Object();
	
	public Research createResearch(Research research) throws Exception;

	public Research changeResearch(Research research) throws Exception;
	
	public Research getResearch(Integer researchId) throws Exception;
	
	public List<Research> getResearchList(NgsDataRegist regist) throws Exception;
	
	public List<Research> getResearchList(Map<String, Object> params) throws Exception;
	
	public int getResearchCount(NgsDataRegist regist) throws Exception;
	
	public int getResearchCount(Map<String, Object> params) throws Exception;
	
	public Research deleteResearch(Research research) throws Exception;
}
