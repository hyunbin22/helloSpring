package com.kh.spring.board.model.dao;

import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;

import com.kh.spring.board.model.vo.Attachment;

public interface BoardDao {

	//게시판 리스트 불러오기
	List<Map<String, String>> selectList(SqlSessionTemplate session, int cPage, int numPerPage);
	int selectBoardCount(SqlSessionTemplate session);
	
	//게시글 등록
	int insertBoard(SqlSessionTemplate session, Map<String, String> param);
	int insertAttachment(SqlSessionTemplate session, Attachment att);
	//상세보기
	Map<String, String> selectBoard(SqlSessionTemplate session, int boardNo);
	List<Attachment> selectAttachList(SqlSessionTemplate session, int boardNo);

}
