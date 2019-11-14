package com.insilicogen.gdkm.service.impl;

//import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.insilicogen.gdkm.dao.AttachmentDAO;
import com.insilicogen.gdkm.model.Attachment;
import com.insilicogen.gdkm.service.AttachmentService;
import com.insilicogen.gdkm.util.EgovStringUtil;

@Service("AttachmentService")
public class AttachmentServiceImpl implements AttachmentService{
	
	@Resource(name="AttachmentDAO")
	AttachmentDAO attachmentDAO;

	@Override
	public int createAttachment(Attachment attachment) throws Exception {
		return attachmentDAO.insertAttachment(attachment);
	}

	@Override
	public int deleteAttachment(String attachments) throws Exception {
		String[] arrayAttachments = EgovStringUtil.splitStringToStringArray(attachments);
		return attachmentDAO.deleteAttachment(arrayAttachments);
	}
}
