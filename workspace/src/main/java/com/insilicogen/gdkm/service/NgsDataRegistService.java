package com.insilicogen.gdkm.service;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.insilicogen.gdkm.model.NgsDataRegist;

public interface NgsDataRegistService {
	
	public static final Object Lock = new Object();
	
	public NgsDataRegist createNgsDataRegist(NgsDataRegist regist) throws Exception;
	
	public NgsDataRegist changeNgsDataRegist(NgsDataRegist regist) throws Exception;
	
	public NgsDataRegist getNgsDataRegist(Integer registId) throws Exception;
	
	public List<NgsDataRegist> getNgsDataRegistList(Map<String, Object> params) throws Exception;
	
	public List<NgsDataRegist> getNgsDataLinkedList(NgsDataRegist regist) throws Exception;
	
	public int getNgsDataRegistCount(Map<String, Object> params) throws Exception;
	
	public int deleteNgsDataRegist(NgsDataRegist regist) throws Exception;
	
	public File downloadRegistDataExcelFile(Map<String, Object> params) throws Exception;
	
	public List<Map<String, String>> getRawdataNode(Map<String, Object> params) throws Exception;
	
	public List<Map<String, String>> getProcessedNode(Map<String, Object> params) throws Exception;
}
