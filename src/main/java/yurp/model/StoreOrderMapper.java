package yurp.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface StoreOrderMapper {
	
	@Select("<script>"
			+ "select rt.*, s.s_name "
			+ "from rt "
			+ "join store as s on rt.s_code = s.s_code "
			+ "<where>"
			+ "	<trim prefix=' ' suffixOverrides = 'and | or'> "
			+ "		<if test='rStat != null and rStat != \"\"' > "
			+ "		r_stat like concat('%',#{rStat},'%') and"
			+ "		</if> "
			+ "		<if test='start != null and start != \"\"' > "
			+ "			request_date >= #{start} and"
			+ "		</if> "
			+ "		<if test='end != null and end != \"\"' > "
			+ "			 #{end} >= request_date and"
			+ "		</if> "
			+ "		<if test='start != null and start != \"\" and end != null and end != \"\"' > "
			+ "			request_date BETWEEN #{start} and #{end}"
			+ "		</if> "
			+ "		<if test='proceseRes != null and proceseRes != \"\"' > "
			+ "			 procese_res = #{proceseRes} and"
			+ "		</if> "
			+ "	</trim>"
			+ "</where> "
			+ "</script> ")
	List<StoreOrderDTO> list(StoreOrderDTO dto);
	
	
	@Select("<script>"
			+ "select * from rt "
			+ "<where>"
			+ "	<trim prefix=' ' suffixOverrides = 'and | or'> "
			+ "  out_store != '본사' and in_store != '본사' and "
			+ "		<if test='rStat != null and rStat != \"\"' > "
			+ "		r_stat like concat('%',#{rStat},'%') and"
			+ "		</if> "
			+ "		<if test='start != null and start != \"\"' > "
			+ "			request_date >= #{start} and"
			+ "		</if> "
			+ "		<if test='end != null and end != \"\"' > "
			+ "			 #{end} >= request_date and"
			+ "		</if> "
			+ "		<if test='start != null and start != \"\" and end != null and end != \"\"' > "
			+ "			request_date BETWEEN #{start} and #{end}"
			+ "		</if> "
			+ "		<if test='proceseRes != null and proceseRes != \"\"' > "
			+ "			 procese_res = #{proceseRes} and"
			+ "		</if> "
			+ "		<if test='sName != null and sName != \"\"' > "
			+ "			 in_store = #{sName} and"
			+ "		</if> "
			+ "	</trim>"
			+ "</where> "
			+ "</script> ")
	List<StoreOrderDTO> rtlist(StoreOrderDTO dto);
	
	
	//...
	@Select("select p.p_num, p.p_code, p.color ,p.p_size, p.li_price, r.r_stat, p.p_name, r.b_code, r.cnt, r.s_code, r.price, rt.procese_res, "
			+ "i.cnt AS store_cnt, "
			+ "i2.cnt AS admin_cnt, "
			+ "b.b_name as b_name, "
			+ "s.s_name as s_name "
			+ "from rtdetail r "
			+ "join rt on r.r_stat = rt.r_stat "
			+ "join product p on r.p_code = p.p_code "
			+ "JOIN  inventory i ON i.p_code = r.p_code AND i.s_code = r.s_code " //세션
			+ "JOIN inventory i2 ON i2.p_code = r.p_code AND i2.s_code = 'admin' "
			+ "join brand b on b.b_code = p.b_code "
			+ "join store s on s.s_code = i.s_code "
			+ "where r.r_stat = #{rStat} "
			+ "GROUP BY p.p_num, p.color, p.p_size, r_stat, p.p_name, r.b_code, r.cnt ")
	List<StoreOrderDTO> detail(String rStat);
	
	@Select("select * from rt "
			+ "where r_stat = #{rStat}")
	StoreOrderDTO getSO(String rStat);
	
	
	@Select("select p.p_num ,p.color ,p.p_size,r.r_stat,p.p_name,r.b_code, r.req_cnt, "
			+ " i.cnt AS store_cnt, "
			+ " i2.cnt AS in__cnt "
			+ " , r2.out_store, r2.in_store "
			+ "from rtdetail r "
			+ "join product p on r.p_code = p.p_code "
			+ "JOIN  inventory i ON i.p_code = r.p_code AND i.s_code = (select s_code from store s where s.s_name = '굽은다리점') " //요청하는 매장 (세션)
			+ "JOIN inventory i2 ON i2.p_code = r.p_code AND i2.s_code = (select s_code from store s where s.s_name = '강남그린점') " //요청 받는매장
			+ "join rt r2 on r2.out_store = '강남그린점' AND  r2.in_store = '굽은다리점' " //요청 받는 매장과 하는 매장
			+ "where r.r_stat = #{rStat}"
			+ "GROUP BY p.p_num, p.color, p.p_size, r.r_stat, p.p_name, r.b_code, r.req_cnt ")
	List<StoreOrderDTO> rtdetail(String rStat);
	
	
	@Select("select * from store")
	List<StoreDTO> slist();
	
	@Select("select * from brand")
	List<BrandDTO> blist();

	@Select("select * from product")
	List<ProductDTO> plist();
	
	@Select("select max(r_stat) from rt where request_date = CURDATE()")
	String maxStat();
	
	@Insert("insert into rt (r_stat,stat_cnt,stat_price,out_store,in_store,request_date,s_code) "
			+ "values (#{rStat},#{statCnt},#{statPrice},'강남그린점','굽은다리점',sysdate(),'yurp001')") //세션
	int insert(StoreOrderDTO dto);
	
	
	@Insert({"<script>"
			+ "<foreach collection='rtArr' item='rt' separator=';' index='i'>"
				+ "insert into rtdetail(s_code,b_code,p_code,r_stat,req_cnt) "
				+ "values ("
				+"(select s_code from store  where s_name = '강남그린점'), #{rt.bCode}," //세션
				+ "(select p_code from product where p_num = #{rt.pNum} and color = #{rt.color} and p_size = #{rt.pSize}),"
				+ "(select max(r_stat) from rt where request_date = CURDATE()),#{rt.reqCnt})"
			+ "</foreach>"
			+"</script>"})
	int detailInsert(ArrayList<StoreOrderDTO> rtArr,String rStat, String sName);
	
	
	@Select("<script>"
			+ "select p.*, s_code, i.cnt as cnt "
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
		, "i.s_code=#{arrival} "	
		, "</where> "
		, "</script> "
	})
	List<ProductDTO> add(String [] arr, String arrival);

	
	@Insert({
		"<script>"
		,"insert into rt "
		,"(r_stat, out_store, in_store, stat_price, stat_cnt, s_code) values "
		,"<if test='inStore != null'> "
		,"(#{rStat}, #{outStore}, #{inStore}, #{statPrice}, #{statCnt}, #{inStore}) "
		,"</if>"
		,"</script>"
	})
	int insertSO(StoreOrderDTO dto);

	
	@Insert({
		"<script>"
		,"insert into rtdetail "
		,"(r_stat, price, cnt, b_code, p_code, s_code) values "
		, "<foreach collection='arr' item='prod' separator=',' index='i'>"
		, "<if test='prod.pCode != null'>"
		, "(#{prod.rStat}, #{prod.price}, #{prod.cnt}, #{prod.bCode}, #{prod.pCode}, #{prod.inStore}) "
		, "</if>"
		, "</foreach>"
		,"</script>"
	})
	int insertSODetail(ArrayList<StoreOrderDTO> arr);

}


