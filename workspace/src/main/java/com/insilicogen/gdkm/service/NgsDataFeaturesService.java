package com.insilicogen.gdkm.service;

import java.util.List;
import java.util.Map;

import com.insilicogen.gdkm.model.AchiveFeaturesXref;
import com.insilicogen.gdkm.model.NgsDataAchive;
import com.insilicogen.gdkm.model.NgsDataFeatures;

public interface NgsDataFeaturesService {
	
	public int createFeatures(NgsDataFeatures features);
	
	public int deleteFeatures(int features_id);
	
	public int deleteFeatures(NgsDataAchive achive);
	
	public int changeFeatures(NgsDataFeatures features);
	
	public NgsDataFeatures selectOneFeatures(int features_id);
	
	public List<NgsDataFeatures> getFeaturesList(Map<String, Object> params);
	
	public int getFeaturesCount(Map<String, Object> params);
	
	public int createAchiveFeaturesXref(AchiveFeaturesXref afxref);
	
	public int deleteAchiveFeaturesXref(int afxref_id);
	
	public int changeAchiveFeaturesXref(AchiveFeaturesXref afxref);
	
	public AchiveFeaturesXref selectOneAchiveFeaturesXref(int afxref_id);
	
	public List<AchiveFeaturesXref> selectListAchiveFeaturesXref();
	
}
