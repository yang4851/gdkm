package com.insilicogen.gdkm.service;

import java.util.List;
import java.util.Map;

import com.insilicogen.gdkm.model.Research;
import com.insilicogen.gdkm.model.ResearchAttachment;

public interface ResearchAttachmentService {

	public static final Object Lock = new Object();
	
	public ResearchAttachment createAttachment(ResearchAttachment attachment) throws Exception;
	
	public ResearchAttachment changeAttachment(ResearchAttachment attachment) throws Exception;
	
	public ResearchAttachment getAttachment(Integer attachmentId) throws Exception;
	
	public List<ResearchAttachment> getAttachmentList(Research research) throws Exception;
	
	public List<ResearchAttachment> getAttachmentList(Map<String, Object> params) throws Exception;

	public List<ResearchAttachment> getAttachmentList(Research research, Map<String, Object> params) throws Exception;
	
	public int getAttachmentCount(Research research) throws Exception;
	
	public int getAttachmentCount(Map<String, Object> params) throws Exception;
	
	public int getAttachmentCount(Research research, Map<String, Object> params) throws Exception;
	
	public ResearchAttachment deleteAttachment(ResearchAttachment attachment) throws Exception;
	
	public List<ResearchAttachment> deleteAttachment(Research research) throws Exception;
}
