package aop;


import exception.LoginException;
import javax.servlet.http.HttpSession;


import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import logic.User;

@Component
@Aspect

public class UserLigubAspect {

	@Before("execution(* controller.User*.idCheck*(..)) && args(..,userid,session)") // point cut
	public void userIdCheck(String userid, HttpSession session) throws Throwable {
		User loginUser = (User) session.getAttribute("loginUser"); // user 객체 가져옴
		if (loginUser == null) {
			throw new LoginException("[idCheck]로그인 후 거래하세요", "../user/login");
			// 그냥 login으로 써도 된다. user 컨트롤러의 요청읜 user기 때문에
			// [idCheck] 어떤 에러가 발생했는지 확인 위해서
		}

		if (!loginUser.getUserid().equals("admin") && (!loginUser.getUserid().equals(userid))) {
			throw new LoginException("본인만 거래 가능합니다", "../item/list");
		}
	}
    //UserController.loginCheck*(..,HttpSession session) =>pointcut
	@Before
	("execution(* controller.User*.loginCheck*(..)) && args(..,session)") 
		public void loginCheck(HttpSession session) throws Throwable{
		    User loginUser = (User)session.getAttribute("loginUser");
			
			if(loginUser == null) {
				throw new LoginException("로그인 후 거래하세요","../user/login"); 
		}
	} 
}