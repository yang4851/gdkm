package com.insilicogen.gdkm.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ResearchAttachment extends Attachment {

	private static final long serialVersionUID = -4840694891401502341L;

	private String registNo;
	
	private Research research;
	
	private String description;

	public ResearchAttachment() {
		super();
	}
	
	public ResearchAttachment(int id) {
		setId(id);
	}
	
	public String getRegistNo() {
		return registNo;
	}

	public void setRegistNo(String registNo) {
		this.registNo = registNo;
	}

	public Research getResearch() {
		return research;
	}

	public void setResearch(Research research) {
		this.research = research;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
