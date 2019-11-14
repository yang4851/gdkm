package com.insilicogen.gdkm.service;

import java.util.List;

import com.insilicogen.gdkm.model.AchiveHeaderXref;
import com.insilicogen.gdkm.model.NgsDataAchive;
import com.insilicogen.gdkm.model.NgsDataFeaturesHeader;

public interface NgsDataFeaturesHeaderService {
	
	public int createHeader(NgsDataFeaturesHeader header);
	
	public int deleteHeader(int header_id);
	public int deleteHeader(NgsDataAchive achive); 
	
	public int changeHeader(NgsDataFeaturesHeader header);
	public NgsDataFeaturesHeader selectOneHeader(int header_id);
	public List<NgsDataFeaturesHeader> selectOneHeader(NgsDataAchive achive);
	public List<NgsDataFeaturesHeader> selectListHeader();
	
	public int createAchiveHeaderXref(AchiveHeaderXref ahxref);
	public int deleteAchiveHeaderXref(int ahxref_id);
	public int changeAchiveHeaderXref(AchiveHeaderXref ahxref);
	public AchiveHeaderXref selectOneAchiveHeaderXref(int ahxref_id);
	public List<AchiveHeaderXref> selectListAchiveHeaderXref();
}
