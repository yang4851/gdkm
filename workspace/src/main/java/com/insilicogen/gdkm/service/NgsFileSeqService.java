package com.insilicogen.gdkm.service;

import com.insilicogen.gdkm.model.NgsDataAchive;
import com.insilicogen.gdkm.model.NgsFileSeqQuantity;

/**
 * NGS 서열파일(fasta, fastq) 관리 서비스
 */
public interface NgsFileSeqService {

	public NgsFileSeqQuantity createNgsFileSeqQuantity(NgsFileSeqQuantity quantity) throws Exception;
	
	public NgsFileSeqQuantity changeNgsFileSeqQuantity(NgsFileSeqQuantity quantity) throws Exception;
	
	public NgsFileSeqQuantity getNgsFileSeqQuantity(Integer quantityId) throws Exception;
	
	public NgsFileSeqQuantity getNgsFileSeqQuantity(NgsDataAchive achive) throws Exception;
	
	public int deleteNgsFileSeqQuantity(NgsFileSeqQuantity quantity) throws Exception;
	
	public int deleteNgsFileSeqQuantity(NgsDataAchive achive) throws Exception;
}
