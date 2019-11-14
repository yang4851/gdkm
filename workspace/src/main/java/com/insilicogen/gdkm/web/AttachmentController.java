package com.insilicogen.gdkm.web;

import java.util.Locale;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.insilicogen.exception.RestTransferException;
import com.insilicogen.exception.ZeroCountException;
import com.insilicogen.gdkm.model.Attachment;
import com.insilicogen.gdkm.service.AttachmentService;

@RestController
public class AttachmentController  extends AbstractController{
	
	@Resource(name="AttachmentService")
	AttachmentService attachmentService;

	@Resource(name = "messageSource")
	private MessageSource messageSource;
	
	@RequestMapping(value="/attachments", method=RequestMethod.POST)
	public void createAttachment(
			HttpSession session
			,@RequestBody Attachment attachment
			) throws Exception{
		 checkAuthorization(session);
		try{
			synchronized(AttachmentService.Lock){
				if(attachment.getRegistUser()==null){
					throw new Exception("user가 존재하지 않습니다.");
				}
				attachmentService.createAttachment(attachment);
			}
		}catch(Exception e){
			String args = messageSource.getMessage("ENTITY.CODE",null, Locale.US);
			String message = messageSource.getMessage("ERR002", new String[]{args}, Locale.US);
			throw new RestTransferException(message+" - "+e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//			throw new RestTransferException("그룹코드 등록 오류 - " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value="/attachments/{attachment_ids}", method=RequestMethod.DELETE)
	public void deleteAttachment(
			@PathVariable("attachment_ids") String attachmentIds
			,HttpSession session
			) throws Exception{
		checkAuthorization(session);
		try{
			synchronized(AttachmentService.Lock){
				int deleteCnt = attachmentService.deleteAttachment(attachmentIds);
				if(deleteCnt == 0)
					throw new ZeroCountException();
			}
		}catch(Exception e){
			String args = messageSource.getMessage("ENTITY.CODE",null, Locale.US);
			String message = messageSource.getMessage("ERR005", new String[]{args}, Locale.US);
			throw new RestTransferException(message+" - "+e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//			throw new RestTransferException("그룹코드 삭제 오류 - " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
}
