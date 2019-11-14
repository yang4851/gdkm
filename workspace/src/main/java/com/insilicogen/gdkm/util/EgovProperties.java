package com.insilicogen.gdkm.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * Class Name : EgovProperties.java Description : properties값들을 파일로부터 읽어와
 * Globals클래스의 정적변수로 로드시켜주는 클래스로 문자열 정보 기준으로 사용할 전역변수를 시스템 재시작으로 반영할 수 있도록 한다.
 * Modification Information
 *
 * 수정일 수정자 수정내용 ------- -------- --------------------------- 2009.01.19 박지욱 최초
 * 생성 2011.07.20 서준식 Globals파일의 상대경로를 읽은 메서드 추가
 * 
 * @author 공통 서비스 개발팀 박지욱
 * @since 2009. 01. 19
 * @version 1.0
 * @see
 *
 */

public class EgovProperties {

	// 프로퍼티값 로드시 에러발생하면 반환되는 에러문자열
	public static final String ERR_CODE = " EXCEPTION OCCURRED";
	public static final String ERR_CODE_FNFE = " EXCEPTION(FNFE) OCCURRED";
	public static final String ERR_CODE_IOE = " EXCEPTION(IOE) OCCURRED";

	// 파일구분자
	static final char FILE_SEPARATOR = File.separatorChar;

	// 프로퍼티 파일의 물리적 위치
	public static final String RELATIVE_PATH_PREFIX = EgovProperties.class.getResource("").getPath()
			.substring(0, EgovProperties.class.getResource("").getPath().lastIndexOf("classes")).replaceAll("%20", " ")
			+ "classes" + System.getProperty("file.separator");

	public static final String GLOBALS_PROPERTIES_FILE = RELATIVE_PATH_PREFIX + "egovframework"
			+ System.getProperty("file.separator") + "egovProps" + System.getProperty("file.separator")
			+ "globals.properties";

	/**
	 * 인자로 주어진 문자열을 Key값으로 하는 상대경로 프로퍼티 값을 절대경로로 반환한다(Globals.java 전용)
	 * 
	 * @param keyName String
	 * @return String
	 */
	public static String getPathProperty(String keyName) {
		String value = ERR_CODE;
		value = "99";
		debug(GLOBALS_PROPERTIES_FILE + " : " + keyName);
		FileInputStream fis = null;
		try {
			Properties props = new Properties();
			fis = new FileInputStream(EgovWebUtil.filePathBlackList(GLOBALS_PROPERTIES_FILE));
			props.load(new java.io.BufferedInputStream(fis));
			value = props.getProperty(keyName).trim();
			value = RELATIVE_PATH_PREFIX + "egovProps" + System.getProperty("file.separator") + value;
		} catch (FileNotFoundException fne) {
			debug(fne);
		} catch (IOException ioe) {
			debug(ioe);
		} catch (Exception e) {
			debug(e);
		} finally {
			try {
				if (fis != null)
					fis.close();
			} catch (Exception ex) {
				Logger.getLogger(EgovProperties.class).debug("IGNORED: " + ex.getMessage());
			}

		}
		
		return value;
	}

	/**
	 * 인자로 주어진 문자열을 Key값으로 하는 프로퍼티 값을 반환한다(Globals.java 전용)
	 * 
	 * @param keyName String
	 * @return String
	 */
	public static String getProperty(String keyName) {
		String value = ERR_CODE;
		debug("GLOBALS_PROPERTIES_FILE==" + GLOBALS_PROPERTIES_FILE + " : " + keyName);
		FileInputStream fis = null;
		try {
			Properties props = new Properties();
			fis = new FileInputStream(EgovWebUtil.filePathBlackList(GLOBALS_PROPERTIES_FILE));

			props.load(new java.io.BufferedInputStream(fis));
			value = props.getProperty(keyName).trim();
		} catch (FileNotFoundException fne) {
			debug("FileNotFoundException==" + fne);
		} catch (IOException ioe) {
			debug("IOException==" + ioe);
		} catch (Exception e) {
			debug("Exception==" + e);
		} finally {
			try {
				if (fis != null)
					fis.close();
			} catch (Exception ex) {
				Logger.getLogger(EgovProperties.class).debug("IGNORED: " + ex.getMessage());
			}

		}
		return value;
	}

