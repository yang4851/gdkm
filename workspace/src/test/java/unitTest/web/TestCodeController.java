package unitTest.web;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import com.insilicogen.gdkm.Globals;
import com.insilicogen.gdkm.model.Code;
import com.insilicogen.gdkm.model.User;
import com.insilicogen.gdkm.util.JsonUtil;
import com.mysql.fabric.xmlrpc.base.Params;

/**
 * exception test
 * @author sado
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
public class TestCodeController {

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

	@Test
	public void test1CreateCode() throws Exception{
		for(int i = 1; i < 11; i++){
			Code code = new Code();
			code = codeInsert(code, i);

			String json = JsonUtil.getJsonString2(code);
			
			// 로그인 안한 경우
			this.mockMvc.perform(post("/codes")
					.contentType(APPLICATION_JSON_UTF8)
					.content(json))
			.andExpect(status().isUnauthorized())
			.andReturn();
			
			// 코드 중복 검사 에러
			this.mockMvc.perform(get("/codes/check/id?codeId="+"FF")
					.contentType(APPLICATION_JSON_UTF8))
			.andExpect(status().isInternalServerError())
			.andReturn();
			
			// 코드 중복 검사 성공
			this.mockMvc.perform(get("/codes/check/id?codeId="+code.getCode())
					.contentType(APPLICATION_JSON_UTF8))
			.andExpect(status().isOk())
			.andReturn();
			
			// 코드 생성 에러
			this.mockMvc.perform(post("/codes")
					.contentType(APPLICATION_JSON_UTF8)
					.content(json)
					.sessionAttr(Globals.LOGIN_USER, new User()))
			.andExpect(status().isInternalServerError())
			.andReturn();
			// 코드 생성 성공
//			this.mockMvc.perform(post("/codes")
//					.contentType(APPLICATION_JSON_UTF8)
//					.content(json)
//					.sessionAttr(Globals.LOGIN_USER, new User()))
//			.andExpect(status().isOk())
//			.andReturn();
		}
	}
	
	//@Test
	public void test2ChangeCode() throws Exception{
		for(int i = 1; i < 11; i++){
			Code code = new Code();
			code = codeInsert(code, i);
			System.err.println(code.toString());
			
			String json = JsonUtil.getJsonString2(code);
			// 로그인 안한 경우
			this.mockMvc.perform(put("/codes/{code_id}","09"+i)
					.contentType(APPLICATION_JSON_UTF8)
					.content(json))
			.andExpect(status().isUnauthorized())
			.andReturn();
			
			// 코드 수정  에러
			this.mockMvc.perform(put("/codes/{code_id}","asdasd")
					.contentType(APPLICATION_JSON_UTF8)
					.content(json)
					.sessionAttr(Globals.LOGIN_USER, new User()))
			.andExpect(status().isInternalServerError())
			.andReturn();
			
			// 코드 수정  성공
			this.mockMvc.perform(put("/codes/{code_id}","09"+i)
					.contentType(APPLICATION_JSON_UTF8)
					.content(json)
					.sessionAttr(Globals.LOGIN_USER, new User()))
			.andExpect(status().isOk())
			.andReturn();
			
		}
	}
	
	//@Test
	public void test3GetCode() throws Exception{
		for(int i = 1; i < 11; i++){
			// 로그인 안한 경우
			this.mockMvc.perform(get("/codes/{code_id}","09"+i)
					.contentType(APPLICATION_JSON_UTF8))
			.andExpect(status().isUnauthorized())
			.andReturn();
			
			// 코드 조회 에러
			this.mockMvc.perform(get("/codes/{code_id}","09dads"+i)
					.contentType(APPLICATION_JSON_UTF8)
					.sessionAttr(Globals.LOGIN_USER, new User()))
			.andExpect(status().isInternalServerError())
			.andReturn();
			
			// 코드 조회 성공
			this.mockMvc.perform(get("/codes/{code_id}","09"+i)
					.contentType(APPLICATION_JSON_UTF8)
					.sessionAttr(Globals.LOGIN_USER, new User()))
			.andExpect(status().isOk())
			.andReturn();
		}
	}
	
	//@Test
	public void test4GetCodeList() throws Exception{
		for(int i = 1; i < 11; i++){
			Map<String, Object> params = new HashMap<String, Object>();
			params.put(Globals.PARAM_PAGE, "0");
			params.put(Globals.PARAM_ROW_SIZE,"10");
			params.put(Globals.PARAM_IS_BASICFORM, true);
			
			// 코드 목록 조회.
			String json = JsonUtil.getJsonString2(params);
			MvcResult mvcResult = this.mockMvc.perform(get("/codes")
					.contentType(APPLICATION_JSON_UTF8)
					.sessionAttr(Globals.LOGIN_USER, new User()))
			.andExpect(status().isOk())
			.andReturn();
			
			String content = mvcResult.getResponse().getContentAsString();
			System.err.println(content);
		}
	}
	
	//@Test
	public void test5DeleteCode() throws Exception{
		for(int i = 1; i < 11; i++){
			// 로그인 안한 경우
			this.mockMvc.perform(delete("/codes/{code_ids}", "09"+i)
					.contentType(APPLICATION_JSON_UTF8))
			.andExpect(status().isUnauthorized())
			.andReturn();
			
			// 코드 삭제 오류
			this.mockMvc.perform(delete("/codes/{code_ids}", "asdadad")
					.contentType(APPLICATION_JSON_UTF8)
					.sessionAttr(Globals.LOGIN_USER, new User()))
			.andExpect(status().isInternalServerError())
			.andReturn();
			
			// 코드 삭제 성공
			this.mockMvc.perform(delete("/codes/{code_ids}", "09"+i)
					.contentType(APPLICATION_JSON_UTF8)
					.sessionAttr(Globals.LOGIN_USER, new User()))
			.andExpect(status().isOk())
			.andReturn();
			
		}
	}
	
	private Code codeInsert(Code code, int i) throws ParseException {
		// 코드생성
		code.setCode("09"+i);
		code.setGroup("asad");
		code.setName("dsada");
		code.setRegistDate(sdfDate.parse("2018-02-0"+(i+1)));
		code.setUseYn("y");
		
		// 코드 수정
//		code.setCode("sdsd"+i);
//		code.setGroup("test0");
//		code.setName("수정한다.");
//		code.setRegistDate(sdfDate.parse("2018-02-0"+(i+1)));
//		code.setUseYn("asddd");
		
		return code;
	}
}
