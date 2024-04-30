package yurp.model;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface DashboardMapper {

	@Select("select * from notice "
			+ "order by n_no "
			+ "desc limit 10;")
	List<NoticeDTO> notice();
	

	@Select("select * from ascenter "
			+ "where reg_date = #{today} "
			+ "limit 10")
	List<AsDTO> as(String today);
}
