package com.insilicogen.gdkm.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.insilicogen.gdkm.dao.NgsDataStatisticsDAO;
import com.insilicogen.gdkm.model.NgsDataStatistics;
import com.insilicogen.gdkm.service.NgsDataStatisticsService;

@Service("NgsDataStatisticsServiceImpl")
public class NgsDataStatisticsServiceImpl implements NgsDataStatisticsService {

	static Logger logger = LoggerFactory.getLogger(NgsDataStatisticsServiceImpl.class);
	
	@Resource(name="NgsDataStatisticsDAO")
	NgsDataStatisticsDAO statisticsDAO;
	
	@Override
	public List<Integer> getRegistYearList() {
		try {
			return statisticsDAO.selectYearList();
		} catch (Exception e) {
			logger.error(e.getMessage());
			
			List<Integer> yearList = new ArrayList<Integer>();
			yearList.add(Calendar.getInstance().get(Calendar.YEAR));
			
			return yearList;
		}
	}
	
	@Override
	public List<NgsDataStatistics> getTaxonStatistics(Map<String, Object> params) {
		try {
			return statisticsDAO.selectTaxonStats(params);
		} catch(Exception e) {
			logger.error(e.getMessage());
			return Collections.emptyList();
		}
	}

	@Override
	public List<NgsDataStatistics> getNgsRegistStatistics(Map<String, Object> params) {
		try {
			return statisticsDAO.selectNgsRegistStats(params);
		} catch(Exception e) {
			logger.error(e.getMessage());
			return Collections.emptyList();
		}
	}

	@Override
	public int getRegistTaxonCount(Map<String, Object> params) {
		try {
			return statisticsDAO.selectTaxonCount(params);
		} catch(Exception e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int getRegistDataCount(Map<String, Object> params) {
		try {
			return statisticsDAO.selectRegistDataCount(params);
		} catch(Exception e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public Date getLastUpdateDate(Map<String, Object> params) {
		try {
			return statisticsDAO.selectLastUpdated(params);
		} catch(Exception e) {
			logger.error(e.getMessage());
			return null;
		}
	}
}
