package com.insilicogen.gdkm.service;

import com.insilicogen.gdkm.model.Attachment;

public interface AttachmentService {
	
	public static final Object Lock = new Object();
	
	public int createAttachment(Attachment attachment) throws Exception;
	
	public int deleteAttachment(String attachments) throws Exception;

}
