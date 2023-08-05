package com.nt.service;

import java.io.File;
import java.io.FileOutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class StorageService {

	@Value("${application.bucket.name}")
	private String bucketName;
	
	@Autowired
	private AmazonS3 s3Client;
	
	public String uploadFile(MultipartFile file) {
			File fileobj= convertMultiPartFileToFile(file);
			String fileName=System.currentTimeMillis() + "_" + file.getOriginalFilename();
			s3Client.putObject(new PutObjectRequest(bucketName, fileName, fileobj));
			fileobj.delete();
			return "File Upload :  " + fileName; 
	}

	private File convertMultiPartFileToFile(MultipartFile file) {
		File convertfile=new File(file.getOriginalFilename());
		try(FileOutputStream fos=new FileOutputStream(convertfile)){
			fos.write(file.getBytes());
		} catch (Exception e) {
			log.error("Error Converting multipartFile to file",e);
		}
		return convertfile;
	}
	
	
}
