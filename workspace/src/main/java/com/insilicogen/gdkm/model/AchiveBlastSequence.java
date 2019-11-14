package com.insilicogen.gdkm.model;

public class AchiveBlastSequence {
	
	private Integer abseqId;
	private Integer achiveId;
	private String sequenceName;
	
	public Integer getAbseqId() {
		return abseqId;
	}
	public void setAbseqId(Integer abseqId) {
		this.abseqId = abseqId;
	}
	public Integer getAchiveId() {
		return achiveId;
	}
	public void setAchiveId(Integer achiveId) {
		this.achiveId = achiveId;
	}
	public String getSequenceName() {
		return sequenceName;
	}
	public void setSequenceName(String sequenceName) {
		this.sequenceName = sequenceName;
	}
	
	@Override
	public String toString() {
		return "AchiveBlastSequence [abseqId=" + abseqId + ", achiveId=" + achiveId + ", sequenceName=" + sequenceName
				+ "]";
	}
	
}
