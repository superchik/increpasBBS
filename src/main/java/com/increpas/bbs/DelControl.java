package com.increpas.bbs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import mybatis.dao.BbsDAO;

@Controller
public class DelControl {
	
	@Autowired
	private BbsDAO b_dao;
	
	@RequestMapping("/delete.inc")
	public String del(String b_idx, String cPage) {
		b_dao.del(b_idx);
		
		return "redirect:list.inc?cPage="+cPage;
	}
}
