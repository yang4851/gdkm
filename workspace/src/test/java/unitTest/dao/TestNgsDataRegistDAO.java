package unitTest.dao;

import static org.junit.Assert.*;

import java.util.Date;
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

import com.insilicogen.gdkm.Globals;
import com.insilicogen.gdkm.dao.NgsDataRegistDAO;
import com.insilicogen.gdkm.model.NgsDataRegist;
import com.insilicogen.gdkm.model.Taxonomy;
import com.insilicogen.gdkm.model.User;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations={"classpath:/egovframework/spring/context-*.xml"})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestNgsDataRegistDAO {
	
	@Resource(name="NgsDataRegistDAO")
	private NgsDataRegistDAO dao;
	
	private Taxonomy[] taxonomies = new Taxonomy[4];
	
	private String[] strains = new String[] {
		"strain-01", "strain-02", "strain-03"	
	};
	
	private String[] dataTypes = new String[] {
		Globals.REGIST_DATA_RAWDATA, 
		Globals.REGIST_DATA_PROCESSEDDATA
	};

	private String[] approvalStatus = new String[] { "approve", "approve", "reject", "reject" };
	
	private String[] openYn = new String[] { "N", "N", "Y", "Y" };
	
	private String[] registStatus = new String[] { "ready", "ready", "validate", "validate", "submit", "submit", "complete", "complete" };
	
	private User user;
	
	private int loop = 100;
	
	@Before
	public void setup() {		
		taxonomies[0] = new Taxonomy();
		taxonomies[0].setTaxonId(1794524);
		taxonomies[1] = new Taxonomy();
		taxonomies[1].setTaxonId(1794525);
		taxonomies[2] = new Taxonomy();
		taxonomies[2].setTaxonId(1794526);
		taxonomies[3] = new Taxonomy();
		taxonomies[3].setTaxonId(1794527);
		
		user = new User();
		user.setUserId("administrator");
		user.setName("admin");
		user.setAuthority("admin");
	}
	
	@Test
	public void test1InsertNgsDataRegist() throws Exception {
		for(int i=0; i < loop; i++) {
			NgsDataRegist dataRegist = new NgsDataRegist();
			dataRegist.setTaxonomy(taxonomies[i%taxonomies.length]);
			dataRegist.setDataType(dataTypes[i%dataTypes.length]);
			dataRegist.setStrain(strains[i%strains.length]);
			dataRegist.setRegistUser(user);
			
			assertEquals(1, dao.insertNgsDataRegist(dataRegist));
			assertEquals(i+1, dataRegist.getId());
		}
	}
	
	@Test
	public void test2SelectNgsDataRegist() throws Exception{
		for(int i=0; i < 1 ; i++) {
			NgsDataRegist expect = dao.selectNgsDataRegist(i+1);
			assertEquals(i+1, expect.getId());
			assertNotNull(expect.getRegistNo());
			assertEquals(dataTypes[i%dataTypes.length], expect.getDataType());
			assertEquals(taxonomies[i%taxonomies.length], expect.getTaxonomy());
			assertEquals(strains[i%strains.length], expect.getStrain());
			assertEquals("N", expect.getOpenYn());
			assertNull(expect.getOpenDate());
			assertNull(expect.getApprovalStatus());
			assertNull(expect.getApprovalUser());
			assertNull(expect.getApprovalDate());
			assertEquals(user, expect.getRegistUser());
			assertEquals("ready", expect.getRegistStatus());
			assertEquals(user, expect.getRegistUser());
			assertTrue(expect.getRegistDate().getTime() <= System.currentTimeMillis());
			assertEquals(user, expect.getUpdateUser());
			assertTrue(expect.getUpdateDate().getTime() <= System.currentTimeMillis());
		}
	}
	
	@Test
	public void test3UpdateNgsDataRegist() throws Exception{
		for(int i=0; i < loop ; i++) {
			NgsDataRegist expect = new NgsDataRegist();
			expect.setId(i+1);
			expect.setTaxonomy(taxonomies[(i+1)%taxonomies.length]);
			expect.setStrain(strains[(i+1)%strains.length]);
			expect.setOpenYn(openYn[(i+1)%openYn.length]);
			expect.setOpenDate(new Date());
			expect.setApprovalStatus(approvalStatus[(i+1)%approvalStatus.length]);
			expect.setApprovalDate(new Date());
			expect.setApprovalUser(user);
			expect.setRegistStatus(registStatus[(i+1)%registStatus.length]);
			expect.setUpdateUser(user);
			
			assertEquals(1, dao.updateNgsDataRegist(expect));
			
			expect = dao.selectNgsDataRegist(i+1);
			assertEquals(i+1, expect.getId());
			assertNotNull(expect.getRegistNo());
			assertEquals(dataTypes[i%dataTypes.length], expect.getDataType());
			assertEquals(taxonomies[(i+1)%taxonomies.length], expect.getTaxonomy());
			assertEquals(strains[(i+1)%strains.length], expect.getStrain());
			assertEquals(openYn[(i+1)%openYn.length], expect.getOpenYn());
			assertTrue(expect.getOpenDate().getTime() < System.currentTimeMillis());
			assertEquals(approvalStatus[(i+1)%approvalStatus.length], expect.getApprovalStatus());
			assertEquals(user, expect.getApprovalUser());
			assertTrue(expect.getApprovalDate().getTime() < System.currentTimeMillis());
			assertEquals(user, expect.getRegistUser());
			assertEquals(registStatus[(i+1)%registStatus.length], expect.getRegistStatus());
			assertEquals(user, expect.getRegistUser());
			assertTrue(expect.getRegistDate().getTime() <= System.currentTimeMillis());
			assertEquals(user, expect.getUpdateUser());
			assertTrue(expect.getUpdateDate().getTime() <= System.currentTimeMillis());
		}
	}
	
	@Test
	public void test4SelectNgsDataRegistCount() throws Exception{
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("dataType", Globals.REGIST_DATA_RAWDATA);
//		assertEquals(44, dao.selectNgsDataRegistCount(params));
		assertEquals(4, dao.selectNgsDataRegistCount(params));
		
		params.put("taxonId", taxonomies[0].getTaxonId());
//		assertEquals(23, dao.selectNgsDataRegistCount(params));
		assertEquals(2, dao.selectNgsDataRegistCount(params));
		params.remove("taxonId");
		
		params.put("openYn", "N");
//		assertEquals(44, dao.selectNgsDataRegistCount(params));
		assertEquals(3, dao.selectNgsDataRegistCount(params));
		params.remove("openYn");
		
		params.put("approvalStatus", approvalStatus[0]);
//		assertEquals(0, dao.selectNgsDataRegistCount(params));
		assertEquals(1, dao.selectNgsDataRegistCount(params));
		params.remove("approvalStatus");
		
		params.put("registStatus", registStatus[0]);
//		assertEquals(43, dao.selectNgsDataRegistCount(params));
		assertEquals(2, dao.selectNgsDataRegistCount(params));
		params.remove("registStatus");
		
		// RawData 일치검색 테스트 
		params.put("match", "exact");
//		params.put("strain", strains[0]);
//		assertEquals(16, dao.selectNgsDataRegistCount(params));
		params.put("strain", strains[1]);
		assertEquals(1, dao.selectNgsDataRegistCount(params));
		params.remove("strain");
		
		params.put("species", "Lactobacillus acidophilus NCFM");
		assertEquals(0, dao.selectNgsDataRegistCount(params));
		params.remove("species");
		
		params.put("keyword", "GDKM-BA-N-0001");
		assertEquals(1, dao.selectNgsDataRegistCount(params));
		params.remove("keyword");
		
		// RawData 유사검색 테스트
		params.remove("keyword");
		params.put("match", "partial");
		params.put("strain", "strain-");
//		assertEquals(44, dao.selectNgsDataRegistCount(params));
		assertEquals(1, dao.selectNgsDataRegistCount(params));
		params.remove("strain");
		
		params.put("species", "Lactobacillus acidophilus");
		assertEquals(0, dao.selectNgsDataRegistCount(params));
		params.remove("species");
		
		params.put("keyword", "GDKM-BA-N");
//		assertEquals(44, dao.selectNgsDataRegistCount(params));
		assertEquals(4, dao.selectNgsDataRegistCount(params));
		params.remove("keyword");
		
		// ProcessedData 일치검색 테스트
		params.put("dataType", Globals.REGIST_DATA_PROCESSEDDATA);
		params.put("match", "exact");
		params.put("strain", strains[0]);
//		assertEquals(16, dao.selectNgsDataRegistCount(params));
		assertEquals(1, dao.selectNgsDataRegistCount(params));
		params.remove("strain");
		
		params.put("species", "Lactobacillus acidophilus NCFM");
		assertEquals(0, dao.selectNgsDataRegistCount(params));
		params.remove("species");
		
		params.put("keyword", "GDKM-BA-A-0001");
		assertEquals(1, dao.selectNgsDataRegistCount(params));
		params.remove("keyword");
		
		// ProcessedData 유사검색 테스트
		params.put("match", "partial");
		params.put("strain", "strain-");
//		assertEquals(48, dao.selectNgsDataRegistCount(params));
		assertEquals(2, dao.selectNgsDataRegistCount(params));
		params.remove("strain");
		
		params.put("species", "Lactobacillus acidophilus");
		assertEquals(0, dao.selectNgsDataRegistCount(params));
		params.remove("species");
		
		params.put("keyword", "GDKM-BA-A-00");
//		assertEquals(48, dao.selectNgsDataRegistCount(params));
		assertEquals(3, dao.selectNgsDataRegistCount(params));
		
		params.put("firstIndex", 10);
//		assertEquals(48, dao.selectNgsDataRegistList(params).size());
		assertEquals(3, dao.selectNgsDataRegistList(params).size());
		
		params.put("rowSize", 10);
//		assertEquals(10, dao.selectNgsDataRegistList(params).size());
		assertEquals(0, dao.selectNgsDataRegistList(params).size());
	}
	
//	@Test
	public void test5DeleteNgsDataRegist() throws Exception{		
//		for(int i=0 ; i < loop/2 ; i++){
//			assertEquals(1, dao.deleteNgsDataRegist(new int[]{ i+1 }));
//		}
//		
//		int[] ids = IntStream.rangeClosed(100/2, 100).toArray();
//		assertEquals(loop/2, dao.deleteNgsDataRegist(ids));
	}
}
