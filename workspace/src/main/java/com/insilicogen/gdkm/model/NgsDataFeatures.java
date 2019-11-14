package com.insilicogen.gdkm.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class NgsDataFeatures implements Serializable {
	
	private static final long serialVersionUID = 6212719887993941342L;
	
	private int featuresId;
	private int headerId;
	private String type;
	private String gene;
	private int start;
	private int end;
	private String strand;
	private String product;
	private String sequence;
	private int codonStart;
	private String ecNumber;
	private String proteinId;
	private int translTable;
	private String locusTag;
	
	public int getFeaturesId() {
		return featuresId;
	}
	public void setFeaturesId(int featuresId) {
		this.featuresId = featuresId;
	}
	public int getHeaderId() {
		return headerId;
	}
	public void setHeaderId(int headerId) {
		this.headerId = headerId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getGene() {
		return gene;
	}
	public void setGene(String gene) {
		this.gene = gene;
	}
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getEnd() {
		return end;
	}
	public void setEnd(int end) {
		this.end = end;
	}
	public String getStrand() {
		return strand;
	}
	public void setStrand(String strand) {
		this.strand = strand;
	}
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}
	public String getSequence() {
		return sequence;
	}
	public void setSequence(String sequence) {
		this.sequence = sequence;
	}
	
	public int getCodonStart() {
		return codonStart;
	}
	public void setCodonStart(int codonStart) {
		this.codonStart = codonStart;
	}
	public String getEcNumber() {
		return ecNumber;
	}
	public void setEcNumber(String ecNumber) {
		this.ecNumber = ecNumber;
	}
	public String getProteinId() {
		return proteinId;
	}
	public void setProteinId(String proteinId) {
		this.proteinId = proteinId;
	}
	public int getTranslTable() {
		return translTable;
	}
	public void setTranslTable(int translTable) {
		this.translTable = translTable;
	}
	public String getLocusTag() {
		return locusTag;
	}
	public void setLocusTag(String locusTag) {
		this.locusTag = locusTag;
	}
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
