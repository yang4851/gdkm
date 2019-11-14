package unitTest.web;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.insilicogen.gdkm.model.NgsDataAchive;
import com.insilicogen.gdkm.model.User;
import com.insilicogen.gdkm.util.JsonUtil;

/**
 * exception test
 * @author sado
 * @since 2018. 7. 6.
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations={
		"file:src/main/webapp/WEB-INF/config/springmvc/dispatcher-servlet.xml"
		,"file:src/main/resources/egovframework/spring/context-*.xml"

})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestNgsDataFeaturesController {
	
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
		sessionJson = JsonUtil.getJsonString2(sessionUser);getClass();
	}
	
	//@Test
	public void test1GetFeature() throws Exception{

		// id 이상
		this.mockMvc.perform(get("/achieves/{achiveId}/features", "asda")
				.contentType(APPLICATION_JSON_UTF8))
		.andExpect(status().isBadRequest())
		.andReturn();
		
		// id 없음
		this.mockMvc.perform(get("/achieves/{achiveId}/features", 1000)
				.contentType(APPLICATION_JSON_UTF8))
		.andExpect(status().isNotFound())
		.andReturn();
		
		// 호출 성공
		MvcResult mvcResult = this.mockMvc.perform(get("/achieves/{achiveId}/features", 9)
				.contentType(APPLICATION_JSON_UTF8))
		.andExpect(status().isOk())
		.andReturn();
		
		String content = mvcResult.getResponse().getContentAsString();
		System.err.println(content);	
		
	}
	
	//@Test
	public void test2GetFeatureHeader() throws Exception{

		// id 이상
		this.mockMvc.perform(get("/achieves/{achiveId}/feature-header", "asda")
				.contentType(APPLICATION_JSON_UTF8))
		.andExpect(status().isBadRequest())
		.andReturn();
		
		// id 없음
		this.mockMvc.perform(get("/achieves/{achiveId}/feature-header", 1000)
				.contentType(APPLICATION_JSON_UTF8))
		.andExpect(status().isNotFound())
		.andReturn();
		
		// 호출 성공
		MvcResult mvcResult = this.mockMvc.perform(get("/achieves/{achiveId}/feature-header", 9)
				.contentType(APPLICATION_JSON_UTF8))
		.andExpect(status().isOk())
		.andReturn();
		
		String content = mvcResult.getResponse().getContentAsString();
		System.err.println(content);		
	}
	
	//@Test
	public void test3GetGenbankFileAsText() throws Exception{

		// id 이상
		this.mockMvc.perform(get("/achieves/{achiveId}/gbk", "asda")
				.contentType(APPLICATION_JSON_UTF8))
		.andExpect(status().isBadRequest())
		.andReturn();
		
		// id 없음
		this.mockMvc.perform(get("/achieves/{achiveId}/gbk", 1000)
				.contentType(APPLICATION_JSON_UTF8))
		.andExpect(status().isNotFound())
		.andReturn();
		
		// 호출 성공
		MvcResult mvcResult = this.mockMvc.perform(get("/achieves/{achiveId}/gbk", 9)
				.contentType(APPLICATION_JSON_UTF8))
		.andExpect(status().isOk())
		.andReturn();
		
		String content = mvcResult.getResponse().getContentAsString();
		System.err.println(content);		
	}
	
	//@Test
	public void test4GetGffFileAsText() throws Exception{
		
		// id 이상
		this.mockMvc.perform(get("/achieves/{achiveId}/gview.gff", "asda")
				.contentType(APPLICATION_JSON_UTF8))
		.andExpect(status().isBadRequest())
		.andReturn();
		
		// id 없음
		this.mockMvc.perform(get("/achieves/{achiveId}/gview.gff", 1000)
				.contentType(APPLICATION_JSON_UTF8))
		.andExpect(status().isNotFound())
		.andReturn();
		
		// 호출 성공
		MvcResult mvcResult = this.mockMvc.perform(get("/achieves/{achiveId}/gview.gff", 9)
				.contentType(APPLICATION_JSON_UTF8))
				.andExpect(status().isOk())
				.andReturn();
		
		String content = mvcResult.getResponse().getContentAsString();
		System.err.println(content);		
	}
	
	//@Test
	public void test5CreateJbrowse() throws Exception{
		
		List<NgsDataAchive> achiveList = new ArrayList<NgsDataAchive>();
		NgsDataAchive achive = new NgsDataAchive();
		achive.setId(1000);
		
		achiveList.add(achive);
		String json = JsonUtil.getJsonString2(achiveList);
		// id 없음
		this.mockMvc.perform(post("/achieves/Jbrowse")
				.contentType(APPLICATION_JSON_UTF8)
				.content(json))
		.andExpect(status().isNotFound())
		.andReturn();
		
		achive.setId(9);
		json = JsonUtil.getJsonString2(achiveList);
		// 호출 성공
		MvcResult mvcResult = this.mockMvc.perform(post("/achieves/Jbrowse")
				.contentType(APPLICATION_JSON_UTF8)
				.content(json))
				.andExpect(status().isOk())
				.andReturn();
		
		String content = mvcResult.getResponse().getContentAsString();
		System.err.println(content);		
	}
}
