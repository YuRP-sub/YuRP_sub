package yurp.model;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;


@Mapper
public interface AsMapper {

	//------공통
	@Select("SELECT ascenter.*, a.s_name as sName, a.addr as addr from ascenter "
			+ "INNER JOIN store AS a ON a.s_code = ascenter.s_code")
	List<AsDTO> list(AsDTO dto);
	
	@Select("SELECT ascenter.*, a.s_name, b.p_name "
			+ "FROM ascenter "
			+ "INNER JOIN store AS a ON a.s_code = ascenter.s_code "
			+ "INNER JOIN product AS b ON b.p_code = ascenter.p_code "
			+ "WHERE ascenter.a_no = #{aNo}")
	AsDTO detail(int aNo);
	
	//------본사
	@Update("update ascenter set "
			+ "res_con = #{resCon}, as_price = #{asPrice}, as_res = #{asRes}, as_prog = #{asProg} "
			+ "where a_no = #{aNo}")
	int modify(AsDTO dto);
	
	
	//------매장
	@Insert("insert into ascenter (as_num, s_code, as_name, as_tel, p_code, as_note, pur_date) values "
			+ "(#{asNum},#{sCode},#{asName},#{asTel},#{pCode},#{asNote},#{purDate})")
	int storeInsert(AsDTO dto);
	
	// 접수번호 자동
	@Select("SELECT as_num+1 from ascenter ORDER BY a_no DESC LIMIT 1")
	int asNumSelect(AsDTO dto);
	
	@Update("update ascenter set "
			+ "as_name = #{asName}, as_tel = #{asTel}, p_code = #{pCode}, as_note = #{asNote}, pur_date = #{purDate} "
			+ "where a_no = #{aNo}")
	int storeModify(AsDTO dto);
	
	@Delete("delete from ascenter where a_no = #{aNo}")
	int delete(AsDTO dto);
	
	
	//------검색
	//매장
	@Select("SELECT * FROM store")
	List<AsDTO> sNames();
	
	//전체 매장
	@Select({
		" <script> "
		," SELECT * FROM store "
		, " <where> "
		, " 	<if test='sName != null'> "
		, " 		AND s_name like concat('%', #{sName}, '%')"
		, " 	</if> "
		, " 	<if test='sCode != null'> "
		, " 		AND s_code like concat('%', #{sCode}, '%')"
		, " 	</if> "
		, " </where> "
		, " </script> "})
	List<AsDTO> sCodeSearch(AsDTO dto);
	
	//전체 상품
	@Select({
		" <script> "
		," SELECT * FROM product "
		, " <where> "
		, " 	<if test='pName != null'> "
		, " 		AND p_name like concat('%', #{pName}, '%')"
		, " 	</if> "
		, " 	<if test='pCode != null'> "
		, " 		AND p_code like concat('%', #{pCode}, '%')"
		, " 	</if> "
		, " </where> "
		, " </script> "})
	List<AsDTO> pCodeSearch(AsDTO dto);
	
	
	@Select({
		" <script> "
		," SELECT ascenter.*, a.s_name as sName from ascenter "
		, "INNER JOIN store AS a ON a.s_code = ascenter.s_code"
		, " <where> "
		, " 	<if test='start != null and start != \"\"' > "
		, "			AND reg_date >= #{start} "
		, " 	</if> "
		, " 	<if test='end != null and end != \"\"' > "
		, "			AND reg_date &lt;= #{end} "
		, " 	</if> "
		, " 	<if test='start != null and start != \"\" and end != null and end != \"\"' > "
		, "			AND reg_date BETWEEN #{start} and #{end} "
		, " 	</if> "
		, " 	<if test='sName != null'> "
		, " 		AND a.s_name = #{sName} "
		, " 	</if> "
		, " 	<if test='asRes != null'> "
		, " 		AND as_res = #{asRes} "
		, " 	</if> "
		, " 	<if test='asProg != null'> "
		, " 		AND as_prog = #{asProg} "
		, " 	</if> "
		, " 	<if test='asNum != null'> "
		, " 		AND as_num like concat('%', #{asNum}, '%')"
		, " 	</if> "
		, " </where> "
		, " </script> "})
	List<AsDTO> listPname(AsDTO dto);

}
