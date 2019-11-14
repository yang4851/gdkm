package unitTest.dao;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.insilicogen.gdkm.dao.TaxonomyDAO;
import com.insilicogen.gdkm.model.Taxonomy;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations={"classpath:/egovframework/spring/context-*.xml"})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestTaxonomyDAO {
	
	@Resource(name="TaxonomyDAO")
	private TaxonomyDAO dao;
	
//	@Test
	public void test1SelectTaxonomy() throws Exception {
		assertEquals(3105092, dao.selectTaxonomy(3105092).getTaxonId());
		assertEquals(1629204, dao.selectTaxonomy(3105092).getNcbiTaxonId());
		assertEquals(1842479, dao.selectTaxonomy(3105092).getParentId());
		assertEquals(77, dao.selectTaxonomy(1842479).getNumberOfChild());
		assertEquals("Annelida sp. BOLD:ACQ9976", dao.selectTaxonomy(3105092).getName());
		assertEquals("species", dao.selectTaxonomy(3105092).getRank());
	}
	
//	@Test
	public void test2SelectTaxnomyList() throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("taxonId", 3105092);
		assertEquals(1, dao.selectTaxonomyList(params).size());
		params.remove("taxonId");
		
		params.put("parentId", 1842479);
		assertEquals(77, dao.selectTaxonomyList(params).size());
		params.remove("parentId");
		
		params.put("rank", "superkingdom");
		assertEquals(3, dao.selectTaxonomyList(params).size());
		params.remove("rank");
		
		params.put("dataType", "rawdata");
		assertEquals(7, dao.selectTaxonomyList(params).size());
		params.remove("dataType");
		
		params.put("registered", true);
		assertEquals(7, dao.selectTaxonomyList(params).size());
		params.remove("registered");
		
		params.put("field", "ncbiTaxonId");
		params.put("keyword", "Annelida");
		assertEquals(0, dao.selectTaxonomyList(params).size());
		
		params.put("field", "name");
		params.put("keyword", "Annelida");
		assertEquals(81, dao.selectTaxonomyList(params).size());
		params.remove("keyword");
	}
	
//	@Test
	public void test3SelectTaxonomyCount() throws Exception {
		assertEquals(77, dao.selectChildCount(1842479));
	}
	
	@Test
	public void test6SelectTaxonNode() throws Exception{
		Map<String,Object> params = new HashMap<String, Object>();
		List<Taxonomy> list = dao.selectTaxonNode(params);
		System.out.println(list);
	}
}
