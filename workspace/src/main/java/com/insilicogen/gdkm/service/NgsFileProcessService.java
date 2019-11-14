package com.insilicogen.gdkm.service;

import java.io.File;
import java.util.List;

import com.insilicogen.gdkm.model.NgsDataAchive;
import com.insilicogen.gdkm.model.NgsDataRegist;

public interface NgsFileProcessService {

	/**
	 * 데이터 등록 파일 검증 비동기 실행 메소드
	 * 서비스 모듈에서 발생하는 DB 처리는 처리 완료 후 DB auto commit 하지 않고 
	 * 내부 특점 시점에 commit 시키도록 구현해야 함. 
	 * 
	 * 파라미터 {@link NgsDataRegist}에 해당하는 모든 NGS 파일({@link NgsDataAchive}) 목록을 호출해 
	 * {@link NgsDataAchiveService#validateAchiveFile(NgsDataAchive)} 메소드를 호출 
	 * 
	 * @param regist	NGS 데이터 등록정보 
	 * @return	NGS 데이터 등록 성과물 
	 * @throws Exception
	 */
	
	public static final Object Lock = new Object();
	
	public void validateNgsData(List<NgsDataRegist> registList) throws Exception;
	
	public void validateAchiveFile(NgsDataAchive achive);
	
	public List<NgsDataAchive> unzipAchiveFile(NgsDataAchive achive);
	
	public void executeFastQC(NgsDataAchive achive);
	
	public void executeClcAssemblyCell(NgsDataAchive achive);
	
	public void parseGenbankFile(NgsDataAchive achive);
	
	public void buildBrowserTrack(NgsDataAchive achive);
	
	public void changeRegistStatus(NgsDataRegist regist, String status) throws Exception;
	
	public void changeVerifyStatus(NgsDataAchive achive, String status);
	
	public void createProcessLog(NgsDataAchive achive, String type, String status);
	
	public void createJbrowse(NgsDataAchive achive);
	
	public void createJbrowse(NgsDataAchive achive, File outputDir);
	
	public void startFileDelete();
}
