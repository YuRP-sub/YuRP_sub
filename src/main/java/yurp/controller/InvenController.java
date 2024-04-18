package yurp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.annotation.Resource;
import yurp.model.InvenDTO;
import yurp.model.InvenMapper;
import yurp.model.TemplateData;

@Controller
@RequestMapping("/stock/inven")
public class InvenController {
	
	@Resource
	InvenMapper invenMapper;

	@RequestMapping("{service}")
	String invenList(Model mm, InvenDTO dto,TemplateData templateData) {
		templateData.setCate("stock/inven");
		
		mm.addAttribute("invenData",invenMapper.list(dto));
		return "template";
	}
	
	
}
