package yurp.model;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class SellDTO {
	
	int sellNo, liPrice, discount, pPrice, cnt, totPrice,allTot,sNo,
	grade,pNo;
	String start,end,pCode, pNum, sCode, addr, sName,
	color, pSize, season, pName, sellDate, manager;
	
}





