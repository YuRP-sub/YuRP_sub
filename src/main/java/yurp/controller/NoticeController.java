package yurp.controller;

import java.io.FileOutputStream;

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
import yurp.model.NoticeDTO;
import yurp.model.NoticeMapper;
import yurp.model.StoreDTO;
import yurp.model.TemplateData;

@Controller
@RequestMapping("/notice")
public class NoticeController {

	@Resource
	NoticeMapper mapper;

	@ModelAttribute
	TemplateData templateData(TemplateData data, HttpServletRequest request) {
		
		String uri = request.getRequestURI();
		String service = uri.substring(uri.lastIndexOf("/")+1);
		
		//System.out.println("temp-service :"+service);
		
		data.setCate("notice");
		data.setService(service);
		System.out.println("templateData:"+data);
		
		return data;
	}
	
	//**공지사항 목록*/
	@RequestMapping("{service}")
	String list(Model mm, NoticeDTO dto, TemplateData templateData, HttpServletRequest request) {
		templateData.setCate("notice");
		
		StoreDTO loginInfo = (StoreDTO)request.getSession().getAttribute("loginStore");
		mm.addAttribute("login", loginInfo);
		
		switch(templateData.getService()) {
		case "list":
			mm.addAttribute("nList", mapper.list(dto));
			break;
		
		case  "modify":
			mm.addAttribute("dto",mapper.detail(dto.getNNo()));
			break;
		}
//		mm.addAttribute("nList", mapper.listPname(dto));
		return "template";
	}
	
	@RequestMapping("{service}/{no}")  /// detail, deleteForm, modifyForm
	String serviceNo(Model mm,@PathVariable int no, NoticeDTO dto, TemplateData templateData, HttpServletRequest request) {

		templateData.setCate("notice");

		StoreDTO loginInfo = (StoreDTO)request.getSession().getAttribute("loginStore");
		System.out.println(no);
		mm.addAttribute("login", loginInfo);                                           
		mm.addAttribute("dto",mapper.detail(no));
		
		return "template";
	}
	
	@RequestMapping({"insertReg"})  ///deleteReg, modifyReg 
	String insertReg(Model mm, NoticeDTO dto, MultipartHttpServletRequest mr, HttpServletRequest request, TemplateData templateData) {
		
		String file = "";
		for (MultipartFile mpf : mr.getFiles("upFile")) {
			file += mpf.getOriginalFilename();			
			fileUpload(mpf, request);
		}
		mm.addAttribute("file",file);
		dto.setFile(file); // 파일명을 DTO에 설정
		//mm.addAttribute("dto", mapper.insert(dto));
		mapper.insert(dto);
		templateData.setMsg("추가되었습니다");
		templateData.setGoUrl("list");
		
		
		return "inc/alert";
	}
	
	//**공지사항 상세보기*/
//	@RequestMapping("detail/{nNo}")
//	String detail(Model mm, @PathVariable int nNo, NoticeDTO dto, HttpServletRequest request) {
//		mm.addAttribute("dto", mapper.detail(nNo));
//
//		StoreDTO loginInfo = (StoreDTO)request.getSession().getAttribute("loginStore");
//		mm.addAttribute("login", loginInfo);
//		
//		return "notice/detail";
//	}

	//insert.html 열기
	//**공지사항 추가*/
//	@GetMapping("insert")
//	void insertFrom() {}
//	
//	@PostMapping("insert")
//	String insertReg(Model mm, NoticeDTO dto, MultipartHttpServletRequest mr, HttpServletRequest request) {
//	
//		String file = "";
//		for (MultipartFile mpf : mr.getFiles("upFile")) {
//			file += mpf.getOriginalFilename();			
//			fileUpload(mpf, request);
//		}
//		mm.addAttribute("file",file);
//		dto.setFile(file); // 파일명을 DTO에 설정
//		mm.addAttribute("dto", mapper.insert(dto));
//		return "redirect:/notice/list";
//	}
//	

	//**공지사항 수정*/
	@GetMapping("modify/{nNo}")
	String modifyForm(Model mm, NoticeDTO dto) {
		mm.addAttribute("dto",mapper.detail(dto.getNNo()));
		return "notice/modify";
	}
	
	@PostMapping("modify/{nNo}")
	String modifyReg(Model mm, NoticeDTO dto, MultipartHttpServletRequest mr, HttpServletRequest request) {
		
		String file = "";
		for (MultipartFile mpf : mr.getFiles("upFile")) {
			file += mpf.getOriginalFilename();			
			fileUpload(mpf, request);
		}
		mm.addAttribute("file",file);
		dto.setFile(file); // 파일명을 DTO에 설정
		mm.addAttribute("dto", mapper.modify(dto));
		return "redirect:/notice/list";		//작성 후 상세보기로 이동
	}
	
	//**공지사항 삭제*/
	@GetMapping("delete/{nNo}")
	String delete(Model mm, NoticeDTO dto) {
		mm.addAttribute("dto",  mapper.delete(dto));
		return "redirect:/notice/list";
	}

	
	
	//**공지사항 사진추가*/
	void fileUpload(MultipartFile mf, HttpServletRequest request) {
        
		int pos = mf.getContentType().indexOf("/");
		String type = mf.getContentType().substring(0,pos);
		System.out.println(type.equals("image")+","+type);
		
		if(mf.isEmpty()) {	// 파일 없으면 저장 x
			System.out.println("파일 없음");
			return;

		}else if(!type.equals("image")) {
			System.out.println("파일형식에러");
			return;
		}else{
			// 업로드된 파일이 비어 있는지 확인
			if (!mf.isEmpty()) {
				try {
					// 파일 저장 경로
					String path = request.getServletContext().getRealPath("view\\notice\\fff")+"\\";
					//path = "C:\\GYUHWI\\workspace\\YuRP\\src\\main\\webapp\\view\\notice\\fff\\";
					//path = "workspace\\YuRP\\src\\main\\webapp\\view\\notice\\fff\\";
					
					// 업로드된 파일을 지정된 경로에 저장
					FileOutputStream fos = new FileOutputStream(path+mf.getOriginalFilename());
					fos.write(mf.getBytes());
					fos.close();
					
					System.out.println("성공띠");
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				System.out.println("모한");
			}
			
		}
		
    }
}
