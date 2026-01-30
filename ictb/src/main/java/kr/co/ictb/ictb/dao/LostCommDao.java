package kr.co.ictb.ictb.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import kr.co.ictb.ictb.vo.LostCommVO;

@Mapper
public interface LostCommDao {
	void addLostComm(LostCommVO vo);
	List<LostCommVO> listLostComment(int num);
	void lostcommdelete(int num);
	void lostcommupdate(Map<String, String> map);

}
