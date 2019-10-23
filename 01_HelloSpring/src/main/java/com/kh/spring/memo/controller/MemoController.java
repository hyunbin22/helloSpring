package com.kh.spring.memo.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kh.spring.memo.model.service.MemoService;

@Controller
public class MemoController {
	
	@Autowired
	MemoService service;
	
	@RequestMapping("/memo/memo.do")
	public String memoList(Model model) {
		
		List<Map> list = service.selectList();
		model.addAttribute("list",list);
		
		return "memo/memo";
	}

}
