package yurp.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface InandoutMapper {

	@Select("<script>"
			+ "select "
			+ "io_no,  "
			+ "reg_date,  "
			+ "io_stat,  "
			+ "type,  "
			+ "start,  "
			+ "arrival,  "
			+ "tot_cnt,  "
			+ "tot_price,  "
			+ "process "
			+ "from inandout  "
			+ "<where>"
			+ "	<trim prefix=' ' suffixOverrides = 'and | or'> "
			
			+ "		<if test='ioStat != null and ioStat != \"\"' > "
			+ "			io_stat like concat('%',#{ioStat},'%') and"
			+ "		</if> "
			+ "		<if test='type != null and type != \"\"' > "
			+ "			type like concat('%',#{type},'%') and"
			+ "		</if> "
			
			+ "		<if test='process != null and process != \"\"' > "
			+ "			process = #{process} and"
			+ "		</if> "
			
			+ "		<if test='sday != null and sday != \"\"' > "
			+ "			reg_date >= #{sday} and"
			+ "		</if> "
			
			+ "		<if test='eday != null and eday != \"\"' > "
			+ "			 #{eday} >= reg_date and"
			+ "		</if> "
			
			+ "		<if test='sday != null and sday != \"\" and eday != null and eday != \"\"' > "
			+ "			reg_date BETWEEN #{sday} and #{eday}"
			+ "		</if> "
			+ "	</trim>"
			+ "</where> "
			+ "</script> "
			 )
	List<InandoutDTO> list(InandoutDTO dto);
	
	@Select("<script> "
			+ "select "
			+ "sum(tot_cnt) as tot_cnt, "
			+ "sum(tot_price) as tot_price "
			+ "from inandout "
			+ "<where>"
			+ "	<trim prefix=' ' suffixOverrides = 'and | or'> "
			
			+ "		<if test='ioStat != null and ioStat != \"\"' > "
			+ "			io_stat like concat('%',#{ioStat},'%') and"
			+ "		</if> "
			+ "		<if test='type != null and type != \"\"' > "
			+ "			type like concat('%',#{type},'%') and"
			+ "		</if> "
			
			+ "		<if test='process != null and process != \"\"' > "
			+ "			process = #{process} and"
			+ "		</if> "
			
			+ "		<if test='sday != null and sday != \"\"' > "
			+ "			reg_date >= #{sday} and"
			+ "		</if> "
			
			+ "		<if test='eday != null and eday != \"\"' > "
			+ "			 #{eday} >= reg_date and"
			+ "		</if> "
			
			+ "		<if test='sday != null and sday != \"\" and eday != null and eday != \"\"' > "
			+ "			reg_date BETWEEN #{sday} and #{eday}"
			+ "		</if> "
			+ "	</trim>"
			+ "</where> "
			+ "</script> "
			 )
	List<InandoutDTO> tot(InandoutDTO dto);
	

	@Select("<script> "
			+"select "
			+ "a.p_code, "
			+ "a.io_stat, "
			+ "e.reg_date, "
			+ "a.cnt , "
			+ "b.p_num , "
			+ "b.p_name , "
			+ "b.color , "
			+ "b.p_size , "
			+ "b.season , "
			+ "b.p_price , "
			+ "b.discount , "
			+ "b.li_price , "
			+ "c.b_name , "
			+ "d.s_name , "
			+ "d.grade , "
			+ "(a.cnt * b.p_price) as tot_price "
			+ "from iodetail as a "
			+ "left join product as b on a.p_code = b.p_code "
			+ "left join brand as c on a.b_code = c.b_code "
			+ "left join store as d on a.s_code = d.s_code "
			+ "left join inandout as e on a.io_stat = e.io_stat "
			+ "<where>"
			+ "		<if test='search != null and search != \"\"' > "
			+ " 		a.io_stat = #{search} "
			+ "		</if> "
			+ "</where> "
			+ "</script> "
			 )
	List<InandoutDTO> viewDetail(InandoutDTO dto);
	
	
	@Select("select * from store where s_code != 'as'")
	List<StoreDTO> storeList();
	

	@Select("<script>"
			+ "select DISTINCT p.*,i.s_code,i2.s_code, "
			+ " i.cnt AS start_cnt, "
			+ "	i2.cnt AS arr_cnt "
			+ "from product p "
			+ " left JOIN  inventory i on i.s_code = #{start}"//출밞매장
			+ " left JOIN  inventory i2 on i2.s_code = #{arrival}" //도착매장	
			+ "<where>"
			+ "	<trim prefix=' ' suffixOverrides = 'and | or'> "
			+   "<if test='bCode != null'> "
			+   "p.b_code=#{bCode} and "
			+   "</if>"			
			+ 	"<if test='pCode != null and pCode != \"\"'> "
			+ 	"p.p_code like concat('%',#{pCode},'%') "
			+ 	"</if>"
			+ "	</trim>"
			+ "</where> "
			+ "</script> ")
	List<ProductDTO> insertProdList(HashMap<String, String> param);
	
	@Select("<script>"
			+ "select p.*, s_code, cnt as cnt, mov_cnt "
			+ "from inventory i "
			+ "join product as p on i.p_code = p.p_code "
			+ "<where>"
			+ "i.s_code = #{start} and "
			+ "	<trim prefix=' ' suffixOverrides = 'and | or'> "
			+   "<if test='bCode != null'> "
			+   "p.b_code=#{bCode} and "
			+   "</if>"			
			+ 	"<if test='pCode != null and pCode != \"\"'> "
			+ 	"p.p_code like concat('%',#{pCode},'%') "
			+ 	"</if>"
			+ "	</trim>"
			+ "</where>"
			+ "</script>")
	List<ProductDTO> storeProdList(HashMap<String, String> param);
	
	
//	@Select({"<script>"
//		, "<foreach collection='arr' item='prod' separator=' ' index='i'>"
//		, "select * from product p "
//		, "where p_code = #{prod};"
//		, "</foreach>"
//		,"</script>"})
	@Select({
		" <script> "
		,"select * from product "
		, "<where> "
		, "p_code in ("
		, "<foreach collection='arr' item='prod' separator=',' index='i'> "
		, "<if test='prod != null'> "
		, "#{prod} "
		, "</if> "
		, "</foreach> "
		, ")"
		, "</where> "
		, "</script> "
	})
	List<ProductDTO> add(String [] arr);
}
