package com.insilicogen.gdkm.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.insilicogen.gdkm.model.NgsDataAchive;
import com.insilicogen.gdkm.model.NgsDataRegist;
import com.insilicogen.gdkm.model.NgsFileAnnotation;

import egovframework.rte.psl.dataaccess.EgovAbstractMapper;

@Repository("NgsFileAnnotationDAO")
public class NgsFileAnnotationDAO extends EgovAbstractMapper {
	
	public int insertNgsFileAnnotation(NgsFileAnnotation annotation) throws Exception {
		return insert("NgsFileAnnotation.insert", annotation);
	}

	public NgsFileAnnotation selectNgsFileAnnotation(Integer annotationId) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("annotationId", annotationId);
		
		return selectOne("NgsFileAnnotation.get", params);
	}
	
	public NgsFileAnnotation selectNgsFileAnnotation(NgsDataAchive achive) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("achiveId", achive.getId());
		
		return selectOne("NgsFileAnnotation.get", params);
	}
	
	public List<NgsFileAnnotation> selectNgsFileAnnotationList(Integer registId) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("registId", registId);
		
		return selectList("NgsFileAnnotation.select", params);
	}
	
	public int selectNgsFileAnnotationCount(Integer registId) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("registId", registId);
		
		return selectOne("NgsFileAnnotation.count", params);
	}
	
	public int updateNgsFileAnnotation(NgsFileAnnotation annotation) throws Exception {
		return update("NgsFileAnnotation.update", annotation);
	}
	
	public int deleteNgsFileAnnotation(NgsFileAnnotation annotation) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("annotationId", annotation.getId());
		
		return delete("NgsFileAnnotation.delete", params);
	}

	public int deleteNgsFileAnnotation(NgsDataAchive achive) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("achiveId", achive.getId());
		
		return delete("NgsFileAnnotation.delete", params);
	}
	
	public int deleteNgsFileAnnotation(NgsDataRegist dataRegist) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("registId", dataRegist.getId());
		
		return delete("NgsFileAnnotation.delete", params);
	}
}
