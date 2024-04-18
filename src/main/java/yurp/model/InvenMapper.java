package yurp.model;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface InvenMapper {

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
