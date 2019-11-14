package unitTest.web;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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

import com.insilicogen.gdkm.Globals;
import com.insilicogen.gdkm.model.NgsDataRegist;
import com.insilicogen.gdkm.model.User;
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
public class TestNgsDataRegistController {

	@Resource(name="messageSource")
	private MessageSource messageSource;
	
	@Autowired
	private WebApplicationContext wac;
    private MockMvc mockMvc;
    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
    User sessionUser = new User();
    String sessionJson = null;
    String ngsJson = null;
    String json = null;
    NgsDataRegist ngsDataRegist = new NgsDataRegist();
	
	@Before
	public void setup() throws Exception{
		this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
		sessionUser.setUserId("administrator1");
		sessionJson = JsonUtil.getJsonString2(sessionUser);
		
		ngsDataRegist.setId(12345);
		ngsDataRegist.setRegistNo("regist12345");
		ngsJson = JsonUtil.getJsonString2(ngsDataRegist);
	}
	
	@Test
	public void test1Create() throws Exception{
		//로그인안한경우
		MvcResult mvcResult = this.mockMvc.perform(post("/regist-data")
				.contentType(APPLICATION_JSON_UTF8)
				.content(ngsJson))
			.andExpect(status().isUnauthorized())
			.andReturn();
		String content = mvcResult.getResponse().getContentAsString();
		assertEquals(content, messageSource.getMessage("ERR001", null, Locale.US));
		
		//NGSDATA error (rawdata, processed각각 테스트 진행해봄)
		/*mvcResult = this.mockMvc.perform(post("/regist-data")
				.contentType(APPLICATION_JSON_UTF8)
				.content(ngsJson)
				.sessionAttr(Globals.LOGIN_USER, new User()))
			.andExpect(status().isBadRequest())
			.andReturn();
		content = mvcResult.getResponse().getContentAsString();
		String dataType = messageSource.getMessage("ENTITY.RAWDATA", null, Locale.US);
		assertEquals(content, messageSource.getMessage("ERR002", new String[]{ dataType }, Locale.US));*/
	}
	
	@Test
	public void test2Update() throws Exception{
		//로그인안한경우
		MvcResult mvcResult = this.mockMvc.perform(put("/regist-data/{data_id}", "1")
				.contentType(APPLICATION_JSON_UTF8)
				.content(ngsJson))
				.andExpect(status().isUnauthorized())
				.andReturn();
		String content = mvcResult.getResponse().getContentAsString();
		assertEquals(content, messageSource.getMessage("ERR001", null, Locale.US));
		
		//NGSDATA error (rawdata, processed각각 테스트 진행해봄)
		/*mvcResult = this.mockMvc.perform(put("/regist-data/{data_id}", "1")
				.contentType(APPLICATION_JSON_UTF8)
				.content(ngsJson)
				.sessionAttr(Globals.LOGIN_USER, new User()))
			.andExpect(status().isNotFound())
			.andReturn();
		content = mvcResult.getResponse().getContentAsString();
		String dataType = messageSource.getMessage("ENTITY.RAWDATA", null, Locale.US);
		assertEquals(content, messageSource.getMessage("ERR006", new String[]{ dataType }, Locale.US));*/
	}
	
