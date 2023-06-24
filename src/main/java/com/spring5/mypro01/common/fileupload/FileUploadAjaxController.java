package com.spring5.mypro01.common.fileupload;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.spring5.mypro01.common.fileupload.domain.AttachFileDTO;

import net.coobird.thumbnailator.Thumbnailator;

@Controller
public class FileUploadAjaxController {
	private String repoPath = "C:/myupload";
	
	//1. 파일 업로드 요청 JSP 페이지 호출
	@GetMapping(value = "/fileUploadAjax")
	public String callFileUploadAjaxPage() {
		System.out.println("==[Ajax-요청-업로드] 페이지 표시==");
		return "sample/fileUploadAjax";
	}
	
	
	//이미지 파일인지 여부 판단
	private boolean checkIsImageFile(File uploadFile) {
		String contentType = null;
		
		try {
			contentType = Files.probeContentType(uploadFile.toPath());
			System.out.println("업로드 파일 타입: " + contentType);
			return contentType.startsWith("image");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	
	//날짜 형식 경로 문자열 생성 메서드
	private String getDateFmtPathName() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
		
		String dataFmtPathName = simpleDateFormat.format(new Date());
		
		return dataFmtPathName;
	}
	
	
	//2. 파일 업로드 처리
	@PostMapping(value = "/fileUploadAjaxAction",
				 produces = "application/json; charset=utf-8")
	@ResponseBody
	public ResponseEntity<List<AttachFileDTO>> fileUploadActionPost(MultipartFile[] uploadFiles) {
		
		List<AttachFileDTO> attachFileList = new ArrayList<AttachFileDTO>();
		
		String dateFmtPathName = getDateFmtPathName();
		
		File fileUploadPath = new File(repoPath, dateFmtPathName);
		
		if (!fileUploadPath.exists()) {
			fileUploadPath.mkdirs();
		}
		
		String uploadFileName = null;
		
		for (MultipartFile uploadFile : uploadFiles) {
			System.out.println("==========================");
			System.out.println("Upload File Name: " + uploadFile.getOriginalFilename());
			System.out.println("Upload File Size: " + uploadFile.getSize());
			
			AttachFileDTO attachFileDTO = new AttachFileDTO();
			
			uploadFileName = uploadFile.getOriginalFilename();
			uploadFileName = uploadFileName.substring(uploadFileName.lastIndexOf("\\") + 1);
			
			attachFileDTO.setFileName(uploadFileName);
			attachFileDTO.setUploadPath(dateFmtPathName);
			attachFileDTO.setRepoPath(repoPath);
			
			UUID uuid = UUID.randomUUID();
			uploadFileName = uuid.toString() + "_" + uploadFileName;
			
			attachFileDTO.setUuid(uuid.toString());
			
			File saveUploadFile = new File(fileUploadPath, uploadFileName);
			
			try {
				uploadFile.transferTo(saveUploadFile);
				
				if (checkIsImageFile(saveUploadFile)) {
					FileOutputStream fileOutputStream =
							new FileOutputStream(new File(fileUploadPath, "s_" + uploadFileName));
					
					Thumbnailator.createThumbnail(uploadFile.getInputStream(), fileOutputStream, 20, 20);
					fileOutputStream.close();
					attachFileDTO.setFileType("I");
				} else {
					attachFileDTO.setFileType("F");
				}
				
			} catch (IllegalStateException | IOException e) {
				System.out.println("error: " + e.getMessage());
			}
			
			attachFileList.add(attachFileDTO);
					
		} // for-end
		
		return new ResponseEntity<List<AttachFileDTO>> (attachFileList, HttpStatus.OK);
	}
	
	
	@GetMapping("/displayThumbnail")
	@ResponseBody
	public ResponseEntity<byte[]> sendTumbnailFile(String fileName) {
		System.out.println("fileName: " + fileName);
		File file = new File(fileName);
		
		ResponseEntity<byte[]> result = null;
		
		HttpHeaders header = new HttpHeaders();
		
		try {
			header.add("Content-Type", Files.probeContentType(file.toPath()));
			result = new ResponseEntity<byte[]>(FileCopyUtils.copyToByteArray(file), header, HttpStatus.OK);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	
	
	@PostMapping("/deleteFile")
	@ResponseBody
	public ResponseEntity<String> deleteFile(String fileName, String fileType) {
		
		try {
			fileName = URLDecoder.decode(fileName, "utf-8");
			System.out.println("Decoded_fileName: " + fileName);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		File delFile = new File(fileName);
		
		boolean delResult = delFile.delete();
		
		if (fileType.equals("I")) {
			delFile = new File(fileName.replace("s_", ""));
			
			boolean delResultForImage = delFile.delete();
			
			delResult = delResult && delResultForImage; 
			
		}
		
		return delResult ? new ResponseEntity<String>("S", HttpStatus.OK)
						 : new ResponseEntity<String>("F", HttpStatus.OK);
		
	}
	
	
}
