package logic;



import java.util.Date;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Setter
@Getter
@ToString
public class Comment {
	private int num;
	private int seq;
	@NotEmpty(message="작성자를 입력하세요.")
	private String writer;
	@NotNull(message="비밀번호를 입력하세요.")
	private int pass;
	@NotEmpty(message="내용을 입력하세요.")
	private String content;
	private Date regdate;

}
