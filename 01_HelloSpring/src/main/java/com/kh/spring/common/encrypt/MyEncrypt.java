package com.kh.spring.common.encrypt;

public interface MyEncrypt {
	String encrypt(String msg) throws Exception;
	String decrypt(String msg) throws Exception;
}
