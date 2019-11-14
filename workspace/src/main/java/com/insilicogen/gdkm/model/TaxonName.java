package com.insilicogen.gdkm.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class TaxonName{
	
	private static final long serialVersionUID = -2219168075548797135L;
	
	private int taxonId;
	
	private String name;
	
	private String nameClass;
	
	public int getTaxonId() {
		return taxonId;
	}

	public void setTaxonId(int taxonId) {
		this.taxonId = taxonId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNameClass() {
		return nameClass;
	}

	public void setNameClass(String nameClass) {
		this.nameClass = nameClass;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
