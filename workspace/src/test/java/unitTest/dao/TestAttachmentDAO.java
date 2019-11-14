package unitTest.dao;

import static org.junit.Assert.assertEquals;

import java.text.SimpleDateFormat;
//import java.util.List;

import javax.annotation.Resource;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.insilicogen.gdkm.dao.AttachmentDAO;
import com.insilicogen.gdkm.model.Attachment;
import com.insilicogen.gdkm.model.User;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations={"classpath:/egovframework/spring/context-*.xml"})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestAttachmentDAO {
	@Resource(name="AttachmentDAO")
	private AttachmentDAO attachmentDAO;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	@Test
	public void test1InsertAttachment() throws Exception{
		for(int i = 1; i < 11; i++){
			Attachment attachment = new Attachment();
			attachment.setId(i);
			attachment.setName("Attachment"+i);
			attachment.setType(".xlsx");
			attachment.setSize(23535L);
			User user = new User();
			user.setUserId("testId2");
			attachment.setRegistUser(user);
			
			int insertCnt = attachmentDAO.insertAttachment(attachment);
			assertEquals(1, insertCnt);
			Thread.sleep(1000);
		}
	}
	
	@Test
	public void test2Delete() throws Exception{
		String[] ids = new String[11];
		for(int i = 1; i < 11; i++){
			ids[i] = i + ",";
		}
		int deleteCnt = attachmentDAO.deleteAttachment(ids);
		assertEquals(10, deleteCnt);
	}
	
}
