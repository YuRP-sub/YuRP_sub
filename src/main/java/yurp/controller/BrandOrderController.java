package yurp.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.annotation.Resource;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import yurp.model.ArrayDTO;
import yurp.model.BrandDTO;
import yurp.model.OrdersDTO;
import yurp.model.BrandOrderMapper;
import yurp.model.DTOs;
import yurp.model.Excel;
import yurp.model.ProductDTO;
import yurp.model.ProductMapper;

@Controller
@RequestMapping("/brandOrder")
public class BrandOrderController {

	@Resource
	BrandOrderMapper mapper;
	
	@Resource
	ProductMapper pmapper;
	
	@RequestMapping("order/list")
	String list(Model model,OrdersDTO dto) {
		model.addAttribute("listData", mapper.list(dto));
		model.addAttribute("blist", mapper.blist());
		return "brandOrder/order/list";
	}
	
	@GetMapping("order/request")
	void request(Model model) {
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
		model.addAttribute("blist",mapper.blist());
		model.addAttribute("stat",st);
	}
	
	@GetMapping("order/prodAdd")
	void prodAdd(Model model, ProductDTO dto) {
		model.addAttribute("blist",mapper.blist());
		model.addAttribute("prod",pmapper.prodList(dto));
		model.addAttribute("bCode",dto.getBCode());
	}

	
	@RequestMapping("order/detail")
	String detail(Model model, @RequestParam String oStat) {
		model.addAttribute("detailData",mapper.detail(oStat));
		return "brandOrder/order/detail";
	}
	
	@PostMapping("insert")
	String excel(HttpServletRequest request,ArrayDTO arr,@RequestParam int []reqCnt, OrdersDTO dto) {
		
		ServletContext servletContext = request.getServletContext();
//		Excel ex = new Excel(servletContext,pmapper.excelArr(arr.getArr()),reqCnt,dto.getOStat());
		mapper.oinsert(dto);
		mapper.detailInsert(arr.getOrdersArr());
		return "redirect:/brandOrder/order/list";
	}
	
	
	
	
	//브랜드 관리
	@GetMapping("brand/list")
	String blist(Model model) {
		model.addAttribute("blist",mapper.blist());
		return "brandOrder/brand/list";
	}
	
	@PostMapping("brand/list")
	void insert(BrandDTO dto) {
		mapper.insert(dto);
	}
	
	@RequestMapping("brand/{bName}")
	@ResponseBody
	BrandDTO bdetail(@PathVariable String bName, Model model) {
	    return mapper.bdetail(bName);
	}

	
	@PostMapping("brand/{bNo}")
	String modify(BrandDTO dto) {
		mapper.modify(dto);
		return "brandOrder/brand/list";
	}
	
	@GetMapping("brand/delete/{bNo}")
	String delete(BrandDTO dto) {
		mapper.delete(dto);
		return "redirect:/brandOrder/brand/list";
	}

	
	
	
}
