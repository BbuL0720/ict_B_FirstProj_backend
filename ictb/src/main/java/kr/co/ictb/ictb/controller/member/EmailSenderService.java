package kr.co.ictb.ictb.controller.member;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import kr.co.ictb.ictb.dao.CertificationNumberRedisDao;
import kr.co.ictb.ictb.dao.MemberDAO;

@Service
public class EmailSenderService {
	//implementation 'org.springframework.boot:spring-boot-starter-mail' (build.gradle)
	//JavaMailSender -> 이메일 메시지를 생성하고 메일을 보내주는 역할
	@Autowired
	private JavaMailSender mailSender; 
	
	@Autowired
	private MemberDAO memberDAO;
	
	@Autowired
	private CertificationNumberRedisDao certificationNumberRedisDao;
	
	private String authCode;
	
	//이메일 중복검사 : 중복이면 1 아니면 0
	public int duplicateEmail(String email) {
		int checkEmail = memberDAO.countByEmail(email);
		return checkEmail > 0 ? 1 : 0;
	}
	public String useEmailCheck(String email) {
		String checkEmail = memberDAO.useCheckEmail(email);
		return checkEmail;
	}
	//무작위의 6자리 난수를 만드는 메서드 
	public void createAuthCode() {
		int length = 6;
		StringBuilder authCode = new StringBuilder(); //StringBuilder -> 문자열과 문자열을 결합할때 사용됨
		Random random = new Random(); 
		for(int i=0; i < length; i++) {
			int type = random.nextInt(3); //0,1,2 의 랜덤한숫자를 type에 저장
			switch(type) {
			case 0: authCode.append(random.nextInt(10)); // type이 0일 경우: random.nextInt(10) => 0~9까지의 랜덤한 숫자를 뽑음
			break;
			case 1: authCode.append((char)(random.nextInt(26)+65)); //type이 1일 경우: A~Z까지 대문자를 뽑아옴
			break;
			case 2: authCode.append((char)(random.nextInt(26)+97));// type이 2일 경우: a~z까지 소문자를 뽑아옴
			break;
			default:
				throw new IllegalArgumentException("Unexpected value:"+ type); //메서드에 잘못된 또는 부적절한 인수가 전달될 때 발생하는 런타임 예외
			}
		}
		//멤버 필드에 랜덤화된 인증코드 6자리를 저장해둠
		this.authCode = authCode.toString();
	}
	//회원가입 : 이메일 인증번호 발송
	 public void sendEmail(String toEmail) {
		 System.out.println(toEmail);
		 createAuthCode();
		 MimeMessage message = mailSender.createMimeMessage();
		 try {
			MimeMessageHelper helper = new MimeMessageHelper(message,true);
			System.out.println("toEmail = [" + toEmail + "]");
			helper.setFrom("llsm0720@naver.com");
			helper.setTo(toEmail);
			helper.setSubject("ICTB팀의 회원가입 인증번호 발송");
			StringBuilder body = new StringBuilder();
			body.append("<html><body>");
			body.append("<h1>ICTB팀의 회원가입을 위한 인증번호</h1>");
			body.append("<p>회원 가입을 완료하기 위해 아래의 인증코드를 입력해주세요.</p>");
			body.append("<p>인증코드 : <strong>");
			body.append(authCode);
			body.append("</strong></p>");
			body.append("</body></html>");
			helper.setText(body.toString(),true);
			mailSender.send(message);
			System.out.println("인증코드(테스트용):" + authCode);
			certificationNumberRedisDao.saveCertifiRedisNumber(toEmail, authCode);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	 }// 인증번호확인, 즉 해당 이메일에 대한 인증코드의 시도횟수를 3회 까지 해준다는 설정
	 public boolean isVerify(String email , String authCode) {
		 final int MAX_ATTEMPT = 3;
		 if(!certificationNumberRedisDao.hasKey(email)) {
			 return false;
		 }
		 int attemptCount = certificationNumberRedisDao.getAttempt(email);
		 if(attemptCount >= MAX_ATTEMPT) {
			 System.out.println("인증실패: 최대 인증 시도 초과");
			 return false;
		 }
		 System.out.println("Redis의 이메일 :"+certificationNumberRedisDao.hasKey(email));
		 System.out.println("Redis의 인증코드:"+authCode);
		 String savedCode = certificationNumberRedisDao.getCertifiRedisNumber(email);
		 if(savedCode != null && savedCode.equals(authCode)) { // redis에 저장된 이메일이 null이 아니면서 인증코드와 같다면 true 아니면 false
			 certificationNumberRedisDao.deleteCertifiRedisNumber(email);
			 return true;
		 }else {
			 certificationNumberRedisDao.increaseAttempt(email);
			 System.out.println("인증 실패 ("+(attemptCount+1)+"회)");
			 return false;
		 }
	 }
}
















