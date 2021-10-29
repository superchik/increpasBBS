package com.increpas.bbs;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import mybatis.dao.BbsDAO;
import mybatis.vo.BbsVO;
import spring.util.FileRenameUtil;
import spring.vo.ImgVO;

@Controller
public class WriteControl {
	
	//에디터에서 이미지가 들어갈 때 해당 이미지를 받아서 저장할 위치
	private String editor_img = "/resources/editor_img";
	
	private String write_path = "/resources/bbs_upload";
	
	@Autowired
	private ServletContext application;
	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private BbsDAO b_dao;

	@RequestMapping("/write.inc")
	public String goWrite() {
		return "write";
	}
	
	@RequestMapping(value = "/saveImg.inc", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> saveImg(ImgVO vo){
		//반환객체 생성
		Map<String, String> map = new HashMap<String, String>();
		
		//넘어온 이미지를 vo에서 가져온다.
		MultipartFile f = vo.getS_file();
		String fname = null;
		
		if(f.getSize() > 0) {
			String realPath = application.getRealPath(editor_img);
			
			fname = f.getOriginalFilename();
			
			// 이미지가 이미 저장된 이미지 이름과 동일하다면 파일명 뒤에 숫자를 하나 붙여서 이름이 같은 경우가 발생하지 않도록
			// 해야 한다.
			fname = FileRenameUtil.checkSameFileName(fname, realPath);
			
			try {
				f.transferTo(new File(realPath, fname));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		String img_path = request.getContextPath();
		
		map.put("url", img_path+editor_img);
		map.put("fname", fname);
		
		return map;
	}
	
	@RequestMapping(value = "/write.inc", method = RequestMethod.POST)
	public ModelAndView write(BbsVO vo) {
		ModelAndView mv = new ModelAndView();
		
		MultipartFile f = vo.getFile();
		String fname = null;
		
		if(f != null && f.getSize() > 0) {
			String realPath = application.getRealPath(write_path);
			
			fname = f.getOriginalFilename();
			fname = FileRenameUtil.checkSameFileName(fname, realPath);
			
			try {
				f.transferTo(new File(realPath, fname));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		vo.setFile_name(fname);
		vo.setOri_name(fname);
		
		if(vo.getBname() == null) {
			vo.setBname("BBS");
		}
		
		vo.setIp(request.getRemoteAddr());
		int cnt = b_dao.add(vo);
		
		mv.addObject("fname", fname);
		mv.setViewName("redirect:/list.inc");
		
		return mv;
	}
}
