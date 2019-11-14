package unitTest.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.insilicogen.gdkm.Globals;
import com.insilicogen.gdkm.model.CodeGroup;
import com.insilicogen.gdkm.model.User;
import com.insilicogen.gdkm.util.JsonUtil;

/**
 * exception test
 * @author sado
 * @since 2018. 7. 4.
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations={
		"file:src/main/webapp/WEB-INF/config/springmvc/dispatcher-servlet.xml"
		,"file:src/main/resources/egovframework/spring/context-*.xml"

})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestCodeGroupController {

	SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat sdfDateTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	
	@Autowired
	private WebApplicationContext wac;
    private MockMvc mockMvc;
    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
    User sessionUser = new User();
    
    String sessionJson = null;
	
	@Before
	public void setUp() throws Exception {
		
		this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
		sessionUser.setUserId("administrator1");
		sessionJson = JsonUtil.getJsonString2(sessionUser);
		
	}

//	@Test
//	public void test1CreateCodeGroup() throws Exception{
//		for(int i = 1; i < 11; i++){
//			CodeGroup codeGroup = new CodeGroup();
//			codeGroup = codeGroupInsert(codeGroup, i);
//			System.err.println(codeGroup.toString());
//
//			String json = JsonUtil.getJsonString2(codeGroup);
//			
//			// 로그인 안한 경우
//			this.mockMvc.perform(post("/codeGroups")
//					.contentType(APPLICATION_JSON_UTF8)
//					.content(json))
//			.andExpect(status().isUnauthorized())
//			.andReturn();
//			
//			// 그룹 코드 중복 검사 오류
//			this.mockMvc.perform(get("/codeGroups/check/id")
//					.contentType(APPLICATION_JSON_UTF8)
//					.param("codeGroupId", "FF")
//					.sessionAttr(Globals.LOGIN_USER, new User()))
//			.andExpect(status().isInternalServerError())
//			.andReturn();
//			
//			// 그룹 코드 중복 검사 성공
//			this.mockMvc.perform(get("/codeGroups/check/id")
//					.contentType(APPLICATION_JSON_UTF8)
//					.param("codeGroupId", codeGroup.getCodeGroup())
//					.sessionAttr(Globals.LOGIN_USER, new User()))
//			.andExpect(status().isOk())
//			.andReturn();
//			
//			// 그룹코드 생성 오류
//			this.mockMvc.perform(post("/codeGroups")
//					.contentType(APPLICATION_JSON_UTF8)
//					.content(json)
//					.sessionAttr(Globals.LOGIN_USER, new User()))
//			.andExpect(status().isInternalServerError())
//			.andReturn();
//			
//			// 그룹코드 생성 성공
//			this.mockMvc.perform(post("/codeGroups")
//					.contentType(APPLICATION_JSON_UTF8)
//					.content(json)
//					.sessionAttr(Globals.LOGIN_USER, new User()))
//			.andExpect(status().isOk())
//			.andReturn();
//		}
//	}

//	@Test
//	public void test2ChangeCodeGroup() throws Exception{
//		for(int i = 1; i < 11; i++){
//			CodeGroup codeGroup = new CodeGroup();
//			codeGroup = codeGroupInsert(codeGroup, i);
//			System.err.println(codeGroup.toString());
//
//			String json = JsonUtil.getJsonString2(codeGroup);
//			
//			// 로그인 안한 경우
//			this.mockMvc.perform(put("/codeGroups/{codeGroup_id}", "test0")
//					.contentType(APPLICATION_JSON_UTF8)
//					.content(json))
//			.andExpect(status().isUnauthorized())
//			.andReturn();
//			
//			// 그룹코드 수정 오류
//			this.mockMvc.perform(put("/codeGroups/{codeGroup_id}", codeGroup.getCodeGroup())
//					.contentType(APPLICATION_JSON_UTF8)
//					.content(json)
//					.sessionAttr(Globals.LOGIN_USER, new User()))
//			.andExpect(status().isInternalServerError())
//			.andReturn();
//			
//			// 그룹코드 수정 성공
//			this.mockMvc.perform(put("/codeGroups/{codeGroup_id}", codeGroup.getCodeGroup())
//					.contentType(APPLICATION_JSON_UTF8)
//					.content(json)
//					.sessionAttr(Globals.LOGIN_USER, new User()))
//			.andExpect(status().isOk())
//			.andReturn();
//		}
//	}
	
//	@Test
//	public void test3GetCodeGroup() throws Exception{
//		for(int i = 1; i < 11; i++){
//			
//			// 로그인 안한 경우
//			this.mockMvc.perform(get("/codeGroups/{codeGroup_id}", "test0")
//					.contentType(APPLICATION_JSON_UTF8))
//			.andExpect(status().isUnauthorized())
//			.andReturn();
//			
//			// 그룹코드 조회 오류
//			this.mockMvc.perform(get("/codeGroups/{codeGroup_id}", "asdasds")
//					.contentType(APPLICATION_JSON_UTF8)
//					.sessionAttr(Globals.LOGIN_USER, new User()))
//			.andExpect(status().isInternalServerError())
//			.andReturn();
//			
//			// 그룹코드 조회 성공
//			this.mockMvc.perform(get("/codeGroups/{codeGroup_id}", codeGroup.getCodeGroup())
//					.contentType(APPLICATION_JSON_UTF8)
//					.sessionAttr(Globals.LOGIN_USER, new User()))
//			.andExpect(status().isOk())
//			.andReturn();
//		}
//	}
	
//	@Test
//	public void test4GetCodeGroupList() throws Exception{
//		for(int i = 1; i < 11; i++){
//			// 그룹코드 조회 오류
//			this.mockMvc.perform(get("/codeGroups")
//					.contentType(APPLICATION_JSON_UTF8)
//					.sessionAttr(Globals.LOGIN_USER, new User()))
//			.andExpect(status().isInternalServerError())
//			.andReturn();
//			
//			// 그룹코드 조회 성공
//			MvcResult mvcResult = this.mockMvc.perform(get("/codeGroups")
//					.contentType(APPLICATION_JSON_UTF8)
//					.sessionAttr(Globals.LOGIN_USER, new User()))
//			.andExpect(status().isOk())
//			.andReturn();
//			
//			String content = mvcResult.getResponse().getContentAsString();
//			System.err.println(content);
//		}
//	}
	
//	@Test
//	public void test5DeleteCodeGroup() throws Exception{
//		for(int i = 1; i < 11; i++){
////			// 로그인 안한 경우
//			this.mockMvc.perform(delete("/codeGroups/{codeGroup_ids}", "testtt"+i)
//					.contentType(APPLICATION_JSON_UTF8))
//			.andExpect(status().isUnauthorized())
//			.andReturn();
//			
//			// 코드 삭제 오류
//			this.mockMvc.perform(delete("/codeGroups/{codeGroup_ids}", "testtt"+i)
//					.contentType(APPLICATION_JSON_UTF8)
//					.sessionAttr(Globals.LOGIN_USER, new User()))
//			.andExpect(status().isInternalServerError())
//			.andReturn();
//			
//			// 코드 삭제 성공
//			this.mockMvc.perform(delete("/codeGroups/{codeGroup_ids}", "09"+i)
//					.contentType(APPLICATION_JSON_UTF8)
//					.sessionAttr(Globals.LOGIN_USER, new User()))
//			.andExpect(status().isOk())
//			.andReturn();
//			
//		}
//	}
	
	private CodeGroup codeGroupInsert(CodeGroup codeGroup, int i) throws ParseException {
		// 코드그룹 생성 성공
		codeGroup.setCodeGroup("testtt"+i);
		codeGroup.setName("testname"+i);
		codeGroup.setRegistDate(sdfDate.parse("2018-02-0"+(i+1)));
		codeGroup.setUseYn("y");
		
		// 코드그룹 생성 오류
//		codeGroup.setCodeGroup("FF");
//		codeGroup.setCodeGroup("group"+i);
//		codeGroup.setName("testname - "+i);
//		codeGroup.setRegistDate(sdfDate.parse("2018-02-0"+(i+1)));
//		codeGroup.setUseYn("v");
		
		// 코드 수정 성공
		
		// 코드 수정 오류
		codeGroup.setCodeGroup("testtt"+i);
		codeGroup.setName("ggg"+i);
		codeGroup.setRegistDate(sdfDate.parse("2018-02-0"+(i+1)));
		codeGroup.setUseYn("v");
		
		return codeGroup;
	}
	
}
