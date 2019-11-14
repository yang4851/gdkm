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

import com.insilicogen.gdkm.model.Code;
import com.insilicogen.gdkm.model.Research;
import com.insilicogen.gdkm.model.ResearchOmicsFile;
import com.insilicogen.gdkm.model.User;
import com.insilicogen.gdkm.service.ResearchOmicsFileService;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations={"classpath:/egovframework/spring/context-*.xml"})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestResearchOmicsFileService {

	@Resource(name="ResearchOmicsFileService")
	ResearchOmicsFileService service;
	
	private User owner;
	
	private String[] types;
	
	private Code[] categories;
	
	private Research[] researches;
	
	@Before
	public void setup() {
		owner = new User();
		owner.setUserId("administrator");
	
		categories = new Code[2];
		categories[0] = new Code();
		categories[0].setCode("FF-01");		
		categories[1] = new Code();
		categories[1].setCode("FF-02");
		
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
	public void test1CreateOmicsFile() throws Exception {
		for(int i=0; i < 10 ; i++) {
			ResearchOmicsFile omicsFile = new ResearchOmicsFile();
			omicsFile.setResearch(researches[i%researches.length]);
			omicsFile.setCategory(categories[i%categories.length]);
			omicsFile.setName("omics-file-name" + i);
			omicsFile.setSize(123456789L+i);
			omicsFile.setType(types[i%types.length]);
			omicsFile.setDescription("description" + i);
			omicsFile.setRegistUser(owner);
			
			assertEquals(i+1, service.createOmicsFile(omicsFile).getId());
		}
	}
	
	@Test
	public void test2GetOmicsFile() throws Exception {
		for(int i=0; i < 10 ; i++) {
			assertNotNull(service.getOmicsFile(i+1));
			assertEquals(i+1, service.getOmicsFile(i+1).getId());
			assertNotNull(service.getOmicsFile(i+1).getRegistNo());
			assertEquals(researches[i%researches.length], service.getOmicsFile(i+1).getResearch());
			assertEquals(categories[i%categories.length], service.getOmicsFile(i+1).getCategory());
			assertEquals("omics-file-name"+i, service.getOmicsFile(i+1).getName());
			assertEquals(new Long(123456789+i), service.getOmicsFile(i+1).getSize());
			assertEquals(types[i%types.length], service.getOmicsFile(i+1).getType());
			assertEquals("description"+i, service.getOmicsFile(i+1).getDescription());
			assertEquals(owner, service.getOmicsFile(i+1).getRegistUser());
			assertNotNull(service.getOmicsFile(i+1).getRegistDate());
		}
	}
	
	@Test
	public void test3ChangeOmicsFile() throws Exception {
		for(int i=0; i < 10 ; i++) {
			ResearchOmicsFile omicsFile = new ResearchOmicsFile(i+1);
			omicsFile.setCategory(categories[(i+1)%categories.length]);
			omicsFile.setName("omics-file-name-modified" + i);
			omicsFile.setSize(123456789L+(i+1));
			omicsFile.setType(types[(i+1)%types.length]);
			omicsFile.setDescription("description-modified" + i);
			
			assertNotNull(service.changeOmicsFile(omicsFile));
			
			assertEquals(i+1, service.getOmicsFile(i+1).getId());
			assertNotNull(service.getOmicsFile(i+1).getRegistNo());
			assertEquals(researches[i%researches.length], service.getOmicsFile(i+1).getResearch());
			assertEquals(categories[(i+1)%categories.length], service.getOmicsFile(i+1).getCategory());
			assertEquals("omics-file-name-modified"+i, service.getOmicsFile(i+1).getName());
			assertEquals(new Long(123456789+(i+1)), service.getOmicsFile(i+1).getSize());
			assertEquals(types[(i+1)%types.length], service.getOmicsFile(i+1).getType());
			assertEquals("description-modified"+i, service.getOmicsFile(i+1).getDescription());
			assertEquals(owner, service.getOmicsFile(i+1).getRegistUser());
			assertNotNull(service.getOmicsFile(i+1).getRegistDate());
		}
	}
	
	@Test
	public void test4GetOmicsFileList() throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		assertEquals(10, service.getOmicsFileList(params).size());
		
		params.put("keyword", "description-modified");
		assertEquals(10, service.getOmicsFileList(params).size());
		
		params.put("keyword", "description-modified5");
		assertEquals(1, service.getOmicsFileList(params).size());
		
		params.put("fields", new String[] {"description"});
		assertEquals(1, service.getOmicsFileList(params).size());
		
		params.put("fields", new String[] {"registNo"});
		assertEquals(0, service.getOmicsFileList(params).size());
		
		params.put("fields", new String[] {"registNo", "description"});
		assertEquals(1, service.getOmicsFileList(params).size());
		
		params = new HashMap<String, Object>();
		params.put("category", categories[0]);
		assertEquals(5, service.getOmicsFileList(params).size());
		
		params = new HashMap<String, Object>();
		params.put("fileType", types[0]);
		assertEquals(2, service.getOmicsFileList(params).size());
		
		assertEquals(4, service.getOmicsFileList(researches[0]).size());
		
		params = new HashMap<String, Object>();
		params.put("firstIndex", 0);
		params.put("rowSize", 5);
		assertEquals(5, service.getOmicsFileList(params).size());

		params.put("registUser", owner);
		assertEquals(5, service.getOmicsFileList(params).size());
	}
	
	@Test
	public void test5GetOmicsFileCount() throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		assertEquals(10, service.getOmicsFileCount(params));
		
		params.put("keyword", "description-modified");
		assertEquals(10, service.getOmicsFileCount(params));
		
		params.put("keyword", "description-modified5");
		assertEquals(1, service.getOmicsFileCount(params));
		
		params.put("fields", new String[] {"description"});
		assertEquals(1, service.getOmicsFileCount(params));
		
		params.put("fields", new String[] {"registNo"});
		assertEquals(0, service.getOmicsFileCount(params));
		
		params.put("fields", new String[] {"registNo", "description"});
		assertEquals(1, service.getOmicsFileCount(params));
		
		params = new HashMap<String, Object>();
		params.put("category", categories[0]);
		assertEquals(5, service.getOmicsFileCount(params));
		
		params = new HashMap<String, Object>();
		params.put("fileType", types[0]);
		assertEquals(2, service.getOmicsFileCount(params));
		
		assertEquals(4, service.getOmicsFileCount(researches[0]));
		
		params.put("registUser", owner);
		assertEquals(2, service.getOmicsFileCount(params));
	}
	
	@Test
	public void test6DeleteOmicsFile() throws Exception {
		assertNotNull(service.deleteOmicsFile(new ResearchOmicsFile(1)));
		assertNotNull(service.deleteOmicsFile(new ResearchOmicsFile(2)));
		assertNotNull(service.deleteOmicsFile(new ResearchOmicsFile(3)));
		assertEquals(3, service.deleteOmicsFile(researches[0]).size());
		assertEquals(2, service.deleteOmicsFile(researches[1]).size());
		assertEquals(2, service.deleteOmicsFile(researches[2]).size());
	}
}
