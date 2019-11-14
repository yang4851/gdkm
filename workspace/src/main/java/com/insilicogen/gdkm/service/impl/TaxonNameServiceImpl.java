package com.insilicogen.gdkm.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.insilicogen.gdkm.dao.TaxonNameDAO;
import com.insilicogen.gdkm.model.TaxonName;
import com.insilicogen.gdkm.service.TaxonNameService;

@Service("TaxonNameService")
public class TaxonNameServiceImpl implements TaxonNameService{
	
	@Resource(name="TaxonNameDAO")
	TaxonNameDAO taxonNameDAO;

	@Override
	public TaxonName selectTaxonName(int taxonId) throws Exception {
		return taxonNameDAO.selectTaxonName(taxonId);
	}

}
