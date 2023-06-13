package dao.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import logic.Board;

public interface BoardMapper {
	//인터페이스는 접근제한자 쓸 수 없다.
	String select = "select num, writer, pass, title, content, file1 fileurl, "
			+ " regdate, readcnt, grp, grplevel, grpstep, boardid from board";

	@Select("select ifnull(max(num),0) from board")
	int maxNum();

	@Insert("insert into board (num, boardid, writer, pass, title, content, file1, regdate, readcnt, grp, grplevel, grpstep) "
			+ " values (#{num}, #{boardid}, #{writer}, #{pass}, #{title}, #{content}, #{fileurl}, now(), 0 ,#{grp}, #{grplevel}, #{grpstep})")
	void insert(Board board);

	@Select({"<script>",
			 "select count(*) from board where boardid=#{boardid}",
			 "<if test='searchtype != null'> and ${searchtype} like '%${searchcontent}%'</if>",
			 "</script>"})
	int count(Map<String, Object> param);

	@Select({"<script>",
			select,
			"<if test='num != null'> where num = #{num} </if>",
			"<if test='boardid != null'> where boardid = #{boardid} </if>",
			" <if test='searchcontent != null'> and ${searchtype} like '%${searchcontent}%'</if>",
			"<if test='limit != null'> order by grp desc, grpstep asc limit #{startrow}, #{limit} </if>",
			"</script>"})
	List<Board> select(Map<String, Object> param);
	
	@Update("update board set readcnt = readcnt + 1 where num=#{num}")
	void addReadcnt(Map<String, Object> param);

	@Update("update board set grpstep=grpstep + 1"
			+ " where grp = #{grp} and grpstep > #{grpstep}")
	void updateGrpStep(Map<String, Object> param);

	@Update("update board set writer=#{writer}, title=#{title}, content=#{content}, file1=#{fileurl} where num=#{num}")
	void update(Board board);

	@Delete("delete from board where num=#{num}")
	void delete(Integer num);
    /*
     * [{writer = 홍길동, cnt= 10},{writer = 김삿갓, cnt= 7}...] 
     */
	
   @Select("select writer,count(*) cnt from board where boardid=#{value} "
		    + " group by writer order by 2 desc limit 0,7")
	List<Map<String, Object>> graph1(String id);
   
    /*
	 * date_format(날짜,패턴) : 날짜 형식의 데이터를 패턴에 맞는 문자열로 리턴 sql 함수
	 *     day 컬럼 : 2023-06-07 형식의 문자열 컬럼
	 *     
	 *    [  컬럼명    컬럼값     컬럼명   컬럼값
  	 *       {day :2023-06-07, cnt : 10},
	 *       {day :2023-06-06, cnt : 3},
	 *       {day :2023-06-05, cnt : 10},
	 *       ....
	 *     ]
	 *     
	 *     select 컬럼 -5
	 *     from  테이블 -1
	 *     where 조건   -2
	 *     group by 컬럼  -3 (별명 사용할 수 없음.mariadb는 가능)
	 *     having 조건  -4
	 *     order by 컬럼 -6 (컬럼,순서,별명)
	 */
   @Select("select date_format(regdate,'%Y-%m-%d') day, count(*) cnt from board "
		    + " where boardid=${value} group by date_format(regdate,'%Y-%m-%d') "
		    + " order by day desc limit 0,7")
List<Map<String, Object>> graph2(String id);

@Insert("insert into comment  (num, boardid, writer, pass, title, content, file1, regdate, readcnt, grp, grplevel, grpstep) "
		+  " values (#{num}, #{boardid}, #{writer}, #{pass}, #{title}, #{content}, #{fileurl}, now(), 0 ,#{grp}, #{grplevel}, #{grpstep})")   
   void cinsert(Board board);
    
	
	
	
}
