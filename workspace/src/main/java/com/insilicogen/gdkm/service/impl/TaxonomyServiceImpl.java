package com.insilicogen.gdkm.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.insilicogen.gdkm.dao.TaxonomyDAO;
import com.insilicogen.gdkm.exception.NgsDataException;
import com.insilicogen.gdkm.model.Taxonomy;
import com.insilicogen.gdkm.service.TaxonomyService;

@Service("TaxonomyService")
public class TaxonomyServiceImpl implements TaxonomyService {

	static Logger logger = LoggerFactory.getLogger(TaxonomyServiceImpl.class);
	
	@Resource(name="TaxonomyDAO")
	TaxonomyDAO taxonomyDAO;
	
	@Override
	public Taxonomy getTaxonomy(Integer taxonId) throws Exception {
		if(taxonId == null || taxonId < 1) {
			logger.error("Taxonomy 호출 ID 오류 - taxonId=" + taxonId );
			throw new NgsDataException("Invalid taxon id - " + taxonId, HttpStatus.BAD_REQUEST);
		}
		
		return taxonomyDAO.selectTaxonomy(taxonId);
	}

	@Override
	public List<Taxonomy> getTaxonomyList(Map<String, Object> params) {
		try {
			return taxonomyDAO.selectTaxonomyList(params);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return Collections.emptyList();
		}
	}
	
	@Override
	public int getTaxonommyCount(Map<String, Object> params) {
		try {
			return taxonomyDAO.selectTaxonomyCount(params);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return 0;
		}
	}
	
	@Override
	public List<Taxonomy> getHierarchyList(Taxonomy taxonomy) {
		return getHierarchyList(taxonomy, false);
	}
	
	/**
	 * 파라미터로 입력받은 taxonomy의 상위 계층분류 목록을 모두 호출해 목록으로 반환
	 * 해당 Taxonomy가 어떤 계통분류에 해당하는 지 확인할 때 사용
	 * 
	 * @param includeSub 
	 * 		아종 등 핵심 계통이 아닌 것들도 모두 반환할 것인지 결정, 
	 * 		true 인 경우 : 모든 계통을 포함
	 * 		false 인 경우 : 핵심 카테고리만 반환(코드 내 ranks 변수에 해당하는 것들만 반환)
	 */
	@Override
	public List<Taxonomy> getHierarchyList(Taxonomy taxonomy, boolean includeSub) {
		List<String> ranks = Arrays.asList(new String[]{ "superkingdom", "subkingdom", "kingdom", "phylum", "class", "order", "family", "genus", "species" });
		List<Taxonomy> hierachyList = new ArrayList<Taxonomy>();
		hierachyList.add(taxonomy);
		
		try {
			Taxonomy parent = taxonomy;
			do {
				parent = getTaxonomy(parent.getParentId());
				if(parent == null)
					break;
				
				if(!includeSub && ranks.indexOf(parent.getRank()) < 0)
					continue;
				
				hierachyList.add(0, parent);
			} while(parent.getParentId() > 1);
		} catch(Exception e) {}
		
		return hierachyList;
	}
	
	@Override
	public List<Taxonomy> getChildTaxonomyList(Taxonomy taxonomy) {
		try {
			return taxonomyDAO.selectChildList(taxonomy.getTaxonId());
		} catch (Exception e) {
			return Collections.emptyList();
		}
	}

	@Override
	public int getChildTaxonomyCount(Taxonomy taxonomy) {
		try {
			return taxonomyDAO.selectChildCount(taxonomy.getTaxonId());
		} catch (Exception e) {
			return 0;
		}
	}
	
	@Override
	public List<Taxonomy> getTaxonNode(Map<String,Object> params) throws Exception{
		return taxonomyDAO.selectTaxonNode(params);
	}
}
