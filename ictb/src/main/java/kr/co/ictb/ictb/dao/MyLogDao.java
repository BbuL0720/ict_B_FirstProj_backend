package kr.co.ictb.ictb.dao;


import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import kr.co.ictb.ictb.vo.MyLoginLoggerVO;

@Mapper
public interface MyLogDao {
  @Insert("insert into myloginlog values(MYLOGINLOGS_SEQ.nextval,#{idn},#{reip},#{uagent},#{status},sysdate)")
  public void addLoginLoggin(MyLoginLoggerVO vo);
}