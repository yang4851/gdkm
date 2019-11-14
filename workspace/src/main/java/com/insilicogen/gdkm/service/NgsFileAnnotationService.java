package com.insilicogen.gdkm.service;

import com.insilicogen.gdkm.model.NgsDataAchive;
import com.insilicogen.gdkm.model.NgsFileAnnotation;

/**
 * Genebank 파일(gbk) 관리 서비스
 */
public interface NgsFileAnnotationService {

	public NgsFileAnnotation createNgsFileAnnotation(NgsFileAnnotation annotation) throws Exception;
	
	public NgsFileAnnotation changeNgsFileAnnotation(NgsFileAnnotation annotation) throws Exception;
	
	public NgsFileAnnotation getNgsFileAnnotation(Integer annotationId) throws Exception;
	
	public NgsFileAnnotation getNgsFileAnnotation(NgsDataAchive achive) throws Exception;
	
	public int deleteNgsFileAnnotation(NgsFileAnnotation annotation) throws Exception;
	
	public int deleteNgsFileAnnotation(NgsDataAchive achive) throws Exception;
	
}
