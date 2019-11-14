package unitTest.dao;

import static org.junit.Assert.assertEquals;

import java.text.SimpleDateFormat;
//import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.insilicogen.gdkm.dao.CodeGroupDAO;
import com.insilicogen.gdkm.model.CodeGroup;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations={"classpath:/egovframework/spring/context-*.xml"})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestCodeGroupDAO {
	@Resource(name="CodeGroupDAO")
	private CodeGroupDAO codeGroupDAO;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@Test
	public void test1InsertCodeGroup() throws Exception{
		for(int i = 0; i < 10; i++){
			CodeGroup codeGroup = new CodeGroup();
			codeGroup.setCodeGroup("test" + i);
			codeGroup.setName("이름" + i);
			codeGroup.setUseYn(i%2==0?"Y":"N");
			int insertCnt = codeGroupDAO.insertCodeGroup(codeGroup);
			assertEquals(1, insertCnt);
			Thread.sleep(1000);
		}
	}

	@Test
	public void test2SelectCodeGroup() throws Exception{
		int i = 1;
		String codeGroupId = "cg"+i;
		CodeGroup codeGroup = codeGroupDAO.selectCodeGroup(codeGroupId);
		
		assertEquals("이름"+i, codeGroup.getName());
		assertEquals(i%2==0?"Y":"N", codeGroup.getUseYn());
	}
	
//	@Test
//	public void test3SelectList() throws Exception{
//		String codeGroupId = "";
//		String name = "";
//		String useYn = "";
//		List<CodeGroup> codeGroupList = codeGroupDAO.selectCodeGroupList(codeGroupId, name, useYn, 0, 10);
//		assertEquals(10, codeGroupList.size());
//		int j = 0;
//		for(int i = 10; i > 0; i--){
//			CodeGroup codeGroup = codeGroupList.get(j);
//			assertEquals("cg" + i, codeGroup.getCodeGroup());
//			assertEquals("이름" + i, codeGroup.getName());
//			j++;
//		}
//		useYn="Y";
//		codeGroupList = codeGroupDAO.selectCodeGroupList(codeGroupId, name, useYn, 0, 10);
//		assertEquals(5, codeGroupList.size());
//		
//		codeGroupId="g6";
//		codeGroupList = codeGroupDAO.selectCodeGroupList(codeGroupId, name, useYn, 0, 10);
//		assertEquals(1, codeGroupList.size());
//		assertEquals("이름6", codeGroupList.get(0).getName());
//		
//		name="름6";
//		codeGroupList = codeGroupDAO.selectCodeGroupList(codeGroupId, name, useYn, 0, 10);
//		assertEquals(1, codeGroupList.size());
//		assertEquals("이름6", codeGroupList.get(0).getName());
//		name="름4";
//		codeGroupList = codeGroupDAO.selectCodeGroupList(codeGroupId, name, useYn, 0, 10);
//		assertEquals(0, codeGroupList.size());
//		
//		codeGroupId="g4";
//		codeGroupList = codeGroupDAO.selectCodeGroupList(codeGroupId, name, useYn, 0, 10);
//		assertEquals(1, codeGroupList.size());
//		assertEquals("이름4", codeGroupList.get(0).getName());
//		
//		codeGroupId = "";
//		name = "";
//		useYn = "";
//		codeGroupList = codeGroupDAO.selectCodeGroupList(codeGroupId, name, useYn, 0, 10);
//		assertEquals(10, codeGroupList.size());
//		j = 0;
//		for(int i = 10; i > 0; i--){
//			CodeGroup codeGroup = codeGroupList.get(j);
//			assertEquals("cg" + i, codeGroup.getCodeGroup());
//			assertEquals("이름" + i, codeGroup.getName());
//			j++;
//		}
//		useYn="Y";
//		codeGroupList = codeGroupDAO.selectCodeGroupList(codeGroupId, name, useYn, 0, 10);
//		assertEquals(5, codeGroupList.size());
//		
//	}
	
	@Test
	public void test4Update() throws Exception{
		int i = 1;
		CodeGroup codeGroup = new CodeGroup();
		codeGroup.setCodeGroup("cg"+i);
		codeGroup.setName("업뎃네임1");
		codeGroup.setUseYn("N");
		codeGroup.setRegistDate(sdf.parse("2018-02-03 11:20:30"));
		codeGroupDAO.updateCodeGroup(codeGroup);
		
		CodeGroup updateCodeGroup = codeGroupDAO.selectCodeGroup("cg"+i);
		
		assertEquals("업뎃네임1", updateCodeGroup.getName());
	}
	
	@Test
	public void test5Delete() throws Exception{
		String[] ids = new String[12];
		for(int i = 0; i < 10; i++){
			ids[i-1] = "cg"+i;
		}
		int deleteCnt = codeGroupDAO.deleteCodeGroup(ids);
		assertEquals(10, deleteCnt);
	}
	
}
