package com.insilicogen.gdkm.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.zip.GZIPInputStream;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.io.FilenameUtils;

/**
 * ex) 압축파일 엔트리 읽어 들이기 예제 
 * <pre>File file = new File("./samples/test_fastq.zip");
		
		ZipFile zip = new ZipFile(file);
		Enumeration<?> entries = zip.entries();
		while(entries.hasMoreElements()) {
			ZipEntry entry = (ZipEntry)entries.nextElement();
			System.out.println(entry.getName() + ", " + entry.getSize()  + ", " + entry.isDirectory());
		}</pre>
 */
public class ZipFileUtils {

	/**
	 * 압축파일(zippedFile)을 압축파일이 있는 폴더에 압축된 파일들을 해제
	 * 압축해제는 zip과 gzip 방식만 가능함.
	 * 
	 * ex) /temp/test.zip 파일을 해제하면 /temp 폴더에 test.zip 파일 안에 있는 파일들을 해제시킴
	 * 
	 * @param zippedFile	압축파일(zip, gzip)
	 * @return				압축이 해제된 파일 목록
	 * @throws IOException
	 */
	public static List<File> unzip(File zippedFile) throws IOException {
		return unzip(zippedFile, zippedFile.getParentFile());
	}

	/**
	 * 압축파일(zippedFile)을 특정대상 폴더(destDir)에 압축된 파일들을 해제
	 * 압축해제는 zip과 gzip 방식만 가능함.
	 * 
	 * ex) zippedFile : /temp/test.zip
	 *     destDir : /temp/source/
	 *      
	 * 압축을 해제하면 test.zip 으로 압축된 파일들을 /temp/source 폴더에 해제시킴
	 * 
	 * @param zippedFile	압축파일
	 * @param destDir		압축해제 대상 폴더
	 * @return				압축이 해제된 파일 목록
	 * @throws IOException
	 */
	public static List<File> unzip(File zippedFile, File destDir) throws IOException {
		String ext = FilenameUtils.getExtension(zippedFile.getAbsolutePath());
		if("gz".equalsIgnoreCase(ext) || "gzip".equalsIgnoreCase(ext)) {
			String filename = FilenameUtils.getBaseName(zippedFile.getAbsolutePath());
			File destFile = new File(destDir, filename);
			
			return unzip(new GZIPInputStream(new FileInputStream(zippedFile)), destFile);
		} else {
			return unzip(new FileInputStream(zippedFile), destDir, Charset.defaultCharset().name());
		}		
	}
	
	/**
	 * GZIP 방식으로 압축된 파일을 대상폴더(destFile)에 압축을 해제
	 * 단일 압축된 gzip 파일만 사용가능하며 파일 이름에 원본 파일의 전체 이름이 들어가 포함되어 있어야 함.
	 * 
	 * ex) /samples/test_fasta.fa.gz 파일을 /samples/test_fasta 폴더에 압축을 해제하는 경우  
	 *     /samples/test_fasta/test_fasta.fa 파일이 해제됨
	 * 
	 * @see #unzip(File, File)
	 * @param gis		GZIP파일 입력 스트림
	 * @param destFile	압축 해제 대상 폴더
	 * @return			압축이 해제된 파일 목록
	 * @throws IOException
	 */
	static List<File> unzip(GZIPInputStream gis, File destFile) throws IOException {
		int written = 0;
		byte[] buffer = new byte[1024 * 8];
		List<File> unzippedList = new ArrayList<File>();
		
		ensureDirectory(destFile.getParentFile());
		
		FileOutputStream fos = new FileOutputStream(destFile); 
		while((written=gis.read(buffer)) > -1)
			fos.write(buffer, 0, written);
		
		gis.close();
		fos.close();
		
		unzippedList.add(destFile);
		return unzippedList;
	}
	
	/**
	 * ZIP 방식으로 압축된 파일을 대상폴더(destFile)에 압축을 해제
	 * 
	 * ex) /samples/test_fastaq.zip 파일을 /samples/test_fastq 폴더에 압축을 해제하는 경우  
	 *     /samples/test_fasta/test_fastq_01.fa 과 /samples/test_fasta/test_fastq_02.fa 파일이 해제됨  
	 * 
	 * @see #unzip(File, File)
	 * @param is		ZIP파일의 입력 스티림
	 * @param destDir	압축해제 대상 폴더 
	 * @param encoding	압축해제 인코딩 방식(Default=UTF-8)
	 * @return			압축이 해제된 파일 목록
	 * @throws IOException
	 */
	static List<File> unzip(InputStream is, File destDir, String encoding) throws IOException {
		
		int written = 0;
		byte[] buffer = new byte[1024 * 8];
		List<File> unzippedList = new ArrayList<File>();
		
		ensureDirectory(destDir);
		
		ZipArchiveEntry entry = null;
		ZipArchiveInputStream zis = new ZipArchiveInputStream(is, encoding, false);
		
		while ((entry = zis.getNextZipEntry()) != null) {
			String name = entry.getName();
			File target = new File(destDir, name);
			
			if (entry.isDirectory()) {
				ensureDirectory(target);
			} else {
				target.createNewFile();
				BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(target));
				while ((written = zis.read(buffer)) >= 0) {
					bos.write(buffer, 0, written);
				}
				bos.close();
				
				unzippedList.add(target);
			}
		}
		
