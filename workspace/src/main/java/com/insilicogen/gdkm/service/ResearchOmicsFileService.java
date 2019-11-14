package com.insilicogen.gdkm.service;

import java.util.List;
import java.util.Map;

import com.insilicogen.gdkm.model.Research;
import com.insilicogen.gdkm.model.ResearchOmicsFile;

public interface ResearchOmicsFileService {

	public static final Object Lock = new Object();
	
	public ResearchOmicsFile createOmicsFile(ResearchOmicsFile omicsFile) throws Exception;
	
	public ResearchOmicsFile changeOmicsFile(ResearchOmicsFile omicsFile) throws Exception;
	
	public ResearchOmicsFile getOmicsFile(Integer omicsFileId) throws Exception;
	
	public List<ResearchOmicsFile> getOmicsFileList(Research research) throws Exception;
	
	public List<ResearchOmicsFile> getOmicsFileList(Map<String, Object> params) throws Exception;
	
	public List<ResearchOmicsFile> getOmicsFileList(Research research, Map<String, Object> params) throws Exception;
	
	public int getOmicsFileCount(Research research) throws Exception;
	
	public int getOmicsFileCount(Map<String, Object> params) throws Exception;
	
	public int getOmicsFileCount(Research research, Map<String, Object> params) throws Exception;
	
	public ResearchOmicsFile deleteOmicsFile(ResearchOmicsFile omicsFile) throws Exception;
	
	public List<ResearchOmicsFile> deleteOmicsFile(Research research) throws Exception;
}
