package kr.co.ictb.ictb.controller.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.ictb.ictb.dao.MyLogDao;

@Service
public class MyLogService {
	@Autowired
	public MyLogDao myLogDao;
}
