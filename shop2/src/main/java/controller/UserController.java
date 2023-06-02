package controller;

import java.util.List;

import javax.security.auth.message.callback.PrivateKeyCallback.Request;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.websocket.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.annotation.SessionScope;
import org.springframework.web.servlet.ModelAndView;

import exception.LoginException;
import logic.Sale;
import logic.ShopService;
import logic.User;

@Controller
@RequestMapping("user") //get,post 둘 다 상관 없다.
public class UserController {
	@Autowired
	private ShopService service;

	@GetMapping("*") // 메서드가 get 방식 일 때
	// @PostMapping("*") => 컨트롤러에 설정되지 않은 모든 요청시 호출 되는 메서드
	public ModelAndView join() {
		ModelAndView mav = new ModelAndView();
		mav.addObject(new User());
		return mav;

	}

	@PostMapping("join")// 메서드가 post 방식일 때
	public ModelAndView ueseAdd(@Valid User user, BindingResult bresult) { //@Valid User user BindingResult bresult  가 있어야 유효성 검사한 결과를 넣을 수 있다.
		ModelAndView mav = new ModelAndView();
		if (bresult.hasErrors()) {
			mav.getModel().putAll(bresult.getModel());
			// reject 메서드 : global error에 추가
			bresult.reject("error.input.user");
			bresult.reject("error.input.check");
			return mav;
		}
		// 정상 입력갑 : 회원 가입 하기 => db의 useraccount 테이블에 저장
		try {
			service.userInsert(user); // user : 화면에서 입력 되고 있는 내용
			mav.addObject("user", user);
		} catch (DataIntegrityViolationException e) {
			// DataIntegrityViolationException : db에서 중복 key 오류시 발생되는 예외 객체
			e.printStackTrace();
			bresult.reject("error.duplicate.user"); // global 오류 등록
			mav.getModel().putAll(bresult.getModel());
			return mav;
		}
		mav.setViewName("redirect:login");
		return mav;
	}

	@PostMapping("login")
	public ModelAndView login(@Valid User user, BindingResult bresult, HttpSession session) {
		ModelAndView mav = new ModelAndView();

		if (bresult.hasErrors()) {
			mav.getModel().putAll(bresult.getModel());
			bresult.reject("error.login.input");
			return mav;
		}
		/*
		 * 1.userid에 맞는 user를 db에서 조회하기
		 */
		try {
			User dbUser = service.selectUserOne(user.getUserid());
		} catch (EmptyResultDataAccessException e) { // 조회된 데이터 없는 경우 발생
			e.printStackTrace();
			bresult.reject("error.login.id"); // id를 확인하세요.
			mav.getModel().putAll(bresult.getModel());
			return mav;
		}
		/*
		 * 2. 비밀번호 검증 일치 : session.setAttribute("loginUser",dbUser) = > 로그인 정보 불일치 :
		 * 비밀번호를 확인하세요. 출력(error.login.password)
		 * 
		 * 3.mypage로 페이지 이동 => 현재는 404오류 발생
		 */

		User dbUser = service.selectUserOne(user.getUserid());
		if (dbUser.getPassword().equals(user.getPassword())) {
			session.setAttribute("loginUser", dbUser);
			mav.setViewName("redirect:mypage?userid=" + user.getUserid());
//				System.out.println("db:" + dbUser.getPassword());
//			    System.out.println("user:"+user.getPassword());
//			    System.out.println(dbUser.getPassword().equals(user.getPassword()));
//			  
		} else {
			bresult.reject("error.login.pass");
			mav.getModel().putAll(bresult.getModel());
		}

		return mav;

//		try {
//			User dbUser = service. selectUserpass(user.getUserid(),user.getPassword());
//			User loginUser=(User)session.getAttribute("loginUser");
//			session.setAttribute("loginUser", dbUser);
//		
//		} catch (EmptyResultDataAccessException e) {
//			e.printStackTrace();
//			bresult.reject("error.login.pass");
//			mav.getModel().putAll(bresult.getModel());
//			return mav;
//
//		}
//		mav.setViewName("redirect:mypage");
//		return mav;
//	}
		/*
		 * AOP 설정하기 UserLigubAspect 클래스의 userIdCheck 메서드로 구현하기 1.pointcut :
		 * UserController 클래스의 idcheck로 시작하는 메서드이고, 마지막 매개변수의 userid,session인 경우 2.로그인
		 * 여부 검증 - 로그인이 안 된 경우 로그인 후 거래하세요. 메세지 출력. login 페이지 호출 3.admin이 아니면서, 로그인 아이디와
		 * 파라미터 userid 값이 다른 경우 - 본인만 거래 가능합니다. 메세지 출력, item.list 페이지 호출
		 */
	}

	@RequestMapping("mypage")
	public ModelAndView idCheckMypage(String userid, HttpSession session) {
		ModelAndView mav = new ModelAndView();
		User user = service.selectUserOne(userid);
		List<Sale> salelist = service.salelist(userid);
		mav.addObject("user", user); // 회원정보데이터
		mav.addObject("salelist", salelist); // 주문목록
		return mav;
	}

