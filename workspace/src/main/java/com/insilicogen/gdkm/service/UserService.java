package com.insilicogen.gdkm.service;

import java.util.List;
import java.util.Map;

import com.insilicogen.gdkm.model.User;

public interface UserService {
	
	public static final Object Lock = new Object();
	
	public int createUser(User user) throws Exception;
	
	public User getUser(String id) throws Exception;
	
	public List<User> getUserList(Map<String, Object> params, boolean isBasicForm) throws Exception;
	
	public int getUserCount(Map<String, Object> params) throws Exception;
	
	public int changeUser(User user) throws Exception;
	
	public int changeUserPw(User user) throws Exception;
	
	public int deleteUser(String ids) throws Exception;
	
	public User loginAuthentication(User user) throws Exception;
	
	public boolean comparePassword(String id, String pw) throws Exception;
}
