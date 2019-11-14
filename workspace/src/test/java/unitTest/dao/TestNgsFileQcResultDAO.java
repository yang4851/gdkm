package unitTest.dao;

import static org.junit.Assert.*;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.insilicogen.gdkm.dao.NgsFileQcResultDAO;
import com.insilicogen.gdkm.model.NgsDataAchive;
import com.insilicogen.gdkm.model.NgsDataRegist;
import com.insilicogen.gdkm.model.NgsFileQcResult;
import com.insilicogen.gdkm.model.User;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations={"classpath:/egovframework/spring/context-*.xml"})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestNgsFileQcResultDAO {

	@Resource(name="NgsFileQcResultDAO")
	private NgsFileQcResultDAO dao;
	
	private User user;
	
	private NgsDataRegist regist;
	
	private NgsDataAchive[] achives = new NgsDataAchive[10];
	
	private String[] status = new String[] { "ready", "success", "failed", "warn" }; 
	
	@Before
	public void setup() {
		regist = new NgsDataRegist();
		regist.setId(1);
		
		for(int i=0; i < 10 ; i++) {
			achives[i] = new NgsDataAchive();
			achives[i].setId(i+1);
			achives[i].setDataRegist(regist);
		}
		
		user = new User();
		user.setUserId("administrator");
		user.setName("admin");
		user.setAuthority("admin");
	}
	
	@Test
	public void test1InsertNgsFileQc() throws Exception {
		for(int i=0; i < 10 ; i++) {
			NgsFileQcResult data = new NgsFileQcResult();
			data.setAchive(achives[i]);
			data.setFileName("fileName-" + i);
			data.setFilePath("filePath-" + i);
			data.setFileSize(i+1);
			data.setFileType("fileType-" + i);
			data.setSummary("summary-" + i);
			data.setStatus(status[i%status.length]);
			data.setRegistUser(user);
			
			assertEquals(1, dao.insertNgsFileQcResult(data));
		}
	}
	
	@Test
	public void test2SelectNgsFileQc() throws Exception {
		for(int i=0; i < 10 ; i++) {
			NgsFileQcResult data = dao.selectNgsFileQcResult(i+1);
			assertEquals(i+1, data.getId());
			assertEquals(achives[i], data.getAchive());
			assertEquals("fileName-" + i, data.getFileName());
			assertEquals("filePath-" + i, data.getFilePath());
			assertEquals(i+1, data.getFileSize());
			assertEquals("fileType-" + i, data.getFileType());
			assertEquals("summary-" + i, data.getSummary());
			assertEquals(status[i%status.length], data.getStatus());
			assertEquals(user, data.getRegistUser());
		}
	}
	
	@Test
	public void test3UpdateNgsFileQc() throws Exception {
		for(int i=0; i < 10 ; i++) {
			NgsFileQcResult data = new NgsFileQcResult();
			data.setId(i+1);
			data.setAchive(achives[i]);
			data.setFileName("fileName-" + (i+1));
			data.setFilePath("filePath-" + (i+1));
			data.setFileSize(i+2);
			data.setFileType("fileType-" + (i+1));
			data.setSummary("summary-" + (i+1));
			data.setStatus(status[(i+1)%status.length]);
			data.setRegistUser(user);
			
			assertEquals(1, dao.updateNgsFileQcResult(data));
			
			data = dao.selectNgsFileQcResult(i+1);
			assertEquals(i+1, data.getId());
			assertEquals(achives[i], data.getAchive());
			assertEquals("fileName-" + (i+1), data.getFileName());
			assertEquals("filePath-" + (i+1), data.getFilePath());
			assertEquals(i+2, data.getFileSize());
			assertEquals("fileType-" + (i+1), data.getFileType());
			assertEquals("summary-" + (i+1), data.getSummary());
			assertEquals(status[(i+1)%status.length], data.getStatus());
			assertEquals(user, data.getRegistUser());
		}
	}
	
	@Test
	public void test4SelectNgsFileQcList() throws Exception {
		for(int i=0; i < 10 ; i++) {
			assertEquals(1, dao.selectNgsFileQcResultList(achives[i]).size());
		} 
		
		assertEquals(10, dao.selectNgsFileQcResultList(regist).size());
	}
	
	@Test
	public void test5SelectNgsFileQcCount() throws Exception {
		for(int i=0; i < 10 ; i++) {
			assertEquals(1, dao.selectNgsFileQcResultCount(achives[i]));
		}
		
		assertEquals(10, dao.selectNgsFileQcResultCount(regist));
	}
	
	@Test
	public void test6DeleteNgsFileQc() throws Exception {
		for(int i=0 ; i < 5 ; i++) {
			assertEquals(1, dao.deleteNgsFileQcResult(new NgsFileQcResult(i+1)));
		}
		
		assertEquals(1, dao.deleteNgsFileQcResult(achives[5]));
		assertEquals(4, dao.deleteNgsFileQcResult(regist));
	}
}
