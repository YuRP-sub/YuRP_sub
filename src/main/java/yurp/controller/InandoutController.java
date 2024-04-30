package yurp.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.annotation.Resource;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import yurp.model.DTOs;
import yurp.model.InandoutDTO;
import yurp.model.InandoutMapper;
import yurp.model.ProductDTO;
import yurp.model.ProductMapper;
import yurp.model.SellDTO;
import yurp.model.StoreDTO;
import yurp.model.TemplateData;

@Controller
@RequestMapping("/stock/inandout")
public class InandoutController {
	
	@Resource
	InandoutMapper inandoutmapper;
	
	@Resource
	ProductMapper productmapper;
	
	@ModelAttribute
	TemplateData templateData(TemplateData data, HttpServletRequest request) {
		
		String uri = request.getRequestURI();
		String service = uri.substring(uri.lastIndexOf("/")+1);
		
		//System.out.println("temp-service :"+service);
		
		data.setCate("stock");
		data.setSubCate("inandout");
		data.setService(service);
		
		return data;
	}

	@RequestMapping("{service}")
	String sellList(Model mm, InandoutDTO dto, DTOs dtos, TemplateData templateData, HttpServletRequest request) {
		templateData.setCate("stock/inandout");
		System.out.println(dto);
		HttpSession session = request.getSession();
		StoreDTO logDTO = (StoreDTO)session.getAttribute("loginStore");
		String logID = logDTO.getSCode();

		mm.addAttribute("storeList",inandoutmapper.storeList());
		
		mm.addAttribute("inandoutData",inandoutmapper.list(dto));
		mm.addAttribute("totData",inandoutmapper.tot(dto));	
		
		switch(templateData.getService()) {
		case "accept":

			inandoutmapper.accept(dto.getIoStat());
			inandoutmapper.acceptCalcInven(dtos.getIoArr());
			templateData.setMsg("해당전표의 입고를 완료하였습니다.");
			templateData.setGoUrl("/stock/inandout/list");
			return "inc/alert";
			
		case "reject":
			inandoutmapper.reject(dto.getIoStat());
			inandoutmapper.rejCalcInven(dtos.getIoArr());
			// inandout 처리여부 거절로 update
			// inventory 상품의 수량만큼 도착매장 이동재고에서 빼고, 출발매장의 이동재고에 추가 및 새로운 출고전표 등록
			templateData.setMsg("입고를 거절하였습니다.");
			templateData.setGoUrl("/stock/inandout/list");			
			return "inc/alert";	

		}
		
		return "template";
	}
	

	
	@GetMapping("prodAdd")
	void prodAdd(Model mm, HttpServletRequest request) {
		HashMap<String,String> param = new HashMap<>();
		String bCode = request.getParameter("bCode");
		String pCode = request.getParameter("pCode");
		param.put("start", request.getParameter("start"));
		param.put("bCode", bCode);
		param.put("pCode", pCode);

		System.out.println("pcode: "+pCode);
		mm.addAttribute("blist",productmapper.brandList());

		if(pCode!="" && pCode!=null || bCode!=null) {			
			mm.addAttribute("products",inandoutmapper.storeProdList(param));
		}	
	}
	
	
	@GetMapping("add")
	public @ResponseBody HashMap<String, ProductDTO> add(@RequestParam("key") String arr, @RequestParam("start") String start) {
		HashMap<String, ProductDTO> res = new HashMap<>();
		List<ProductDTO> dataArr = inandoutmapper.add(arr.split(","),start);
		for (ProductDTO rr : dataArr) {
			res.put(rr.getPCode(), rr);
		}
		return res;
	}
	
	
	@RequestMapping({"insertReg"})
	String ioReg(Model mm, DTOs dtos, InandoutDTO dto, TemplateData templateData) {
		switch(templateData.getService()) {
		case "insertReg":
			inandoutmapper.insertIo(dto);
			inandoutmapper.insertIoDetail(dtos.getIoArr());
			inandoutmapper.calcInven(dtos.getIoArr());
			templateData.setMsg("출고전표가 등록되었습니다.");
			templateData.setGoUrl("list");
		}
		return "inc/alert";
	}
	
	
	
	@RequestMapping("detail")
	String viewDetail(Model mm, @RequestParam String ioStat) {
//		System.out.println(inandoutmapper.getIO(ioStat).getStart()+'>'+inandoutmapper.getIO(ioStat).getArrival());

		mm.addAttribute("ioInfo", inandoutmapper.getIO(ioStat));
		mm.addAttribute("viewDetail", inandoutmapper.detail(ioStat));
		return "stock/inandout/detail";
	}
	
