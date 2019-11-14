package unitTest.service;

import static org.junit.Assert.assertEquals;

import java.text.SimpleDateFormat;
import java.util.List;

import javax.annotation.Resource;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.insilicogen.gdkm.model.Code;
import com.insilicogen.gdkm.model.NgsDataRegist;
import com.insilicogen.gdkm.service.NgsDataRegistService;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations={"classpath:/egovframework/spring/context-*.xml"})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestNgsRawDataService {
	
	@Resource(name="NgsDataRegistService")
	NgsDataRegistService ngsDataRegistService;
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
//	@Test
//	public void test1InsertNgsDataRegist() throws Exception{
//		Code code = new Code();
//		for(int i = 1; i < 11; i++){
//			NgsDataRegist rawData = new NgsDataRegist();
//			rawData = rawDataInsert(rawData, i);
//			
//			int insertCnt = ngsDataRegistService.getNgsDataRegist(rawData);
//			assertEquals(1, insertCnt);
//			Thread.sleep(1000);
//		}
//	}
//	
//	@Test
//	public void test2SelectNgsDataRegist() throws Exception{

//		int rawDataId = 3;
//		NgsDataRegist ngsRawData = ngsDataRegistService.selectNgsDataRegist(rawDataId);
//		
//		//T_NGS_RAW_DATA
//		assertEquals(rawDataId, ngsRawData.getRegistId());
//		assertEquals("code" + rawDataId, ngsRawData.getExprType().getCode());
//		assertEquals("code" + rawDataId, ngsRawData.getDomain().getCode());
//		assertEquals("Kimchi microbes strain - UCC11" + rawDataId, ngsRawData.getCultureCondition());
//		assertEquals("Control of kimchi additive reaction" + rawDataId, ngsRawData.getTreatment());
//		assertEquals("code3", ngsRawData.getReplicate().getCode());
//		assertEquals("Lactobacillus salivarius UCC11" + rawDataId, ngsRawData.getSampleName());
//		assertEquals("prokaryote", ngsRawData.getOrganType());
//		assertEquals("solar salt", ngsRawData.getSampleType());
//		assertEquals("South Korea", ngsRawData.getGeographLocation());
//		assertEquals("code3", ngsRawData.getSampleSource().getCode());
//		assertEquals("이곳은 샘플에대한 코맨트를 남겨주세요 " + rawDataId, ngsRawData.getSampleComment());
//		assertEquals("code3", ngsRawData.getSeqPlatform().getCode());
//		assertEquals("RANDOM", ngsRawData.getConstruct());
//		assertEquals("illumina Hiseq", ngsRawData.getSelection());
//		assertEquals("TruSeq", ngsRawData.getSequencer());
//		assertEquals("WGS", ngsRawData.getStrategy());
//		assertEquals(150, ngsRawData.getReadLength());
//		assertEquals(250, ngsRawData.getInsertSize());
//		assertEquals("code3", ngsRawData.getReadType().getCode());
//		assertEquals("5`-GTTCAGAGTTCACAGTCCGACGATC  / 3`-TGGAATTCTCGGGTGCCAAGG", ngsRawData.getAdapterSeq());
//		assertEquals("illumina 1.5+Phred+64", ngsRawData.getQualityScoreVersion());
//		assertEquals("Phred", ngsRawData.getBaseCallingProgram());
//		//T_NGS_DATA_REGIST
//		assertEquals((ngsRawData.getDataType() == "rawData") ? "GDKM-BA-N-000" + rawDataId : "GDKM-BA-A-000" + rawDataId, ngsRawData.getRegistNo());
//		assertEquals("Bacteria", ngsRawData.getTaxon().getTaxonName().getName());
//		assertEquals((rawDataId%2==0) ? "rawData": "annotationData", ngsRawData.getDataType());
//		assertEquals("N", ngsRawData.getOpenYn());
//		assertEquals(java.util.Date.class, ngsRawData.getOpenDate().getClass());
//		assertEquals("대기", ngsRawData.getApprovalStatus());
//		assertEquals(java.util.Date.class, ngsRawData.getApprovalDate().getClass());
//		assertEquals("대기", ngsRawData.getRegistStatus());
//		assertEquals("testId" + rawDataId, ngsRawData.getRegistUser().getUserId());
//		assertEquals("이름" + rawDataId, ngsRawData.getRegistUser().getName());
//		assertEquals(java.util.Date.class, ngsRawData.getRegistDate().getClass());
//		assertEquals("testId" + rawDataId, ngsRawData.getUpdateUser().getUserId());
//		assertEquals("이름" + rawDataId, ngsRawData.getUpdateUser().getName());
//		assertEquals(java.util.Date.class, ngsRawData.getUpdateDate().getClass());
//		//T_NGS_DATA_ACHIVE
//		assertEquals(0, ngsRawData.getNgsDataAchiveList().size());
//	}
//	
//	@Test
//	public void test3SelectList() throws Exception{
//		String openStatus = "Y";
//		String registStatus = "";
//		String registDateStart = "20180201";
//		String registTo = "20180208";
//		String acceptStatus = "";
//		String acceptDateStart = "20180201";
//		String acceptDateEnd = "20180208";
//		String column = "sampleSource";
//		String query = "code";
//		int page = 0;
//		int rowSize = 10;
//		
//		List<NgsDataRegist> rawDataList = ngsDataRegistService.selectNgsDataRegistList(openStatus,
//										registStatus, registDateStart, registTo,
//										acceptStatus, acceptDateStart, acceptDateEnd,
//										column, query, page, rowSize);
//		assertEquals(3, rawDataList.size());
//		
//		openStatus = "Y";
//		registStatus = "";
//		registDateStart = "20180203";
//		registTo = "20180209";
//		acceptStatus = "";
//		acceptDateStart = "";
//		acceptDateEnd = "";
//		column = "";
//		query = "";
//		page = 0;
//		rowSize = 10;
//		rawDataList = ngsDataRegistService.selectNgsDataRegistList(openStatus,
//				registStatus, registDateStart, registTo,
//				acceptStatus, acceptDateStart, acceptDateEnd,
//				column, query, page, rowSize);
//		assertEquals(3, rawDataList.size());
//		
//		openStatus = "N";
//		registStatus = "";
//		registDateStart = "20180203";
//		registTo = "20180207";
//		acceptStatus = "";
//		acceptDateStart = "";
//		acceptDateEnd = "";
//		page = 0;
//		rowSize = 10;
//		rawDataList = ngsDataRegistService.selectNgsDataRegistList(openStatus,
//				registStatus, registDateStart, registTo,
//				acceptStatus, acceptDateStart, acceptDateEnd,
//				column, query, page, rowSize);
//		assertEquals(2, rawDataList.size());
//		openStatus = "";
//		column = "";
//		query = "";
//		rawDataList = ngsDataRegistService.selectNgsDataRegistList(openStatus,
//				registStatus, registDateStart, registTo,
//				acceptStatus, acceptDateStart, acceptDateEnd,
//				column, query, page, rowSize);
//		assertEquals(4, rawDataList.size());
//		openStatus = "Y";
//		rawDataList = ngsDataRegistService.selectNgsDataRegistList(openStatus,
//				registStatus, registDateStart, registTo,
//				acceptStatus, acceptDateStart, acceptDateEnd,
//				column, query, page, rowSize);
//		assertEquals(2, rawDataList.size());
//		
//		openStatus = "N";
//		registStatus = "";
//		registDateStart = "20180203";
//		registTo = "20180205";
//		acceptStatus = "";
//		acceptDateStart = "";
//		acceptDateEnd = "";
//		column = "코드";
//		query = "source";
//		page = 0;
//		rowSize = 10;
//		rawDataList = ngsDataRegistService.selectNgsDataRegistList(openStatus,
//				registStatus, registDateStart, registTo,
//				acceptStatus, acceptDateStart, acceptDateEnd,
//				column, query, page, rowSize);
//		assertEquals(1, rawDataList.size());
//		
//		openStatus = "";
//		registStatus = "";
//		registDateStart = "20180203";
//		registTo = "20180206";
//		acceptStatus = "";
//		acceptDateStart = "";
//		acceptDateEnd = "";
//		column = "";
//		query = "";
//		page = 0;
//		rowSize = 10;
//		rawDataList = ngsDataRegistService.selectNgsDataRegistList(openStatus,
//				registStatus, registDateStart, registTo,
//				acceptStatus, acceptDateStart, acceptDateEnd,
//				column, query, page, rowSize);
//		assertEquals(3, rawDataList.size());
//	}
	
	@Test
	public void test4SelectListCount() throws Exception{
//		String openStatus = "Y";
//		String registStatus = "";
//		String registDateStart = "20180201";
//		String registTo = "20180208";
//		String acceptStatus = "";
//		String acceptDateStart = "20180201";
//		String acceptDateEnd = "20180208";
//		String column = "sampleSource";
//		String query = "code";
//		String firstIndex = "";
//		int rawDataListCount = ngsDataRegistService.getNgsDataRegistCount(openStatus,
//				registStatus, registDateStart, registTo,
//				acceptStatus, acceptDateStart, acceptDateEnd,
//				column, query);
//		assertEquals(3, rawDataListCount);
//		
//		openStatus = "Y";
//		registStatus = "";
//		registDateStart = "";
//		registTo = "";
//		acceptStatus = "";
//		acceptDateStart = "";
//		acceptDateEnd = "";
//		column = "";
//		query = "";
//		rawDataListCount = ngsDataRegistService.getNgsDataRegistCount(openStatus,
//				registStatus, registDateStart, registTo,
//				acceptStatus, acceptDateStart, acceptDateEnd,
//				column, query);
//		assertEquals(5, rawDataListCount);
//		
//		openStatus = "Y";
//		registStatus = "";
//		registDateStart = "";
//		registTo = "";
//		acceptStatus = "승인";
//		acceptDateStart = "";
//		acceptDateEnd = "";
//		column = "";
//		query = "";
//		rawDataListCount = ngsDataRegistService.getNgsDataRegistCount(openStatus,
//				registStatus, registDateStart, registTo,
//				acceptStatus, acceptDateStart, acceptDateEnd,
//				column, query);
//		assertEquals(2, rawDataListCount);
	}
	
	@Test
	public void test5Update() throws Exception{
//		int id = 3;
//		int updateId =  4;
//		NgsDataRegist rawData = new NgsDataRegist();
//		Code code = new Code();
//		rawData.setId(id);
//		rawData.setRegistId(updateId);
//		code.setCode("code"+updateId);
//		rawData.setExprType(code);
//		code.setCode("code"+updateId);
//		rawData.setDomain(code);
//		rawData.setCultureCondition("Kimchi microbes strain - UCC11"+updateId);
//		rawData.setTreatment("Control of kimchi additive reaction"+updateId);          
//		code.setCode("code"+updateId);
//		rawData.setReplicate(code);         
//		rawData.setSampleName("Lactobacillus salivarius UCC11"+updateId);
//		rawData.setOrganType("prokaryote");     
//		rawData.setSampleType("solar salt");
//		rawData.setGeographLocation("South Korea");
//		code.setCode("code"+updateId);
//		rawData.setSampleSource(code);
//		rawData.setSampleComment("이곳은 샘플에대한 코맨트를 남겨주세요 " + updateId);      
//		code.setCode("code"+updateId);
//		rawData.setSeqPlatform(code);   
//		rawData.setConstruct("TruSeq");
//		rawData.setSelection("illumina Hiseq"); 
//		rawData.setSequencer("RANDOM"); 
//		rawData.setStrategy("WGS");
//		rawData.setReadLength(150);   
//		rawData.setInsertSize(250);
//		code.setCode("code"+updateId);
//		rawData.setReadType(code);
//		rawData.setAdapterSeq("5`-GTTCAGAGTTCACAGTCCGACGATC  / 3`-TGGAATTCTCGGGTGCCAAGG");     
//		rawData.setQualityScoreVersion("illumina 1.5+Phred+64");
//		rawData.setBaseCallingProgram("Phred");
//			
//		ngsDataRegistService.updateNgsDataRegist(rawData);
//		
//		NgsDataRegist updateNgsDataRegist = ngsDataRegistService.selectNgsDataRegist(updateId);
//		
//		//T_NGS_RAW_DATA
//		assertEquals(updateId, rawData.getRegistId());
//		assertEquals("code" + updateId, rawData.getExprType().getCode());
//		assertEquals("code" + updateId, rawData.getDomain().getCode());
//		assertEquals("Kimchi microbes strain - UCC11" + updateId, rawData.getCultureCondition());
//		assertEquals("Control of kimchi additive reaction" + updateId, rawData.getTreatment());
//		assertEquals("code" + updateId, rawData.getReplicate().getCode());
//		assertEquals("Lactobacillus salivarius UCC11" + updateId, rawData.getSampleName());
//		assertEquals("prokaryote", rawData.getOrganType());
//		assertEquals("solar salt", rawData.getSampleType());
//		assertEquals("South Korea", rawData.getGeographLocation());
//		assertEquals("code" + updateId, rawData.getSampleSource().getCode());
//		assertEquals("이곳은 샘플에대한 코맨트를 남겨주세요 " + updateId, rawData.getSampleComment());
//		assertEquals("code" + updateId, rawData.getSeqPlatform().getCode());
//		assertEquals("TruSeq", rawData.getConstruct());
//		assertEquals("illumina Hiseq", rawData.getSelection());
//		assertEquals("RANDOM", rawData.getSequencer());
//		assertEquals("WGS", rawData.getStrategy());
//		assertEquals(150, rawData.getReadLength());
//		assertEquals(250, rawData.getInsertSize());
//		assertEquals("code" + updateId, rawData.getReadType().getCode());
//		assertEquals("5`-GTTCAGAGTTCACAGTCCGACGATC  / 3`-TGGAATTCTCGGGTGCCAAGG", rawData.getAdapterSeq());
//		assertEquals("illumina 1.5+Phred+64", rawData.getQualityScoreVersion());
//		assertEquals("Phred", rawData.getBaseCallingProgram());
	}
	
	@Test
	public void test6Delete() throws Exception{
//		String ids = "";
//		for(int i = 1; i < 11; i++){
//			ids += i+",";
//		}
//		int deleteCnt = ngsDataRegistService.deleteNgsDataRegist(ids);
//		assertEquals(10, deleteCnt);
	}
	
	private NgsDataRegist rawDataInsert(NgsDataRegist rawData, int i) {
//		Code code = new Code();
//		rawData.setRegistId(i);
//		code.setCode("code"+i);
//		rawData.setExprType(code);
//		code.setCode("code"+i);
//		rawData.setDomain(code);
//		rawData.setCultureCondition("Kimchi microbes strain - UCC11"+i);
//		rawData.setTreatment("Control of kimchi additive reaction"+i);          
//		code.setCode("code"+i);
//		rawData.setReplicate(code);         
//		rawData.setSampleName("Lactobacillus salivarius UCC11"+i);
//		rawData.setOrganType("prokaryote");     
//		rawData.setSampleType("solar salt");
//		rawData.setGeographLocation("South Korea");
//		code.setCode("code"+i);
//		rawData.setSampleSource(code);
//		rawData.setSampleComment("이곳은 샘플에대한 코맨트를 남겨주세요 " + i);      
//		code.setCode("code"+i);
//		rawData.setSeqPlatform(code);   
//		rawData.setConstruct("TruSeq");
//		rawData.setSelection("RANDOM"); 
//		rawData.setSequencer("illumina Hiseq"); 
//		rawData.setStrategy("WGS");
//		rawData.setReadLength(150);   
//		rawData.setInsertSize(250);
//		code.setCode("code"+i);
//		rawData.setReadType(code);
//		rawData.setAdapterSeq("5`-GTTCAGAGTTCACAGTCCGACGATC  / 3`-TGGAATTCTCGGGTGCCAAGG");     
//		rawData.setQualityScoreVersion("illumina 1.5+Phred+64");
//		rawData.setBaseCallingProgram("Phred");
		return rawData;
	}
}
