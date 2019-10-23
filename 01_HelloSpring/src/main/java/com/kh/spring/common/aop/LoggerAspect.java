package com.kh.spring.common.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class LoggerAspect {

	private Logger logger = LoggerFactory.getLogger(LoggerAspect.class);
	
	// 특정 메소드 실행 전|후|전/후에 실행할 로직 
	// AOP를 적용하기위해선 또하나의 라이브러리가 필요함.
	// around방식(전후처리) * 전처리만, 후처리만 가능
	public Object loggerAdvice(ProceedingJoinPoint joinPoint) throws Throwable{
		
		Signature sig = joinPoint.getSignature();
		//logger.debug("[signature]" + sig);
		String type=sig.getDeclaringTypeName();
		String methodName = sig.getName();
		String componentName = "";
		if(type.indexOf("Controller") >= -1) {
			componentName = "Controller";
		} else if(type.indexOf("Service") >= -1) {
			componentName = "ServiceImpl";
		} else if(type.indexOf("Dao") >= -1) {
			componentName = "DaoImpl";
		}
		
		logger.debug("[Before]" + componentName + type + "." + methodName + "()");
		
		//return joinPoint.proceed();
		//joinPoint.proceed()를 기준으로 전처리, 후처리가 나눠짐
		//전처리 끝
		Object obj = joinPoint.proceed();
		//후처리 시작
		logger.debug("[After]" + componentName + type + "." + methodName + "()");
		
		return obj;
		
	}
	
	//전처리 메소드
	public void before(JoinPoint joinPoint) {
		joinPoint.getSignature();	//클래스명이나 메소드명 확인 가능
		//이전단계에서 넘어오는 파라미터값 확인
		Object[] objs = joinPoint.getArgs();
		for(Object obj : objs) {
			
		}
		logger.debug("*before*전처리 전용");
	}
	
	
}