		zis.close();
		return unzippedList;
	}
	
	
	public static void zip(File src) throws IOException {
		zip(src, Charset.defaultCharset().name(), true);
	}

	/**
	 * zips the given file(or dir) and create 
	 * 
	 * @param src file or directory to compress 
	 * @param includeSrc if true and src is directory, then src is not included in the compression. if false, src is included. 
	 * @throws IOException
	 */
	public static void zip(File src, boolean includeSrc) throws IOException {
		zip(src, Charset.defaultCharset().name(), includeSrc);
	}

	/**
	 * compresses the given src file (or directory) with the given encoding 
	 * 
	 * @param src 
	 * @param charSetName 
	 * @param includeSrc 
	 * @throws IOException
	 */
	public static void zip(File src, String charSetName, boolean includeSrc) throws IOException {
		zip(src, src.getParentFile(), charSetName, includeSrc);
	}

	/**
	 * compresses the given src file(or directory) and writes to the given output stream. 
	 * 
	 * @param src 
	 * @param os 
	 * @throws IOException
	 */
	public static void zip(File src, OutputStream os) throws IOException {
		zip(src, os, Charset.defaultCharset().name(), true);
	}

	/**
	 * * compresses the given src file(or directory) and create the compressed file under the given destDir. 
	 * 
	 * @param src 
	 * @param destDir 
	 * @param charSetName 
	 * @param includeSrc 
	 * @throws IOException
	 */
	public static void zip(File src, File destDir, String charSetName, boolean includeSrc) throws IOException {
		String fileName = src.getName();
		if (!src.isDirectory()) {
			int pos = fileName.lastIndexOf(".");
			if (pos > 0) {
				fileName = fileName.substring(0, pos);
			}
		}
		
		fileName += ".zip";
		ensureDirectory(destDir);
		
		File zippedFile = new File(destDir, fileName);
		if (!zippedFile.exists())
			zippedFile.createNewFile();
		
		zip(src, new FileOutputStream(zippedFile), charSetName, includeSrc);
	}

	public static void zip(File src, OutputStream os, String charsetName, boolean includeSrc) throws IOException {
		ZipArchiveOutputStream zos = new ZipArchiveOutputStream(os);
		zos.setEncoding(charsetName);
		FileInputStream fis;
		int length;
		ZipArchiveEntry ze;
		byte[] buf = new byte[8 * 1024];
		String name;
		
		Stack<File> stack = new Stack<File>();
		File root;
		
		if (src.isDirectory()) {
			if (includeSrc) {
				stack.push(src);
				root = src.getParentFile();
			} else {
				File[] fs = src.listFiles();
				for (int i = 0; i < fs.length; i++) {
					stack.push(fs[i]);
				}
				root = src;
			}
		} else {
			stack.push(src);
			root = src.getParentFile();
		}
		
		while (!stack.isEmpty()) {
			File f = stack.pop();
			name = toPath(root, f);
			if (f.isDirectory()) {
				File[] fs = f.listFiles();
				for (int i = 0; i < fs.length; i++) {
					if (fs[i].isDirectory())
						stack.push(fs[i]);
					else
						stack.add(0, fs[i]);
				}
			} else {
				ze = new ZipArchiveEntry(name);
				zos.putArchiveEntry(ze);
				fis = new FileInputStream(f);
				while ((length = fis.read(buf, 0, buf.length)) >= 0) {
					zos.write(buf, 0, length);
				}
				fis.close();
				zos.closeArchiveEntry();
			}
		}
		zos.close();
	} 

	static void ensureDirectory(File dir) {
		if(!dir.exists())
			dir.mkdirs();
	}
	
	static String toPath(File root, File dir) {
		String path = dir.getAbsolutePath();
		path = path.substring(root.getAbsolutePath().length()).replace(File.separatorChar, '/');
		if (path.startsWith("/"))
			path = path.substring(1);
		
		if (dir.isDirectory() && !path.endsWith("/"))
			path += "/";
		
		return path;
	}
	
	public static void main(String[] args) throws IOException {
		File destDir = new File("./samples/test_fasta/");
		File zipFile = new File("./samples/test_fasta.fa.gz");
		
		List<File> files = ZipFileUtils.unzip(zipFile, destDir);
		for(File file : files) {
			System.out.println(file.getName() + " (" + file.length() + ")");
		}
		
		System.out.println(destDir.getAbsolutePath() + " :: exists=" + destDir.exists());
	}
}
