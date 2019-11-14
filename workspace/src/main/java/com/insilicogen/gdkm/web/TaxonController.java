package com.insilicogen.gdkm.web;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.insilicogen.gdkm.Globals;
import com.insilicogen.gdkm.exception.TaxonomyException;
import com.insilicogen.gdkm.model.PageableList;
import com.insilicogen.gdkm.model.Taxonomy;
import com.insilicogen.gdkm.service.TaxonomyService;

@RestController
public class TaxonController extends AbstractController{

	static Comparator<Taxonomy> SORT_BY_NAME = new Comparator<Taxonomy>() {
		@Override
		public int compare(Taxonomy o1, Taxonomy o2) {
			String name1 = o1.getName().toUpperCase();
			String name2 = o2.getName().toUpperCase();	
			return name1.compareTo(name2);
		}
	};
	
	@Resource(name="TaxonomyService")
	TaxonomyService taxonomyService;
	
	/**
	 * 
	 * @param params
	 * 	- taxonId : taxonId 가 일치하는 1개의 목록 검색
	 * 	- parentId : 해당 ID의 하위 Taxonomy 목록 검색
	 * 	- rank : 계통분류가 rank에 해당하는 Taxonomy 목록 검색
	 * 	- name : 계통분류 이름이 동일한 Taxonomy 목록 검색
	 *  - keyword : 계통분류 이름이 포함된 Taxonomy 목록 검색
	 * 	- registered : NGS 데이터 등록에 사용된 Taxonomy 목록 검색 
	 * 	- dataType : NGS 등록 데이터 종류({@link Globals#REGIST_DATA_RAWDATA}, {@link Globals#REGIST_DATA_PROCESSEDDATA})에 해당하는 Taxonomy 목록 검색  	
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/taxonomies", method=RequestMethod.GET)
	public PageableList<Taxonomy> getTaxonomyList(@RequestParam Map<String, Object> params)throws Exception{
		try{
			int page = getCurrentPage(params.get(Globals.PARAM_PAGE));
			int rowSize = getRowSize(params.get(Globals.PARAM_ROW_SIZE));
			int firstIndex = (page-1) * rowSize;
			
			params.put(Globals.PARAM_FIRST_INDEX, firstIndex);
			params.put(Globals.PARAM_ROW_SIZE, rowSize);
			
			List<Taxonomy> taxonomyList = taxonomyService.getTaxonomyList(params);
			int totalCount = taxonomyService.getTaxonommyCount(params);
			
			PageableList<Taxonomy> pageableList = new PageableList<Taxonomy>();
			pageableList.setRowSize(rowSize);
			pageableList.setPage(page);
			pageableList.setList(taxonomyList);
			pageableList.setTotal(totalCount);
			
			return pageableList;
		} catch(TaxonomyException e) {
			throw e;
		} catch(Exception e) {
			throw new TaxonomyException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/**
	 * 
	 * @param params
	 * 	- taxonId : taxonId 가 일치하는 1개의 목록 검색
	 * 	- parentId : 해당 ID의 하위 Taxonomy 목록 검색
	 * 	- rank : 계통분류가 rank에 해당하는 Taxonomy 목록 검색
	 * 	- name : 계통분류 이름이 동일한 Taxonomy 목록 검색
	 *  - keyword : 계통분류 이름이 포함된 Taxonomy 목록 검색
	 * 	- registered : NGS 데이터 등록에 사용된 Taxonomy 목록 검색  	
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/taxonomies/tree", method=RequestMethod.GET)
	public List<Taxonomy> getTaxonomyTree(@RequestParam Map<String, Object> params)throws Exception{
		try{
			List<Taxonomy> taxonomyList = new ArrayList<Taxonomy>();
			
			if(hasStringValue(params, "keyword")) {
				List<Taxonomy> searchedList = taxonomyService.getTaxonomyList(params);
				
				for(Taxonomy taxonomy : searchedList) {
					List<Taxonomy> ancestorList = taxonomyService.getHierarchyList(taxonomy, true);
					List<Taxonomy> childList = taxonomyList;
					
					for(Taxonomy parent : ancestorList) {
						int index = childList.indexOf(parent);
						if(index < 0) {
							childList.add(parent);
							Collections.sort(childList, SORT_BY_NAME);
							
							if(parent.getNumberOfChild() > 0)
								parent.setChildren(new ArrayList<Taxonomy>());
						} else {
							parent = childList.get(index);
						}
						
						childList = parent.getChildren();
						if(childList == null)
							break;
					}
				}
			} else if(hasIntegerValue(params, "parentId")) {
				taxonomyList = taxonomyService.getTaxonomyList(params);
			} else {
				if(params.containsKey("rank")){
					params.remove("rank");
					taxonomyList = taxonomyService.getTaxonomyList(params);
				}else{
					params.put("rank", "superkingdom");
					taxonomyList = taxonomyService.getTaxonomyList(params);
				}
			}
			
			Collections.sort(taxonomyList, SORT_BY_NAME);
			
			return taxonomyList;
		} catch(TaxonomyException e) {
			throw e;
		} catch(Exception e) {
			throw new TaxonomyException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	

	@RequestMapping(value="/taxonomies/integrated-tree", method=RequestMethod.GET)
	public List<Taxonomy> getTaxonNode(
			@RequestParam Map<String,Object> params
			)throws Exception{
		return taxonomyService.getTaxonNode(params);
	}
	
	
	@RequestMapping(value="/taxonomies/{taxonId}", method=RequestMethod.GET)
	public Taxonomy getTaxonomy(@PathVariable("taxonId") Integer taxonId)throws Exception{
		try{
			return taxonomyService.getTaxonomy(taxonId);
		}catch(Exception e){
			throw new TaxonomyException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value="/taxonomies/{taxonId}/hierarchies", method=RequestMethod.GET)
	public List<Taxonomy> getHierarchyList(@PathVariable("taxonId") Integer taxonId)throws Exception{
		try{
			Taxonomy taxonomy = taxonomyService.getTaxonomy(taxonId);
			if(taxonomy == null) 
				throw new TaxonomyException("Could not find taxonomy '" + taxonId + "'");
			
			return taxonomyService.getHierarchyList(taxonomy);
		} catch(TaxonomyException e) {
			throw e;
		} catch(Exception e) {
			throw new TaxonomyException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	static boolean hasIntegerValue(Map<String, Object> params, String key) {
		if(params == null || params.size() == 0)
			return false;
		
		try {
			Integer value = Integer.valueOf(params.get(key).toString());
			return (value > 0);
		} catch (Exception e) {
			return false;
		}
	}
	
	static boolean hasStringValue(Map<String, Object> params, String key) {
		if(params == null || params.size() == 0)
			return false;
		
		try {
			return StringUtils.isNotBlank(params.get(key).toString());
		} catch (Exception e) {
			return false;
		}
	}
}
