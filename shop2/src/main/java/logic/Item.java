package logic;

import java.util.Date;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class Item {
	private int id;
	@NotEmpty(message="상품명을 입력하세요") //@NotEmpty : 값이 없거나 빈 문자열인 경우(name이란 파라미터 값이 없거나 빈 문자열로 들어 온 경우) 
	private String name;
	@Min(value=10,message="10원 이상 가능합니다.")
	@Max(value=100000,message="10만원 이하만 가능합니다.")
	private int price;
	@NotEmpty(message="상품설명을 입력하세요.")
	private String description;
	private String pictureUrl;
	private MultipartFile picture; //picture file에서 업로드된 파일의 내용
}