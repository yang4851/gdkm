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
import com.insilicogen.gdkm.service.CodeService;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations={"classpath:/egovframework/spring/context-*.xml"})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestCodeService {
	
	@Resource(name="CodeService")
	CodeService codeService;
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
//	@Test
	public void test1InsertCode() throws Exception{
		for(int i = 1; i < 11; i++){
			Code code = new Code();
			code.setCode("code" + i);
			code.setGroup("cg" + i);
			code.setName("코드명" + i);
			code.setUseYn("Y");
			
			int insertCnt = codeService.createCode(code);
			assertEquals(1, insertCnt);
			Thread.sleep(1000);
		}
	}
	
	@Test
	public void test2SelectCode() throws Exception{
		String i = "2";
		Code code = codeService.getCode("code" + i);
		assertEquals("code" + i, code.getCode());
		assertEquals("cg"+i, code.getGroup());
		assertEquals("코드명"+i, code.getName());
		assertEquals("Y", code.getUseYn());
	}
	
//	@Test
//	public void test3SelectList() throws Exception{
//		String codeGroupId = "";
//		String code = "";
//		String name = "";
//		String useYn = "";
//		List<Code> codeList = codeService.selectCodeList(codeGroupId, code, name, useYn, 0, 10);
//		assertEquals(10, codeList.size());
//		int j = 0;
//		for(int i = 10; i > 0; i--){
//			Code code1 = codeList.get(j);
//			assertEquals("code"+i, code1.getCode());
//			assertEquals("cg"+i, code1.getGroup());
//			assertEquals("코드명"+i, code1.getName());
//			assertEquals("Y", code1.getUseYn());
//			j++;
//		}
//		
//		codeGroupId = "";
//		code = "de1";
//		name = "";
//		useYn = "";
//		codeList = codeService.selectCodeList(codeGroupId, code, name, useYn, 0, 10);
//		assertEquals(2, codeList.size());
//		code = "code2";
//		codeList = codeService.selectCodeList(codeGroupId, code, name, useYn, 0, 10);
//		assertEquals(1, codeList.size());
//		
//		codeGroupId = "10";
//		code = "de1";
//		name = "";
//		useYn = "";
//		codeList = codeService.selectCodeList(codeGroupId, code, name, useYn, 0, 10);
//		assertEquals(1, codeList.size());
//		
//		codeGroupId = "";
//		code = "";
//		name = "명1";
//		useYn = "";
//		codeList = codeService.selectCodeList(codeGroupId, code, name, useYn, 0, 10);
//		assertEquals(2, codeList.size());
//		codeGroupId = "0";
//		codeList = codeService.selectCodeList(codeGroupId, code, name, useYn, 0, 10);
//		assertEquals(1, codeList.size());
//		useYn="N";
//		codeList = codeService.selectCodeList(codeGroupId, code, name, useYn, 0, 10);
//		assertEquals(0, codeList.size());
//		useYn="Y";
//		codeList = codeService.selectCodeList(codeGroupId, code, name, useYn, 0, 10);
//		assertEquals(1, codeList.size());
//		useYn="";
//		codeList = codeService.selectCodeList(codeGroupId, code, name, useYn, 0, 10);
//		assertEquals(1, codeList.size());
//	}
//	
////	@Test
//	public void test4Update() throws Exception{
//		String preparedCode = "code1";
//		int i = 2;
//		Code code = new Code();
//		
//		code.setCode(preparedCode);
//		code.setName("name"+i);
//		code.setGroup("cg"+i);
//		code.setUseYn("N");
//		codeService.updateCode(code);
//		
//		Code updateCode = codeService.selectCode(preparedCode);
//		
//		assertEquals(preparedCode, updateCode.getCode());
//		assertEquals("name"+i, updateCode.getName());
//		assertEquals("cg"+i, updateCode.getGroup());
//		assertEquals("N", updateCode.getUseYn());
//	}
//	
////	@Test
//	public void test5Delete() throws Exception{
//		String ids = "";
//		for(int i = 1; i < 11; i++){
//			ids += "code" + i + ",";
//		}
//		int deleteCnt = codeService.deleteCode(ids);
//		assertEquals(10, deleteCnt);
//	}
	
}
