package kr.co.ictb.ictb.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor //매개변수가 있는 기본생성자생성
@NoArgsConstructor //매개변수가 없는 기본생성자 생성
public class EmailCountCheckVO {
private boolean success;
private String reason;
}
