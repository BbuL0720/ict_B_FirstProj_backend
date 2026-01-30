package kr.co.ictb.ictb.vo;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Alias("bcvo")
public class BoardCommVO {
	private int bcnum;
	private int bnum;
	private String writer;
	private String udate;
	private String comment_text;
}