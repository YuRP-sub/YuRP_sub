package yurp.model;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;


@Mapper
public interface NoticeMapper {

//	@Select("select * from notice")
//	List<NoticeDTO> list(NoticeDTO dto);

//	@Select("select * from NOTICE order by n_no desc limit #{startPage}, #{endPage}")
//	List<NoticeDTO> list();
	
	@Select("select * from notice where n_no = #{no}")
	NoticeDTO detail(int no);
	
	@Insert("insert into notice (title, content, file) values (#{title},#{content},#{file})")
	int insert(NoticeDTO dto);
	
	@Update("update notice set "
			+ "title = #{title}, content = #{content}, file = #{file} "
			+ "where n_no = #{nNo}")
	int modify(NoticeDTO dto);

	@Delete("delete from notice where n_no = #{nNo}")
	int delete(NoticeDTO dto);
	
	/**제목, 내용 검색*/
	@Select({
		" <script> "
		," SELECT * from notice "
		, " <where> "
		, " 	<if test='title != null'> "
		, " 		AND title like concat('%', #{title}, '%')"
		, " 	</if> "
		, " 	<if test='content != null'> "
		, " 		AND content like concat('%', #{content}, '%')"
		, " 	</if> "
		, " </where> "
		, " </script> "})
	List<NoticeDTO> list(NoticeDTO dto);
}
