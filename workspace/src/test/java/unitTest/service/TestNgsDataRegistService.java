package unitTest.service;

//import static org.junit.Assert.assertEquals;

//import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.annotation.Resource;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

//import com.insilicogen.gdkm.model.Code;
//import com.insilicogen.gdkm.model.NgsDataRegist;
//import com.insilicogen.gdkm.model.Taxon;
//import com.insilicogen.gdkm.model.User;
import com.insilicogen.gdkm.service.NgsDataRegistService;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations={"classpath:/egovframework/spring/context-*.xml"})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestNgsDataRegistService {
	
	@Resource(name="NgsDataRegistService")
	NgsDataRegistService ngsDataRegistService;
	
	SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat sdfDateTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	

	@Test
	public void test1InsertNgsDataRegist() throws Exception{
//		for(int i = 1; i < 11; i++){
//			NgsDataRegist ngsData = new NgsDataRegist();
//			ngsData = ngsDataInsert(ngsData, i);
//			
//			int insertCnt = ngsDataRegistService.insertNgsDataRegist(ngsData);
//			assertEquals(1, insertCnt);
//			Thread.sleep(1000);
//		}
	}
	
	@Test
	public void test2SelectNgsDataRegist() throws Exception{

//		int ngsDataId = 2;
//		NgsDataRegist ngsDataRegist = ngsDataRegistService.selectNgsDataRegist(ngsDataId);
//		
//		assertEquals(ngsDataId,ngsDataRegist.getId());
//		assertEquals("GDKM-BA-N-000" + ngsDataId,ngsDataRegist.getRegistNo());
//		assertEquals(1794524,ngsDataRegist.getTaxon().getId());
//		assertEquals((ngsDataId%2==0) ? "rawData": "annotationData", ngsDataRegist.getDataType());
//		switch(ngsDataId){
//			case  1: case 5: case 8: 
//				assertEquals("CC02124", ngsDataRegist.getStrain());
//				break;
//			
//			case  2: case 3: case 6: 
//				assertEquals("ASDF111", ngsDataRegist.getStrain());
//				break;
//				
//			case  4: case 9: 
//				assertEquals("aswef025", ngsDataRegist.getStrain());
//				break;
//				
//			case  7: case 10: 
//				assertEquals("asw1124jg", ngsDataRegist.getStrain());
//				break;
//		}
//		assertEquals("Y", ngsDataRegist.getOpenYn());
//		assertEquals("2018-02-0"+(ngsDataId+1), sdfDate.format(ngsDataRegist.getOpenDate()));
//		switch(ngsDataId%3){
//		case 0:
//			assertEquals("대기", ngsDataRegist.getApprovalStatus());
//			break;
//		case 1:
//			assertEquals("준비", ngsDataRegist.getApprovalStatus());
//			break;
//		case 2:
//			assertEquals("승인", ngsDataRegist.getApprovalStatus());
//			break;
//		default :
//			assertEquals("그외", ngsDataRegist.getApprovalStatus());
//		}
//		assertEquals("2018-02-0"+(ngsDataId+1),sdfDate.format(ngsDataRegist.getApprovalDate()));
//		
//		switch(ngsDataId%3){
//		case 0:
//			assertEquals("대기", ngsDataRegist.getRegistStatus());
//			break;
//		case 1:
//			assertEquals("등록", ngsDataRegist.getRegistStatus());
//			break;
//		case 2:
//			assertEquals("완료", ngsDataRegist.getRegistStatus());
//			break;
//		default :
//			assertEquals("그외", ngsDataRegist.getRegistStatus());
//		}
//		
//		assertEquals("testId"+ngsDataId,ngsDataRegist.getRegistUser().getUserId());
//		assertEquals("2018-02-0"+ngsDataId+" 11:20:3"+ngsDataId,sdfDateTime.format(ngsDataRegist.getRegistDate()));
//		assertEquals("testId"+ngsDataId,ngsDataRegist.getUpdateUser().getUserId());
//		assertEquals("2018-02-0"+ngsDataId+" 11:20:3"+ngsDataId,sdfDateTime.format(ngsDataRegist.getUpdateDate()));
	}
	
	@Test
	public void test3Update() throws Exception{

//		int ngsDataId = 2;
//		
//		Code code = new Code();
//		NgsDataRegist ngsData = new NgsDataRegist();
//		ngsData.setId(ngsDataId);
//		ngsData.setRegistNo("registUpdateNo" + ngsDataId);
//		Taxon taxon = new Taxon();
//		taxon.setId(1794524);
//		ngsData.setTaxon(taxon);
//		ngsData.setDataType((ngsDataId%2==0) ? "updateRawData"+ngsDataId : "updateAnnotationData" + ngsDataId);
//		switch(ngsDataId){
//			case  1: case 5: case 8: 
//				ngsData.setStrain("awefwe11");
//				break;
//			
//			case  2: case 3: case 6: 
//				ngsData.setStrain("wwwerh45");
//				break;
//				
//			case  4: case 9: 
//				ngsData.setStrain("zzz1123");
//				break;
//				
//			case  7: case 10: 
//				ngsData.setStrain("aaa234");
//				break;
//		}
//		
//		ngsData.setOpenYn((ngsDataId%2==0) ? "Y" : "N");
//		ngsData.setOpenDate(sdfDate.parse("2018-02-0"+(ngsDataId+1)));
//		switch(ngsDataId%3){
//		case 0:
//			ngsData.setApprovalStatus("update대기");
//			break;
//		case 1:
//			ngsData.setApprovalStatus("update준비");
//			break;
//		case 2:
//			ngsData.setApprovalStatus("update승인");
//			break;
//		default :
//			ngsData.setApprovalStatus("update그외");
//		}
//		ngsData.setApprovalDate(sdfDate.parse("2018-02-0"+(ngsDataId+1)));
//		switch(ngsDataId%3){
//			case 0:
//				ngsData.setRegistStatus("update대기");
//				break;
//			case 1:
//				ngsData.setRegistStatus("update등록");
//				break;
//			case 2:
//				ngsData.setRegistStatus("update완료");
//				break;
//			default :
//				ngsData.setRegistStatus("update그외");
//		}
//		User user = new User();
//		user.setUserId("testId"+ngsDataId);
//		ngsData.setRegistUser(user);
//		ngsData.setRegistDate(sdfDateTime.parse("2018-05-0"+ngsDataId+" 11:20:3"+ngsDataId));
//		ngsData.setUpdateUser(user);
//		ngsData.setUpdateDate(sdfDateTime.parse("2018-04-0"+ngsDataId+" 11:20:3"+ngsDataId));
//		
//		int updateCnt = ngsDataRegistService.updateNgsDataRegist(ngsData);
//		assertEquals(1, updateCnt);
//		
//		NgsDataRegist updateNgsData = ngsDataRegistService.selectNgsDataRegist(ngsDataId);
//		
//		assertEquals(2,updateNgsData.getId());
//		assertEquals("registUpdateNo2",updateNgsData.getRegistNo());
//		assertEquals(1794524,updateNgsData.getTaxon().getId());
//		assertEquals("updateRawData2", updateNgsData.getDataType());
//		switch(ngsDataId){
//			case  1: case 5: case 8: 
//				assertEquals("awefwe11", updateNgsData.getStrain());
//				break;
//			
//			case  2: case 3: case 6: 
//				assertEquals("wwwerh45", updateNgsData.getStrain());
//				break;
//				
//			case  4: case 9: 
//				assertEquals("zzz1123", updateNgsData.getStrain());
//				break;
//				
//			case  7: case 10: 
//				assertEquals("aaa234", updateNgsData.getStrain());
//				break;
//		}
//		
//		assertEquals("Y", updateNgsData.getOpenYn());
//		assertEquals("2018-02-0"+(ngsDataId+1), sdfDate.format(updateNgsData.getOpenDate()));
//		switch(ngsDataId%3){
//		case 0:
//			assertEquals("update대기", updateNgsData.getApprovalStatus());
//			break;
//		case 1:
//			assertEquals("update준비", updateNgsData.getApprovalStatus());
//			break;
//		case 2:
//			assertEquals("update승인", updateNgsData.getApprovalStatus());
//			break;
//		default :
//			assertEquals("update그외", updateNgsData.getApprovalStatus());
//		}
//		assertEquals("2018-02-0"+(ngsDataId+1),sdfDate.format(updateNgsData.getApprovalDate()));
//		
//		switch(ngsDataId%3){
//		case 0:
//			assertEquals("update대기", updateNgsData.getRegistStatus());
//			break;
//		case 1:
//			assertEquals("update등록", updateNgsData.getRegistStatus());
//			break;
//		case 2:
//			assertEquals("update완료", updateNgsData.getRegistStatus());
//			break;
//		default :
//			assertEquals("update그외", updateNgsData.getRegistStatus());
//		}
//		
//		assertEquals("testId"+ngsDataId,updateNgsData.getRegistUser().getUserId());
//		assertEquals("2018-05-0" +ngsDataId+ " 11:20:3"+ngsDataId,sdfDateTime.format(updateNgsData.getRegistDate()));
//		assertEquals("testId"+ngsDataId,updateNgsData.getUpdateUser().getUserId());
//		assertEquals("2018-04-0" +ngsDataId+ " 11:20:3"+ngsDataId,sdfDateTime.format(updateNgsData.getUpdateDate()));
	}
	
//	@Test
//	public void test4Delete() throws Exception{
//		String ids = "";
//		for(int i = 1; i < 11; i++){
//			ids += i+",";
//		}
//		int deleteCnt = ngsDataRegistService.deleteNgsDataRegist(ids);
//		assertEquals(10, deleteCnt);
//	}
	
	/*@Test
	public void asdf()throws Exception{
		NgsDataRegist ngsData = new NgsDataRegist();
		for(int i = 1; i<11; i++){
			ngsData = ngsDataInsert(ngsData, i);
			ngsDataService.updateNgsDataRegist(ngsData);
		}
	}*/
	
//	private NgsDataRegist ngsDataInsert(NgsDataRegist ngsData, int i) throws ParseException {
//		Code code = new Code();
//		Taxon taxon = new Taxon();
//		taxon.setId(1794524);
//		ngsData.setTaxon(taxon);
//		ngsData.setDataType((i%2==0) ? "rawData": "annotationData");
//		switch(i){
//		case  1: case 5: case 8: 
//			ngsData.setStrain("CC02124");
//			break;
//		
//		case  2: case 3: case 6: 
//			ngsData.setStrain("ASDF111");
//			break;
//			
//		case  4: case 9: 
//			ngsData.setStrain("aswef025");
//			break;
//			
//		case  7: case 10: 
//			ngsData.setStrain("asw1124jg");
//			break;
//	}
//		ngsData.setOpenYn((i%2==0) ? "Y" : "N");
//		ngsData.setOpenDate(sdfDate.parse("2018-02-0"+(i+1)));
//		switch(i%3){
//		case 0:
//			ngsData.setApprovalStatus("대기");
//			break;
//		case 1:
//			ngsData.setApprovalStatus("준비");
//			break;
//		case 2:
//			ngsData.setApprovalStatus("승인");
//			break;
//		default :
//			ngsData.setApprovalStatus("그외");
//		}
//		ngsData.setApprovalDate(sdfDate.parse("2018-02-0"+(i+1)));
//		switch(i%3){
//			case 0:
//				ngsData.setRegistStatus("대기");
//				break;
//			case 1:
//				ngsData.setRegistStatus("등록");
//				break;
//			case 2:
//				ngsData.setRegistStatus("완료");
//				break;
//			default :
//				ngsData.setRegistStatus("그외");
//		}
//		User user = new User();
//		user.setUserId("testId"+i);
//		ngsData.setApprovalUser(user);
//		ngsData.setRegistUser(user);
//		ngsData.setRegistDate(sdfDateTime.parse("2018-02-0"+i+" 11:20:3"+i));
//		ngsData.setUpdateUser(user);
//		ngsData.setUpdateDate(sdfDateTime.parse("2018-02-0"+i+" 11:20:3"+i));
//		
//		return ngsData;
//	}	
	
}
