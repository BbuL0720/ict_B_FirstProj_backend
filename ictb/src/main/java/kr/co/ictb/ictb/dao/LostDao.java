package kr.co.ictb.ictb.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import kr.co.ictb.ictb.vo.LostImageVO;
import kr.co.ictb.ictb.vo.LostVO;

@Mapper
public interface LostDao {
	void lostAdd(LostVO vo);
	void lostAddImg(List<LostImageVO> livo);
//	List<LostVO> list(Map<String, String> map);
	List<Map<String, Object>> lostList(Map<String, String> map);
	List<Map<String, Object>> lostDetail(int lnum);
	void lostDelete(int lnum);
	int lostTotalCount(Map<String, String> map);
	void lostUpdate(LostVO vo);
	void deleteLostImgs(int lnum);
	void insertLostImgsForUpdate(Map<String, Object> map);
	void lostStatusUpdate(int lnum);
	List<Map<String, Object>> homelost();


}