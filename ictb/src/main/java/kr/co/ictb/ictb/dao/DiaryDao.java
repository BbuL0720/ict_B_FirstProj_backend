package kr.co.ictb.ictb.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import kr.co.ictb.ictb.vo.DiaryPageVO;
import kr.co.ictb.ictb.vo.DiaryVO;

@Mapper
public interface DiaryDao {
	void addDiary(DiaryVO dvo);
	void addImgs(List<DiaryPageVO> divo);
	int totalCount();
	List<Map<String, Object>> list(int num);
	List<Map<String, Object>> detail(int num);
}