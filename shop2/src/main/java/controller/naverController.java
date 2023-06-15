package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("naver")
public class naverController {
	@GetMapping("*") // localhost : 8080/naver/search 요청이 들어오면 => /WEB-INF/view/naver/search.jsp 뷰
	public String naver() {
		return null; // 뷰 이름 의미 함 null: url과 같은 이름의 뷰를 선택
	}

	@RequestMapping("naversearch") //naver/search.jsp 페이지에서 ajax으로 요청됨 뷰 없이 직접 json객체 전달
	@ResponseBody // 뷰 없이 바로 데이터를 클라이언트로 전송(데이터를 전송할때도 있고 뷰를 전송할 떄도 있을 때) ,@RestController랑 같은 의미
	public JSONObject naversearch(String data, int display, int start, String type) {


		String clientId = "hVPEuSehqw4ke0yCgOaL";
		String clientSecret = "uGdJ02YeFf";
		StringBuffer json = new StringBuffer();
		int cnt = (start - 1) * display + 1; //네이버에 요청 시작 건 수 
		String text = null;
		try {
			text = URLEncoder.encode(data, "UTF-8");
			String apiURL = "https://openapi.naver.com/v1/search/" + type + ".json?query=" // 유형 여러개 가져오려고 type로 바꿔줌
					+ text + "&display=" + display + "&start=" + cnt; // json 결과

			URL url = new URL(apiURL);

			HttpURLConnection con = (HttpURLConnection) url.openConnection(); //apiURL에 접속 됨
			con.setRequestMethod("GET");
			con.setRequestProperty("X-Naver-Client-Id", clientId);
			con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
			int responseCode = con.getResponseCode(); // 네이버에서 응답 코드
			BufferedReader br; // 네이버가 전송한 데이터. 네이버에서 수신 된 데이터
			if (responseCode == 200) {// 정상 처리, 네이버가 알려줌
				// con.getInputStream() : 입력 스트림. 네이버 데이터 수신
				br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));

			} else { // 검색 시 오류 발생,네이버에서 알려줌
				// con.getErrorStream() : 입력 스트림. 네이버에서 오류 데이터 수신
				br = new BufferedReader(new InputStreamReader(con.getErrorStream(), "UTF-8"));
			}
			String inputLine;
			// readLine() : 한줄 입력
			// xml : 네이버에서 전송 된 데이터 전체
			while ((inputLine = br.readLine()) != null) {
				json.append(inputLine); // 네이버에서 전송한 데이터를 저장
			}
			br.close();

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (MalformedURLException e1) {

		} catch (IOException e1) {
			e1.printStackTrace();
		}
		//json : 동적 문자열 객체. 네이버에서 전송한 json 형태의 문자열 데이터
		JSONParser parser = new JSONParser();
		JSONObject jsonObj = null;
		try {
			//json.toString() : String 객체. 문자열 객체
			//jsonDate : json 객체
			jsonObj = (JSONObject) parser.parse(json.toString());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		System.out.println(json);

		return jsonObj;

	}
}
