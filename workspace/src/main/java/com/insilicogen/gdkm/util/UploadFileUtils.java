package com.insilicogen.gdkm.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.lang3.StringUtils;

public class UploadFileUtils {


	public static File getTempFile() throws IOException {
		return Files.createTempFile(getTempDirPath(), "gdkm", "temp").toFile();
	}
	
	public static File getTempDir() {
		return getTempDirPath().toFile();
	}
	
	public static Path getTempDirPath() {
		String rootPath = EgovProperties.getProperty("UPLOADFILE.ROOT.DIR");
		if(EgovProperties.ERR_CODE == rootPath) {
			try {
				return Files.createTempDirectory("gdkm");
			} catch (Exception e) {
				rootPath = "/data/gdkm";
			}
		}
		
		String tempPath = StringUtils.replace(rootPath, "\\", "/");
		if(StringUtils.indexOf(tempPath, ":") >= 0) {
			tempPath = StringUtils.substring(tempPath, 1);
		}
		
		Path tempDir = Paths.get(tempPath, "temporary"); 
		tempDir.toFile().mkdirs();
		
		return tempDir;
	}
	
}
