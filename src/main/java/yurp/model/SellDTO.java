package yurp.model;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class SellDTO {
	
	int sellNo, liPrice, discount, pPrice, cnt, totPrice,allTot,sNo,

	grade,pNo, resPrice, resCnt;
	String start,end,pCode, pNum, sCode, sName, addr, 
	color, pSize, season, pName, sellDate, manager, bCode, bName;

	
}





