package dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;



import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import logic.Comment;
import dao.mapper.CommMapper;



@Repository
public class CommDao {
	@Autowired
	private SqlSessionTemplate template;
	private Map<String,Object> param = new HashMap<>(); //파라미터 전송할 때 쓸거임
	private Class<CommMapper> cls = CommMapper.class;
	
	public void insert(Comment comm) {
	    template.getMapper(cls).insert(comm);

	}

	public int maxseq(int num) {
		 return template.getMapper(cls).maxseq(num);
	}
	
	
	public void delete(int num, int seq) {
		  template.getMapper(cls).delete(num,seq);

		
	}
	public List<Comment> list(Integer num) {
		
		return template.getMapper(cls).list(num);
	}

	public Comment selectOne(int num, int seq) {
		return template.getMapper(cls).selectOne(num,seq);
	}


	}


	

