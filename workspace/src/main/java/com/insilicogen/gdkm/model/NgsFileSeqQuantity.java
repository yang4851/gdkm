package com.insilicogen.gdkm.model;

import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class NgsFileSeqQuantity extends AbstractIdObject {

	private static final long serialVersionUID = -7785400981547409920L;

	private int achiveId;
	
	private NgsDataAchive achive;
	
	private int readCount;
	
	private int totalLength;
	
	private float averageLength;
	
	private int numberOfA;
	
	private int numberOfT;
	
	private int numberOfG;
	
	private int numberOfC;
	
	private int numberOfN;
	
	private int n50;
	
	private Date registDate;
	
	private User registUser;

	
	public int getAchiveId() {
		return achiveId;
	}

	public void setAchiveId(int achiveId) {
		this.achiveId = achiveId;
	}

	public NgsFileSeqQuantity() {
		super();
	}
	
	public NgsFileSeqQuantity(int id) {
		super(id);
	}
	
	public NgsDataAchive getAchive() {
		return achive;
	}

	public void setAchive(NgsDataAchive achive) {
		this.achive = achive;
	}

	public int getReadCount() {
		return readCount;
	}

	public void setReadCount(int readCount) {
		this.readCount = readCount;
	}

	public int getTotalLength() {
		return totalLength;
	}

	public void setTotalLength(int totalLength) {
		this.totalLength = totalLength;
	}

	public float getAverageLength() {
		return averageLength;
	}

	public void setAverageLength(float averageLength) {
		this.averageLength = averageLength;
	}

	public int getNumberOfA() {
		return numberOfA;
	}

	public void setNumberOfA(int numberOfA) {
		this.numberOfA = numberOfA;
	}

	public int getNumberOfT() {
		return numberOfT;
	}

	public void setNumberOfT(int numberOfT) {
		this.numberOfT = numberOfT;
	}

	public int getNumberOfG() {
		return numberOfG;
	}

	public void setNumberOfG(int numberOfG) {
		this.numberOfG = numberOfG;
	}

	public int getNumberOfC() {
		return numberOfC;
	}

	public void setNumberOfC(int numberOfC) {
		this.numberOfC = numberOfC;
	}

	public int getNumberOfN() {
		return numberOfN;
	}

	public void setNumberOfN(int numberOfN) {
		this.numberOfN = numberOfN;
	}

	public int getN50() {
		return n50;
	}

	public void setN50(int n50) {
		this.n50 = n50;
	}

	public Date getRegistDate() {
		return registDate;
	}

	public void setRegistDate(Date registDate) {
		this.registDate = registDate;
	}

	public User getRegistUser() {
		return registUser;
	}

	public void setRegistUser(User registUser) {
		this.registUser = registUser;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
	
}
