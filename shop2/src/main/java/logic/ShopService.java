package logic;

import java.io.File;
import java.net.http.HttpRequest;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import dao.BoardDao;
import dao.ItemDao;
import dao.UserDao;
import dao.SaleDao;
import dao.SaleItemDao;

@Service // @Compoent + Service(controller 기능과 dao 기능의 중간 역할 기능(중간다리))
public class ShopService {
	@Autowired // itemDao 객체 주입
	private ItemDao itemDao;

	@Autowired
	private UserDao userDao;
	@Autowired
	private SaleDao saleDao;
	@Autowired
	private SaleItemDao saleItemDao;
	@Autowired
	private BoardDao boardDao;

	public List<Item> itemList() {
		return itemDao.list();
	}

	public Item getItem(Integer id) {
		return itemDao.getItem(id);
	}

	public void itemCreate(Item item, HttpServletRequest request) {
		if (item.getPicture() != null && !item.getPicture().isEmpty()) {
			// 업로드해야 되는 파일의 내용이 있는 경우
			String path = request.getServletContext().getRealPath("/") + "img/";
			uploadFileCreate(item.getPicture(), path);
			// 업로드 된 파일 이름을 setPictureUrl에다 저장
			item.setPictureUrl(item.getPicture().getOriginalFilename());
		}
		// db에 내용 저장
		int maxid = itemDao.maxId(); // item 테이블에 저장된 최대 id 값
		item.setId(maxid + 1);
		itemDao.insert(item);// db에 추가

	}