	/*
	 * 로그아웃 로그아웃 후 login 페이지로 이동
	 */
	@RequestMapping("logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:login";

	}

	// 로그인 상태, 본인 정보만 조회 => AOP 클래스(UserLoginAsopect.userIdCheck() 검증)
	@GetMapping({ "update", "delete" })
	public ModelAndView idCheckUser(String userid, HttpSession session) {
		ModelAndView mav = new ModelAndView();
		User user = service.selectUserOne(userid);
		List<Sale> salelist = service.salelist(userid);
		mav.addObject("user", user);
		return mav;
	}

//	@PostMapping("update")
//	public ModelAndView update(@Valid User user, BindingResult bresult, String userid,
//			HttpSession session) {
//		ModelAndView mav = new ModelAndView();
//		
//		if (bresult.hasErrors()) {
//			mav.getModel().putAll(bresult.getModel());
//			bresult.reject("error.update.check");
//			return mav;
//		} 
//		  //비밀번호 검증
//		   User loginUser = (User)session.getAttribute("loginUser");
//		if (!loginUser.getPassword().equals(user.getPassword())) {
//			mav.getModel().putAll(bresult.getModel());
//			bresult.reject("error.input.pass");
//			return mav;
//			
//		}
//		//비밀번호 일치 => 데이터 수정
//		try {
//			service.userUppdate(user); //user = > 입력된 데이터
//			if(!loginUser.getUserid().equals(user.getUserid())) //입력된 정보랑 로그인 정보랑 같으면?
//		     	session.setAttribute("loginUser", loginUser);
//				mav.getModel().putAll("redirect:mypage?userid="+user.getUserid());
//			
//			}catch (Exception e) {
//				e.printStackTrace();
//			  throw new LoginException
//			  ("고객 정보 수정 실패","update?userid="+user.getUserid());
//			}
//		return mav;
//		
//		}
	@PostMapping("update")
	public ModelAndView idCheckUpdate(@Valid User user, BindingResult bresult, HttpSession session) {
		ModelAndView mav = new ModelAndView();
		if (bresult.hasErrors()) {// 입력값 검증
			mav.getModel().putAll(bresult.getModel());
			bresult.reject("error.update.user");
			return mav;
		}
		try {
			User dbUser = service.selectPassOne(user.getUserid(), user.getPassword());
			User loginUser = (User) session.getAttribute("loginUser");
			session.setAttribute("loginUser", dbUser);
			service.userUpdate(user); // 화면에서 user 정보 들어있는 user 객체
			mav.addObject("user", user);
		} catch (EmptyResultDataAccessException e) {
			e.printStackTrace();
			bresult.reject("error.login.pass");
			mav.getModel().putAll(bresult.getModel());
			return mav;
		}
		mav.setViewName("redirect:mypage?userid=" + user.getUserid());
		return mav;
	}

	/*
	 * 탈퇴 검증 UserLoginAspect.userIdCheck() 메서드 실행 하도록 설정
	 * 
	 * 회원탈퇴 1.파라미터 정보 저장(userud, password) -관리자인 경우 탈퇴 불가
	 * 
	 * 2.비밀번호 검증 => 로그인 된 비밀번호 검증 본인 탈퇴: 본인 비밀번호 관리자가 타인 탈퇴 : 관리자 비밀번호
	 * 
	 * 3.비밀번호 불일치 메세지 출력 후 delete 페이지 이동
	 * 
	 * 일치 : db에서 해당 사용자 정보 삭제하기 본인탈퇴 : 로그아웃, login 페이지 이동 관리자 탈퇴 : admin/list 페이지
	 * 이동=> 404오류 발생
	 * 
	 */
//	@PostMapping("delete")
//	public ModelAndView idCheckdelete(@Valid User user, BindingResult bresult, HttpSession session) {
//		ModelAndView mav = new ModelAndView();
//		
//		 try {
//		  User dbUser = service.selectPassOne(user.getUserid(), user.getPassword());
//		  session.setAttribute("Userid", dbUser);
//		  service.userDelete(user); 
//			mav.addObject("user", user);
//			
//		 }catch (Exception e) {
//			 e.printStackTrace();
//				throw new LoginException
//				("비밀번호 오류","delete?userid="+user.getUserid());
//		}
//		 session.invalidate();
//		 mav.setViewName("redirect:login");
//		 return mav;
//}
	@PostMapping("delete")
	public String idCheckDelete(String password, String userid, HttpSession session) {
		// () 위에 있는 괄호에 있는거는 파라미터 값을 의미
		// aop 때문에 password, userid, session 순서로 만들었다

		// 관리자 탈퇴 불가
		if (userid.equals("admin"))
			throw new LoginException("관리자 탈퇴는 불가합니다.", "mypage?userid=" + userid);

		// 2. 비밀번호검증 : 로그인된 정보
		User loginUser = (User) session.getAttribute("loginUser");
		// session으로부터 로그인 정보 가져옴
		// password : 입력된 비밀번호
		// loginUser.getPassword() : 로그인 사용자의 비밀번호

		if (!password.equals(loginUser.getPassword())) {
			throw new LoginException("비밀번호를 확인하세요.", "delete?userid=" + userid);
		}

		// 4. 비밀번호 일치 : 고객정보 제거
		try {
			service.userDelete(userid);
		} catch (DataIntegrityViolationException e) {
			throw new LoginException("주문정보가 존재합니다. 탈퇴 불가 합니다. 관리자 연락 요망", "mypage?userid=" + userid);
		} catch (Exception e) {
			e.printStackTrace();
			throw new LoginException("탈퇴시 오류발생.", "delete?userid=" + userid);
		}

		// 탈퇴 성공 : 회원정보가 제거 된 상태
		if (loginUser.getUserid().equals("admin")) { // 관리자가 회원을 강제 탈퇴 한 경우 => 아직 코딩안해서 404 뜰거임
			return "redirect:../admin/list";
		} else { // 본인탈퇴
			session.invalidate();
			return "redirect:login";
		}
	}

