package com.insilicogen.gdkm.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.insilicogen.gdkm.model.NgsDataRegist;
import com.insilicogen.gdkm.model.NgsRawData;

import egovframework.rte.psl.dataaccess.EgovAbstractMapper;

@Repository("NgsRawDataDAO")
public class NgsRawDataDAO extends EgovAbstractMapper{

	public int insertNgsRawData(NgsRawData rawData) throws Exception{
		return insert("NgsRawData.insert", rawData);
	}
	
	public NgsRawData selectNgsRawData(int registId) throws Exception{
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("registId", registId);
		
		return selectOne("NgsRawData.get", params);
	}

	public NgsRawData selectNgsRawData(NgsDataRegist dataRegist) throws Exception{
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("dataRegist", dataRegist);
		
		return selectOne("NgsRawData.get", params);
	}

	public int updateNgsRawData(NgsRawData rawData) throws Exception{
		return update("NgsRawData.update", rawData);
	}
	
	public int deleteNgsRawData(NgsDataRegist registData) {
		return delete("NgsRawData.delete", new int[] { registData.getId() });
	}
	
	public int deleteNgsRawData(NgsRawData rawData) {
		return delete("NgsRawData.delete", new int[] { rawData.getRegistId() });
	}
	
	public int deleteNgsRawData(int id) throws Exception{
		return delete("NgsRawData.delete", id);
	}
	
	public int deleteNgsRawData(String id) throws Exception{
		return delete("NgsRawData.delete", id);
	}
	
	@Deprecated
	public List<NgsRawData> selectNgsRawDataList(String openStatus, String registStatus, String registFrom, String registTo, 
			String approvalStatus, String approvalDateStart, String approvalDateEnd,
			String column, String query, int firstIndex, int rowSize){
		Map<String, Object> options = new HashMap<String, Object>();
		if(StringUtils.isNoneBlank(openStatus))
			options.put("openStatus", openStatus);
		if(StringUtils.isNoneBlank(registStatus))
			options.put("registStatus", registStatus);
		if(StringUtils.isNoneBlank(registFrom))
			options.put("registFrom", registFrom);
		if(StringUtils.isNoneBlank(registTo))
			options.put("registTo", registTo);
		if(StringUtils.isNoneBlank(approvalStatus))
			options.put("approvalStatus", approvalStatus);
		if(StringUtils.isNoneBlank(approvalDateStart))
			options.put("approvalDateStart", approvalDateStart);
		if(StringUtils.isNoneBlank(approvalDateEnd))
			options.put("approvalDateEnd", approvalDateEnd);
		if(StringUtils.isNoneBlank(column))
			options.put("column", column);
		if(StringUtils.isNoneBlank(query))
			options.put("query", query);
		options.put("firstIndex", firstIndex);
		options.put("rowSize", rowSize);
		return selectList("NgsRawData.select", options);
	}
	
	/**
	 * @param openStatus
	 * @param registStatus
	 * @param registFrom
	 * @param registTo
	 * @param approvalStatus
	 * @param approvalDateStart
	 * @param approvalDateEnd
	 * @param column
	 * @param query
	 * @return
	 */
	@Deprecated
	public int getNgsRawDataCount(String openStatus, String registStatus, String registFrom, String registTo, 
			String approvalStatus, String approvalDateStart, String approvalDateEnd,
			String column, String query){
		Map<String, Object> options = new HashMap<String, Object>();
		if(StringUtils.isNoneBlank(openStatus))
			options.put("openStatus", openStatus);
		if(StringUtils.isNoneBlank(registStatus))
			options.put("registStatus", registStatus);
		if(StringUtils.isNoneBlank(registFrom))
			options.put("registFrom", registFrom);
		if(StringUtils.isNoneBlank(registTo))
			options.put("registTo", registTo);
		if(StringUtils.isNoneBlank(approvalStatus))
			options.put("approvalStatus", approvalStatus);
		if(StringUtils.isNoneBlank(approvalDateStart))
			options.put("approvalDateStart", approvalDateStart);
		if(StringUtils.isNoneBlank(approvalDateEnd))
			options.put("approvalDateEnd", approvalDateEnd);
		if(StringUtils.isNoneBlank(column))
			options.put("column", column);
		if(StringUtils.isNoneBlank(query))
			options.put("query", query);
		return selectOne("NgsRawData.count", options);
	}
} 
