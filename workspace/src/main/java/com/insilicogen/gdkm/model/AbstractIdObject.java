package com.insilicogen.gdkm.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class AbstractIdObject implements Serializable{
	
	private static final long serialVersionUID = -854492786272701132L;
	
	private int id;
	
	protected AbstractIdObject(){
		super();
	}
	
	protected AbstractIdObject(int id){
		super();
		this.id = id;
	}
	
	public int getId(){
		return id;
	}
	
	public void setId(int id){
		this.id = id;
	}
	
	@Override
	public boolean equals(Object obj){
		if(obj == null || getClass() != obj.getClass())
			return false;
		
		if(this == obj)
			return true;
		
		AbstractIdObject model = (AbstractIdObject) obj;
		if(id<1 || model.getId()<1)
			return false;
		
		return (id == model.getId());
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
