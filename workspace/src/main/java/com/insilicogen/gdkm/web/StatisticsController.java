package com.insilicogen.gdkm.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.insilicogen.gdkm.model.NgsDataStatistics;
import com.insilicogen.gdkm.service.NgsDataStatisticsService;

@RestController
public class StatisticsController extends AbstractController {
	
	@Resource(name="NgsDataStatisticsServiceImpl")
	NgsDataStatisticsService statisticsService;
	
	@RequestMapping(value="/statistics/years", method=RequestMethod.GET)
	public List<Integer> getRegistYearList() throws Exception{
		return statisticsService.getRegistYearList();
	}
	
	@RequestMapping(value="/statistics/species", method=RequestMethod.GET)
	public List<NgsDataStatistics> getSpeciesStatistics(@RequestParam Map<String, Object> params) throws Exception{
		return statisticsService.getTaxonStatistics(params);
	}

	@RequestMapping(value="/statistics/regists", method=RequestMethod.GET)
	public List<NgsDataStatistics> getNgsRegistStatistics(@RequestParam Map<String, Object> params) throws Exception{
		return statisticsService.getNgsRegistStatistics(params);
	}
	
	@RequestMapping(value="/statistics/summary", method=RequestMethod.GET)
	public Map<String, Object> getNgsRegistSummary(@RequestParam Map<String, Object> params) throws Exception{
		Map<String, Object> summary = new HashMap<String, Object>();
		summary.put("taxonCount", statisticsService.getRegistTaxonCount(params));
		summary.put("registCount", statisticsService.getRegistDataCount(params));
		summary.put("lastUpdated", statisticsService.getLastUpdateDate(params));
		
		return summary;
	}
}
