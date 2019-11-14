package unitTest.dao;

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

import com.insilicogen.gdkm.dao.ResearchDAO;
import com.insilicogen.gdkm.model.Research;
import com.insilicogen.gdkm.model.User;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations={"classpath:/egovframework/spring/context-*.xml"})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestResearchDAO {

	static String[] status = new String[] { 
		"ready", "verifying", "invalid", "verified", "complete" 	
	};
	
	@Resource(name="ResearchDAO")
	private ResearchDAO dao;
	
	private User owner;
	
	@Before
	public void setup() {
		owner = new User();
		owner.setUserId("administrator");
	}
	
	@Test
	public void test1InsertResearch() throws Exception {
		for(int i=0; i < 10; i++) {
			Research research = new Research();
			research.setTitle("title" + i);
			research.setJournal("journal" + i);
			research.setVolume("volume" + i);
			research.setIssue("issue" + i);
			research.setPages("pages" + i);
			research.setPublished("published" + i);
			research.setSubmitter("submitter" + i);
			research.setSubmitOrgan("submitOrgan" + i);
			research.setFullTextLink("fullTextLink" + i);
			research.setContent("content" + i);
			research.setOpenYn("N");
			research.setRegistUser(owner);
			
			assertEquals(1, dao.insertResearch(research));
			assertEquals(i+1, research.getId());
		}
	}
	
	@Test
	public void test2SelectResearch() throws Exception {
		for(int i=0; i < 10; i++) {
			assertNotNull(dao.selectResearch(i+1));
			assertEquals("title"+i, dao.selectResearch(i+1).getTitle());
			assertEquals("journal"+i, dao.selectResearch(i+1).getJournal());
			assertEquals("volume"+i, dao.selectResearch(i+1).getVolume());
			assertEquals("issue"+i, dao.selectResearch(i+1).getIssue());
			assertEquals("pages"+i, dao.selectResearch(i+1).getPages());
			assertEquals("published"+i, dao.selectResearch(i+1).getPublished());
			assertEquals("submitter"+i, dao.selectResearch(i+1).getSubmitter());
			assertEquals("submitOrgan"+i, dao.selectResearch(i+1).getSubmitOrgan());
			assertEquals("fullTextLink"+i, dao.selectResearch(i+1).getFullTextLink());
			assertEquals("content"+i, dao.selectResearch(i+1).getContent());
			assertEquals("N", dao.selectResearch(i+1).getOpenYn());
			assertNull(dao.selectResearch(i+1).getOpenDate());
			assertEquals(owner, dao.selectResearch(i+1).getRegistUser());
			assertNotNull(dao.selectResearch(i+1).getRegistDate());
			assertEquals(owner, dao.selectResearch(i+1).getUpdateUser());
			assertNotNull(dao.selectResearch(i+1).getUpdateDate());
		}
	}
	
	@Test
	public void test3UpdateResearch() throws Exception {
		for(int i=0; i < 10; i++) {
			Research research = new Research();
			research.setId(i+1);
			research.setTitle("title-modified" + i);
			research.setJournal("journal-modified" + i);
			research.setVolume("volume-modified" + i);
			research.setIssue("issue-modified" + i);
			research.setPages("pages-modified" + i);
			research.setPublished("published-modified" + i);
			research.setSubmitter("submitter-modified" + i);
			research.setSubmitOrgan("submitOrgan-modified" + i);
			research.setFullTextLink("fullTextLink-modified" + i);
			research.setContent("content-modified" + i);
			research.setOpenYn("Y");
			research.setOpenDate(new Date());
			research.setRegistStatus(status[i%status.length]);
			research.setUpdateUser(owner);
			assertEquals(1, dao.updateResearch(research));
			
			assertEquals("title-modified"+i, dao.selectResearch(i+1).getTitle());
			assertEquals("journal-modified"+i, dao.selectResearch(i+1).getJournal());
			assertEquals("volume-modified"+i, dao.selectResearch(i+1).getVolume());
			assertEquals("issue-modified"+i, dao.selectResearch(i+1).getIssue());
			assertEquals("pages-modified"+i, dao.selectResearch(i+1).getPages());
			assertEquals("published-modified"+i, dao.selectResearch(i+1).getPublished());
			assertEquals("submitter-modified"+i, dao.selectResearch(i+1).getSubmitter());
			assertEquals("submitOrgan-modified"+i, dao.selectResearch(i+1).getSubmitOrgan());
			assertEquals("fullTextLink-modified"+i, dao.selectResearch(i+1).getFullTextLink());
			assertEquals("content-modified"+i, dao.selectResearch(i+1).getContent());
			assertEquals("Y", dao.selectResearch(i+1).getOpenYn());
			assertNotNull(dao.selectResearch(i+1).getOpenDate());
			assertEquals(status[i%status.length], dao.selectResearch(i+1).getRegistStatus());
			assertEquals(owner, dao.selectResearch(i+1).getRegistUser());
			assertNotNull(dao.selectResearch(i+1).getRegistDate());
			assertEquals(owner, dao.selectResearch(i+1).getUpdateUser());
			assertNotNull(dao.selectResearch(i+1).getUpdateDate());
		}
	}
	
	@Test
	public void test4SelectResearchList() throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		assertEquals(10, dao.selectResearchList(params).size());
		
		params.put("keyword", "title-modified");
		assertEquals(10, dao.selectResearchList(params).size());
		
		params.put("keyword", "title-modified1");
		assertEquals(1, dao.selectResearchList(params).size());
		
		params.put("fields", new String[] { "registNo" });
		assertEquals(0, dao.selectResearchList(params).size());
		
		params.put("fields", new String[] { "registNo", "title" });
		assertEquals(1, dao.selectResearchList(params).size());
		
		params.put("keyword", "journal");
		params.remove("fields");
		assertEquals(10, dao.selectResearchList(params).size());
		
		params = new HashMap<String, Object>();
		params.put("openYn", "N");
		assertEquals(0, dao.selectResearchList(params).size());
		
		params.put("openYn", "Y");
		assertEquals(10, dao.selectResearchList(params).size());
		
//		params.put("openFrom", "2019-08-27");
//		params.put("openTo", "2019-08-27");
//		assertEquals(10, dao.selectResearchList(params).size());
		
		params.put("firstIndex", 0);
		params.put("rowSize", 5);
		assertEquals(5, dao.selectResearchList(params).size());
		
		params.put("registStatus", status[0]);
		assertEquals(2, dao.selectResearchList(params).size());
		
		params.put("registUser",  owner);
		assertEquals(2, dao.selectResearchList(params).size());
	}
	
	@Test
	public void test5SelectResearchCount() throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		assertEquals(10, dao.selectResearchCount(params));
		
		params.put("keyword", "title-modified");
		assertEquals(10, dao.selectResearchCount(params));
		
		params.put("keyword", "title-modified1");
		assertEquals(1, dao.selectResearchCount(params));
		
		params.put("fields", new String[] { "registNo" });
		assertEquals(0, dao.selectResearchCount(params));
		
		params.put("fields", new String[] { "registNo", "title" });
		assertEquals(1, dao.selectResearchCount(params));
		
		params = new HashMap<String, Object>();
		params.put("openYn", "N");
		assertEquals(0, dao.selectResearchCount(params));
		
		params.put("openYn", "Y");
		assertEquals(10, dao.selectResearchCount(params));
		
//		params.put("openFrom", "2019-08-27");
//		params.put("openTo", "2019-08-27");
//		assertEquals(10, dao.selectResearchCount(params));
		
		params.put("registStatus", status[0]);
		assertEquals(2, dao.selectResearchCount(params));
		
		params.put("registUser",  owner);
		assertEquals(2, dao.selectResearchCount(params));
	}
	
	@Test
	public void test6DeleteResearch() throws Exception {
		for(int i=0; i < 10 ; i++) {
			assertEquals(1, dao.deleteResearch(new Research(i+1)));
		}
	}
}
