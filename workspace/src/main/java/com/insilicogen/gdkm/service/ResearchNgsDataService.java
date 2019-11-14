package com.insilicogen.gdkm.service;

import java.util.List;
import java.util.Map;

import com.insilicogen.gdkm.model.NgsDataRegist;
import com.insilicogen.gdkm.model.Research;

public interface ResearchNgsDataService {

	public static final Object Lock = new Object();
	
	public NgsDataRegist appendLinkedData(Research research, NgsDataRegist data) throws Exception;
	
	public boolean hasLinkedData(Research research, NgsDataRegist data) throws Exception;
	
	public List<NgsDataRegist> setLinkedData(Research research, List<NgsDataRegist> dataList) throws Exception;
	
	public List<NgsDataRegist> getLinkedDataList(Research research) throws Exception;
	
	public List<NgsDataRegist> getLinkedDataList(Map<String, Object> params) throws Exception;
	
	public List<NgsDataRegist> getLinkedDataList(Research research, Map<String, Object> params) throws Exception;
	
	public int getLinkedDataCount(Research research) throws Exception;
	
	public int getLinkedDataCount(Map<String, Object> params) throws Exception;
	
	public int getLinkedDataCount(Research research, Map<String, Object> params) throws Exception;
	
	public List<NgsDataRegist> deleteLinkedData(Research research) throws Exception;

	public int deleteLinkedData(NgsDataRegist data) throws Exception;
	
	public NgsDataRegist deleteLinkedData(Research research, NgsDataRegist data) throws Exception;
	
}
