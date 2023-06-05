package dao.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;


import logic.User;

public interface UserMapper {
	@Insert("insert into useraccount (userid,username,password,"
			+ " birthday, phoneno,postcode,address,email)  values"
			+ " (#{userid},#{username},#{password}"
			+ " #{birthday},#{phoneno},#{postcode},#{address},#{email})")
	void insert(User user);

	@Select({"<script>",
			"select * from useraccount ",
			"<if test='userid != null'> where userid=#{userid}</if>",
			"<if test='userids != null'> where userid in "
			+ "<foreach collection='userids' irem='id' separator=',' open='(' close=')'>#{id}"
		    + "</foreach></if>",
			"</script>"}) //동적쿼리
	List<User> select(Map<String,Object> param);

	
	@Update(" update useraccount set username=#{username},"
		    + " birthday=#{birthday}, phoneno=#{phoneno},"
			+ " postcode=#{postcode}, address=#{address},email=#{email} where userid=#{userid}")
	void update(User user);

	@Delete("delete from useraccount where userid=#{userid}")
	void delete(Map<String,Object> param);
	
	@Update("update useraccount set password=#{password} where userid=#{userid")
	void chgpass(Map<String,Object> param);
    
	@Select({"<script>",
			"select ${col} from useraccount where email=#{email} and phoneno=#{phoneno} "
			+ "<if test='userid != null'> and userid=#{userid} </if> ", 
			"</script>"})
	String search(Map<String, Object> param);

	
}
