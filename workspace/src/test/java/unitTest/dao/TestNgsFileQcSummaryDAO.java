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

import com.insilicogen.gdkm.dao.NgsFileQcSummaryDAO;
import com.insilicogen.gdkm.model.NgsDataAchive;
import com.insilicogen.gdkm.model.NgsDataRegist;
import com.insilicogen.gdkm.model.NgsFileQcSummary;
import com.insilicogen.gdkm.model.User;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations={"classpath:/egovframework/spring/context-*.xml"})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestNgsFileQcSummaryDAO {

	@Resource(name="NgsFileQcSummaryDAO")
	private NgsFileQcSummaryDAO dao;
	
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
	public void test1InsertNgsFileQcSummary() throws Exception {
		for(int i=0; i < 10 ; i++) {
			NgsFileQcSummary data = new NgsFileQcSummary();
			data.setAchive(achives[i]);
			data.setSummary("summary-" + i);
			data.setStatus(status[i%status.length]);
			data.setRegistUser(user);
			
			assertEquals(1, dao.insertNgsFileQcSummary(data));
		}
	}
	
	@Test
	public void test2SelectNgsFileQcSummary() throws Exception {
		for(int i=0; i < 10 ; i++) {
			NgsFileQcSummary data = dao.selectNgsFileQcSummary(i+1);
			assertEquals(i+1, data.getId());
			assertEquals(achives[i], data.getAchive());
			assertEquals("summary-" + i, data.getSummary());
			assertEquals(status[i%status.length], data.getStatus());
			assertEquals(user, data.getRegistUser());
		}
	}
	
	@Test
	public void test3UpdateNgsFileQcSummary() throws Exception {
		for(int i=0; i < 10 ; i++) {
			NgsFileQcSummary data = new NgsFileQcSummary();
			data.setId(i+1);
			data.setAchive(achives[i]);
			data.setSummary("summary-" + (i+1));
			data.setStatus(status[(i+1)%status.length]);
			data.setRegistUser(user);
			
			assertEquals(1, dao.updateNgsFileQcSummary(data));
			
			data = dao.selectNgsFileQcSummary(i+1);
			assertEquals(i+1, data.getId());
			assertEquals(achives[i], data.getAchive());
			assertEquals("summary-" + (i+1), data.getSummary());
			assertEquals(status[(i+1)%status.length], data.getStatus());
			assertEquals(user, data.getRegistUser());
		}
	}
	
	@Test
	public void test4SelectNgsFileQcSummaryList() throws Exception {
		assertEquals(10, dao.selectNgsFileQcSummaryList(new NgsDataRegist(1)).size());
		assertEquals(1, dao.selectNgsFileQcSummaryList(new NgsDataAchive(1)).size());
	}
	
	@Test
	public void test5SelectNgsFileQcSummaryCount() throws Exception {
		assertEquals(10, dao.selectNgsFileQcSummaryCount(new NgsDataRegist(1)));
		assertEquals(1, dao.selectNgsFileQcSummaryCount(new NgsDataAchive(1)));
	}
	
	@Test
	public void test6DeleteNgsFileQcSummary() throws Exception {
		for(int i=0 ; i < 5 ; i++) {
			assertEquals(1, dao.deleteNgsFileQcSummary(new NgsFileQcSummary(i+1)));
		}
		
		assertEquals(1, dao.deleteNgsFileQcSummary(achives[5]));
		assertEquals(4, dao.deleteNgsFileQcSummary(regist));
	}
}
