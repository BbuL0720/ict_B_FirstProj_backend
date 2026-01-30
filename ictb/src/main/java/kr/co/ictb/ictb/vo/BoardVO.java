package kr.co.ictb.ictb.vo;

import org.apache.ibatis.type.Alias;
import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Alias("bvo")
public class BoardVO {
	private int bnum;
	private String category;		//어떤 게시판인지
	private String title;
	private String writer;
	private String udate;
	private String image;			//이미지명
	private String content;
	private MultipartFile mfile;
}