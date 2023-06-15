package dao;



import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import dao.mapper.ExMapper;

import logic.Exchange;

@Repository
public class ExDao {
   @Autowired
	private SqlSessionTemplate template;
	private Class<ExMapper> cls = ExMapper.class;
    public void insert(Exchange ex) {
    	template.getMapper(cls).insert(ex);
    }
}
