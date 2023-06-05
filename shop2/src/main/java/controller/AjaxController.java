package controller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.Buffer;
import java.security.Provider.Service;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.qos.logback.classic.spi.STEUtil;
import logic.ShopService;

/* 
 *  @Controller : @Component + Controller 기능
 *     호출 되는 메서드 리턴 타입: ModelAndView : 뷰 이름 + 데이터
 *     호출 되는 메서드 리턴 타입: String       : 뷰 이름
 *     
 *  @RestController :  @Component + Controller 기능 + 클라이언트에 데이터를 직접 전달
 *  호출 되는 메서드 리턴 타입: String    : 클라이언트에 전달되는 문자열 값
 *  호출 되는 메서드 리턴 타입: Object    : 클라이언트에 전달되는 값.(JSON 형태)
 *  
 *  Spring 4.0 이후에 추가
 *  Spring 4.0 이전 버전에서는 @ResponseBody 기능으로 설정하였음
 *  @ResponseBody 어노테이션은 메서드에 설정함
 *
 */
@RestController // 뷰단 필요 없이 컨트롤러에서 직접 한다.
@RequestMapping("ajax")
public class AjaxController {
	
	ShopService service;
	@Autowired
	@RequestMapping("select") //서비스 안에 내용을 쓸 수 있게

