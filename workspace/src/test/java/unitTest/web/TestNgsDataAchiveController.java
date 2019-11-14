package unitTest.web;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.insilicogen.gdkm.Globals;
import com.insilicogen.gdkm.model.Code;
import com.insilicogen.gdkm.model.NgsDataAchive;
import com.insilicogen.gdkm.model.PageableList;
import com.insilicogen.gdkm.model.User;
import com.insilicogen.gdkm.util.JsonUtil;


/**
 * exception test
 * @author sado
 * @since 2018. 7. 5.
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations={
		"file:src/main/webapp/WEB-INF/config/springmvc/dispatcher-servlet.xml"
		,"file:src/main/resources/egovframework/spring/context-*.xml"

})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestNgsDataAchiveController {

	SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat sdfDateTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	
	@Autowired
	private WebApplicationContext wac;
    private MockMvc mockMvc;
    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
    User sessionUser = new User();
    HttpServletRequest request= null;
    String sessionJson = null;
    
	
	@Before
	public void setUp() throws Exception {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
		sessionUser.setUserId("administrator1");
		sessionJson = JsonUtil.getJsonString2(sessionUser);
	}

	//@Test
	public void test1UploadAchiveFiles() throws Exception{

		// multipart X : 400
		this.mockMvc.perform(post("/regist-data/{registId}/achieves", 1)
				.contentType(APPLICATION_JSON_UTF8))
		.andExpect(status().isBadRequest())
		.andReturn();
		
		// registId에 숫자말고 다른거 왔을때 : 400
		this.mockMvc.perform(fileUpload("/regist-data/{registId}/achieves", "asdasd")
				.file(new MockMultipartFile("test_file", "test.t", "text/plain", new byte[]{1,2,3}))
				.contentType("multipart/form-data")
				.sessionAttr(Globals.LOGIN_USER, new User()))
		.andExpect(status().isBadRequest())
		.andReturn();
		
		// 로그인 인증 : 401
		this.mockMvc.perform(fileUpload("/regist-data/{registId}/achieves", 1)
				.contentType("multipart/form-data"))
		.andExpect(status().isUnauthorized())
		.andReturn();
		
		// registId가 없는 id일때
		this.mockMvc.perform(fileUpload("/regist-data/{registId}/achieves", 123)
				.file(new MockMultipartFile("test_file", "test.txt", "text/plain", new byte[]{1,2,3}))
				.contentType("multipart/form-data")
				.sessionAttr(Globals.LOGIN_USER, new User()))
		.andExpect(status().isInternalServerError())
		.andReturn();
		
		// 파일 업로드 성공
		this.mockMvc.perform(fileUpload("/regist-data/{registId}/achieves", 111)
				.file(new MockMultipartFile("test_file", "test.t", "text/plain", new byte[]{1,2,3}))
				.contentType("multipart/form-data")
				.sessionAttr(Globals.LOGIN_USER, new User()))
		.andExpect(status().isOk())
		.andReturn();
		
	}
	
	//@Test
	public void test2GetAchiveFileList() throws Exception{

		// registId에 숫자말고 다른거 왔을때 : 400
		this.mockMvc.perform(get("/regist-data/{registId}/achieves", "asds")
				.contentType(APPLICATION_JSON_UTF8))
		.andExpect(status().isBadRequest())
		.andReturn();
		
		// id가 없을 떄
		this.mockMvc.perform(get("/regist-data/{registId}/achieves", 123)
				.contentType(APPLICATION_JSON_UTF8))
		.andExpect(status().isNotFound())
		.andReturn();
		
		// 조회 성공
		MvcResult mvcResult = this.mockMvc.perform(get("/regist-data/{registId}/achieves", 111)
				.contentType(APPLICATION_JSON_UTF8))
		.andExpect(status().isOk())
		.andReturn();
		
		String content = mvcResult.getResponse().getContentAsString();
		System.out.println(content);
//		String args = messageSource.getMessage("ENTITY.NGSDATA",null,Locale.getDefault());
//		String message = messageSource.getMessage("ERR004", new String[]{args}, Locale.getDefault());
//		assertEquals(content, messageSource.getMessage("ERR004", null, Locale.getDefault()));
		
	}
	
	//@Test
	public void test3GetNgsDataAchive() throws Exception{

		// registId에 숫자말고 다른거 왔을때 : 400
		this.mockMvc.perform(get("/achieves/{achiveId}", "asds")
				.contentType(APPLICATION_JSON_UTF8))
		.andExpect(status().isBadRequest())
		.andReturn();
		
		// registId가 없을 떄 : 400
		this.mockMvc.perform(get("/achieves/{achiveId}", 1234)
				.contentType(APPLICATION_JSON_UTF8))
		.andExpect(status().isNotFound())
		.andReturn();
		
		// 조회 성공
		MvcResult mvcResult = this.mockMvc.perform(get("/achieves/{achiveId}", 111)
				.contentType(APPLICATION_JSON_UTF8))
		.andExpect(status().isOk())
		.andReturn();
		
		// 파일 다운로드 오류
		this.mockMvc.perform(get("/achieves/{achiveId}", 111)
				.contentType("multipart/form-data"))
				.andExpect(status().isNotFound())
				.andReturn();
		
		// 파일 다운로드 성공
		this.mockMvc.perform(get("/achieves/{achiveId}", 118)
				.contentType("application/octet-stream"))
		.andExpect(status().isOk())
		.andReturn();
		
		String content = mvcResult.getResponse().getContentAsString();
		System.out.println(content);
		
	}
	
	//@Test
	public void test4GetAchiveFileList() throws Exception{
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Globals.PARAM_PAGE, "0");
		params.put(Globals.PARAM_ROW_SIZE, "10");
		
		String json = JsonUtil.getJsonString2(params);
		
		// registId에 숫자말고 다른거 왔을때 : 400
		MvcResult mvcResult = this.mockMvc.perform(get("/achieves")
				.contentType(APPLICATION_JSON_UTF8)
//				.param("params", "page", "0")
//				.param("params","rowSize", "10")
				.content(json)
				.sessionAttr(Globals.LOGIN_USER, new User()))
		.andExpect(status().isOk())
		.andReturn();
		
		String content = mvcResult.getResponse().getContentAsString();
		System.err.println(content);
		
	}
	
	//@Test
	public void test5DeleteAchiveFile() throws Exception{
		
		
		// 로그인 인증
		this.mockMvc.perform(delete("/achieves/{ids}", 1)
				.contentType(APPLICATION_JSON_UTF8))
		.andExpect(status().isUnauthorized())
		.andReturn();
		
		// id 이상
		this.mockMvc.perform(delete("/achieves/{ids}", "asdas")
				.contentType(APPLICATION_JSON_UTF8)
				.sessionAttr(Globals.LOGIN_USER, new User()))
		.andExpect(status().isInternalServerError())
		.andReturn();
		
		// id 없음
		this.mockMvc.perform(delete("/achieves/{ids}", 1000)
				.contentType(APPLICATION_JSON_UTF8)
				.sessionAttr(Globals.LOGIN_USER, new User()))
		.andExpect(status().isInternalServerError())
		.andReturn();
		
		// 삭제 성공
		this.mockMvc.perform(delete("/achieves/{ids}", 116)
				.contentType(APPLICATION_JSON_UTF8)
				.sessionAttr(Globals.LOGIN_USER, new User()))
		.andExpect(status().isOk())
		.andReturn();
	}
	
	//@Test
	public void test6GetFileSeqQuantity() throws Exception{
		
		// id 이상
		this.mockMvc.perform(get("/achieves/{achiveId}/quantity", "asd")
				.contentType(APPLICATION_JSON_UTF8))
		.andExpect(status().isBadRequest())
		.andReturn();
		
		// id 없음
		this.mockMvc.perform(get("/achieves/{achiveId}/quantity", 1000)
				.contentType(APPLICATION_JSON_UTF8))
		.andExpect(status().isInternalServerError())
		.andReturn();
		
		// 검증 후 seq-quentity file 생성됨
		this.mockMvc.perform(get("/achieves/{achiveId}/quantity", 8)
				.contentType(APPLICATION_JSON_UTF8))
		.andExpect(status().isOk())
		.andReturn();
		
	}
	
	//@Test
	public void test7GetNgsFileQcResultList() throws Exception{
		
		// id 이상
		this.mockMvc.perform(get("/achieves/{achiveId}/qcResults", "asd")
				.contentType(APPLICATION_JSON_UTF8))
		.andExpect(status().isBadRequest())
		.andReturn();
		
		// id 없음
		this.mockMvc.perform(get("/achieves/{achiveId}/qcResults", 1000)
				.contentType(APPLICATION_JSON_UTF8))
		.andExpect(status().isNotFound())
		.andReturn();
		
		// 호출 성공
		MvcResult mvcResult = this.mockMvc.perform(get("/achieves/{achiveId}/qcResults", 8)
				.contentType(APPLICATION_JSON_UTF8))
		.andExpect(status().isOk())
		.andReturn();
		
		String content = mvcResult.getResponse().getContentAsString();
		System.err.println(content);
		
	}
	
	//@Test
	public void test8GetNgsFileQcSummartList() throws Exception{
		
		// id 이상
		this.mockMvc.perform(get("/achieves/{achiveId}/qcSummary", "asd")
				.contentType(APPLICATION_JSON_UTF8))
		.andExpect(status().isBadRequest())
		.andReturn();
		
		// id 없음
		this.mockMvc.perform(get("/achieves/{achiveId}/qcSummary", 1000)
				.contentType(APPLICATION_JSON_UTF8))
		.andExpect(status().isNotFound())
		.andReturn();
		
		// 호출 성공
		MvcResult mvcResult = this.mockMvc.perform(get("/achieves/{achiveId}/qcSummary", 8)
				.contentType(APPLICATION_JSON_UTF8))
		.andExpect(status().isOk())
		.andReturn();
		
		String content = mvcResult.getResponse().getContentAsString();
		System.err.println(content);
		
	}
	
	@Test
	public void test9GetNgsFileQcReport() throws Exception{
		
		// id 이상
		this.mockMvc.perform(get("/achieves/{achiveId}/qcReport", "asd")
				.contentType(APPLICATION_JSON_UTF8))
		.andExpect(status().isBadRequest())
		.andReturn();
		
		// id 없음
		this.mockMvc.perform(get("/achieves/{achiveId}/qcReport", 1000)
				.contentType(APPLICATION_JSON_UTF8))
		.andExpect(status().isNotFound())
		.andReturn();
		
		// 호출 성공
		this.mockMvc.perform(get("/achieves/{achiveId}/qcReport", 8)
				.contentType(APPLICATION_JSON_UTF8))
		.andExpect(status().isOk())
		.andReturn();
		
	}
	
	
}
