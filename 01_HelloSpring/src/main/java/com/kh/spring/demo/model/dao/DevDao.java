package com.kh.spring.demo.model.dao;

import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;

import com.kh.spring.demo.model.vo.Dev;

public interface DevDao {

	int insertDev(SqlSessionTemplate sqlSession, Dev dev);
	List<Dev> selectDevList(SqlSessionTemplate sqlSession);
	int deleteDev(SqlSessionTemplate sqlSession, int devNo);

}
