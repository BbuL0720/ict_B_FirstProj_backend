package kr.co.ictb.ictb.controller.login.aop;

public class UserAgentUtils {

	public static String parseAgent(String userAgent) {
		String os = "Unkown OS";
		String browser = "Unknown Browser";
		if (userAgent.contains("Windows NT")) {
			os = "Windows";
		} else if (userAgent.contains("Mac OS X")) {
			os = "macOS";
		} else if (userAgent.contains("Android")) {
			os = "Android";
		} else if (userAgent.contains("iPhone") || userAgent.contains("iPad")) {
			os = "iOS";
		}
	
		if(userAgent.contains("Edg/")) {
			browser = "Edge";
		}else if(userAgent.contains("Chrome/") && !userAgent.contains("Edg/")) {
			browser= "chrome";
		}else if(userAgent.contains("Firefox/")) {
			browser = "Firefox";
		}else if(userAgent.contains("Safari/") && !userAgent.contains("Chrome/")) {
			browser = "Safari";
		}
		return os + " - " + browser;
	}
}
		
		