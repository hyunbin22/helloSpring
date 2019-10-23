package com.kh.spring.chatting;

import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class RtcMessage {
	
	private String token;
	private String type;
	private List<String> members;
	private Sdp sdp;
	private Map<String, String> candidate;

}
