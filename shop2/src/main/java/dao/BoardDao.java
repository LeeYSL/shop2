package dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Repository;

import dao.mapper.BoardMapper;
import logic.Board;

@Repository
public class BoardDao {
	@Autowired
	private SqlSessionTemplate template;
	private Map<String,Object> param = new HashMap<>(); //파라미터 전송할 때 쓸거임
	private Class<BoardMapper> cls = BoardMapper.class;


	

	public int maxNum() {
		return template.getMapper(cls).maxNum();
	}

	public void insert(Board board) {
		template.getMapper(cls).insert(board);
	}

	public int count(String boardid, String searchtype, String searchcontent) {
		param.clear();
		param.put("boardid",boardid);
		param.put("searchtype",searchtype);
		param.put("searchcontent",searchcontent);
		return template.getMapper(cls).count(param); 
	}

	public List<Board> list(Integer pageNum, int limit, String boardid, String searchtype, String searchcontent) {
		param.clear();
		param.put("startrow", (pageNum-1) * limit); 
		param.put("limit", limit);
		param.put("boardid", boardid);
		param.put("searchtype",searchtype);
		param.put("searchcontent",searchcontent);
		return template.getMapper(cls).select(param); 
	}

	public Board selectOne(Integer num) {
		param.clear();
		param.put("num", num);
		return template.selectOne("dao.mapper.BoardMapper.select",param);
		// getMapper(cls).select(param); : list에서 쓴걸 똑같이 쓰려는데 return 값이 달라서 이거 못씀.
	}

	public void addReadcnt(Integer num) {
		param.clear();
		param.put("num", num);
		template.getMapper(cls).addReadcnt(param);
		
	}

//	public int grpStepAdd() {
//		return template.queryForObject("select ifnull(max(grpstep),0) from board where num=:num",param, Integer.class);
//	}

//	public void rinsert(Board board) {
//		SqlParameterSource param = new BeanPropertySqlParameterSource(board);
//		//board객체에 있는 파라미터를 내가 프로퍼티로 쓸거임
//		String sql = "insert into board (num, boardid, writer, pass, title, content, file1, regdate, readcnt, grp, grplevel, grpstep) "
//				+ " values (:num, :boardid, :writer, :pass, :title, :content, :fileurl, now(), 0 ,:grp, :grplevel+1, :grpstep)";
//		//file1에는 fil1url 넣어라
//		//조회수는 0 
//		template.update(sql, param);
//	}

	public void updateGrpStep(Board board) { //답변글 등록시 기존 게시물의 grpstep 값을 +1 해서 변경
		param.clear();
		param.put("grp", board.getGrp()); //원글의 grp
		param.put("grpstep", board.getGrpstep()); //원글의 grpstep
		template.getMapper(cls).updateGrpStep(param);
	}


	public void update(Board board) {
		template.getMapper(cls).update(board);
	}

	public void delete(Integer num) {
		template.getMapper(cls).delete(num);
	}

	public List<Map<String, Object>> graph1(String id) {
		return template.getMapper(cls).graph1(id);
	}
}