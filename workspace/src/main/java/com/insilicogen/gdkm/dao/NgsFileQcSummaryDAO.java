package com.insilicogen.gdkm.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.insilicogen.gdkm.model.NgsDataAchive;
import com.insilicogen.gdkm.model.NgsDataRegist;
import com.insilicogen.gdkm.model.NgsFileQcSummary;

import egovframework.rte.psl.dataaccess.EgovAbstractMapper;

@Repository("NgsFileQcSummaryDAO")
public class NgsFileQcSummaryDAO extends EgovAbstractMapper {
	
	public int insertNgsFileQcSummary(NgsFileQcSummary summary) throws Exception {
		return insert("NgsFileQcSummary.insert", summary);
	}

	public NgsFileQcSummary selectNgsFileQcSummary(Integer summaryId) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("summaryId", summaryId);
		
		return selectOne("NgsFileQcSummary.get", params);
	}
	
	public List<NgsFileQcSummary> selectNgsFileQcSummaryList(NgsDataAchive achive) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("achiveId", achive.getId());
		
		return selectList("NgsFileQcSummary.select", params);
	}
	
	public List<NgsFileQcSummary> selectNgsFileQcSummaryList(NgsDataRegist regist) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("registId", regist.getId());
		
		return selectList("NgsFileQcSummary.select", params);
	}
	
	public int selectNgsFileQcSummaryCount(NgsDataAchive achive) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("achiveId", achive.getId());
		
		return selectOne("NgsFileQcSummary.count", params);
	}
	
	public int selectNgsFileQcSummaryCount(NgsDataRegist regist) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("registId", regist.getId());
		
		return selectOne("NgsFileQcSummary.count", params);
	}
	
	public int updateNgsFileQcSummary(NgsFileQcSummary summary) throws Exception {
		return update("NgsFileQcSummary.update", summary);
	}
	
	public int deleteNgsFileQcSummary(NgsFileQcSummary summary) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("summaryId", summary.getId());
		
		return delete("NgsFileQcSummary.delete", params);
	}

	public int deleteNgsFileQcSummary(NgsDataAchive achive) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("achiveId", achive.getId());
		
		return delete("NgsFileQcSummary.delete", params);
	}
	
	public int deleteNgsFileQcSummary(NgsDataRegist dataRegist) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("registId", dataRegist.getId());
		
		return delete("NgsFileQcSummary.delete", params);
	}
}
