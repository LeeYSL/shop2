package interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import exception.LoginException;
import logic.User;

public class BoardInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle // preHandle : /board/write 요청 시 : controller.BoardController.write() 메서드 호출 전
								// 실행
	// AOP는 전이나 후에 실행하지만 Interceptor는 낚아채서 실행한다.
	(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		HttpSession session = request.getSession();
		String boardid = (String) session.getAttribute("boardid");
		User login = (User) session.getAttribute("loginUser");
		if (boardid == null || boardid.equals("1")) {
			if (login == null || !login.getUserid().equals("admin")) { // 로그인 정보 확인
				String msg = "관리자만 등록 가능합니다.";
				String url = request.getContextPath() + "/board/list?boardid=" + boardid;
				throw new LoginException(msg, url);

			}

		}
		return true; // 다음 메서드 호출 가능하다. 다음 메서드 : controller.BoardController.write 호출
	}

}
