package yurp.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import yurp.model.DashboardMapper;
import yurp.model.StoreDTO;
import yurp.model.StoreMapper;
import yurp.model.TemplateData;

@Controller
public class IndexController {
	
	@Resource
	StoreMapper sMapper;
	
	@Resource
	DashboardMapper mapper;
	
	@RequestMapping({"/","index"})
	String index(HttpServletRequest request, TemplateData templateData) {
		String url="index";
		
		HttpSession session = request.getSession();
		
		if(session.getAttribute("loginStore") != null) {
			StoreDTO logDTO = (StoreDTO)session.getAttribute("loginStore");
			String logID = logDTO.getAuth();
			templateData.setCate("main");
			templateData.setService("dashboard");
			
			
			
			return "template";
		}

		return url;
	}
	
	@RequestMapping("dashboard")
	String login(HttpServletRequest request, StoreDTO dto, TemplateData templateData, Model mm) {
		
		StoreDTO data =sMapper.login(dto); 
		Calendar today = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		if(data==null) {
			return "index";
		}else {
			templateData.setCate("main");
			templateData.setService("dashboard");
			HttpSession session = request.getSession();
			session.setAttribute("loginStore", data);
			System.out.println("대시보드 누구니:"+data.getAuth());
			mm.addAttribute(mapper.notice());
			if(data.getAuth().equals("관리자")) {
				
				
			}else if(data.getAuth().equals("AS")) {
				
			}else {
				
			}
			
			
			return "template";
		}		
	}
	
	@GetMapping("logout")
	String logout(HttpSession session) {
		session.invalidate();
		return "index";
	}
}
