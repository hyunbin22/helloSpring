package com.kh.spring.board.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.kh.spring.board.model.service.BoardService;
import com.kh.spring.board.model.vo.Attachment;
import com.kh.spring.common.PageBarFactory;

@Controller
public class BoardController {
	
	private Logger logger = LoggerFactory.getLogger(BoardController.class);
	@Autowired
	BoardService service;
	
	//ModelAndView : 모델과 view를 한번에 묶어서 처리
	@RequestMapping("/board/boardList.do")
	public ModelAndView selectList(@RequestParam(value="cPage", required=false, defaultValue="0") int cPage, Model model) {
		
		//반환될 modelAndView객체 생성
		ModelAndView mv = new ModelAndView();
		int numPerPage = 5;
		List<Map<String,String>> list = service.selectList(cPage, numPerPage);
		int totalCount = service.selectBoardCount();
		
		mv.addObject("pageBar", PageBarFactory.getPageBar(totalCount, cPage, numPerPage, "/spring/board/boardList.do"));
		mv.addObject("count", totalCount);
		mv.addObject("list",list);	//key value형식 -> model로 들어감
		mv.setViewName("board/boardList");	// -> view
		
		return mv;
	}
	
	@RequestMapping("/board/boardForm")
	public String boardForm() {
		return "board/boardForm";
	}
	
	@RequestMapping("/board/boardFormEnd.do")
	public ModelAndView insertBoard(@RequestParam Map<String, String> param, 
			@RequestParam(value="upFile", required=false)MultipartFile[] upFile,
			HttpServletRequest request) {	//파일 여러개는 배열로 하면 됨, 단 폼에서 name값을 같이 해줘야함. 한개면 MultipartFile upFile 
		
		// 파일업로드 처리하기
		//1. 저장경로 지정
		String saveDir = request.getSession().getServletContext().getRealPath("/resources/upload/board");
		List<Attachment> attachList = new ArrayList();	//여러파일 보관용
		
		File dir = new File(saveDir);
		if(!dir.exists()) logger.debug("생성결과 : "+dir.mkdirs());
		for(MultipartFile f : upFile) {
			if(!f.isEmpty()) {
				//파일명 생성(rename)
				String oriFileName = f.getOriginalFilename();
				String ext = oriFileName.substring(oriFileName.lastIndexOf("."));
				//규칙설정
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HHMMssSSS");
				int rdv = (int)(Math.random()*1000);
				String reName = sdf.format(System.currentTimeMillis()) + "_" + rdv + ext;
				
				//파일 실제 저장
				try {
					f.transferTo(new File(saveDir + "/" + reName));	//파일 전송
				} catch(Exception e) {	//IlligalStateException | IOException 원래 이 두개가 들어가야함.
					e.printStackTrace();
				}
				
				//DB에 저장할 데이터 보관
				Attachment att = new Attachment();
				att.setOriginalFileName(oriFileName);
				att.setRenamedFileName(reName);
				attachList.add(att);
				
			}
			
		}
		int result = 0;
		try {
			result = service.insertBoard(param, attachList);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	//테이블이 다름!
		
		String msg = "";
		String loc = "/board/boardList.do";
		if(result > 0) {
			msg = "입력 성공";
		} else {
			msg = "입력 실패";
		}
		
		ModelAndView mv = new ModelAndView();
		mv.addObject("msg", msg);
		mv.addObject("loc",loc);
		mv.setViewName("common/msg");
		return mv;
	}
	
	@RequestMapping("/board/boardView.do")
	public ModelAndView boardView(int boardNo) {
		ModelAndView mv = new ModelAndView();
		Map<String, String> board = service.selectBoard(boardNo);
		List<Attachment> att = service.selectAttachList(boardNo);
		
		mv.addObject("board", board);
		mv.addObject("attach", att);
		mv.setViewName("board/boardView");
		
		return mv;
	}
	
	@RequestMapping("/board/filedownLoad.do")
	public void fileDownLoad(String oName, String rName, HttpServletRequest req, HttpServletResponse res) {
		BufferedInputStream bis = null;
		ServletOutputStream sos = null;
		String dir = req.getSession().getServletContext().getRealPath("/resources/upload/board");
		File saveFile = new File(dir + "/" + rName);
		try {
			FileInputStream fis = new FileInputStream(saveFile);
			bis = new BufferedInputStream(fis);
			sos = res.getOutputStream();
			String resFileName = "";
			boolean isMSIE = req.getHeader("user-agent").indexOf("MSIE")!=-1 || 
					req.getHeader("user-agent").indexOf("Trident")!= -1;
			if(isMSIE) {
				resFileName = URLEncoder.encode(oName,"UTF-8");
				resFileName = resFileName.replaceAll("\\+", "%20");	//띄어쓰기 바꿔주는것
			} else {
				resFileName = new String(oName.getBytes("UTF-8"),"ISO-8859-1");
			}
			res.setContentType("application/octet-stream;charset=utf-8");
			res.addHeader("Content-Disposition", "attachment;filename=\"" + resFileName + "\"");
			res.setContentLength((int)saveFile.length());
			
			int read = 0;
			while((read=bis.read()) != -1) {
				sos.write(read);
			}
		} catch(IOException e) {
			e.printStackTrace();
		} finally {
			try {
				sos.close();
				bis.close();
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
	}

}



















