package com.increpas.bbs;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import mybatis.dao.BbsDAO;
import mybatis.vo.BbsVO;

@Controller
public class ViewControl {

	@Autowired
	private BbsDAO b_dao;
	
	@Autowired
	private HttpServletRequest request;
	
	@RequestMapping("/view.inc")
	public ModelAndView view(String b_idx, String cPage) {
		ModelAndView mv = new ModelAndView();
		
		BbsVO vo = b_dao.getBbs(b_idx);
		
		mv.addObject("vo", vo);
		mv.addObject("nowPage", cPage); // forward로 가기때문에 딱히 저장 안해도 된다.
		mv.addObject("ip", request.getRemoteAddr());
		mv.setViewName("view");
		
		return mv;		
	}
}
