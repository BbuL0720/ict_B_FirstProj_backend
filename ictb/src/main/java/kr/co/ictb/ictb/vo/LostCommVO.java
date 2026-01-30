package kr.co.ictb.ictb.vo;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Alias("lostcommvo")
public class LostCommVO {
	private int lcnum;
	private int lnum;
	private int mnum;
	private String writer;
	private String content;
	private String lcdate;
	
}
