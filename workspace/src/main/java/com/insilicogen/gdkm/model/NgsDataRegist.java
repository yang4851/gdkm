package com.insilicogen.gdkm.model;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * NGS 데이터 등록 기본정보
 * 
 * @see NgsRawData
 * @see NgsAnnotationData
 */
@JsonInclude(Include.NON_NULL)
public class NgsDataRegist extends AbstractIdObject {

	private static final long serialVersionUID = 9167166683221242601L;
	
	/**
	 * NGS 데이터 등록번호
	 */
	private String registNo;

	/**
	 * NGS 등록 데이터 유형
	 *  - rawdata : NGS 원시 데이터 ({@link NgsRawData})
	 *  - processed : NGS 분석 데이터 ({@link NgsProcessedData}) 
	 */
	private String dataType;
	
	/**
	 * 생물종 계통분류 정보 ({@link Taxonomy})
	 */
	private Taxonomy taxonomy;

	/**
	 * 시료 균주 정보(이름)
	 */
	private String strain;
	
	/**
	 * {@link #dataType}이 raw 인 경우 rawData 정보 호출
	 */
	private NgsRawData rawData;
	
	/**
	 * {@link #dataType}이 processed 인 경우 processedData;
	 */
	private NgsProcessedData processedData;
	
	/**
	 * 공개여부 (기본은 비공개 상태)
	 *  - 공개 = Y
	 *  - 비공개 = N
	 */
	private String openYn = "N";
	
	/**
	 * 공개일 (시간은 0시 0분 0초)
	 */
	private Date openDate;
	
	/**
	 * TODO 정확하게 정리해야 함.
	 * 
	 * 제출 승인 상태 (기본은 대기 상태)
	 *  - 대기 = ready
	 *  - 승인 = commit
	 *  - 반려 = reject
	 */
	private String approvalStatus;
	
	/**
	 * 제출 승인 사용자
	 */
	private User approvalUser;
	
	/**
	 * 제출 승인 날짜(시간은 0시 0분 0초)
	 */
	private Date approvalDate;
	
	/**
	 * TODO 정확하게 정리해야 함. 
	 * 
	 * 등록상태 (기본은 대기상태)
	 *  - 준비 = ready
	 *  - 제출 = submitting
	 *  - 검증 = valid
	 *  - 완료 = complete
	 */
	private String registStatus = "ready";
	
	/**
	 * NGS 데이터 정보 등록 사용자
	 */
	private User registUser;
	
	/**
	 * NGS 데이터 정보 등록 일시
	 */
	private Date registDate;
	
	/**
	 * NGS 데이터 정보 변경 사용자
	 */
	private User updateUser;
	
	/**
	 * NGS 데이터 정보 변경 일시
	 */
	private Date updateDate;
	
	/**
	 * NGS 원시데이터와 가공데이터 연결된 데이터 목록 
	 */
	private List<NgsDataRegist> linkedList;
	
	public NgsDataRegist() {
		super();
	}
	
	public NgsDataRegist(Integer registId) {
		super(registId);
	}
	
	public String getRegistNo() {
		return registNo;
	}

	public void setRegistNo(String registNo) {
		this.registNo = registNo;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
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

	public NgsRawData getRawData() {
		return rawData;
	}

	public void setRawData(NgsRawData rawData) {
		this.rawData = rawData;
	}

	public NgsProcessedData getProcessedData() {
		return processedData;
	}

	public void setProcessedData(NgsProcessedData processedData) {
		this.processedData = processedData;
	}

	public String getOpenYn() {
		return openYn;
	}

	public void setOpenYn(String openYn) {
		this.openYn = openYn;
	}

	public Date getOpenDate() {
		return openDate;
	}

	public void setOpenDate(Date openDate) {
		this.openDate = openDate;
	}

	public String getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(String approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

	public User getApprovalUser() {
		return approvalUser;
	}

	public void setApprovalUser(User approvalUser) {
		this.approvalUser = approvalUser;
	}

	public Date getApprovalDate() {
		return approvalDate;
	}

	public void setApprovalDate(Date approvalDate) {
		this.approvalDate = approvalDate;
	}

	public String getRegistStatus() {
		return registStatus;
	}

	public void setRegistStatus(String registStatus) {
		this.registStatus = registStatus;
	}

	public User getRegistUser() {
		return registUser;
	}

	public void setRegistUser(User registUser) {
		this.registUser = registUser;
	}

	public Date getRegistDate() {
		return registDate;
	}

	public void setRegistDate(Date registDate) {
		this.registDate = registDate;
	}

	public User getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(User updateUser) {
		this.updateUser = updateUser;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public List<NgsDataRegist> getLinkedList() {
		return linkedList;
	}
	
	public void setLinkedList(List<NgsDataRegist> linkedList) {
		this.linkedList = linkedList;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
	
}
