package unitTest.dao;

import static org.junit.Assert.*;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.insilicogen.gdkm.dao.NgsDataFeaturesDAO;
import com.insilicogen.gdkm.dao.NgsDataFeaturesXrefDAO;
import com.insilicogen.gdkm.model.NgsDataFeatures;
import com.insilicogen.gdkm.model.NgsDataFeaturesXref;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations={"classpath:/egovframework/spring/context-*.xml"})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestNgsDataFeaturesDAO {
	
	@Resource(name="NgsDataFeaturesDAO")
	NgsDataFeaturesDAO featuresDAO;
	@Resource(name="NgsDataFeaturesXrefDAO")
	NgsDataFeaturesXrefDAO xrefDAO;
	
	NgsDataFeatures features;
	
	@Before
	public void setUp() throws Exception {
		features = new NgsDataFeatures();
		features.setCodonStart(1);
		features.setEcNumber("ec");
		features.setGene("venB");
		features.setHeaderId(1);
		features.setLocusTag("loc");
		features.setProduct("product");
		features.setSequence("AACCG");
		features.setType("gene");
		features.setTranslTable(1);
		features.setProteinId("Y123");
		features.setStart(1);
		features.setEnd(55555);
		features.setStrand("NAGATIVE");
	}

//	@Test
//	public void test1InsertFeatures() {
//		
//		assertEquals(0, features.getFeatures_id());
//		assertEquals(1, featuresDAO.insertFeatures(features));
//		assertNotEquals(0, features.getFeatures_id());
//	}
	
//	@Test
//	public void test2DeleteFeatures() {
//		NgsDataFeatures savedFeatures = featuresDAO.selectOneFeatures(2);
//		assertNotNull(savedFeatures);
//		
//		assertEquals(1, featuresDAO.deleteFeatures(2));
//		
//		savedFeatures = featuresDAO.selectOneFeatures(2); 
//		assertNull(savedFeatures);
//		
//	}
	
//	@Test
//	public void test3UpdateFeatures() {
//		features.setFeatures_id(4);
//		features.setHeader_id(2);
//		features.setSequence("AAAAAAA");
//		
//		NgsDataFeatures savedFeatures = featuresDAO.selectOneFeatures(features.getFeatures_id());
//		assertNotNull(savedFeatures);
//		
//		assertEquals(1, featuresDAO.updateFeatures(features));
//		
//		savedFeatures = featuresDAO.selectOneFeatures(features.getFeatures_id());
//		assertEquals(features.getSequence(), savedFeatures.getSequence());
//		
//	}
	
	@Test
	public void test4SelectOneFeatures() {
//		NgsDataFeatures savedFeatures = featuresDAO.selectOneFeatures(22);
//		System.out.println(savedFeatures);
		
		NgsDataFeaturesXref x = xrefDAO.selectOneXref(2);
		System.out.println(x);
		
		
	}
}
