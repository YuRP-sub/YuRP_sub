package yurp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import yurp.model.AsDTO;
import yurp.model.AsMapper;
import yurp.model.NoticeDTO;
import yurp.model.StoreDTO;
import yurp.model.TemplateData;

@Controller
@RequestMapping("/as")
public class AsController {

	@Resource
	AsMapper mapper;

	@ModelAttribute
	TemplateData templateData(TemplateData data, HttpServletRequest request) {

		String uri = request.getRequestURI();
		String service = uri.substring(uri.lastIndexOf("/") + 1);

		// System.out.println("temp-service :"+service);

		data.setCate("as");
		data.setService(service);
		System.out.println("templateData:" + data);

		return data;
	}

	/** as 목록 */
	@RequestMapping("{service}")
	String list(Model mm, AsDTO dto, TemplateData templateData) {
		templateData.setCate("as");

//		mm.addAttribute("sNames", mapper.sNames());	
//		mm.addAttribute("aList",mapper.list(dto));
//		mm.addAttribute("aList", mapper.listPname(dto));	// 검색기능

		System.out.println(templateData.getService());

		mm.addAttribute("sNames", mapper.sNames());
		mm.addAttribute("aList", mapper.list(dto));

		return "template";
	}

	@RequestMapping({ "insertReg","modify/modifyReg" }) /// deleteReg, modifyReg
	String insertReg(Model mm, AsDTO dto, TemplateData templateData) {
		
		switch (templateData.getService()) {
		case "insertReg":
			mapper.storeInsert(dto);
			templateData.setMsg("추가되었습니다");
			templateData.setGoUrl("list");
			break;
		case "modifyReg":
			mapper.storeModify(dto);
			templateData.setMsg("수정되었습니다");
			templateData.setGoUrl("/as/list");
			break;
		}

		return "inc/alert";
	}

	@RequestMapping("{service}/{no}")
	String serviceNo(Model mm, @PathVariable int no, AsDTO dto, TemplateData templateData, HttpServletRequest request) {
		templateData.setCate("as");
		System.out.println(templateData.getService());
		switch (templateData.getService()) {
			 
		case "modify":
			mm.addAttribute("dto",mapper.detail(no));
			return "template";

		case "deleteReg":
			mapper.delete(no);
			templateData.setMsg("삭제되었습니다.");
			templateData.setGoUrl("/as/list");
			
		case "detail":
			mm.addAttribute("dto", mapper.detail(no));
			StoreDTO loginInfo = (StoreDTO) request.getSession().getAttribute("loginStore");
			mm.addAttribute("login", loginInfo);
			return "template";

		}
		return "inc/alert";
	}

	/** as 접수처리 */
	@GetMapping("accept/{aNo}")
	String modifyForm(Model mm, AsDTO dto) {
		mm.addAttribute("dto", mapper.detail(dto.getANo()));
		return "as/accept";
	}
	
	@PostMapping("accept/{aNo}")
	String modifyReg(Model mm, AsDTO dto) {
		mm.addAttribute("dto", mapper.modify(dto));
		return "redirect:/as/detail/{aNo}";
	}
	
	// -------검색기능
	/** as 매장 검색 기능 */
	@RequestMapping("sCodeSearch")
	String sCodeSearch(Model mm, AsDTO dto) {
		mm.addAttribute("sCodeSearch", mapper.sCodeSearch(dto));
		return "as/sCodeSearch";
	}

	/** as 상품 검색 기능 */
	@RequestMapping("pCodeSearch")
	String pCodeSearch(Model mm, AsDTO dto) {
		mm.addAttribute("pCodeSearch", mapper.pCodeSearch(dto));
		return "as/pCodeSearch";
	}
	/** as 상세내역 */
//	@RequestMapping("detail/{aNo}")
//	String detail(Model mm, @PathVariable int aNo, TemplateData templateData, HttpServletRequest request) {
//		templateData.setCate("detail");
//		mm.addAttribute("dto", mapper.detail(aNo));
//
//		StoreDTO loginInfo = (StoreDTO) request.getSession().getAttribute("loginStore");
//		mm.addAttribute("login", loginInfo);
//
//		//return "as/detail";
//		return "template";
//	}

	// -----매장
	/** as 접수:매장 */
//	@GetMapping("store/insert")
//	String storeInsertFrom(Model mm, AsDTO dto) { // insert.html 열기
//		//System.out.println(mapper.asNumSelect());
//		// asNum 자동 반영
//		dto.setAsNum(mapper.asNumSelect(dto));
//		mm.addAttribute("dto", dto);
//		return "as/store/insert";
//	}	
//
//	@PostMapping("store/insert")
//	String storeInsert(Model mm, AsDTO dto) {
//		mm.addAttribute("dto", mapper.storeInsert(dto));
//		return "redirect:/as/list";
//	}

	/** as 접수내역 수정:매장 */
//	@GetMapping("modify/{aNo}")
//	String storeModifyForm(Model mm, AsDTO dto) {
//		mm.addAttribute("dto",mapper.detail(dto.getANo()));
//		return "as/modify";
//	}
//	
//	@PostMapping("modify/{aNo}")
//	String storeModifyReg(Model mm, AsDTO dto) {
//		mm.addAttribute("dto", mapper.storeModify(dto));
//		return "redirect:/as/detail/{aNo}";	//작성 후 상세보기로 이동
//	}

//	@RequestMapping("delete/{aNo}")
//	String storeDelete(Model mm, AsDTO dto) {
//		mm.addAttribute("dto", mapper.delete(dto));
//		return "redirect:/as/list";
//	}


}
