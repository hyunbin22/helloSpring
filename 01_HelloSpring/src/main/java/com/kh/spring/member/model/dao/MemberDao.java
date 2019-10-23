package com.kh.spring.member.model.dao;

import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;

import com.kh.spring.member.model.vo.Member;

public interface MemberDao {

	Member selectMemberOne(SqlSessionTemplate session, Member m);
	int insertMember(SqlSessionTemplate session, Member m);
}
