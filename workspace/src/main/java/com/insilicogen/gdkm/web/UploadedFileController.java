package com.insilicogen.gdkm.web;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.insilicogen.exception.RestTransferException;
import com.insilicogen.gdkm.model.Attachment;
import com.insilicogen.gdkm.util.UploadFileUtils;

@RestController
public class UploadedFileController extends AbstractController {

	static final int BUFF_SIZE = 4096;
	
	static Logger logger = LoggerFactory.getLogger(UploadedFileController.class);
	
	@RequestMapping(value="/temporaries", method=RequestMethod.POST)
	public List<Attachment> uploadTempFile(
			HttpServletRequest request, 
			HttpServletResponse response) throws Exception {
		
		if(!ServletFileUpload.isMultipartContent(request))
			throw new RestTransferException("Request is not multipart, please 'multipart/form-data' enctype for your form.");
		
		List<Attachment> uploadedList = new ArrayList<Attachment>();
		
		Map<String, MultipartFile> files = ((MultipartHttpServletRequest)request).getFileMap();
		Iterator<Entry<String, MultipartFile>> itr = files.entrySet().iterator();
		while(itr.hasNext()) {
			Entry<String, MultipartFile> entry = itr.next();
			MultipartFile file =  entry.getValue();
			if(StringUtils.isBlank(file.getOriginalFilename()))
				continue;
			
			Attachment uploadedFile = new Attachment();
			uploadedFile.setName(file.getOriginalFilename());
			uploadedFile.setType(file.getContentType());
			uploadedFile.setSize(file.getSize());
			
			File tempFile = writeFileToStorage(file);
			uploadedFile.setPath(tempFile.getName());
			
			uploadedList.add(uploadedFile);
		}
		
		return uploadedList;
	}
	
	@RequestMapping(value="/temporaries/{tempname}", method= RequestMethod.GET, produces=MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public void downloadTempFile(
			@PathVariable(value="tempname") String tempName, 
			@RequestParam(value="filename", required=false) String fileName, 
			HttpServletRequest request,  HttpServletResponse response) throws Exception {
		
		File tempFile = new File(UploadFileUtils.getTempDir(), tempName);
		if(StringUtils.isBlank(fileName))
			fileName = tempFile.getName();
		
		if(tempFile.exists()) {
			response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
			response.setContentLengthLong(tempFile.length());
			response.setHeader( "Content-Disposition", "attachment; filename=\"" + fileName + "\"" );
			
			response.getOutputStream().write(FileUtils.readFileToByteArray(tempFile));
		} else {
			throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "File not found - " + fileName); 
		}
	}
	
	@RequestMapping(value="/temporaries/{tempname}", method= RequestMethod.DELETE)
	public Attachment deleteTempFile(
			@PathVariable(value="tempname") String tempName, 
			@RequestParam(value="filename", required=false) String fileName) throws Exception {
		
		File tempFile = new File(UploadFileUtils.getTempDir(), tempName);
		if(StringUtils.isBlank(fileName))
			fileName = tempFile.getName();
		
		if(tempFile.exists()) {
			try {
				Attachment file = new Attachment();
				file.setName(fileName);
				file.setSize(tempFile.length());
				file.setPath(tempFile.getName());
				
				tempFile.delete();
				logger.debug("Remove temporary file-" + tempFile.getAbsolutePath());
				return file;
			} catch(Exception e) {
				logger.error(e.getMessage());
				throw new RestTransferException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		throw new RestTransferException("File not found - " + fileName, HttpStatus.NOT_FOUND);
	}
	
	static File writeFileToStorage(MultipartFile file) throws IOException {
		InputStream stream = null;
		OutputStream bos = null;
		File tempFile = null;
		
		try {
			tempFile = UploadFileUtils.getTempFile(); 
			stream = file.getInputStream();
			bos = new FileOutputStream(tempFile);

			int bytesRead = 0;
			byte[] buffer = new byte[BUFF_SIZE];

			while ((bytesRead = stream.read(buffer, 0, BUFF_SIZE)) != -1) {
				bos.write(buffer, 0, bytesRead);
			}
		} finally {	
			try {
				bos.close();
			} catch (Exception ignore) {
				logger.debug("IGNORED: " + ignore.getMessage());
			}
		
			try {
				stream.close();
			} catch (Exception ignore) {
				logger.debug("IGNORED: " + ignore.getMessage());
			}	
		}
		
		return tempFile;
	}
}
