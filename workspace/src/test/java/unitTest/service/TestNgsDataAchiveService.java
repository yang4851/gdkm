package unitTest.service;

//import static org.junit.Assert.assertEquals;

import java.text.SimpleDateFormat;
//import java.util.List;

import javax.annotation.Resource;

import org.junit.FixMethodOrder;
//import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

//import com.insilicogen.gdkm.dao.NgsDataAchiveDAO;
//import com.insilicogen.gdkm.model.Code;
//import com.insilicogen.gdkm.model.NgsDataAchive;
//import com.insilicogen.gdkm.model.User;
import com.insilicogen.gdkm.service.NgsDataAchiveService;
//import com.insilicogen.gdkm.util.EgovStringUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations={"classpath:/egovframework/spring/context-*.xml"})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestNgsDataAchiveService {
	
	@Resource(name="NgsDataAchiveService")
	NgsDataAchiveService ngsDataAchiveService;
	
	SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat sdfDateTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	
//	@Test
//	public void test1InsertNgsDataAchive() throws Exception{
//		Code code = new Code();
//		User user = new User();
//		for(int i = 1; i < 11; i++){
//			NgsDataAchive ngsDataAchive = new NgsDataAchive();
//			
////			ngsDataAchive.setRegistId(i);
//			ngsDataAchive.setRegistNo("registNo" + i);
//			ngsDataAchive.setVerifiStatus((i%2==0)?"양호":"에러");
//			ngsDataAchive.setVerifiError((i%2==0)?"에러":"중지");
//			ngsDataAchive.setFileType(".jpg");
//			ngsDataAchive.setFileName("파일명" + i);
//			ngsDataAchive.setFileSize(234235);
//			ngsDataAchive.setChecksum("1011 0001 1110 0110");
//			ngsDataAchive.setRegistStatus("준비");
////			user.setId("encryptId"+i);
//			ngsDataAchive.setRegistUser(user);
//			ngsDataAchive.setRegistDate(sdfDateTime.parse("2019-03-02 11:11:11"));
//			ngsDataAchive.setUpdateUser(user);
//			ngsDataAchive.setUpdateDate(sdfDateTime.parse("2019-03-02 11:11:11"));
//			
////			int insertCnt = ngsDataAchiveService.insertNgsDataAchive(ngsDataAchive);
////			assertEquals(1, insertCnt);
//		}
//	}
//	
//	@Test
//	public void test2getNgsDataAchive() throws Exception{
//		int ngsDataAchiveId = 3;
//		NgsDataAchive ngsDataAchive = ngsDataAchiveService.selectNgsDataAchive(ngsDataAchiveId);
//		assertEquals(ngsDataAchiveId,ngsDataAchive.getId());
////		assertEquals(ngsDataAchiveId,ngsDataAchive.getRegistId());
//		assertEquals("registNo" + ngsDataAchiveId,ngsDataAchive.getRegistNo());
//		assertEquals((ngsDataAchiveId%2==0)?"양호":"에러",ngsDataAchive.getVerifiStatus());
//		assertEquals((ngsDataAchiveId%2==0)?"에러":"중지",ngsDataAchive.getVerifiError());
//		assertEquals(".jpg",ngsDataAchive.getFileType());
//		assertEquals("파일명" + ngsDataAchiveId,ngsDataAchive.getFileName());
//		assertEquals(234235,ngsDataAchive.getFileSize());
//		assertEquals("1011 0001 1110 0110",ngsDataAchive.getChecksum());
//		assertEquals("준비",ngsDataAchive.getRegistStatus());
////		assertEquals("encryptId" + ngsDataAchiveId,ngsDataAchive.getRegistUser().getId());
//		assertEquals(sdfDateTime.parse("2019-03-02 11:11:11"),ngsDataAchive.getRegistDate());
////		assertEquals("encryptId" + ngsDataAchiveId,ngsDataAchive.getUpdateUser().getId());
//		assertEquals(sdfDateTime.parse("2019-03-02 11:11:11"),ngsDataAchive.getUpdateDate());
//	}
//	
//	@Test
//	public void test3SelectNgsDataAchiveList() throws Exception{
//		//TEST1-1(partial)
//		int firstIndex = 0;
//		int rowSize = 5;
//		String registId = "";
//		String dataType = "";
//		String dataTypeKeyword= "";
//		String match= "partial";
//		String strainKeyword= "ASW";
//		List<NgsDataAchive> ngsDataAchiveList = ngsDataAchiveService.selectNgsDataAchiveList(registId, dataType, dataTypeKeyword, match, strainKeyword, firstIndex, rowSize);
//		String arrayStr ="";
//		for(int i = 0; i<ngsDataAchiveList.size(); i++){
//			arrayStr += ngsDataAchiveList.get(i).getId();
//			if(i != ngsDataAchiveList.size()-1){
//				arrayStr += ",";
//			}
//		}
//		assertEquals("10,9,7,4", arrayStr);
//		//TEST1-2(eaxact)
//		match= "exact";
//		strainKeyword= "ASW";
//		ngsDataAchiveList = ngsDataAchiveService.selectNgsDataAchiveList(registId, dataType, dataTypeKeyword, match, strainKeyword, firstIndex, rowSize);
//		arrayStr ="";
//		for(int i = 0; i<ngsDataAchiveList.size(); i++){
//			arrayStr += ngsDataAchiveList.get(i).getId();
//			if(i != ngsDataAchiveList.size()-1){
//				arrayStr += ",";
//			}
//		}
//		assertEquals("", arrayStr);
//		//TEST 2-1(partial)
//		firstIndex = 0;
//		rowSize = 5;
//		registId = "";
//		dataType = "";
//		dataTypeKeyword= "";
//		match= "partial";
//		strainKeyword= "1";
//		ngsDataAchiveList = ngsDataAchiveService.selectNgsDataAchiveList(registId, dataType, dataTypeKeyword, match, strainKeyword, firstIndex, rowSize);
//		arrayStr ="";
//		for(int i = 0; i<ngsDataAchiveList.size(); i++){
//			arrayStr += ngsDataAchiveList.get(i).getId();
//			if(i != ngsDataAchiveList.size()-1){
//				arrayStr += ",";
//			}
//		}
//		assertEquals("10,8,7,6,5", arrayStr);
//		//TEST 2-2(partial-rowSize)
//		rowSize = 10;
//		ngsDataAchiveList = ngsDataAchiveService.selectNgsDataAchiveList(registId, dataType, dataTypeKeyword, match, strainKeyword, firstIndex, rowSize);
//		arrayStr ="";
//		for(int i = 0; i<ngsDataAchiveList.size(); i++){
//			arrayStr += ngsDataAchiveList.get(i).getId();
//			if(i != ngsDataAchiveList.size()-1){
//				arrayStr += ",";
//			}
//		}
//		assertEquals("10,8,7,6,5,3,2,1", arrayStr);
//		//TEST 3-1(partial-dataType)
//		firstIndex = 0;
//		rowSize = 10;
//		registId = "";
//		dataType = "annotationData";
//		dataTypeKeyword= "";
//		match= "partial";
//		strainKeyword= "1";
//		ngsDataAchiveList = ngsDataAchiveService.selectNgsDataAchiveList(registId, dataType, dataTypeKeyword, match, strainKeyword, firstIndex, rowSize);
//		arrayStr ="";
//		for(int i = 0; i<ngsDataAchiveList.size(); i++){
//			arrayStr += ngsDataAchiveList.get(i).getId();
//			if(i != ngsDataAchiveList.size()-1){
//				arrayStr += ",";
//			}
//		}
//		assertEquals("7,5,3,1", arrayStr);
//	}
//	
//	@Test
//	public void test4DeleteNgsDataAchive() throws Exception{
//		int[] ids = new int[11];
//		
//		for(int i = 1; i < 11; i++){
//			ids[i] = i;
//		}
//		ngsDataAchiveService.deleteNgsDataAchive(ids);
//	}
}
