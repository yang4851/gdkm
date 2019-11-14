package com.insilicogen.gdkm.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.insilicogen.gdkm.Globals;
import com.insilicogen.gdkm.model.NgsDataAchive;
import com.insilicogen.gdkm.model.ResearchAttachment;
import com.insilicogen.gdkm.model.ResearchOmicsFile;

public class NgsFileUtils {

	static Logger logger = LoggerFactory.getLogger(NgsFileUtils.class);
	
	static final String DEFAULT_ACHIVE_EXTENSION_REGX = "(fasta|fa|fna|fastq|fq|gff|gff3|gbk|gbff|gb)";
	
	public static File getClcAssemblyCellFile() {
		String toolPath = EgovProperties.getProperty("TOOL.CLC_ASSEMBLY_CELL.PATH");
		if(Globals.isWindows() && !toolPath.matches(".+\\.exe")) {
			toolPath = toolPath + ".exe";
		}
		
		return new File(toolPath);
	}
	
	public static File getFastQcFile() {
		return new File(EgovProperties.getProperty("TOOL.FASTQC.PATH"));
	}
	
	public static String getMd5Checksum(File file) throws IOException {
		FileInputStream fis = null;
		String checksum = null; 
		
		try {
			fis = new FileInputStream(file);
			checksum = DigestUtils.md5Hex(fis);
		} finally {
			try {
				fis.close();
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
		}
		
		return checksum;
	}
	
	public static File getRootDir(NgsDataAchive achive) {
		String rootPath = EgovProperties.getProperty("UPLOADFILE.ROOT.DIR");
		if(StringUtils.isBlank(rootPath))
			rootPath = "/data";
		
		File dir = new File(rootPath);
		if(achive != null) {
			if(Globals.isProcessedData(achive.getDataRegist())) {
				dir = new File(rootPath, Globals.REGIST_DATA_PROCESSEDDATA); 
			} else {
				dir = new File(rootPath, Globals.REGIST_DATA_RAWDATA);
			}
		}
		
		return ensureDir(dir);
	}
	
	public static boolean isFastaFile(File file) {
		return isFastaFile(file.getName());
	}
	
	public static boolean isFastaFile(String fileName) {
		String ext = FilenameUtils.getExtension(fileName).toLowerCase();
		return ext.matches("(fasta|fa|fna)");
	}
	
	public static boolean isFastqFile(File file) {
		return isFastqFile(file.getName());
	}
	
	public static boolean isFastqFile(String fileName) {
		String ext = FilenameUtils.getExtension(fileName).toLowerCase();
		return ext.matches("(fastq|fq)");
	}

	public static boolean isGffFile(File file) {
		return isGffFile(file.getName());
	}
	
	public static boolean isGffFile(String fileName) {
		String ext = FilenameUtils.getExtension(fileName).toLowerCase();
		return ext.matches("(gff|gff3)");
	}
	
	public static boolean isZippedFile(File file) {
		return isZippedFile(file.getName());
	}
	
	public static boolean isZippedFile(String fileName) {
		String ext = FilenameUtils.getExtension(fileName).toLowerCase();
		return ext.matches("(zip|gz|gzip)");
	}
	
	public static boolean isGenbankFile(File file) {
		return isGenbankFile(file.getName());
	}
	
	public static boolean isGenbankFile(String fileName) {
		String ext = FilenameUtils.getExtension(fileName).toLowerCase();
		return ext.matches("(gbk|gbff|gb)");
	}
	
	public static boolean isH5File(File file) {
		return isH5File(file.getName());
	}
	
	public static boolean isH5File(String fileName) {
		String ext = FilenameUtils.getExtension(fileName).toLowerCase();
		return ext.matches("h5");
	}
	
	public static boolean isNgsAchiveFile(File file) {
		String regex = EgovProperties.getProperty("NGS.ACHIVE.FILE.REGEX");
		if(StringUtils.isBlank(regex)) 
			regex = DEFAULT_ACHIVE_EXTENSION_REGX;
		
		String ext = FilenameUtils.getExtension(file.getName()).toLowerCase();
		return ext.matches(regex);
	}

	public static String getFileType(File file) {
		return getFileType(file.getName());
	}
	
	public static String getFileType(String fileName) {
		if(isFastaFile(fileName))
			return Globals.FILE_TYPE_FASTA;
		
		if(isFastqFile(fileName))
			return Globals.FILE_TYPE_FASTQ;
		
		if(isGffFile(fileName))
			return Globals.FILE_TYPE_GFF;
		
		if(isGenbankFile(fileName))
			return Globals.FILE_TYPE_GBK;
		
		if(isZippedFile(fileName))
			return Globals.FILE_TYPE_ZIPPED;
		
		return Globals.FILE_TYPE_OTHER;
	}
	
	public static File ensureDir(File dir) {
		if(!dir.exists())
			dir.mkdirs();
		
		return dir;
	}
	
	/**
	 * 압축된 NGS 성과물 파일 압축해제 대상 폴더 반환
	 * 
	 *  폴더 경로와 이름은 압축 원본파일과 동일한 경로에 압축파일 이름으로 된 폴더에 압축을 해제함
	 *  
	 *  <pre>
	 *  (예제) 저장된 성과물 등록번호가 GDKM-N-BA-00001-01 인 경우 
	 *   > 성과물 파일 절대 경로 = /data/GDKM-N-BA-00001-01_achive_file.zip
	 *   > 압축파일 해제 폴더 = /data/GDKM-N-BA-00001-01_unzipped
	 *  </pre> 
	 *   
	 * @param achive	성과물 정보
	 * @return	성과물 압축해제 대상 폴더
	 */
	public static File getUnzippedDir(NgsDataAchive achive) {
		String dirName = EgovProperties.getProperty("UPLOADFILE.UNZIPPED.DIR");
		if(StringUtils.isBlank(dirName))
			dirName = "unzipped";
		
		dirName = achive.getRegistNo() + "_" + dirName;
		File unzippedDir = new File(getRootDir(achive), dirName);
		
		return ensureDir(unzippedDir);
	}
	
	public static File getClcOutputDir(NgsDataAchive achive) {
		String dirName = EgovProperties.getProperty("TOOL.CLC_ASSEMBLY_CELL.OUTPUT.DIR");
		if(StringUtils.isBlank(dirName))
			dirName = "seqinfo";
		
		dirName = achive.getRegistNo() + "_" + dirName;
		File outputDir = new File(getRootDir(achive), dirName);
		
		return ensureDir(outputDir);
	}
	
	public static File getFastQcOutputDir(NgsDataAchive achive) {
		String dirName = EgovProperties.getProperty("TOOL.FASTQC.OUTPUT.DIR");
		if(StringUtils.isBlank(dirName))
			dirName = "fastqc";
		
		dirName = achive.getRegistNo() + "_" + dirName;
		File outputDir = new File(getRootDir(achive), dirName);
		
		return ensureDir(outputDir);
	}
	
	public static File getGviewInputDir(NgsDataAchive achive) {
		String dirName = EgovProperties.getProperty("TOOL.GVIEW.INPUT.DIR");
		if(StringUtils.isBlank(dirName))
			dirName = "fastqc";
		
		dirName = achive.getRegistNo() + "_" + dirName;
		File outputDir = new File(getRootDir(achive), dirName);
		
		return ensureDir(outputDir);
	}

	public static File getJBrowserDir() {
		String dirName = EgovProperties.getProperty("TOOL.JBROWSER.PATH");
		if(StringUtils.isBlank(dirName))
			dirName = "/tomcat/apache-tomcat-8.5.31/webapps/ROOT/JBrowse-1.12.3";
		
		return new File(dirName);
	}
	
	public static File getPerpareRefSeqPerl() {
		File dir = new File(getJBrowserDir(), "bin");
		return new File(dir, "prepare-refseqs.pl");
	}
	
	public static File getFlatFileToJsonPerl() {
		File dir = new File(getJBrowserDir(), "bin");
		return new File(dir, "flatfile-to-json.pl");
	}
	
	public static File getGenerateNamesPerl() {
		File dir = new File(getJBrowserDir(), "bin");
		return new File(dir, "generate-names.pl");
	}
	
	public static File getJBrowserTrackFolder(NgsDataAchive achive) {
		File dir = new File(getJBrowserDir(), "/gdkm");
		return new File(dir, achive.getRegistNo());
	}
	
	public static File getFastQcSummaryFile(NgsDataAchive achive) {
		File dir = getFastQcOutputDir(achive);
		File summaryFile = findFileFromDir(dir, "summary.txt");
		if(summaryFile == null) {
			String dirName = achive.getRegistNo() + "_" + FilenameUtils.getBaseName(achive.getFileName()) + "_" + FilenameUtils.getExtension(achive.getFileName());
			dir = new File(dir, dirName);
			if(dir.isDirectory()) {
				summaryFile = new File(dir, "summary.txt");
			}
		}
			
		return summaryFile;
	}
	
	private static File findFileFromDir(File dir, String fileName) {
		File[] children = dir.listFiles();
		for(int i=0; i < children.length ; i++) {
			if(children[i].isFile()) { 
				if(fileName.equals(children[i].getName())) 
					return children[i];
			} else if(children[i].isDirectory()) {
				File file = findFileFromDir(children[i], fileName);
				if(file != null) 
					return file;
			}
		}
		
		return null;
	}
	
	/**
	 * 성과물이 저장될 폴더에 저장될 실제 파일을 반환
	 * 
	 *   파일이름 체계는 원본이름 앞에 성과물 등록번호를 붙이도록 함.
	 *   
	 *   <pre> 
	 *   (예제) {@link NgsDataAchive}의 등록번호(RegistNo)가 GDKM-N-BA-00001-01 인 경우
	 *     업로드 된 파일 이름 = uploaded_file.txt
	 *     저장되는 파일 이름 = GDKM-N-BA-00001-01_uploaded_file.txt
	 *   </pre>
	 * 
	 * @param achive	성과물 정보
	 * @return	성과물이 저장될 실제 파일
	 */
	public static File getPhysicalFile(NgsDataAchive achive) {
		String realName = achive.getFileName().trim();
		realName = achive.getRegistNo() + "_" + realName.replaceAll("\\s+", "_");
		
		return new File(getRootDir(achive), realName);
	}
	
	public static File getPhysicalFile(ResearchAttachment attachment) {
		String realName = attachment.getName().trim();
		realName = attachment.getRegistNo() + "_" + realName.replaceAll("\\s+", "_");
		
		String rootPath = EgovProperties.getProperty("UPLOADFILE.ROOT.DIR");
		if(StringUtils.isBlank(rootPath))
			rootPath = "/data";
		
		File dir = ensureDir(new File(rootPath, "research_attachment"));
		return new File(dir, realName);
	}
	
	public static File getPhysicalFile(ResearchOmicsFile omicsFile) {
		String realName = omicsFile.getName().trim();
		realName = omicsFile.getRegistNo() + "_" + realName.replaceAll("\\s+", "_");
		
		String rootPath = EgovProperties.getProperty("UPLOADFILE.ROOT.DIR");
		if(StringUtils.isBlank(rootPath))
			rootPath = "/data";
		
		File dir = ensureDir(new File(rootPath, "research_omics"));
		return new File(dir, realName);
	}
}
