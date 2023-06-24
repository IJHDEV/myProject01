package com.spring5.mypro01.service;

import com.spring5.mypro01.common.paging.domain.MyReplyPagingCreatorDTO;
import com.spring5.mypro01.common.paging.domain.MyReplyPagingDTO;
import com.spring5.mypro01.domain.MyReplyVO;

public interface MyReplyService {
	

	public MyReplyPagingCreatorDTO getReplyList(MyReplyPagingDTO myReplyPaging);
	
	public Long registerReplyForBoard(MyReplyVO myReply);
	
	public Long registerReplyForReply(MyReplyVO myReply);
	
	public MyReplyVO getReply(Long bno, Long rno);
	
	public int modifyReply(MyReplyVO myReply);
	
	public int removeReply(Long bno, Long rno);
	
	public int modifyRdelFlagReply(Long bno, Long rno);
	
//	public int removeAllReply(long bno);

//	public int modifyRdelFlagAllReply(long bno);
	
	
}
