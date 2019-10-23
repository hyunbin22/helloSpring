package com.kh.spring.board.model.vo;

import java.sql.Date;

import lombok.Data;

@Data
public class Attachment {
	
	private int attachmentNo;
	private int boardNo;
	private String originalFileName;
	private String renamedFileName;
	private Date uploadDate;
	private String status;
	private int downloadCount;
}
