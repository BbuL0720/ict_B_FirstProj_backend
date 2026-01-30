package kr.co.ictb.ictb.vo;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;

@Alias("todo")
@Getter
@Setter
public class TodoVO {
	private int tnum;
	private String tid;
	private String tdate;
	private String description;
	
}
