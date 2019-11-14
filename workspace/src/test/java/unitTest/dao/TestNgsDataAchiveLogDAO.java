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

import com.insilicogen.gdkm.dao.NgsDataAchiveLogDAO;
import com.insilicogen.gdkm.model.NgsDataAchive;
import com.insilicogen.gdkm.model.NgsDataAchiveLog;
import com.insilicogen.gdkm.model.User;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations={"classpath:/egovframework/spring/context-*.xml"})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestNgsDataAchiveLogDAO {

	@Resource(name="NgsDataAchiveLogDAO")
	private NgsDataAchiveLogDAO dao;
	
	private int[] achiveIds = new int[] { 1, 2 };
	
	private String[] types = new String[] { "FASTQC", "CLC", "UNZIP" };
	
	private String[] status = new String[] { "ready", "runninig", "complete", "error" };
	
	private User user;
	
	@Before
	public void setup() {
		user = new User();
		user.setUserId("administrator");
		user.setName("admin");
		user.setAuthority("admin");
	}
	
	@Test
	public void test1InsertNgsDataAchiveLog() throws Exception {
		for(int i=0; i < 100 ; i++) {
			NgsDataAchiveLog log = new NgsDataAchiveLog();
			log.setAchiveId(achiveIds[i%achiveIds.length]);
			log.setProcessType(types[i%types.length]);
			log.setProcessStatus(status[i%status.length]);
			log.setProcessLog("processLog-" + i);
			log.setRegistUser(user);
			
			assertEquals(1, dao.insertNgsDataAchiveLog(log));
		}
	}
	
	@Test
	public void test2SelectNgsDataAchiveLog() throws Exception {
		for(int i=0; i < 100 ; i++) {
			NgsDataAchiveLog log = dao.selectNgsDataAchiveLog(i+1);
			assertEquals(i+1, log.getId());
			assertEquals(achiveIds[i%achiveIds.length], log.getAchiveId());
			assertEquals(types[i%types.length], log.getProcessType());
			assertEquals(status[i%status.length], log.getProcessStatus());
			assertEquals("processLog-" + i, log.getProcessLog());
			assertEquals(user, log.getRegistUser());
		}
	}
	
	@Test
	public void test3SelectNgsDataAchiveLogList() throws Exception {
		assertEquals(50, dao.selectNgsDataAchiveLogList(new NgsDataAchive(1)).size());
		assertEquals(50, dao.selectNgsDataAchiveLogList(new NgsDataAchive(2)).size());
	}
	
	@Test
	public void test4SelectNgsDataAchiveLogCount() throws Exception {
		assertEquals(50, dao.selectNgsDataAchiveLogCount(new NgsDataAchive(1)));
		assertEquals(50, dao.selectNgsDataAchiveLogCount(new NgsDataAchive(2)));
	}
	
	@Test
	public void test5DeleteNgsDataAchiveLog() throws Exception {
		assertEquals(1, dao.deleteNgsDataAchiveLog(new NgsDataAchiveLog(1)));
		assertEquals(49, dao.deleteNgsDataAchiveLog(new NgsDataAchive(1)));
		assertEquals(50, dao.deleteNgsDataAchiveLog(new NgsDataAchive(2)));
	}
}

