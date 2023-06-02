package dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;
import javax.xml.transform.Templates;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import dao.mapper.ItemMapper;
import dao.mapper.SaleMapper;
import logic.Sale;
import logic.SaleItem;
 
@Repository 
public class SaleDao {
	@Autowired
	private SqlSessionTemplate template;
	private Map<String,Object> param = new HashMap<>();
	private final Class<SaleMapper> cls = SaleMapper.class;
	

	public int getMaxSaleId() { //saleid의 최대값 조회
		return template.getMapper(cls).getMaxSaleId();
		//	("select ifnull(max(saleid),0) from sale",param,Integer.class);	
	}
	public void insert(Sale sale) {
//     String sql = "insert into sale (saleid, userid, saledate)"
//    		    + " values (:saleid, :userid, now())"; 
 	template.getMapper(cls).insert(sale);
 	
	}
	public List<Sale> list(String userid) {
//		String sql = "select * from sale where userid=:userid"
//				+ " order by saleid desc"; //최근 주문 순서로 조회
		param.clear();
		param.put("userid", userid);
		return template.getMapper(cls).select(param);
	}
}