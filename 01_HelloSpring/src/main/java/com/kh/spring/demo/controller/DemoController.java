package com.kh.spring.demo.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kh.spring.demo.model.service.DevService;
import com.kh.spring.demo.model.vo.Dev;

@Controller
public class DemoController {

	//	DevService devService = new DevServiceImpl();	//이제 이거 안씀
	@Autowired
	DevService devService;

	@RequestMapping("/demo/demo.do")
	public String demo() {
		return "demo/demo";
	}

	@RequestMapping("/demo/demo1.do")
	public String demo1(HttpServletRequest req, HttpServletResponse res) { //servlet doGet이랑 똑같음!
		String devName = req.getParameter("devName");
		int devAge = Integer.parseInt(req.getParameter("devAge"));
		String devEmail = req.getParameter("devEmail");
		String devGender = req.getParameter("devGender");
		String[] devLangs = req.getParameterValues("devLang");

		//		System.out.println(devName + devAge + devEmail + devGender);
		//		for(String a : devLangs) {
		//			System.out.println(a);
		//		}

		Dev dev = new Dev(devName, devAge, devEmail, devGender, devLangs);
		req.setAttribute("dev", dev);


		return "demo/demoResult";
	}

	//@RequestParam(value="name명") 매개변수지정 => 매개변수 위치 : req.getParameter() 할 필요 없음.
	//	@RequestMapping("/demo/demo2.do")
	//	public String demo2(
	//			@RequestParam(value="devName") String devName,
	//			@RequestParam(value="devAge", required=false, defaultValue = "19") int devAge,	//required=false : 있으면 받고 없으면 받지마!
	//			@RequestParam(value="devEmail") String devEmail,
	//			@RequestParam(value="devGender") String devGender,
	//			@RequestParam(value="devLang") String[] devLang,
	//			HttpServletRequest req
	//			) {


	//자동으로 다 들어감! 매개변수의 변수명 == 파라미터 name값
	//public String demo2(String devName, int devAge, String devEmail, String devGender, String[] devLang) {	
	//		System.out.println(devName);
	//		System.out.println(devAge);
	//		System.out.println(devEmail);
	//		System.out.println(devGender);
	//		for(String a : devLang) {
	//			System.out.println(a);
	//		}
	//		
	//		Dev dev = new Dev(devName, devAge, devEmail, devGender, devLang);
	//		req.setAttribute("dev",dev);
	//		
	//		return "demo/demoResult";
	//	}

	//Model객체를 이용해서 데이터 넘기기
	@RequestMapping("/demo/demo2.do")
	public String demo2(String devName, int devAge, String devEmail, String devGender, String[] devLang, Model model) {	
		System.out.println(devName);
		System.out.println(devAge);
		System.out.println(devEmail);
		System.out.println(devGender);
		for(String a : devLang) {
			System.out.println(a);
		}

		Dev dev = new Dev(devName, devAge, devEmail, devGender, devLang);
		model.addAttribute("dev",dev);

		return "demo/demoResult";
	}

	@RequestMapping("/demo/demo3.do")
	public String demo3(@RequestParam Map map, Model model) {

		model.addAttribute("dev",map);

		return "demo/demoResult";
	}

	@RequestMapping("/demo/demo4.do")
	public String demo4(Dev dev, Model model) {	//단, vo객체의 명칭과 view의 key값이 일치해야함.

		model.addAttribute("dev", dev);

		return "demo/demoResult";
	}

	@RequestMapping("/demo/insertDev.do")
	public String insertDev(Dev dev) {

		int result = devService.insertDev(dev);

		System.out.println(result);
		//리다이렉트로 페이지 전환
		//return "/"; //==> /WEB-INF/views//.jsp; -> 404에러!
		return "redirect:/";	//다른 서블릿을 요청할수도 있음
	}

	@RequestMapping("/demo/selectDevList.do")
	public String selectDev(Model model) {
		List<Dev> list=devService.selectDevList();
		model.addAttribute("list",list);
		return "/demo/devList";
	}
	
	@RequestMapping("/demo/updateDev.do")
	public String updateDev(int devNo) {
		
		return "/demo/demo";
	}
	
	@RequestMapping("/demo/deleteDev.do")
	public String deleteDev(int devNo) {
		
		int result = devService.deleteDev(devNo);
		
		return "/demo/devList";
	}

}



















