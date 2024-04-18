package yurp.model;

import lombok.Data;

@Data
public class StoreDTO {
	Integer sNo, grade;
	String sCode, sPw, sName, addr, tel, email, manager, auth;
	

}
