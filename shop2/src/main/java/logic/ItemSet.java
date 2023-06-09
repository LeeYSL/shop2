package logic;

import java.util.Date;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@AllArgsConstructor //멤버 변수값으로 생성자 만들어줌
public class ItemSet {
	private Item item;
    private Integer quantity;
	
}