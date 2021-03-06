package it.course.myblogc4.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import it.course.myblogc4.entity.DbFile;
import it.course.myblogc4.entity.Post;
import it.course.myblogc4.repository.RatingRepository;
import it.course.myblogc4.repository.UserRepository;

@Service
public class PdfFileService {
	
	@Autowired UserRepository userRepository;
	@Autowired RatingRepository ratingRepository;
	
	private static Font FONT_TITLE = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD, new BaseColor(0,102,204));
	private static Font FONT_CONTENT = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL);
	private static Font FONT_AUTHOR = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.ITALIC);
	private static Font FONT_DATE = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.ITALIC, BaseColor.LIGHT_GRAY);
	
	public InputStream createPdfFromPost(Post p) throws Exception{
		
		// title
		// image
		// content
		// author username
		// updatedAt
		// avg rating
		
		String title = p.getTitle();
		String content = p.getContent();
		String username = userRepository.findById(p.getAuthor().getId()).get().getUsername();
		String updatedAt = String.format("%1$tY-%1$tm-%1$td", p.getUpdatedAt());
		
		Double average = ratingRepository.postAvg(p.getId());
		String avg = average == null ? "-" : String.format("%.2f", average);
		
		DbFile image = p.getPostImage();
		
		List<String> tagList = p.getTags().stream().map(t -> t.getTagName()).collect(Collectors.toList());
		
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		
		Document document = new Document(PageSize.A4, 50, 50, 50, 50);
		PdfWriter.getInstance(document, out);
		
		document.open();
		
		addMetaData(document, title, username, tagList.toString());
		
		Paragraph pTitle = new Paragraph(title, FONT_TITLE);
		pTitle.setAlignment(Element.ALIGN_LEFT);
		document.add(pTitle);
		
		if(image != null) {
			Image img = Image.getInstance(image.getData());
			document.add(img);
		}
		
		Paragraph pContent = new Paragraph(content, FONT_CONTENT);
		pTitle.setAlignment(Element.ALIGN_JUSTIFIED_ALL);
		document.add(pContent);
		
		Paragraph pUpdateAt = new Paragraph("Date: "+updatedAt, FONT_DATE);
		pUpdateAt.setAlignment(Element.ALIGN_RIGHT);
		document.add(addEmptyLine());
		document.add(pUpdateAt);
		
		Paragraph pAuthor = new Paragraph("Author: "+username, FONT_AUTHOR);
		pAuthor.setAlignment(Element.ALIGN_RIGHT);
		document.add(addEmptyLine());
		document.add(pAuthor);
		
		Paragraph pAverage = new Paragraph("Average: "+avg, FONT_AUTHOR);
		pAverage.setAlignment(Element.ALIGN_RIGHT);
		document.add(addEmptyLine());
		document.add(pAverage);
		
		document.close();
		
		InputStream in = new ByteArrayInputStream(out.toByteArray());
		
		return in;
		
	}
	
	private static Paragraph addEmptyLine() {
		return new Paragraph(" ");
	}
	
	private void addMetaData(Document document, String title, String author, String tagList) {
		document.addTitle(title);
		document.addAuthor(author);
		document.addKeywords(tagList);
	}

}
