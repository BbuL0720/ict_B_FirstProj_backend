package kr.co.ictb.ictb.controller.login;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kr.co.ictb.ictb.vo.MemberVO;

@RestController
@RequestMapping("/login")
public class LoginController {
	@Autowired
	private LoginService loginService;
	@PostMapping("/dologin")
	public String doLogin(HttpSession session, HttpServletRequest request,
			@RequestHeader("User-Agent") String userAgent,@RequestBody MemberVO vo) {
		Map<String, Object> result = loginService.loginCheck(vo);
		System.out.println("result:"+result);
		if(result != null && result.get("CNT") != null) {
			int cnt = ((Number) result.get("CNT")).intValue();
			if(cnt == 1) {
				System.out.println("세션 처리 완료");
				vo.setMname(result.get("MNAME").toString());
				vo.setColor(result.get("COLOR").toString());
				session.setAttribute("loginMember", vo);
				System.out.println(vo.getMname());
				System.out.println(vo.getMid());
				return "success";
			}
		}
		return "fail";
	}
	@GetMapping("/dologout")
	public String doLogout(HttpSession session,HttpServletRequest request,
			@RequestHeader("User-Agent") String userAgent) {
		System.out.println("로그아웃 처리 완료");
		session.invalidate();
		return "logout";
		
	}
	@GetMapping("/session")
	public MemberVO session(HttpSession session) {
		MemberVO loginMember = (MemberVO) session.getAttribute("loginMember");
		if(loginMember != null) {
			loginMember.setPassword(null);
		}

		System.out.println("test");
		return loginMember;
	}
}







