package com.kh.spring.board.model.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.kh.spring.board.model.vo.Attachment;

@Repository
public class BoardDaoImpl implements BoardDao {

	@Override
	public List<Map<String, String>> selectList(SqlSessionTemplate session, int cPage, int numPerPage ) {
		// TODO Auto-generated method stub
		RowBounds rows=new RowBounds((cPage-1)*numPerPage,numPerPage);
		return session.selectList("board.selectList", null, rows);
	}

	@Override
	public int selectBoardCount(SqlSessionTemplate session) {
		// TODO Auto-generated method stub
		return session.selectOne("board.selectBoardCount");
	}
	
	//게시글등록
	@Override
	public int insertBoard(SqlSessionTemplate session, Map<String, String> param) {
		// TODO Auto-generated method stub
		return session.insert("board.insertBoard", param);
	}

	@Override
	public int insertAttachment(SqlSessionTemplate session, Attachment att) {
		// TODO Auto-generated method stub
		return session.insert("board.insertAttachment", att);
	}

	@Override
	public Map<String, String> selectBoard(SqlSessionTemplate session, int boardNo) {
		// TODO Auto-generated method stub
		return session.selectOne("board.selectBoard", boardNo);
	}

	@Override
	public List<Attachment> selectAttachList(SqlSessionTemplate session, int boardNo) {
		// TODO Auto-generated method stub
		return session.selectList("board.selectAttachList", boardNo);
	}
	
	
	
	
	

}
