package kr.co.ictb.ictb.vo;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Alias("dpvo")
public class DiaryPageVO {
	private int dnum;
    private String imagename;
    private String content;
}