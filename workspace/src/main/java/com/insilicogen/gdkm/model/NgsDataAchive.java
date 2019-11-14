package com.insilicogen.gdkm.model;

import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.insilicogen.gdkm.Globals;

@JsonInclude(Include.NON_NULL)
public class NgsDataAchive extends AbstractIdObject {

	private static final long serialVersionUID = 1619913402779667999L;

	/**
	 * 성과물 등록 번호
	 */
	private String registNo;
	
	/**
	 * NGS 데이터 등록 정보
	 */
	private NgsDataRegist dataRegist;
	
	/**
	 * 성과물 파일 이름
	 */
	private String fileName;

	/**
	 * 성과물 파일 종류
	 * 
	 *  fasta = FASTA 형식 (fasta, fa)
	 *  fastq = FASTQ 형식 (fastq, fq)
	 *  genbank = Genbank 형식 (gbk)
	 *  gff = GFF 형식(gff, gff3)
	 *  zipped = 압축 파일(zip, gz, gzip)
	 *  etc = 기타형식 (기타 형식)
	 */
	private String fileType = Globals.FILE_TYPE_OTHER;
	
	/**
	 * 성과물 파일 크기 
	 */
	private long fileSize;
	
	/**
	 * MD5 체크섬
	 */
	private String checksum;
	
	/**
	 * 검증 상태
	 * 
	 * 대기 		= {@link Globals#STATUS_VERIFY_READY}
	 * 검증중		= {@link Globals#STATUS_VERIFY_RUNNING} 
	 * 검증완료(경고) = {@link Globals#STATUS_VERIFY_WARN}
	 * 검증완료(성공) = {@link Globals#STATUS_VERIFY_SUCCESS}
	 * 검증완료(실패) = {@link Globals#STATUS_VERIFY_FAILED}
	 */
	private String verifiStatus;
	
	/**
	 * 검증처리 결과 로그(에러 메시지, 처리 완료 메시지, 처리 경고 메시지)
	 */
	private String verifiError;
	
	/**
	 * 성과물 업로드 상태
	 * 
	 * 업로드 성공 			= {@link Globals#STATUS_REGIST_READY}
	 * 검증중				= {@link Globals#STATUS_REGIST_VALIDATING} 
	 * 업로드(등록) 실패	= {@link Globals#STATUS_REGIST_FAILED}
	 * 등록 완료 			= {@link Globals#STATUS_REGIST_SUCCESS}
	 */
	private String registStatus = Globals.STATUS_REGIST_READY;
	
	/**
	 * 등록 사용자 
	 */
	private User registUser;
	
	/**
	 * 등록 일시
	 */
	private Date registDate;
	
	/**
	 * 변경 사용자 
	 */
	private User updateUser;
	
	/**
	 * 변경 일시
	 */
	private Date updateDate;

	public NgsDataAchive() {
		super();
	}
	
	public NgsDataAchive(Integer achiveId) {
		super(achiveId);
	}
	
	public String getRegistNo() {
		return registNo;
	}

	public void setRegistNo(String registNo) {
		this.registNo = registNo;
	}

	public NgsDataRegist getDataRegist() {
		return dataRegist;
	}

	public void setDataRegist(NgsDataRegist dataRegist) {
		this.dataRegist = dataRegist;
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

	public String getVerifiStatus() {
		return verifiStatus;
	}

	public void setVerifiStatus(String verifiStatus) {
		this.verifiStatus = verifiStatus;
	}

	public String getVerifiError() {
		return verifiError;
	}

	public void setVerifiError(String verifiError) {
		this.verifiError = verifiError;
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

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
	
}
