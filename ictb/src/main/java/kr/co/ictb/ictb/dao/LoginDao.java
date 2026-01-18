package kr.co.ictb.ictb.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import kr.co.ictb.ictb.vo.MemberVO;
@Mapper
public interface LoginDao {

	@Select("SELECT MNAME ,MNUM ,COLOR, COUNT(*) cnt FROM MEMBER WHERE mid=#{mid} AND PASSWORD=#{password} GROUP BY MNAME,COLOR,MNUM")
	Map<String, Object> loginCheck(MemberVO vo);
}
