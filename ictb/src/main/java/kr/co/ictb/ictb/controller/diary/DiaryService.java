package kr.co.ictb.ictb.controller.diary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.ictb.ictb.dao.DiaryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.ictb.ictb.vo.DiaryPageVO;
import kr.co.ictb.ictb.vo.DiaryVO;

@Service
public class DiaryService {
	@Autowired
	private DiaryDao diaryDao;
	
	@Transactional
	public void addDiarylist(DiaryVO dvo, List<DiaryPageVO> dpvo) {
		diaryDao.addDiary(dvo);
		diaryDao.addImgs(dpvo);
	}
	
	public int totalCount() {
		return diaryDao.totalCount();
	}
	
	public List<Map<String, Object>> list(int num){
		return diaryDao.list(num);
	}
	
	public Map<String, Object> detail(int num){
		List<Map<String, Object>> rows = diaryDao.detail(num);
		Map<String, Object> result = new HashMap<>();
		result.put("num", rows.get(0).get("num"));
		result.put("writer", rows.get(0).get("writer"));
		result.put("udate", rows.get(0).get("udate"));
		List<Map<String, Object>> pages = new ArrayList<>();
		for(Map<String, Object> row : rows) {
			Map<String, Object> page = new HashMap<>();
			page.put("image", (String) row.get("IMAGE"));
			page.put("content", (String) row.get("CONTENT"));
			pages.add(page);
		}
		result.put("pages", pages);
		return result;
	}
}