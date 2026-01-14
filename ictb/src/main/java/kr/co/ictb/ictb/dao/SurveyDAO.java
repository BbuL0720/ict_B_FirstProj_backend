package kr.co.ictb.ictb.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import io.lettuce.core.dynamic.annotation.Param;
import kr.co.ictb.ictb.vo.SurveyContentVO;
import kr.co.ictb.ictb.vo.SurveyResultVO;
import kr.co.ictb.ictb.vo.SurveyVO;

@Mapper
public interface SurveyDAO {
	int maxSurveyNum();
	List<SurveyResultVO> findBySNUM(int num);
	void saveSurvey(SurveyVO vo);
	void saveSurveyContentList(List<SurveyContentVO> list);
	void incrementSurveyCount(@Param("subcode") int subcode,
			@Param("surveytype") String surveytype);
}


