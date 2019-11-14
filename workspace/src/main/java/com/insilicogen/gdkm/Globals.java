package com.insilicogen.gdkm;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.insilicogen.gdkm.model.AbstractIdObject;
import com.insilicogen.gdkm.model.NgsDataRegist;
import com.insilicogen.gdkm.model.Research;
import com.insilicogen.gdkm.model.User;

public class Globals {
    
	public static final String REGIST_DATA_RAWDATA = "rawdata";	
	public static final String REGIST_DATA_PROCESSEDDATA = "processed";
	
	public static final String STATUS_APPROVAL_ACCEPT = "accept";
	public static final String STATUS_APPROVAL_REJECT = "reject";
	
	public static final String STATUS_OPEN_Y = "Y";
	public static final String STATUS_OPEN_N = "N";
	
	public static final String STATUS_REGIST_READY = "ready"; //등록접수
	public static final String STATUS_REGIST_VALIDATING = "validating"; //검증중
	public static final String STATUS_REGIST_VALIDATED = "validated"; //검증완료
	public static final String STATUS_REGIST_ERROR = "error"; //파일에러
	public static final String STATUS_REGIST_SUBMIT = "submit"; //접수중
	public static final String STATUS_REGIST_FAILED = "failed"; //등록실패
	public static final String STATUS_REGIST_SUCCESS = "success"; //승인완료 
	

	static final String[] REGIST_STATUS = new String[] {
		Globals.STATUS_REGIST_READY,
		Globals.STATUS_REGIST_VALIDATING,
		Globals.STATUS_REGIST_VALIDATED,
		Globals.STATUS_REGIST_ERROR,
		Globals.STATUS_REGIST_SUBMIT, 
		Globals.STATUS_REGIST_FAILED,
		Globals.STATUS_REGIST_SUCCESS
	};
	
	public static final List<String> REGIST_STATUS_LIST = Arrays.asList(REGIST_STATUS);
	
	public static final String STATUS_VERIFY_READY = "ready";
	public static final String STATUS_VERIFY_RUNNING = "running";
	public static final String STATUS_VERIFY_SUCCESS = "success";
	public static final String STATUS_VERIFY_WARN = "warn";
	public static final String STATUS_VERIFY_FAILED = "failed";
	
	// NGS 데이터 파일 종류 
	public static final String FILE_TYPE_FASTA = "fasta";
	public static final String FILE_TYPE_FASTQ = "fastq";
	public static final String FILE_TYPE_GFF = "gff";
	public static final String FILE_TYPE_GBK = "gbk";
	public static final String FILE_TYPE_ZIPPED = "zipped";
	public static final String FILE_TYPE_OTHER = "other";
	
	public static final String PROCESS_TYPE_UNZIP = "unzip";
	public static final String PROCESS_TYPE_FASTQC = "fastQC";
	public static final String PROCESS_TYPE_SEQINFO = "seqinfo";
	public static final String PROCESS_TYPE_GENBANK = "genbank";
	public static final String PROCESS_TYPE_GFF = "gff";
	
	public static final String PARAM_PAGE = "page";
	public static final String PARAM_ROW_SIZE = "rowSize";
	public static final String PARAM_FIRST_INDEX = "firstIndex";
	public static final String PARAM_IS_BASICFORM = "isBasicForm";
	
	public static final String PARAM_OPEN_YN = "openYn";
	public static final String PARAM_ACTION = "action";
	public static final String PARAM_ACTION_APPEND = "append";
	public static final String PARAM_ACTION_REMOVE = "remove";
	public static final String PARAM_ACTION_OPEN = "open";
	public static final String PARAM_ACTION_VERIFY = "verify";
	public static final String PARAM_ACTION_SUBMIT = "submit";
	public static final String PARAM_ACTION_REJECT = "reject";
	public static final String PARAM_ACTION_APPROVE = "approve";
	
	public static final String PARAM_SECTION = "section";
	public static final String PARAM_SECTION_REGIST = "regist";
	public static final String PARAM_SECTION_SYSTEM = "system";
	public static final String PARAM_SECTION_PUBLIC = "public";
	
	public static final String LOGIN_USER = "LOGIN_USER";
	
	public static boolean isProcessedData(NgsDataRegist regist) {
		if(regist != null && REGIST_DATA_PROCESSEDDATA.equals(regist.getDataType())) {
			return (regist.getProcessedData() != null);
		}
		
		return false;
	}
	
	public static boolean isRawData(NgsDataRegist regist) {
		if(regist != null && REGIST_DATA_RAWDATA.equals(regist.getDataType())) {
			return (regist.getRawData() != null);
		}
		
		return false;
	}
	
	public static boolean isWindows() {
		String os = System.getProperty("os.name").toLowerCase();
		return (os.indexOf("win") >= 0);
	}
	
	public static boolean isValidModel(AbstractIdObject obj) {
		return (obj != null && obj.getId() > 0);
	}
	
	public static boolean isValidUser(User user) {
		return (user != null && StringUtils.isNotBlank(user.getUserId()));
	}
	
	public static boolean isOpenData(NgsDataRegist regist) {
		if(!STATUS_REGIST_SUCCESS.equals(regist.getRegistStatus()))
			return false;
				
		return STATUS_OPEN_Y.equals(regist.getOpenYn());
	}
	
	public static boolean isOpenData(Research research) {
		if(!STATUS_REGIST_SUCCESS.equals(research.getRegistStatus()))
			return false;
				
		return STATUS_OPEN_Y.equals(research.getOpenYn());
	}
	
	public static boolean isAcceptedData(NgsDataRegist regist) {
		return STATUS_APPROVAL_ACCEPT.equals(regist.getApprovalStatus());
	}
	
	public static boolean isVerifiRunnable(NgsDataRegist regist) {
		return (!STATUS_REGIST_VALIDATING.equals(regist.getRegistStatus())
				&& !STATUS_REGIST_SUBMIT.equals(regist.getRegistStatus())
				&& !STATUS_REGIST_FAILED.equals(regist.getRegistStatus())
				&& !STATUS_REGIST_SUCCESS.equals(regist.getRegistStatus())); 
	}
	
	public static boolean isVerifiRunnable(Research research) {
		return (!STATUS_REGIST_VALIDATING.equals(research.getRegistStatus())
				&& !STATUS_REGIST_FAILED.equals(research.getRegistStatus())
				&& !STATUS_REGIST_SUCCESS.equals(research.getRegistStatus())); 
	}
}