	public List<String> select(String si, String gu, HttpServletRequest request) {
		BufferedReader fr = null;
		String path = request.getServletContext().getRealPath("/") + "file/sido.txt"; // sido.txt라는 파일을 읽어서 한 줄 씩 가져와
		try {
			fr = new BufferedReader(new FileReader(path));
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Set : 중복 불가
		// LinkedHashset : 순서유지. 중복 불가. 리스트아님(첨자 사용 안 됨).
		Set<String> set = new LinkedHashSet<>();
		String data = null;
		if (si == null && gu == null) { // 첫번째 시도 선택하기
			try {
				while ((data = fr.readLine()) != null) { // fr.readLine() : 한줄씩 읽어라
					String[] arr = data.split("\\s+"); // \\s+ : 공백 한 개 이상
					// 공백으로 분리해서 배열로 만들어라
					if (arr.length >= 3)
						set.add(arr[0].trim()); // 중복 제거 됨.
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (gu == null) { // si 파라미터 존재
			si = si.trim();
			try {
				while ((data = fr.readLine()) != null) {
					String[] arr = data.split("\\s+");
					if (arr.length >= 3 && arr[0].equals(si) && !arr[1].contains(arr[0])) {
						set.add(arr[1].trim());

					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else { // si 파라미터,gu 파라미터 존재 => 구군선택 : 동값 전송
			si = si.trim();
			gu = gu.trim();
			try {
				while ((data = fr.readLine()) != null) {
					String[] arr = data.split("\\s+");
					if (arr.length >= 3 && arr[0].equals(si) && arr[1].equals(gu) && !arr[0].equals(arr[1])
							&& !arr[2].contains(arr[1])) {
						if (arr.length > 3) {
							if (arr[3].contains(arr[1]))
								continue;
							arr[2] += " " + arr[3];
						}
						set.add(arr[2].trim());
					}

				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		List<String> list = new ArrayList<>(set); // set 객체 => list 객체로 변경
		return list; // 리스트 객체가 브라우저에 전달. 뷰가 아님
						// pom.xml의 fasterxml.jackson...의 설정에 의해서 브라우저는 배열로 인식함
	}

//	@RequestMapping("select2") //클라이언트로 문자열 전송.인코딩 설정이 필요.
	/*
	 * produces : 클라이언트에 전달되는 데이터의 특징을 설정 text/plain : 데이터 특징. 순수문자.
	 * 
	 * text/html : html 형식의 문자. text/xml : xml 형식의 문자. -- 이 두개는 뷰단 만드는게 좋다?
	 * 
	 * charset=utf-8 : 한글은 utg-8로 인식
	 */
	@RequestMapping(value = "select2", produces = "text/plain; charset=utf-8")
	public String select2(String si, String gu, HttpServletRequest request) {
		BufferedReader fr = null;
		String path = request.getServletContext().getRealPath("/") + "file/sido.txt"; // sido.txt라는 파일을 읽어서 한 줄 씩 가져와
		try {
			fr = new BufferedReader(new FileReader(path));
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Set : 중복 불가
		// LinkedHashset : 순서유지. 중복 불가. 리스트아님(첨자 사용 안 됨).
		Set<String> set = new LinkedHashSet<>();
		String data = null;
		if (si == null && gu == null) {
			try {
				while ((data = fr.readLine()) != null) {
					String[] arr = data.split("\\s+"); // \\s+ : 공백 한 개 이상
					// 공백으로 분리해서 배열로 만들어라
					if (arr.length >= 3)
						set.add(arr[0].trim()); // 중복 제거 됨.
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		List<String> list = new ArrayList<>(set); // set 객체 => list 객체로 변경
		return list.toString(); // list에 있는 내용을 문자열로 바꿔라 - ["서울특별시","경기도"....] :
	}

	@RequestMapping(value = "exchange", produces = "text/html; charset=utf-8") // 한글로 보내고 있어서?sitemesh에서 설정 해줘야 됨
	public String exchange() {
		Document doc = null;
		List<List<String>> trlist = new ArrayList<>(); // 미국,중국,일본,유로 통화들만 저장 목록
		String url = "https://www.koreaexim.go.kr/wg/HPHKWG057M01#tab1";
		String exdate = null;
		try {
			doc = Jsoup.connect(url).get();
			Elements trs = doc.select("tr"); // tr 태그들을 가져와서 Elements에 넣는다?
			// p.table-unit : class 속성의 값이 table-unit 인 p태그
			exdate = doc.select("p.table-unit").html(); // 조회기준일 : 2023.06.02
			for (Element tr : trs) {
				List<String> tdlist = new ArrayList<>(); // tr태그의 td 태그 목록을 가지고 있다.
				Elements tds = tr.select("td"); // tr태그의 하위 td 태그들
				for (Element td : tds) {
					tdlist.add(td.html()); //
				}
				if (tdlist.size() > 0) {
					if (tdlist.get(0).equals("USD") // 미달러 통화코드
							|| tdlist.get(0).equals("CNH") // 중국 통화코드
							|| tdlist.get(0).equals("JSP(100)") // 일본 통화코드
							|| tdlist.get(0).equals("EUR")) { // 유로의 통화코드
						trlist.add(tdlist);

					}

				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		StringBuilder sb = new StringBuilder();
		sb.append("<h4 class='w3-center'>수출입은행<br>" + exdate + "</h4>");
		sb.append("<table class='w3-table-all'>");
		sb.append("<tr><th>통화</th><th>기준율</th><th>받으실때</th><th>보내실때</th></tr>");
		for (List<String> tds : trlist) {
			sb.append("<tr><td>" + tds.get(0) + "<br>" + tds.get(1) + "</td><td>" + tds.get(4) + "</td>");
			sb.append("<td>" + tds.get(2) + "</td><td>" + tds.get(3) + "</td><tr>");

		}
		sb.append("</table>");
		return sb.toString();
	}



	@RequestMapping("exchange2") 
	public Map<String,Object> exchange2() {
//		public List<List<String>> exchange2() {
		Document doc = null;
		List<List<String>> trlist = new ArrayList<>(); // 미국,중국,일본,유로 통화들만 저장 목록
		String url = "https://www.koreaexim.go.kr/wg/HPHKWG057M01#tab1";
		String exdate = null;
		try {
			doc = Jsoup.connect(url).get();
			Elements trs = doc.select("tr"); // tr 태그들을 가져와서 Elements에 넣는다?
			// p.table-unit : class 속성의 값이 table-unit 인 p태그
			exdate = doc.select("p.table-unit").html(); // 조회기준일 : 2023.06.02
			for (Element tr : trs) {
				List<String> tdlist = new ArrayList<>(); // tr태그의 td 태그 목록을 가지고 있다.
				Elements tds = tr.select("td"); // tr태그의 하위 td 태그들
				for (Element td : tds) {
					tdlist.add(td.html()); //
				}
				if (tdlist.size() > 0) {
					if (tdlist.get(0).equals("USD") // 미달러 통화코드
							|| tdlist.get(0).equals("CNH") // 중국 통화코드
							|| tdlist.get(0).equals("JSP(100)") // 일본 통화코드
							|| tdlist.get(0).equals("EUR")) { // 유로의 통화코드
						trlist.add(tdlist);

					}

				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		Map<String,Object> map = new HashMap<>();
		map.put("exdate",exdate); //일자
		map.put("trlist",trlist); //환율 목록
		return map;
//		return trlist;
	}
	@RequestMapping("graph1")
	public List<Map.Entry<String, Integer>> graph1(String id) {
		Map<String,Integer> map = Service.graph1(id); //{"홍길동"=10,"김삿갓":7,.....}
		List<Map.Entry<String, Integer>> list = new ArrayList<>();
		for(Map.Entry<String, Integer> m : map.entrySet() ) {
			list.add(m);
		}
		Collections.sort(list,(m1,m2)->m2.getValue() - m1.getValue()); //등록 건 수 의 내림차순 정렬
		return list;
		
	}
}
