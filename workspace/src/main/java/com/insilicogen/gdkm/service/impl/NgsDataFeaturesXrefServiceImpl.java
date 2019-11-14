package com.insilicogen.gdkm.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.insilicogen.gdkm.dao.NgsDataFeaturesXrefDAO;
import com.insilicogen.gdkm.model.NgsDataFeaturesXref;
import com.insilicogen.gdkm.service.NgsDataFeaturesXrefService;

@Service("NgsDataFeaturesXrefService")
public class NgsDataFeaturesXrefServiceImpl implements NgsDataFeaturesXrefService {

	@Resource(name="NgsDataFeaturesXrefDAO")
	private NgsDataFeaturesXrefDAO xrefDAO;
	
	@Override
	public int createXref(NgsDataFeaturesXref xref) {
		return xrefDAO.insertXref(xref);
	}

	@Override
	public int deleteXref(int xref_id) {
		return xrefDAO.deleteXref(xref_id);
	}

	@Override
	public int changeXref(NgsDataFeaturesXref xref) {
		return xrefDAO.updateXref(xref);
	}

	@Override
	public NgsDataFeaturesXref selectOneXref(int xref_id) {
		return xrefDAO.selectOneXref(xref_id);
	}

	@Override
	public List<NgsDataFeaturesXref> selectListXref() {
		return xrefDAO.selectListXref();
	}

}
