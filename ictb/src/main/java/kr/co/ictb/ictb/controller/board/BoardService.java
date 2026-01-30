package kr.co.ictb.ictb.controller.board;

import java.util.List;
import java.util.Map;

import kr.co.ictb.ictb.dao.BoardDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	@Transactional
	public void delete(int num) {
		boardDao.deleteComments(num);
		boardDao.deleteBoard(num);
	}
	public int total(Map<String, String> map) {
		return boardDao.total(map);
	}
	public void update(BoardVO vo) {
		boardDao.update(vo);
	}

	public List<Map<String, Object>> homeboard() {
		return  boardDao.homeboard();
	}

}
