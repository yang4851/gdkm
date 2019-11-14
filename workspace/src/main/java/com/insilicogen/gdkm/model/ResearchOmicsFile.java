package com.insilicogen.gdkm.model;

public class ResearchOmicsFile extends Attachment {

	private static final long serialVersionUID = -2297312054630155992L;

	private String registNo;
	
	private Research research;
	
	private Code category;
	
	private String description;

	public ResearchOmicsFile() {
		super();
	}
	
	public ResearchOmicsFile(int id) {
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

	public Code getCategory() {
		return category;
	}

	public void setCategory(Code category) {
		this.category = category;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
