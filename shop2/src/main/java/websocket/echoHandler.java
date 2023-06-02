package websocket;


import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class echoHandler extends TextWebSocketHandler implements InitializingBean {
//WebSocketSession : 클라이언트. 채팅 중인  브라우저 한 개
//현재 채팅중인 모든 브라우저의 세션 저장	
	private Set<WebSocketSession> clients = new HashSet<WebSocketSession>();

	@Override //소켓 접속 완료 상태
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		super.afterConnectionEstablished(session);
		System.out.println("클라이언트 접속: " + session.getId());
		clients.add(session);
	}

	@Override //클라이언트에서 메세지가 수신 된 경우
	public void handleMessage(WebSocketSession session,WebSocketMessage<?> message) throws Exception {
		//클라이언트가 전송한 메세지
		String loadMessage = (String)message.getPayload();
		System.out.println(session.getId()+":클라이언트 메세지:" + loadMessage);
		clients.add(session);
	    for(WebSocketSession s : clients) {  //브로드캐스팅
	    	s.sendMessage(new TextMessage(loadMessage)); //클라이언트에서 받은 메세지를 모든 클라이언트한테 다시 전송
	    }
		
	} 

	
	@Override
	public void handleTransportError(WebSocketSession session,Throwable exception) throws Exception {
		super.handleTransportError(session,exception);
		System.out.println("오류발생:" + exception.getMessage());
	}
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		super.afterConnectionClosed(session, status);
		System.out.println("클라이언트 접속 해제:" + status.getReason());
		clients.remove(session);
	}
 	@Override //구현 안 하면 에러난다.
 	public void afterPropertiesSet() throws Exception {}
 
}
