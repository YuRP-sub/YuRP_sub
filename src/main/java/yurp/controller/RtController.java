package yurp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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

@Controller
@RequestMapping("/stock/rt")
public class RtController {

	@Resource
	StoreOrderMapper mapper;
	
	@Resource
	ProductMapper pmapper;
	
	@GetMapping("list")
	String list(Model model,StoreOrderDTO dto) {
		model.addAttribute("slist",mapper.slist());
		model.addAttribute("listData",mapper.rtlist(dto));
		return "stock/rt/list";
	}
	
	@RequestMapping("detail")
	String detail(Model model, @RequestParam String rStat) {
		model.addAttribute("detailData",mapper.rtdetail(rStat));
		return "stock/rt/detail";
	}
	
	@RequestMapping("request")
	String request(Model model) {
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
		
		model.addAttribute("stat",st);
		model.addAttribute("slist",mapper.slist());
		return "stock/rt/request";
	}
//	
	@GetMapping("prodAdd")
	void prodAdd(Model model, ProductDTO dto) {
		model.addAttribute("blist",mapper.blist());
		model.addAttribute("prod",pmapper.storeProdList(dto));
		model.addAttribute("sName",dto.getSName());
	}
	
	@PostMapping("insert")
	String excel(HttpServletRequest request,StoreOrderDTO dto,DTOs dtos) {
		mapper.insert(dto);
		mapper.detailInsert(dtos.getRtArr(),dto.getRStat(),dto.getSName());
		return "redirect:/stock/rt/list";
	}
}
