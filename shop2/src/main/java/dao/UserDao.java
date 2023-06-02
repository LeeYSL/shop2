package dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import logic.User;

@Repository
public class UserDao {
	private NamedParameterJdbcTemplate template;
	private RowMapper<User> mapper = new BeanPropertyRowMapper<User>(User.class);
	private Map<String, Object> param = new HashMap<>();

	@Autowired
	public void setDataSource(DataSource dataSource) {
		template = new NamedParameterJdbcTemplate(dataSource);
	}

	public void insert(User user) { // user : 화면에서 입력받은 값을 저장하고 있는 객체
		// param : user 객체의 프로퍼티를 이용하여 db에 값을 등록
		SqlParameterSource param = new BeanPropertySqlParameterSource(user);
		String sql = "insert into useraccount (userid,username,password, "
				+ " birthday, phoneno,postcode,address,email) values " + " (:userid,:username,:password,"
				+ " :birthday,:phoneno,:postcode,:address,:email)";
		template.update(sql, param);
	}

	public User selectOne(String userid) {
		param.clear();
		param.put("userid", userid);
		return template.queryForObject("select * from useraccount where userid=:userid", param, mapper);
	}

	public User selectPassOne(String userid, String password) {
		param.clear();
		param.put("userid", userid);
		param.put("password", password);
		return template.queryForObject("select * from useraccount where userid=:userid and password=:password", param,
				mapper);

	}

	public void update(@Valid User user) {
		SqlParameterSource param = new BeanPropertySqlParameterSource(user);
		String sql = "update useraccount set username=:username, " + " birthday=:birthday, phoneno=:phoneno,"
				+ " postcode=:postcode, address=:address,email=:email" + " where userid=:userid";
		template.update(sql, param);

	}
 
	public void delete(String userid) {
		param.clear();
		param.put("userid", userid);
		template.update("delete from useraccount where userid=:userid", param);

	}

	public void update(String userid, String chgpass) {
		param.clear();
		param.put("userid", userid);
		param.put("password", chgpass);
		template.update("update useraccount set password=:password where userid=:userid",param);

 
	}

	public List<User> list() {
		return template.query("select * from useraccount", param,mapper); 
		//mapper : useraccount에 있는 컬럼명과 userclass 있는 이름이 같은걸 가져와..? 무슨뜻..
	}
  
	// selete * from useraccount where userid in ('admin','test1')
	public List<User> list(String[] idchks) { //마이바티스는 같은 이름을 쓸 수 없지만 지금은 오버로딩 되어서 쓸 수 있다.
		StringBuilder ids = new StringBuilder();
		  for(int i=0;i<idchks.length; i++) {
			  ids.append("'").append(idchks[i]).append((i==idchks.length-1)?"'":"',");
	}
		  /*
		   * mapper : select 구문의 실행 결과 값 
		   * mapper = new BeanPropertyRowMapper<User>(User.class);  
		   *   = > mapper는 user 클래스를 가지고 있어서
		   *     1. user 객체 생성
		   *     2. userid 컬럼을 setUserid  로 넣어줌 
		   *     ....
		   */
		  String sql = "select * from useraccount where userid in (" + ids.toString() + ")";	
		return template.query(sql, mapper);
	}


	public String search(User user) {
	   String col ="userid";
	   if(user.getUserid() != null) col = "password";
	   String sql = "select "+ col +" from useraccount "
			   + "where email=:email and phoneno=:phoneno";
	   if(user.getUserid() != null) {
		   sql += " and userid=:userid";
	   }
	   /*
	    * BeanPropertySqlParameterSource(user) : user 객체의 프로퍼티로 파라미터 설정.
	    *              : email : user.getEmail()
	    *              : phoneno : user.getPhone()
	    *              
	    *  String.class : select 구문의 결과의 자료형             
	    */
	   SqlParameterSource param = 
			     new BeanPropertySqlParameterSource(user);
		return template.queryForObject(sql, param,String.class);
	}


}
