package aop;


import javax.servlet.http.HttpSession;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import exception.CartEmptyException;
import exception.LoginException;
import logic.Cart;
import logic.User;

@Component //객체화
@Aspect //Aspect 클래스다.
public class CartAspect {  
	/* pointcut -
	 * Cartcontroller 클래스의 매개변수 마지막이 httpsession인 메서드 중 check로 시작하는 메서드 
     * 
	 */
	@Before("execution(* controller.Cart*.check*(..)) && args(..,session)")  
	public void cartCheck(HttpSession session) throws Throwable{ 
		//매개변수를 통해 session 객체로 카트 컨트롤러에 check로 시작하는 메서드랑 전달?
		User loginUser =(User)session.getAttribute("loginUser");
		if(loginUser == null) { //로그아웃 상태
			throw new LoginException("회원만 주문 가능합니다.","../user/login"); 
		
		}
		Cart cart = (Cart)session.getAttribute("CART");
		if(cart == null || cart.getItemSetList().size() == 0) { //카트가 비어 있거나 주문 리스트에 없으면 
			throw new CartEmptyException("장바구니에 상품이 없습니다.","../item/list");
		}
		  
	}

}
