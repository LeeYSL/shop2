package logic;

import java.util.Date;

import javax.validation.constraints.NotEmpty;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
/*
 * lombok : setter,getter,toString, 생성자들을 자동 생성해주는 유틸리티
 * lombok 사용
 *    - lombok 설치
 *    - lombok 관련 jar
 *    
 *    lombok에서 사용하는 어노테이션 설명
 *    @Setter : 자동으로 setter(setXXX()) 소스 생성
 *    @Getter : 자동으로 getter(setXXX()) 소스 생성
 *    @Tostring : 자동으로 모든 멤버를 출력하도록 toString 메서드 소스 생성
 *    @EqualsAndHashCode : equls 함수와 hashcode 함수를 자동 오버라이딩
 *    @AllArgsConstructor : 모든 멤버를 매개변수로 가진 생성자 구현
 *    @NotArgsConstructor : 매개변수 없는 생성자 구현
 *    @RequiredArgConstructor : final, @NotNull 인 멤버 변수만 매개변수로 갖는 생성자 구현
 *    
 *    @Data : Getter,Setter,Tostring,EqualsAndHashCode,RequiredArgConstructor 를 자동으로 구현
 */
@Setter
@Getter
@ToString
public class Board {
	private int num;
	private String boardid;
	@NotEmpty(message="글쓴이를 입력하세요.")
	private String writer;
	@NotEmpty(message="비밀번호를 입력하세요.")
	private String pass;
	@NotEmpty(message="제목을 입력하세요.")
	private String title;
	@NotEmpty(message="내용을 입력하세요.")
	private String content;
	private MultipartFile file1;
	private String fileurl;
	private Date regdate;
	private int readcnt;
	private int grp;
	private int grplevel;
	private int grpstep;
	public int commcnt;
}