package it.course.myblogc4.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.course.myblogc4.entity.AdvisoryStatus;
import it.course.myblogc4.payload.response.ReportAuthor;
import it.course.myblogc4.payload.response.ReportPost;
import it.course.myblogc4.payload.response.ReportReader;
import it.course.myblogc4.repository.AdvisoryRepository;
import it.course.myblogc4.repository.CommentRepository;
import it.course.myblogc4.repository.PostRepository;
import it.course.myblogc4.repository.PostVisitedRepository;
import it.course.myblogc4.repository.UserRepository;

@Service
public class XlsFileService {
	
	@Autowired UserRepository userRepository;
	@Autowired PostRepository postRepository;
	@Autowired PostVisitedRepository postVisitedRepository;
	@Autowired CommentRepository commentRepository;
	@Autowired AdvisoryRepository advisoryRepository;
	
	public InputStream createReport() throws Exception{
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		
		
		/* generazione dell xls */
		
		
		// creazione del file
		HSSFWorkbook workbook = new HSSFWorkbook();
		
		//add to xls dei 3 sheet
		createExcelAuthorReport(workbook);
		createExcelReaderReport(workbook);
		createExcelPostReport(workbook);
		
		//chiusura del file
		workbook.write(out);
		workbook.close();
		
		
		InputStream in = new ByteArrayInputStream(out.toByteArray());
		
		return in;
		
	}
	
	// AUTHOR REPORT SHEET
	public void createExcelAuthorReport(HSSFWorkbook workbook){

		HSSFSheet sheet = workbook.createSheet("Author Report");
		HSSFCellStyle style = createStyleForTitle(workbook);		
		
		int rownum = 2;
		Cell cell;
		Row row;

		row = sheet.createRow(rownum);
		
		// INTESTAZIONI COLONNA
		
		// Id
		cell = row.createCell(0, CellType.STRING);
		cell.setCellValue("User Id");
		cell.setCellStyle(style);

		// Username
		cell = row.createCell(1, CellType.STRING);
		cell.setCellValue("Username");
		cell.setCellStyle(style);

		// Nbr. posts written
		cell = row.createCell(2, CellType.STRING);
		cell.setCellValue("Nr. posts written");
		cell.setCellStyle(style);
		
		// Nbr. views
		cell = row.createCell(3, CellType.STRING);
		cell.setCellValue("Nr. views");
		cell.setCellStyle(style);

		// Average rate post
		cell = row.createCell(4, CellType.STRING);
		cell.setCellValue("Average rate post");
		cell.setCellStyle(style);
		
		// INSERT DATA
		List<ReportAuthor> ras = userRepository.getReportAuthor();
		for (ReportAuthor ra : ras) {
			rownum++;
			
			row = sheet.createRow(rownum);

			// Id
			cell = row.createCell(0, CellType.NUMERIC);
			cell.setCellValue(ra.getId());

			// Username
			cell = row.createCell(1, CellType.STRING);
			cell.setCellValue(ra.getUsername());

			// Nbr. posts written
			cell = row.createCell(2, CellType.NUMERIC);
			cell.setCellValue(ra.getNrWrittenPosts());

			// Nbr. views
			cell = row.createCell(3, CellType.NUMERIC);
			cell.setCellValue(ra.getNrViews());
			
			// Average rate post
			cell = row.createCell(4, CellType.NUMERIC);
			cell.setCellValue(ra.getAvgWrittenPosts());

		}
		
		
		/* INSERT TOTAL */
		
		rownum++;
		row = sheet.createRow(rownum);
		
		// total authors
		cell = row.createCell(1, CellType.NUMERIC);
		cell.setCellValue(ras.size());
		cell.setCellStyle(style);
		
		// total posts
		cell = row.createCell(2, CellType.NUMERIC);
		cell.setCellValue(postRepository.count());
		cell.setCellStyle(style);
		
		// total views
		cell = row.createCell(3, CellType.NUMERIC);
		cell.setCellValue(postVisitedRepository.count());
		cell.setCellStyle(style);
				
		//Auto size columns
		sheet.autoSizeColumn(0);
		sheet.autoSizeColumn(1);
		sheet.autoSizeColumn(2);
		sheet.autoSizeColumn(3);
		sheet.autoSizeColumn(4);
		
	}
	
