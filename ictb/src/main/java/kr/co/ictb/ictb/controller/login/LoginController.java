package kr.co.ictb.ictb.controller.login;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.SSLContext;

import kr.co.ictb.ictb.vo.MyLoginLoggerVO;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.co.ictb.ictb.dao.LoginDao;
import kr.co.ictb.ictb.vo.MemberVO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/login")
public class LoginController {
	

	@Autowired
	private LoginService loginService;
	@Autowired
	private LoginDao dao;
	
	@Autowired
    MessageUtils messageUtils;
	
	@Value("${passwordless.corpId}")
	private String corpId;
	
	@Value("${passwordless.serverId}")
	private String serverId;
	
	@Value("${passwordless.serverKey}")
	private String serverKey;
	
	@Value("${passwordless.simpleAutopasswordUrl}")
	private String simpleAutopasswordUrl;
	
	@Value("${passwordless.restCheckUrl}")
	private String restCheckUrl;
	
	@Value("${passwordless.pushConnectorUrl}")
	private String pushConnectorUrl;
	
	@Value("${passwordless.recommend}")
	private String recommend;
	
	// Passwordless URL
		private String isApUrl = "/ap/rest/auth/isAp";									// Check Passwordless Registration Status
		private String joinApUrl = "/ap/rest/auth/joinAp";								// Passwordless Registration REST API
		private String withdrawalApUrl = "/ap/rest/auth/withdrawalAp";					// Passwordless Deregistration REST API
		private String getTokenForOneTimeUrl = "/ap/rest/auth/getTokenForOneTime";		// Passwordless One-Time Token Request REST API
		private String getSpUrl = "/ap/rest/auth/getSp";								// Passwordless Authentication Request REST API
		private String resultUrl = "/ap/rest/auth/result";								// Passwordless Authentication Result Request REST API
		private String cancelUrl = "/ap/rest/auth/cancel";

		@PostMapping("/dologin")
		        public String doLogin(HttpSession session, @RequestBody MemberVO vo) {
		            Map<String, Object> result = loginService.loginCheck(vo);

		            if (result != null && result.get("CNT") != null) {
		                int cnt = ((Number) result.get("CNT")).intValue();
		                if (cnt == 1) {
		                    vo.setMnum(Integer.parseInt(String.valueOf(result.get("MNUM"))));
		                    vo.setMname(result.get("MNAME").toString());
		                    vo.setColor(result.get("COLOR").toString());
		                    session.setAttribute("loginMember", vo);
		                    return "success";
		                }
		            }
		            return "fail";
		        }


	@GetMapping("/dologout")
	public String doLogout(HttpSession session, HttpServletRequest request,
			@RequestHeader("User-Agent") String userAgent) {
		System.out.println("로그아웃 처리 완료!");
		session.invalidate();
		return "logout";
	}

	@GetMapping("/session")
	public MemberVO session(HttpSession session) {
		MemberVO loginMember = (MemberVO) session.getAttribute("loginMember");
		if (loginMember != null) {
			loginMember.setPassword(null);
		}
		return loginMember; // name,userid
	}

	@PostMapping("/changePassword")
	public ResponseEntity<?> changePassword(@RequestBody MemberVO vo) {
		
		return ResponseEntity.ok("변경되었습니다");
	}


	@PostMapping("/signup")
	public Map<String, Object> join(@RequestBody MemberVO userinfo) {

	    Map<String, Object> mapResult = new HashMap<>();

	    if(userinfo.getMid() == null || userinfo.getPassword() == null) {
	        mapResult.put("result", "FAIL");
	        return mapResult;
	    }

	    MemberVO existUser = dao.getUserInfo(userinfo);

	    if(existUser != null) {
	        mapResult.put("result", "DUPLICATE");
	    } else {
	        dao.createUserInfo(userinfo);
	        mapResult.put("result", "OK");
	        System.out.println("회원가입완료");
	    }

	    return mapResult;
	}
	@PostMapping("/getLog")
	public MyLoginLoggerVO getLoginLog() {
		return dao.getLoginLog();
	}

	@PostConstruct
	public void checkKey() {
	    log.info("Passwordless serverKey length = {}", serverKey.length());
	    log.info("Passwordless serverKey = [{}]", serverKey);
	}

