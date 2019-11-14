package unitTest.web;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.annotation.Resource;

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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.insilicogen.gdkm.Globals;
import com.insilicogen.gdkm.model.Attachment;
import com.insilicogen.gdkm.model.User;
import com.insilicogen.gdkm.service.UserService;
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
public class TestAttachmentController {

	SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat sdfDateTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	
	@Autowired
	private WebApplicationContext wac;
    private MockMvc mockMvc;
    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
    User sessionUser = new User();
    
    String sessionJson = null;
	
    @Resource
    UserService userService;
    
	@Before
	public void setUp() throws Exception {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
		sessionUser.setUserId("administrator1");
		sessionJson = JsonUtil.getJsonString2(sessionUser);
	}

	@Test
	public void test1CreateAttachment() throws Exception{
		for(int i = 1; i < 11; i++){
			Attachment attachment = new Attachment();
			attachment = attachmentInsert(attachment, i);

			String json = JsonUtil.getJsonString2(attachment);
			// 로그인 안한 경우
			this.mockMvc.perform(post("/attachments")
					.contentType(APPLICATION_JSON_UTF8)
					.content(json))
			.andExpect(status().isUnauthorized())
			.andReturn();
			
			// 첨부파일 생성 오류
			this.mockMvc.perform(post("/attachments")
					.contentType(APPLICATION_JSON_UTF8)
					.content(json)
					.sessionAttr(Globals.LOGIN_USER, new User()))
			.andExpect(status().isInternalServerError())
			.andReturn();
			
			// 첨부파일 생성
//			this.mockMvc.perform(post("/attachments")
//					.contentType(APPLICATION_JSON_UTF8)
//					.content(json)
//					.sessionAttr(Globals.LOGIN_USER, new User()))
//			.andExpect(status().isOk())
//			.andReturn();
		}
	}

	@Test
	public void test2DeleteAttachment() throws Exception{
		for(int i = 0; i < 10; i++){
			Attachment attachment = new Attachment();
			attachment = attachmentInsert(attachment, i);

			String json = JsonUtil.getJsonString2(attachment);
			// 로그인 안한 경우
			this.mockMvc.perform(delete("/attachments/{attachment_ids}", i)
					.contentType(APPLICATION_JSON_UTF8))
			.andExpect(status().isUnauthorized())
			.andReturn();
			
			// 첨부파일 삭제 에러
			this.mockMvc.perform(delete("/attachments/{attachment_ids}", i+30)
					.contentType(APPLICATION_JSON_UTF8)
					.sessionAttr(Globals.LOGIN_USER, new User()))
			.andExpect(status().isInternalServerError())
			.andReturn();
			
			// 첨부파일 삭제 성공
//			this.mockMvc.perform(delete("/attachments/{attachment_ids}", i)
//					.contentType(APPLICATION_JSON_UTF8)
//					.sessionAttr(Globals.LOGIN_USER, new User()))
//			.andExpect(status().isOk())
//			.andReturn();
		}
	}
	
	public Attachment attachmentInsert(Attachment attachment, int i) throws Exception{
		// 첨부파일 생성
		User user = new User();
		user = userService.getUser("administrator1");
//		attachment.setName("dd");
//		attachment.setRegistDate(sdfDate.parse("2018-02-0"+(i+1)));
//		attachment.setSize(200);
//		attachment.setType("ser - "+i);
//		attachment.setRegistUser(user);
		
		// 생성 오류
		attachment.setName("dd");
		attachment.setRegistDate(sdfDate.parse("2018-02-0"+(i+1)));
		attachment.setSize(200L);
		attachment.setType("ser - "+i);
		return attachment;
	}
	
	
}
