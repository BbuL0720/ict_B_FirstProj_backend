package kr.co.ictb.ictb.vo;

import java.util.List;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;

@Alias("surveyvo")
@Setter
@Getter
public class SurveyVO {
    private int snum;
    private int mnum;
    private int code;
    private String fin;
    private String sub;
    private String sdate;
    private List<SurveyContentVO> contents;
    //SurveyContentVO의 값 list로 받는다. (1대多) 
}