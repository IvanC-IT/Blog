package it.course.myblogc4.service;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import it.course.myblogc4.entity.DbFile;
import it.course.myblogc4.repository.DbFileRepository;
import it.course.myblogc4.repository.UserRepository;

@Service
public class DbFileService {
	
	@Value("${avatar.image.width}")
	int avatarWidth;
	
	@Value("${avatar.image.height}")
	int avatarHeight;
	
	@Value("${avatar.image.size}")
	int avatarSize;
	
	
	@Value("${post.image.width}")
	int postWidth;
	
	@Value("${post.image.height}")
	int postHeight;
	
	@Value("${post.image.size}")
	int postSize;
	
	
	@Autowired DbFileRepository dbFileRepository;
	
	@Autowired UserRepository userRepository;
	
	public BufferedImage getBufferedImage(MultipartFile file) {
		
		BufferedImage bf = null;
		try {
			bf = ImageIO.read(file.getInputStream());
			
		} catch (IOException e) {
			e.printStackTrace();
			
		}
		return bf;
	}
	
	public boolean ctrlAvatarKb(MultipartFile file) {
		
		if(file != null) {
			if(file.getSize() > avatarSize || file.getSize() < 1) {
				return false;
			}
		}
		return true;
	}
	
	
	public boolean ctrlAvatarDimension(BufferedImage bf) {
		
		if(bf != null) {
			if(bf.getHeight() > avatarHeight || bf.getWidth() > avatarWidth) {
				return false;
			}
		}
		return true;
	}
	
	public boolean ctrlPostImageDimension(BufferedImage bf) {
		
		if(bf != null) {
			if(bf.getHeight() > postHeight || bf.getWidth() > postWidth) {
				return false;
			}
		}
		return true;
	}
	
	public boolean ctrlPostImageKb(MultipartFile file) {
		
		if(file != null) {
			if(file.getSize() > postSize || file.getSize() < 1) {
				return false;
			}
		}
		return true;
	}
	

	public DbFile storeDbFile (MultipartFile file)  throws IOException{
		
		String fileName = file.getOriginalFilename();
		String type = file.getContentType();
		
		try {
			byte[] content = file.getBytes();
			DbFile dbFile = new DbFile(fileName,type,content);
			dbFileRepository.save(dbFile);
			return dbFile;
		} catch (IOException e) {
			throw new IOException();
		}
		
		
	}

	public boolean ctrlSameImage(MultipartFile multipartFile, DbFile dbFile){


        if(dbFile != null){
            try {
                if(Arrays.equals(multipartFile.getBytes(), dbFile.getData()))
                    return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}