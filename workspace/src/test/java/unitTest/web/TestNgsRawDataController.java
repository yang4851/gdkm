package unitTest.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

//import com.insilicogen.gdkm.model.Code;
import com.insilicogen.gdkm.model.NgsRawData;
//import com.insilicogen.gdkm.model.Taxon;
//import com.insilicogen.gdkm.model.User;
import com.insilicogen.gdkm.util.JsonUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations={"classpath:/egovframework/spring/context-*.xml"})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestNgsRawDataController {
	
	SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat sdfDateTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	
	@Autowired
	private WebApplicationContext wac;
    private MockMvc mockMvc;
    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
	
	@Before
	public void setup() throws Exception{
		this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}
	
	@Test
	public void test1InsertNgsRawData() throws Exception{
		for(int i = 1; i < 11; i++){
			NgsRawData ngsRawData = new NgsRawData();
			ngsRawData = ngsDataInsert(ngsRawData, i);
			
			String json = JsonUtil.getJsonString2(ngsRawData);
			
			this.mockMvc.perform(post("/raw-data")
					.contentType(APPLICATION_JSON_UTF8)
					.content(json))
			.andExpect(status().isOk());
		}
	}
	
	private NgsRawData ngsDataInsert(NgsRawData ngsRawData, int i) throws ParseException {
//		Code code = new Code();
//		Taxon taxon = new Taxon();
//		taxon.setId(1794524);
//		ngsRawData.setTaxon(taxon);
//		ngsRawData.setDataType((i%2==0) ? "rawData": "annotationData");
//		switch(i){
//		case  1: case 5: case 8: 
//			ngsRawData.setStrain("CC02124");
//			break;
//		
//		case  2: case 3: case 6: 
//			ngsRawData.setStrain("ASDF111");
//			break;
//			
//		case  4: case 9: 
//			ngsRawData.setStrain("aswef025");
//			break;
//			
//		case  7: case 10: 
//			ngsRawData.setStrain("asw1124jg");
//			break;
//	}
//		ngsRawData.setOpenYn((i%2==0) ? "Y" : "N");
//		ngsRawData.setOpenDate(sdfDate.parse("2018-02-0"+(i+1)));
//		switch(i%3){
//		case 0:
//			ngsRawData.setApprovalStatus("대기");
//			break;
//		case 1:
//			ngsRawData.setApprovalStatus("준비");
//			break;
//		case 2:
//			ngsRawData.setApprovalStatus("승인");
//			break;
//		default :
//			ngsRawData.setApprovalStatus("그외");
//		}
//		ngsRawData.setApprovalDate(sdfDate.parse("2018-02-0"+(i+1)));
//		switch(i%3){
//			case 0:
//				ngsRawData.setRegistStatus("대기");
//				break;
//			case 1:
//				ngsRawData.setRegistStatus("등록");
//				break;
//			case 2:
//				ngsRawData.setRegistStatus("완료");
//				break;
//			default :
//				ngsRawData.setRegistStatus("그외");
//		}
//		User user = new User();
////		user.setId("encryptId"+i);
//		ngsRawData.setRegistUser(user);
//		ngsRawData.setRegistDate(sdfDateTime.parse("2018-02-0"+i+" 11:20:3"+i));
//		ngsRawData.setUpdateUser(user);
//		ngsRawData.setUpdateDate(sdfDateTime.parse("2018-02-0"+i+" 11:20:3"+i));
		
//		ngsRawData.setRegistId(i);
//		code.setId("코드Test"+i);
//		ngsRawData.setExprType(code);
//		code.setId("코드Test"+i);
//		ngsRawData.setTaxonomy(code);
//		ngsRawData.setCultureCondition("Kimchi microbes strain - UCC11"+i);
//		ngsRawData.setTreatment("Control of kimchi additive reaction"+i);          
//		code.setId("코드Test"+i);
//		ngsRawData.setReplicate(code);         
//		ngsRawData.setSampleName("Lactobacillus salivarius UCC11"+i);
//		ngsRawData.setOrganType("prokaryote");     
//		ngsRawData.setBioSampleType("solar salt");
//		ngsRawData.setGeographLocation("South Korea");
//		code.setId("코드Test"+i);
//		ngsRawData.setSource(code);
//		ngsRawData.setSampleComment("이곳은 샘플에대한 코맨트를 남겨주세요 " + i);      
//		code.setId("코드Test"+i);
//		ngsRawData.setSeqPlatform(code);   
//		ngsRawData.setConstruct("TruSeq");
//		ngsRawData.setSelection("RANDOM"); 
//		ngsRawData.setInstrumentModel("illumina Hiseq"); 
//		ngsRawData.setStrategy("WGS");
//		ngsRawData.setReadLength(150);   
//		ngsRawData.setInsertSize(250);
//		code.setId("코드Test"+i);
//		ngsRawData.setReadType(code);
//		ngsRawData.setAdapterSeq("5`-GTTCAGAGTTCACAGTCCGACGATC  / 3`-TGGAATTCTCGGGTGCCAAGG");     
//		ngsRawData.setQualityScoreVersion("illumina 1.5+Phred+64");
//		ngsRawData.setBaseCallingProgram("Phred");
		
		return ngsRawData;
	}	
	
	
}
