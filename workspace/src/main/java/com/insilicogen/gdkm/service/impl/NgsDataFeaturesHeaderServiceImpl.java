package com.insilicogen.gdkm.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.insilicogen.gdkm.dao.NgsDataFeaturesDAO;
import com.insilicogen.gdkm.dao.NgsDataFeaturesHeaderDAO;
import com.insilicogen.gdkm.dao.NgsDataFeaturesXrefDAO;
import com.insilicogen.gdkm.model.AchiveHeaderXref;
import com.insilicogen.gdkm.model.NgsDataAchive;
import com.insilicogen.gdkm.model.NgsDataFeaturesHeader;
import com.insilicogen.gdkm.service.NgsDataFeaturesHeaderService;

@Service("NgsDataFeaturesHeaderService")
public class NgsDataFeaturesHeaderServiceImpl implements NgsDataFeaturesHeaderService{

	@Resource(name="NgsDataFeaturesHeaderDAO")
	private NgsDataFeaturesHeaderDAO headerDAO;
	
	@Resource(name="NgsDataFeaturesDAO")
	private NgsDataFeaturesDAO featuresDAO;
	
	@Resource(name="NgsDataFeaturesXrefDAO")
	private NgsDataFeaturesXrefDAO featuresXrefDAO;
	
	@Override
	public int createHeader(NgsDataFeaturesHeader header) {
		return headerDAO.insertHeader(header);
	}

	@Override
	public int deleteHeader(int header_id) {
		return headerDAO.deleteHeader(header_id);
	}
	
	@Override
	public int deleteHeader(NgsDataAchive achive) {
		List<Integer> headerIdList = headerDAO.selectHeaderIdList(achive);
		if(headerIdList.size() == 0)
			return 0;
		
		headerDAO.deleteAchiveHeaderXref(achive);
		
		for(Integer headerId : headerIdList) {
			NgsDataFeaturesHeader header = new NgsDataFeaturesHeader();
			header.setHeaderId(headerId);
			
			List<Integer> featureIdList = featuresDAO.selectFeatureIdList(header);
			if(featureIdList.size() > 0){
				featuresXrefDAO.deleteXref(featureIdList);
			}
			
			featuresDAO.deleteFeatures(header);
		}
		
		return headerDAO.deleteHeader(headerIdList);
	}

	@Override
	public int changeHeader(NgsDataFeaturesHeader header) {
		return headerDAO.updateHeader(header);
	}

	@Override
	public NgsDataFeaturesHeader selectOneHeader(int header_id) {
		return headerDAO.selectOneHeader(header_id);
	}
	
	@Override
	public List<NgsDataFeaturesHeader> selectOneHeader(NgsDataAchive achive) {
		return headerDAO.selectOneHeaderOfAchive(achive.getId());
	}

	@Override
	public List<NgsDataFeaturesHeader> selectListHeader() {
		return headerDAO.selectListHeader();
	}

	@Override
	public int createAchiveHeaderXref(AchiveHeaderXref ahxref) {
		return headerDAO.insertAchiveHeaderXref(ahxref);
	}

	@Override
	public int deleteAchiveHeaderXref(int ahxref_id) {
		return headerDAO.deleteAchiveHeaderXref(ahxref_id);
	}

	@Override
	public int changeAchiveHeaderXref(AchiveHeaderXref ahxref) {
		return headerDAO.updateAchiveHeaderXref(ahxref);
	}

	@Override
	public AchiveHeaderXref selectOneAchiveHeaderXref(int ahxref_id) {
		return headerDAO.selectOneAchiveHeaderXref(ahxref_id);
	}

	@Override
	public List<AchiveHeaderXref> selectListAchiveHeaderXref() {
		return headerDAO.selectListAchiveHeaderXref();
	}

}
