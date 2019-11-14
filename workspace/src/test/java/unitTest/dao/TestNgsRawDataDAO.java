package unitTest.dao;

import static org.junit.Assert.*;

import java.util.stream.IntStream;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.insilicogen.gdkm.dao.NgsRawDataDAO;
import com.insilicogen.gdkm.model.Code;
import com.insilicogen.gdkm.model.NgsRawData;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations={"classpath:/egovframework/spring/context-*.xml"})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestNgsRawDataDAO {

	@Resource(name="NgsRawDataDAO")
	private NgsRawDataDAO dao;
	
	private Code[] codes = new Code[5];
	
	private int loop = 100;
	
	@Before
	public void setup() {
		for(int i=0; i < 5 ; i++) {
			codes[i] = new Code();
			codes[i].setCode("0" + (i+1));
			codes[i].setGroup("test"+i);
			codes[i].setName("code-0" + (i+1));
		}
	}
	
	@Test
	public void test1InsertNgsRawData() throws Exception {
		for(int i=0; i < loop ; i+=2) {
			NgsRawData data = new NgsRawData();
			data.setRegistId(i+1);
			data.setExprType(codes[i%codes.length]);
			data.setDomain(codes[i%codes.length]);
			data.setCultureCondition("cultureCondition-" + i);
			data.setTreatment("treatment-" + i);
			data.setReplicate(codes[i%codes.length]);
			data.setSampleName("sampleName-" + i);
			data.setOrganismType("organType-" + i);
			data.setSampleType("sampleType-" + i);
			data.setSampleSource(codes[i%codes.length]);
			data.setGeographLocation("geographLocation-" + i);
			data.setSampleComment("sampleComment-" + i);
			data.setPlatform(codes[i%codes.length]);
			data.setConstruction("construct-" + i);
			data.setSelection("selection-" + i);
			data.setSequencer("sequencer-" + i);
			data.setStrategy("strategy-" + i);
			data.setReadType(codes[i%codes.length]);
			data.setReadLength(i);
			data.setInsertSize(i+1);
			data.setAdapterSeq("adapterSeq-" + i);
			data.setQualityScoreVersion("qualityScoreVersion-" + i);
			data.setBaseCallingProgram("baseCallingProgram-" + i);
			
			assertEquals(1, dao.insertNgsRawData(data));
		}
	}
	
	@Test
	public void test2SelectNgsRawData() throws Exception {
		for(int i=0; i < loop ; i+=2) {
			NgsRawData data = dao.selectNgsRawData(i+1);
			assertEquals(i+1, data.getRegistId());
			assertEquals(codes[i%codes.length], data.getExprType());
			assertEquals(codes[i%codes.length], data.getDomain());
			assertEquals("cultureCondition-" + i, data.getCultureCondition());
			assertEquals("treatment-" + i, data.getTreatment());
			assertEquals(codes[i%codes.length], data.getReplicate());
			assertEquals("sampleName-" + i, data.getSampleName());
			assertEquals("organType-" + i, data.getOrganismType());
			assertEquals("sampleType-" + i, data.getSampleType());
			assertEquals(codes[i%codes.length], data.getSampleSource());
			assertEquals("geographLocation-" + i, data.getGeographLocation());
			assertEquals("sampleComment-" + i, data.getSampleComment());
			assertEquals(codes[i%codes.length], data.getPlatform());
			assertEquals("construct-" + i, data.getConstruction());
			assertEquals("selection-" + i, data.getSelection());
			assertEquals("sequencer-" + i, data.getSequencer());
			assertEquals("strategy-" + i, data.getStrategy());
			assertEquals(codes[i%codes.length], data.getReadType());
			assertEquals(i, data.getReadLength());
			assertEquals(i+1, data.getInsertSize());
			assertEquals("adapterSeq-" + i, data.getAdapterSeq());
			assertEquals("qualityScoreVersion-" + i, data.getQualityScoreVersion());
			assertEquals("baseCallingProgram-" + i, data.getBaseCallingProgram());
		}
	}
	
	@Test
	public void test3UpdateNgsRawData() throws Exception {
		for(int i=0; i < loop ; i+=2) {
			NgsRawData data = new NgsRawData();
			data.setRegistId(i+1);
			data.setExprType(codes[(i+1)%codes.length]);
			data.setDomain(codes[(i+1)%codes.length]);
			data.setCultureCondition("cultureCondition-" + (i+1));
			data.setTreatment("treatment-" + (i+1));
			data.setReplicate(codes[(i+1)%codes.length]);
			data.setSampleName("sampleName-" + (i+1));
			data.setOrganismType("organType-" + (i+1));
			data.setSampleType("sampleType-" + (i+1));
			data.setSampleSource(codes[(i+1)%codes.length]);
			data.setGeographLocation("geographLocation-" + (i+1));
			data.setSampleComment("sampleComment-" + (i+1));
			data.setPlatform(codes[(i+1)%codes.length]);
			data.setConstruction("construct-" + (i+1));
			data.setSelection("selection-" + (i+1));
			data.setSequencer("sequencer-" + (i+1));
			data.setStrategy("strategy-" + (i+1));
			data.setReadType(codes[(i+1)%codes.length]);
			data.setReadLength(i+1);
			data.setInsertSize(i+2);
			data.setAdapterSeq("adapterSeq-" + (i+1));
			data.setQualityScoreVersion("qualityScoreVersion-" + (i+1));
			data.setBaseCallingProgram("baseCallingProgram-" + (i+1));
			assertEquals(1, dao.updateNgsRawData(data));
			
			data = dao.selectNgsRawData(i+1);
			assertEquals(i+1, data.getRegistId());
			assertEquals(codes[(i+1)%codes.length], data.getExprType());
			assertEquals(codes[(i+1)%codes.length], data.getDomain());
			assertEquals("cultureCondition-" + (i+1), data.getCultureCondition());
			assertEquals("treatment-" + (i+1), data.getTreatment());
			assertEquals(codes[(i+1)%codes.length], data.getReplicate());
			assertEquals("sampleName-" + (i+1), data.getSampleName());
			assertEquals("organType-" + (i+1), data.getOrganismType());
			assertEquals("sampleType-" + (i+1), data.getSampleType());
			assertEquals(codes[(i+1)%codes.length], data.getSampleSource());
			assertEquals("geographLocation-" + (i+1), data.getGeographLocation());
			assertEquals("sampleComment-" + (i+1), data.getSampleComment());
			assertEquals(codes[(i+1)%codes.length], data.getPlatform());
			assertEquals("construct-" + (i+1), data.getConstruction());
			assertEquals("selection-" + (i+1), data.getSelection());
			assertEquals("sequencer-" + (i+1), data.getSequencer());
			assertEquals("strategy-" + (i+1), data.getStrategy());
			assertEquals(codes[(i+1)%codes.length], data.getReadType());
			assertEquals(i+1, data.getReadLength());
			assertEquals(i+2, data.getInsertSize());
			assertEquals("adapterSeq-" + (i+1), data.getAdapterSeq());
			assertEquals("qualityScoreVersion-" + (i+1), data.getQualityScoreVersion());
			assertEquals("baseCallingProgram-" + (i+1), data.getBaseCallingProgram());
		}
	}
	
	@Test
	public void test4DeleteNgsRawData() throws Exception {
		/*for(int i=0 ; i < loop/2 ; i+=2){
			assertEquals(1, dao.deleteNgsRawData(new int[]{ i+1 }));
		}
		
		int[] ids = IntStream.iterate((loop/2)+1, i -> i + 2).limit(25).toArray();
		assertEquals(loop/4, dao.deleteNgsRawData(ids));*/
	}
}
