package dao.mapper;

import org.apache.ibatis.annotations.Insert;

import logic.Exchange;

public interface ExMapper {
	
	@Insert("insert into exchange (code,name,primeamt,sellamt,buyamt,edate) "
			  + " values (#{code}, #{name}, #{primeamt}, #{sellamt}, #{buyamt}, #{edate})")
	void insert(Exchange ex);

}
