package kr.co.ictb.ictb.vo;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Alias("lvo")
public class MyLoginLoggerVO {
	private int num;
	private String idn; //접속자 아이디
    private String reip,uagent; // 아이피,에이전트
    private String status; // 상태값
    private String sstime,eetime; // 로그인/로그아웃 시간
}
/*CREATE TABLE myloginlog(
	num NUMBER CONSTRAINT myloginlogs_num_pk PRIMARY KEY,
	idn VARCHAR2(10),
	reip VARCHAR2(30),
	uagent VARCHAR2(100),
	status VARCHAR2(10),
	sstime DATE DEFAULT SYSDATE,
	CONSTRAINT myloginlogs_idn_fk FOREIGN KEY(idn) REFERENCES MEMBER(mid)
);*/