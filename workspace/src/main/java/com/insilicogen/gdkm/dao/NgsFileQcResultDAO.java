package com.insilicogen.gdkm.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.insilicogen.gdkm.model.NgsDataAchive;
import com.insilicogen.gdkm.model.NgsDataRegist;
import com.insilicogen.gdkm.model.NgsFileQcResult;

import egovframework.rte.psl.dataaccess.EgovAbstractMapper;

@Repository("NgsFileQcResultDAO")
public class NgsFileQcResultDAO extends EgovAbstractMapper {
	
	public int insertNgsFileQcResult(NgsFileQcResult result) throws Exception {
		return insert("NgsFileQcResult.insert", result);
	}

	public NgsFileQcResult selectNgsFileQcResult(Integer resultId) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("resultId", resultId);
		
		return selectOne("NgsFileQcResult.get", params);
	}
	
	public List<NgsFileQcResult> selectNgsFileQcResultList(NgsDataRegist regist) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("registId", regist.getId());
		
		return selectList("NgsFileQcResult.select", params);
	}
	
	public List<NgsFileQcResult> selectNgsFileQcResultList(NgsDataAchive achive) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("achiveId", achive.getId());
		
		return selectList("NgsFileQcResult.select", params);
	}
	
	public int selectNgsFileQcResultCount(NgsDataRegist regist) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("registId", regist.getId());
		
		return selectOne("NgsFileQcResult.count", params);
	}
	
	public int selectNgsFileQcResultCount(NgsDataAchive achive) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("achiveId", achive.getId());
		
		return selectOne("NgsFileQcResult.count", params);
	}
	
	public int updateNgsFileQcResult(NgsFileQcResult result) throws Exception {
		return update("NgsFileQcResult.update", result);
	}
	
	public int deleteNgsFileQcResult(NgsFileQcResult result) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("resultId", result.getId());
		
		return delete("NgsFileQcResult.delete", params);
	}

	public int deleteNgsFileQcResult(NgsDataAchive achive) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("achiveId", achive.getId());
		
		return delete("NgsFileQcResult.delete", params);
	}
	
	public int deleteNgsFileQcResult(NgsDataRegist regist) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("registId", regist.getId());
		
		return delete("NgsFileQcResult.delete", params);
	}
}
