package com.spring5.mypro01.common.filedownload;

import java.io.UnsupportedEncodingException;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class FileDownloadAjaxController {
	
	//20252525
	@GetMapping(value = "fileDownloadAjax",
				produces = "application/octet-stream")
	@ResponseBody
	public ResponseEntity<Resource> fileDownloadActionAjax(String fileName, @RequestHeader("User-Agent") String userAgent) {
		System.out.println("전달된 파일이름: " + fileName);
		Resource resource = new FileSystemResource(fileName);
		
		if(!resource.exists()) {
			return new ResponseEntity<Resource>(HttpStatus.NOT_FOUND);
		}
		
		String downloadName = resource.getFilename();
		
		downloadName = downloadName.substring(downloadName.indexOf("_") + 1);
		
		HttpHeaders httpHeaders = new HttpHeaders();
		
		try {
			if(userAgent.contains("Trident") || userAgent.contains("MSIE") ||
				userAgent.contains("Edge") || userAgent.contains("Edg")) {
				
				downloadName = new String(downloadName.getBytes("utf-8"), "ISO-8859-1");
				System.out.println("MS브라우저: " + downloadName);
				
			} else {
				downloadName = new String(downloadName.getBytes("utf-8"), "ISO-8859-1");
				System.out.println("MS 이외의 브라우저: " + downloadName);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		httpHeaders.add("Content-Disposition", "attachment; fileName=" + downloadName);
		
		return new ResponseEntity<Resource>(resource, httpHeaders, HttpStatus.OK);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
