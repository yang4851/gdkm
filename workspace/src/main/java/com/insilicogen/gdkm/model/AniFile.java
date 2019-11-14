package com.insilicogen.gdkm.model;

import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class AniFile {

	private String achiveRegistNo;
	private String dataRegistNo;
	private Integer achiveId;
	private Integer registId;
	private String fileName;
	private String fileType = "other";
	private long fileSize;
	private String checksum;
	private Taxonomy taxonomy;
	private String strain;
	private Date registDate;
	private String dataType;
	
	private String aniLabel;
	private NgsFileSeqQuantity seqQuantityVO;
//	private NgsProcessedData processedData;
	
	public String getAchiveRegistNo() {
		return achiveRegistNo;
	}
	public void setAchiveRegistNo(String achiveRegistNo) {
		this.achiveRegistNo = achiveRegistNo;
	}
	public String getDataRegistNo() {
		return dataRegistNo;
	}
	public void setDataRegistNo(String dataRegistNo) {
		this.dataRegistNo = dataRegistNo;
	}
	public Integer getAchiveId() {
		return achiveId;
	}
	public void setAchiveId(Integer achiveId) {
		this.achiveId = achiveId;
	}
	public Integer getRegistId() {
		return registId;
	}
	public void setRegistId(Integer registId) {
		this.registId = registId;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public long getFileSize() {
		return fileSize;
	}
	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}
	public String getChecksum() {
		return checksum;
	}
	public void setChecksum(String checksum) {
		this.checksum = checksum;
	}
	public Taxonomy getTaxonomy() {
		return taxonomy;
	}
	public void setTaxonomy(Taxonomy taxonomy) {
		this.taxonomy = taxonomy;
	}
	public String getStrain() {
		return strain;
	}
	public void setStrain(String strain) {
		this.strain = strain;
	}
	public Date getRegistDate() {
		return registDate;
	}
	public void setRegistDate(Date registDate) {
		this.registDate = registDate;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	
//	public NgsProcessedData getProcessedData() {
//		return processedData;
//	}
//	public void setProcessedData(NgsProcessedData processedData) {
//		this.processedData = processedData;
//	}
	public NgsFileSeqQuantity getSeqQuantityVO() {
		return seqQuantityVO;
	}
	public void setSeqQuantityVO(NgsFileSeqQuantity seqQuantityVO) {
		this.seqQuantityVO = seqQuantityVO;
	}
	public String getAniLabel() {
		return aniLabel;
	}
	public void setAniLabel(String aniLabel) {
		this.aniLabel = aniLabel;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
