package com.spring5.mypro01.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class MyBoardAttachFileVO {
	private String uuid;
	private String uploadPath;
	private String fileName;
	private String fileType;
	private long bno;
	private String repoPath = "C:/myupload";
}
