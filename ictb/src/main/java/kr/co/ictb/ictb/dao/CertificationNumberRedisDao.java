package kr.co.ictb.ictb.dao;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

@Repository  
public class CertificationNumberRedisDao {
	//implementation 'org.springframework.boot:spring-boot-starter-data-redis' (build.gradle)
	//StringRedisTemplate ->  Redis 서버에 데이터를 문자열 형태로 저장·조회할 때 사용 
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	
	//email(key)에 대한 authCode(value)와 인증제한시간 및 시도횟수,초기값 설정
	public void saveCertifiRedisNumber(String email , String authCode) {
		stringRedisTemplate.opsForValue().set(email, authCode,Duration.ofSeconds(10000)); 
		stringRedisTemplate.opsForValue().set(email + ":attempt","0",Duration.ofSeconds(10000));
	}
	//redis의 저장된 이메일에 대한 인증번호를 가져오기위한 설정 이메일 = '인증번호' 
	public String getCertifiRedisNumber(String email) {
		return stringRedisTemplate.opsForValue().get(email);
	}
	//redis의 저장된 이메일이 인증성공or만료될시 해당 이메일에 대한 시도횟수와 인증번호를 삭제,초기화 하게끔 하는 설정 
	public void deleteCertifiRedisNumber(String email) {
		stringRedisTemplate.delete(email);
		stringRedisTemplate.delete(email + ":attempt");
	}
	//redis의 저장된 이메일의 key값이 존재하는지 아닌지 확인하기 위한 설정
	public boolean hasKey(String email) {
		return stringRedisTemplate.hasKey(email);
	}
	//인증확인을 시도했을때 시도횟수를 증가시키는 설정
	public void increaseAttempt(String email) {
		stringRedisTemplate.opsForValue().increment(email + ":attempt");
	}
	//해당 email의 key값에 인증확인 시도횟수를 가져와 숫자형으로 바꿔주는 설정
	public int getAttempt(String email) {
		String attemptStr = stringRedisTemplate.opsForValue().get(email + ":attempt");
		return attemptStr == null ? 0 : Integer.parseInt(attemptStr);
	}
}
