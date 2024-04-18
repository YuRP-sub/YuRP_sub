package yurp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import yurp.model.TemplateData;

@Controller
@RequestMapping("/{cate}")
public class TemplateController {
	
//	@ModelAttribute
//	TemplateData templateData(TemplateData data) {
//		
//		System.out.println("fbbdf:"+data);
//		return data;
//	}
//
//	@RequestMapping(value = {
//			"/",
//			"{service}", 
//			"/{service}/{no}", 
//			"/{service}/{code}", 
//			"{subCate}/{service}", 
//			"{subCate}/{service}/{no}", 
//			"{subCate}/{service}/{code}"})
//	String viewTotal() {
//		System.out.println("viewTotal");
//		return "template";
//	}
}