	/**
	 * 주어진 파일에서 인자로 주어진 문자열을 Key값으로 하는 프로퍼티 상대 경로값을 절대 경로값으로 반환한다
	 * 
	 * @param fileName String
	 * @param key String
	 * @return String
	 */
	public static String getPathProperty(String fileName, String key) {
		FileInputStream fis = null;
		try {
			Properties props = new Properties();
			fis = new FileInputStream(EgovWebUtil.filePathBlackList(fileName));
			props.load(new java.io.BufferedInputStream(fis));
			fis.close();

			String value = props.getProperty(key);
			value = RELATIVE_PATH_PREFIX + "egovProps" + System.getProperty("file.separator") + value;
			return value;
		} catch (java.io.FileNotFoundException fne) {
			return ERR_CODE_FNFE;
		} catch (java.io.IOException ioe) {
			return ERR_CODE_IOE;
		} finally {
			try {
				if (fis != null)
					fis.close();
			} catch (Exception ex) {
				debug(ex); // 2011.10.10 보안점검 후속조치
			}
		}
	}

	/**
	 * 주어진 파일에서 인자로 주어진 문자열을 Key값으로 하는 프로퍼티 값을 반환한다
	 * 
	 * @param fileName String
	 * @param key String
	 * @return String
	 */
	public static String getProperty(String fileName, String key) {
		FileInputStream fis = null;
		try {
			Properties props = new Properties();
			fis = new FileInputStream(EgovWebUtil.filePathBlackList(fileName));
			props.load(new java.io.BufferedInputStream(fis));
			fis.close();

			String value = props.getProperty(key);
			return value;
		} catch (java.io.FileNotFoundException fne) {
			return ERR_CODE_FNFE;
		} catch (java.io.IOException ioe) {
			return ERR_CODE_IOE;
		} finally {
			try {
				if (fis != null)
					fis.close();
			} catch (Exception ex) {
				Logger.getLogger(EgovProperties.class).debug("IGNORED: " + ex.getMessage());
			}
		}
	}

	/**
	 * 주어진 프로파일의 내용을 파싱하여 (key-value) 형태의 구조체 배열을 반환한다.
	 * 
	 * @param property String
	 * @return ArrayList
	 */
	public static ArrayList<?> loadPropertyFile(String property) {

		// key - value 형태로 된 배열 결과
		ArrayList<Map<String, String>> keyList = new ArrayList<Map<String, String>>();

		String src = property.replace('\\', FILE_SEPARATOR).replace('/', FILE_SEPARATOR);
		FileInputStream fis = null;
		try {

			File srcFile = new File(EgovWebUtil.filePathBlackList(src));
			if (srcFile.exists()) {

				Properties props = new Properties();
				fis = new FileInputStream(src);
				props.load(new java.io.BufferedInputStream(fis));
				fis.close();

				Enumeration<?> plist = props.propertyNames();
				if (plist != null) {
					while (plist.hasMoreElements()) {
						Map<String, String> map = new HashMap<String, String>();
						String key = (String) plist.nextElement();
						map.put(key, props.getProperty(key));
						keyList.add(map);
					}
				}
			}
		} catch (Exception ex) {
			debug(ex); // 2011.10.10 보안점검 후속조치
		} finally {
			try {
				if (fis != null)
					try { fis.close(); } catch(Exception e) {}
			} catch (Exception ex) {
				Logger.getLogger(EgovProperties.class).debug("IGNORED: " + ex.getMessage());
			}
		}

		return keyList;
	}

	/**
	 * 시스템 로그를 출력한다.
	 * 
	 * @param obj Object
	 */
	private static void debug(Object obj) {
		if (obj instanceof java.lang.Exception) {
			Logger.getLogger(EgovProperties.class).debug("IGNORED: " + ((Exception) obj).getMessage());
		}
	}
}
