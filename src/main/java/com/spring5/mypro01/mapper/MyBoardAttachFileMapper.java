package com.spring5.mypro01.mapper;

import java.util.List;

import com.spring5.mypro01.domain.MyBoardAttachFileVO;

public interface MyBoardAttachFileMapper {
	
	//특정 게시물의 첨부파일 목록 조회
	public List<MyBoardAttachFileVO> selectAttachFiles(long bno);
	
	
	//특정 게시물의 첨부파일 정보 입력
	public void insertAttachFile(MyBoardAttachFileVO attachFile);
	
	
	//특정 게시물의 첨부파일 하나를 삭제
	public void deleteAttachFile(String uuid);
	
	
	//특정 게시물의 모든 첨부파일 삭제
	public void deleteAttachFiles(Long bno);
}
