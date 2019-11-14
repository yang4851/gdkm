package com.insilicogen.gdkm.model;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Code implements Serializable {
	
	private static final long serialVersionUID = -4939627567540332244L;

	private String code;
	
	private String name;
	
	private String group;
	
	private String useYn;
	
	private Date registDate;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getUseYn() {
		return useYn;
	}

	public void setUseYn(String useYn) {
		this.useYn = useYn;
	}

	public Date getRegistDate() {
		return registDate;
	}

	public void setRegistDate(Date registDate) {
		this.registDate = registDate;
	}

	@Override
	public boolean equals(Object obj){
		if(obj == null || getClass() != obj.getClass())
			return false;
		
		if(this == obj)
			return true;
		
		Code comp = (Code)obj;
		if(StringUtils.isBlank(code) || StringUtils.isBlank(comp.getCode()))
			return false;
		
//		if(StringUtils.equals(group, comp.getGroup())) {
//			return StringUtils.equals(code, comp.getCode());
//		}
//		
//		return false;
		
		return StringUtils.equals(code, comp.getCode());
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
	
}
