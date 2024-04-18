package yurp.model;

import java.util.Date;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class NoticeDTO {
	
	// 공지번호
	Integer nNo;
	//		제목, 	내용,	   파일이름
	String title, content, file;
	//파일
	MultipartFile [] upFile;
	//	 등록 날짜
	Date regDate;
	
}
