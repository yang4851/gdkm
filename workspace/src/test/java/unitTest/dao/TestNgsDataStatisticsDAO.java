package unitTest.dao;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.insilicogen.gdkm.Globals;
import com.insilicogen.gdkm.dao.NgsDataStatisticsDAO;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations={"classpath:/egovframework/spring/context-*.xml"})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestNgsDataStatisticsDAO {

	@Resource(name="NgsDataStatisticsDAO")
	NgsDataStatisticsDAO dao;
	
	@Test
	public void testSelectYearList() throws Exception {
		assertEquals(1, dao.selectYearList().size());
	}
	
	@Test
	public void testSelectTaxonStats() throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		assertEquals(2, dao.selectTaxonStats(params).size());
		
		params.put("dataType", Globals.REGIST_DATA_RAWDATA);
		assertEquals(2, dao.selectTaxonStats(params).size());
		
		params.put("registStatus", Globals.STATUS_REGIST_SUCCESS);
		assertEquals(1, dao.selectTaxonStats(params).size());
		
		params.put("openYn", Globals.STATUS_OPEN_Y);
		assertEquals(1, dao.selectTaxonStats(params).size());
		
		params.put("fromYear", 2017);
		assertEquals(1, dao.selectTaxonStats(params).size());
		
		params.put("toYear", 2018);
		assertEquals(1, dao.selectTaxonStats(params).size());
		
		params.put("toYear", 2017);
		assertEquals(0, dao.selectTaxonStats(params).size());
	}
	
	@Test
	public void testSelectNgsFileStats() throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		assertEquals(1, dao.selectNgsRegistStats(params).size());
		
		params.put("dataType", Globals.REGIST_DATA_RAWDATA);
		assertEquals(1, dao.selectNgsRegistStats(params).size());
		
		params.put("registStatus", Globals.STATUS_REGIST_SUCCESS);
		assertEquals(1, dao.selectNgsRegistStats(params).size());
		
		params.put("openYn", Globals.STATUS_OPEN_Y);
		assertEquals(1, dao.selectNgsRegistStats(params).size());
		
		params.put("period", "monthly");
		assertEquals(1, dao.selectNgsRegistStats(params).size());
		
		params.put("fromYear", 2017);
		assertEquals(1, dao.selectNgsRegistStats(params).size());
		
		params.put("toYear", 2018);
		assertEquals(1, dao.selectNgsRegistStats(params).size());
		
		params.put("toYear", 2017);
		assertEquals(0, dao.selectNgsRegistStats(params).size());
	}
	
	@Test
	public void testSelectTaxonCount() throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		assertEquals(2, dao.selectTaxonCount(params));
		
		params.put("dataType", Globals.REGIST_DATA_RAWDATA);
		assertEquals(2, dao.selectTaxonCount(params));
		
		params.put("registStatus", Globals.STATUS_REGIST_SUCCESS);
		assertEquals(1, dao.selectTaxonCount(params));
		
		params.put("openYn", Globals.STATUS_OPEN_Y);
		assertEquals(1, dao.selectTaxonCount(params));
		
		params.put("fromYear", 2017);
		assertEquals(1, dao.selectTaxonCount(params));
		
		params.put("toYear", 2018);
		assertEquals(1, dao.selectTaxonCount(params));
	}
	
	@Test
	public void testSelectNgsFileCount() throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		assertEquals(108, dao.selectRegistDataCount(params));
		
		params.put("dataType", Globals.REGIST_DATA_RAWDATA);
		assertEquals(54, dao.selectRegistDataCount(params));
		
		params.put("registStatus", Globals.STATUS_REGIST_SUCCESS);
		assertEquals(10, dao.selectRegistDataCount(params));
		
		params.put("openYn", Globals.STATUS_OPEN_Y);
		assertEquals(10, dao.selectRegistDataCount(params));
		
		params.put("fromYear", 2017);
		assertEquals(10, dao.selectRegistDataCount(params));
		
		params.put("toYear", 2018);
		assertEquals(10, dao.selectRegistDataCount(params));
	}
	
	@Test
	public void testSelectLastUpdated() throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		assertEquals(1527175073000L, dao.selectLastUpdated(params).getTime());
		
		params.put("dataType", Globals.REGIST_DATA_RAWDATA);
		assertEquals(1526867543000L, dao.selectLastUpdated(params).getTime());
		
		params.put("registStatus", Globals.STATUS_REGIST_SUCCESS);
		assertEquals(1526446556000L, dao.selectLastUpdated(params).getTime());
		
		params.put("openYn", Globals.STATUS_OPEN_Y);
		assertEquals(1526446556000L, dao.selectLastUpdated(params).getTime());
	}
}
