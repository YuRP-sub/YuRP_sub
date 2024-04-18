package yurp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.annotation.Resource;
import yurp.model.InandoutDTO;
import yurp.model.InandoutMapper;
import yurp.model.TemplateData;

@Controller
@RequestMapping("/stock/inandout")
public class InandoutController {
	
	@Resource
	InandoutMapper inandoutmapper;

	@RequestMapping("{service}")
	String sellList(Model mm, InandoutDTO dto,TemplateData templateData) {
		templateData.setCate("stock/inandout");
		
		mm.addAttribute("inandoutData",inandoutmapper.list(dto));
		mm.addAttribute("totData",inandoutmapper.tot(dto));
		return "template";
	}
	
	@RequestMapping("detail")
	String viewDetail(Model mm, InandoutDTO dto) {
		mm.addAttribute("viewDetail", inandoutmapper.viewDetail(dto));
		return "stock/inandout/detail";
	}
	
}
