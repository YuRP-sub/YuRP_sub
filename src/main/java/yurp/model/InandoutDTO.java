package yurp.model;

import lombok.Data;

@Data
public class InandoutDTO {
	int ioNo, totPrice, totCnt,cnt,price,pPrice,discount,liPrice, resCnt;
	String ioStat, type, start, arrival, sCode, bCode, pCode,
	process, eday,sday,regDate,pName,pNum,color,pSize,season,
	bName,sName,grade,search;
	
	int sellNo,allTot,sNo,pNo;
	String end, addr, sellDate, manager;
	
}





