package kr.co.ictb.ictb.vo;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Alias("lostimgvo")
public class LostImageVO {
	private int lostimgsid;
	private String imgname;

}
