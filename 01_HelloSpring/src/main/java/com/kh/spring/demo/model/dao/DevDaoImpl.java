package com.kh.spring.demo.model.dao;

import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.kh.spring.demo.model.vo.Dev;

@Repository
public class DevDaoImpl implements DevDao {

	@Override
	public int insertDev(SqlSessionTemplate sqlSession, Dev dev) {

		return sqlSession.insert("dev.insertDev", dev);
	}

	@Override
	public List<Dev> selectDevList(SqlSessionTemplate sqlSession) {
		// TODO Auto-generated method stub
		return sqlSession.selectList("dev.selectDevList");
	}

	@Override
	public int deleteDev(SqlSessionTemplate sqlSession, int devNo) {
		// TODO Auto-generated method stub
		return sqlSession.delete("dev.deleteDev", devNo);
	}
}
