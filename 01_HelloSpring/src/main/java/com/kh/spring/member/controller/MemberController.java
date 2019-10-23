package com.kh.spring.member.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kh.spring.board.model.service.BoardService;
import com.kh.spring.member.model.service.MemberService;
import com.kh.spring.member.model.vo.Member;

@SessionAttributes(value= {"loginMember"})
@Controller
public class MemberController {

	private Logger logger=LoggerFactory.getLogger(MemberController.class);
	
	@Autowired
	MemberService service;
	@Autowired
	BoardService bService;
	@Autowired
	BCryptPasswordEncoder pwEncoder;

	@RequestMapping("/member/checkId.do")
	@ResponseBody
	public String responseBody(String userId, Model model)throws JsonProcessingException {
		
		Member m=new Member();
		m.setUserId(userId);
		m=service.selectMemberOne(m);
		//jackson gson과 비슷한역활을 함. 
		ObjectMapper mapper=new ObjectMapper();
		List<Map<String,String>> list=bService.selectList(1,5);
		return mapper.writeValueAsString(list);	
		
		
	}
	
	
	/* viewResolver(jsonView)를 통해서 ajax처리하는 방법 */
//	public ModelAndView ajaxViewResolver(String userId,ModelAndView mv) {
//		Member m=new Member();
//		m.setUserId(userId);
//		m=service.selectMemberOne(m);
//		//Member member=service.selectMemberOne(m);//객체넘기기
//		
//		boolean isUsable=(m!=null&&m.getUserId()!=null)?false:true;
//		//mv.addObject("member",member);//JSONObject, 객체말고 단일값만 전송
//		//JSONArray.add()
//		mv.addObject("userId","ghdkfhsd");
//		
//		mv.addObject("isUsable",isUsable);
//		mv.setViewName("jsonView");//명칭을 반드시 jsonView라고 작성을 해야함.!
//		return mv;
//	}
	
	/* ajax통신 stream이용하는 방법 */
//	public void ajaxStream(String userId, HttpServletResponse res) 
//			throws IOException{
//		Member m=new Member();
//		m.setUserId(userId);
//		m=service.selectMemberOne(m);
//		boolean isUsable=(m!=null&&m.getUserId()!=null)?false:true;
//		
//		res.getWriter().print(isUsable);
//	}
	
	/* 화상채팅구현 */
	@RequestMapping("/viewChatting.do")
	public String viewChatting() {
		return "chatting/viewChatting";
	}
	
	
	@RequestMapping("/member/memberLogin.do")
	public String login(Member m, Model model,HttpSession session) {
		
		logger.debug(m.getUserId());
		logger.debug(m.getPassword());
		Member result=service.selectMemberOne(m);
		String msg="";
		String loc="/";
		/* if(result.getPassword().equals(m.getPassword())) { */
		logger.debug(pwEncoder.encode(m.getPassword()));
		
		if(pwEncoder.matches(m.getPassword(), result.getPassword())) {
			msg="로그인 성공";
			session.setAttribute("loginMember", result);
			model.addAttribute("loginMember",result);
		}
		else {
			msg="로그인 실패";
		}
		model.addAttribute("msg",msg);
		model.addAttribute("loc",loc);
		
		return "common/msg";
	}
	
	@RequestMapping("/member/memberEnroll.do")
	public String enroll() {
		//화면전환용 메소드
		return "member/memberEnroll";
	}
	
	@RequestMapping("/member/memberEnrollEnd.do")
	public String enrollEnd(Member m, Model model) {
		
		m.setPassword(pwEncoder.encode(m.getPassword()));
		int result=service.insertMember(m);
		String msg="";
		String loc="/";
		if(result>0) {
			msg="회원가입성공!";
		}
		else {
			msg="회원가입실패!";
		}
		
		model.addAttribute("msg",msg);
		model.addAttribute("loc",loc);
		
		return "common/msg";
	}
	
	@RequestMapping("/member/memberLogout.do")
	public String logout(HttpSession session,SessionStatus status) {
		
		if(!status.isComplete()) {
			status.setComplete();
			/* session.invalidate(); */
		}
		return "redirect:/";
	}
	
}