	// Login
	@PostMapping(value="/passwordlessManageCheck", produces="application/json;charset=utf8")
	public Map<String, Object> passwordlessManageCheck(
	        @RequestParam(value = "mid", required = false) String mid,
	        HttpServletRequest request) {

	    Map<String, Object> mapResult = new HashMap<>();

	    // 1. ID가 없는 경우
	    if(mid == null || mid.isEmpty()) {
	        mapResult.put("result", messageUtils.getMessage("text.passwordless.empty")); // "ID is empty"
	        return mapResult;
	    }

	    // 2. DB에서 사용자 존재 여부 확인
	    MemberVO user = loginService.findByMid(mid);
	    if(user == null) {
	        mapResult.put("result", messageUtils.getMessage("text.passwordless.invalid")); // "Invalid ID"
	        return mapResult;
	    }

	    // 3. Passwordless 토큰 발급
	    String token = UUID.randomUUID().toString();
	    long nowTime = System.currentTimeMillis();

	    HttpSession session = request.getSession(true);
	    session.setAttribute("PasswordlessToken", token);
	    session.setAttribute("PasswordlessTime", nowTime);

	    mapResult.put("PasswordlessToken", token);
	    mapResult.put("result", "OK");

	    // 로그 확인용
	    log.info("passwordlessManageCheck : mid [{}] Token [{}] issued at [{}]", mid, token, nowTime);

	    return mapResult;
	}


	
	@RequestMapping(value="/passwordlessCallApi")
	public ModelMap passwordlessCallApi(
			@RequestParam(value = "url", required = false) String url,
			@RequestParam(value = "params", required = false) String params,
			HttpServletRequest request, HttpServletResponse response){

		ModelMap modelMap = new ModelMap();
		String result = "";

		boolean existMember = false;
		
		if(url == null)		url = "";
		if(params == null)	params = "";
		
		Map<String, String> mapParams = getParamsKeyValue(params);
		
		String userId = "";
		String userToken = "";
		
		HttpSession session = request.getSession();
		String sessionUserToken = (String) session.getAttribute("PasswordlessToken");
		String sessionTime = String.valueOf( session.getAttribute("PasswordlessTime"));
		
		if(sessionUserToken == null)	sessionUserToken = "";
		if(sessionTime == null)			sessionTime = "";
		
		long nowTime = System.currentTimeMillis();
		long tokenTime = 0L;
		int gapTime = 0;
		try {
			tokenTime = Long.parseLong(sessionTime);
			gapTime = (int) (nowTime - tokenTime);
		}
		catch(Exception e) {
			gapTime = 99999999;
		}
		
		userId = mapParams.get("userId");
		userToken = mapParams.get("token");
		
		boolean matchToken = false;
		if(!sessionUserToken.equals("") && sessionUserToken.equals(userToken))
			matchToken = true;
		
		log.info("passwordlessCallApi : [" + url + "] userId=" + userId + ", Token Match [" + matchToken + "] userToken[" + userToken + "], sessionUserToken [" + sessionUserToken + "], gapTime [" + gapTime + "]");
		
		if(userId == null)		userId = "";
		if(userToken == null)	userToken = "";
		
		// Identity verification for QR request and cancellation
		if (url.equals("joinApUrl") || url.equals("withdrawalApUrl")) {
		    // If the user is not logged in for Passwordless settings
		    if (!matchToken) {
		        modelMap.put("result", messageUtils.getMessage("text.passwordless.abnormal")); // This is not a normal user.
		        return modelMap;
		    }
		    // If more than 5 minutes have passed after logging in for Passwordless settings, handle as Timeout
		    else if (gapTime > 5 * 60 * 1000) {
		        modelMap.put("result", messageUtils.getMessage("text.passwordless.expired")); // Passwordless management token expired.
		        return modelMap;
		    }
		}

		
		if(!url.equals("resultUrl")) {
			log.info("passwordlessCallApi : url [" + url + "] params [" + params + "] userId [" + userId + "]");
		}
		
		MemberVO userinfo = new MemberVO();
		userinfo.setMid(userId);
		MemberVO newUserinfo = loginService.getUserInfo(userinfo);
		
		if(newUserinfo == null) {
			String tmp_result = messageUtils.getMessage("text.passwordless.idnotexist");	// ID [" + id + "] does not exist.
			modelMap.put("result", tmp_result.replace("@@@", userId));
			return modelMap;
		}
		else {
			String random = java.util.UUID.randomUUID().toString();
	    	String sessionId = System.currentTimeMillis() + "_sessionId";
	    	String apiUrl = "";
	    	String ip = request.getRemoteAddr();
	    	
	    	if(ip.equals("0:0:0:0:0:0:0:1"))
	    		ip = "127.0.0.1";
	    	
	    	if(url.equals("isApUrl"))				{ apiUrl = isApUrl; }
			if(url.equals("joinApUrl"))				{ apiUrl = joinApUrl; }
			if(url.equals("withdrawalApUrl"))		{ apiUrl = withdrawalApUrl; }
			if(url.equals("getTokenForOneTimeUrl"))	{ apiUrl = getTokenForOneTimeUrl; }
			if(url.equals("getSpUrl"))				{ apiUrl = getSpUrl; params += "&clientIp=" + ip + "&sessionId=" + sessionId + "&random=" + random + "&password="; }
			if(url.equals("resultUrl"))				{ apiUrl = resultUrl;}
			if(url.equals("cancelUrl"))				{ apiUrl = cancelUrl;}
			
			if(!url.equals("resultUrl")) {
				log.info("passwordlessCallApi : url [" + url + "], param [" + params + "], apiUrl [" + apiUrl + "]");
			}
			
			if(!apiUrl.equals("")) {
				try {
					if(!url.equals("getSpUrl") && !url.equals("resultUrl")) {
						log.info("passwordlessCallApi : url [" + (restCheckUrl + apiUrl + "?" + params) + "]");
					}

					result = callApi("POST", restCheckUrl + apiUrl, params);
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
			
			if(!url.equals("getSpUrl") && !url.equals("resultUrl")) {
				log.info("passwordlessCallApi : result [" + result + "]");
			}

			// One-Time Token Request
			if(url.equals("getTokenForOneTimeUrl")) {
				String token = "";
				String oneTimeToken = "";
				
				JSONParser parser = new JSONParser();
				try {
					JSONObject jsonResponse = (JSONObject)parser.parse(result);
				    JSONObject jsonData = (JSONObject) jsonResponse.get("data");
				    token = (String) (jsonData).get("token");
				    oneTimeToken = getDecryptAES(token, serverKey.getBytes());
				} catch(ParseException pe) {
					pe.printStackTrace();
				}
				log.info("passwordlessCallApi : token [" + token + "] --> oneTimeToken [" + oneTimeToken + "]");
				
				modelMap.put("oneTimeToken", oneTimeToken);
			}
			
			// Passwordless Authentication Request REST API
			if(url.equals("getSpUrl")) {
				modelMap.put("sessionId", sessionId);
			}
			
			// Passwordless QR Registration Waiting WebSocket URL
			if(url.equals("joinApUrl")) {
				modelMap.put("pushConnectorUrl", pushConnectorUrl);
			}
			
			// Passwordless QR Registration Waiting WebSocket URL
			if(url.equals("isApUrl")) {
				log.info("passwordlessCallApi : mapParams=" + mapParams.toString());
				try {
					String isQRReg = mapParams.get("QRReg");
					if(isQRReg.equals("T")) {
						JSONParser parser = new JSONParser();
						JSONObject jsonResponse = (JSONObject)parser.parse(result);
					    JSONObject jsonData = (JSONObject) jsonResponse.get("data");
					    boolean exist = (boolean) (jsonData).get("exist");
					    
					    if(exist) {
					    	  // Change password after QR registration is complete
					    	log.info("passwordlessCallApi : QR Registration Complete --> Change Password");
					    	String newPw = Long.toString(System.currentTimeMillis()) + ":" + userId;
							userinfo.setMid(userId);
							userinfo.setPassword(newPw);
							loginService.changePassword(userinfo);
					    }
					}
				}
				catch(NullPointerException npe) {
					//
				} catch(ParseException pe) {
					//
				}
			}
			
			// Passwordless Approval Waiting
			if(url.equals("resultUrl")) {
				JSONParser parser = new JSONParser();
				
				try {
					JSONObject jsonResponse = (JSONObject)parser.parse(result);
				    JSONObject jsonData = (JSONObject) jsonResponse.get("data");
				    
				    if(jsonData != null) {
					    String auth = (String) (jsonData).get("auth");
					    
					    if(auth != null && auth.equals("Y")) {
					    	// Change password upon successful login
					    	log.info("passwordlessCallApi : Login Success --> Change Password");
							String newPw = Long.toString(System.currentTimeMillis()) + ":" + userId;
							userinfo = new MemberVO();
							userinfo.setMid(userId);
							userinfo.setPassword(newPw);
							loginService.changePassword(userinfo);
							
							Map<String, Object> result1 = loginService.loginCheck(userinfo);
	                        if (result != null && result1.get("CNT") != null) {
	                            int cnt = ((Number) result1.get("CNT")).intValue();
	                            if (cnt == 1) {                          	
	                            	userinfo.setMname(result1.get("MNAME").toString());
	                            	
	                                session.setAttribute("loginMember", userinfo);
							
						}
					    
                            }
                        }
				    }
				} catch(ParseException pe) {
					pe.printStackTrace();
				}
			}

			modelMap.put("result", "OK");
		}
		
		JSONParser parser = new JSONParser();
        JSONObject jsonResult;
        try {
            jsonResult = (JSONObject) parser.parse(result);
            modelMap.put("data", jsonResult.get("data"));
            modelMap.put("result", jsonResult.get("result"));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

		return modelMap;
	}
	
	public String callApi(String type, String requestURL, String params) {

 		String retVal = "";
 		Map<String, String> mapParams = getParamsKeyValue(params);

 		try {
			URIBuilder b = new URIBuilder(requestURL);
			
			Set<String> set = mapParams.keySet();
			Iterator<String> keyset = set.iterator();
			while(keyset.hasNext()) {
				String key = keyset.next();
				String value = mapParams.get(key);
				b.addParameter(key, value);
			}
			URI uri = b.build();
			
			// 신뢰할 수 없는 인증서 허용
	        SSLContext sslContext = SSLContextBuilder
	            .create()
	            .loadTrustMaterial(new TrustSelfSignedStrategy())
	            .build();
		
		    CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		    
		    org.apache.http.HttpResponse response;
		    
		    if(type.toUpperCase().equals("POST")) {
		        HttpPost httpPost = new HttpPost(uri);
		        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");
		    	response = httpClient.execute(httpPost);
		    }
		    else {
		    	HttpGet httpGet = new HttpGet(uri);
		    	httpGet.addHeader("Content-Type", "application/x-www-form-urlencoded");
		    	response = httpClient.execute(httpGet);
		    }
		    
		    HttpEntity entity = response.getEntity();
		    retVal = EntityUtils.toString(entity);
		} catch(Exception e) {
			System.out.println("Rest API call error !!!");
			System.out.println(e.toString());
		}
		
		return retVal;
 	}
	
	public Map<String, String> getParamsKeyValue(String params) {
 		String[] arrParams = params.split("&");
         Map<String, String> map = new HashMap<String, String>();
         for (String param : arrParams)
         {
         	String name = "";
         	String value = "";
         	
         	String[] tmpArr = param.split("=");
             name = tmpArr[0];
             
             if(tmpArr.length == 2)
             	value = tmpArr[1];
             
             map.put(name, value);
         }

         return map;
 	}
	
	private static String getDecryptAES(String encrypted, byte[] key) {
 		String strRet = null;
 		
 		byte[]  strIV = key;
 		if ( key == null || strIV == null ) return null;
 		try {
 			SecretKey secureKey = new SecretKeySpec(key, "AES");
 			Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
 			c.init(Cipher.DECRYPT_MODE, secureKey, new IvParameterSpec(strIV));
 			byte[] byteStr = java.util.Base64.getDecoder().decode(encrypted);//Base64Util.getDecData(encrypted);
 			strRet = new String(c.doFinal(byteStr), "utf-8");
 		} catch (Exception e) {
 			e.printStackTrace();
 		}
 		return strRet;	
 	}
}