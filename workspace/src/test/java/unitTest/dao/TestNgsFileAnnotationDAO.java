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

import com.insilicogen.gdkm.dao.NgsFileAnnotationDAO;
import com.insilicogen.gdkm.model.NgsDataAchive;
import com.insilicogen.gdkm.model.NgsDataRegist;
import com.insilicogen.gdkm.model.NgsFileAnnotation;
import com.insilicogen.gdkm.model.User;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations={"classpath:/egovframework/spring/context-*.xml"})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestNgsFileAnnotationDAO {

	@Resource(name="NgsFileAnnotationDAO")
	private NgsFileAnnotationDAO dao;
	
	private User user;
	
	private NgsDataRegist regist;
	
	private NgsDataAchive[] achives = new NgsDataAchive[10];
	
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
	public void test1InsertNgsFileAnnotation() throws Exception {
		for(int i=0; i < 10 ; i++) {
			NgsFileAnnotation data = new NgsFileAnnotation();
			data.setAchive(achives[i]);
			data.setGeneCount(i+1);
			data.setCdsCount(i+2);
			data.setRegistUser(user);
			
			assertEquals(1, dao.insertNgsFileAnnotation(data));
		}
	}
	
	@Test
	public void test2SelectNgsFileAnnotation() throws Exception {
		for(int i=0; i < 10 ; i++) {
			NgsFileAnnotation data = dao.selectNgsFileAnnotation(i+1);
			assertEquals(i+1, data.getId());
			assertEquals(achives[i], data.getAchive());
			assertEquals(i+1, data.getGeneCount());
			assertEquals(i+2, data.getCdsCount());
			assertEquals(user, data.getRegistUser());
		}
	}
	
	@Test
	public void test3UpdateNgsFileAnnotation() throws Exception {
		for(int i=0; i < 10 ; i++) {
			NgsFileAnnotation data = new NgsFileAnnotation();
			data.setId(i+1);
			data.setAchive(achives[i]);
			data.setGeneCount(i+2);
			data.setCdsCount(i+3);
			
			assertEquals(1, dao.updateNgsFileAnnotation(data));
			
			data = dao.selectNgsFileAnnotation(i+1);
			assertEquals(i+1, data.getId());
			assertEquals(achives[i], data.getAchive());
			assertEquals(i+2, data.getGeneCount());
			assertEquals(i+3, data.getCdsCount());
			assertEquals(user, data.getRegistUser());
		}
	}
	
	@Test
	public void test4SelectNgsFileAnnotationList() throws Exception {
		assertEquals(10, dao.selectNgsFileAnnotationList(1).size());
	}
	
	@Test
	public void test5SelectNgsFileAnnotationCount() throws Exception {
		assertEquals(10, dao.selectNgsFileAnnotationCount(1));
	}
	
	@Test
	public void test6DeleteNgsFileAnnotation() throws Exception {
		for(int i=0 ; i < 5 ; i++) {
			assertEquals(1, dao.deleteNgsFileAnnotation(new NgsFileAnnotation(i+1)));
		}
		
		assertEquals(1, dao.deleteNgsFileAnnotation(achives[5]));
		assertEquals(4, dao.deleteNgsFileAnnotation(regist));
	}
}
