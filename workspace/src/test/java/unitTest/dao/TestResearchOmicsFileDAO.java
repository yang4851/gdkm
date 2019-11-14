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

import com.insilicogen.gdkm.dao.ResearchOmicsFileDAO;
import com.insilicogen.gdkm.model.Code;
import com.insilicogen.gdkm.model.Research;
import com.insilicogen.gdkm.model.ResearchOmicsFile;
import com.insilicogen.gdkm.model.User;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations={"classpath:/egovframework/spring/context-*.xml"})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestResearchOmicsFileDAO {

	@Resource(name="ResearchOmicsFileDAO")
	ResearchOmicsFileDAO dao;
	
	private User owner;
	
	private String[] types = new String[] { 
		"type1", "type2", "type3", "other"	
	};
	
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
		
		researches = new Research[3];
		researches[0] = new Research(1);
		researches[0].setRegistNo("GDKM-R-0001");
		researches[1] = new Research(2);
		researches[1].setRegistNo("GDKM-R-0002");
		researches[2] = new Research(3);
		researches[2].setRegistNo("GDKM-R-0003");
	}
	
	@Test
	public void test1InsertOmicsFile() throws Exception {
		for(int i=0; i < 10 ; i++) {
			ResearchOmicsFile omicsFile = new ResearchOmicsFile();
			omicsFile.setResearch(researches[i%researches.length]);
			omicsFile.setCategory(categories[i%categories.length]);
			omicsFile.setName("omics-file-name" + i);
			omicsFile.setSize(123456789L+i);
			omicsFile.setType(types[i%types.length]);
			omicsFile.setDescription("description" + i);
			omicsFile.setRegistUser(owner);
			
			assertEquals(1, dao.insertOmicsFile(omicsFile));
			assertEquals(i+1, omicsFile.getId());
		}
	}
	
	@Test
	public void test2SelectOmicsFile() throws Exception {
		for(int i=0; i < 10 ; i++) {
			assertNotNull(dao.selectOmicsFile(i+1));
			assertEquals(i+1, dao.selectOmicsFile(i+1).getId());
			assertNotNull(dao.selectOmicsFile(i+1).getRegistNo());
			assertEquals(researches[i%researches.length], dao.selectOmicsFile(i+1).getResearch());
			assertEquals(categories[i%categories.length], dao.selectOmicsFile(i+1).getCategory());
			assertEquals("omics-file-name"+i, dao.selectOmicsFile(i+1).getName());
			assertEquals(new Long(123456789+i), dao.selectOmicsFile(i+1).getSize());
			assertEquals(types[i%types.length], dao.selectOmicsFile(i+1).getType());
			assertEquals("description"+i, dao.selectOmicsFile(i+1).getDescription());
			assertEquals(owner, dao.selectOmicsFile(i+1).getRegistUser());
			assertNotNull(dao.selectOmicsFile(i+1).getRegistDate());
		}
	}
	
	@Test
	public void test3UpdateOmicsFile() throws Exception {
		for(int i=0; i < 10 ; i++) {
			ResearchOmicsFile omicsFile = new ResearchOmicsFile(i+1);
			omicsFile.setCategory(categories[(i+1)%categories.length]);
			omicsFile.setName("omics-file-name-modified" + i);
			omicsFile.setSize(123456789L+(i+1));
			omicsFile.setType(types[(i+1)%types.length]);
			omicsFile.setDescription("description-modified" + i);
			
			assertEquals(1, dao.updateOmicsFile(omicsFile));
			
			assertEquals(i+1, dao.selectOmicsFile(i+1).getId());
			assertNotNull(dao.selectOmicsFile(i+1).getRegistNo());
			assertEquals(researches[i%researches.length], dao.selectOmicsFile(i+1).getResearch());
			assertEquals(categories[(i+1)%categories.length], dao.selectOmicsFile(i+1).getCategory());
			assertEquals("omics-file-name-modified"+i, dao.selectOmicsFile(i+1).getName());
			assertEquals(new Long(123456789+(i+1)), dao.selectOmicsFile(i+1).getSize());
			assertEquals(types[(i+1)%types.length], dao.selectOmicsFile(i+1).getType());
			assertEquals("description-modified"+i, dao.selectOmicsFile(i+1).getDescription());
			assertEquals(owner, dao.selectOmicsFile(i+1).getRegistUser());
			assertNotNull(dao.selectOmicsFile(i+1).getRegistDate());
		}
	}
	
	@Test
	public void test4SelectOmicsFileList() throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		assertEquals(10, dao.selectOmicsFileList(params).size());
		
		params.put("keyword", "description-modified");
		assertEquals(10, dao.selectOmicsFileList(params).size());
		
		params.put("keyword", "description-modified5");
		assertEquals(1, dao.selectOmicsFileList(params).size());
		
		params.put("fields", new String[] {"description"});
		assertEquals(1, dao.selectOmicsFileList(params).size());
		
		params.put("fields", new String[] {"registNo"});
		assertEquals(0, dao.selectOmicsFileList(params).size());
		
		params.put("fields", new String[] {"registNo", "description"});
		assertEquals(1, dao.selectOmicsFileList(params).size());
		
		params = new HashMap<String, Object>();
		params.put("category", categories[0]);
		assertEquals(5, dao.selectOmicsFileList(params).size());
		
		params = new HashMap<String, Object>();
		params.put("fileType", types[0]);
		assertEquals(2, dao.selectOmicsFileList(params).size());
		
		assertEquals(4, dao.selectOmicsFileList(researches[0]).size());
		
		params = new HashMap<String, Object>();
		params.put("firstIndex", 0);
		params.put("rowSize", 5);
		assertEquals(5, dao.selectOmicsFileList(params).size());
		
