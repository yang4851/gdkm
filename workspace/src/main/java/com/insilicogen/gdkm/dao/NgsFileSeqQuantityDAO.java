package com.insilicogen.gdkm.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.insilicogen.gdkm.model.NgsDataAchive;
import com.insilicogen.gdkm.model.NgsDataRegist;
import com.insilicogen.gdkm.model.NgsFileSeqQuantity;

import egovframework.rte.psl.dataaccess.EgovAbstractMapper;

@Repository("NgsFileSeqQuantityDAO")
public class NgsFileSeqQuantityDAO extends EgovAbstractMapper {
	
	public int insertNgsFileSeqQuantity(NgsFileSeqQuantity quantity) throws Exception {
		return insert("NgsFileSeqQuantity.insert", quantity);
	}

	public NgsFileSeqQuantity selectNgsFileSeqQuantity(Integer quantityId) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("quantityId", quantityId);
		
		return selectOne("NgsFileSeqQuantity.get", params);
	}
	
	public NgsFileSeqQuantity selectNgsFileSeqQuantity(NgsDataAchive achive) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("achiveId", achive.getId());
		
		return selectOne("NgsFileSeqQuantity.get", params);
	}
	
	public List<NgsFileSeqQuantity> selectNgsFileSeqQuantityList(Integer registId) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("registId", registId);
		
		return selectList("NgsFileSeqQuantity.select", params);
	}
	
	public int selectNgsFileSeqQuantityCount(Integer registId) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("registId", registId);
		
		return selectOne("NgsFileSeqQuantity.count", params);
	}
	
	public int updateNgsFileSeqQuantity(NgsFileSeqQuantity quantity) throws Exception {
		return update("NgsFileSeqQuantity.update", quantity);
	}
	
	public int deleteNgsFileSeqQuantity(NgsFileSeqQuantity quantity) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("quantityId", quantity.getId());
		
		return delete("NgsFileSeqQuantity.delete", params);
	}

	public int deleteNgsFileSeqQuantity(NgsDataAchive achive) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("achiveId", achive.getId());
		
		return delete("NgsFileSeqQuantity.delete", params);
	}
	
	public int deleteNgsFileSeqQuantity(NgsDataRegist dataRegist) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("registId", dataRegist.getId());
		
		return delete("NgsFileSeqQuantity.delete", params);
	}
}
