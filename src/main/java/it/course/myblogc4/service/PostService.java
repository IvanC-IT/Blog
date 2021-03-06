package it.course.myblogc4.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

@Service
public class PostService {
	
	public boolean isExactMatch(String wordToFind, String source) {
		
		String pattern = "\\b"+wordToFind+"\\b";
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(source);

		return m.find();
	}

}
