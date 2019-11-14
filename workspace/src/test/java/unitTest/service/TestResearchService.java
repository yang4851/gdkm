package unitTest.service;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.insilicogen.gdkm.Globals;
import com.insilicogen.gdkm.model.NgsDataRegist;
import com.insilicogen.gdkm.model.Research;
import com.insilicogen.gdkm.model.User;
import com.insilicogen.gdkm.service.ResearchService;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations={"classpath:/egovframework/spring/context-*.xml"})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestResearchService {

	@Resource(name="ResearchService")
	private ResearchService service;
	
	private String[] status;
	
	private User owner;
	
	@Before
	public void setup() {
		owner = new User();
		owner.setUserId("administrator");
		
		status = new String[5];
		status[0] = Globals.STATUS_REGIST_READY;
		status[1] = Globals.STATUS_REGIST_VALIDATING;
		status[2] = Globals.STATUS_REGIST_VALIDATED;
		status[3] = Globals.STATUS_REGIST_ERROR;
		status[4] = Globals.STATUS_REGIST_SUCCESS;
	}
	
	@Test
	public void test1CreateResearch() throws Exception {
		for(int i=0; i < 10; i++) {
			Research research = new Research();
			research.setTitle("title" + i);
			research.setSubmitter("submitter" + i);
			research.setSubmitOrgan("submitOrgan" + i);
			research.setFullTextLink("fullTextLink" + i);
			research.setContent("content" + i);
			research.setOpenYn("N");
			research.setRegistUser(owner);
			
			assertNotNull(service.createResearch(research));
			assertEquals(i+1, research.getId());
		}
	}
	
	@Test
	public void test2GetResearch() throws Exception {
		for(int i=0; i < 10; i++) {
			assertNotNull(service.getResearch(i+1));
			assertNotNull(service.getResearch(i+1).getRegistNo());
			assertEquals("title"+i, service.getResearch(i+1).getTitle());
			assertEquals("submitter"+i, service.getResearch(i+1).getSubmitter());
			assertEquals("submitOrgan"+i, service.getResearch(i+1).getSubmitOrgan());
			assertEquals("fullTextLink"+i, service.getResearch(i+1).getFullTextLink());
			assertEquals("content"+i, service.getResearch(i+1).getContent());
			assertEquals("N", service.getResearch(i+1).getOpenYn());
			assertNull(service.getResearch(i+1).getOpenDate());
			assertEquals(owner, service.getResearch(i+1).getRegistUser());
			assertNotNull(service.getResearch(i+1).getRegistDate());
			assertEquals(owner, service.getResearch(i+1).getUpdateUser());
			assertNotNull(service.getResearch(i+1).getUpdateDate());
		}
	}
	
	@Test
	public void test3ChangeResearch() throws Exception {
		for(int i=0; i < 10; i++) {
			Research research = new Research();
			research.setId(i+1);
			research.setTitle("title-modified" + i);
			research.setSubmitter("submitter-modified" + i);
			research.setSubmitOrgan("submitOrgan-modified" + i);
			research.setFullTextLink("fullTextLink-modified" + i);
			research.setContent("content-modified" + i);
			research.setOpenYn("Y");
			research.setOpenDate(new Date());
			research.setRegistStatus(status[i%status.length]);
			research.setUpdateUser(owner);
			assertNotNull(service.changeResearch(research));
			
			assertEquals("title-modified"+i, service.getResearch(i+1).getTitle());
			assertEquals("submitter-modified"+i, service.getResearch(i+1).getSubmitter());
			assertEquals("submitOrgan-modified"+i, service.getResearch(i+1).getSubmitOrgan());
			assertEquals("fullTextLink-modified"+i, service.getResearch(i+1).getFullTextLink());
			assertEquals("content-modified"+i, service.getResearch(i+1).getContent());
			assertEquals("Y", service.getResearch(i+1).getOpenYn());
			assertNotNull(service.getResearch(i+1).getOpenDate());
			assertEquals(status[i%status.length], service.getResearch(i+1).getRegistStatus());
			assertEquals(owner, service.getResearch(i+1).getRegistUser());
			assertNotNull(service.getResearch(i+1).getRegistDate());
			assertEquals(owner, service.getResearch(i+1).getUpdateUser());
			assertNotNull(service.getResearch(i+1).getUpdateDate());
		}
	}
	
	@Test
	public void test4GetResearchList() throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		assertEquals(10, service.getResearchList(params).size());
		
		params.put("keyword", "title-modified");
		assertEquals(10, service.getResearchList(params).size());
		
		params.put("keyword", "title-modified1");
		assertEquals(1, service.getResearchList(params).size());
		
		params.put("fields", new String[] { "registNo" });
		assertEquals(0, service.getResearchList(params).size());
		
		params.put("fields", new String[] { "registNo", "title" });
		assertEquals(1, service.getResearchList(params).size());
		
		params = new HashMap<String, Object>();
		params.put("openYn", "N");
		assertEquals(0, service.getResearchList(params).size());
		
		params.put("openYn", "Y");
		assertEquals(10, service.getResearchList(params).size());
		
		params.put("openFrom", "2019-08-27");
		params.put("openTo", "2019-08-27");
		assertEquals(10, service.getResearchList(params).size());
		
		params.put("firstIndex", 0);
		params.put("rowSize", 5);
		assertEquals(5, service.getResearchList(params).size());
		
		params.put("registStatus", status[0]);
		assertEquals(2, service.getResearchList(params).size());
		
		params.put("registUser",  owner);
		assertEquals(2, service.getResearchList(params).size());
		
		assertEquals(0, service.getResearchList(new NgsDataRegist(30)).size());
	}
	
	@Test
	public void test5GetResearchCount() throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		assertEquals(10, service.getResearchCount(params));
		
		params.put("keyword", "title-modified");
		assertEquals(10, service.getResearchCount(params));
		
		params.put("keyword", "title-modified1");
		assertEquals(1, service.getResearchCount(params));
		
		params.put("fields", new String[] { "registNo" });
		assertEquals(0, service.getResearchCount(params));
		
		params.put("fields", new String[] { "registNo", "title" });
		assertEquals(1, service.getResearchCount(params));
		
		params = new HashMap<String, Object>();
		params.put("openYn", "N");
		assertEquals(0, service.getResearchCount(params));
		
		params.put("openYn", "Y");
		assertEquals(10, service.getResearchCount(params));
		
		params.put("openFrom", "2019-08-27");
		params.put("openTo", "2019-08-27");
		assertEquals(10, service.getResearchCount(params));
		
		params.put("registStatus", status[0]);
		assertEquals(2, service.getResearchCount(params));
		
		params.put("registUser",  owner);
		assertEquals(2, service.getResearchCount(params));
		
		assertEquals(0, service.getResearchCount(new NgsDataRegist(30)));
	}
	
	@Test
	public void test6DeleteResearch() throws Exception {
		for(int i=0; i < 10 ; i++) {
			try {
				service.deleteResearch(new Research(i+1));
				assertTrue(true);
			} catch(Exception e) {
				assertFalse(true);
			}
		}
	}
}
