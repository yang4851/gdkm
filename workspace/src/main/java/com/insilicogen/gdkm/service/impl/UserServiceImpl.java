package com.insilicogen.gdkm.service.impl;

//import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.insilicogen.gdkm.dao.UserDAO;
import com.insilicogen.gdkm.exception.UserLoginException;
import com.insilicogen.gdkm.model.User;
import com.insilicogen.gdkm.service.UserService;
import com.insilicogen.gdkm.util.EgovStringUtil;

@Service("UserService")
public class UserServiceImpl implements UserService {

	@Resource(name="UserDAO")
	private UserDAO userDAO;

	@Override
	public int createUser(User user) throws Exception {
		user.setPassword(EgovStringUtil.getCryptoSHA256String(user.getPassword()));
		return userDAO.insertUser(user);
	}

	@Override
	public User getUser(String id) throws Exception {
		User user = userDAO.selectUser(id);
		return user;
	}

	@Override
	public List<User> getUserList(Map<String, Object> params, boolean isBasicForm)
			throws Exception {
		List<User> userList = userDAO.selectList(params, isBasicForm);
		return userList;
	}
	
	@Override
	public int getUserCount(Map<String, Object> params) throws Exception {
		return userDAO.getUserCount(params);
	}

	@Override
	public int changeUser(User user) throws Exception {
		user.setPassword(EgovStringUtil.getCryptoSHA256String(user.getPassword()));
		return userDAO.updateUser(user);
	}
	
	@Override
	public int changeUserPw(User user) throws Exception{
		user.setPassword(EgovStringUtil.getCryptoSHA256String(user.getPassword()));
		return userDAO.updateUserPw(user);
	}

	@Override
	public int deleteUser(String ids) throws Exception {
		String[] arrayIds = EgovStringUtil.splitStringToStringArray(ids);
		return userDAO.deleteUser(arrayIds);
	}

	@Override
	public User loginAuthentication(User requestUser) throws Exception {
		
		User selectUser = userDAO.selectUser(requestUser.getUserId());
		
		String selectUserPw = selectUser.getPassword();
		String requestUserPw = EgovStringUtil.getCryptoSHA256String(requestUser.getPassword());
		
		if(selectUserPw.equals(requestUserPw)){
			selectUser.setUserId(selectUser.getUserId());
			return selectUser;
		}else{
			throw new UserLoginException("사용자 아이디와 비밀번호가 일치하지 않습니다.", HttpStatus.UNAUTHORIZED);
		}
	}

	@Override
	public boolean comparePassword(String id, String pw) throws Exception {
		
		User user = userDAO.selectUser(id);
		String requestUserPw = EgovStringUtil.getCryptoSHA256String(pw);
		
		if(user.getPassword().equals(requestUserPw)){
			return true;
		}else{
			return false;
		}
	}
}