	// READER REPORT SHEET
	public void createExcelReaderReport(HSSFWorkbook workbook){

		HSSFSheet sheet = workbook.createSheet("Reader Report");
		HSSFCellStyle style = createStyleForTitle(workbook);

		int rownum = 2;
		Cell cell;
		Row row;
		
		rownum = 2;
		row = sheet.createRow(rownum);
		
		// Id
		cell = row.createCell(0, CellType.NUMERIC);
		cell.setCellValue("ID");
		cell.setCellStyle(style);

		// Username
		cell = row.createCell(1, CellType.STRING);
		cell.setCellValue("Username");
		cell.setCellStyle(style);
		
		// Nr. Of Comments
		cell = row.createCell(2, CellType.STRING);
		cell.setCellValue("Nr. of comments");
		cell.setCellStyle(style);
		
		// Nr. Of Advisories
		cell = row.createCell(3, CellType.STRING);
		cell.setCellValue("Nr. of Banned Comments");
		cell.setCellStyle(style);
		
		// Enabled
		cell = row.createCell(4, CellType.STRING);
		cell.setCellValue("Enabled(Y/N)");
		cell.setCellStyle(style);
		
		List<ReportReader> rrr = userRepository.getReportReader();
		for (ReportReader rr : rrr) {
			rownum++;
		
			row = sheet.createRow(rownum);
			
			// Id
			cell = row.createCell(0, CellType.NUMERIC);
			cell.setCellValue(rr.getId());
			
			// Username
			cell = row.createCell(1, CellType.STRING);
			cell.setCellValue(rr.getUsername());
			
			// Nbr. posts written
			cell = row.createCell(2, CellType.NUMERIC);
			cell.setCellValue(rr.getNumberOfComments());
			
			// Average rate post
			cell = row.createCell(3, CellType.NUMERIC);
			cell.setCellValue(rr.getNumberOfBannedComment());
			
			cell = row.createCell(4, CellType.STRING);
			cell.setCellValue(rr.getEnabled() == true ? "Y" : "N");
			
		}

		/* INSERT TOTAL */
		
		rownum++;
		row = sheet.createRow(rownum);
		
		// total reader
		cell = row.createCell(1, CellType.NUMERIC);
		cell.setCellValue(rrr.size());
		cell.setCellStyle(style);
		
		// total comments
		cell = row.createCell(2, CellType.NUMERIC);
		cell.setCellValue(commentRepository.count());
		cell.setCellStyle(style);
		
		// total views
		cell = row.createCell(3, CellType.NUMERIC);
		//cell.setCellValue(advisoryRepository.countByAdvisoryStatusEquals(AdvisoryStatus.CLOSED_WITH_CONSEQUENCE));
		cell.setCellValue(advisoryRepository.countByAdvisoryStatusEqualsSQL("CLOSED_WITH_CONSEQUENCE"));
		cell.setCellStyle(style);
				
		//Auto size columns
		sheet.autoSizeColumn(0);
		sheet.autoSizeColumn(1);
		sheet.autoSizeColumn(2);
		sheet.autoSizeColumn(3);
		sheet.autoSizeColumn(4);

		

	}
	
	// POST REPORT SHEET
	public void createExcelPostReport(HSSFWorkbook workbook){

		HSSFSheet sheet = workbook.createSheet("Post Report");
		HSSFCellStyle style = createStyleForTitle(workbook);

		int rownum = 2;
		Cell cell;
		Row row;
		
		rownum = 2;
		row = sheet.createRow(rownum);
		
		// Id
		cell = row.createCell(0, CellType.NUMERIC);
		cell.setCellValue("ID");
		cell.setCellStyle(style);

		// Username
		cell = row.createCell(1, CellType.STRING);
		cell.setCellValue("Title");
		cell.setCellStyle(style);
		
		// Author
		cell = row.createCell(2, CellType.STRING);
		cell.setCellValue("Author");
		cell.setCellStyle(style);
		
		// Average
		cell = row.createCell(3, CellType.NUMERIC);
		cell.setCellValue("Average");
		cell.setCellStyle(style);
		
		// Published
		cell = row.createCell(4, CellType.STRING);
		cell.setCellValue("Published(Y/N)");
		cell.setCellStyle(style);
		
		// Approved
		cell = row.createCell(5, CellType.STRING);
		cell.setCellValue("Approved(Y/N)");
		cell.setCellStyle(style);
		
		// Views
		cell = row.createCell(6, CellType.NUMERIC);
		cell.setCellValue("Views");
		cell.setCellStyle(style);
		
		List<ReportPost> rrr = postRepository.getReportPost();
		for (ReportPost rr : rrr) {
			rownum++;
		
			row = sheet.createRow(rownum);
			
			// Id
			cell = row.createCell(0, CellType.NUMERIC);
			cell.setCellValue(rr.getId());
			
			// Username
			cell = row.createCell(1, CellType.STRING);
			cell.setCellValue(rr.getTitle());
			
			// Nbr. posts written
			cell = row.createCell(2, CellType.NUMERIC);
			cell.setCellValue(rr.getAuthor());
			
			// Average rate post
			cell = row.createCell(3, CellType.NUMERIC);
			cell.setCellValue(rr.getAvg());
			
			cell = row.createCell(4, CellType.STRING);
			cell.setCellValue(rr.getPublished() == true ? "Y" : "N");
			
			cell = row.createCell(5, CellType.STRING);
			cell.setCellValue(rr.getApproved() == true ? "Y" : "N");
			
			// Views
			cell = row.createCell(6, CellType.NUMERIC);
			cell.setCellValue(rr.getVisits());
			
		}

		/* INSERT TOTAL */
		
		rownum++;
		row = sheet.createRow(rownum);
		
		// total posts
		cell = row.createCell(1, CellType.NUMERIC);
		cell.setCellValue(rrr.size());
		cell.setCellStyle(style);
		
		// total published
		cell = row.createCell(4, CellType.NUMERIC);
		cell.setCellValue(postRepository.countByPublishedTrue());
		cell.setCellStyle(style);
		
		// total approved
		cell = row.createCell(5, CellType.NUMERIC);
		cell.setCellValue(postRepository.countByApprovedTrue());
		cell.setCellStyle(style);
		
		// total views
		cell = row.createCell(6, CellType.NUMERIC);
		cell.setCellValue(postVisitedRepository.count());
		cell.setCellStyle(style);
				
		//Auto size columns
		sheet.autoSizeColumn(0);
		sheet.autoSizeColumn(1);
		sheet.autoSizeColumn(2);
		sheet.autoSizeColumn(3);
		sheet.autoSizeColumn(4);
		sheet.autoSizeColumn(5);
		sheet.autoSizeColumn(6);

		}
	
	private static HSSFCellStyle createStyleForTitle(HSSFWorkbook workbook) {

		HSSFFont font = workbook.createFont();
		font.setBold(true);
		HSSFCellStyle style = workbook.createCellStyle();
		style.setFont(font);
		return style;
	}
	
	
	

}
