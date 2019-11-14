package unitTest.service;

import static org.junit.Assert.*;

import javax.annotation.Resource;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.insilicogen.gdkm.Globals;
import com.insilicogen.gdkm.dao.NgsDataAchiveDAO;
import com.insilicogen.gdkm.model.NgsDataAchive;
import com.insilicogen.gdkm.service.NgsFileProcessService;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations={"classpath:/egovframework/spring/context-*.xml"})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestNgsFileProcessService {
	
	@Resource(name="NgsFileProcessService")
	NgsFileProcessService service;
	
	@Resource(name="NgsDataAchiveDAO")
	NgsDataAchiveDAO dao;
	
	@Test
	public void testValidateAchiveFile() throws Exception {
		NgsDataAchive achive = dao.selectNgsDataAchive(3);
		assertNotNull(achive);
		service.validateAchiveFile(achive);
//		Thread.sleep(300000);
		
		achive = dao.selectNgsDataAchive(1);
		assertEquals(Globals.STATUS_VERIFY_SUCCESS, achive.getVerifiStatus());
		assertEquals(Globals.STATUS_REGIST_VALIDATED , achive.getRegistStatus());
	}
	
	@Test
	public void testTransaction() throws Exception {
		
	}
}
