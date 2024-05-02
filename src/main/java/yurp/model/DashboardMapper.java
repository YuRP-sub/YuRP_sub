package yurp.model;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface DashboardMapper {

	@Select("select * from notice "
			+ "order by n_no "
			+ "desc limit 10")
	List<NoticeDTO> notice();
	

	@Select("select a.*, s.s_name from ascenter as a "
			+ "join store as s on a.s_code = s.s_code "
			+ "where a.reg_date = #{today} "
			+ "order by a.a_no "
			+ "desc limit 10")
	List<AsDTO> as(String today);
	
	@Select({"<script>"
			,"select s.*, b.b_code, b.b_name, "
			, "sum(cnt*p.p_price) as resPrice, "
			,"sum(cnt) as resCnt "
			,"from brand b "
			,"join product p on p.b_code = b.b_code "
			,"join sell s on s.p_code = p.p_code "
			,"<where> "
			,"sell_date = #{today} "
			,"</where> "
			,"group by b.b_code "
			,"</script>"
			})
	List<SellDTO> sellAdmin(String today);
	
	
	@Select({"<script>"
		,"select s.*, b.b_code, b.b_name, "
		,"sum(cnt*p.p_price) as resPrice, "
		,"sum(cnt) as resCnt "
		,"from brand b "
		,"join product p on p.b_code = b.b_code "
		,"join sell s on s.p_code = p.p_code "
		,"<where> "
		,"sell_date = #{today} "
		,"and s_code = #{log.sCode} "
		,"</where> "
		,"group by b.b_code "
		,"</script>"
	})
	List<SellDTO> sellStore(StoreDTO log, String today);
	
	@Select("select * from inandout "
			+ "order by reg_date "
			+ "desc limit 10")
	List<InandoutDTO> ioAdmin();
	
	@Select("select * from inandout "
			+ "where start = #{sCode} "
			+ "or arrival = #{sCode} "
			+ "order by reg_date "
			+ "desc limit 10")
	List<InandoutDTO> ioStore(StoreDTO log);
	
	
	@Select("select rt.*, s.s_name from rt "
			+ "join store as s on rt.s_code = s.s_code "
			+ "where request_date = #{today} "
			+ "order by r_no "
			+ "desc limit 10")
	List<StoreOrderDTO>soAdmin(String today);
	
	@Select("select rt.*, s.s_name from rt "
			+ "join store as s on rt.s_code = s.s_code "
			+ "where request_date = #{today} "
			+ "and rt.s_code = #{log.sCode} "
			+ "order by r_no "
			+ "desc limit 10")
	List<StoreOrderDTO>soStore(StoreDTO log, String today);
	
	
}