	public void uploadFileCreate(MultipartFile file, String path) {
		// file : 파일의 내용 path : 업로드할 파일
		// path : 업로드할 폴더
		String orgFile = file.getOriginalFilename(); // 파일 이름
		File f = new File(path);
		if (!f.exists())
			f.mkdirs(); // 폴더 만들어줘
		try {
			// file에 저장 된 내용을 path+orgFile(이미지 밑에 원래 파일 이름으로 바꿔서) 저장해라
			file.transferTo(new File(path + orgFile));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void itemUpdate(Item item, HttpServletRequest request) {
		if (item.getPicture() != null && !item.getPicture().isEmpty()) {
			// 업로드해야 되는 파일의 내용이 있는 경우
			String path = request.getServletContext().getRealPath("/") + "img/";
			uploadFileCreate(item.getPicture(), path);
			// 업로드 된 파일 이름을 setPictureUrl에다 저장
			item.setPictureUrl(item.getPicture().getOriginalFilename());
		}
		// db에 내용 저장
//		int maxid = itemDao.maxId(); //item 테이블에 저장된 최대 id 값
//		item.setId(maxid+1);
		itemDao.update(item);// db에 추가

	}

	public void itemDelete(Integer id) {
		itemDao.delete(id);

	}

	public void userInsert(User user) {
		userDao.insert(user);

	}

	public User selectUserOne(String userid) {
		return userDao.selectOne(userid);
	}

	/*
	 * 1.로그인 정보, 장바구니 정보를 이용해서 sale,saleitem 테이블에 데이터 저징 2.결과는 sale 객체에 저장 -sale 테이블
	 * 저장 : saleid값 구하기. 최대값+1 ,saleitem 테이블 저장 : Cart 데이터를 이용하여 저장
	 * 
	 */
	public Sale checkend(User loginUser, Cart cart) {
		int maxsaleid = saleDao.getMaxSaleId(); // saleid 최대 값 조회
		Sale sale = new Sale();
		sale.setSaleid(maxsaleid + 1);
		sale.setUser(loginUser);
		sale.setUserid(loginUser.getUserid());
		saleDao.insert(sale); // sale 테이블에 데이터 추가
		int seq = 0;
		for (ItemSet is : cart.getItemSetList()) {
			SaleItem saleItem = new SaleItem(sale.getSaleid(), ++seq, is);
			sale.getItemlist().add(saleItem);
			saleItemDao.insert(saleItem); // saleitem 테이블에 데이터 추가
		}
		return sale; // 주문정보, 주문상품정보,상품정보,사용자 정보
	}

	public User selectPassOne(String userid, String password) {
		return userDao.selectPassOne(userid, password);
	}

	public List<Sale> salelist(String userid) {
		List<Sale> list = saleDao.list(userid);// id 사용자가 주문 정복목록
		for (Sale sa : list) {
			// saleitemlist : 한개의 주문에 해당하는 주문상품 목록
			List<SaleItem> saleitemlist = saleItemDao.list(sa.getSaleid());
			for (SaleItem si : saleitemlist) {
				Item item = itemDao.getItem(si.getItemid()); // 상품정보
				si.setItem(item);
			}
			sa.setItemlist(saleitemlist);
		}
		return list;
	}

	public void userDelete(String userid) {
		userDao.delete(userid);

	}

	public void userUpdate(@Valid User user) {
		userDao.update(user);

	}

	public void passupdate(String userid, String chgpass) {
		userDao.update(userid,chgpass);
		
	}

	public List<User> list() {
		return userDao.list(); //회원 목록 조회
	}

	public List<User> getUserList(String[] idchks) {
		return userDao.list(idchks);
	}

	public String getSearch(User user) {
		return userDao.search(user);
}

	public void boardWrite(Board board, HttpServletRequest request) {
		int maxnum = boardDao.maxNum(); // 등록 된 게시물의 최대 num 값 리턴
		board.setNum(++maxnum);
		board.setGrp(maxnum);
		if(board.getFile1() != null && !board.getFile1().isEmpty()) {
			String path = request.getServletContext().getRealPath("/") + "board/file/";
			this.uploadFileCreate(board.getFile1(), path);
			board.setFileurl(board.getFile1().getOriginalFilename());
		}
         	boardDao.insert(board);
		
	}

	public int boardcount(String boardid,String searchtype, String searchcontent) {
		return  boardDao.count(boardid,searchtype,searchcontent);
	}

	public List<Board> boardlist(Integer pageNum, int limit, String boardid,String searchtype, String searchcontent) {
		return boardDao.list(pageNum,limit,boardid,searchtype,searchcontent);
	}

	public Board getBoard(Integer num) {
	    return boardDao.selectOne(num); //board 레코드 조회
		
	}
	public void addReadcnt(Integer num) {
	    boardDao.addReadcnt(num); //조회수 증가
		
	}
     @Transactional // 보통 서비스 부분에 넣음. 트랜젝션(거래를 원자화한다. all or nothing) 처리한다는 뜻
     //오류가 나면 rollback 처리 함
	public void boardReply(Board board) {
		boardDao.updateGrpStep(board);// 이미 등록된 grpstep 값 1 씩 증가
		int max = boardDao.maxNum();
		//원글의 부분을 답변글로 바꾸는 부분?
 		board.setNum(++max); // 원글의 num 값을 답변글의 num 값으로 변경
 		                     // 원글의 grp 값 => 답변글의 grp 값을 동일 설정 필요 없음
 		                     // 원글의 boardid => 답변글의 boardid 값을 동일. 설정 필요 없음
 		board.setGrplevel(board.getGrplevel() +1); // 원글의 grplevel => +1을 해서 답변글의 grplevel 값으로 설정 
 		board.setGrpstep(board.getGrpstep() +1 ); // 원글의 grpstep => +1을 해서 답변글의 grpstep 값으로 설정 	
 		boardDao.insert(board);	
		
	}

	public void boardUpdate(Board board, HttpServletRequest request) {
		if(board.getFile1() != null && !board.getFile1().isEmpty()) { //업르드 된 파일이 있는 상태
			String path = request.getServletContext().getRealPath("/") + "board/file/";
			//파일 업로드 : board.getFile()의 내용을 파일로 생성
			this.uploadFileCreate(board.getFile1(), path);
			board.setFileurl(board.getFile1().getOriginalFilename());
		}
		boardDao.update(board);

	}

	public void boardDelete(Integer num) {
		boardDao.delete(num);
	}		
	
}