package dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Repository;

import dao.mapper.SaleItemMapper;
import logic.SaleItem;
 
@Repository //shop service에 주입
public class SaleItemDao {
	@Autowired
//	private NamedParameterJdbcTemplate template;
	private SqlSessionTemplate template;
	private Map<String, Object> param = new HashMap<>(); 
	private final Class<SaleItemMapper> cls = SaleItemMapper.class;
	public void insert(SaleItem saleItem) {
		   template.getMapper(cls).insert(saleItem);
			
		}
	public List<SaleItem> list(int saleid) {
		param.clear();
		param.put("saleid", saleid);
		return template.getMapper(cls).select(param);
	}
}