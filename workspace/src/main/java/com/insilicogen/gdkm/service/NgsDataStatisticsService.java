package com.insilicogen.gdkm.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.insilicogen.gdkm.model.NgsDataStatistics;

public interface NgsDataStatisticsService {

	public List<Integer> getRegistYearList();
	
	public List<NgsDataStatistics> getTaxonStatistics(Map<String, Object> params);
	
	public List<NgsDataStatistics> getNgsRegistStatistics(Map<String, Object> params);
	
	public int getRegistTaxonCount(Map<String, Object> params);
	
	public int getRegistDataCount(Map<String, Object> params);
	
	public Date getLastUpdateDate(Map<String, Object> params);
	
}
