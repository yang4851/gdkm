package com.insilicogen.gdkm.model;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.insilicogen.gdkm.Globals;

@JsonInclude(Include.NON_NULL)
public class Research extends AbstractIdObject {

	private static final long serialVersionUID = -2794500298322942106L;

	/** 
	 * 등록번호
	 */
	private String registNo;
	
	private String title;
	
	private String journal;
	
	private String volume;
	
	private String issue;
	
	private String pages;
	
	private String published;
	
	private String submitter;
	
	private String submitOrgan;
	
	private String fullTextLink;
	
	/**
	 * 논문 내용
	 */
	private String content;
	
	private String openYn = Globals.STATUS_OPEN_N;
		
	private Date openDate;
	
	/**
	 * 등록상태 (기본은 대기상태)
	 *  - 준비 = ready
	 *  - 검증 = valid
	 *  - 완료 = complete
	 */
	private String registStatus = Globals.STATUS_REGIST_READY;
	
	private User registUser;
	
	private Date registDate;
	
	private User updateUser;
	
	private Date updateDate;

	private List<ResearchAttachment> attachmentList;
	
	private List<ResearchOmicsFile> omicsList;
	
	private List<NgsDataRegist> linkedList;
	
	public Research() {
		super();
	}
	
	public Research(int id) {
		setId(id);
	}
	
	public String getRegistNo() {
		return registNo;
	}

	public void setRegistNo(String registNo) {
		this.registNo = registNo;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getJournal() {
		return journal;
	}

	public void setJournal(String journal) {
		this.journal = journal;
	}

	public String getVolume() {
		return volume;
	}

	public void setVolume(String volume) {
		this.volume = volume;
	}

	public String getIssue() {
		return issue;
	}

	public void setIssue(String issue) {
		this.issue = issue;
	}

	public String getPages() {
		return pages;
	}

	public void setPages(String pages) {
		this.pages = pages;
	}

	public String getPublished() {
		return published;
	}

	public void setPublished(String published) {
		this.published = published;
	}

	public String getSubmitter() {
		return submitter;
	}

	public void setSubmitter(String submitter) {
		this.submitter = submitter;
	}

	public String getSubmitOrgan() {
		return submitOrgan;
	}

	public void setSubmitOrgan(String submitOrgan) {
		this.submitOrgan = submitOrgan;
	}

	public String getFullTextLink() {
		return fullTextLink;
	}

	public void setFullTextLink(String fullTextLink) {
		this.fullTextLink = fullTextLink;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
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
	
	public List<ResearchAttachment> getAttachmentList() {
		return attachmentList;
	}

	public void setAttachmentList(List<ResearchAttachment> attachmentList) {
		this.attachmentList = attachmentList;
	}

	public List<ResearchOmicsFile> getOmicsList() {
		return omicsList;
	}

	public void setOmicsList(List<ResearchOmicsFile> omicsList) {
		this.omicsList = omicsList;
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
