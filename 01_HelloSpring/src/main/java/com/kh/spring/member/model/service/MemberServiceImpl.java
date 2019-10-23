package com.kh.spring.member.model.service;

import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kh.spring.member.model.dao.MemberDao;
import com.kh.spring.member.model.vo.Member;

@Service
public class MemberServiceImpl implements MemberService {

	@Autowired
	MemberDao dao;
	@Autowired
	SqlSessionTemplate session;
	
	@Override
	public Member selectMemberOne(Member m) {
		return dao.selectMemberOne(session, m);
	}

	@Override
	public int insertMember(Member m) {
		return dao.insertMember(session, m);
	}

}
