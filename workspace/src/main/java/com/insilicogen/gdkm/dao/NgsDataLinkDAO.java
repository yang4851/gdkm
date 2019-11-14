package com.insilicogen.gdkm.dao;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.insilicogen.gdkm.model.NgsDataLink;
import com.insilicogen.gdkm.model.NgsDataRegist;

import egovframework.rte.psl.dataaccess.EgovAbstractMapper;

@Repository("NgsDataLinkDAO")
public class NgsDataLinkDAO extends EgovAbstractMapper{
	
	public int insertNgsDataLink(NgsDataLink ngsDataLink) throws Exception{
		return insert("NgsDataLink.insert", ngsDataLink);
	}
	
	public NgsDataLink selectNgsDataLink(int ngsDataLinkId) throws Exception{
		return selectOne("NgsDataLink.select", ngsDataLinkId);
	}
	
	public int deleteNgsDataLinkOfRawData(NgsDataRegist rawData) throws Exception {
//		if(!Globals.REGIST_DATA_RAWDATA.equals(rawData.getDataType())) {
//			throw new IllegalArgumentException("Invalid raw data information - " + rawData.getRegistNo());
//		}
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("rawRegistId", rawData.getId());
		
		return delete("NgsDataLink.delete", params);
	}
	
	public int deleteNgsDataLinkOfRawData(String rawRegistId) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("rawRegistId", rawRegistId);
		return delete("NgsDataLink.delete", params);
	}
	
	public int deleteNgsDataLinkOfRawData(int[] ids) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("rawRegistIds", ids);
		
		return delete("NgsDataLink.delete", params);
	}
	
	public int deleteNgsDataLinkOfProcessedData(NgsDataRegist processedData) throws Exception {
//		if(!Globals.REGIST_DATA_PROCESSEDDATA.equals(processedData.getDataType())) {
//			throw new IllegalArgumentException("Invalid processed data information - " + processedData.getRegistNo());
//		}
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("processedRegistId", processedData.getId());
		
		return delete("NgsDataLink.delete", params);
	}
	
	public int deleteNgsDataLinkOfProcessedData(Integer processedRegistId) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("processedRegistId", processedRegistId);
		return delete("NgsDataLink.delete", params);
	}
	
	public int deleteNgsDataLinkOfProcessedData(int[] ids) throws Exception{
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("processedRegistIds", ids);
		
		return delete("NgsDataLink.delete", params);
	}
}
