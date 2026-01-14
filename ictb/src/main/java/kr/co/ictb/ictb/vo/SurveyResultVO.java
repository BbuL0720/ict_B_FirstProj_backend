package kr.co.ictb.ictb.vo;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;

@Alias("surveyresultvo")
@Setter
@Getter
public class SurveyResultVO {
	//list 출력용
	private int surveyNum;
	private String surveySub;
	private int surveyCode;
	private String surveyDate;
	private int subCode;
	private String surveytype;
	private String surveytitle;
	private int surveycnt;
}
