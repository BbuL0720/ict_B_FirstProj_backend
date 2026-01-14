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
import org.springframework.web.bind.annotation.RestController;

import kr.co.ictb.ictb.vo.SurveyVO;


@RestController
@RequestMapping("/api/survey")
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

	@GetMapping("/result/{num}")
	public ResponseEntity<SurveyVO> getSurveyResult(@PathVariable("num") int num) {
		SurveyVO surveyVO = surveyService.findBySNUM(num);
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
		int subcode = (int) payload.get("subcode");
		String surveytype = (String) payload.get("surveytype");

		System.out.println("subcode: " + subcode);
		System.out.println("surveytype: " + surveytype);

		try {
			surveyService.incrementSurveyCount(subcode, surveytype);
			return ResponseEntity.ok("투표 성공");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
		}
	}
}
