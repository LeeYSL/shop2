package controller;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.http.HttpSession;



import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import exception.LoginException;
import logic.Mail;
import logic.ShopService;
import logic.User;

/*
 * AdminController의 모든 메서서는 관리자 로그인이 필요함
 *  AOP로 설정 AdminLoginAspect.adminCheck() 메서드 
 *   1. 로그아웃 상태 => 로그인 하세여. login 페이지 이동
 *   2.관리자 로그인이 아닌 경우   
 * 
 */

@Controller
@RequestMapping("admin") // 요청이 admin으로 들어오면 실행해라
public class AdminController {
	@Autowired
	private ShopService service;

//	
//	@GetMapping("*") 
//	public ModelAndView join() {
//		ModelAndView mav = new ModelAndView();
//		mav.addObject(new User());
//		return mav;
//	}
	@RequestMapping("list") //get,post 둘 다 상관 없다. o
	public ModelAndView adminChecklist(String sort, HttpSession Session) {
		ModelAndView mav = new ModelAndView();
		// list : db에 등록 된 모든 회원 정보를 저장하고 있는 목록
		List<User> list = service.list(); // 회원 목록을 조회해라
		if (sort != null) {
			switch (sort) {
			case "10":
				Collections.sort(list, new Comparator<User>() {
					@Override
					public int compare(User u1, User u2) {
						return u1.getUserid().compareTo(u2.getUserid()); // 결과가 음수가 나오면 u1, 양수가 나오면 u2
					}
				}); // 기존 방식 코등
				break;

			case "11":
				Collections.sort(list, (u1, u2) -> u2.getUserid().compareTo(u1.getUserid())); // 람다식
				break;

			case "20":
				Collections.sort(list, (u1, u2) -> u1.getUsername().compareTo(u2.getUsername())); // 람다식
				break;

			case "21":
				Collections.sort(list, (u1, u2) -> u2.getUsername().compareTo(u1.getUsername())); // 람다식
				break;
			case "30":
				Collections.sort(list, (u1, u2) -> u1.getUsername().compareTo(u2.getPhoneno())); // 람다식
				break;
			case "31":
				Collections.sort(list, (u1, u2) -> u2.getPhoneno().compareTo(u1.getPhoneno())); // 람다식
				break;
			case "40":
				Collections.sort(list, (u1, u2) -> u1.getBirthday().compareTo(u2.getBirthday())); // 람다식
				break;
			case "41":
				Collections.sort(list, (u1, u2) -> u2.getBirthday().compareTo(u1.getBirthday())); // 람다식
				break;
			case "50":
				Collections.sort(list, (u1, u2) -> u1.getEmail().compareTo(u2.getEmail())); // 람다식
				break;
			case "51":
				Collections.sort(list, (u1, u2) -> u2.getEmail().compareTo(u1.getEmail())); // 람다식
				break;
			}
		}
		mav.addObject("list", list);
		return mav;
	}
	/* -메일-
	 * 전송: 
	 * 수신: 여러명
	 * 전송일: 현재
	 * 제목: title
	 * 내용: mimemuiltpart
	 * 
	 */
	@RequestMapping("mailForm") 
	public ModelAndView mailForm(String[] idchks, HttpSession session) {
		//String[] idchks : idchks 파라미터 값 여러가 가능 . request.getParamaterValues("파라미터")
		//String로 받으면 에러는 안나지만 , 로 받아옴?
		ModelAndView mav = new ModelAndView("admin/mail"); //내용,첨부파일//
		if(idchks == null || idchks.length == 0) {
			throw new LoginException("메일을 보낼 대상자를 선택하세요","list");
		}
		List<User> list = service.getUserList(idchks);
		mav.addObject("list", list);
		return mav;
	}
	@RequestMapping("mail") 
	public ModelAndView mail (Mail mail, HttpSession session) {
		ModelAndView mav = new ModelAndView("alert");
		Properties prop = new Properties();
		try {
			//mail.properties : resources 폴더에 생성
			// java,resources 폴더의 내용은 : WEB-INF/classes에 복사 됨 
			//classes : 내가 구현한 코드 
			 FileInputStream fis = new FileInputStream(session.getServletContext().getRealPath("/")+
					 "/WEB-INF/classes/mail.properties");
			 prop.load(fis);
			 prop.put("mail.smtp.user",mail.getNaverid());
		}catch (IOException e) {
			e.printStackTrace();
		}
		
		mailSend(mail,prop);
		mav.addObject("message","메일 전송이 완료 되었습니다.");
		mav.addObject("url", "list");
		return mav;
		
	}
	private boolean mailSend(Mail mail, Properties prop) {
		MyAuthenticator auth = new MyAuthenticator(mail.getNaverid(),mail.getNaverpw());
		Session session = Session.getInstance(prop,auth);
		MimeMessage msg = new MimeMessage(session);
		try {
			//보내는 메일
			msg.setFrom(new InternetAddress(mail.getNaverid() + "@naver.com"));
			//받는 메일 정보
			List<InternetAddress> addrs = new ArrayList<InternetAddress>();
			String[] emails = mail.getRecipient().split(",");
			for(String email : emails) {
				try {
					addrs.add(new InternetAddress(new String(email.getBytes("UTF-8"),"8859_1")));
				}catch (UnsupportedEncodingException ue) {
					ue.printStackTrace();
				}
			}
			InternetAddress[] arr = new InternetAddress[emails.length];
			for(int i=0;i<addrs.size();i++) {
				arr[i] = addrs.get(i);
			}
			msg.setRecipients(Message.RecipientType.TO,arr); //  수신 메일 설정
			msg.setSentDate(new Date());//메일 전송 일자
			msg.setSubject(mail.getTitle()); //제목
			MimeMultipart multipart = new MimeMultipart();
			MimeBodyPart message = new MimeBodyPart();
			message.setContent(mail.getContents(),mail.getMtype()); //메시지 부분에 내용 넣을 수 있게?
			multipart.addBodyPart(message);
			//첨부파일 추가
			for(MultipartFile mf : mail.getFile1()) {
				//mf : 첨부 된 파일 중 한 개
				if((mf != null) && (!mf.isEmpty())) {
					multipart.addBodyPart(bodypart(mf)); //multipart : 내용, 첨부파일 들어감
				}
			}
			msg.setContent(multipart);
			//msg : 메일 전체 객체(전송메일 주소, 수신메일주소,제목,전송일자,내용,첨부파일)
			Transport.send(msg);
			return true;
			
 		}catch (MessagingException me) {
			me.printStackTrace();
		}
		return false;
	}
	private BodyPart bodypart(MultipartFile mf) {
		MimeBodyPart body = new MimeBodyPart();
		String orgFile = mf.getOriginalFilename();
		String path = "c:/mailupload/";
		File f1 = new File(path);
		if(!f1.exists()) f1.mkdirs();
		File f2 = new File(path + orgFile);
		try {
			mf.transferTo(f2); //파일 업로드
			body.attachFile(f2); // 이메일 첨부
			body.setFileName(new String(orgFile.getBytes("UTF-8"),"8859_1")); //첨부 된 파일의 파일 명을 인식할 수 있도록 8859_1로 바꿔서 넣어준다.
		}catch (Exception e) {
			e.printStackTrace();
		}
		return body;
	}
//AdminCotroller의 내부 클래스
  private final class MyAuthenticator extends Authenticator { // 네이버한테 인증 클래스로 전달? 원래 classs는 private 사용 안 되는데 여기선 된다...??
	  private String id;
	  private String pw;
	  public  MyAuthenticator(String id, String pw) {
		  this.id = id;
		  this.pw = pw;

		  
		
	}
	@Override
	protected PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(id,pw);
		
	}
	  
  }
}
