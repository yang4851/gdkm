package com.insilicogen.gdkm.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.insilicogen.gdkm.model.AniFile;

import egovframework.rte.psl.dataaccess.EgovAbstractMapper;

@Repository("AniDAO")
public class AniDAO extends EgovAbstractMapper{

	public List<AniFile> selectAchiveOfFasta(Map<String, Object> params) throws Exception {
		
		List<AniFile> result = selectList("Tools.selectAchiveOfFasta", params);
		return result;
	}

	public Integer countAchiveOfFasta(Map<String, Object> params) throws Exception {
		Integer total = selectOne("Tools.countAchiveOfFasta", params);
		return total;
	} 
	
}
