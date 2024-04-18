package yurp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import yurp.model.StoreDTO;
import yurp.model.StoreMapper;
import yurp.model.TemplateData;

@Controller
public class IndexController {
	
	@Resource
	StoreMapper sMapper;
	
	@RequestMapping({"/","index"})
	String index(HttpServletRequest request, TemplateData templateData) {
		String url="index";
		
		HttpSession session = request.getSession();
		
		if(session.getAttribute("loginStore") != null) {
			templateData.setCate("main");
			templateData.setService("dashboard");
			return "template";
		}

		return url;
	}
	
	@RequestMapping("{service}")
	String login(HttpServletRequest request, StoreDTO dto, TemplateData templateData) {
		
		StoreDTO data =sMapper.login(dto); 
		
		if(data==null) {
			return "index";
		}else {
			templateData.setCate("main");
			HttpSession session = request.getSession();
			System.out.println(data);
			session.setAttribute("loginStore", data);
			return "template";
		}		
	}
	
	@GetMapping("logout")
	String logout(HttpSession session) {
		session.invalidate();
		return "index";
	}
}
