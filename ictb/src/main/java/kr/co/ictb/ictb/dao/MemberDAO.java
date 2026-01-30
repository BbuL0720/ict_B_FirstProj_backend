package kr.co.ictb.ictb.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.ictb.ictb.vo.MemberVO;
@Mapper
public interface MemberDAO {
	void insertMember(MemberVO vo);
	
	int countByEmail(String email);
	
	int checkId(String id);
	
	String useCheckEmail(String email);
	int memberInfo(MemberVO vo);
	
//  비밀번호 변경
    void changePassword(MemberVO vo);
    
    void changeColor(MemberVO vo);
    
    List<MemberVO> mypageInfo(MemberVO vo);
}
