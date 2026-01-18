package kr.co.ictb.ictb.vo;

import java.util.List;

import org.apache.ibatis.type.Alias;
import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Alias("lostvo")
public class LostVO {
	private int lnum;
	private int mnum;
	private String title;
	private String category;
	private String contents;
	private String lphone;
	private String writer;
	private String litem;
	private String reip;
	private String status;
	private String itemcategory;
	private String losttime;
	private String lostloc;
	private String ldate;
	private List<LostImageVO> getimglist;
}
