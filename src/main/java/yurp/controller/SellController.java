package yurp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import yurp.model.SellDTO;
import yurp.model.SellMapper;
import yurp.model.TemplateData;

@Controller
@RequestMapping("/stock/sales")
public class SellController {
	
	@Resource
	SellMapper sellmapper;


	@RequestMapping("{service}")  ///list, inserForm
	String service(Model mm,SellDTO dto, TemplateData templateData) {
		
		templateData.setCate("stock/sales");
		

		mm.addAttribute("sellData",sellmapper.list(dto));
		mm.addAttribute("totData",sellmapper.tot(dto));
		
		
		return "template";
	}
	
	
	@RequestMapping("sNameSearch")
	String sNameSearch(Model mm, SellDTO dto) {
		mm.addAttribute("sNameSearch", sellmapper.sNameSearch(dto));
		return "stock/sales/sNameSearch";
	}
	
	@RequestMapping("pNumSearch")
	String pNumSearch(Model mm, SellDTO dto) {
		mm.addAttribute("mainData", sellmapper.pNumSearch(dto));
		return "stock/sales/pNumSearch";
	}
	
	
	
/*
	@RequestMapping({"insertReg","deleteReg","modifyReg"})  ///deleteReg, modifyReg 
	String insertReg(Model mm, SellDTO dto, TemplateData templateData) {
		
		switch(templateData.getService()) {
		case "insertReg":
			//sellmapper.insert(dto);
			templateData.setMsg("추가되었습니다");
			templateData.setGoUrl("list");
			break;
		case "deleteReg":
			//sellmapper.delete(dto);
			templateData.setMsg("추가되었습니다");
			templateData.setGoUrl("detail/???");
			break;
		case "modifyReg":
			//sellmapper.modify(dto);
			templateData.setMsg("추가되었습니다");
			templateData.setGoUrl("list");
			break;
		
		}
		
		return "inc/alert";
	}
	
	
	@RequestMapping("{service}/{no}")  /// detail, deleteForm, modifyForm
	String serviceNo(Model mm, SellDTO dto, TemplateData templateData) {

		templateData.setDir("stock/sales");

		mm.addAttribute("sellData",sellmapper.list(dto));
		mm.addAttribute("totData",sellmapper.tot(dto));
		
		return "stock/template";
	}
	
 */

	
	
	
}
