package yurp.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

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
			+ "			io_stat like concat('%',#{ioStat},'%') and "
			+ "		</if> "
			+ "		<if test='type != null and type != \"\"' > "
			+ "			type like concat('%',#{type},'%') and "
			+ "		</if> "
			
			+ "		<if test='process != null and process != \"\"' > "
			+ "			process = #{process} and "
			+ "		</if> "
			
			+ "		<if test='sday != null and sday != \"\"' > "
			+ "			reg_date >= #{sday} and "
			+ "		</if> "
			
			+ "		<if test='start != null and start != \"\"' > "
			+ "			start = #{start} and "
			+ "		</if> "
			+ "		<if test='arrival != null and arrival != \"\"' > "
			+ "			arrival = #{arrival}  and "
			+ "		</if> "

			
			+ "		<if test='eday != null and eday != \"\"' > "
			+ "			 #{eday} >= reg_date and "
			+ "		</if> "
			
			+ "		<if test='sday != null and sday != \"\" and eday != null and eday != \"\"' > "
			+ "			reg_date BETWEEN #{sday} and #{eday}"
			+ "		</if> "
			+ "	</trim>"
			+ "</where> "
			+ "order by reg_date desc"
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
			+ " a.io_stat = #{ioStat} "

			+ "</where> "
			+ "</script> "
			 )
	List<InandoutDTO> viewDetail(String ioStat);
	
	@Select("select i.*, s.s_name as start_name, s2.s_name as arrival_name, "
			+ "s.manager as start_manager, s2.manager as arrival_manager"			
			+ " from inandout as i "
			+ "join store s on i.start = s.s_code "
			+ "join store s2 on i.arrival = s2.s_code "
			+ "where io_stat = #{ioStat}")
	InandoutDTO getIO(String ioStat);
	
	@Select("select i.*, p.*, b.b_name "
			+ "from iodetail i "
			+ "join product p on i.p_code = p.p_code "
			+ "join brand b on i.b_code = b.b_code "
			+ "where i.io_stat = #{ioStat}")
	List<InandoutDTO> detail(String ioStat);
	
	
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
			+ 	"p.p_code like concat('%',#{pCode},'%') and "
			+ 	"cnt &gt; 0 "
			+ 	"</if>"
			+ "	</trim>"
			+ "</where>"
			+ "</script>")
	List<ProductDTO> storeProdList(HashMap<String, String> param);
	
	
	@Select({
		" <script> "
		,"select p.*, i.cnt, i.mov_cnt, i.s_code, b.b_name "
		, "from product p "
		, "join inventory as i on p.p_code = i.p_code "
		, "join brand as b on p.b_code = b.b_code "
		, "<where> "	
		, "p.p_code in ("
		, "<foreach collection='arr' item='prod' separator=',' index='i'> "
		, "<if test='prod != null'> "
		, "#{prod} "
		, "</if> "
		, "</foreach> "
		, ") and "
		, "i.s_code=#{start} "	
		, "</where> "
		, "</script> "
	})
	List<ProductDTO> add(String [] arr, String start);



	@Insert({
		"<script> "
		,"insert into inandout "
		, "(io_stat, type, start, arrival, tot_price, tot_cnt, process, s_code) values "
		, "<if test='arrival != null'> "
		, "(#{ioStat}, #{type}, #{start}, "
		, "#{arrival}, #{totPrice}, #{totCnt}, '미처리', #{start}) "
		, "</if>"
		, "</script>"})
	int insertIo(InandoutDTO dto);

	
	@Insert({
		"<script>"
		,"insert into iodetail "
		,"(io_stat, price, cnt, b_code, p_code, s_code) values "
		, "<foreach collection='arr' item='prod' separator=',' index='i'>"
		, "<if test='prod.pCode != null'>"
		, "(#{prod.ioStat}, #{prod.price}, #{prod.cnt}, #{prod.bCode}, #{prod.pCode}, #{prod.start}) "
		, "</if>"
		, "</foreach>"
		,"</script>"
	})
	int insertIoDetail(ArrayList<InandoutDTO> arr);
	
	@Insert({
		"<script>"
		, "<foreach collection='arr' item='prod' separator=' ' index='i'>"
		, "<if test='prod.pCode != null'>"
		,"update inventory set "
		,"mov_cnt= (mov_cnt + #{prod.cnt}) "
		, "where s_code=#{prod.arrival} and p_code=#{prod.pCode}; "
		,"update inventory set "
		,"cnt=#{prod.resCnt} "
		, "where s_code=#{prod.start} and p_code=#{prod.pCode}; "
		, "</if>"
		, "</foreach>"
		,"</script>"
	})
	int calcInven(ArrayList<InandoutDTO> arr);
	
	@Update({
		" <script> "
		, "<foreach collection='arr' item='prod' separator=' ' index='i'> "
		,"update product set "
		, "b_code=#{prod.bCode}, season=#{prod.season}, grade=#{prod.grade}, p_name=#{prod.pName}, p_num=#{prod.pNum}, color=#{prod.color}, p_size=#{prod.pSize}, p_code=#{prod.pCode}, li_price=#{prod.liPrice}, discount=#{prod.discount}, p_price=#{prod.pPrice} "
		, "where "
		, "p_no = #{prod.pNo}; "
		, "</foreach> "
		, "</script> "
	})
	int removeCnt(ArrayList<InandoutDTO> arr);
	
	@Update(
		"<script>"
		+ "update inandout set "
		+ "process = '거절' "
		+ "where io_stat = #{ioStat} "
		+ "</script>")
	int reject(String ioStat);
	
	
	@Update({
		"<script>"
		, "<foreach collection='arr' item='prod' separator=' ' index='i'>"
		, "<if test='prod.pCode != null'>"
		,"update inventory set "
		,"cnt = (cnt + #{prod.cnt}) "
		, "where s_code=#{prod.start} and p_code=#{prod.pCode}; "
		,"update inventory set "
		,"mov_cnt= (mov_cnt - #{prod.resCnt}) "
		, "where s_code=#{prod.arrival} and p_code=#{prod.pCode}; "
		, "</if>"
		, "</foreach>"
		,"</script>"})
	int rejCalcInven(ArrayList<InandoutDTO> arr);
	
	@Update(
		"<script>"
		+ "update inandout set "
		+ "process = '승인' "
		+ "where io_stat = #{ioStat} "
		+ "</script>")
	int accept(String ioStat);
	
	
	@Update({
		"<script>"
		, "<foreach collection='arr' item='prod' separator=' ' index='i'>"
		, "<if test='prod.pCode != null'>"
		,"update inventory set "
		,"mov_cnt=(mov_cnt - #{prod.cnt}), "
		,"cnt=(cnt + #{prod.cnt}) "
		, "where s_code=#{prod.arrival} and p_code=#{prod.pCode}; "
		, "</if>"
		, "</foreach>"
		,"</script>"})
	int acceptCalcInven(ArrayList<InandoutDTO> arr);

	
}
