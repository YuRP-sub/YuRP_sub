package yurp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import yurp.model.TemplateData;

@Controller
public class TemplateController {

	
	@RequestMapping("over")
	String over(Model mm, TemplateData templateData) {
		
		templateData.setMsg("세션 만료");
		templateData.setGoUrl("/index");
		
		return "inc/alert";
	}
	
	@RequestMapping("noauth")
	String noauth(Model mm, TemplateData templateData) {
		
		templateData.setMsg("접근권한이 없습니다");
		templateData.setGoUrl("/index");
		
		return "inc/alert";
	}
}
