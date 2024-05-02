package yurp.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class StoreDTO {
	Integer sNo;
	Integer grade;
	
	@NotEmpty(message  = "매장명을 입력하세요")
	@Pattern(regexp = "^[ㄱ-ㅎ가-힣a-zA-Z0-9]*$", message  = "특수문자 입력 불가")
	String sName;
	
	@NotEmpty(message  = "매장코드를 입력하세요")
	@Pattern(regexp = "^[a-zA-Z0-9]*$", message  = "영어, 숫자조합")
	String sCode;
	
	@NotEmpty(message  = "비밀번호를 입력하세요")
	String  sPw;
	
	@NotEmpty(message  = "주소를 입력하세요")
	String addr;
	
	@NotEmpty(message  = "연락처를 입력하세요")
	@Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$",message = "연락처 형식이 잘못됐습니다")
	String tel;
	
	@NotEmpty(message  = "이메일을 입력하세요")
	@Pattern(regexp = "^[a-zA-Z0-9_!#$%&'\\*+/=?{|}~^.-]+@[a-zA-Z0-9.-]+$", message  = "이메일 형식이 잘못됐습니다")
	String email;
	
	@NotEmpty(message  = "담당자를 입력하세요")
	String manager;
	
	String auth;
	

}
