package yurp.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.annotation.Resource;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import yurp.model.ArrayDTO;
import yurp.model.DTOs;
import yurp.model.InandoutDTO;
import yurp.model.InandoutMapper;
import yurp.model.OrdersDTO;
import yurp.model.ProductDTO;
import yurp.model.ProductMapper;
import yurp.model.StoreDTO;
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
	
	@Resource
	InandoutMapper inandoutmapper;
	
	
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
//			String st = "";
//			if(mapper.maxStat() != null) {
//				String [] arr =  mapper.maxStat().split("-");
//				int stat = Integer.parseInt(arr[1])+1;
//				if(stat> 9){
//					st = "00"+stat;
//				}else{
//					st = "000"+stat;
//				}
//			}else {
//				st = "0001";
//			}
//			
//			mm.addAttribute("stat",st);
			mm.addAttribute("slist",mapper.slist());
			mm.addAttribute("plist",mapper.plist());
			break;
		}
		
		return "template";
	}
	
	
	
	@RequestMapping("detail")
	String detail(Model model, @RequestParam String rStat) {
		System.out.println(rStat);

		model.addAttribute("soInfo", mapper.getSO(rStat));
		model.addAttribute("detailData",mapper.detail(rStat));
		return "storeOrder/detail";
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
		mm.addAttribute("blist",pmapper.brandList());

		if(pCode!="" && pCode!=null || bCode!=null) {			
			mm.addAttribute("products",mapper.storeProdList(param));
		}	
	}
	
	

	
	@GetMapping("addSO")
	public @ResponseBody HashMap<String, ProductDTO> add(
			@RequestParam("key") String arr, 
			@RequestParam("start") String start, 
			@RequestParam("arrival") String arrival
			) {
		System.out.println(arrival);
		HashMap<String, ProductDTO> res = new HashMap<>();
		List<ProductDTO> dataArr = mapper.add(arr.split(","),arrival);
		List<ProductDTO> adminArr = mapper.add(arr.split(","),start);
		for (ProductDTO rr : dataArr) {
			for (ProductDTO ad : adminArr) {
				if(rr.getPCode().equals(ad.getPCode())) {
					rr.setAdminCnt(ad.getCnt());
				}
			}
			res.put(rr.getPCode(), rr);
		}
		
		System.out.println(res);
		return res;
	}
	
	@PostMapping("insert")
	String excel(HttpServletRequest request,StoreOrderDTO dto,DTOs dtos) {
		System.out.println(dto);

		System.out.println(dtos.getRtArr());
		mapper.insert(dto);
		mapper.detailInsert(dtos.getRtArr(),dto.getRStat(),dto.getSName());
		return "redirect:/storeOrder/list";
	}
	
	
	
	@RequestMapping({"insertReg"})
	String ioReg(Model mm, DTOs dtos, StoreOrderDTO dto, TemplateData templateData) {
		System.out.println("rtDto : "+dto);
		System.out.println("rtDTOs : "+dtos.getRtArr());
		switch(templateData.getService()) {
		case "insertReg":
			mapper.insertSO(dto);
			mapper.insertSODetail(dtos.getRtArr());
			templateData.setMsg("발주요청이 등록되었습니다.");
			templateData.setGoUrl("list");
		}
		return "inc/alert";
	}
}
