package kr.co.ictb.ictb.controller.lost;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.ictb.ictb.dao.LostDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.ictb.ictb.vo.LostImageVO;
import kr.co.ictb.ictb.vo.LostVO;


@Service
public class LostService {
	@Autowired
	private LostDao lostDao;
	
	@Transactional
	public void lostTransactionProcess(LostVO livo, List<LostImageVO> livoList) {
		lostDao.lostAdd(livo);
		lostDao.lostAddImg(livoList);
	}
	
	public void lostAddWoutImage(LostVO livo) {
		lostDao.lostAdd(livo);
	}
	
	public void lostUpdateWoutImage(LostVO livo) {
		lostDao.lostUpdate(livo);
	}
	
	@Transactional
	public void lostUpdateTransactionProcess(LostVO lostVO, List<LostImageVO> imgList) {
	    // 1. 게시글 본문 업데이트 (기존 lostUpdate 실행)
	    lostDao.lostUpdate(lostVO);
	    
	    // 이미지는 무조건 '삭제 후 재등록' 전략을 사용합니다.
	    // 2. 기존 이미지 싹 지우기
	    lostDao.deleteLostImgs(lostVO.getLnum());
	    
	    // 3. 만약 새로 업로드된(또는 유지된) 파일 리스트가 있다면 DB에 넣기
	    if (imgList != null && !imgList.isEmpty()) {
	        Map<String, Object> map = new HashMap<>();
	        map.put("lnum", lostVO.getLnum()); // 게시글 번호 명시
	        map.put("list", imgList);       // 파일 리스트
	        
	        lostDao.insertLostImgsForUpdate(map);
	    }
	}
	
	public void lostAdd(LostVO vo) {
		lostDao.lostAdd(vo);
	}
	public List<Map<String, Object>> lostList(Map<String, String> map){
		return lostDao.lostList(map);
	}
	public Map<String, Object> lostDetail(int lnum) {
		List<Map<String, Object>> rows = lostDao.lostDetail(lnum);
		for (Map.Entry<String, Object> e : rows.get(0).entrySet()) {
			System.out.println(e.getKey()+":"+e.getValue());
		}
		if (rows.isEmpty()) return null;
		
		Map<String, Object> result = new HashMap<>();
		result.put("lnum", rows.get(0).get("LNUM"));
		result.put("mnum", rows.get(0).get("MNUM"));
		result.put("title", rows.get(0).get("TITLE"));
		result.put("category", rows.get(0).get("CATEGORY"));
		result.put("contents", rows.get(0).get("CONTENTS"));
		result.put("lphone", rows.get(0).get("LPHONE"));
		result.put("writer", rows.get(0).get("WRITER"));
		result.put("litem", rows.get(0).get("LITEM"));
		result.put("reip", rows.get(0).get("REIP"));
		result.put("status", rows.get(0).get("STATUS"));
		result.put("itemcategory", rows.get(0).get("ITEMCATEGORY"));
		result.put("losttime", rows.get(0).get("LOSTTIME"));
		result.put("lostloc", rows.get(0).get("LOSTLOC"));
		result.put("ldate", rows.get(0).get("LDATE"));
		
		List<String> images = new ArrayList<>();
		for (Map<String, Object> row : rows) {
			images.add((String) row.get("IMGNAME"));
		}
		result.put("getimglist", images);
		return result;
	}
	public void lostDelete(int lnum) {
		lostDao.lostDelete(lnum);
	}
	
	public int lostTotalCount(Map<String, String> map) {
		return lostDao.lostTotalCount(map);
	}
	
	public void lostStatusUpdate(int lnum) {
		lostDao.lostStatusUpdate(lnum);
	}

	public List<Map<String, Object>> homelost() {
		return lostDao.homelost();
	}

}