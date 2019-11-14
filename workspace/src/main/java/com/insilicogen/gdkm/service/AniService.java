package com.insilicogen.gdkm.service;

import java.io.File;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import com.insilicogen.gdkm.model.AniFile;
import com.insilicogen.gdkm.model.AniParameter;
import com.insilicogen.gdkm.model.AniResult;
import com.insilicogen.gdkm.model.NgsFileSeqQuantity;

public interface AniService {
	
	public int run(OutputStream outputStream, OutputStream errorStream, AniParameter parameter) throws Exception;
	public AniResult resultTabFileParse(File target) throws Exception;
	public List<AniFile> selectAchiveOfFasta(Map<String, Object> params) throws Exception;
	public Integer countAchiveOfFasta(Map<String, Object> params) throws Exception;
	public NgsFileSeqQuantity executeClcAssemblyCell(File target);
}
