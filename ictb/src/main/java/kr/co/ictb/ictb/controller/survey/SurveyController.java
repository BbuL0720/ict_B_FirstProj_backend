package kr.co.ictb.ictb.controller.survey;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kr.co.ictb.ictb.controller.survey.SurveyService;
import kr.co.ictb.ictb.vo.SurveyVO;
import oracle.jdbc.proxy.annotation.Post;

@RestController
@RequestMapping("/survey")
public class SurveyController {
	@Autowired
	private SurveyService surveyService;

	@PostMapping("/addsurvey")
	public ResponseEntity<String> saveSurvey(@RequestBody SurveyVO vo) {
		surveyService.saveSurvey(vo);
		System.out.println("sub:" + vo.getSub());
		System.out.println("title:" + vo.getContents().get(0).getSurveytitle());
		return ResponseEntity.ok("success");
	}

	@GetMapping("/latest")
	public ResponseEntity<SurveyVO> getLatestSurvey() {
		SurveyVO surveyVO = surveyService.findBySNUM(surveyService.maxSurveyNum());
		if (surveyVO != null) {
			return ResponseEntity.ok(surveyVO);
		} else {
			return ResponseEntity.noContent().build();
		}
	}

	@GetMapping("/detail")
	public ResponseEntity<SurveyVO> getSurveyResult(@RequestParam("snum") int num) {
		SurveyVO surveyVO = surveyService.findBySNUM(num);
		System.out.println("surveyResult: "+num);
		if (surveyVO != null) {
			return ResponseEntity.ok(surveyVO);
		} else {
			return ResponseEntity.noContent().build();
		}
	}

	@GetMapping("/allList")
	public ResponseEntity<List<SurveyVO>> getAllSurvey() {
		List<SurveyVO> surveyList = surveyService.getSurveyList();
		if (surveyList != null) {
			return ResponseEntity.ok(surveyList);
		} else {
			return ResponseEntity.noContent().build();
		}
	}

	@PostMapping("/updateCount")
	public ResponseEntity<String> incrementSurveyCount(@RequestBody Map<String, Object> payload) {
		int snum = Integer.parseInt(String.valueOf(payload.get("snum")));
		int mnum = Integer.parseInt(String.valueOf(payload.get("mnum")));
		String surveytype = (String) payload.get("surveytype");
		
		String result = surveyService.incrementSurveyCount(snum, mnum, surveytype);
		System.out.println("subcode: " + snum);
		System.out.println("mnum: " + mnum);
		System.out.println("surveytype: " + surveytype);

		try {
			if ("Vote Successful".equals(result)) {
				return ResponseEntity.ok("투표 성공");
			} else {
				return ResponseEntity.ok("기참여자");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
		}
	}
	
	@GetMapping("/finishSurvey")
	public void finishSurvey(@RequestParam("snum") int snum) {
		surveyService.finishSurvey(snum);
	}
	
	@GetMapping("/deleteSurvey")
	public void deleteSurvey(@RequestParam("snum") int snum) {
		surveyService.deleteSurvey(snum);
	}
}