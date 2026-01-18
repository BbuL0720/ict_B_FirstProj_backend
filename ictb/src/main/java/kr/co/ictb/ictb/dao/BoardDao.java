package kr.co.ictb.ictb.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import kr.co.ictb.ictb.vo.BoardVO;

@Mapper
public interface BoardDao {
	List<BoardVO> list(Map<String, String> map);
	BoardVO detail(int num);
	void add(BoardVO vo);
	void delete(int num);
	int total(Map<String, String> map);
	List<Map<String, Object>> homeboard();
}