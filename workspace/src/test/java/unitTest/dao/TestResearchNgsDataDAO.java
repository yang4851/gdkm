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

import com.insilicogen.gdkm.dao.ResearchNgsDataDAO;
import com.insilicogen.gdkm.model.NgsDataRegist;
import com.insilicogen.gdkm.model.Research;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations={"classpath:/egovframework/spring/context-*.xml"})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestResearchNgsDataDAO {

	@Resource(name="ResearchNgsDataDAO")
	ResearchNgsDataDAO dao;
	
	private Research[] researches;
	
	private NgsDataRegist[] regists;
	
	@Before
	public void setup() {
		researches = new Research[3];
		researches[0] = new Research(1);
		researches[0].setRegistNo("GDKM-R-0001");
		researches[1] = new Research(2);
		researches[1].setRegistNo("GDKM-R-0002");
		researches[2] = new Research(3);
		researches[2].setRegistNo("GDKM-R-0003");
		
		regists = new NgsDataRegist[10];
		for(int i=0; i < 10 ; i++) {
			regists[i] = new NgsDataRegist(30+i);
		}
	}
	
	@Test
	public void test1InsertNgsData() throws Exception {
		for(int i=0; i < researches.length ; i++) {
			for(int j=0; j < regists.length ; j++) {
				assertEquals(1, dao.insertNgsData(researches[i], regists[j]));
			}
		}
	}
	
	@Test
	public void test2GetNgsDataList() throws Exception {
		for(int i=0; i < researches.length ; i++) {
			assertEquals(10, dao.selectNgsDataList(researches[i]).size());
		}
	}
	
	@Test
	public void test3GetNgsDataCount() throws Exception {
		for(int i=0; i < researches.length ; i++) {
			assertEquals(10, dao.selectNgsDataCount(researches[i]));
		}
	}
	
	@Test
	public void test4DeleteNgsData() throws Exception {
		for(int i=0; i < researches.length ; i++) {
			for(int j=0; j < regists.length/2 ; j++) {
				assertEquals(1, dao.deleteNgsData(researches[i], regists[j]));
			}
			
			assertEquals(regists.length/2, dao.deleteNgsData(researches[i]));
		}
	}
}
