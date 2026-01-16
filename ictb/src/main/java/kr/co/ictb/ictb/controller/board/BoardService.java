package kr.co.ictb.ictb.controller.board;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.ictb.ictb.dao.BoardDao;
import kr.co.ictb.ictb.vo.BoardVO;

@Service
public class BoardService {
	@Autowired
	private BoardDao boardDao;
	
	public List<BoardVO> list(Map<String, String> map){
		return boardDao.list(map);
	}
	public BoardVO detail(int num) {
		return boardDao.detail(num);
	}
	public void add(BoardVO vo) {
		boardDao.add(vo);
	}
	public void delete(int num) {
		boardDao.delete(num);
	}
	public int total(Map<String, String> map) {
		return boardDao.total(map);
	}
}
