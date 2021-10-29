package com.increpas.bbs;

import java.io.File;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import mybatis.dao.BbsDAO;
import mybatis.vo.BbsVO;
import spring.util.FileRenameUtil;

@Controller
public class EditControl {

	@Autowired
	private BbsDAO b_dao;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private ServletContext application;

	private String path = "/resources/bbs_upload";
	
	// 이렇게도 가능함
	/*public String edit(String b_idx, Model m) {
		BbsVO vo = b_dao.getBbs(b_idx);
		
		m.addAttribute("vo", vo); // model은 request에 저장됨
									// forward시 사용가능함
		
		return "edit";
	}*/
	
//	@RequestMapping(value = "/edit.inc", method = RequestMethod.POST)
//	public ModelAndView edit2(BbsVO vo, String cPage) throws Exception {
//		ModelAndView mv = new ModelAndView();
//		
//		// 요청시 파일이 첨부된 요청인지? 파일첨부가 없는 요청인지 구별
//		// 파일이 첨부된 것은 multipart로 시작 아닌것은 application으로 get은 null이다
//		String ctx = request.getContentType();
//		
//		if(ctx.startsWith("multipart")) {
//			MultipartFile mf = vo.getFile();
//			if(mf != null && mf.getSize() > 0) {
//				String realPath = application.getRealPath(path);
//				
//				String fname = mf.getOriginalFilename();
//				
//				fname = FileRenameUtil.checkSameFileName(fname, realPath);
//				
//				mf.transferTo(new File(realPath, fname)); // 첨부파일 업로드
//				
//				vo.setFile_name(fname);
//				vo.setOri_name(fname);
//			}
//			vo.setIp(request.getRemoteAddr());
//			
//			b_dao.fix(vo); // db 저장
//			mv.setViewName("redirect:/view.inc?b_idx="+vo.getB_idx()+"&cPage="+cPage);
//		}
//		
//		return mv;
//	}

	@RequestMapping(value = "/edit.inc", method = RequestMethod.POST)
	public ModelAndView edit(BbsVO vo, String cPage) {
		ModelAndView mv = new ModelAndView();

		String type = request.getContentType();
		
		if (type.startsWith("application")) {
			BbsVO bvo = b_dao.getBbs(vo.getB_idx());
			mv.addObject("vo", bvo);
			mv.addObject("nowPage", cPage);
			mv.setViewName("edit");

		} else if (type.startsWith("multipart")) {
			MultipartFile mf = vo.getFile();
			if (mf != null && mf.getSize() > 0) {
				// 절대경로 얻기
				String realPath = application.getRealPath(path);

				String fname = mf.getOriginalFilename();

				fname = FileRenameUtil.checkSameFileName(fname, realPath);

				try {
					mf.transferTo(new File(realPath, fname)); // 파일 업로드
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			vo.setIp(request.getRemoteAddr());
			int cnt = b_dao.fix(vo);
			mv.setViewName("redirect:/view.inc?b_idx=" + vo.getB_idx() + "&cPage=" + cPage);
		}

		return mv;
	}
}
