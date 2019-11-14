package com.insilicogen.gdkm.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.FileCopyUtils;
import org.springframework.web.servlet.view.AbstractView;

public class FileDownloadUtil extends AbstractView {
	public FileDownloadUtil() {
		 super.setContentType("application/octet-stream");
	}

	@Override
	protected void renderMergedOutputModel(Map model, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String fileName = (String) model.get("fileName");
		File file = (File) model.get("file");
		String header = request.getHeader("User-Agent");
		if (header.contains("MSIE")) {
			String docName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
			response.setHeader("Content-Disposition", "attachment;filename=" + docName + ";");
		} else if (header.contains("Firefox")) {
			String docName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
			response.setHeader("Content-Disposition", "attachment; filename=\"" + docName + "\"");
		} else if (header.contains("Opera")) {
			String docName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
			response.setHeader("Content-Disposition", "attachment; filename=\"" + docName + "\"");
		} else if (header.contains("Chrome")) {
			String docName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
			response.setHeader("Content-Disposition", "attachment; filename=\"" + docName + "\"");
		}

		response.setContentType("application/octet-stream; charset=utf-8");
		response.setContentLength((int) file.length());
		response.setHeader("Content-Transfer-Encoding", "binary");
		OutputStream out = response.getOutputStream();
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			FileCopyUtils.copy(fis, out);
		} catch (java.io.IOException ioe) {
			ioe.printStackTrace();
		} finally {
			if (fis != null)
				fis.close();
			if (out != null)
				out.close();
		}
	}

}
