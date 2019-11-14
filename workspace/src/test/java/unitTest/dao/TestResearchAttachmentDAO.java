package unitTest.dao;

import static org.junit.Assert.*;

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

import com.insilicogen.gdkm.dao.ResearchAttachmentDAO;
import com.insilicogen.gdkm.model.Research;
import com.insilicogen.gdkm.model.ResearchAttachment;
import com.insilicogen.gdkm.model.User;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations={"classpath:/egovframework/spring/context-*.xml"})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestResearchAttachmentDAO {

	@Resource(name="ResearchAttachmentDAO")
	ResearchAttachmentDAO dao;
	
	private User owner;
	
	private String[] types = new String[] { 
		"type1", "type2", "type3", "other"	
	};
	
	private Research[] researches; 
	
	@Before
	public void setup() {
		owner = new User();
		owner.setUserId("administrator");
		
		researches = new Research[3];
		researches[0] = new Research(1);
		researches[0].setRegistNo("GDKM-R-0001");
		researches[1] = new Research(2);
		researches[1].setRegistNo("GDKM-R-0002");
		researches[2] = new Research(3);
		researches[2].setRegistNo("GDKM-R-0003");
	}
	
	@Test
	public void test1InsertAttachment() throws Exception {
		for(int i=0; i < 10 ; i++) {
			ResearchAttachment attachment = new ResearchAttachment();
			attachment.setResearch(researches[i%researches.length]);
			attachment.setName("attachment-file-name" + i);
			attachment.setSize(123456789L+i);
			attachment.setType(types[i%types.length]);
			attachment.setDescription("description" + i);
			attachment.setRegistUser(owner);
			
			assertEquals(1, dao.insertAttachment(attachment));
			assertEquals(i+1, attachment.getId());
		}
	}
	
	@Test
	public void test2GetAttachment() throws Exception {
		for(int i=0; i < 10 ; i++) {
			assertNotNull(dao.selectAttachment(i+1));
			assertEquals(i+1, dao.selectAttachment(i+1).getId());
			assertNotNull(dao.selectAttachment(i+1).getRegistNo());
			assertEquals(researches[i%researches.length], dao.selectAttachment(i+1).getResearch());
			assertEquals("attachment-file-name"+i, dao.selectAttachment(i+1).getName());
			assertEquals(new Long(123456789+i), dao.selectAttachment(i+1).getSize());
			assertEquals(types[i%types.length], dao.selectAttachment(i+1).getType());
			assertEquals("description"+i, dao.selectAttachment(i+1).getDescription());
			assertEquals(owner, dao.selectAttachment(i+1).getRegistUser());
			assertNotNull(dao.selectAttachment(i+1).getRegistDate());
		}
	}
	
	@Test
	public void test3UpdateAttachment() throws Exception {
		for(int i=0; i < 10 ; i++) {
			ResearchAttachment attachment = new ResearchAttachment(i+1);
			attachment.setName("attachment-file-name-modified" + i);
			attachment.setSize(123456789L+(i+1));
			attachment.setType(types[(i+1)%types.length]);
			attachment.setDescription("description-modified" + i);
			
			assertEquals(1, dao.updateAttachment(attachment));
			
			assertEquals(i+1, dao.selectAttachment(i+1).getId());
			assertNotNull(dao.selectAttachment(i+1).getRegistNo());
			assertEquals(researches[i%researches.length], dao.selectAttachment(i+1).getResearch());
			assertEquals("attachment-file-name-modified"+i, dao.selectAttachment(i+1).getName());
			assertEquals(new Long(123456789+(i+1)), dao.selectAttachment(i+1).getSize());
			assertEquals(types[(i+1)%types.length], dao.selectAttachment(i+1).getType());
			assertEquals("description-modified"+i, dao.selectAttachment(i+1).getDescription());
			assertEquals(owner, dao.selectAttachment(i+1).getRegistUser());
			assertNotNull(dao.selectAttachment(i+1).getRegistDate());
		}
	}
	
	@Test
	public void test4GetAttachmentList() throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		assertEquals(10, dao.selectAttachmentList(params).size());
		
		params.put("keyword", "description-modified");
		assertEquals(10, dao.selectAttachmentList(params).size());
		
		params.put("keyword", "description-modified5");
		assertEquals(1, dao.selectAttachmentList(params).size());
		
		params.put("fields", new String[] {"description"});
		assertEquals(1, dao.selectAttachmentList(params).size());
		
		params.put("fields", new String[] {"registNo"});
		assertEquals(0, dao.selectAttachmentList(params).size());
		
		params.put("fields", new String[] {"registNo", "description"});
		assertEquals(1, dao.selectAttachmentList(params).size());
		
		params = new HashMap<String, Object>();
		params.put("fileType", types[0]);
		assertEquals(2, dao.selectAttachmentList(params).size());
		
		assertEquals(4, dao.selectAttachmentList(researches[0]).size());
		
		params = new HashMap<String, Object>();
		params.put("firstIndex", 0);
		params.put("rowSize", 5);
		assertEquals(5, dao.selectAttachmentList(params).size());
		
		params.put("registFrom", "2019-08-27");
		params.put("registTo", "2019-08-27");
		assertEquals(5, dao.selectAttachmentList(params).size());
		
		params.put("registUser", owner);
		assertEquals(5, dao.selectAttachmentList(params).size());
	}
	
	@Test
	public void test5GetAttachmentCount() throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		assertEquals(10, dao.selectAttachmentCount(params));
		
		params.put("keyword", "description-modified");
		assertEquals(10, dao.selectAttachmentCount(params));
		
		params.put("keyword", "description-modified5");
		assertEquals(1, dao.selectAttachmentCount(params));
		
		params.put("fields", new String[] {"description"});
		assertEquals(1, dao.selectAttachmentCount(params));
		
		params.put("fields", new String[] {"registNo"});
		assertEquals(0, dao.selectAttachmentCount(params));
		
		params.put("fields", new String[] {"registNo", "description"});
		assertEquals(1, dao.selectAttachmentCount(params));
		
		params = new HashMap<String, Object>();
		params.put("fileType", types[0]);
		assertEquals(2, dao.selectAttachmentCount(params));
		
		assertEquals(4, dao.selectAttachmentCount(researches[0]));
		
		params = new HashMap<String, Object>();
		params.put("registFrom", "2019-08-27");
		params.put("registTo", "2019-08-27");
		assertEquals(10, dao.selectAttachmentCount(params));
		
		params.put("registUser", owner);
		assertEquals(10, dao.selectAttachmentCount(params));
	}
	
	@Test
	public void test6DeleteAttachment() throws Exception {
		assertEquals(1, dao.deleteAttachment(new ResearchAttachment(1)));
		assertEquals(1, dao.deleteAttachment(new ResearchAttachment(2)));
		assertEquals(1, dao.deleteAttachment(new ResearchAttachment(3)));
		assertEquals(3, dao.deleteAttachment(researches[0]));
		assertEquals(2, dao.deleteAttachment(researches[1]));
		assertEquals(2, dao.deleteAttachment(researches[2]));
	}
}
