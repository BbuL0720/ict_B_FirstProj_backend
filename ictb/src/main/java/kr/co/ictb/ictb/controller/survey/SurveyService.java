package kr.co.ictb.ictb.controller.survey;

import java.util.ArrayList;
import java.util.List;

import kr.co.ictb.ictb.dao.SurveyDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import kr.co.ictb.ictb.vo.SurveyContentVO;
import kr.co.ictb.ictb.vo.SurveyResultVO;
import kr.co.ictb.ictb.vo.SurveyVO;

@Service
public class SurveyService {
	@Autowired
	private SurveyDAO surveyDAO;

	public int maxSurveyNum() {
		return surveyDAO.maxSurveyNum();
	}

	// surveyDAO.saveSurvey(vo);와 surveyDAO.saveSurveyContentList(contentList);
	// 두 쿼리 인서트문이 완료되기 전까지 하나라도 오류가 발생하면 모두 롤백한다.
	// 그렇지않으면, 모두 커밋처리한다. 이를 '단위처리'라고 한다.
	@Transactional
	public void saveSurvey(SurveyVO vo) {
		// 1. Survey 저장
		surveyDAO.saveSurvey(vo);
		// 2. SurveyContent 리스트 준비
		char stype = 'A';
		List<SurveyContentVO> contentList = new ArrayList<>();
		for (SurveyContentVO sc : vo.getContents()) {
			SurveyContentVO contentVO = new SurveyContentVO();
			contentVO.setSurveytitle(sc.getSurveytitle());
			contentVO.setSurveytype(String.valueOf(stype));
			contentVO.setSurveycnt(0);
			contentList.add(contentVO);
			stype++;
		}
		// 3. 한번에 INSERT ALL
		surveyDAO.saveSurveyContentList(contentList);
	}

	public List<SurveyVO> getSurveyList() {
		List<SurveyVO> surveyList = new ArrayList<>();

		for (int i = 0; i < surveyDAO.maxSurveyNum(); i++) {
			List<SurveyResultVO> result = surveyDAO.findBySNUM((int) i + 1);
			if (result.isEmpty()) {
				continue; // 데이터가 없는 경우 처리
			} else {

				// SurveyVO 및 SurveyContentVO 매핑
				SurveyVO surveyVO = new SurveyVO();
				List<SurveyContentVO> contentVOList = new ArrayList<>();

				// 첫 번째 행을 기준으로 Survey 정보를 설정
				SurveyResultVO resultVO = result.get(0);
				surveyVO.setSnum(resultVO.getSurveyNum());
				surveyVO.setMnum(resultVO.getSuveyMnum());
				surveyVO.setFin(resultVO.getSurveyFin());
				surveyVO.setSub(resultVO.getSurveySub());
				surveyVO.setCode(resultVO.getSurveyCode());
				surveyVO.setSdate(resultVO.getSurveyDate()); // survey.code

				// SurveyContentVO 설정
				for (SurveyResultVO vo : result) {
					SurveyContentVO contentVO = new SurveyContentVO();
					contentVO.setSurveytype(vo.getSurveytype());
					contentVO.setSurveytitle(vo.getSurveytitle());
					contentVO.setSurveycnt(vo.getSurveycnt());
					contentVOList.add(contentVO);
				}

				surveyVO.setContents(contentVOList);
				surveyList.add(surveyVO);
			}
		}
		return surveyList;
	}

	public SurveyVO findBySNUM(int num) {

		List<SurveyResultVO> result = surveyDAO.findBySNUM(num);

		if (result == null) {
			return null; // 데이터가 없는 경우 처리
		}
		SurveyVO surveyVO = new SurveyVO();
		List<SurveyContentVO> contentVOList = new ArrayList<>();

		SurveyResultVO resultVO = result.get(0);
		surveyVO.setSnum(resultVO.getSurveyNum());
		surveyVO.setSub(resultVO.getSurveySub());
		surveyVO.setMnum(resultVO.getSuveyMnum());
		surveyVO.setFin(resultVO.getSurveyFin());
		surveyVO.setCode(resultVO.getSurveyCode());
		surveyVO.setSdate(resultVO.getSurveyDate());

		for (SurveyResultVO vo : result) {
			SurveyContentVO contentVO = new SurveyContentVO();
			contentVO.setSurveytype(vo.getSurveytype());
			contentVO.setSurveytitle(vo.getSurveytitle());
			contentVO.setSurveycnt(vo.getSurveycnt());
			contentVOList.add(contentVO);
		}
		surveyVO.setContents(contentVOList);
		return surveyVO;
	}
	@Transactional
	public String incrementSurveyCount(int snum, int mnum, String surveytype) {
		int count = surveyDAO.hasVoted(snum, mnum);

		if (count > 0) {
			System.out.println("Already Voted");
			return "Already Voted";
		} else {
			surveyDAO.incrementSurveyCount(snum, surveytype);
			surveyDAO.insertVoteHistory(snum, mnum);
			System.out.println("Vote Successful");
			return "Vote Successful";
		}
	}

	public void insertSurveyHistory(@RequestParam("snum") int snum,
			@RequestParam("mnum") int mnum) {
		surveyDAO.insertVoteHistory(snum, mnum);
	}

	public int readSurveyHistory(@RequestParam("snum") int snum,
			@RequestParam("mnum") int mnum) {
		return surveyDAO.hasVoted(snum, mnum);
	}

	public void finishSurvey(int snum) {
		surveyDAO.finishSurvey(snum);
	}

	public void deleteSurvey(int snum) {
		surveyDAO.deleteSurvey(snum);
	}
}