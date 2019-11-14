package unitTest.dao;

import static org.junit.Assert.assertEquals;

import java.text.SimpleDateFormat;
import java.util.List;

import javax.annotation.Resource;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.insilicogen.gdkm.dao.UserDAO;
import com.insilicogen.gdkm.model.User;
import com.insilicogen.gdkm.util.EgovStringUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations={"classpath:/egovframework/spring/context-*.xml"})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestUserDAO {
	@Resource(name="UserDAO")
	private UserDAO userDAO;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	@Test
	public void test1InsertUser() throws Exception{
		
		for(int i = 1; i < 11; i++){
			User user = new User();
			user.setUserId("testId" + i);
			user.setPassword(EgovStringUtil.getCryptoSHA256String("testPw" + i));
			user.setName("이름" + i);
			user.setEmail("test"+i+"@naver.com");
			user.setInstitute("소속"+i);
			user.setDepartment("부서"+i);
			user.setPhone("010-1111-123"+i);
			//admin,member
			user.setAuthority(i%2==0 ? "admin" : "member");
			user.setUpdateUser("administrator");
			
			int insertCnt = userDAO.insertUser(user);
			assertEquals(1, insertCnt);
			Thread.sleep(1000);
		}
	}

	@Test
	public void test2SelectUser() throws Exception{
		int i = 1;
		String userId = "testId" + i;
		User resultUser = userDAO.selectUser(userId);
		
		assertEquals(EgovStringUtil.getCryptoSHA256String("testPw" + i), resultUser.getPassword());
		assertEquals("이름"+i, resultUser.getName());
		assertEquals("test" + i + "@naver.com", resultUser.getEmail());
		assertEquals("소속"+i, resultUser.getInstitute());
		assertEquals("부서"+i, resultUser.getDepartment());
		assertEquals("010-1111-123"+i, resultUser.getPhone());
		assertEquals(i%2==0 ? "admin" : "member", resultUser.getAuthority());
		assertEquals("administrator", resultUser.getUpdateUser());
	}
	
//	@Test
//	public void test3SelectList() throws Exception{
//		String userId = "";
//		String userNm = "";
//		String institution = "";
//		List<User> userList = userDAO.selectList(userId, userNm, institution, 0, 10);
//		assertEquals(10, userList.size());
//		for(int i = 10; i < userList.size(); i--){
//			User user = userList.get(i);
//			assertEquals("testId" + i, user.getUserId());
//			assertEquals(EgovStringUtil.getCryptoSHA256String("testPw" + i), user.getPassword());
//			assertEquals("이름" + i, user.getName());
//			assertEquals("test" + i + "@naver.com", user.getEmail());
//			assertEquals("소속"+i, user.getInstitute());
//			assertEquals("부서"+i, user.getDepartment());
//			assertEquals("010-1111-123"+i, user.getPhone());
//			assertEquals(i%2==0 ? "admin" : "member", user.getAuthority());
//			assertEquals("administrator", user.getUpdateUser());
//		}
//		
//		userId = "Id3";
//		userNm = "3";
//		institution = "";
//		userList = userDAO.selectList(userId, userNm, institution, 0, 10);
//		assertEquals(1, userList.size());
//		for(int i = 1; i < userList.size(); i--){
//			User user = userList.get(i);
//			assertEquals("testId" + i, user.getUserId());
//			assertEquals(EgovStringUtil.getCryptoSHA256String("testPw" + i), user.getPassword());
//			assertEquals("이름" + i, user.getName());
//			assertEquals("test" + i + "@naver.com", user.getEmail());
//			assertEquals("소속"+i, user.getInstitute());
//			assertEquals("부서"+i, user.getDepartment());
//			assertEquals("010-1111-123"+i, user.getPhone());
//			assertEquals(i%2==0 ? "admin" : "member", user.getAuthority());
//			assertEquals("administrator", user.getUpdateUser());
//		}
//		
//		userId = "Id1";
//		userNm = "";
//		institution = "소속";
//		userList = userDAO.selectList(userId, userNm, institution, 0, 10);
//		assertEquals(2, userList.size());
//		for(int j = 0; j < userList.size(); j++){
//			if(j==0){
//				int i=10;
//				User user = userList.get(0);
//				assertEquals("testId" + i, user.getUserId());
//				assertEquals(EgovStringUtil.getCryptoSHA256String("testPw" + i), user.getPassword());
//				assertEquals("이름" + i, user.getName());
//				assertEquals("test" + i + "@naver.com", user.getEmail());
//				assertEquals("소속"+i, user.getInstitute());
//				assertEquals("부서"+i, user.getDepartment());
//				assertEquals("010-1111-123"+i, user.getPhone());
//				assertEquals(i%2==0 ? "admin" : "member", user.getAuthority());
//				assertEquals("administrator", user.getUpdateUser());
//			}else{
//				int i=1;
//				User user = userList.get(1);
//				assertEquals("testId" + i, user.getUserId());
//				assertEquals(EgovStringUtil.getCryptoSHA256String("testPw" + i), user.getPassword());
//				assertEquals("이름" + i, user.getName());
//				assertEquals("test" + i + "@naver.com", user.getEmail());
//				assertEquals("소속"+i, user.getInstitute());
//				assertEquals("부서"+i, user.getDepartment());
//				assertEquals("010-1111-123"+i, user.getPhone());
//				assertEquals(i%2==0 ? "admin" : "member", user.getAuthority());
//				assertEquals("administrator", user.getUpdateUser());
//			}
//		}
//	}
	
	@Test
	public void test4Update() throws Exception{
		User user = new User();
		int i = 1;
		user.setUserId("testId" + i);
		user.setName("업뎃네임" + i);
		user.setEmail("업댓메일" + i + "@naver.com");
		user.setInstitute("업뎃소속" + i);
		user.setDepartment("업댓부서" + i);
		user.setPhone("업댓연락처" + i);
		user.setAuthority(i%2==0 ? "admin" : "member");
		user.setUpdateUser("update user");
		
		userDAO.updateUser(user);
		
		User updateUser = userDAO.selectUser("testId"+i);
		
		assertEquals("업뎃네임" + i, updateUser.getName());
		assertEquals("업댓메일" + i + "@naver.com", updateUser.getEmail());
		assertEquals("업뎃소속" + i, updateUser.getInstitute());
		assertEquals("업댓부서" + i, updateUser.getDepartment());
		assertEquals("업댓연락처" + i, updateUser.getPhone());
		assertEquals(i%2==0 ? "admin" : "member", updateUser.getAuthority());
		assertEquals("update user", updateUser.getUpdateUser());
	}
	
	@Test 
	public void test5UpdatePw() throws Exception{
		User user = new User();
		int i = 1;
		user.setUserId("testId" + i);
		user.setPassword(EgovStringUtil.getCryptoSHA256String("updatePw" + i));
		userDAO.updateUserPw(user);
		User updateUser = userDAO.selectUser("testId"+i);
		assertEquals(EgovStringUtil.getCryptoSHA256String("updatePw" + i), updateUser.getPassword());
	}
	
	@Test
	public void test6Delete() throws Exception{
		String[] ids = new String[11];
		for(int i = 1; i < 11; i++){
			ids[i] = "testId"+i;
		}
		int deleteCnt = userDAO.deleteUser(ids);
		assertEquals(10, deleteCnt);
	}
}
