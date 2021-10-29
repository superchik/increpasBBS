package mybatis.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import mybatis.vo.BbsVO;

@Component
public class BbsDAO {

	@Autowired
	private SqlSessionTemplate ss;

	// 원하는 페이지의 게시물 목록 기능
	public BbsVO[] getList(String begin, String end, String bname) {
		BbsVO[] ar = null;
		
		// 매퍼(bbs.list)를 호출하기 위해 Map 구조 생성
		Map<String, String> map =  new HashMap<String, String>();
		map.put("begin", begin);
		map.put("end", end);
		map.put("bname", bname);
		
		List<BbsVO> list =  ss.selectList("bbs.list", map);
		
		if(list != null && !list.isEmpty()) {
			ar = new BbsVO[list.size()];
			list.toArray(ar);
		}
		
		
		return ar;
	}
	
	public int getTotalCount(String bname) {
		return ss.selectOne("bbs.totalCount", bname);
	}
	
	public BbsVO getBbs(String b_idx) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("b_idx", b_idx);
		
		BbsVO vo = ss.selectOne("bbs.getBbs", map);
		
		return vo;
	}
	
	public int add(BbsVO vo) {
		int cnt = ss.insert("bbs.add", vo);
		
		return cnt;
	}
	
	public int fix(BbsVO vo) {
		int cnt = ss.update("bbs.fix", vo);
		
		return cnt;
	}
	
	public int del(String b_idx) {
		int cnt = ss.update("bbs.del", b_idx);
		return cnt;
	}
}
