package com.kh.spring.member.model.vo;

import java.sql.Date;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor // 기본생성자
@Data	//멤버변수에 대한 setter / getter, 매개변수가 있는 생성자, toString, Equals, hashcode까지 다 만들어줌
@ToString
public class Member {
	private String userId;
	private String password;
	private String userName;
	private String gender;
	private int age;
	private String email;
	private String phone;
	private String address;
	private String[] hobby;
	private Date enrollDate;
}
