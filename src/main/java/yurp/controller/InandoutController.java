package yurp.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import yurp.model.DTOs;
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
	
	@ModelAttribute
	TemplateData templateData(TemplateData data, HttpServletRequest request) {
		
		String uri = request.getRequestURI();
		String service = uri.substring(uri.lastIndexOf("/")+1);
		
		//System.out.println("temp-service :"+service);
		
		data.setCate("stock");
		data.setSubCate("inandout");
		data.setService(service);
		
		return data;
	}

	@RequestMapping("{service}")
	String sellList(Model mm, InandoutDTO dto,TemplateData templateData, HttpServletRequest request) {
		templateData.setCate("stock/inandout");

		mm.addAttribute("storeList",inandoutmapper.storeList());
		
		if(templateData.getService() == "list") {
			mm.addAttribute("inandoutData",inandoutmapper.list(dto));
			mm.addAttribute("totData",inandoutmapper.tot(dto));			
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
		param.put("bCode", bCode);
		param.put("pCode", pCode);

		System.out.println("pcode: "+pCode);
		mm.addAttribute("blist",productmapper.brandList());

		if(pCode!="" && pCode!=null || bCode!=null) {			
			mm.addAttribute("products",inandoutmapper.storeProdList(param));
		}	
	}
	
	
	@GetMapping("add")
	public @ResponseBody HashMap<String, ProductDTO> add(@RequestParam("key") String arr, @RequestParam("start") String start) {
		HashMap<String, ProductDTO> res = new HashMap<>();
		List<ProductDTO> dataArr = inandoutmapper.add(arr.split(","),start);
		for (ProductDTO rr : dataArr) {
			res.put(rr.getPCode(), rr);
		}
		return res;
	}
	
	
	@RequestMapping({"insertReg"})
	String ioReg(Model mm, DTOs dtos, InandoutDTO dto, TemplateData templateData) {
		switch(templateData.getService()) {
		case "insertReg":
			inandoutmapper.insertIo(dto);
			inandoutmapper.insertIoDetail(dtos.getIoArr());
			inandoutmapper.calcInven(dtos.getIoArr());
			templateData.setMsg("출고전표가 등록되었습니다.");
			templateData.setGoUrl("list");
		}
		return "inc/alert";
	}
}
