package dao.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import logic.SaleItem;

public interface SaleItemMapper {

	@Select("select * from saleitem where saleid = #{saleid}")
	List<SaleItem> select(Map<String, Object> param);
   
	 @Insert("insert into saleitem(saleid, seq,itemid,quantity) "
			 + "value (#{saleid},#{seq},#{itemid},#{quantity})")
	void insert(SaleItem saleItem);
	

}