	@Test
	public void test3Get() throws Exception{
		//로그인안한경우
		MvcResult mvcResult = this.mockMvc.perform(get("/regist-data/{data_id}", "1"))
				.andExpect(status().isUnauthorized())
				.andReturn();
		String content = mvcResult.getResponse().getContentAsString();
		assertEquals(content, messageSource.getMessage("ERR001", null, Locale.US));
		
		//없는 ID를 요청했을때, null exception
		mvcResult = this.mockMvc.perform(get("/regist-data/{data_id}", "2421")
				.sessionAttr(Globals.LOGIN_USER, new User()))
				.andExpect(status().isNotFound())
				.andReturn();
		content = mvcResult.getResponse().getContentAsString();
		String dataType = messageSource.getMessage("ENTITY.NGSDATA", null, Locale.US);
		assertEquals(content, messageSource.getMessage("ERR006",  new String[]{ dataType }, Locale.US));
	
		//형식에 맞지 않는 요청시 id < 1 이거나,  id==null일때
		String registId = "0";
		mvcResult = this.mockMvc.perform(get("/regist-data/{data_id}", registId)
				.sessionAttr(Globals.LOGIN_USER, new User()))
				.andExpect(status().isBadRequest())
				.andReturn();
		content = mvcResult.getResponse().getContentAsString();
		dataType = messageSource.getMessage("ENTITY.NGSDATA", null, Locale.US);
		assertEquals(content, messageSource.getMessage("ERR004",  new String[]{ dataType }, Locale.US) + registId);
		
		//성공
		registId = "1";
		mvcResult = this.mockMvc.perform(get("/regist-data/{data_id}", registId)
				.sessionAttr(Globals.LOGIN_USER, new User()))
				.andExpect(status().isOk())
				.andReturn();
	}
	
	@Test
	public void test4Delete() throws Exception{
		String registId = "1";
		//로그인안한경우
		MvcResult mvcResult = this.mockMvc.perform(delete("/regist-data/{data_id}", registId))
				.andExpect(status().isUnauthorized())
				.andReturn();
		String content = mvcResult.getResponse().getContentAsString();
		assertEquals(content, messageSource.getMessage("ERR001", null, Locale.US));
		
		//없는 ID를 요청했을때, null exception
		registId = "2421";
		mvcResult = this.mockMvc.perform(delete("/regist-data/{data_id}", registId)
				.sessionAttr(Globals.LOGIN_USER, new User()))
				.andExpect(status().isNotFound())
				.andReturn();
		content = mvcResult.getResponse().getContentAsString();
		String dataType = messageSource.getMessage("ENTITY.NGSDATA", null, Locale.US);
		assertEquals(content, messageSource.getMessage("ERR006",  new String[]{ dataType }, Locale.US));

		//30 submit / 32 success / 34 validating
		String[] registIds = {"30","32","34"};
		for(int i = 0; i < registIds.length; i++){
			mvcResult = this.mockMvc.perform(delete("/regist-data/{data_id}", registIds[i])
					.contentType(APPLICATION_JSON_UTF8)
//					.content(ngsJson))
					.sessionAttr(Globals.LOGIN_USER, new User()))
					.andExpect(status().isBadRequest())
					.andReturn();
			content = mvcResult.getResponse().getContentAsString();
			dataType = messageSource.getMessage("ENTITY.PROCESSED", null, Locale.US);
			assertEquals(content, messageSource.getMessage("ERR008",  new String[]{ dataType }, Locale.US));
		}
		//삭제
		/*registId = "99";
		this.mockMvc.perform(delete("/regist-data/{data_id}", registId)
				.contentType(APPLICATION_JSON_UTF8)
				.content(sessionJson)
				.sessionAttr(Globals.LOGIN_USER, new User()))
				.andExpect(status().isOk());*/
	}
	