	@RequestMapping("excel/{ioStat}")
	public void excelExport(@PathVariable String ioStat, HttpServletResponse response) throws IOException {

		Workbook workbook = new SXSSFWorkbook();		
		Sheet sheet = workbook.createSheet();
		
		List<InandoutDTO> dtos = inandoutmapper.detail(ioStat);
		InandoutDTO info = inandoutmapper.getIO(ioStat);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		int rowIdx = 0;
		
		//타이틀
		Row titleRow = sheet.createRow(rowIdx);
		
		Cell titleCell = titleRow.createCell(0);
		sheet.addMergedRegion(new CellRangeAddress(rowIdx, rowIdx, 0, 11));
		titleCell.setCellValue("입/출고 전표");
		rowIdx++;
		
		Row brRow1 = sheet.createRow(rowIdx);
		Cell brCell1 = brRow1.createCell(0);
		sheet.addMergedRegion(new CellRangeAddress(rowIdx, rowIdx, 0, 11));
		brCell1.setCellValue("");
		rowIdx++;
		
		// 전표번호
		Row infoRow1 = sheet.createRow(rowIdx);
		
		Cell infoCell1 = infoRow1.createCell(0);
		sheet.addMergedRegion(new CellRangeAddress(rowIdx, rowIdx, 0, 6));
		infoCell1.setCellValue("전표 번호 : "+ info.getIoStat());
		rowIdx++;
		
		// 전표정보
		Row infoRow2 = sheet.createRow(rowIdx);
		
		Cell infoCell2_1 = infoRow2.createCell(0);
		sheet.addMergedRegion(new CellRangeAddress(rowIdx, rowIdx, 0, 3));
		infoCell2_1.setCellValue("등록일자 : "+sdf.format(info.getRegDate()));
		Cell infoCell2_2 = infoRow2.createCell(4);
		sheet.addMergedRegion(new CellRangeAddress(rowIdx, rowIdx, 4, 6));
		infoCell2_2.setCellValue("출발점 : "+ info.getStartName());
		Cell infoCell2_3 = infoRow2.createCell(7);
		sheet.addMergedRegion(new CellRangeAddress(rowIdx, rowIdx, 7, 9));
		infoCell2_3.setCellValue("도착점 : "+ info.getArrivalName());
		rowIdx++;
		
		Row brRow2 = sheet.createRow(rowIdx);
		Cell brCell2 = brRow2.createCell(0);
		sheet.addMergedRegion(new CellRangeAddress(rowIdx, rowIdx, 0, 11));
		brCell2.setCellValue("");
		rowIdx++;
		
		// 헤더
		Row headerRow = sheet.createRow(rowIdx);
		
		Cell headerCell1 = headerRow.createCell(0);
		headerCell1.setCellValue("no");
		Cell headerCell2 = headerRow.createCell(1);
		headerCell2.setCellValue("브랜드");
		Cell headerCell3 = headerRow.createCell(2);
		headerCell3.setCellValue("시즌");
		Cell headerCell4 = headerRow.createCell(3);
		headerCell4.setCellValue("등급");
		Cell headerCell5 = headerRow.createCell(4);
		headerCell5.setCellValue("품명");
		Cell headerCell6 = headerRow.createCell(5);
		headerCell6.setCellValue("품번");
		Cell headerCell7 = headerRow.createCell(6);
		headerCell7.setCellValue("컬러");
		Cell headerCell8 = headerRow.createCell(7);
		headerCell8.setCellValue("사이즈");
		Cell headerCell9 = headerRow.createCell(8);
		headerCell9.setCellValue("상품코드");
		Cell headerCell10 = headerRow.createCell(9);
		headerCell10.setCellValue("정가");
		Cell headerCell11 = headerRow.createCell(10);
		headerCell11.setCellValue("수량");
		Cell headerCell12 = headerRow.createCell(11);
		headerCell12.setCellValue("금액");
		rowIdx++;
		
		int len = 0;
		for (InandoutDTO dto : dtos) {
			len++;
			String grade = dto.getGrade()=="1"? "스페셜":"일반";
			Row bodyRow = sheet.createRow(rowIdx);
			
			Cell bodyCell1 = bodyRow.createCell(0);
			bodyCell1.setCellValue(len);
			Cell bodyCell2 = bodyRow.createCell(1);
			bodyCell2.setCellValue(dto.getBName());
			Cell bodyCell3 = bodyRow.createCell(2);
			bodyCell3.setCellValue(dto.getSeason());
			Cell bodyCell4 = bodyRow.createCell(3);
			bodyCell4.setCellValue(grade);
			Cell bodyCell5 = bodyRow.createCell(4);
			bodyCell5.setCellValue(dto.getPName());
			Cell bodyCell6 = bodyRow.createCell(5);
			bodyCell6.setCellValue(dto.getPNum());
			Cell bodyCell7 = bodyRow.createCell(6);
			bodyCell7.setCellValue(dto.getColor());
			Cell bodyCell8 = bodyRow.createCell(7);
			bodyCell8.setCellValue(dto.getPSize());
			Cell bodyCell9 = bodyRow.createCell(8);
			bodyCell9.setCellValue(dto.getPCode());
			Cell bodyCell10 = bodyRow.createCell(9);
			bodyCell10.setCellValue(dto.getLiPrice());
			Cell bodyCell11 = bodyRow.createCell(10);
			bodyCell11.setCellValue(dto.getCnt());
			Cell bodyCell12 = bodyRow.createCell(11);
			bodyCell12.setCellValue(dto.getPrice());			
			rowIdx++;
		}
		Row brRow3 = sheet.createRow(rowIdx);
		Cell brCell3 = brRow3.createCell(0);
		sheet.addMergedRegion(new CellRangeAddress(rowIdx, rowIdx, 0, 11));
		brCell3.setCellValue("");
		rowIdx++;
		//통계
		Row footerRow = sheet.createRow(rowIdx);
		
		Cell footerCell1 = footerRow.createCell(0);
		footerCell1.setCellValue("통계");
		
		Cell footerCell2 = footerRow.createCell(10);
		footerCell2.setCellValue(info.getTotCnt());
		
		Cell footerCell3 = footerRow.createCell(11);
		footerCell3.setCellValue(info.getTotPrice());
		

		String fileName = info.getIoStat();
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
		
		ServletOutputStream servletOutputStream = response.getOutputStream();
		
		workbook.write(servletOutputStream);
		workbook.close();
		servletOutputStream.flush();
		servletOutputStream.close();

	}

}
