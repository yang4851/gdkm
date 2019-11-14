package com.insilicogen.gdkm.dao;

import org.springframework.stereotype.Repository;

import com.insilicogen.gdkm.model.TaxonName;

import egovframework.rte.psl.dataaccess.EgovAbstractMapper;

@Repository("TaxonNameDAO")
public class TaxonNameDAO extends EgovAbstractMapper{
	
	public TaxonName selectTaxonName(int taxonId) throws Exception{
		return selectOne("TaxonName.select", taxonId);
	}

}
