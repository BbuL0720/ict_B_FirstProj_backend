package kr.co.ictb.ictb.vo;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;

@Alias("surveyContentvo")
@Setter
@Getter
public class SurveyContentVO {
	private String surveytype;
	private String surveytitle;
	private int surveycnt;
}
