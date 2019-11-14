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

import com.insilicogen.gdkm.dao.NgsProcessedDataDAO;
import com.insilicogen.gdkm.model.Code;
import com.insilicogen.gdkm.model.NgsProcessedData;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations={"classpath:/egovframework/spring/context-*.xml"})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestNgsProcessedDataDAO {

	@Resource(name="NgsProcessedDataDAO")
	private NgsProcessedDataDAO dao;
	
	private Code[] codes = new Code[5];
	
	private int loop = 100;
	
	@Before
	public void setup() {
		for(int i=0; i < 5 ; i++) {
			codes[i] = new Code();
			codes[i].setCode("0" + (i+1));
			codes[i].setGroup("test");
			codes[i].setName("code-0" + (i+1));
		}
	}
	
	@Test
	public void test1InsertNgsProcessedData() throws Exception {
		for(int i=1; i < loop ; i+=2) {
			NgsProcessedData data = new NgsProcessedData();
			data.setRegistId(i+1);
			data.setSubmitter("submitter-" + i);
			data.setSubmitOrgan("submitOrgan-" + i);
			data.setProject("project-" + i);
			data.setProjectType("projectType-" + i);
			data.setSubmitComment("submitComment-" + i);
			data.setAssemblyMethod("assemblyMethod-" + i);
			data.setAssemblyLevel(codes[i%codes.length]);
			data.setGenomeType(codes[i%codes.length]);
			data.setGenomeCoverage("genomeCoverage-" + i);
			data.setSeqComment("seqComment-" + i);
			data.setRefTitle("refTitle-" + i);
			data.setRefPubStatus("refPubStatus-" + i);
			data.setRefComment("refComment-" + i);
			
			assertEquals(1, dao.insertNgsProcessedData(data));
		}
	}
	
	@Test
	public void test2SelectNgsProcessedData() throws Exception {
		for(int i=1; i < loop ; i+=2) {
			NgsProcessedData data = dao.selectNgsProcessedData(i+1);
			assertEquals(i+1, data.getRegistId());
			assertEquals("submitter-" + i, data.getSubmitter());
			assertEquals("submitOrgan-" + i, data.getSubmitOrgan());
			assertEquals("project-" + i, data.getProject());
			assertEquals("projectType-" + i, data.getProjectType());
			assertEquals("submitComment-" + i, data.getSubmitComment());
			assertEquals("assemblyMethod-" + i, data.getAssemblyMethod());
			assertEquals(codes[i%codes.length], data.getAssemblyLevel());
			assertEquals(codes[i%codes.length], data.getGenomeType());
			assertEquals("genomeCoverage-" + i, data.getGenomeCoverage());
			assertEquals("seqComment-" + i, data.getSeqComment());
			assertEquals("refTitle-" + i, data.getRefTitle());
			assertEquals("refPubStatus-" + i, data.getRefPubStatus());
			assertEquals("refComment-" + i, data.getRefComment());
		}
	}
	
	@Test
	public void test3UpdateNgsProcessedData() throws Exception {
		for(int i=1; i < loop ; i+=2) {
			NgsProcessedData data = new NgsProcessedData();
			data.setRegistId(i+1);
			data.setSubmitter("submitter-" + (i+1));
			data.setSubmitOrgan("submitOrgan-" + (i+1));
			data.setProject("project-" + (i+1));
			data.setProjectType("projectType-" + (i+1));
			data.setSubmitComment("submitComment-" + (i+1));
			data.setAssemblyMethod("assemblyMethod-" + (i+1));
			data.setAssemblyLevel(codes[(i+1)%codes.length]);
			data.setGenomeType(codes[(i+1)%codes.length]);
			data.setGenomeCoverage("genomeCoverage-" + (i+1));
			data.setSeqComment("seqComment-" + (i+1));
			data.setRefTitle("refTitle-" + (i+1));
			data.setRefPubStatus("refPubStatus-" + (i+1));
			data.setRefComment("refComment-" + (i+1));
			assertEquals(1, dao.updateNgsProcessedData(data));
			
			data = dao.selectNgsProcessedData(i+1);
			assertEquals(i+1, data.getRegistId());
			assertEquals("submitter-" + (i+1), data.getSubmitter());
			assertEquals("submitOrgan-" + (i+1), data.getSubmitOrgan());
			assertEquals("project-" + (i+1), data.getProject());
			assertEquals("projectType-" + (i+1), data.getProjectType());
			assertEquals("submitComment-" + (i+1), data.getSubmitComment());
			assertEquals("assemblyMethod-" + (i+1), data.getAssemblyMethod());
			assertEquals(codes[(i+1)%codes.length], data.getAssemblyLevel());
			assertEquals(codes[(i+1)%codes.length], data.getGenomeType());
			assertEquals("genomeCoverage-" + (i+1), data.getGenomeCoverage());
			assertEquals("seqComment-" + (i+1), data.getSeqComment());
			assertEquals("refTitle-" + (i+1), data.getRefTitle());
			assertEquals("refPubStatus-" + (i+1), data.getRefPubStatus());
			assertEquals("refComment-" + (i+1), data.getRefComment());			
		}
	}
	
	@Test
	public void test4DeleteNgsProcessedData() throws Exception {
		/*for(int i=1 ; i < loop/2 ; i+=2){
			assertEquals(1, dao.deleteNgsProcessedData(new int[]{ i+1 }));
		}
		
		int[] ids = IntStream.iterate(loop/2+2, i -> i + 2).limit(25).toArray();
		assertEquals(loop/4, dao.deleteNgsProcessedData(ids));*/
	}
}
