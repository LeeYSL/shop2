package controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import exception.BoardException;
import exception.LoginException;
import logic.Board;
import logic.Comment;
import logic.ShopService;

@Controller
@RequestMapping("board")
public class BoardController {
	@Autowired
	private ShopService service;

	@GetMapping("*")
	public ModelAndView write() {
		ModelAndView mav = new ModelAndView();
		mav.addObject(new Board());
		return mav;
	}

	/*
	 * 1. 유효성 검증 2. db의 board 테이블에 내용 저장, 파일 업로드 3. 등록 성공 시 list 재요청 등록 실패 시 write
	 * 재요청
	 */
	@PostMapping("write")
	public ModelAndView writePost(@Valid Board board, BindingResult bresult, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		mav.addObject(new Board());
		if (bresult.hasErrors()) {
			mav.getModel().putAll(bresult.getModel());
			// bresult.reject("error.input.board");
		}
		String boardid = (String) request.getSession().getAttribute("boardid");
		if (boardid == null)
			boardid = "1";
		request.getSession().setAttribute(boardid, boardid);
		board.setBoardid(boardid);
		service.boardWrite(board, request);
		mav.setViewName("redirect:list?boardid=" + boardid);
		return mav;

	}

	@RequestMapping("list")
	/*
	 * @RequestParam : 파라미터 값을 저장하는 Map 객체 파라미터 값과 객체(현재 내 변수명을) 맵핑해주는 기능을 가진 어노테이션
	 * 1.파라미터 이름과 매개변수 이름이 같은 경우 2.Bean 클래스 객체의 프로퍼티 이름과 파라미터 이름이 같은 경우 3. Map 객체를
	 * 이용하는 경우
	 * 
	 * 검색 기능 추가
	 * 
	 */
	public ModelAndView list(@RequestParam Map<String, String> param, HttpSession session) { // 들어오는 모든 객체를 map객체로 전달하라는
																								// 뜻
		ModelAndView mav = new ModelAndView();
		System.out.println(param);
		Integer pageNum = null;
		if (param.get("pageNum") != null)
			pageNum = Integer.parseInt(param.get("pageNum"));
		String boardid = param.get("boardid");
		String searchtype = param.get("searchtype");
		String searchcontent = param.get("searchcontent");
		if (pageNum == null || pageNum.toString().equals("")) {
			pageNum = 1;
		}
		if (boardid == null || boardid.equals("")) {
			boardid = "1";
		}
		session.setAttribute("boardid", boardid);
		if (searchtype == null || searchcontent == null || searchtype.trim().equals("")
				|| searchcontent.trim().equals("")) { // 하나라도 공백이거나 null 이면
			searchtype = null; // 둘 다 null로 하겠다.
			searchcontent = null; // 둘 다 null로 하겠다.

		}
		String boardName = null;
		switch (boardid) {
		case "1":
			boardName = "공지사항";
			break;
		case "2":
			boardName = "자유게시판";
			break;
		case "3":
			boardName = "Q&A";
			break;

		}
		int limit = 10; // 한 페이지에 보여줄 게시물 건 수
		int listcount = service.boardcount(boardid, searchtype, searchcontent); // 등록 된 게시물 건 수
		// boardlist : 현재 페이지에 보여줄 게시물 목록
		List<Board> boardlist = service.boardlist(pageNum, limit, boardid, searchtype, searchcontent); // boardlist : 현재
																										// 조회 조건에 맞는 게시물
																										// 건 수
		// 페이징 처리를 위한 값
		int maxpage = (int) ((double) listcount / limit + 0.95);// 등록 건수에 따른 최대 페이지
		int startpage = (int) ((pageNum / 10.0 + 0.9) - 1) * 10 + 1;// 페이지의 시작 번호
		int endpage = startpage + 9; // 화면에 보여줄 페이지 끝 번호
		if (endpage > maxpage)
			endpage = maxpage; // 페이지의 끝 번호는 최대 페이지 클 수 없다.
		int boardno = listcount - (pageNum - 1) * limit; // boardno : 화면 출력 시 보여지는 게시물 번호
		String today = new SimpleDateFormat("yyyyMMdd").format(new Date()); // 오늘 날짜를 문자열로 저장
		// 20230530이런 식으로 나온다. list에서 오늘 날짜를 - 없이 yyyyMMdd로 표시 했기 때문에 여기서도 똑같이 맞춰 줘야 된다.
		mav.addObject("boardid", boardid);
		mav.addObject("boardName", boardName);
		mav.addObject("pageNum", pageNum);
		mav.addObject("maxpage", maxpage);
		mav.addObject("startpage", startpage);
		mav.addObject("endpage", endpage);
		mav.addObject("listcount", listcount);
		mav.addObject("boardlist", boardlist);
		mav.addObject("boardno", boardno);
		mav.addObject("today", today);
		return mav;

	}

