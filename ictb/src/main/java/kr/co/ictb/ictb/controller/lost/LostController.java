package kr.co.ictb.ictb.controller.lost;

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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.ictb.ictb.vo.LostCommVO;
import kr.co.ictb.ictb.vo.LostImageVO;
import kr.co.ictb.ictb.vo.LostVO;
import kr.co.ictb.ictb.vo.PageVO;

@RestController
@RequestMapping("/lost")
public class LostController {

	@Autowired
	private LostService lostService;
	
	@Autowired
	private LostStatService lostStatService;
	
	@Autowired
	private PageVO pageVO;
	
	@Value("${spring.servlet.multipart.location}")
	private String uploadDir;
	
	@Autowired
	private LostCommService lostCommService;

	@PostMapping("/addlost")
	public ResponseEntity<?> addLost(LostVO lostVO,
			@RequestParam("images") MultipartFile[] images, HttpServletRequest request){
		lostVO.setReip(request.getRemoteAddr());
		List<LostImageVO> imageList = new ArrayList<>();
		try {
			for (MultipartFile file : images) {
				System.out.println("asdf");
				if(!file.isEmpty()) {
					String oriFilename = file.getOriginalFilename();
					File f = new File(uploadDir + "/lost/", oriFilename);
					file.transferTo(f);
					LostImageVO imageVO = new LostImageVO();
					imageVO.setImgname(oriFilename);
					imageList.add(imageVO);
					System.out.println(imageVO.getImgname());
					System.out.println(lostVO.getTitle());
					System.out.println("진행중!");
				}
			}
			System.out.println("dddd");
			lostVO.setGetimglist(imageList);
			System.out.println("ffff");
			
			lostService.lostTransactionProcess(lostVO, imageList);
			System.out.println("분실물 정상처리");
			return ResponseEntity.ok("분실물 등록 성공");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("분실물 오류발생");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("업로드 실패");
		}
	}
	
	@PostMapping("/addlostwoutimg")
	public ResponseEntity<?> addLostWoutImage(LostVO lostVO, HttpServletRequest request) {
		lostVO.setReip(request.getRemoteAddr());
		lostService.lostAddWoutImage(lostVO);
		return ResponseEntity.ok("이미지 없는 분실물 등록 성공");
	}
	
	@PostMapping("/updatelost")
	public ResponseEntity<?> updateLost(LostVO lostVO,
			@RequestParam("images") MultipartFile[] images, HttpServletRequest request){
		List<LostImageVO> imageList = new ArrayList<>();
		System.out.println("lostVO = " + lostVO.getContents());
		try {
			for (MultipartFile file : images) {
				if(!file.isEmpty()) {
					String oriFilename = file.getOriginalFilename();
					File f = new File(uploadDir + "/lost/", oriFilename);
					file.transferTo(f);
					LostImageVO imageVO = new LostImageVO();
					imageVO.setImgname(oriFilename);
					imageVO.setLostimgsid(lostVO.getLnum());
					imageList.add(imageVO);
					System.out.println(imageVO.getImgname());
					System.out.println("진행중!");
				}
			}
			lostService.lostUpdateTransactionProcess(lostVO, imageList);
			System.out.println("분실물 수정 정상처리");
			return ResponseEntity.ok("분실물 수정 성공");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("분실물 수정 오류발생");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("업로드 실패");
		}
	}
	
	@PostMapping("/updatelostwoutimg")
	public ResponseEntity<?> updateLostWoutImage(LostVO lostVO) {
		lostService.lostUpdateWoutImage(lostVO);
		return ResponseEntity.ok("이미지 없는 분실물 수정 성공");
	}
	
