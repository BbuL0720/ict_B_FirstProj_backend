package kr.co.ictb.ictb.controller.friend;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import kr.co.ictb.ictb.vo.FriendRequestVO;
import kr.co.ictb.ictb.vo.MemberVO;

@RestController
@RequestMapping("/api/friends")
public class FriendController {
	@Autowired
	private FriendService friendService;
	
	@GetMapping("/members")
	public List<MemberVO> getAllMembers(HttpSession session){
		MemberVO loginMember = (MemberVO) session.getAttribute("loginMember");
		return friendService.getAllExcept(loginMember.getMid());
	}
	@PostMapping("/request")
	public ResponseEntity<?> sendFriendRequest(@RequestBody Map<String, String> body , HttpSession session){
		MemberVO loginMember = (MemberVO) session.getAttribute("loginMember");
		String receiverId = body.get("receiverId");
		System.out.println(loginMember.getMid());
		friendService.sendRequest(loginMember.getMid(),receiverId);
		return ResponseEntity.ok("친구 요청 완료");
	}
	@GetMapping("/incoming")
	public List<FriendRequestVO> getIncomingRequests(HttpSession session){
		MemberVO loginMember = (MemberVO) session.getAttribute("loginMember");
		return friendService.getPendingRequests(loginMember.getMid());
	}
	@PostMapping("/respond")
	public ResponseEntity<?> respondToRequest(@RequestBody Map<String, String> body){
		Long reqId = Long.parseLong(body.get("frid"));
		String action = body.get("action");
		friendService.respond(reqId, action);
		return ResponseEntity.ok("처리완료");
	}
	@GetMapping("/myfriends")
	public List<MemberVO> myfriends(HttpSession session){
		MemberVO loginMember = (MemberVO) session.getAttribute("loginMember");
		return friendService.getFriends(loginMember.getMid());
	}
	@GetMapping("/outgoing")
	public List<FriendRequestVO> getoutgoingRequests(HttpSession session){
		MemberVO loginMember = (MemberVO) session.getAttribute("loginMember");
		return friendService.getSendRequests(loginMember.getMid());
	}
}
















