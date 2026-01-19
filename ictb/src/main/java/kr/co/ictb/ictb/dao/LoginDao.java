package kr.co.ictb.ictb.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import kr.co.ictb.ictb.vo.MemberVO;

@Mapper
public interface LoginDao {
	
	// Login Check
//    MemberVO checkPassword(MemberVO vo);
    
    // Search for User Information
    MemberVO getUserInfo(MemberVO vo);
    
    // Password Update
    void updatePassword(MemberVO vo);
    
    // User Registration
    void createUserInfo(MemberVO vo);
    
    // User Deletion
    void withdrawUserInfo(MemberVO vo);
    
    //passowrdless 로그인일때 id존재 여부 확인
    MemberVO findByMid(String mid);
    
 // Password Change ==>적용 안한것
 //   void changepw(UserInfo userinfo);
//------------여기까지PasswordLess-----------------------
	
	Map<String, Object> loginCheck(MemberVO vo);

//  비밀번호 변경
	void changePassword(MemberVO vo);
}
