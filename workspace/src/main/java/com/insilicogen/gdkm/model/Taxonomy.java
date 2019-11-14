package com.insilicogen.gdkm.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * 생물종 계통분류 객체
 * @author sungjin
 */
@JsonInclude(Include.NON_NULL)
public class Taxonomy implements Serializable {

	private static final long serialVersionUID = 4642992836521596789L;
	
	private int taxonId;
	
	private int ncbiTaxonId;
	
	private int parentId;
	
	/**
	 * 생물 분류명(종명)
	 */
	private String name;
	
	/**
	 * 생물 분류 단계 순위
	 */
	private String rank;
	
	/**
	 * 하위노드 개수
	 */
	private int numberOfChild;
	
	/**
	 * 하위노드 목록
	 */
	private List<Taxonomy> children;

	public int getTaxonId() {
		return taxonId;
	}

	public void setTaxonId(int taxonId) {
		this.taxonId = taxonId;
	}

	public int getNcbiTaxonId() {
		return ncbiTaxonId;
	}

	public void setNcbiTaxonId(int ncbiTaxonId) {
		this.ncbiTaxonId = ncbiTaxonId;
	}

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}
	
	public int getNumberOfChild() {
		return numberOfChild;
	}

	public void setNumberOfChild(int numberOfChild) {
		this.numberOfChild = numberOfChild;
	}

	public List<Taxonomy> getChildren() {
		return children;
	}

	public void setChildren(List<Taxonomy> children) {
		this.children = children;
	}

	@Override
	public boolean equals(Object obj){
		if(obj == null || getClass() != obj.getClass())
			return false;
		
		if(this == obj)
			return true;
		
		return (taxonId == ((Taxonomy)obj).getTaxonId());
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
	
}
