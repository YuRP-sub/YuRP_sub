package yurp.controller;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.TreeMap;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.annotation.Resource;
import yurp.model.InvenDTO;
import yurp.model.InvenMapper;
import yurp.model.InvenSCntDTO;
import yurp.model.TemplateData;

@Controller
@RequestMapping("/stock/inven")
public class InvenController {
	
	@Resource
	InvenMapper invenMapper;

//	@RequestMapping("{service}")
//	String invenMapp(Model mm, InvenDTO dto,TemplateData templateData) {
//		templateData.setCate("stock/inven");
//		
//		mm.addAttribute("invenData",invenMapper.list(dto));
//		return "template";
//	}
	
	@RequestMapping("{service}")
	String invenList(Model mm, InvenDTO dto,TemplateData templateData) {
		templateData.setCate("stock/inven");
		
		ArrayList<String> p_List = invenMapper.invenCode("p_code");
		
		ArrayList<String> p_search = invenMapper.pSearch(dto);
		//System.out.println("pSearch: " + p_search);
		
		p_List.retainAll(p_search); 
		System.out.println("p_List: " + p_List);
		
		TreeMap<String, InvenDTO> resMap = new TreeMap<>();
		for (String pp : p_List) {
			InvenDTO rto = invenMapper.productDetail(pp);
			
			rto.setPpSum(new LinkedHashMap<String, Integer>());
			resMap.put(pp, rto);
		}
		
		
		
		ArrayList<String> s_List = invenMapper.invenCode("s_code");
		
		//s_List.retainAll(p_search); 
		
		for (String ttt : s_List) {
			ArrayList<InvenSCntDTO> scList = invenMapper.invenS_Cnt(ttt);
			//System.out.println(ttt + " :"+scList);
			
			for (InvenSCntDTO scDto : scList) {
				
				if(resMap.get(scDto.getPCode())!=null) {
				resMap.get(scDto.getPCode()).getPpSum().put(scDto.getSCode(), scDto.getSCnt());
				}
			}
		}
		
		System.out.println("resMap:"+resMap);
		
		
		
		mm.addAttribute("stData",invenMapper.stList(dto));
		mm.addAttribute("invenData",resMap);
		
		return "template";
		//return "stock/inven/list";
	}
	
	
}
