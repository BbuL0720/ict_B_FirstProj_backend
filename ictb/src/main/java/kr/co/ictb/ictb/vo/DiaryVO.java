package kr.co.ictb.ictb.vo;

import java.util.List;

import org.apache.ibatis.type.Alias;
import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Alias("dvo")
public class DiaryVO {
	private int num;
	private String writer;
	private String udate;
	private List<DiaryPageVO> diaryPages;
}