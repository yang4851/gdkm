package com.insilicogen.gdkm.web;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.insilicogen.exception.ZeroCountException;
import com.insilicogen.gdkm.Globals;
import com.insilicogen.gdkm.exception.UserException;
import com.insilicogen.gdkm.exception.UserLoginException;
import com.insilicogen.gdkm.model.PageableList;
import com.insilicogen.gdkm.model.User;
import com.insilicogen.gdkm.service.UserService;
import com.insilicogen.gdkm.util.EgovStringUtil;

@RestController
public class UserController extends AbstractController{
	
	@Resource(name="messageSource")
	private MessageSource messageSource;
	
	@Resource(name="UserService")
	UserService userService;

	@RequestMapping(value="/users", method=RequestMethod.POST)
	public void createUser(
			@RequestBody User user
			,HttpSession session
			) throws Exception{
		checkAuthorization(session);
		try{
			synchronized(UserService.Lock){
				userService.createUser(user);
			}
		}catch(Exception e){
			String dataType = messageSource.getMessage("ENTITY.USER", null, Locale.US);
			String message = messageSource.getMessage("ERR022", new String[]{ dataType, e.getMessage() }, Locale.US);
			throw new UserException(message, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value="/users/{user_id}", method=RequestMethod.PUT)
	public void changeUser(@PathVariable("user_id") String userId
			,@RequestBody User user
			,HttpSession session
			) throws Exception{
		checkAuthorization(session);
		try{
			synchronized(UserService.Lock){
				int changeCnt = userService.changeUser(user);
				if(changeCnt == 0)
					throw new ZeroCountException();
			}
		}catch(Exception e){
			String dataType = messageSource.getMessage("ENTITY.USER", null, Locale.US);
			String message = messageSource.getMessage("ERR023", new String[]{ dataType, e.getMessage() }, Locale.US);
			throw new UserException(message, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value="/users/{user_id}/password", method=RequestMethod.PUT)
	public void changeUserPw(@PathVariable("user_id") String userId
			,@RequestBody User user ,HttpSession session) throws Exception{
		try{
			User sessionUser = checkAuthorization(session);
			if("admin".equals(sessionUser.getAuthority())) {
				sessionUser = userService.getUser(sessionUser.getUserId());
			} else if(StringUtils.equals(sessionUser.getUserId(), user.getUserId())) {
				sessionUser = userService.getUser(user.getUserId());
			} else {
				throw new IllegalArgumentException("변경 권한 없음");
			}
			
			String confirmPwd = EgovStringUtil.getCryptoSHA256String(user.getAuthPw());
			if(!StringUtils.equals(confirmPwd, sessionUser.getPassword()))
				throw new IllegalArgumentException("비밀번호 미일치");
			
			User source = userService.getUser(user.getUserId());
			source.setPassword(user.getPassword());
			
			synchronized(UserService.Lock){
				if(userService.changeUserPw(source) == 0)
					throw new ZeroCountException();
			}
		}catch(Exception e){
			String dataType = messageSource.getMessage("ENTITY.USER.PASSWORD", null, Locale.US);
			String message = messageSource.getMessage("ERR023", new String[]{ dataType, e.getMessage() }, Locale.US);
			throw new UserException(message, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value="/users/{user_id}", method=RequestMethod.GET)
	public User getUser(
			@PathVariable("user_id") String userId
			,HttpSession session
			) throws Exception{
		checkAuthorization(session);
		User user = new User();
		try{
			user = userService.getUser(userId);
		}catch(Exception e){
			String dataType = messageSource.getMessage("ENTITY.USER", null, Locale.US);
			String message = messageSource.getMessage("ERR024", new String[]{ dataType, e.getMessage() }, Locale.US);
			throw new UserException(message, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if(user==null)
			throw new NullPointerException();
		return user;
	}
	
	@RequestMapping(value="/users", method=RequestMethod.GET)
	public PageableList<User> getUserList(
			@RequestParam Map<String, Object> params,
			HttpSession session
			){
		checkAuthorization(session);
		try{
			int page = getCurrentPage(params.get(Globals.PARAM_PAGE));
			int rowSize = getRowSize(params.get(Globals.PARAM_ROW_SIZE));
			int firstIndex = (page-1) * rowSize;
			
			params.put("firstIndex", firstIndex);
			params.put("rowSize", rowSize);
			
			Boolean isBasicForm = getBasicMode(params.get(Globals.PARAM_IS_BASICFORM));
			List<User> userList = userService.getUserList(params, isBasicForm);
			int totalCount = userService.getUserCount(params);
			
			PageableList<User> users = new PageableList<User>();
			users.setRowSize(rowSize);
			users.setPage(page);
			users.setList(userList);
			users.setTotal(totalCount);
			
			return users;
		}catch(Exception e){
			String dataType = messageSource.getMessage("ENTITY.USER", null, Locale.US);
			String message = messageSource.getMessage("ERR024", new String[]{ dataType, e.getMessage() }, Locale.US);
			throw new UserException(message, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value="/users/{user_id}", method=RequestMethod.DELETE)
	public void deleteUser(
			@PathVariable("user_id") String userIds
			,HttpSession session
			) throws Exception{
		checkAuthorization(session);
		
		try{
			synchronized(UserService.Lock){
				int deleteCnt = userService.deleteUser(userIds);
				if(deleteCnt == 0)
					throw new ZeroCountException();
			}
		}catch(Exception e){
			String dataType = messageSource.getMessage("ENTITY.USER", null, Locale.US);
			String message = messageSource.getMessage("ERR025", new String[]{ dataType, e.getMessage() }, Locale.US);
			throw new UserException(message, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value="/login", method=RequestMethod.POST)
	public User login(@RequestBody User user, HttpSession session) throws Exception{
		User loginUser = new User();
		try{
			loginUser = userService.loginAuthentication(user);
			session.setAttribute(Globals.LOGIN_USER, loginUser);
		}catch(NullPointerException ne){
			String message = messageSource.getMessage("ERR014", null, Locale.US);
			throw new UserException(message, HttpStatus.INTERNAL_SERVER_ERROR);
		}catch(UserLoginException ule){
			String message = messageSource.getMessage("ERR015", null, Locale.US);
			throw new UserException(message, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return loginUser;
	}
	
	@RequestMapping(value="/logout", method=RequestMethod.POST)
	public void logout(HttpSession session){
		session.removeAttribute("LOGIN_USER");
	}
	
	/**
	 * parsley를 활용한 중복체크 기능
	 * 200 - true
	 * 400 - false
	 * @author iksu
	 * @date 2018. 1. 25.
	 */
	@RequestMapping(value="/users/check/id", method=RequestMethod.GET)
	public void getUserIdCheck(
			@RequestParam("userId") String id, HttpSession session
			)throws Exception{
		checkAuthorization(session);
		User user = userService.getUser(id);
		if(user==null){
			throw new UserException("사용가능- " , HttpStatus.OK);
		}else{
			throw new UserException(messageSource.getMessage("ERR016", null, Locale.US), HttpStatus.BAD_REQUEST);
		}
	}
	
	/**
	 * parsley를 활용한 userPw일치여부 기능
	 * @author iksu
	 * @date 2018. 1. 25.
	 */
	@RequestMapping(value="/users/check/pw", method=RequestMethod.GET)
	public void getUserPwCheck(@RequestParam("userPw") String pw, HttpSession session)throws Exception {
		User user = (User) session.getAttribute(Globals.LOGIN_USER);
		String id = user.getUserId();
		
		boolean result = userService.comparePassword(id, pw);
		if(result){
			throw new UserException("현재 비밀번호 일치- " , HttpStatus.OK);
		}else{
			throw new UserException("현재 비밀번호 불일치- " , HttpStatus.BAD_REQUEST);
		}
	}
	
}
