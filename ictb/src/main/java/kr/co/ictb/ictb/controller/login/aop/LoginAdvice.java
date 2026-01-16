package kr.co.ictb.ictb.controller.login.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kr.co.ictb.ictb.dao.MyLogDao;
import kr.co.ictb.ictb.vo.MemberVO;
import kr.co.ictb.ictb.vo.MyLoginLoggerVO;

@Component
@Aspect
public class LoginAdvice {
//		login 성공했을때(세션) => 로그 저장 == (after)
//		logout 세션 삭제전에 => 로그 저장 == (before)
//		두개 합치면 around
	@Autowired
	private MyLogDao myLogDao;

	private void createLoggin(String methodName, Object[] fd, ProceedingJoinPoint jp, String status) {
		MyLoginLoggerVO lvo = new MyLoginLoggerVO();
//		첫번쨰 매변수 등의 규칙을 정해놓으면(다른 매개변수와 같게 만들면) 배열의 index로 구분이 가능
		if (fd[0] instanceof HttpSession && fd[1] instanceof HttpServletRequest) {
			HttpSession session = (HttpSession) fd[0];
			HttpServletRequest request = (HttpServletRequest) fd[1];
			MemberVO vo = (MemberVO) session.getAttribute("loginMember");
			
			if (vo != null) { // 로그인 정보가 있으면
				lvo.setIdn(vo.getMid());
				lvo.setStatus(status);
				lvo.setReip(request.getRemoteAddr());
				String userAgent = request.getHeader("User-Agent");
				String parsedAgent = UserAgentUtils.parseAgent(userAgent);
				lvo.setUagent(parsedAgent);
				System.out.println("로그인 기록 : " + lvo);
				System.out.println("IDN : " + lvo.getIdn());
				System.out.println("Agent : " + lvo.getUagent());
				System.out.println("reip : " + lvo.getReip());
				System.out.println("status : " + lvo.getStatus());
				System.out.println("-----------------------------------");
//				myLogDao.addLoginLoggin(lvo);

			}
		}

	}

	@Around("execution(* kr.co.ictb.ictb.controller.login.LoginController.doLog*(..))")
	public String loginLogger(ProceedingJoinPoint jp) {
//		해당 타겟의 메서드 dolog로 시작하는 메서드의 매개변수들을 배열로 받아온다.
		Object[] fd = jp.getArgs();
		String rpath = null;
		String methodName = jp.getSignature().getName();
		try {
			if (methodName.equals("doLogin")) {
				rpath = (String) jp.proceed();// dologin메서드 호출
				createLoggin(methodName, fd, jp, "login"); //login 후 세션 추가
			} else if (methodName.equals("doLogout")) {
				createLoggin(methodName, fd, jp, "logout"); // 세션 삭제전 사용
				rpath = (String) jp.proceed(); // doLogout 메서드 호출 -세션 사라짐
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}

		System.out.println("return:" + rpath);
		return rpath;
	}
}










