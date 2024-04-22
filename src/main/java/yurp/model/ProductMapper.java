package yurp.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface ProductMapper {

	@Select(
			"<script>"
			+ "select p.*, b.b_name "
			+ "from product as p join brand as b on p.b_code = b.b_code "
			+ "<where> "
			
			+ "<trim prefix=' ' suffixOverrides = 'and | or'>"
			+ "<if test='season != null'> "
			+ "season=#{season} and"
			+ "</if>"
			+ "<if test='bCode != null'> "
			+ "p.b_code=#{bCode} and"
			+ "</if>"
			+ "<if test='pName != null and pName != \"\"'> "
			+ "p_name like concat('%',#{pName},'%') and"
			+ "</if>"
			+ "<if test='pCode != null and pCode != \"\"'> "
			+ "p_code like concat('%',#{pCode},'%') "
			+ "</if>"
			+ "</trim>"

			+ "</where>"
			+ "</script>")
	List<ProductDTO> list(ProductDTO dto);
	

//창고재고
	@Select("<script>"
			+ "select DISTINCT p.p_code ,p.p_num ,p.color ,p.p_size,p.p_name,p.b_code,i.s_code,i2.s_code,p.li_price, "
			+ " i.cnt AS store_cnt, "
			+ "	i2.cnt AS admin__cnt "
			+ "from product p "
			+ " JOIN  inventory i on i.s_code = 'yurp002'"//세션
			+ " JOIN  inventory i2 on i2.s_code = 'admin'"
			+ "<where>"
			+ " i.p_code = p.p_code and p.b_code = #{bCode} "
			+ "	<trim prefix=' ' suffixOverrides = 'and | or'> "
			+ 	"<if test='pNumChk != null and pNumChk != \"\"'> "
			+ 	"and p.p_num like concat('%',#{pNumChk},'%') "
			+ 	"</if>"
			+ "	</trim>"
			+ "</where> "
			+ "</script> "
			 )
	List<ProductDTO> prodList(ProductDTO dto);
	
		
	//타매장
		@Select("<script>"
				+ "select DISTINCT p.p_code ,p.p_num ,p.color ,p.p_size,p.p_name,p.b_code,i.s_code,i2.s_code,p.li_price, "
				+ " i.cnt AS store_cnt, "
				+ "	i2.cnt AS in__cnt "
				+ "from product p "
				+ " JOIN  inventory i on i.s_code = 'admin'"//세션
				+ " JOIN  inventory i2 on i2.s_code = (select s_code from store s where s.s_name = #{sName})" //파라미터?	
				+ "<where>"
				+ " i.p_code = p.p_code and p.b_code = #{bCode}  "
				+ "	<trim prefix=' ' suffixOverrides = 'and | or'> "
				+ 	"<if test='pNumChk != null and pNumChk != \"\"'> "
				+ 	"and p.p_num like concat('%',#{pNumChk},'%') "
				+ 	"</if>"
				+ "	</trim>"
				+ "</where> "
				+ "</script> "
				 )
		List<ProductDTO> storeProdList(ProductDTO dto);
	
	@Select("select * from brand")
	List<BrandDTO> brandList();
	
	@Select("select season from product "
			+ "group by season "
			+ "order by season desc")
	List<String> seasonList();
	
	@Select("select * from store "
			+ "where s_code not in ('as','admin')")
	List<StoreDTO> storeList();
	
	
	@Insert({
		" <script> "
		,"insert into product "
		, "(b_code, season, grade, p_name, p_num, color, p_size, p_code, li_price, p_price) values "
		, "<foreach collection='arr' item='prod' separator=',' index='i'>"
		, "<if test='prod.bCode != null'>"
		, "(#{prod.bCode}, #{prod.season}, #{prod.grade}, "
		, "#{prod.pName}, #{prod.pNum}, #{prod.color}, #{prod.pSize}, #{prod.pCode}, "
		, "#{prod.liPrice}, #{prod.liPrice})"
		, "</if>"
		, "</foreach>"
		, "</script> "})
	int insert(ArrayList<ProductDTO> arr);
	
	@Insert({
		"<script>"
		,"insert into inventory "
		,"(p_code, cnt, s_code) values "
		, "<foreach collection='arr' item='prod' separator=',' index='i'>"
		, "<if test='prod.pCode != null'>"
		, "(#{prod.pCode}, #{prod.cnt}, 'admin') "
		, "</if>"
		, "</foreach>"
		,"</script>"
	})
	int setInvenAdmin(ArrayList<ProductDTO> arr);
	
	@Insert({
		"<script>"
		,"insert into inventory "
		,"(p_code, cnt, s_code) values "
		, "<foreach collection='arr' item='prod' separator=',' index='i'>"
		, "<if test='prod.pCode != null'>"
		, 	"<foreach collection='stArr' item='st' separator=',' index='i'>"
		, 		"(#{prod.pCode}, 0, #{st.sCode}) "
		,	 "</foreach>"
		, "</if>"
		, "</foreach>"
		,"</script>"
	})
	int setInvenStore(ArrayList<ProductDTO> arr, ArrayList<StoreDTO> stArr);
	
	
	@Select({
		"<script>"
		, "<foreach collection='arr' item='prod' separator=';' index='i'>"
			, "select p.b_code, p.p_num, p.color, p.p_size, p.p_name, p.p_price, i.cnt, i.i_no, li_price "
			, "from product p "
			, "join inventory i "
			, "where i.i_no = #{prod.iNo}"
		, "</foreach>"
		,"</script>"})
	ArrayList<ProductDTO> excelArr(ArrayList<ProductDTO> arr);

	@Delete({
		" <script> "
		,"delete from product "
		, "<where> "
		, "p_no in ("
		, "<foreach collection='arr' item='prod' separator=',' index='i'> "
		, "<if test='prod.pNo != null'> "
		, "#{prod.pNo} "
		, "</if> "
		, "</foreach> "
		, ")"
		, "</where> "
		, "</script> "
	})
	int delete(ArrayList<ProductDTO> arr);
	
	@Select({
		" <script> "
		,"select * from product "
		, "<where> "
		, "p_no in ("
		, "<foreach collection='arr' item='prod' separator=',' index='i'> "
		, "<if test='prod.pNo != null'> "
		, "#{prod.pNo} "
		, "</if> "
		, "</foreach> "
		, ")"
		, "</where> "
		, "</script> "
	})
	List<ProductDTO> modList(ArrayList<ProductDTO> arr);
	

	@Update({
		" <script> "
		, "<foreach collection='arr' item='prod' separator=' ' index='i'> "
		,"update product set "
		//, "<if test='prod.pNo != null'> "
		, "b_code=#{prod.bCode}, season=#{prod.season}, grade=#{prod.grade}, p_name=#{prod.pName}, p_num=#{prod.pNum}, color=#{prod.color}, p_size=#{prod.pSize}, p_code=#{prod.pCode}, li_price=#{prod.liPrice}, discount=#{prod.discount}, p_price=#{prod.pPrice} "
		//, "</if> "
		//, "<where> "
		, "where "
		, "p_no = #{prod.pNo}; "
		//, "</where> "
		, "</foreach> "
		, "</script> "
	})
	int modReg(ArrayList<ProductDTO> arr);
	

	
	//규휘가 말아보는 상품 검색 
	@Select({
		" <script> "
		," SELECT * from product "
		, "INNER JOIN brand AS a ON a.b_code = product.b_code"  
		, "INNER JOIN inventory AS b ON b.cnt = product.cnt"  
		, " <where> "
		, " 	<if test='bCode != null'> "
		, " 		AND a.b_code = #{bCode} "
		, " 	</if> "
		, " 	<if test='pNum != null'> "
		, " 		AND p_num like concat('%', #{pNum}, '%')"
		, " 	</if> "
		, " </where> "
		, " </script> "})
	List<ProductDTO> storeOrderS(ProductDTO dto);
}
