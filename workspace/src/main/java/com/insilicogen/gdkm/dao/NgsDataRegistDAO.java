package com.insilicogen.gdkm.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.insilicogen.gdkm.model.NgsDataRegist;

import egovframework.rte.psl.dataaccess.EgovAbstractMapper;

@Repository("NgsDataRegistDAO")
public class NgsDataRegistDAO extends EgovAbstractMapper {

	public int insertNgsDataRegist(NgsDataRegist regist) throws Exception {
		return insert("NgsDataRegist.insert", regist);
	}

	public NgsDataRegist selectBasicNgsDataRegist(Integer registId) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("registId", registId);
		
		return selectOne("NgsDataRegist.getBasic", params);
	}
	
	public NgsDataRegist selectNgsDataRegist(Integer registId) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("registId", registId);
		
		return selectOne("NgsDataRegist.get", params);
	}
	
	public List<NgsDataRegist> selectNgsDataRegistList(Map<String, Object> params) throws Exception {
		return selectList("NgsDataRegist.select", params);
	}
	
	public int selectNgsDataRegistCount(Map<String, Object> params) throws Exception {
		return selectOne("NgsDataRegist.count", params);
	}

	public int updateNgsDataRegist(NgsDataRegist regist) throws Exception {
		return update("NgsDataRegist.update", regist);
	}
	
	public int deleteNgsDataRegist(Integer registId) throws Exception {
		return delete("NgsDataRegist.delete", registId);
	}
	
	public List<Map<String, String>> selectRawdataNode(Map<String, Object> params) throws Exception{
		return selectList("NgsDataRegist.selectRawdataNode", params);
	}
	
	public List<Map<String, String>> selectProcessedNode(Map<String, Object> params) throws Exception{
		return selectList("NgsDataRegist.selectProcessedNode", params);
	}

}
