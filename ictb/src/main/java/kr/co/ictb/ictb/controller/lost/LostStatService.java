package kr.co.ictb.ictb.controller.lost;

import java.util.List;
import java.util.Map;

import kr.co.ictb.ictb.dao.LostStatDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LostStatService {
	@Autowired
	private LostStatDao lostStatDao;
	
	public List<Map<String, Object>> lostlocstat(String searchValue) {
		return lostStatDao.lostlocstat(searchValue);
	}
	
	public List<Map<String, Object>> lostcatstat(String searchValue) {
		return lostStatDao.lostcatstat(searchValue);
	}
	
	public List<Map<String, Object>> losttimestat(Map<String, String> map) {
		return lostStatDao.losttimestat(map);
	}


}
