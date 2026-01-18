package kr.co.ictb.ictb.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.co.ictb.ictb.vo.SurveyContentVO;
import kr.co.ictb.ictb.vo.SurveyResultVO;
import kr.co.ictb.ictb.vo.SurveyVO;

@Mapper
public interface SurveyDAO {
	int maxSurveyNum();
	List<SurveyResultVO> findBySNUM(int num);
	void saveSurvey(SurveyVO vo);
	void saveSurveyContentList(List<SurveyContentVO> list);
	void finishSurvey(int snum);
	void deleteSurvey(int snum);

	void incrementSurveyCount(@Param("snum") int snum,
							  @Param("surveytype") String surveytype);

	int hasVoted(@Param("snum") int snum,
				 @Param("mnum") int mnum);

	void insertVoteHistory(@Param("snum") int snum,
						   @Param("mnum") int mnum);
}

