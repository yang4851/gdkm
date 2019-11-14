package com.insilicogen.gdkm.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.insilicogen.gdkm.model.Research;
import com.insilicogen.gdkm.model.ResearchAttachment;

import egovframework.rte.psl.dataaccess.EgovAbstractMapper;

@Repository("ResearchAttachmentDAO")
public class ResearchAttachmentDAO extends EgovAbstractMapper{

	public int insertAttachment(ResearchAttachment attachment) throws Exception {
		if(attachment == null)
			throw new NullPointerException("Attachment data is null.");
		
		if(attachment.getResearch() == null || attachment.getResearch().getId() < 1)
			throw new NullPointerException("Research data of attachment is null");
		
		return insert("ResearchAttachment.insert", attachment);
	}
	
	public ResearchAttachment selectAttachment(int attachmentId) throws Exception {
		if(attachmentId < 1)
			throw new IllegalArgumentException("Invalid attachment ID-" + attachmentId);
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("attachmentId", attachmentId);
		
		return selectOne("ResearchAttachment.get", params);
	}
	
	public int updateAttachment(ResearchAttachment attachment) throws Exception {
		if(attachment == null)
			throw new NullPointerException("Attachment data is null.");
		
		if(attachment.getId() < 1)
			throw new IllegalArgumentException("Invalid attachment ID-" + attachment.getId());
		
		return update("ResearchAttachment.update", attachment);
	}
	
	public List<ResearchAttachment> selectAttachmentList(Research research) throws Exception {
		if(research == null || research.getId() < 1) 
			throw new IllegalArgumentException("Invalid research data");
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("researchId", research.getId());
		
		return selectAttachmentList(params);
	}
	
	public List<ResearchAttachment> selectAttachmentList(Map<String, Object> params) throws Exception {
		if(params == null)
			params = new HashMap<String, Object>();
		
		return selectList("ResearchAttachment.select", params);
	}
	
	public int selectAttachmentCount(Research research) throws Exception {
		if(research == null || research.getId() < 1) 
			throw new IllegalArgumentException("Invalid research data");
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("researchId", research.getId());
		
		return selectAttachmentCount(params);
	}
	
	public int selectAttachmentCount(Map<String, Object> params) throws Exception {
		if(params == null)
			params = new HashMap<String, Object>();
		
		return selectOne("ResearchAttachment.count", params);
	}
	
	public int deleteAttachment(ResearchAttachment attachment) throws Exception {
		if(attachment == null)
			throw new NullPointerException("Attachment data is null.");
		
		if(attachment.getId() < 1)
			throw new IllegalArgumentException("Invalid attachment ID-" + attachment.getId());
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("attachmentId", attachment.getId());
		
		return deleteAttachment(params);
	}
	
	public int deleteAttachment(Research research) throws Exception {
		if(research == null)
			throw new NullPointerException("Research data is null.");
		
		if(research.getId() < 1)
			throw new IllegalArgumentException("Invalid research data ID-" + research.getId());
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("researchId", research.getId());
		
		return deleteAttachment(params);
	}
	
	public int deleteAttachment(Map<String, Object> params) throws Exception {
		if(params == null || params.isEmpty())
			throw new IllegalArgumentException("Invalid remove attachment options.");
		
		return delete("ResearchAttachment.delete", params);
	}
}
