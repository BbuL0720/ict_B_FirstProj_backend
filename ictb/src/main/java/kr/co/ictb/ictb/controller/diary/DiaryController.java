package kr.co.ictb.ictb.controller.diary;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import kr.co.ictb.ictb.vo.BoardVO;
import kr.co.ictb.ictb.vo.DiaryPageVO;
import kr.co.ictb.ictb.vo.DiaryVO;

@RestController
@RequestMapping("/diary")
public class DiaryController {
	@Autowired
	private DiaryService diaryService;

	@Value("${spring.servlet.multipart.location}")
	private String uploadDir;

	@PostMapping("/form")
	public ResponseEntity<?> addDiarylist(@ModelAttribute DiaryVO diaryVO, @RequestParam("images") MultipartFile[] images, @RequestParam("contents") String[] contents) {
		try {
			List<DiaryPageVO> diaryPages = new ArrayList<>();
			int i = 0;
			for (MultipartFile file : images) {
				String Filename = file.getOriginalFilename();
				File serverFile = new File(uploadDir + "/diary/", Filename);
				file.transferTo(serverFile); // 예외발생 가능성 여기서 생김
				DiaryPageVO dpvo = new DiaryPageVO();
				dpvo.setImagename(Filename);
				dpvo.setContent(contents[i++]);
				diaryPages.add(dpvo);
			}
			diaryVO.setDiaryPages(diaryPages);
			diaryService.addDiarylist(diaryVO, diaryPages);
			return ResponseEntity.ok("다이어리 등록 완료");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("다이어리 등록 실패");
		}
	}
	
	@GetMapping("/list")
	public Map<String, Object> diaryList(@RequestParam("page") String page){
		int cPage = Integer.parseInt(page);
		int totalCnt = diaryService.totalCount();
		int totalPage = (int) Math.ceil(totalCnt / (double) 6);
		if (cPage > totalPage) {
			cPage = totalPage;
		}
		int totalBlock = (int) Math.ceil(totalPage / (double) 5);
		int startPage = (cPage-1)*5 + 1;
		int endPage = cPage*5;
		if (endPage > totalPage) {
			endPage = totalPage;
		}
		List<Map<String, Object>> list = diaryService.list(cPage);	//num,writer,udate,image
		
		Map<String, Object> response = new HashMap<>();
		response.put("diarys", list);
		response.put("totalCnt", totalCnt);
		response.put("totalPage", totalPage);
		response.put("page", cPage);
		response.put("startPage", startPage);
		response.put("endPage", endPage);
		return response;
	}
	
	@GetMapping("/detail")
	public Map<String, Object> diarydetail(@RequestParam("num") int num){
		return diaryService.detail(num);
	}
}