package yurp.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
import yurp.model.StoreOrderDTO;
import yurp.model.TemplateData;

@Controller
@RequestMapping("/brandOrder/brand")
public class BrandOrderController {

	@Resource
	BrandOrderMapper mapper;
	
	@Resource
	ProductMapper pmapper;
	
	@ModelAttribute
	TemplateData templateData(TemplateData data, HttpServletRequest request) {
		
		String uri = request.getRequestURI();
		String service = uri.substring(uri.lastIndexOf("/")+1);
		
		data.setCate("brandOrder");
		data.setService(service);
		System.out.println("templateData : " + data);
		
		return data;
	}
	
	@RequestMapping("{service}")
	String blist(Model mm, TemplateData templateData, BrandDTO dto) {
		templateData.setCate("brandOrder/brand");
		
		System.out.println(templateData.getService());
		
		switch(templateData.getService()) {
		case "list":
			mm.addAttribute("blist",mapper.blist());
			break;
//		case "{bCode}":
//			mm.addAttribute("bdetail",mapper.bdetail(dto.getBCode()));
//			break;
		}
		

		return "template";
	}
	
	
	
	
	
//	@RequestMapping("order/list")
//	String list(Model model,OrdersDTO dto) {
//		model.addAttribute("listData", mapper.list(dto));
//		model.addAttribute("blist", mapper.blist());
//		return "brandOrder/order/list";
//	}
//	
//	@GetMapping("order/request")
//	void request(Model model) {
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
//		model.addAttribute("blist",mapper.blist());
//		model.addAttribute("stat",st);
//	}
//	
//	@GetMapping("order/prodAdd")
//	void prodAdd(Model model, ProductDTO dto) {
//		model.addAttribute("blist",mapper.blist());
//		model.addAttribute("prod",pmapper.prodList(dto));
//		model.addAttribute("bCode",dto.getBCode());
//	}
//
//	
//	@RequestMapping("order/detail")
//	String detail(Model model, @RequestParam String oStat) {
//		model.addAttribute("detailData",mapper.detail(oStat));
//		return "brandOrder/order/detail";
//	}
//	
//	@PostMapping("insert")
//	String excel(HttpServletRequest request,ArrayDTO arr,@RequestParam int []reqCnt, OrdersDTO dto) {
//		
//		ServletContext servletContext = request.getServletContext();
////		Excel ex = new Excel(servletContext,pmapper.excelArr(arr.getArr()),reqCnt,dto.getOStat());
//		mapper.oinsert(dto);
//		mapper.detailInsert(arr.getOrdersArr());
//		return "redirect:/brandOrder/order/list";
//	}
//	
//	
	
	
	//브랜드 관리
//	@GetMapping("brand/list")
//	String blist(Model model) {
//		model.addAttribute("blist",mapper.blist());
//		return "brandOrder/brand/list";
//	}
	
	@PostMapping("insert")
	String insert(BrandDTO dto) {
		mapper.insert(dto);
		return "brandOrder/brand/list";
	}

//	@RequestMapping("brand/{bCode}")
//	@ResponseBody
//	BrandDTO bdetail(@PathVariable String bCode, Model model) {
//		System.out.println(mapper.bdetail(bCode));
//	    return mapper.bdetail(bCode);
//	}

	
	@PostMapping("modify/{bNo}")
	String modify(BrandDTO dto) {
		System.out.println(dto);
		mapper.modify(dto);
		return "brandOrder/brand/list";
	}
	
	@GetMapping("delete/{bNo}")
	String delete(BrandDTO dto) {
		mapper.delete(dto);
		return "redirect:/brandOrder/brand/list";
	}

	@GetMapping("detail")
	public @ResponseBody BrandDTO bDetail(@RequestParam("bCode") String bCode) {
		BrandDTO res = new BrandDTO();
		res = mapper.bdetail(bCode);
//		System.out.println(res);
		return res;
	}
	
	
	
}
