package unitTest.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.List;
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
import com.insilicogen.gdkm.dao.NgsDataAchiveDAO;
import com.insilicogen.gdkm.model.NgsDataAchive;
import com.insilicogen.gdkm.model.NgsDataRegist;
import com.insilicogen.gdkm.model.User;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations={"classpath:/egovframework/spring/context-*.xml"})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestNgsDataAchiveDAO {
	
	@Resource(name="NgsDataAchiveDAO")
	private NgsDataAchiveDAO dao;
	
	private String[] verifiStatus = new String[] { "ready", "success", "fail" };
	
	private String[] achiveStatus = new String[] { "ready", "validate", "submit", "complete" };
	
	private String[] types = new String[] { "fasta", "fastq", "gbk", "gff", "other" };
	
	private NgsDataRegist[] registList = new NgsDataRegist[100];
	
	private User user;
	
	@Before
	public void setup() {
		for(int i=0; i < 100 ; i++) {
			int no = ((int)Math.ceil(i/2)) + 1;
			String registNo = ((i%2 == 0) ? "GDKM-BA-N-" : "GDKM-BA-A-") + String.format("%04d", no);
			
			registList[i] = new NgsDataRegist();
			registList[i].setId(i+1);
			registList[i].setRegistNo(registNo);
		}
		
		user = new User();
		user.setUserId("administrator");
		user.setName("admin");
		user.setAuthority("admin");
	}
	
//	@Test
	public void test1InsertNgsDataAchive() throws Exception{
		for(int i = 0; i < 10 ; i++){
			for(int j = 0 ; j < 10 ; j++) {
				NgsDataAchive data = new NgsDataAchive();
				data.setDataRegist(registList[i]);
				data.setFileName("fileName-" + j);
				data.setFileType(types[j%types.length]);
				data.setFileSize(i*j);
				data.setChecksum("checksum-" + j);
				data.setVerifiStatus(verifiStatus[j%verifiStatus.length]);
				data.setVerifiError("verifiError-" + j);
				data.setRegistStatus(achiveStatus[j%achiveStatus.length]);
				data.setRegistUser(user);
				data.setUpdateUser(user);
				
				assertEquals(1, dao.insertNgsDataAchive(data));
			}
		}
	}
	
//	@Test
	public void test2getNgsDataAchive() throws Exception{
		for(int i = 0; i < 10 ; i++){
			for(int j = 0 ; j < 10 ; j++) {
				int expectId = (i*10)+j+1;
				String expectNo = registList[i].getRegistNo() + "-" + String.format("%03d", j+1);
				
				NgsDataAchive data = dao.selectNgsDataAchive(expectId);
				assertEquals(expectId, data.getId());
				assertEquals(expectNo, data.getRegistNo());
				assertEquals("fileName-"+j, data.getFileName());
				assertEquals(types[j%types.length], data.getFileType());
				assertEquals((long)i*j, data.getFileSize());
				assertEquals("checksum-" + j, data.getChecksum());
				assertEquals(verifiStatus[j%verifiStatus.length], data.getVerifiStatus());
				assertEquals("verifiError-" + j, data.getVerifiError());
				assertEquals(achiveStatus[j%achiveStatus.length], data.getRegistStatus());
				assertEquals(user, data.getRegistUser());
				assertTrue(data.getRegistDate().getTime() <= System.currentTimeMillis());
				assertEquals(user, data.getUpdateUser());
				assertTrue(data.getUpdateDate().getTime() <= System.currentTimeMillis());
			}
		}
	}
	
//	@Test
	public void test3UpdateNgsDataAchive() throws Exception{
		for(int i = 0; i < 10 ; i++){
			for(int j = 0 ; j < 10 ; j++) {
				int expectId = (i*10)+j+1;
				String expectNo = registList[i].getRegistNo() + "-" + String.format("%03d", j+1);
				
				NgsDataAchive data = new NgsDataAchive();
				data.setId(expectId);
				data.setDataRegist(registList[i]);
				data.setFileName("fileName-" + (j+1));
				data.setFileType(types[(j+1)%types.length]);
				data.setFileSize(i*(j+1));
				data.setChecksum("checksum-" + (j+1));
				data.setVerifiStatus(verifiStatus[(j+1)%verifiStatus.length]);
				data.setVerifiError("verifiError-" + (j+1));
				data.setRegistStatus(achiveStatus[(j+1)%achiveStatus.length]);
				data.setRegistUser(user);
				data.setUpdateUser(user);
				
				assertEquals(1, dao.updateNgsDataAchive(data));
				
				data = dao.selectNgsDataAchive(expectId);
				assertEquals(expectId, data.getId());
				assertEquals(expectNo, data.getRegistNo());
				assertEquals("fileName-"+(j+1), data.getFileName());
				assertEquals(types[(j+1)%types.length], data.getFileType());
				assertEquals((long)i*(j+1), data.getFileSize());
				assertEquals("checksum-" + (j+1), data.getChecksum());
				assertEquals(verifiStatus[(j+1)%verifiStatus.length], data.getVerifiStatus());
				assertEquals("verifiError-" + (j+1), data.getVerifiError());
				assertEquals(achiveStatus[(j+1)%achiveStatus.length], data.getRegistStatus());
				assertEquals(user, data.getRegistUser());
				assertTrue(data.getRegistDate().getTime() <= System.currentTimeMillis());
				assertEquals(user, data.getUpdateUser());
				assertTrue(data.getUpdateDate().getTime() <= System.currentTimeMillis());
			}
		}
	}

//	@Test
	public void test4SelectNgsDataAchive() throws Exception{
		Map<String, Object> params = new HashMap<String, Object>();
		assertEquals(100, dao.selectNgsDataAchiveList(params).size());
		
		params.put("registId", 1);
		assertEquals(10, dao.selectNgsDataAchiveList(params).size());
		params.remove("registId");
		
		params.put("dataType", Globals.REGIST_DATA_RAWDATA);
		assertEquals(50, dao.selectNgsDataAchiveList(params).size());
		
		params.put("taxonId", 228064);
		assertEquals(30, dao.selectNgsDataAchiveList(params).size());
		params.remove("taxonId");
		
		params.put("openYn", "Y");
		assertEquals(20, dao.selectNgsDataAchiveList(params).size());
		
		params.put("openYn", "N");
		assertEquals(30, dao.selectNgsDataAchiveList(params).size());
		params.remove("openYn");
		
		params.put("achiveStatus", achiveStatus[0]);
		assertEquals(10, dao.selectNgsDataAchiveList(params).size());
		params.remove("achiveStatus");
		
		params.put("registUser", user);
		assertEquals(50, dao.selectNgsDataAchiveList(params).size());
		params.remove("registUser");
		
		params.put("registUserId", user.getUserId());
		assertEquals(50, dao.selectNgsDataAchiveList(params).size());		
		params.remove("registUserId");
		
		params.put("fileType", "fasta");
		assertEquals(10, dao.selectNgsDataAchiveList(params).size());		
		params.remove("fileType");
		
		params.put("match", "exact");
		params.put("species", "Lactobacillus acidophilus NCFM");
		assertEquals(30, dao.selectNgsDataAchiveList(params).size());		
		params.remove("species");
		
		params.put("strain", "strain-01");
		assertEquals(20, dao.selectNgsDataAchiveList(params).size());		
		params.remove("strain");
		
		params.put("keyword", "fileName-1");
		assertEquals(5, dao.selectNgsDataAchiveList(params).size());		
		params.remove("keyword");
		
		params.put("keyword", "GDKM-BA-N-0001-001");
		assertEquals(1, dao.selectNgsDataAchiveList(params).size());		
		params.remove("keyword");
		
		params.put("match", "containes");
		params.put("dataType", Globals.REGIST_DATA_PROCESSEDDATA);
		params.put("species", "Lactobacillus acidophilus");
		assertEquals(50, dao.selectNgsDataAchiveList(params).size());		
		params.remove("species");
		
		params.put("strain", "strain");
		assertEquals(50, dao.selectNgsDataAchiveList(params).size());		
		params.remove("strain");
		
		params.put("keyword", "fileName");
		assertEquals(50, dao.selectNgsDataAchiveList(params).size());		
		params.remove("keyword");
		
		params.put("keyword", "GDKM-BA-A-0001");
		assertEquals(10, dao.selectNgsDataAchiveList(params).size());		
		params.remove("keyword");
	}
	
//	@Test
	public void test5DeleteNgsDataAchive() throws Exception{
		for(int i=0 ; i < 50 ; i++){
			assertEquals(1, dao.deleteNgsDataAchive(i));
		}
		
		/*int[] ids = IntStream.rangeClosed(50, 100).toArray();
		assertEquals(50, dao.deleteNgsDataAchive(i));*/
	}
}