package com.insilicogen.gdkm.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.insilicogen.gdkm.model.NgsDataAchive;
import com.insilicogen.gdkm.model.NgsDataAchiveLog;

import egovframework.rte.psl.dataaccess.EgovAbstractMapper;

@Repository("NgsDataAchiveLogDAO")
public class NgsDataAchiveLogDAO extends EgovAbstractMapper{

	public int insertNgsDataAchiveLog(NgsDataAchiveLog log) throws Exception {
		return insert("NgsDataAchiveLog.insert", log);
	}
	
	public NgsDataAchiveLog selectNgsDataAchiveLog(int logId) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("logId", logId);
		
		return selectOne("NgsDataAchiveLog.get", params);
	}
	
	public List<NgsDataAchiveLog> selectNgsDataAchiveLogList(NgsDataAchive achive) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("achiveId", achive.getId());
		
		return selectList("NgsDataAchiveLog.select", params);
	}
	
	public int selectNgsDataAchiveLogCount(NgsDataAchive achive) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("achiveId", achive.getId());
		
		return selectOne("NgsDataAchiveLog.count", params);
	}
	
	public int deleteNgsDataAchiveLog(NgsDataAchiveLog log) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("logId", log.getId());
		
		return delete("NgsDataAchiveLog.delete", params);
	}
	
	public int deleteNgsDataAchiveLog(NgsDataAchive achive) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("achiveId", achive.getId());
		
		return delete("NgsDataAchiveLog.delete", params);
	}
}
