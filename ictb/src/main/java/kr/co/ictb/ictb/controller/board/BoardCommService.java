package kr.co.ictb.ictb.controller.board;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.ictb.ictb.dao.BoardCommDao;
import kr.co.ictb.ictb.vo.BoardCommVO;

@Service
public class BoardCommService {

	@Autowired
	private BoardCommDao boardCommDao;

	public List<BoardCommVO> list(int num) {
		return boardCommDao.list(num);
	}
	public void add(BoardCommVO vo) {
		boardCommDao.add(vo);
	}
}