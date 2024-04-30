package yurp.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface InvenMapper {
	
	
	@Select("select distinct ${vv} from inventory")
	ArrayList<String> invenCode(String vv);
	
	@Select("select * from product where p_code = #{vv} ")
	InvenDTO productDetail(String vv);
	
	@Select("<script>"
			+ "select distinct "
			+ "a.p_code "
			+ "from inventory as a "
			+ "left join product as b on a.p_code = b.p_code "
			+ "<where>"
			+ "	<trim prefix=' ' suffixOverrides = 'and | or'> "
			+ "		<if test='season != null and season != \"\"' > "
			+ "			b.season like concat('%',#{season},'%') and"
			+ "		</if> "
			+ "		<if test='pName != null and pName != \"\"' > "
			+ "			b.p_name like concat('%',#{pName},'%') and"
			+ "		</if> "
			+ "		<if test='pSize != null and pSize != \"\"' > "
			+ "			b.p_size like concat('%',#{pSize},'%') and "
			+ "		</if> "
			+ "		<if test='color != null and color != \"\"' > "
			+ "			b.color like concat('%',#{color},'%') "
			+ "		</if> "
			+ "	</trim>"
			+ "</where> "
			+ "</script> ")	
	ArrayList<String> pSearch(InvenDTO dto);
	
	@Select("select "
			+ "p_code, "
			+ "s_code, " 
			+ "sum(cnt) as s_cnt "
			+ "from inventory  "
			+ "where s_code = #{s_code} "
			+ "group by p_code, s_code ")
	ArrayList<InvenSCntDTO> invenS_Cnt(String s_code);
	
//	@Select("<script>"
//			+ "select "
//			+ "a.p_code, "
//			+ "a.s_code, "
//			+ "sum(a.cnt) as s_cnt "
//			+ "from inventory as a "
//			+ "left join product as b on a.p_code = b.p_code "
//			+ "<where>"
//			+ "	<trim prefix=' ' suffixOverrides = 'and | or'> "
//			+ " 		s_code = #{s_code} and "
//			+ "		<if test='season != null and season != \"\"' > "
//			+ "			b.season like concat('%',#{season},'%') and"
//			+ "		</if> "
//			+ "		<if test='pName != null and pName != \"\"' > "
//			+ "			b.p_name like concat('%',#{pName},'%') and"
//			+ "		</if> "
//			
//			+ "		<if test='pSize != null and pSize != \"\"' > "
//			+ "			b.p_size like concat('%',#{pSize},'%') and "
//			+ "		</if> "
//			
//			+ "		<if test='color != null and color != \"\"' > "
//			+ "			b.color like concat('%',#{color},'%') "
//			+ "		</if> "
//			
//			+ "	</trim>"
//			+ "</where> "
//			+ "group by a.p_code, a.s_code"
//			+ "</script> "
//			)
//	ArrayList<InvenSCntDTO> invenS_Cnt(String s_code);
	
	
	//@Select(ttt)
	//List<InvenDTO> list22(InvenDTO dto);

	@Select("<script>"
			+ "select "
			+ "a.i_no , "
			+ "b.p_name , "
			+ "b.season , "
			+ "b.p_size , "
			+ "b.color , "
			+ "a.cnt, "
			+ "b.li_price , "
			+ "b.p_price "
			+ "from inventory as a "
			+ "left join product as b on a.p_code = b.p_code "
			+ "<where>"
			+ "	<trim prefix=' ' suffixOverrides = 'and | or'> "
			
			+ "		<if test='season != null and season != \"\"' > "
			+ "			b.season like concat('%',#{season},'%') and"
			+ "		</if> "
			+ "		<if test='pName != null and pName != \"\"' > "
			+ "			b.p_name like concat('%',#{pName},'%') and"
			+ "		</if> "
			
			+ "		<if test=' pm != null and pm != \"\" ' > "
			+ "			<choose> "
			+ "				<when test='pm == 1 '>"
			+ "					a.cnt >= 0 "
			+ "				</when> "
			+ "				<otherwise> "
			+ "				 	0 >	a.cnt "
			+ "				</otherwise> "
			+ "			</choose> "
			+ "		</if> "
			
			+ "		<if test='pSize != null and pSize != \"\"' > "
			+ "			b.p_size like concat('%',#{pSize},'%') and "
			+ "		</if> "
			
			+ "		<if test='color != null and color != \"\"' > "
			+ "			b.color like concat('%',#{color},'%') "
			+ "		</if> "
			
			+ "	</trim>"
			+ "</where> "
			+ "</script> "
			 )
	List<InvenDTO> list(InvenDTO dto);
	
	
	@Select("select "
			+ "s_name "
			+ "from store "
			+ "where s_name not in ('as센터') "
			)
	List<InvenDTO> stList(InvenDTO dto);
	
	
//	@Select("<script> "
//			+ "select "
//			+ "sum(b.p_price*a.cnt) as all_tot "
//			+ "from sell as a "
//			+ "left join product as b on a.p_code = b.p_code "
//			+ "left join store as c on a.s_code = c.s_code"
//			+ "<where>"
//			+ "	<trim prefix=' ' suffixOverrides = 'and | or'> "
//			+ "		<if test='pNum != null and pNum != \"\"' > "
//			+ "			b.p_num like concat('%',#{pNum},'%') and"
//			+ "		</if> "
//			+ "		<if test='pName != null and pName != \"\"' > "
//			+ "			b.p_name like concat('%',#{pName},'%') and"
//			+ "		</if> "
//			
//			+ "		<if test='sName != null and sName != \"\"' > "
//			+ "			c.s_name like concat('%',#{sName},'%') and"
//			+ "		</if> "
//			
//			+ "		<if test='start != null and start != \"\"' > "
//			+ "			a.sell_date >= #{start} and"
//			+ "		</if> "
//			
//			+ "		<if test='end != null and end != \"\"' > "
//			+ "			 #{end} >= a.sell_date and"
//			+ "		</if> "
//			
//			+ "		<if test='start != null and start != \"\" and end != null and end != \"\"' > "
//			+ "			a.sell_date BETWEEN #{start} and #{end}"
//			+ "		</if> "
//			+ "	</trim>"
//			+ "</where> "
//			+ "</script> ")
//	List<SellDTO> tot(SellDTO dto);
	
	
}
