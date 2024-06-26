package yurp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
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
			mm.addAttribute("storeDTO",mapper.detail(no));
			return "template";
			
		case "delete":
			mapper.delete(no);
			templateData.setMsg("삭제되었습니다.");
			templateData.setGoUrl("/manage/list");
			
		}
		return "inc/alert";
	}
	
	@RequestMapping("insert")
	String joinForm(StoreDTO dto,TemplateData templateData) {
		System.out.println("insert 실행");
		return "template";
	}

	@PostMapping("insertReg")
	String manageInsertReg( @Valid StoreDTO dto,BindingResult res,TemplateData templateData) {
		
		if(res.hasErrors()) {
			System.out.println("insertReg에러-------------");
			//return "/manage/insert";
			templateData.setCate("manage");
			templateData.setService("insert");
			return "template";
		}else {
			mapper.insert(dto);
			templateData.setMsg("등록되었습니다.");
			templateData.setGoUrl("list");
			return "inc/alert";
		}
		
	}
	
	@RequestMapping("modify/modifyReg")
	String managemodifyReg(@Valid StoreDTO dto, BindingResult res, TemplateData templateData) {
		
		if(res.hasErrors()) {
			System.out.println("modifyReg에러-------------" +dto);
			templateData.setCate("manage");
			templateData.setService("modify");
			//mm.addAttribute("dto",dto);
			return "template";
		}else {
			mapper.modify(dto);
			templateData.setMsg("수정되었습니다.");
			templateData.setGoUrl("/manage/list");
			
			return "inc/alert";
		}
	}
	
	@PostMapping("chkSCode")
	@ResponseBody
	public int chkSCode(@RequestParam("sCode") String sCode) {
		int cnt = mapper.chkSCode(sCode);
		System.out.println(sCode + " : "+ cnt);
		return cnt;
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