	@RequestMapping("detail") // get으로 해도 됨
	public ModelAndView detail(Integer num) {
		ModelAndView mav = new ModelAndView();
		Board board = service.getBoard(num); // num 게시판 내용 조회
		service.addReadcnt(num);
		mav.addObject("board", board);
		if (board.getBoardid() == null || board.getBoardid().equals("1"))
			mav.addObject("boardName", "공지사항");
		else if (board.getBoardid().equals("2"))
			mav.addObject("boardName", "자유게시판");
		else if (board.getBoardid().equals("3"))
			mav.addObject("boardName", "QNA");
		//댓글 목록 화면에 전달
		List<Comment> commlist = service.commentlist(num);
		mav.addObject("commlist" , commlist);
		//유효성 검증에 필요한 Comment 객체
		Comment comm = new Comment();
		comm.setNum(num);
		mav.addObject("comment",comm);
		return mav;
	}

	@GetMapping({ "reply", "update", "delete" })
	public ModelAndView reply(Integer num, HttpSession session) {
		ModelAndView mav = new ModelAndView();
		String boardid = (String) session.getAttribute("boardid");
		Board board = service.getBoard(num);
		mav.addObject("board", board);

		if (boardid == null || boardid.equals("1"))
			mav.addObject("boardName", "공지사항");
		else if (board.getBoardid().equals("2"))
			mav.addObject("boardName", "자유게시판");
		else if (board.getBoardid().equals("3"))
			mav.addObject("boardName", "QNA");
		return mav;

	}

	/*
	 * 1.유효성 검사하기-파라미터 값 저장 - 원글 정보 : num,grp,grplevel,grpstep,boardid - 답글 정보 :
	 * writer,pass,subject,content 2.db에 insert => service.boardReply() -원글의 grpstep
	 * 보다 큰 이미 등록 된 답글의 grpstep 값을 + 1 => boardDao.grpStepAdd() -num :maxNum() + 1
	 * -db에 insert => boardDao.insert() grp : 원글과 동일 grplevel : 원글의 grplevel + 1
	 * grpstep : 원글의 grpstep + 1
	 * 
	 * 3. 등록 성공 : list 페이지 이동 등록 실패 : "답변 등록 시 오류 발생" reply 페이지 이동
	 */

	@PostMapping("reply")
	public ModelAndView reply(@Valid Board board, BindingResult bresult) {
		ModelAndView mav = new ModelAndView();
		// 유효성 검사
		if (bresult.hasErrors()) {
			Board dbboard = service.getBoard(board.getNum()); // 원글 정보를 db에서 읽기
			Map<String, Object> map = bresult.getModel();
			Board b = (Board) map.get("board"); // 화면에서 입력 받은 값을 저장한 board 객체
			b.setTitle(dbboard.getTitle());// 원글의 제목으로 변경
			mav.getModel().putAll(bresult.getModel());
			return mav;
		}
		try {
			service.boardReply(board);
			mav.setViewName("redirect:list?boardid=" + board.getBoardid());
		} catch (Exception e) {
			e.printStackTrace();
			throw new LoginException("답변 등록시 오류 발생", "reply?num=" + board.getNum());
		}

		return mav;

	}
	/*
	 * 1.유효성 검증 2.비밀번호 검증 - 비밀번호 불일치 : 비밀번호가 틀립니다. 메세지 출력 후 update로 페이지 이동 3.비밀번호 일치
	 * : db에 내용 수정. 업로드 되는 파일이 있는 경우 파일 업로드 db 내용 수정 4.수정 완료 : detail 페이지 이동 - 수정 실패
	 * : 수정 실패 메세지 출력 update 페이지 이동
	 */

//	@PostMapping("update")
//	public ModelAndView update(@Valid Board board, BindingResult bresult, HttpServletRequest request,
//			HttpSession session, String pass) { // HttpServletRequest request: 파일 업로드 때문에 리퀘스트 객체 받아서 요청되는 위차 만들어 처리함
//		ModelAndView mav = new ModelAndView();
//		if (bresult.hasErrors()) {
//			mav.getModel().putAll(bresult.getModel());
//			bresult.reject("error.update.board");
//			return mav;
//		}
//		try {
//			Board dbboard = service.getBoard(board.getNum());
////			User User = service.selectPassOne(user.getUserid(), user.getPassword());
////			User loginUser = (User) session.getAttribute("loginUser"); 
//			if(password.equals(dbboard.getPass())){
//				service.boarddate(board);
//				mav.addObject("board", board);	
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new LoginException("비밀번호를 확인하세요.", "redirect:update");
//		}
//		mav.setViewName("redirect:detail");
//		return mav;
//	}
//}
	@PostMapping("update")
	public ModelAndView update(@Valid Board board, BindingResult bresult, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		if (bresult.hasErrors()) {
			mav.getModel().putAll(bresult.getModel());
			return mav;
		}
		Board dbBoard = service.getBoard(board.getNum()); // db에 있는 비밀번호
		if (!board.getPass().equals(dbBoard.getPass())) {
			throw new BoardException("비밀번호를 확인하세요.", "update?num=" + board.getNum());
		}
		// 입력 갑 정상, 비밀번호 일치
		try {
			service.boardUpdate(board, request); // 파일 업로드, db게시물 수정
			mav.setViewName("redirect:detail?num=" + board.getNum());
		} catch (Exception e) {
			e.printStackTrace();
			throw new BoardException("게시물 수정 실패.", "update?num=" + board.getNum());
		}
		return mav;
	}
	/*
	 * 1.num,pass 파라미터 저장 -> 매개변수 처리 2.비밀번호 검증 : db에서 num에 해당하는 게시글을 조회 db에 등록 된
	 * 비밀번호와 입력 된 비밀번호 비교 비밀번호 오류 : error.board.password 코드 값 설정 => delete.jsp로 전달
	 * 3.비밀번호가 일치 :db에서 num에 해당하는 게시글을 삭제 삭제 성공 : list 페이지 이동 삭제 실패 : delete 페이지 이동
	 * 
	 */

