package yurp.model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;


public class Excel {

	public Excel(ServletContext servletContext,ArrayList<ProductDTO> arr,int[]reqCnt,String excleName) {
		
		
		  try {
			Workbook wb = new XSSFWorkbook();
	        Sheet sheet = wb.createSheet("발주");
	        Row row = null;
	        Cell cell = null;
	        int rowNum = 0;

	        // Header
	        row = sheet.createRow(rowNum++);
	        cell = row.createCell(0);
	        cell.setCellValue("no");
	        cell = row.createCell(1);
	        cell.setCellValue("브랜드");
	        cell = row.createCell(2);
	        cell.setCellValue("품번");
	        cell = row.createCell(3);
	        cell.setCellValue("컬러");
	        cell = row.createCell(4);
	        cell.setCellValue("사이즈");
	        cell = row.createCell(5);
	        cell.setCellValue("가격");
	        cell = row.createCell(6);
	        cell.setCellValue("제품명");
	        cell = row.createCell(7);
	        cell.setCellValue("요청수량");
	        

	        // Body
	        for (int i=0; i<reqCnt.length; i++) {
	        	ProductDTO product = arr.get(i);
	        	 row = sheet.createRow(rowNum++);
	        	 cell = row.createCell(0);
	        	 cell.setCellValue(i + 1); 
	        	 cell = row.createCell(1);
	        	 cell.setCellValue(product.getBCode());
	        	 cell = row.createCell(2);
	        	 cell.setCellValue(product.getPNum());
	        	 cell = row.createCell(3);
	        	 cell.setCellValue(product.getColor());
	        	 cell = row.createCell(4);
	        	 cell.setCellValue(product.getPSize());
	        	 cell = row.createCell(5);
	        	 cell.setCellValue(product.getLiPrice());
	        	 cell = row.createCell(6);
	        	 cell.setCellValue(product.getPName());
	        	 cell = row.createCell(7);
	        	 cell.setCellValue(reqCnt[i]);
	        }
	        
	        //폴더 만들어서 거기다 저장할거임
	        String directoryPath = servletContext.getRealPath("/excel");
	        File directory = new File(directoryPath);
	        if (!directory.exists()) {
	            directory.mkdirs();
	        }

	      
	        String filePath = directoryPath + "/"+excleName+".xlsx";
	        File file = new File(filePath);

	        
	        FileOutputStream fos = new FileOutputStream(file);
	        wb.write(fos);
	        fos.close();

	        // 컨텐츠 타입과 파일명 지정
//	        response.setContentType("ms-vnd/excel");
//	        response.setHeader("Content-Disposition", "attachment;filename=example.xlsx");
//
//	
//				wb.write(response.getOutputStream());
//				wb.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	       
	    
	}
	
}
