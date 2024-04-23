package yurp.model;

import java.util.Date;

import lombok.Data;

@Data
public class StoreOrderDTO {
	String rStat,outStore,inStore,proceseRes,sCode,pCode,bCode,pNum,color,pName,pSize,sName,start,end,bName;
	Integer statCnt ,statPrice, cnt, price, reqCnt,adminCnt,storeCnt,inCnt, liPrice;
	Date requestDate;

}
