package com.kh.spring.chatting;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.BinaryWebSocketHandler;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.web.socket.handler.TextWebSocketHandler;	//얘는 문자만 받음

public class ViewChatting extends BinaryWebSocketHandler {
	
	//session관리를 직접 해줘야함
	//websocket에 접속한 session관리
	private static Map<String, WebSocketSession> clients = new HashMap();

	//메세지를 받는 메소드
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) {
		ObjectMapper mapper = new ObjectMapper();
		//client가 보낸 JSON데이터를 파싱처리하기 위해 jackson을 사용
		RtcMessage msg = getMessageObject(message);	//메세지파싱용 메소드
		session.getAttributes().put("msg", msg);	//session객체에 보낸 메세지를 저장
		//session관리를 위해 clients객체에 세션을 추가
		clients.put(msg.getToken(), session);
		sessionChecking();	//접속이 종료된 session을 clients에서 삭제함.
		
		//접속한 회원을 보내기
		adminBroadCast();	//현재 접속회원 접속자에게 전송하기
		//화면연결하는 로직 구성
		for(Map.Entry<String, WebSocketSession> client : clients.entrySet()) {
			WebSocketSession s = client.getValue();
			if(!client.getKey().equals(msg.getToken())) {
				try {
					s.sendMessage(new TextMessage(mapper.writeValueAsString(msg)));
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private void adminBroadCast() {
		ObjectMapper mapper = new ObjectMapper();
		RtcMessage msg = new RtcMessage();
		msg.setToken("admin");
		msg.setType("member");
		msg.setMembers(new ArrayList(clients.keySet()));	//접속한 client 아이디를 RtcMessage members에 대입
		try {
			for(Map.Entry<String, WebSocketSession> client : clients.entrySet()) {
				client.getValue().sendMessage(new TextMessage(mapper.writeValueAsString(msg)));//session값
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void sessionChecking() {
		Iterator<Map.Entry<String, WebSocketSession>> it = clients.entrySet().iterator();
		while(it.hasNext()) {
			Map.Entry<String, WebSocketSession> client = it.next();
			if(!client.getValue().isOpen()) {
				it.remove();
			}
		}
	}
	//메세지파싱용
	private RtcMessage getMessageObject(TextMessage message) {
		ObjectMapper mapper = new ObjectMapper();
		RtcMessage m=null;
		try {
			m = mapper.readValue(message.getPayload(), RtcMessage.class);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return m;
	}
	
	
	

}
