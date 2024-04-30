package yurp.model;

import java.util.LinkedHashMap;

import lombok.Data;

@Data
public class InvenDTO {
	int iNo, movCnt, cnt, liPrice, pPrice;
	String pName, sName, season, color, pSize, pm, plus, pCode, sCode;
	LinkedHashMap<String, Integer> ppSum;
}





