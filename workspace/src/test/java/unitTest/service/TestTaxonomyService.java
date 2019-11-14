package unitTest.service;

import static org.junit.Assert.*;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.insilicogen.gdkm.model.Taxonomy;
import com.insilicogen.gdkm.service.TaxonomyService;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations={"classpath:/egovframework/spring/context-*.xml"})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestTaxonomyService {

	@Resource(name="TaxonomyService")
	TaxonomyService service;
	
	Taxonomy taxonomy;
	
	@Before
	public void setup() throws Exception { 
		taxonomy = new Taxonomy();
		taxonomy.setTaxonId(228064);
		taxonomy.setNcbiTaxonId(272621);
		taxonomy.setParentId(1233);
		taxonomy.setName("Lactobacillus acidophilus NCFM");
		taxonomy.setRank("no rank");
	}
	
	@Test
	public void test1GetHierarchicalList() {
		assertEquals(7, service.getHierarchyList(taxonomy).size());
	}
	
	@Test
	public void test2GetChildTaxononmyList() {
		taxonomy.setTaxonId(1233);
		assertEquals(13, service.getChildTaxonomyList(taxonomy).size());
	}
	
	@Test
	public void test3GetChildTaxononmyCount() {
		taxonomy.setTaxonId(1233);
		assertEquals(13, service.getChildTaxonomyCount(taxonomy));
	}
}