	@RequestMapping("/lostlist")
	public Map<String, Object> upBoardList(@RequestParam Map<String, String> paramMap, HttpServletRequest request) {
		System.out.println("Method =>" + request.getMethod());
		// Map의 키값이 파라미터 => cPage
		// 현재 페이지값
		String cPage = paramMap.get("cPage");
		System.out.println("cPage: " + cPage);
		System.out.println("category: " + paramMap.get("category"));
		System.out.println("searchType:" + paramMap.get("searchType"));
		System.out.println("searchValue:" + paramMap.get("searchValue"));
		System.out.println("itemcategory:" + paramMap.get("itemcategory"));
		System.out.println("lostloc:" + paramMap.get("lostloc"));
		System.out.println("**************************");
		// 1.총 게시물 수 => PageVO에 해당 property에 setter호출해서 값을 저장 해둔다.
		int totalCnt = lostService.lostTotalCount(paramMap);
		pageVO.setTotalRecord(totalCnt);
		System.out.println("TotalCount:" + pageVO.getTotalRecord());
		System.out.println("********************************");
		// 2. 총페이지 수 구하기 => totalCnt 총게시물수 / numPerPage; 한 페이지당 보여질 게시물 수
		// 11개의 데이터 -> 11/10.0 => 1.1 => 2
		int totalPage = (int) Math.ceil(totalCnt / (double) pageVO.getNumPerPage());
		pageVO.setTotalPage(totalPage);
		System.out.println("TotalPage :" + pageVO.getTotalPage());
		System.out.println("********************************");
		// 3. 총블록수 저장
		// 전체페이지 / pagePerBlock
		// [1][2][3][4][5]| [6][7][8][][]
		int totalBlock = (int) Math.ceil(totalPage / (double) pageVO.getPagePerBlock());
		pageVO.setTotalBlock(totalBlock);
		System.out.println("TotalBlock:" + pageVO.getTotalBlock());
		System.out.println("********************************");
		// 4.현재 페이지 설정
		if (cPage != null) {
			pageVO.setNowPage(Integer.parseInt(cPage));
		} else {
			pageVO.setNowPage(1);
		}
		System.out.println("cPage:" + pageVO.getNowPage());
		System.out.println("********************************");
		// 5. 현재 페이지의 시작 게시물과 끝 게시물 번호를 계산 해서 pageVO에 저장한다.
		//시작페이지 공식 현재페이지값 - 1 * 페이지당보여줄수 + 1
		//공식에 값을 대입해서 결과를 예측 해보자.
		//cPage가 1일때 = 1 - 1 * 10 + 1
		//시작페이지가 0이면 안되기때문에 +1 을 더함 => 1
		//cPage가 2일때 = 2 - 1 * 10 + 1 => 11
	    //단이 5 단이고 범위가 3이다.
	    // 4 5 6
	    //for(int j=4; j<7; j++)
		pageVO.setBeginPerPage((pageVO.getNowPage() - 1) * pageVO.getNumPerPage() + 1);
		pageVO.setEndPerPage(pageVO.getBeginPerPage() + pageVO.getNumPerPage() - 1);
	    System.out.println("5. beginPerPage = " + pageVO.getBeginPerPage());
	    System.out.println("5. endPerPage = " + pageVO.getEndPerPage());
	    System.out.println("********************************");
		
	    
	    
	    //6. result Map
	    // json으로 메서드가 반환할 타입이고 그 데이터를 저장할 맵 ---------- 
		Map<String, Object> response = new HashMap<>();
		// 기존의 paramMap에 새로운 데이터를 추가한다. (시작과, 끝 번호를 추가)
	    // mapper에서 사용할 값들을 포함한다.
		Map<String, String> map = new HashMap<>(paramMap);
		map.put("begin", String.valueOf(pageVO.getBeginPerPage()));
	 	map.put("end", String.valueOf(pageVO.getEndPerPage()));
	   //  페이징처리 결과 데이터가 저장이 되어서 반환 
	 	List<Map<String, Object>> list = lostService.lostList(map);
	 	System.out.println("List Size =>"+list.size());
	 	//-------------------------------------------------------
	 	// 7. 페이지 블록을 구현 
	 	int startPage = (int)((pageVO.getNowPage() - 1) / pageVO.getPagePerBlock()) 
	 			* pageVO.getPagePerBlock() + 1;
	 	int endPage = startPage + pageVO.getPagePerBlock() - 1;
	 	//블록 초기화 전체 페이지값보다 크다면 전체 페이지값을 마지막 블록페이지 값으로 저장 
	 	if(endPage > pageVO.getTotalPage()) {
	 		endPage = pageVO.getTotalPage();
	 	}
	    System.out.println("7. startPage = " + startPage);
	    System.out.println("7. endPage = " + endPage);
	 	response.put("data", list);  // 페이징 처리가 완료된 리스트를 저장한 데이터
	 	//----------------------------------------------------------
		response.put("totalItems", pageVO.getTotalRecord()); // 전체 게시물의 수 count
		response.put("totalPages", pageVO.getTotalPage()); // 전체 페이지
		response.put("currentPage", pageVO.getNowPage()); // 현재 페이지
		response.put("startPage", startPage); // 블록의 시작
		response.put("endPage", endPage); // 블록의 끝 
		return response;
	}
	
	@GetMapping("/lostdetail")
	public Map<String, Object> detail(@RequestParam("num") int num) {
		System.out.println("NUM IS:"+num);
		return lostService.lostDetail(num);
	}
	
	@DeleteMapping("/lostdelete")
	public String lostDelete(@RequestParam("num") int num) {
		lostService.lostDelete(num);
		return "Ok";
	}
	
	@PostMapping("/loststatusupdate")
	public void lostStatusUpdate(@RequestParam("lnum") int num) {
		lostService.lostStatusUpdate(num);
	}
	
	//댓글
	@PostMapping("/lostcommadd")
	public ResponseEntity<?> lostComm(@RequestBody LostCommVO vo){
		lostCommService.addLostComm(vo);
		return ResponseEntity.ok().body(1);
	}
	
	@GetMapping("/lostcommlist")
	public List<LostCommVO> listLostComm(@RequestParam("num") int num){
		return lostCommService.listComment(num);
	}
	
	@DeleteMapping("/lostcommdelete")
	public String lostCommDelete(@RequestParam("lcnum") int lcnum) {
		System.out.println(lcnum);
		lostCommService.lostCommDelete(lcnum);
		return "Ok";
	}
	
	@PostMapping("/lostcommupdate")
	public void lostCommUpdate(@RequestBody Map<String, String> map) {
		lostCommService.lostCommUpdate(map);
	}
	
	
	//통계
	@GetMapping("/lostlocstat")
	public List<Map<String, Object>> lostlocstat(@RequestParam("searchValue") String searchValue) {
		return lostStatService.lostlocstat(searchValue);
	}
	
	@GetMapping("/lostcatstat")
	public List<Map<String, Object>> lostcatstat(@RequestParam("searchValue") String searchValue) {
		return  lostStatService.lostcatstat(searchValue);
	}
	
	@PostMapping("/losttimestat")
	public List<Map<String, Object>> losttimestat(@RequestBody Map<String, String> map) {
		return lostStatService.losttimestat(map);
	}

	@PostMapping("/homelost")
	public List<Map<String, Object>> homelost() {
		return lostService.homelost();
	}
	
}