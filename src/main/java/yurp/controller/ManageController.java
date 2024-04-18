package yurp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import yurp.model.StoreDTO;
import yurp.model.StoreMapper;
import yurp.model.TemplateData;

@Controller
@RequestMapping("/manage")
public class ManageController {

	@Resource
	StoreMapper mapper;
	
	@ModelAttribute
	TemplateData templateData(TemplateData data, HttpServletRequest request) {
		
		String uri = request.getRequestURI();
		String service = uri.substring(uri.lastIndexOf("/")+1);
		
		System.out.println("temp-service :"+service);
		
		data.setCate("manage");
		data.setService(service);
		System.out.println("templateData:"+data);
		
		return data;
	}
	
	@RequestMapping("{service}")
	String manageList(Model mm, StoreDTO dto, TemplateData templateData) {
		templateData.setCate("manage");

		mm.addAttribute("storeList",mapper.list(dto));

		return "template";
	}
	
	@RequestMapping("{service}/{no}")
	String serviceNo(Model mm, @PathVariable int no, TemplateData templateData) {
		templateData.setCate("manage");
		System.out.println("service no :"+templateData.getService());
		switch(templateData.getService()) {
		case "modify":			
			mm.addAttribute("dto",mapper.detail(no));
			return "template";
			
		case "delete":
			mapper.delete(no);
			templateData.setMsg("삭제되었습니다.");
			templateData.setGoUrl("/manage/list");
			
		}
		return "inc/alert";
	}
	

	@RequestMapping({"insertReg","modify/modifyReg"})
	String manageReg(Model mm, StoreDTO dto, TemplateData templateData) {
		switch(templateData.getService()) {
		case "insertReg":
			mapper.insert(dto);
			templateData.setMsg("등록되었습니다.");
			templateData.setGoUrl("list");
			break;
		case "modifyReg":
			System.out.println("modiregDTO : "+dto);
			System.out.println("modireg 왔");
			mapper.modify(dto);
			templateData.setMsg("수정되었습니다.");
			templateData.setGoUrl("/manage/list");
			break;
		}

		return "inc/alert";
	}
	
//	@GetMapping("delete/{sNo}")
//	String delete(@PathVariable int sNo) {
//		System.out.println("삭제할껴?");
//		int cnt = mapper.delete(sNo);
//		System.out.println("delete 실행 : "+cnt);
//		return "redirect:/manage";
//	}
//	
//	@GetMapping("modify/{sNo}")
//	String modifyForm(Model mm, @PathVariable int sNo) {
//		System.out.println("modifyForm 실행 : "+sNo);
//		mm.addAttribute("dto",mapper.detail(sNo));
//		return "manage/modify";
//	}
	
//	@PostMapping("modify/{sNo}")
//	String modifyReg(StoreDTO dto) {
//
//		int cnt = mapper.modify(dto);
//		System.out.println("---modifyReg"+cnt);
//		return "redirect:/manage";
//	}
	
}
