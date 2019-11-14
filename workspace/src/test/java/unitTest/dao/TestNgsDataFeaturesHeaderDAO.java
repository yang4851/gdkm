package unitTest.dao;

import static org.junit.Assert.fail;

import java.util.Date;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.insilicogen.gdkm.dao.NgsDataFeaturesHeaderDAO;
import com.insilicogen.gdkm.model.NgsDataFeaturesHeader;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations={"classpath:/egovframework/spring/context-*.xml"})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestNgsDataFeaturesHeaderDAO {

	@Resource(name="NgsDataFeaturesHeaderDAO")
	NgsDataFeaturesHeaderDAO headerDAO;
	
	NgsDataFeaturesHeader header;
	
	
	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {
		System.out.println(headerDAO.selectListHeader());
	}

}