	@PostMapping("delete")
	public ModelAndView delete(Board board, BindingResult bresult) {
		ModelAndView mav = new ModelAndView();
		if (board.getPass() == null || board.getPass().trim().equals("")) {
			bresult.reject("error.required.pass");
			return mav;
		}
		Board dbboard = service.getBoard(board.getNum());
		if (!board.getPass().equals(dbboard.getPass())) {
			bresult.reject("error.board.password");
			return mav;

		}
		try {
			service.boardDelete(board.getNum());
			mav.setViewName("redirect:list?boardid=" + dbboard.getBoardid());
		} catch (Exception e) {
			e.printStackTrace();
			bresult.reject("error.board.fail");
		}

		return mav;
	}

	@RequestMapping("imgupload")
	public String imgupload(MultipartFile upload, String CKEditorFuncNum, HttpServletRequest request, Model model) { // 매개변수로
																														// 넣어주면
																														// 뷰단까지
																														// 전달해줌?

		/*
		 * upload : CKEditor 모듈에서 업로드 되는 이미지의 이름. 업로드 되는 이미지 파일의 내용. 이미지 값
		 * CKEditorFuncNum : CKEditor 모듈에서 파라미터로 전달되는 값 리턴헤야 되는 값. 리턴해야 되는 값 model :
		 * ModelAndView 중 Model에 해당하는 객체 뷰에 전달할 데이터 정보 저장할 객체 return 타입이 String : view
		 * 이름 /WEB-INF/view/ckedit.jsp
		 * 
		 */
		// 업로드 되는 위치 폴더 값
		// request.getServletContext().getRealPath("/") : 웹 어플리케이션의 절대 경로 값.
		String path = request.getServletContext().getRealPath("/") + "board/imgfile/";
		service.uploadFileCreate(upload, path); // upload (파일의 내용),path(업로드 되는 폴더)
		// request.getContextPath() : 프로젝트명(웹어플리케이션 서버 이름). shop1/
		// http://localhost:8080/shop1/board/imagfile/사진.jpg
		String fileName = request.getContextPath() // 웹 어플리케이션 경로 웹 url 정보
				+ "/board/imgfile/" + upload.getOriginalFilename();
		model.addAttribute("fileName", fileName);
		return "ckedit";// view 이름. /WEB-INF/view/ckedit.jsp

	}

	@RequestMapping("comment") //댓글 등록
	public ModelAndView comment(@Valid Comment comm, BindingResult bresult) {
		ModelAndView mav = new ModelAndView();
		if(bresult.hasErrors()) {
			mav.getModel().putAll(bresult.getModel());
			return mav;
		}
 		
		int seq = service.commmaxseq(comm.getNum());
		comm.setSeq(++seq);
		service.comminsert(comm);
		mav.setViewName("redirect:detail?num="+comm.getNum()+"#comment");
	//	return "redirect:detail?num="+comm.getNum()+"#comment";
		return mav;
       

	}
	@RequestMapping("commdel")
	public String commdel(Comment comm) {
		//현재 댓글을 아무나 삭제 가능 => 수정 필요.
		//업무요건1 : 로그인 한 회원만 댓글 가능 => 내 글만 삭제 가능
		//업무요건2 : 로그아웃 상태에서도 댓글 가능 => 비밀번호 추가. 비밀번호 검증 필요 
	    Comment dbcomm = service.commSelectOne(comm.getNum(),comm.getSeq());
		if(dbcomm.getPass() == comm.getPass()) {
			service.commdel(comm.getNum(),comm.getSeq());
	    
		}else  // 비밀번호 틀린경우
			throw new BoardException("댓글삭제 실패","detail?num=" +comm.getNum()+"#comment");
			return "redirect:detail?num="+comm.getNum()+"#comment";
				
	}
}
