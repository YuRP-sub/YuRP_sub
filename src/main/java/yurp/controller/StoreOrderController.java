package yurp.controller;

import java.util.ArrayList;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.annotation.Resource;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import yurp.model.ArrayDTO;
import yurp.model.DTOs;
import yurp.model.OrdersDTO;
import yurp.model.ProductDTO;
import yurp.model.ProductMapper;
import yurp.model.StoreOrderDTO;
import yurp.model.StoreOrderMapper;
import yurp.model.TemplateData;

@Controller
@RequestMapping("/storeOrder")
public class StoreOrderController {

	@Resource
	StoreOrderMapper mapper;
	
	@Resource
	ProductMapper pmapper;
	
	
	@ModelAttribute
	TemplateData templateData(TemplateData data, HttpServletRequest request) {
		
		String uri = request.getRequestURI();
		String service = uri.substring(uri.lastIndexOf("/")+1);
		
		data.setCate("storeOrder");
		data.setService(service);
		System.out.println("templateData : " + data);
		
		return data;
	}
	
	@RequestMapping("{service}")
	String list(Model mm, StoreOrderDTO dto, TemplateData templateData, ProductDTO pdto ) {
		templateData.setCate("storeOrder");
		
		System.out.println(templateData.getService());
		switch(templateData.getService()) {
		case "list":
			mm.addAttribute("listData",mapper.list(dto));
			break;			
		case "request":
			String st = "";
			if(mapper.maxStat() != null) {
				String [] arr =  mapper.maxStat().split("-");
				int stat = Integer.parseInt(arr[1])+1;
				if(stat> 9){
					st = "00"+stat;
				}else{
					st = "000"+stat;
				}
			}else {
				st = "0001";
			}
			
			mm.addAttribute("stat",st);
			mm.addAttribute("slist",mapper.slist());
			break;
			
		case "prodAdd":
			mm.addAttribute("blist",mapper.blist());
			mm.addAttribute("prod",pmapper.storeOrderS(pdto));
			break;	
		}
		
		return "template";
	}
	
	
	
	@RequestMapping("detail")
	String detail(Model model, @RequestParam String rStat) {
		System.out.println(rStat);

		model.addAttribute("detailData",mapper.detail(rStat));
		return "storeOrder/detail";
	}
//	
//	@RequestMapping("request")
//	String request(Model model) {
//		String st = "";
//		if(mapper.maxStat() != null) {
//			String [] arr =  mapper.maxStat().split("-");
//			int stat = Integer.parseInt(arr[1])+1;
//			if(stat> 9){
//				st = "00"+stat;
//			}else{
//				st = "000"+stat;
//			}
//		}else {
//			st = "0001";
//		}
//		
//		model.addAttribute("stat",st);
//		model.addAttribute("slist",mapper.slist());
//		return "storeOrder/request";
//	}
//	
//	@GetMapping("prodAdd")
//	void prodAdd(Model model, ProductDTO dto) {
//
//		model.addAttribute("blist",mapper.blist());
//		model.addAttribute("prod",pmapper.storeOrderS(dto));
//
//	}
	
	@PostMapping("insert")
	String excel(HttpServletRequest request,StoreOrderDTO dto,DTOs dtos) {
		System.out.println(dto);

		System.out.println(dtos.getRtArr());
		mapper.insert(dto);
		mapper.detailInsert(dtos.getRtArr(),dto.getRStat(),dto.getSName());
		return "redirect:/storeOrder/list";
	}
}
