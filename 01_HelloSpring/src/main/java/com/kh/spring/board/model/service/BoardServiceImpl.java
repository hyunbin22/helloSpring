package com.kh.spring.board.model.service;

import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kh.spring.board.model.dao.BoardDao;
import com.kh.spring.board.model.vo.Attachment;

@Service
public class BoardServiceImpl implements BoardService {

	@Autowired
	BoardDao dao;
	@Autowired
	SqlSessionTemplate session;
	
	//게시판 리스트 불러오기
	@Override
	public List<Map<String, String>> selectList(int cPage, int numPerPage) {
		// TODO Auto-generated method stub
		return dao.selectList(session, cPage, numPerPage);
	}

	@Override
	public int selectBoardCount() {
		// TODO Auto-generated method stub
		return dao.selectBoardCount(session);
	}
	
	//게시글등록
	@Override
	@Transactional(rollbackFor = Exception.class)	//RuntimeException 발생시	, Exception은 안됨	()추가하면 여기서 에러나면 무조건 롤백
	public int insertBoard(Map<String, String> param, List<Attachment> attachList) throws Exception{
		int result = dao.insertBoard(session, param);	//board테이블에 데이터 입력
		if(result == 0) throw new Exception();	//트랜잭션 처리하기
		if(attachList.size() > 0) {
			for(Attachment a : attachList) {
				a.setBoardNo(Integer.parseInt(param.get("boardNo")));
				result = dao.insertAttachment(session, a);
				if(result == 0) throw new Exception();	//트랜잭션 처리하기
			}
		}
		
		return result;
	}

	@Override
	public Map<String, String> selectBoard(int boardNo) {
		// TODO Auto-generated method stub
		return dao.selectBoard(session, boardNo);
	}

	@Override
	public List<Attachment> selectAttachList(int boardNo) {
		// TODO Auto-generated method stub
		return dao.selectAttachList(session, boardNo);
	}
	
	
	
	

}