//		params.put("registFrom", "2019-08-27");
//		params.put("registTo", "2019-08-27");
//		assertEquals(5, dao.getOmicsFileList(params).size());
		
		params.put("registUser", owner);
		assertEquals(5, dao.selectOmicsFileList(params).size());
	}
	
	@Test
	public void test5SelectOmicsFileCount() throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		assertEquals(10, dao.selectOmicsFileCount(params));
		
		params.put("keyword", "description-modified");
		assertEquals(10, dao.selectOmicsFileCount(params));
		
		params.put("keyword", "description-modified5");
		assertEquals(1, dao.selectOmicsFileCount(params));
		
		params.put("fields", new String[] {"description"});
		assertEquals(1, dao.selectOmicsFileCount(params));
		
		params.put("fields", new String[] {"registNo"});
		assertEquals(0, dao.selectOmicsFileCount(params));
		
		params.put("fields", new String[] {"registNo", "description"});
		assertEquals(1, dao.selectOmicsFileCount(params));
		
		params = new HashMap<String, Object>();
		params.put("category", categories[0]);
		assertEquals(5, dao.selectOmicsFileCount(params));
		
		params = new HashMap<String, Object>();
		params.put("fileType", types[0]);
		assertEquals(2, dao.selectOmicsFileCount(params));
		
		assertEquals(4, dao.selectOmicsFileCount(researches[0]));
		
//		params = new HashMap<String, Object>();
//		params.put("registFrom", "2019-08-27");
//		params.put("registTo", "2019-08-27");
//		assertEquals(10, dao.getOmicsFileCount(params));
		
		params.put("registUser", owner);
		assertEquals(10, dao.selectOmicsFileCount(params));
	}
	
	@Test
	public void test6DeleteOmicsFile() throws Exception {
		assertEquals(1, dao.deleteOmicsFile(new ResearchOmicsFile(1)));
		assertEquals(1, dao.deleteOmicsFile(new ResearchOmicsFile(2)));
		assertEquals(1, dao.deleteOmicsFile(new ResearchOmicsFile(3)));
		assertEquals(3, dao.deleteOmicsFile(researches[0]));
		assertEquals(2, dao.deleteOmicsFile(researches[1]));
		assertEquals(2, dao.deleteOmicsFile(researches[2]));
	}
}
