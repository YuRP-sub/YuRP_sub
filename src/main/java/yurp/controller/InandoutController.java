package yurp.controller;

import java.util.HashMap;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import yurp.model.InandoutDTO;
import yurp.model.InandoutMapper;
import yurp.model.ProductDTO;
import yurp.model.ProductMapper;
import yurp.model.StoreDTO;
import yurp.model.TemplateData;

@Controller
@RequestMapping("/stock/inandout")
public class InandoutController {
	
	@Resource
	InandoutMapper inandoutmapper;
	
	@Resource
	ProductMapper productmapper;

	@RequestMapping("{service}")
	String sellList(Model mm, InandoutDTO dto,TemplateData templateData, HttpServletRequest request) {
		templateData.setCate("stock/inandout");
		
		mm.addAttribute("inandoutData",inandoutmapper.list(dto));
		mm.addAttribute("totData",inandoutmapper.tot(dto));
		mm.addAttribute("storeList",inandoutmapper.storeList());
		
		if(templateData.getService() == "prodAdd") {
			
		}
		return "template";
	}
	
	@RequestMapping("detail")
	String viewDetail(Model mm, InandoutDTO dto) {
		mm.addAttribute("viewDetail", inandoutmapper.viewDetail(dto));
		return "stock/inandout/detail";
	}
	
	@GetMapping("prodAdd")
	void prodAdd(Model mm, HttpServletRequest request) {
		HashMap<String,String> param = new HashMap<>();
		String bCode = request.getParameter("bCode");
		String pCode = request.getParameter("pCode");
		param.put("start", request.getParameter("start"));
		param.put("arrival", request.getParameter("arrival"));
		param.put("bCode", bCode);
		param.put("pCode", pCode);

		System.out.println("pcode: "+pCode);
		mm.addAttribute("blist",productmapper.brandList());

		if(pCode!="" && pCode!=null || bCode!=null) {			
			mm.addAttribute("products",inandoutmapper.storeProdList(param));
		}
		
	}
}
