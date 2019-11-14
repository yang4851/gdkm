package com.insilicogen.gdkm.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.insilicogen.gdkm.model.NgsDataStatistics;

import egovframework.rte.psl.dataaccess.EgovAbstractMapper;

@Repository("NgsDataStatisticsDAO")
public class NgsDataStatisticsDAO extends EgovAbstractMapper {

	public List<Integer> selectYearList() throws Exception {
		return selectList("Statistics.selectYears");
	}
	
	public List<NgsDataStatistics> selectTaxonStats(Map<String, Object> params) {
		return selectList("Statistics.selectTaxon", params);
	}
	
	public List<NgsDataStatistics> selectNgsRegistStats(Map<String, Object> params) {
		return selectList("Statistics.select", params);
	}
	
	public int selectTaxonCount(Map<String, Object> params) {
		return selectOne("Statistics.selectTaxonCount", params);
	}
	
	public int selectRegistDataCount(Map<String, Object> params) {
		return selectOne("Statistics.selectFileCount", params);
	}
	
	public Date selectLastUpdated(Map<String, Object> params) {
		return selectOne("Statistics.selectLastUpdated", params);
	}
}
