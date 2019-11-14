package unitTest.service;

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

import com.insilicogen.gdkm.model.Research;
import com.insilicogen.gdkm.model.ResearchAttachment;
import com.insilicogen.gdkm.model.User;
import com.insilicogen.gdkm.service.ResearchAttachmentService;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations={"classpath:/egovframework/spring/context-*.xml"})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestResearchAttachmentService {

	@Resource(name="ResearchAttachmentService")
	ResearchAttachmentService service;
	
	private User owner;
	
	private String[] types;
	
	private Research[] researches;
	
	@Before
	public void setup() {
		owner = new User();
		owner.setUserId("administrator");
	
		types = new String[4];
		types[0] = "type1";
		types[1] = "type2";
		types[2] = "type3";
		types[3] = "other";
		
		researches = new Research[3];
		researches[0] = new Research(1);
		researches[0].setRegistNo("GDKM-R-0001");
		researches[1] = new Research(2);
		researches[1].setRegistNo("GDKM-R-0002");
		researches[2] = new Research(3);
		researches[2].setRegistNo("GDKM-R-0003");
	}
	
	@Test
	public void test1CreateAttachment() throws Exception {
		for(int i=0; i < 10 ; i++) {
			ResearchAttachment attachment = new ResearchAttachment();
			attachment.setResearch(researches[i%researches.length]);
			attachment.setName("attachment-file-name" + i);
			attachment.setSize(123456789L+i);
			attachment.setType(types[i%types.length]);
			attachment.setDescription("description" + i);
			attachment.setRegistUser(owner);
			
			assertEquals(i+1, service.createAttachment(attachment).getId());
		}
	}
	
	@Test
	public void test2GetAttachment() throws Exception {
		for(int i=0; i < 10 ; i++) {
			assertNotNull(service.getAttachment(i+1));
			assertEquals(i+1, service.getAttachment(i+1).getId());
			assertNotNull(service.getAttachment(i+1).getRegistNo());
			assertEquals(researches[i%researches.length], service.getAttachment(i+1).getResearch());
			assertEquals("attachment-file-name"+i, service.getAttachment(i+1).getName());
			assertEquals(new Long(123456789+i), service.getAttachment(i+1).getSize());
			assertEquals(types[i%types.length], service.getAttachment(i+1).getType());
			assertEquals("description"+i, service.getAttachment(i+1).getDescription());
			assertEquals(owner, service.getAttachment(i+1).getRegistUser());
			assertNotNull(service.getAttachment(i+1).getRegistDate());
		}
	}
	
	@Test
	public void test3ChangeAttachment() throws Exception {
		for(int i=0; i < 10 ; i++) {
			ResearchAttachment attachment = new ResearchAttachment(i+1);
			attachment.setName("attachment-file-name-modified" + i);
			attachment.setSize(123456789L+(i+1));
			attachment.setType(types[(i+1)%types.length]);
			attachment.setDescription("description-modified" + i);
			
			assertNotNull(service.changeAttachment(attachment));
			
			assertEquals(i+1, service.getAttachment(i+1).getId());
			assertNotNull(service.getAttachment(i+1).getRegistNo());
			assertEquals(researches[i%researches.length], service.getAttachment(i+1).getResearch());
			assertEquals("attachment-file-name-modified"+i, service.getAttachment(i+1).getName());
			assertEquals(new Long(123456789+(i+1)), service.getAttachment(i+1).getSize());
			assertEquals(types[(i+1)%types.length], service.getAttachment(i+1).getType());
			assertEquals("description-modified"+i, service.getAttachment(i+1).getDescription());
			assertEquals(owner, service.getAttachment(i+1).getRegistUser());
			assertNotNull(service.getAttachment(i+1).getRegistDate());
		}
	}
	
	@Test
	public void test4GetAttachmentList() throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		assertEquals(10, service.getAttachmentList(params).size());
		
		params.put("keyword", "description-modified");
		assertEquals(10, service.getAttachmentList(params).size());
		
		params.put("keyword", "description-modified5");
		assertEquals(1, service.getAttachmentList(params).size());
		
		params.put("fields", new String[] {"description"});
		assertEquals(1, service.getAttachmentList(params).size());
		
		params.put("fields", new String[] {"registNo"});
		assertEquals(0, service.getAttachmentList(params).size());
		
		params.put("fields", new String[] {"registNo", "description"});
		assertEquals(1, service.getAttachmentList(params).size());
		
		params = new HashMap<String, Object>();
		params.put("fileType", types[0]);
		assertEquals(2, service.getAttachmentList(params).size());
		
		assertEquals(4, service.getAttachmentList(researches[0]).size());
		
		params = new HashMap<String, Object>();
		params.put("firstIndex", 0);
		params.put("rowSize", 5);
		assertEquals(5, service.getAttachmentList(params).size());
		
		params.put("registUser", owner);
		assertEquals(5, service.getAttachmentList(params).size());
	}
	
	@Test
	public void test5GetAttachmentCount() throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		assertEquals(10, service.getAttachmentCount(params));
		
		params.put("keyword", "description-modified");
		assertEquals(10, service.getAttachmentCount(params));
		
		params.put("keyword", "description-modified5");
		assertEquals(1, service.getAttachmentCount(params));
		
		params.put("fields", new String[] {"description"});
		assertEquals(1, service.getAttachmentCount(params));
		
		params.put("fields", new String[] {"registNo"});
		assertEquals(0, service.getAttachmentCount(params));
		
		params.put("fields", new String[] {"registNo", "description"});
		assertEquals(1, service.getAttachmentCount(params));
		
		params = new HashMap<String, Object>();
		params.put("fileType", types[0]);
		assertEquals(2, service.getAttachmentCount(params));
		
		assertEquals(4, service.getAttachmentCount(researches[0]));
		
		params.put("registUser", owner);
		assertEquals(2, service.getAttachmentCount(params));
	}
	
	@Test
	public void test6DeleteAttachment() throws Exception {
		assertNotNull(service.deleteAttachment(new ResearchAttachment(1)));
		assertNotNull(service.deleteAttachment(new ResearchAttachment(2)));
		assertNotNull(service.deleteAttachment(new ResearchAttachment(3)));
		assertEquals(3, service.deleteAttachment(researches[0]).size());
		assertEquals(2, service.deleteAttachment(researches[1]).size());
		assertEquals(2, service.deleteAttachment(researches[2]).size());
	}
}
