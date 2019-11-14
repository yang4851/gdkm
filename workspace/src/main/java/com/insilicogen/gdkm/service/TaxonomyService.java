package com.insilicogen.gdkm.service;

import java.util.List;
import java.util.Map;

import com.insilicogen.gdkm.model.Taxonomy;

public interface TaxonomyService {

	public Taxonomy getTaxonomy(Integer taxonId) throws Exception;
	
	public List<Taxonomy> getTaxonomyList(Map<String, Object> params);
	
	public int getTaxonommyCount(Map<String, Object> params);
	
	public List<Taxonomy> getHierarchyList(Taxonomy taxonomy);
	
	public List<Taxonomy> getHierarchyList(Taxonomy taxonomy, boolean includeSub);
	
	public List<Taxonomy> getChildTaxonomyList(Taxonomy taxonomy);
	
	public int getChildTaxonomyCount(Taxonomy taxonomy);
	
	public List<Taxonomy> getTaxonNode(Map<String,Object> params) throws Exception;
}
