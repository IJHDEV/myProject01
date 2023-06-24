package com.spring5.mypro01.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.spring5.mypro01.common.paging.domain.MyBoardPagingDTO;
import com.spring5.mypro01.domain.MyBoardVO;

public interface MyBoardMapper {
	//CRUD
	
	//목록조회
	public List<MyBoardVO> selectMyBoardList(MyBoardPagingDTO myBoardPaging);
	
	//게시물 총 수 조회
	public long selectRowTotal(MyBoardPagingDTO myBoardPaging);
	
	//게시물 등록1(w/o selectKey)
	public void insertMyBoard(MyBoardVO board);
	
	//게시물 등록2(w selectKey)
	public long insertMyBoardSelectKey(MyBoardVO board);
	
	//특정 게시물 조회
	public MyBoardVO selectMyBoard(long bno);
	
	//특정 게시물 수정
	public int updateMyBoard(MyBoardVO board);
	
	//게시물의 bdelFlag = 1로 설정
	public int updateSetDelFlag(long bno);
	
	//특정 게시물 삭제
	public int deleteMyBoard(long bno);
	
	//게시물 조회수 증가
	public int updateBviewsCnt(long bno);
	
	//댓글 수 변경: 댓글 추가시에 amount에 1, 댓글삭제 시 amount에 -1 전달
	public void updateBReplyCnt(@Param("bno") Long bno, @Param("amount") int amount);
	
	
	
}
