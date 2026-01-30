package kr.co.ictb.ictb.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.ictb.ictb.vo.BoardCommVO;

@Mapper
public interface BoardCommDao {
	List<BoardCommVO> list(int num);
	void add(BoardCommVO vo);
}