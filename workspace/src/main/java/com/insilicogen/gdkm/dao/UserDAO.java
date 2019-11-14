package com.insilicogen.gdkm.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.insilicogen.gdkm.model.User;

import egovframework.rte.psl.dataaccess.EgovAbstractMapper;
@Repository("UserDAO")
public class UserDAO extends EgovAbstractMapper{
	
	public int insertUser(User user) throws Exception{
		return insert("User.insert",user);
	}
	
	public User selectUser(String userId) throws Exception{
		Map<String, Object> conditions = new HashMap<String, Object>();
		conditions.put("userId", userId);
		
		return selectOne("User.get", userId);
	}
	
	public User selectUser(User user) throws Exception{
		return selectOne("User.getAsObject", user);
	}
	
	public List<User> selectList(Map<String, Object> params, boolean isBasicForm) throws Exception{
		String queryId = (isBasicForm) ? "User.selectBasic" : "User.select";
		return selectList(queryId,params);
	}
	
	public int getUserCount(Map<String, Object> params) throws Exception {
		return selectOne("User.count", params);
	}

	
	public int updateUser(User user) throws Exception{
		return update("User.update", user);
	}
	
	public int updateUserPw(User user) throws Exception{
		return update("User.updatePw", user);
	}
	
	public int deleteUser(String[] userIds) throws Exception{
		return delete("User.delete", userIds);
	}

}
