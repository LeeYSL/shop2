package dao.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import logic.Sale;

public interface SaleMapper {

	@Select("select ifnull(max(saleid),0) from sale")
	int getMaxSaleId();
   
	@Insert("insert into sale (saleid, userid, saledate) "
			+ "values (#{saleid},#{userid},now())")
	void insert(Sale sale);

    @Select("select * from sale order by saleid desc" )
	List<Sale> select(Map<String, Object> param);
}
