package com.spring5.mypro01.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spring5.mypro01.common.paging.domain.MyBoardPagingDTO;
import com.spring5.mypro01.domain.MyBoardAttachFileVO;
import com.spring5.mypro01.domain.MyBoardVO;
import com.spring5.mypro01.mapper.MyBoardAttachFileMapper;
import com.spring5.mypro01.mapper.MyBoardMapper;
import com.spring5.mypro01.mapper.MyReplyMapper;

import lombok.AllArgsConstructor;

//@Log4j
@Service
@AllArgsConstructor
public class MyBoardServiceImpl implements MyBoardService {

	private MyBoardMapper myBoardMapper;
	private MyReplyMapper myReplyMapper;
	private MyBoardAttachFileMapper myAttachFileMapper;

	@Override
	public List<MyBoardVO> getBoardList(MyBoardPagingDTO myBoardPaging) {
		return myBoardMapper.selectMyBoardList(myBoardPaging);
	}

	@Override
	public long getRowTotal(MyBoardPagingDTO myBoardPaging) {
		return myBoardMapper.selectRowTotal(myBoardPaging);
	}

	@Transactional
	@Override
	public long registerBoard(MyBoardVO board) {
		myBoardMapper.insertMyBoardSelectKey(board);

		if (board.getAttachFileList() == null || board.getAttachFileList().size() == 0) {
			return board.getBno();
		}

		board.getAttachFileList().forEach(attachFile -> {
			attachFile.setBno(board.getBno());
			myAttachFileMapper.insertAttachFile(attachFile);
		});
		return board.getBno();
	}

	@Override
	public MyBoardVO getBoard(long bno) {
		MyBoardVO board = myBoardMapper.selectMyBoard(bno);
		myBoardMapper.updateBviewsCnt(bno);
		return board;
	}

	@Override
	public MyBoardVO getBoardDetailModify(long bno) {
		return myBoardMapper.selectMyBoard(bno);
	}

	// 특정 게시물의 첨부파일 목록 조회
	@Override
	public List<MyBoardAttachFileVO> getAttachFiles(long bno) {
		return myAttachFileMapper.selectAttachFiles(bno);
	}

	@Transactional
	@Override
	public boolean modifyBoard(MyBoardVO board) {
		long bno = board.getBno();
		
		myAttachFileMapper.deleteAttachFiles(bno);
		boolean boardModifyResult = myBoardMapper.updateMyBoard(board) == 1;
		
		if(boardModifyResult && board.getAttachFileList().size() > 0) {
			
			board.getAttachFileList().forEach(attachFile -> {
				attachFile.setBno(bno);
				myAttachFileMapper.insertAttachFile(attachFile);
				
			});
		}
		
		return boardModifyResult;
	}

	@Transactional
	@Override
	public boolean setBoardDeleted(long bno) {
		myReplyMapper.updateRdelFlagAllReply(bno);
		return myBoardMapper.updateSetDelFlag(bno) == 1;
	}

	@Transactional
	@Override
	public boolean removeBoard(long bno) {
		int delCnt = myReplyMapper.deleteAllReply(bno);
		System.out.println("삭제된 댓글 총 수: " + delCnt);
		return myBoardMapper.deleteMyBoard(bno) == 1;
	}

}
