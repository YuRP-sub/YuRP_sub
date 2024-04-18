package yurp.model;

import java.util.Date;

import lombok.Data;

@Data
public class OrdersDTO {
	Integer oNo;
	String oStat, excel, bCode,pNum,pName,color,pSize,start,end;
	int totPrice, totCnt,reqCnt,cnt;
	Date regDate;
	String pCode = pNum + color + pSize;
}
