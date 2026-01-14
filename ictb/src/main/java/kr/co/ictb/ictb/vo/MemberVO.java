package kr.co.ictb.ictb.vo;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Alias("memvo")
public class MemberVO {
	private int mnum;
	private String mname;
	private String mid;
	private String password;
	private String email;
	private String mdate;
	private String color;
}

/*
 CREATE TABLE MEMBER(
 mnum NUMBER NOT NULL CONSTRAINT MEMBER_num_pk PRIMARY key,
 mname VARCHAR2(50) NOT null,
 mid VARCHAR2(50) NOT null,
 PASSWORD VARCHAR2(30) NOT null,
 email VARCHAR2(30) NOT null,
 CONSTRAINT member_id_UQ UNIQUE(mid)
 );*/
