package com.insilicogen.gdkm.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.insilicogen.gdkm.model.Taxonomy;

import egovframework.rte.psl.dataaccess.EgovAbstractMapper;

@Repository("TaxonomyDAO")
public class TaxonomyDAO extends EgovAbstractMapper{
	
	public Taxonomy selectTaxonomy(Integer taxonId) throws Exception{
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("taxonId", taxonId);
		
		return selectOne("Taxonomy.get", params);
	}
	
	public List<Taxonomy> selectChildList(Integer parentId) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("parentId", parentId);
		
		return selectTaxonomyList(params);
	}
	
	public int selectChildCount(Integer parentId) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("parentId", parentId);
		
		return selectTaxonomyCount(params);
	}

	public List<Taxonomy> selectTaxonomyList(Map<String, Object> params) throws Exception {
		return selectList("Taxonomy.select", params);
	}

	public int selectTaxonomyCount(Map<String, Object> params) throws Exception {
		return selectOne("Taxonomy.count", params);
	}
	
	public List<Taxonomy> selectTaxonNode(Map<String,Object> params) throws Exception{
		return selectList("Taxonomy.selectTreeNode", params);
	}
}
