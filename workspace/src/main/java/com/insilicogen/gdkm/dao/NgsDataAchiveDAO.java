package com.insilicogen.gdkm.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.insilicogen.gdkm.model.NgsDataAchive;

import egovframework.rte.psl.dataaccess.EgovAbstractMapper;

@Repository("NgsDataAchiveDAO")
public class NgsDataAchiveDAO extends EgovAbstractMapper{
	
	public int insertNgsDataAchive(NgsDataAchive achive) throws Exception{
		return insert("NgsDataAchive.insert", achive);
	}
	
	public NgsDataAchive selectNgsDataAchive(int achiveId) throws Exception{
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("achiveId", achiveId);
		return selectOne("NgsDataAchive.get", params);
	}
	
	/**
	 * 검색조거 
	 * NGS 데이터 등록 ID = registId
	 * NGS 데이터 등록 종류 = dataType (RawData|ProcessedData)
	 * 검색 키워드 = keyword
	 * 검색 일치 여부 = match(exact|contains)
	 * 검색 결과 범위 인덱스 = firstIndex
	 * 검색 결과 범위 크기 = rowSize
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<NgsDataAchive> selectNgsDataAchiveList(Map<String, Object> params) throws Exception{
		return selectList("NgsDataAchive.select", params);
	}
	
	public int updateNgsDataAchive(NgsDataAchive achive) throws Exception{
		return update("NgsDataAchive.update", achive);
	}
	
	public int selectNgsDataAchiveCount(Map<String, Object> params) throws Exception{
		return selectOne("NgsDataAchive.count", params);
	}
	
	public List<Map<String, String>> selectAchiveNode(Map<String, Object> params) throws Exception{
		return selectList("NgsDataAchive.selectAchiveNode", params);
	}
	
	public int deleteNgsDataAchive(int achiveId) throws Exception{
		return delete("NgsDataAchive.delete", achiveId);
	}
}
