package com.insilicogen.gdkm.dao;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.insilicogen.gdkm.model.NgsDataRegist;
import com.insilicogen.gdkm.model.NgsProcessedData;

import egovframework.rte.psl.dataaccess.EgovAbstractMapper;

@Repository("NgsProcessedDataDAO")
public class NgsProcessedDataDAO extends EgovAbstractMapper{


	public int insertNgsProcessedData(NgsProcessedData processedData) throws Exception{
		return insert("NgsProcessedData.insert", processedData);
	}
	
	public NgsProcessedData selectNgsProcessedData(int registId) throws Exception{
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("registId", registId);
		
		return selectOne("NgsProcessedData.get", params);
	}

	public NgsProcessedData selectNgsProcessedData(NgsDataRegist dataRegist) throws Exception{
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("dataRegist", dataRegist);
		
		return selectOne("NgsProcessedData.get", params);
	}

	public int updateNgsProcessedData(NgsProcessedData processedData) throws Exception{
		return update("NgsProcessedData.update", processedData);
	}
	
	public int deleteNgsProcessedData(NgsDataRegist registData) {
		return delete("NgsProcessedData.delete", new int[] { registData.getId() });
	}
	
	public int deleteNgsProcessedData(NgsProcessedData rawData) {
		return delete("NgsProcessedData.delete", new int[] { rawData.getRegistId() });
	}
	
	public int deleteNgsProcessedData(int id) throws Exception{
		return delete("NgsProcessedData.delete", id);
	}
	
	public int deleteNgsProcessedData(String id) throws Exception{
		return delete("NgsProcessedData.delete", id);
	}
	
}