	/*
	 * UserLoginAspect. loginCheck() : UserController.loginCheck*(..) 인 메서드 마지막 매개변수
	 * HttpSession인 메서드 1.로그인 검증 => AOP 클래스 2.파라미터 값 저장(매개변수) 3.현재 비밀번호와 입력된 비밀번호 검증
	 * 불일치 : 오류 메세지 출력. password 페이지 이동
	 * 
	 * 4.일치 : db 수정 5.성공 : 로그인 정보 변경, mypage 페이지 이동 실패 : 오류 메세지 츌력. password 페이지 이동
	 * 
	 */
	@PostMapping("password")
	public String loginCheckpw(String password, HttpSession session, String chgpass, String chgpass2) {
		// 비밀번호 검증
		User loginUser = (User) session.getAttribute("loginUser");
//		if (!password.equals(loginUser.getPassword())) {
//			//password : 현재비밀번호
//			//loginUser : 로그인 된 비밀번호
//			throw new LoginException("비밀번호를 확인하세요", "password");
//		}
		try {
			service.passupdate(loginUser.getUserid(), chgpass);
			loginUser.setPassword(chgpass);// 로그인 정보에 비밀번호 수정

		} catch (Exception e) {

			throw new LoginException("비밀번호 변경 시  오류발생.", "password");

		}
		return ("redirect:mypage?userid=" + loginUser.getUserid());
	}

	@PostMapping("{url}search") // ${url} = String url
	// ${url}seach : url 지정 x url 상관 없이 (*) search 인 요청시 호출 되는 메서드
	public ModelAndView search(User user, BindingResult bresult, @PathVariable String url) { //user 앞에 @Valid가 없으면 직접 유효성 검사 하겠다.?
		// @PathVariable : {url}의 이름을 매개변수로 전달
		// ex) 요청이 idsearch인 경우 url <= "id" pwsearch인 경우 url <= "pw"
		ModelAndView mav = new ModelAndView();
		String code = "error.userid.search";
		String title = "아이디"; // 어떤 찾기인지 title이 값을 가지고 있다.
		if (url.equals("pw")) {// 비밀번호 검증인 경우
			title = "비밀번호";
			code = "eroor.password.search";
			if (user.getUserid() == null || user.getUserid().trim().equals("")) {
				// BindingResult.reject() : global error
				// => jsp의 <spring:hasBindErrors ..부분에 오류 출력

				// BindingResult.rejectValue() :
				// =>jsp의 <formLerrors path= ... 부분에 오류 출력
				bresult.rejectValue("userid", "error.required"); // error.required.userid 오류코드
			}
		}
		if (user.getEmail() == null || user.getEmail().trim().equals("")) {
			bresult.rejectValue("email", "error.required"); // error.required.email 오류 코드
		}
		if (user.getPhoneno() == null || user.getPhoneno().trim().equals("")) {
			bresult.rejectValue("phoneno", "error.required");// error.required.phonene 오류 코드
		}
		if (bresult.hasErrors()) {
			mav.getModel().putAll(bresult.getModel());
			return mav;
		}
		// 입력 검증 정상 완료
		if (user.getUserid() != null && user.getUserid().trim().equals("")) {
			user.setUserid(null);
		}
		/*
		 * user.getUserid() == null : 아이디 찾기 =? 아이 user.getUserid() != null : 비밀번호 찾기 =
		 * > 비밀번호
		 */
		String result = null;
		try {
			result = service.getSearch(user);
		} catch (EmptyResultDataAccessException e) {
			bresult.reject(code); 
			mav.getModel().putAll(bresult.getModel());
			return mav;
		}
		mav.addObject("result", result);
		mav.addObject("title", title);
		mav.setViewName("search"); // 어떤 search로 들어와도 그냥 search?
		return mav;
		
	}
}
