package com.kh.spring.memo.model.dao;

import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class MemoDaoImpl implements MemoDao {

	@Override
	public List<Map> selectList(SqlSessionTemplate session) {
		// TODO Auto-generated method stub
		return session.selectList("memo.selectList");
	}

}
