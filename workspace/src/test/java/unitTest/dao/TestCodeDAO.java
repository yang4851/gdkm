package unitTest.dao;

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

import com.insilicogen.gdkm.dao.CodeDAO;
import com.insilicogen.gdkm.model.Code;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations={"classpath:/egovframework/spring/context-*.xml"})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestCodeDAO {
	@Resource(name="CodeDAO")
	private CodeDAO codeDAO;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	@Test
	public void test1InsertCode() throws Exception{
		for(int i = 0; i < 10; i++){
			Code code = new Code();
			code.setCode("0" + (i+1));
			code.setGroup("test"+i);
			code.setName("code-0" + (i+1));
			code.setUseYn("Y");
			
			int insertCnt = codeDAO.insertCode(code);
			assertEquals(1, insertCnt);
			Thread.sleep(1000);
		}
	}

	@Test
	public void test2SelectCode() throws Exception{
		String i = "2";
		Code code = codeDAO.selectCode("0" + (i+1));
		assertEquals("0" + (i+1), code.getCode());
		assertEquals("test"+i, code.getGroup());
		assertEquals("code-0" + (i+1), code.getName());
		assertEquals("Y", code.getUseYn());
	}
	
//	@Test
//	public void test3SelectList() throws Exception{
//		String codeGroupId = "";
//		String code = "";
//		String name = "";
//		String useYn = "";
//		List<Code> codeList = codeDAO.selectList(codeGroupId, code, name, useYn, 0, 10);
//		assertEquals(10, codeList.size());
//		int j = 0;
//		for(int i = 10; i > 0; i--){
//			Code code1 = codeList.get(j);
//			assertEquals("0" + (i+1), code1.getCode());
//			assertEquals("test"+i, code1.getGroup());
//			assertEquals("code-0" + (i+1), code1.getName());
//			assertEquals("Y", code1.getUseYn());
//			j++;
//		}
//		
//		/*codeGroupId = "";
//		code = "est1";
//		name = "";
//		useYn = "";
//		codeList = codeDAO.selectList(codeGroupId, code, name, useYn, 0, 10);
//		assertEquals(2, codeList.size());
//		code = "code2";
//		codeList = codeDAO.selectList(codeGroupId, code, name, useYn, 0, 10);
//		assertEquals(1, codeList.size());
//		
//		codeGroupId = "10";
//		code = "est1";
//		name = "";
//		useYn = "";
//		codeList = codeDAO.selectList(codeGroupId, code, name, useYn, 0, 10);
//		assertEquals(1, codeList.size());
//		
//		codeGroupId = "";
//		code = "";
//		name = "명1";
//		useYn = "";
//		codeList = codeDAO.selectList(codeGroupId, code, name, useYn, 0, 10);
//		assertEquals(2, codeList.size());
//		codeGroupId = "0";
//		codeList = codeDAO.selectList(codeGroupId, code, name, useYn, 0, 10);
//		assertEquals(1, codeList.size());
//		useYn="N";
//		codeList = codeDAO.selectList(codeGroupId, code, name, useYn, 0, 10);
//		assertEquals(0, codeList.size());
//		useYn="Y";
//		codeList = codeDAO.selectList(codeGroupId, code, name, useYn, 0, 10);
//		assertEquals(1, codeList.size());
//		useYn="";
//		codeList = codeDAO.selectList(codeGroupId, code, name, useYn, 0, 10);
//		assertEquals(1, codeList.size());*/
//	}
//	
//	
////	@Test
//	public void test4Update() throws Exception{
//		String preparedCode = "code1";
//		int i = 2;
//		Code code = new Code();
//		
//		code.setCode(preparedCode);
//		code.setName("업뎃네임"+i);
//		code.setGroup("cg"+i);
//		code.setUseYn("N");
//		codeDAO.updateCode(code);
//		
//		Code updateCode = codeDAO.selectCode(preparedCode);
//		
//		assertEquals(preparedCode, updateCode.getCode());
//		assertEquals("업뎃네임"+i, updateCode.getName());
//		assertEquals("cg"+i, updateCode.getGroup());
//		assertEquals("N", updateCode.getUseYn());
//	}
//	
//	@Test
//	public void test5Delete() throws Exception{
//		String[] ids = new String[11];
//		for(int i = 0; i < 10; i++){
//			ids[i] = "0"+i;
//		}
//		int deleteCnt = codeDAO.deleteCode(ids);
//		assertEquals(10, deleteCnt);
//	}
	
}
