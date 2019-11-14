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

import com.insilicogen.gdkm.dao.NgsFileSeqQuantityDAO;
import com.insilicogen.gdkm.model.NgsDataAchive;
import com.insilicogen.gdkm.model.NgsDataRegist;
import com.insilicogen.gdkm.model.NgsFileSeqQuantity;
import com.insilicogen.gdkm.model.User;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations={"classpath:/egovframework/spring/context-*.xml"})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestNgsFileSeqQuantityDAO {

	@Resource(name="NgsFileSeqQuantityDAO")
	private NgsFileSeqQuantityDAO dao;
	
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
	public void test1InsertNgsFileSeqQuantity() throws Exception {
		for(int i=0; i < 10 ; i++) {
			NgsFileSeqQuantity data = new NgsFileSeqQuantity();
			data.setAchive(achives[i]);
			data.setReadCount(i+1);
			data.setTotalLength(i+2);
			data.setAverageLength(i+3);
			data.setNumberOfA(i+4);
			data.setNumberOfT(i+5);
			data.setNumberOfG(i+6);
			data.setNumberOfC(i+7);
			data.setNumberOfN(i+8);
			data.setN50(i+9);
			data.setRegistUser(user);
			
			assertEquals(1, dao.insertNgsFileSeqQuantity(data));
		}
	}
	
	@Test
	public void test2SelectNgsFileSeqQuantity() throws Exception {
		for(int i=0; i < 10 ; i++) {
			NgsFileSeqQuantity data = dao.selectNgsFileSeqQuantity(i+1);
			assertEquals(i+1, data.getId());
			assertEquals(achives[i], data.getAchive());
			assertEquals(i+1, data.getReadCount());
			assertEquals(i+2, data.getTotalLength());
			assertEquals(i+3, (int)data.getAverageLength());
			assertEquals(i+4, data.getNumberOfA());
			assertEquals(i+5, data.getNumberOfT());
			assertEquals(i+6, data.getNumberOfG());
			assertEquals(i+7, data.getNumberOfC());
			assertEquals(i+8, data.getNumberOfN());
			assertEquals(i+9, data.getN50());
			assertEquals(user, data.getRegistUser());
		}
	}
	
	@Test
	public void test3UpdateNgsFileSeqQuantity() throws Exception {
		for(int i=0; i < 10 ; i++) {
			NgsFileSeqQuantity data = new NgsFileSeqQuantity();
			data.setId(i+1);
			data.setAchive(achives[i]);
			data.setReadCount(i+2);
			data.setTotalLength(i+3);
			data.setAverageLength(i+4);
			data.setNumberOfA(i+5);
			data.setNumberOfT(i+6);
			data.setNumberOfG(i+7);
			data.setNumberOfC(i+8);
			data.setNumberOfN(i+9);
			data.setN50(i+10);
			
			assertEquals(1, dao.updateNgsFileSeqQuantity(data));
			
			data = dao.selectNgsFileSeqQuantity(i+1);
			assertEquals(i+1, data.getId());
			assertEquals(achives[i], data.getAchive());
			assertEquals(i+2, data.getReadCount());
			assertEquals(i+3, data.getTotalLength());
			assertEquals(i+4, (int)data.getAverageLength());
			assertEquals(i+5, data.getNumberOfA());
			assertEquals(i+6, data.getNumberOfT());
			assertEquals(i+7, data.getNumberOfG());
			assertEquals(i+8, data.getNumberOfC());
			assertEquals(i+9, data.getNumberOfN());
			assertEquals(i+10, data.getN50());
			assertEquals(user, data.getRegistUser());
		}
	}
	
	@Test
	public void test4SelectNgsFileSeqQuantityList() throws Exception {
		assertEquals(10, dao.selectNgsFileSeqQuantityList(1).size());
	}
	
	@Test
	public void test5SelectNgsFileSeqQuantityCount() throws Exception {
		assertEquals(10, dao.selectNgsFileSeqQuantityCount(1));
	}
	
	@Test
	public void test6DeleteNgsFileSeqQuantity() throws Exception {
		for(int i=0 ; i < 5 ; i++) {
			assertEquals(1, dao.deleteNgsFileSeqQuantity(new NgsFileSeqQuantity(i+1)));
		}
		
		assertEquals(1, dao.deleteNgsFileSeqQuantity(achives[5]));
		assertEquals(4, dao.deleteNgsFileSeqQuantity(regist));
	}
}
