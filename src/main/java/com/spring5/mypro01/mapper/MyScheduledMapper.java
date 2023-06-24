package com.spring5.mypro01.mapper;

import java.util.List;

import com.spring5.mypro01.domain.MyBoardAttachFileVO;

public interface MyScheduledMapper {
	
	public List<MyBoardAttachFileVO> selectAttachFilesBeforeOneDay1();

	
	public List<MyBoardAttachFileVO> selectAttachFilesBeforeOneDay2();
	
	
	public List<String> selectDirs();

}
