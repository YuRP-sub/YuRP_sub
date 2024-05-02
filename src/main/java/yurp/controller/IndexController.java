package yurp.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import yurp.model.DashboardMapper;
import yurp.model.InandoutMapper;
import yurp.model.ProductMapper;
import yurp.model.StoreDTO;
import yurp.model.StoreMapper;
import yurp.model.TemplateData;

@Controller
public class IndexController {
	@Resource
	DashboardMapper mapper;
	
	@Resource
	StoreMapper sMapper;
	
	@Resource
	ProductMapper pMapper;
	
	@Resource
	InandoutMapper ioMapper;
	
	@RequestMapping({"/","index"})
	String index(HttpServletRequest request, StoreDTO dto, TemplateData templateData, Model mm) {
		HttpSession session = request.getSession();
		String url="index";
		Date today = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		StoreDTO data =sMapper.login(dto); 
		if(data != null) {
			session.setAttribute("loginStore", data);			
		}
		
		
		
		System.out.println("로그인 했다 : "+data+"/ 세션이라 : "+session.getAttribute("loginStore"));
		
		if(data != null || session.getAttribute("loginStore") != null) {
			StoreDTO logDTO = (StoreDTO)session.getAttribute("loginStore");
			
			templateData.setCate("main");
			templateData.setService("dashboard");	
			
			mm.addAttribute("notice",mapper.notice());
			mm.addAttribute("storeList",ioMapper.storeList());
			mm.addAttribute("brandList",pMapper.brandList());
			
			switch(logDTO.getAuth()) {
			case "관리자":
				mm.addAttribute("sellToday",mapper.sellAdmin(sdf.format(today)));
				mm.addAttribute("io",mapper.ioAdmin());
				mm.addAttribute("so",mapper.soAdmin(sdf.format(today)));
				break;
			case "매장":
				mm.addAttribute("sellToday",mapper.sellStore(logDTO, sdf.format(today)));
				mm.addAttribute("io",mapper.ioStore(logDTO));
				mm.addAttribute("so",mapper.soStore(logDTO,sdf.format(today)));
				break;
			case "AS":
				mm.addAttribute("asToday",mapper.as(sdf.format(today)));
				break;
			}			
			
			return "template";
		}

		return url;
	}
	
//	@RequestMapping("dashboard")
//	String login(HttpServletRequest request, TemplateData templateData, Model mm) {
//		
//		
//		Date today = new Date();
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//		
//		if(data==null) {
//			return "index";
//		}else {
//			templateData.setCate("main");
//			templateData.setService("dashboard");
//			HttpSession session = request.getSession();
//			session.setAttribute("loginStore", data);
//			System.out.println("대시보드 누구니:"+mapper.soStore(data, sdf.format(today)));
//			mm.addAttribute("noticeRecent",mapper.notice());
//			
//			switch(data.getAuth()) {
//			case "관리자":
//				mm.addAttribute("sellToday",mapper.sellAdmin(sdf.format(today)));
//				mm.addAttribute("ioAdmin",mapper.ioAdmin());
//				mm.addAttribute("soAdmin",mapper.soAdmin(sdf.format(today)));
//				break;
//			case "매장":
//				mm.addAttribute("sellToday",mapper.sellStore(data, sdf.format(today)));
//				mm.addAttribute("ioStore",mapper.ioStore(data));
//				mm.addAttribute("soStore",mapper.soStore(data,sdf.format(today)));
//				break;
//			case "AS":
//				mm.addAttribute("asToday",mapper.as(sdf.format(today)));
//				break;
//			}			
//			
//			return "template";
//		}		
//	}
	
	@GetMapping("logout")
	String logout(HttpSession session) {
		session.invalidate();
		return "index";
	}
}
