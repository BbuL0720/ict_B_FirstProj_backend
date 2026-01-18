package kr.co.ictb.ictb.vo;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;

@Alias("surveyhistoryvo")
@Getter
@Setter
public class SurveyHistoryVO {
	private int hnum;
	private int snum;
	private int mnum;
	private String voteDate;
}
