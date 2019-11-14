package com.insilicogen.gdkm.dao;

import org.springframework.stereotype.Repository;

import com.insilicogen.gdkm.model.Attachment;

import egovframework.rte.psl.dataaccess.EgovAbstractMapper;

@Repository("AttachmentDAO")
public class AttachmentDAO extends EgovAbstractMapper{
	
	public int insertAttachment(Attachment attachment) throws Exception{
		return insert("Attachment.insert", attachment);
	}
	
	public int deleteAttachment(String[] attachmentIds) throws Exception{
		return delete("Attachment.delete", attachmentIds);
	}

}
