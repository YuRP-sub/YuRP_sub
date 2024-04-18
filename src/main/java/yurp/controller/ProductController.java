package yurp.controller;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import yurp.model.BrandDTO;
import yurp.model.DTOs;
import yurp.model.ProductDTO;
import yurp.model.ProductMapper;
import yurp.model.TemplateData;

@Controller
@RequestMapping("/stock/product")
public class ProductController {

	@Resource
	ProductMapper mapper;
	
	@ModelAttribute
	TemplateData templateData(TemplateData data, HttpServletRequest request) {
		
		String uri = request.getRequestURI();
		String service = uri.substring(uri.lastIndexOf("/")+1);
		
		//System.out.println("temp-service :"+service);
		
		data.setCate("stock");
		data.setSubCate("product");
		data.setService(service);
		System.out.println("templateData:"+data);
		
		return data;
	}

	@RequestMapping("{service}")	/// list, insert, modify
	String productList(Model mm, DTOs dtos, TemplateData templateData) {
		
		templateData.setCate("stock/product");
		
//		System.out.println("productList----"+dto);
		
//		HashMap<String, String> brandMap = new HashMap<>();
//		for (BrandDTO brand : mapper.brandList()) {
//			brandMap.put(brand.getBCode(),brand.getBName());
//		}
		switch(templateData.getService()) {
		case "list":
			mm.addAttribute("seasonData", mapper.seasonList());
			mm.addAttribute("brandData", mapper.brandList());
			mm.addAttribute("productData", mapper.list(dtos.getProdArr()));
			break;
			
		case "modify":
			mm.addAttribute("productData", mapper.modList(dtos.getProdArr()));
			mm.addAttribute("brandData", mapper.brandList());
			break;
		}

		return "template";
	}


	@RequestMapping({"insertReg","modifyReg","delete"})
	String productReg(Model mm, DTOs dtos, TemplateData templateData) {
		System.out.println(templateData.getService());
		switch(templateData.getService()) {
		case "insertReg":
			mapper.insert(dtos.getProdArr());
			templateData.setMsg("상품이 추가되었습니다.");
			templateData.setGoUrl("list");
			break;
			
		case "modifyReg":
			mapper.modReg(dtos.getProdArr());
			templateData.setMsg("상품이 수정되었습니다.");
			templateData.setGoUrl("list");
		case "delete":
			mapper.delete(dtos.getProdArr());
			templateData.setMsg("상품이 삭제되었습니다.");
			templateData.setGoUrl("list");
		}
		
		return "inc/alert";
	}

//	@PostMapping("delete")
//	String delete(DTOs dtos) {
////		System.out.println("delete 왔음 : " + dtos.getProdArr());
//		System.out.println("상품삭제reg 완료 : " + mapper.delete(dtos.getProdArr()));
//		return "";
//	}

//	@PostMapping("modify")
//	void productModifyForm(Model mm, DTOs dtos) {
////		System.out.println("수정form 왔음 mm : " + mm);
//		System.out.println("수정form 왔음 brands : " + mapper.brandList());
//
//		mm.addAttribute("productData", mapper.modList(dtos.getProdArr()));
//		mm.addAttribute("brandData", mapper.brandList());
//	}
	
//    @PostMapping("modify/reg") 
//    String productModifyReg(DTOs dtos) {
//// 	    System.out.println("상품수정reg 왔음 : "+dtos.getProdArr());
//	    System.out.println("상품수정reg 완료 : "+ mapper.modReg(dtos.getProdArr())); 
//	    return "redirect:/stock/product"; 
//    }
    
    
    
}
