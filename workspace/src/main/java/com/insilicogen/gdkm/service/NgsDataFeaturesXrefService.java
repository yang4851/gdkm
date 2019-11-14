package com.insilicogen.gdkm.service;

import java.util.List;

import com.insilicogen.gdkm.model.NgsDataFeaturesXref;

public interface NgsDataFeaturesXrefService {
	
	public int createXref(NgsDataFeaturesXref xref);
	
	public int deleteXref(int xref_id);
		
	public int changeXref(NgsDataFeaturesXref xref);
	
	public NgsDataFeaturesXref selectOneXref(int xref_id);
	
	public List<NgsDataFeaturesXref> selectListXref();
}
