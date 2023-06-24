package com.spring5.mypro01.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.spring5.mypro01.common.paging.domain.MyBoardPagingCreatorDTO;
import com.spring5.mypro01.common.paging.domain.MyBoardPagingDTO;
import com.spring5.mypro01.domain.MyBoardAttachFileVO;
import com.spring5.mypro01.domain.MyBoardVO;
import com.spring5.mypro01.service.MyBoardService;

@Controller
@RequestMapping("/myboard")
public class MyBoardController {
	private MyBoardService myBoardService;
	
	//단일생성자 주입방식
	public MyBoardController(MyBoardService myBoardService) {
		this.myBoardService = myBoardService;
	}
	
	//Setter 주입방식
//	public MyBoardController() {
//		System.out.println("MyBoardController 기본생성자");
//	}
//	
//	@Autowired
//	public void setMyBoardService(MyBoardService myBoardService) {
//		this.myBoardService = myBoardService;
//	}
	
	//목록 조회 GET /myboard/list 
	//게시물 목록 조회: http://localhost:8080/mypro01/myboard/list
	@GetMapping("/list")
	public void showBoardList(MyBoardPagingDTO myBoardPaging, Model model) {
		model.addAttribute("boardList", myBoardService.getBoardList(myBoardPaging));
		
		long rowTotal = myBoardService.getRowTotal(myBoardPaging);
		
		MyBoardPagingCreatorDTO myBoardPagingCreator = 
				new MyBoardPagingCreatorDTO(rowTotal, myBoardPaging);
		
		System.out.println("컨트롤러, myBoardPagingCreator: " + myBoardPagingCreator);
		
		model.addAttribute("pagingCreator", myBoardPagingCreator);
		
	}
	
	
	//등록 페이지 호출 GET /myboard/register 목록페이지에서 등록페이지로 이동
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/register")
	public void showBoardRegisterPage() {
	}
	
	
	//등록 처리 POST /myboard/register 등록처리 후 목록페이지로 이동
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/register")
	public String registerNewBoard(MyBoardVO board, RedirectAttributes rttr) {
		long bno = myBoardService.registerBoard(board);
		System.out.println("===============첨부파일 정보===============");
		
		if (board.getAttachFileList() != null) {
			board.getAttachFileList()
				 .forEach(attachFile -> System.out.println(attachFile.toString()));
		} else {
			System.out.println("<<<<<<<<<<<<<<<첨부파일 없음>>>>>>>>>>>>>>>");
		}
		System.out.println("========================================");
		
		rttr.addFlashAttribute("result", bno);
		
		return "redirect:/myboard/list";
	}
	
	
	//특정 게시물 조회 GET /myboard/detail 목록페이지에서 조회페이지 호출(내용표시 및 조회수 1 증가)
	@GetMapping("/detail")
	public void showBoardDetail(Long bno, MyBoardPagingDTO myBoardPaging, Model model) {
		model.addAttribute("board", myBoardService.getBoard(bno));
		model.addAttribute("myBoardPaging", myBoardPaging);
	}
	
	
	//수정 후 --> 조회페이지 호출
	@GetMapping("/detailmod")
	public String showBoardDetailModify(Long bno, MyBoardPagingDTO myBoardPaging, Model model) {
		model.addAttribute("board", myBoardService.getBoardDetailModify(bno));
		model.addAttribute("myBoardPaging", myBoardPaging);
		
		return "/myboard/detail";
	}
	
	
	@GetMapping(value = "/getFiles",
			produces = "application/json;charset=utf-8")
	@ResponseBody
	public ResponseEntity<List<MyBoardAttachFileVO>> showAttachFiles(Long bno) {
		return new ResponseEntity<List<MyBoardAttachFileVO>>(myBoardService.getAttachFiles(bno), HttpStatus.OK);
	}
	
	
	//조회 --> 수정페이지 호출
	@GetMapping("/modify")
	public void showBoardModify(Long bno, MyBoardPagingDTO myBoardPaging, Model model) {
		model.addAttribute("board", myBoardService.getBoardDetailModify(bno));
		model.addAttribute("myBoardPaging", myBoardPaging);
	}
	
	
	//특정 게시물 수정 POST /myboard/modify 수정 처리 후 조회 페이지 이동
	@PostMapping("modify")
	public String modifyBoard(MyBoardVO board, MyBoardPagingDTO myBoardPaging, RedirectAttributes rttr) {
		if (myBoardService.modifyBoard(board)) {
			rttr.addFlashAttribute("result", "successModify");
		} else {
			rttr.addFlashAttribute("result", "failModify");
		}
		rttr.addAttribute("bno", board.getBno());
//		rttr.addAttribute("pageNum", myBoardPaging.getPageNum());
//		rttr.addAttribute("rowAmountPerPage", myBoardPaging.getRowAmountPerPage());
		
		return "redirect:/myboard/detailmod" + myBoardPaging.addPagingParamsToURI();
	}
	
	
	//특정 게시물 삭제요청 POST /myboard/delete 삭제요청 처리 후 목록페이지 이동
	@PostMapping("/delete")
	public String deleteBoard(Long bno, MyBoardPagingDTO myBoardPaging, RedirectAttributes rttr) {
		
		List<MyBoardAttachFileVO> attachFileList = myBoardService.getAttachFiles(bno);
		
		if (myBoardService.setBoardDeleted(bno)) {
			removeAttachFiles(attachFileList);
			rttr.addFlashAttribute("result", "successRemove");
		} else {
			rttr.addFlashAttribute("result", "failRemove");
		}
//		rttr.addAttribute("pageNum", myBoardPaging.getPageNum());
//		rttr.addAttribute("rowAmountPerPage", myBoardPaging.getRowAmountPerPage());
		
		return "redirect:/myboard/list" + myBoardPaging.addPagingParamsToURI();
	}
	
	
	//특정 게시물 삭제 POST /myboard/remove 삭제 처리 후 목록페이지 이동
	@PostMapping("remove")
	public String removeBoard(Long bno, MyBoardPagingDTO myBoardPaging, RedirectAttributes rttr) {
		
		List<MyBoardAttachFileVO> attachFileList = myBoardService.getAttachFiles(bno);
		
		if (myBoardService.removeBoard(bno)) {
			removeAttachFiles(attachFileList);
			rttr.addFlashAttribute("result", "successRemove");
		} else {
			rttr.addFlashAttribute("result", "failRemove");
		}
//		rttr.addAttribute("pageNum", myBoardPaging.getPageNum());
//		rttr.addAttribute("rowAmountPerPage", myBoardPaging.getRowAmountPerPage());
		
		return "redirect:/myboard/list" + myBoardPaging.addPagingParamsToURI(); 
	}
	
	
	private void removeAttachFiles(List<MyBoardAttachFileVO> attachFileList) {
		if (attachFileList == null || attachFileList.size() == 0) {
			return;
		}
		System.out.println("삭제 시작: 삭제파일 목록================\n" + attachFileList.toString());
		
		attachFileList.forEach(attachFile -> {
			
			try {
				Path filePath = Paths.get(attachFile.getRepoPath() + "/" +
						  attachFile.getUploadPath() + "/" +
						  attachFile.getUuid() + "_" +
						  attachFile.getFileName()
						 );
				
				Files.deleteIfExists(filePath);
				
				if(attachFile.getFileType().equals("I")) {
					Path thumbnail = Paths.get(attachFile.getRepoPath() + "/" +
											   attachFile.getUploadPath() + "/s_" +
											   attachFile.getUuid() + "_" +
											   attachFile.getFileName()
										  	  );
					Files.deleteIfExists(thumbnail);
				}
				
			} catch (IOException e) {
				System.out.println("파일삭제오류: " + e.getMessage());
			}
		});
	}
	
}
