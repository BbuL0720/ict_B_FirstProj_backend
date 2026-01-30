package kr.co.ictb.ictb.controller.lost;

import java.util.List;
import java.util.Map;

import kr.co.ictb.ictb.dao.LostCommDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.ictb.ictb.vo.LostCommVO;

@Service
public class LostCommService {
	@Autowired
	private LostCommDao lostCommDao;
	
	public void addLostComm(LostCommVO vo) {
		lostCommDao.addLostComm(vo);
	}
	
	public List<LostCommVO> listComment(int num){
		return lostCommDao.listLostComment(num);
	}
	
	@Transactional
	public void lostCommDelete(int num) {
		lostCommDao.lostcommdelete(num);
	}
	
	public void lostCommUpdate(Map<String, String> map) {
		lostCommDao.lostcommupdate(map);
	}
}