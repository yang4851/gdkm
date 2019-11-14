package com.insilicogen.gdkm.service;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.insilicogen.gdkm.model.NgsDataAchive;
import com.insilicogen.gdkm.model.NgsDataRegist;

public interface NgsDataAchiveService {
	
	public static final Object Lock = new Object();
	
	public NgsDataAchive createNgsDataAchive(NgsDataAchive achive) throws Exception;
	
	public NgsDataAchive createNgsDataAchive(NgsDataRegist regist, MultipartFile sourceFile) throws Exception;
	
	public NgsDataAchive createNgsDataAchive(NgsDataAchive achive, File source) throws Exception;
	
	public NgsDataAchive changeNgsDataAchive(NgsDataAchive achive) throws Exception;
	
	public NgsDataAchive getNgsDataAchive(Integer achiveId) throws Exception;
	
	public List<NgsDataAchive> getNgsDataAchiveList(NgsDataRegist regist) throws Exception;
	
	public List<NgsDataAchive> getNgsDataAchiveList(Map<String, Object> params) throws Exception;
	
	public int getNgsDataAchiveCount(Map<String, Object> params) throws Exception;
	
	public int deleteNgsDataAchive(NgsDataAchive achive) throws Exception;

	List<NgsDataAchive> deleteNgsDataAchive(String registId) throws Exception;
	
	List<Map<String, String>> getAchiveNode(Map<String, Object> params) throws Exception;
}