	@Test
	public void test5validateNgsDataRegists() throws Exception{
		List<NgsDataRegist> list = new ArrayList<NgsDataRegist>();
		list.add(ngsDataRegist);
		String listJson = JsonUtil.getJsonString2(list);
		
		//로그인안한경우
		MvcResult mvcResult = this.mockMvc.perform(put("/regist-data/validations")
				.contentType(APPLICATION_JSON_UTF8)
				.content(listJson))
				.andExpect(status().isUnauthorized())
				.andReturn();
		String content = mvcResult.getResponse().getContentAsString();
		assertEquals(content, messageSource.getMessage("ERR001", null, Locale.US));
		
		//해당 데이터를 찾지못한 경우
		mvcResult = this.mockMvc.perform(put("/regist-data/validations")
				.contentType(APPLICATION_JSON_UTF8)
				.content(listJson)
				.sessionAttr(Globals.LOGIN_USER, new User()))
				.andExpect(status().isNotFound())
				.andReturn();
		String dataType = messageSource.getMessage("ENTITY.NGSDATA", null, Locale.US);
		content = mvcResult.getResponse().getContentAsString();
		assertEquals(content, messageSource.getMessage("ERR006", new String[] { dataType }, Locale.US));
		
		/*
		list = new ArrayList<NgsDataRegist>();
		ngsDataRegist.setId(20);
		list.add(ngsDataRegist);
		listJson = JsonUtil.getJsonString2(list);
		
		//파이일 존재하지 않는 경우(테스트가 안됨... NgsFileProcessServiceImpl.java의 validateNgsData() - > 디버그가 안잡힘)
		mvcResult = this.mockMvc.perform(put("/regist-data/validations")
				.contentType(APPLICATION_JSON_UTF8)
				.content(listJson)
				.sessionAttr(Globals.LOGIN_USER, new User()))
				.andExpect(status().isNotFound())
				.andReturn();
		dataType = messageSource.getMessage("ENTITY.NGSDATA", null, Locale.US);
		content = mvcResult.getResponse().getContentAsString();
		assertEquals(content, messageSource.getMessage("ERR006", new String[] { dataType }, Locale.US));
		*/
		
		//validating(34), submit(30), failed(36), success(32), //  가능 -> ready(33)
		list = new ArrayList<NgsDataRegist>();
		ngsDataRegist.setId(36);
		list.add(ngsDataRegist);
		listJson = JsonUtil.getJsonString2(list);
		mvcResult = this.mockMvc.perform(put("/regist-data/validations")
				.contentType(APPLICATION_JSON_UTF8)
				.content(listJson)
				.sessionAttr(Globals.LOGIN_USER, new User()))
				.andExpect(status().isBadRequest())
				.andReturn();
		dataType = messageSource.getMessage("ENTITY.NGSDATA", null, Locale.US);
		content = mvcResult.getResponse().getContentAsString();
		assertEquals(content, messageSource.getMessage("ERR008", new String[] { dataType }, Locale.US));
		
		
	}
	
	@Test
	public void test6changeConfirmStatus() throws Exception{
		List<NgsDataRegist> list = new ArrayList<NgsDataRegist>();
		ngsDataRegist.setId(100);
		list.add(ngsDataRegist);
		String listJson = JsonUtil.getJsonString2(list);
		
		//로그인안한경우
		MvcResult mvcResult = this.mockMvc.perform(put("/regist-data/confirm-status")
				.contentType(APPLICATION_JSON_UTF8)
				.content(listJson))
			.andExpect(status().isUnauthorized())
			.andReturn();
		String content = mvcResult.getResponse().getContentAsString();
		assertEquals(content, messageSource.getMessage("ERR001", null, Locale.US));
		
		//NgsData가 존재하지 않는경우
		mvcResult = this.mockMvc.perform(put("/regist-data/confirm-status")
				.contentType(APPLICATION_JSON_UTF8)
				.content(listJson)
				.sessionAttr(Globals.LOGIN_USER, new User()))
				.andExpect(status().isNotFound())
				.andReturn();
		content = mvcResult.getResponse().getContentAsString();
		String dataType = messageSource.getMessage("ENTITY.NGSDATA", null, Locale.US);
		assertEquals(content, messageSource.getMessage("ERR006", new String[] {dataType}, Locale.US));
		
		//파일검증완료된 것만 승인요청 가능
		//validating(34), submit(30), failed(36), success(32), //  가능 -> validated(98)
		list = new ArrayList<NgsDataRegist>();
		int id = 34;
		ngsDataRegist.setId(id);
		list.add(ngsDataRegist);
		listJson = JsonUtil.getJsonString2(list);
		if(id!=98){
			mvcResult = this.mockMvc.perform(put("/regist-data/confirm-status")
					.contentType(APPLICATION_JSON_UTF8)
					.content(listJson)
					.sessionAttr(Globals.LOGIN_USER, new User()))
					.andExpect(status().isBadRequest())
					.andReturn();
			
			content = mvcResult.getResponse().getContentAsString();
			assertEquals(content, messageSource.getMessage("ERR012", null, Locale.US));
		}
	}
	
	

}
