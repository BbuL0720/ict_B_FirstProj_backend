package kr.co.ictb.ictb.controller.login;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;

import kr.co.ictb.ictb.dao.LoginDao;
import kr.co.ictb.ictb.vo.MemberVO;

@Service
public class LoginService implements HandlerInterceptor {
	@Autowired
	private LoginDao loginDao;
	
	public Map<String, Object> loginCheck(MemberVO vo){
		return loginDao.loginCheck(vo);
	}

    // Passwordless용 ID 조회
    public MemberVO findByMid(String mid) {
        return loginDao.findByMid(mid);
    }

    // 비밀번호 변경
    public void changePassword(MemberVO vo) {
        loginDao.changePassword(vo);
    }

    // 회원가입
    public void createUserInfo(MemberVO vo) {
        loginDao.createUserInfo(vo);
    }

    // 회원 정보 조회
    public MemberVO getUserInfo(MemberVO vo) {
        return loginDao.getUserInfo(vo);
    }
}
