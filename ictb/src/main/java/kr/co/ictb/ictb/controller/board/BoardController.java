package kr.co.ictb.ictb.controller.board;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.javassist.expr.Instanceof;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import kr.co.ictb.ictb.vo.BoardCommVO;
import kr.co.ictb.ictb.vo.BoardVO;

@RestController
@RequestMapping("/board")
public class BoardController {
	@Autowired
	private BoardService boardService;

	@Autowired
	private BoardCommService boardCommService;

	@Value("${spring.servlet.multipart.location}")
	String filepath;

	@RequestMapping("/list")
	public Map<String, Object> boardList(@RequestParam Map<String, String> paramMap) {	// 여기서 유저는 paramMap에 현재 페이지,
																						// 검색기능변수(searchType,
																						// searchValue) 등등 받아옴
		int page = Integer.parseInt(paramMap.get("page")); // 유저 현재 페이지
		int totalCnt = boardService.total(paramMap); // 총 게시물 수(유저 있는 게시판에서)
		int totalPage = (int) Math.ceil(totalCnt / (double) 10); // 총 페이지 수(우린 페이지마다 게시글 10개)
		if (totalPage == 0) {
			totalPage = 1;
		}
		if (page > totalPage) {
			page = totalPage;
		}
		int totalBlock = (int) Math.ceil(totalPage / (double) 5); // 총 블럭 수(우린 한 블럭에 다섯 페이지 보이게)
		if (totalBlock == 0) {
			totalBlock = 1;
		}
		int startBoard = (page - 1) * 10 + 1; // 보여줄 게시글은 startBoard ~ endBoard까지
		int endBoard = page * 10;

		int startPage = ((page - 1) / 5) * 5 + 1; // 보여줄 페이지 번호는 startPage ~ endPage
		int endPage = ((page - 1) / 5) * 5 + 5;
		if (endPage > totalPage) {
			endPage = totalPage;
		}

		paramMap.put("startBoard", String.valueOf(startBoard)); // 기존 paramMap에 변수 좀 추가해서
		paramMap.put("endBoard", String.valueOf(endBoard));
		List<BoardVO> list = boardService.list(paramMap); // paramMap으로 DB에서 10개 리스트 뽑아서 담아옴
		Map<String, Object> response = new HashMap<>(); // 응답에 쓸 객체 본격 생성
		response.put("data", list); // 페이징 처리가 완료된 리스트를 저장한 데이터
		// ----------------------------------------------------------
		response.put("totalCnt", totalCnt); // 전체 게시물의 수 count
		response.put("totalPage", totalPage); // 전체 페이지
		response.put("page", page); // 현재 페이지
		response.put("startPage", startPage); // 블록의 시작
		response.put("endPage", endPage); // 블록의 끝

		return response;
	}

	@GetMapping("/detail")
	public BoardVO boardDetail(@RequestParam("num") int num) {
		BoardVO bvo = boardService.detail(num);
		return boardService.detail(num);
	}

	@PostMapping("/form")
	public ResponseEntity<?> boardAdd(BoardVO vo) { // 유저 form데이터가 이 vo에 담겨있음(@RequestBody 왜 필요한지 추후 점검)
		if (vo.getMfile() != null) {
			try {
				MultipartFile mf = vo.getMfile(); // 이미지만 mf에 담고
				String oriFn = mf.getOriginalFilename(); // 이미지 이름만 일단 뽑아두기

				StringBuilder path = new StringBuilder(); // (문자열 조립 객체)
				path.append(filepath).append("\\").append(oriFn); // 문자열 조합해서 새 이미지명부터 완성

				File f = new File(path.toString()); // 새 이미지명으로 빈 파일 하나 제작
				mf.transferTo(f); // 기존 이미지 자체를 새 파일에 복사
				vo.setImage(oriFn); // vo객체에 이미지 이름도 등록
			} catch (IllegalStateException | IOException e) {
				e.printStackTrace(); // (예외터지면 실행될 코드)
			}
		}
		boardService.add(vo); // DB에 드디어 업로드
		return ResponseEntity.ok().body("게시글 작성 성공");
	}
	
	@GetMapping("/delete")
	public ResponseEntity<?> boardDelete(@RequestParam("num") int num) {
		boardService.delete(num);
		return ResponseEntity.ok().body("게시글 삭제 성공");
	}

	@GetMapping("/commList")
	public List<BoardCommVO> boardCommList(@RequestParam("num") int num) {
		return boardCommService.list(num);
	}

	@PostMapping("/commAdd")
	public ResponseEntity<?> boardCommAdd(@RequestBody BoardCommVO vo) {
		boardCommService.add(vo);
		return ResponseEntity.ok().body("댓글 작성 성공");
	}
}