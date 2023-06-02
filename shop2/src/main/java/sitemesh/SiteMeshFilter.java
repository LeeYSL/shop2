package sitemesh;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

import org.sitemesh.builder.SiteMeshFilterBuilder;
import org.sitemesh.config.ConfigurableSiteMeshFilter;

@WebFilter("/*")
public class SiteMeshFilter extends ConfigurableSiteMeshFilter{
	
	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) //filterChain: 다음 단계
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest)servletRequest;
		String url = request.getRequestURI(); //요청 된 url 정보 
		if(url.contains("/user/")) url="user";
		else if(url.contains("/admin/")) url="user";
		else if(url.contains("/board/")) url="board";
		else if(url.contains("/item/")) url="item";
		else if(url.contains("/cart/")) url="item";
		else if(url.contains("/chat/")) url="chat";
		else url=""; //빈 url null로 넣어도 상관 없다.
		request.setAttribute("url", url); // 속성 등록
		super.doFilter(servletRequest, servletResponse, filterChain); //다음 프로세스 진행
	}
	@Override
	protected void applyCustomConfiguration(SiteMeshFilterBuilder builder) {
		builder.addDecoratorPath("/*", "/layout/gdulayout.jsp")
		.addExcludedPath("/user/id*") //sitemesh 대상이 아님
		.addExcludedPath("/user/pw*")
		.addExcludedPath("/ajax/*");

	}
} 