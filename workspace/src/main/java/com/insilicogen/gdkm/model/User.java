package com.insilicogen.gdkm.model;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class User implements Serializable {

	private static final long serialVersionUID = -5133303549625497081L;
	
	private String userId;
	private String password;
	private String name;
	private String email;
	private String institute;
	private String department;
	private String phone;
	private String authority;
	private Date registDate;
	private String updateUser;
	private Date updateDate;
	
	private String authPw;
	private String loginAuthorization;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getInstitute() {
		return institute;
	}
	public void setInstitute(String institution) {
		this.institute = institution;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getAuthority() {
		return authority;
	}
	public void setAuthority(String authority) {
		this.authority = authority;
	}
	public Date getRegistDate() {
		return registDate;
	}
	public void setRegistDate(Date registDate) {
		this.registDate = registDate;
	}
	public String getUpdateUser() {
		return updateUser;
	}
	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public String getAuthPw() {
		return authPw;
	}
	public void setAuthPw(String authPw) {
		this.authPw = authPw;
	}
	public String getLoginAuthorization() {
		return loginAuthorization;
	}
	public void setLoginAuthorization(String loginAuthorization) {
		this.loginAuthorization = loginAuthorization;
	}
	
	@Override
	public boolean equals(Object obj){
		if(obj == null || getClass() != obj.getClass())
			return false;
		
		if(this == obj)
			return true;
		
		if(StringUtils.isBlank(userId) || StringUtils.isBlank(((User)obj).getUserId()))
			return false;
				
		return (userId.equals(((User)obj).getUserId()));
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
	
}
