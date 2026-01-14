package kr.co.ictb.ictb.controller.member;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.ictb.ictb.dao.LoginDao;
import kr.co.ictb.ictb.dao.MemberDAO;
import kr.co.ictb.ictb.vo.MemberVO;

@Service
public class MemberService {
	@Autowired
	private MemberDAO memberDAO;
	@Autowired
	private LoginDao loginDao;
	
	public void create(MemberVO vo) {
		memberDAO.insertMember(vo);
	}
	public int checkEmail(String email) {
		return memberDAO.countByEmail(email);
	}
	public int checkId(String id) {
		return memberDAO.checkId(id);
		
	}
	public int memberInfo(MemberVO vo) {
		return memberDAO.memberInfo(vo);
		
	}
	public void changePassword(MemberVO vo){
		memberDAO.changePassword(vo);
	}

	public void changeColor(MemberVO vo){
		memberDAO.changeColor(vo);
	}
	
	public List<MemberVO> mypageInfo(MemberVO vo) {
		return memberDAO.mypageInfo(vo);
	}
}
