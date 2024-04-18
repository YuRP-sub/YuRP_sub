package yurp.model;

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
			+ "type1,  "
			+ "type2,  "
			+ "start,  "
			+ "arrival,  "
			+ "tot_cnt,  "
			+ "tot_price,  "
			+ "r_stat "
			+ "from inandout  "
			+ "<where>"
			+ "	<trim prefix=' ' suffixOverrides = 'and | or'> "
			
			+ "		<if test='ioStat != null and ioStat != \"\"' > "
			+ "			io_stat like concat('%',#{ioStat},'%') and"
			+ "		</if> "
			+ "		<if test='type1 != null and type1 != \"\"' > "
			+ "			type1 like concat('%',#{type1},'%') and"
			+ "		</if> "
			
			+ "		<if test='rStat != null and rStat != \"\"' > "
			+ "			r_stat = #{rStat} and"
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
			+ "		<if test='type1 != null and type1 != \"\"' > "
			+ "			type1 like concat('%',#{type1},'%') and"
			+ "		</if> "
			
			+ "		<if test='rStat != null and rStat != \"\"' > "
			+ "			r_stat = #{rStat} and"
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
	
}
