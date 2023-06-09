package logic;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
 
@Setter
@Getter
@ToString
public class Mail {
  private String naverid;
  private String naverpw;
  private String recipient;
  private String title;
  private String mtype;
  private List<MultipartFile> file1; //메일 전송 시 첨부파일 2개 가능하도록
  private String contents;
  
}
