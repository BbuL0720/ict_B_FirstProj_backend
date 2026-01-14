package kr.co.ictb.ictb.controller.member;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kr.co.ictb.ictb.dao.LoginDao;
import kr.co.ictb.ictb.vo.MemberVO;

@RestController // 빈으로 등록
@RequestMapping("/member")
public class MemberController {
	@Autowired
	private MemberService memberService;

	@PostMapping("/signup")
	public ResponseEntity<?> memberjoin(@RequestBody MemberVO vo) {
		System.out.println(vo.getMname());
		memberService.create(vo);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/idCheck")
	public int idCheck(@RequestParam("id") String id) {
		return memberService.checkId(id);
	}

	@PostMapping("/memberInfo")
	public int idCheck(@RequestBody MemberVO vo) {
		return memberService.memberInfo(vo);
	}

	@PostMapping("/changePassword")
	public ResponseEntity<?> changePassword(@RequestBody MemberVO vo) {
		memberService.changePassword(vo);
		return ResponseEntity.ok("변경되었습니다");
	}

	@PostMapping("/changeColor")
	public void changeColor(@RequestBody MemberVO vo) {
		System.out.println(vo.getColor() + " || " + vo.getMid());

		memberService.changeColor(vo);

	}

	@PostMapping("/mypageInfo")
	public List<MemberVO> mypageInfo(@RequestBody MemberVO vo) {
		return memberService.mypageInfo(vo);

	}

}
