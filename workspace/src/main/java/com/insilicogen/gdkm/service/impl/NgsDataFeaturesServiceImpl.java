package com.insilicogen.gdkm.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.insilicogen.gdkm.dao.NgsDataFeaturesDAO;
import com.insilicogen.gdkm.dao.NgsDataFeaturesXrefDAO;
import com.insilicogen.gdkm.model.AchiveFeaturesXref;
import com.insilicogen.gdkm.model.NgsDataAchive;
import com.insilicogen.gdkm.model.NgsDataFeatures;
import com.insilicogen.gdkm.service.NgsDataFeaturesService;

@Service("NgsDataFeaturesService")
public class NgsDataFeaturesServiceImpl implements NgsDataFeaturesService{

	@Resource(name="NgsDataFeaturesDAO")
	private NgsDataFeaturesDAO featuresDAO;
	
	@Resource(name="NgsDataFeaturesXrefDAO")
	private NgsDataFeaturesXrefDAO featuresXrefDAO;
	
	@Override
	public int createFeatures(NgsDataFeatures features) {
		return featuresDAO.insertFeatures(features);
	}

	@Override
	public int deleteFeatures(int features_id) {
		return featuresDAO.deleteFeatures(features_id);
	}
	
	@Override
	public int deleteFeatures(NgsDataAchive achive) {
		List<Integer> featureIdList = featuresDAO.selectFeatureIdList(achive);
		if(featureIdList.size() == 0)
			return 0;
		
		featuresDAO.deleteAchiveFeaturesXref(achive);
		featuresXrefDAO.deleteXref(featureIdList);
		return featuresDAO.deleteFeatures(featureIdList);
	}

	@Override
	public int changeFeatures(NgsDataFeatures features) {
		return featuresDAO.updateFeatures(features);
	}

	@Override
	public NgsDataFeatures selectOneFeatures(int features_id) {
		return featuresDAO.selectOneFeatures(features_id);
	}

	@Override
	public List<NgsDataFeatures> getFeaturesList(Map<String, Object> params) {
		return featuresDAO.selectListFeatures(params);
	}

	@Override
	public int getFeaturesCount(Map<String, Object> params) {
		return featuresDAO.selectCountFeatures(params);
	}
	
	@Override
	public int createAchiveFeaturesXref(AchiveFeaturesXref afxref) {
		return featuresDAO.insertAchiveFeaturesXref(afxref);
	}

	@Override
	public int deleteAchiveFeaturesXref(int afxref_id) {
		return featuresDAO.deleteAchiveFeaturesXref(afxref_id);
	}

	@Override
	public int changeAchiveFeaturesXref(AchiveFeaturesXref afxref) {
		return featuresDAO.updateAchiveFeaturesXref(afxref);
	}

	@Override
	public AchiveFeaturesXref selectOneAchiveFeaturesXref(int afxref_id) {
		return featuresDAO.selectOneAchiveFeaturesXref(afxref_id);
	}

	@Override
	public List<AchiveFeaturesXref> selectListAchiveFeaturesXref() {
		return featuresDAO.selectListAchiveFeaturesXref();
	}

}
