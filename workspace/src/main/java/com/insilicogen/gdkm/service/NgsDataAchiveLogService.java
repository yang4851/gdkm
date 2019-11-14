package com.insilicogen.gdkm.service;

import java.util.List;

import com.insilicogen.gdkm.model.NgsDataAchive;
import com.insilicogen.gdkm.model.NgsDataAchiveLog;

public interface NgsDataAchiveLogService {

	public NgsDataAchiveLog createNgsDataAchiveLog(NgsDataAchiveLog log) throws Exception;
	
	public NgsDataAchiveLog getNgsDataAchiveLog(Integer logId) throws Exception;
	
	public List<NgsDataAchiveLog> getNgsDataAchiveLogList(NgsDataAchive achive);
	
	public int getNgsDataAchiveLogCount(NgsDataAchive achive);

	public int deleteNgsDataAchvieLog(NgsDataAchiveLog log) throws Exception;
	
	public int deleteNgsDataAchiveLog(NgsDataAchive achive) throws Exception;
	
}
