package dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import dao.mapper.ItemMapper;
import logic.Item;
 
@Repository //@Compoent + dao 기능(데이터베이스 연결)
public class ItemDao {
	 @Autowired
      private SqlSessionTemplate template; //org.mybatis.spring.SqlSessionTemplate 객체 주입
      private Map<String,Object> param = new HashMap<>();
      private final Class<ItemMapper> cls = ItemMapper.class;
      
      public List<Item> list() {
    	  param.clear();
    	  return template.getMapper(cls).select(param); //item 테이블의 전체 내용을 Item 객체의 목록 리턴
      }
	public Item getItem(Integer id) {
		param.clear();
		param.put("id", id);
		//item 테이블의 id 값에 해당하는 전체 내용울 Item 객체의 목록으로 리턴(리턴 타입이 list여서) 한 건만 필요.
//		return template.getMapper(cls).select(param).get(0); 
		return template.selectOne("dao.mapper.ItemMapper.select",param); //template 으로 부터 레코드 한건만 가져와라(selectOne)
		//dao.mapper.ItemMapper 네임스페이스에 있는 select에 있는걸 가져와라?
	}
	public int maxId() {
		return template.getMapper(cls).maxId();
	}
	public void insert(Item item) {
		template.getMapper(cls).insert(item);		
	}
	public void update(Item item) {		
		template.getMapper(cls).update(item);
	}
	public void delete(Integer id) {
	  param.clear();
	  param.put("id", id);
	  template.getMapper(cls).delete(param);	
	}
	public void cartdelete(Integer index) {
		  param.clear();
		  param.put("index", index);
		  template.update("delete from item where index=:index", param);	
		}
public void Cartdelete(Integer index) {
	 param.clear();
	 param.put("index", index);
	 template.update("delete from item where index=:index", param);	
	}
}
