package unitTest.web;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.insilicogen.gdkm.Globals;
import com.insilicogen.gdkm.model.User;
import com.insilicogen.gdkm.util.EgovStringUtil;
import com.insilicogen.gdkm.util.JsonUtil;


/**
 * @author iksu
 * @since 2018. 7. 3.
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations={
		"file:src/main/webapp/WEB-INF/config/springmvc/dispatcher-servlet.xml"
		,"file:src/main/resources/egovframework/spring/context-*.xml"

})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestUserController {

	@Resource(name="messageSource")
	private MessageSource messageSource;
	
	SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat sdfDateTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	
	@Autowired
	private WebApplicationContext wac;
    private MockMvc mockMvc;
    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
    User sessionUser = new User();
    String sessionJson = null;
    String json = null;
	
	@Before
	public void setup() throws Exception{
		this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
//		this.mockMvc = MockMvcBuilders.standaloneSetup(new UserController()).build();
		sessionUser.setUserId("administrator1");
		sessionJson = JsonUtil.getJsonString2(sessionUser);
	}
	
	@Test
	public void test1Create() throws Exception{
		//로그인안한경우
		MvcResult mvcResult = this.mockMvc.perform(post("/users")
				.contentType(APPLICATION_JSON_UTF8)
				.content(sessionJson))
			.andExpect(status().isUnauthorized())
			.andReturn();
		String content = mvcResult.getResponse().getContentAsString();
		assertEquals(content, messageSource.getMessage("ERR001", null, Locale.US));
		
		//등록실패 한경우
		sessionUser.setUserId("exceptionTestId#%$^TGghwehawheawrhawawegawegawehawrh457547457");
		mvcResult = this.mockMvc.perform(post("/users")
				.contentType(APPLICATION_JSON_UTF8)
				.content(sessionJson)
				.sessionAttr(Globals.LOGIN_USER, new User()))
			.andExpect(status().isInternalServerError())
			.andReturn();
//		content = mvcResult.getResponse().getContentAsString();
//		content = EgovStringUtil.trimSentenceFirstPart(content, " - ");
//		String dataType = messageSource.getMessage("ENTITY.USER", null, Locale.getDefault());
//		assertEquals(content, messageSource.getMessage("ERR002", new String[]{ dataType }, Locale.getDefault()));
	}
	
	@Test
	public void test2GetUserIdCheck() throws Exception{
		//로그인안한경우
		MvcResult mvcResult = this.mockMvc.perform(get("/users/check/id?userId=administrator"))
			.andExpect(status().isUnauthorized())
			.andReturn();
		String content = mvcResult.getResponse().getContentAsString();
		assertEquals(content, messageSource.getMessage("ERR001", null, Locale.US));
			
		//중복검사 결과 중복되는 경우
		mvcResult = this.mockMvc.perform(get("/users/check/id?userId=administrator")
				.sessionAttr(Globals.LOGIN_USER, new User()))
			.andExpect(status().isBadRequest())
			.andReturn();
		content = mvcResult.getResponse().getContentAsString();
		assertEquals(content, messageSource.getMessage("ERR016", null, Locale.getDefault()));
		

		//중복검사 결과 중복되지 않는 경우
		mvcResult = this.mockMvc.perform(get("/users/check/id?userId=administrator4574571")
				.sessionAttr(Globals.LOGIN_USER, new User()))
			.andExpect(status().isOk())
			.andReturn();
		content = mvcResult.getResponse().getContentAsString();
		assertEquals(content, "사용가능- ");
	}
	
	@Test
	public void test3ChangeUser() throws Exception{
		//로그인안한경우
		MvcResult mvcResult = this.mockMvc.perform(put("/users/{user_id}", "administrator")
				.contentType(APPLICATION_JSON_UTF8)
				.content(sessionJson))
				.andExpect(status().isUnauthorized())
				.andReturn();
		String content = mvcResult.getResponse().getContentAsString();
		assertEquals(content, messageSource.getMessage("ERR001", null, Locale.US));
		
		//수정실패한 경우
		mvcResult = this.mockMvc.perform(put("/users/{user_id}", "administrator")
				.contentType(APPLICATION_JSON_UTF8)
				.content(sessionJson)
				.sessionAttr(Globals.LOGIN_USER, new User()))
			.andExpect(status().isInternalServerError())
			.andReturn();
//		content = mvcResult.getResponse().getContentAsString();
//		content = EgovStringUtil.trimSentenceFirstPart(content, " - ");
//		String dataType = messageSource.getMessage("ENTITY.USER", null, Locale.getDefault());
//		assertEquals(content, messageSource.getMessage("ERR003", new String[]{ dataType }, Locale.getDefault()));
	}
	
	@Test
	public void test4ChangeUserPw() throws Exception{
		//로그인안한경우
		MvcResult mvcResult = this.mockMvc.perform(put("/users/{user_id}/password", "administrator")
				.contentType(APPLICATION_JSON_UTF8)
				.content(sessionJson))
				.andExpect(status().isUnauthorized())
				.andReturn();
		String content = mvcResult.getResponse().getContentAsString();
		assertEquals(content, messageSource.getMessage("ERR001", null, Locale.US));
		
		//수정실패한 경우
		mvcResult = this.mockMvc.perform(put("/users/{user_id}/password", "administrator")
				.contentType(APPLICATION_JSON_UTF8)
				.content(sessionJson)
				.sessionAttr(Globals.LOGIN_USER, new User()))
				.andExpect(status().isInternalServerError())
				.andReturn();
//		content = mvcResult.getResponse().getContentAsString();
//		content = EgovStringUtil.trimSentenceFirstPart(content, " - ");
//		String dataType = messageSource.getMessage("ENTITY.USER.PASSWORD", null, Locale.getDefault());
//		assertEquals(content, messageSource.getMessage("ERR003", new String[]{ dataType }, Locale.getDefault()));
	}
	
	@Test
	public void test5Login() throws Exception{
		User loginUser = new User();
		loginUser.setUserId("invalidId");
		loginUser.setPassword("rhksflwk!1");
		loginUser.setName("관리자");
		loginUser.setAuthority("admin");
		json = JsonUtil.getJsonString(loginUser);
		//로그인 아이디가 없을 경우
		MvcResult mvcResult = this.mockMvc.perform(post("/login")
				.contentType(APPLICATION_JSON_UTF8)
				.content(json))
				.andExpect(status().isInternalServerError())
				.andReturn();
		String content = mvcResult.getResponse().getContentAsString();
		String dataType = messageSource.getMessage("ENTITY.USER", null, Locale.getDefault());
		assertEquals(content, messageSource.getMessage("ERR014", new String[]{ dataType }, Locale.getDefault()));
		
		loginUser.setUserId("administrator");
		loginUser.setPassword("rhksflwk!11");
		json = JsonUtil.getJsonString(loginUser);
				
		//비밀번호가 정확하지 않을 경우
		mvcResult = this.mockMvc.perform(post("/login")
				.contentType(APPLICATION_JSON_UTF8)
				.content(json))
				.andExpect(status().isInternalServerError())
				.andReturn();
		content = mvcResult.getResponse().getContentAsString();
		dataType = messageSource.getMessage("ENTITY.USER", null, Locale.getDefault());
		assertEquals(content, messageSource.getMessage("ERR015", new String[]{ dataType }, Locale.getDefault()));
	}
	
	@Test
	public void test6GetUser() throws Exception{
		//로그인안한경우
		MvcResult mvcResult = this.mockMvc.perform(get("/users/{user_id}", "administrator"))
				.andExpect(status().isUnauthorized())
				.andReturn();
		String content = mvcResult.getResponse().getContentAsString();
		assertEquals(content, messageSource.getMessage("ERR001", null, Locale.US));
				
		//사용자 조회
		mvcResult = this.mockMvc.perform(get("/users/{user_id}", "administrator")
				.sessionAttr(Globals.LOGIN_USER, new User()))
				.andExpect(status().isOk())
				.andReturn();
		
		//사용자가 존재하지 않는경우
		mvcResult = this.mockMvc.perform(get("/users/{user_id}", "administrator1231231234")
				.sessionAttr(Globals.LOGIN_USER, new User()))
				.andExpect(status().isNotFound())
				.andReturn();
		content = mvcResult.getResponse().getContentAsString();
		String dataType = messageSource.getMessage("ENTITY.USER", null, Locale.getDefault());
		assertEquals(content, messageSource.getMessage("ERR006", new String[]{ dataType }, Locale.getDefault()));
		
		//사용자 조회 오류
		/*mvcResult = this.mockMvc.perform(get("/users/{user_id}", "administrator")
				.sessionAttr(Globals.LOGIN_USER, new User()))
				.andExpect(status().isInternalServerError())
				.andReturn();
		content = mvcResult.getResponse().getContentAsString();
		content = EgovStringUtil.trimSentenceFirstPart(content, " - ");
		dataType = messageSource.getMessage("ENTITY.USER", null, Locale.getDefault());
		assertEquals(content, messageSource.getMessage("ERR004", new String[]{ dataType }, Locale.getDefault()));*/
	}
	
	@Test
	public void test6GetUserList() throws Exception{
		Map map = new HashMap();
		map.put("firstIndex", 0);
		map.put("rowSize", 10);
		
		//로그인안한경우
		MvcResult mvcResult = this.mockMvc.perform(get("/users")
				.contentType(APPLICATION_JSON_UTF8)
				.content(sessionJson))
				.andExpect(status().isUnauthorized())
				.andReturn();
		String content = mvcResult.getResponse().getContentAsString();
		assertEquals(content, messageSource.getMessage("ERR001", null, Locale.US));
		
		//사용자 목록 조회
		mvcResult = this.mockMvc.perform(get("/users")
				.contentType(APPLICATION_JSON_UTF8)
				.content(sessionJson)
				.sessionAttr(Globals.LOGIN_USER, new User()))
				.andExpect(status().isOk())
				.andReturn();
		
		//사용자 목록 조회 오류
		/*mvcResult = this.mockMvc.perform(get("/users")
				.contentType(APPLICATION_JSON_UTF8)
				.content(sessionJson)
				.sessionAttr(Globals.LOGIN_USER, new User()))
				.andExpect(status().isInternalServerError())
				.andReturn();
		content = mvcResult.getResponse().getContentAsString();
		content = EgovStringUtil.trimSentenceFirstPart(content, " - ");
		String dataType = messageSource.getMessage("ENTITY.USER", null, Locale.getDefault());
		assertEquals(content, messageSource.getMessage("ERR004", new String[]{ dataType }, Locale.getDefault()));*/
		
	}

}
