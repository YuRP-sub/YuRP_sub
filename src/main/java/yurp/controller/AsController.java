package yurp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import yurp.model.AsDTO;
import yurp.model.AsMapper;
import yurp.model.StoreDTO;

@Controller
@RequestMapping("/as")
public class AsController {

	@Resource
	AsMapper mapper;
	
	/**as 목록*/
	@RequestMapping("list")
	String list(Model mm, AsDTO dto, HttpServletRequest request) {
		mm.addAttribute("sNames", mapper.sNames());	
		mm.addAttribute("aList",mapper.list(dto));
		mm.addAttribute("aList", mapper.listPname(dto));	// 검색기능
		
		StoreDTO loginInfo = (StoreDTO)request.getSession().getAttribute("loginStore");
		mm.addAttribute("login", loginInfo);
		
		return "as/list";
	}
	
	/**as 상세내역*/
	@RequestMapping("detail/{aNo}")
	String detail(Model mm, @PathVariable int aNo, HttpServletRequest request) {
		mm.addAttribute("dto", mapper.detail(aNo));
		
		StoreDTO loginInfo = (StoreDTO)request.getSession().getAttribute("loginStore");
		mm.addAttribute("login", loginInfo);
		
		return "as/detail";
	}
	
	/**as 접수처리*/
	@GetMapping("modify/{aNo}")
	String modifyForm(Model mm, AsDTO dto) {
		mm.addAttribute("dto",mapper.detail(dto.getANo()));
		return "as/modify";
	}
	
	@PostMapping("modify/{aNo}")
	String modifyReg(Model mm, AsDTO dto) {
		mm.addAttribute("dto", mapper.modify(dto));
		return "redirect:/as/detail/{aNo}";	
	}
	
	
	//-----매장
	/**as 접수:매장*/
	@GetMapping("store/insert")
	String storeInsertFrom(Model mm, AsDTO dto) { // insert.html 열기
		//System.out.println(mapper.asNumSelect());
		// asNum 자동 반영
		dto.setAsNum(mapper.asNumSelect(dto));
		mm.addAttribute("dto", dto);
		return "as/store/insert";
	}	

	@PostMapping("store/insert")
	String storeInsert(Model mm, AsDTO dto) {
		mm.addAttribute("dto", mapper.storeInsert(dto));
		return "redirect:/as/list";
	}
	
	/**as 접수내역 수정:매장*/
	@GetMapping("store/modify/{aNo}")
	String storeModifyForm(Model mm, AsDTO dto) {
		mm.addAttribute("dto",mapper.detail(dto.getANo()));
		return "as/store/modify";
	}
	
	@PostMapping("store/modify/{aNo}")
	String storeModifyReg(Model mm, AsDTO dto) {
		mm.addAttribute("dto", mapper.storeModify(dto));
		return "redirect:/as/detail/{aNo}";	//작성 후 상세보기로 이동
	}
	
	
	@RequestMapping("store/delete/{aNo}")
	String storeDelete(Model mm, AsDTO dto) {
		mm.addAttribute("dto", mapper.delete(dto));
		return "redirect:/as/list";
	}
	
	
	//-------검색기능
	/**as 매장 검색 기능*/
	@RequestMapping("store/sCodeSearch")
	String sCodeSearch(Model mm, AsDTO dto) {
		mm.addAttribute("sCodeSearch", mapper.sCodeSearch(dto));
		return "as/store/sCodeSearch";
	}
	/**as 상품 검색 기능*/
	@RequestMapping("store/pCodeSearch")
	String pCodeSearch(Model mm, AsDTO dto) {
		mm.addAttribute("pCodeSearch", mapper.pCodeSearch(dto));
		return "as/store/pCodeSearch";
	}
}
