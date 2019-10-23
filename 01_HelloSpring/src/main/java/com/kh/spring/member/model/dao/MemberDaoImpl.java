package com.kh.spring.member.model.dao;

import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.kh.spring.member.model.vo.Member;

@Repository
public class MemberDaoImpl implements MemberDao {

	@Override
	public Member selectMemberOne(SqlSessionTemplate session, Member m) {
		// TODO Auto-generated method stub
		return session.selectOne("member.selectMemberOne", m);
	}

	@Override
	public int insertMember(SqlSessionTemplate session, Member m) {
		// TODO Auto-generated method stub
		return session.insert("member.insertMember", m);
	}

}
