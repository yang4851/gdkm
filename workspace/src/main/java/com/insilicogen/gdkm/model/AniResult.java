package com.insilicogen.gdkm.model;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class AniResult {
	
	private List<String> sources;
	private List<List<String>> targets;
	private String imgDir;
	
	public List<String> getSources() {
		return sources;
	}
	public void setSources(List<String> sources) {
		this.sources = sources;
	}
	public List<List<String>> getTargets() {
		return targets;
	}
	public void setTargets(List<List<String>> targets) {
		this.targets = targets;
	}
	public String getImgDir() {
		return imgDir;
	}
	public void setImgDir(String imgDir) {
		this.imgDir = imgDir;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
