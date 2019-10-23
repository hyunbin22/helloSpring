package com.kh.spring.board.model.service;

import java.util.List;
import java.util.Map;

import com.kh.spring.board.model.vo.Attachment;

public interface BoardService {

	//게시판리스트 불러오기
	List<Map<String, String>> selectList(int cPage, int numPerPage);
	int selectBoardCount();
	//게시글등록
	int insertBoard(Map<String, String> param, List<Attachment> attachList) throws Exception;
	//상세보기
	Map<String, String> selectBoard(int boardNo);
	List<Attachment> selectAttachList(int boardNo);

}
