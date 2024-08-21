package com.example.JustGetStartedBackEnd.API.Image.Service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.example.JustGetStartedBackEnd.API.Image.Entity.Image;
import com.example.JustGetStartedBackEnd.API.Image.ExceptionType.ImageExceptionType;
import com.example.JustGetStartedBackEnd.API.Image.Repository.ImageRepository;
import com.example.JustGetStartedBackEnd.Exception.BusinessLogicException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class APIImageService {
    private final AmazonS3Client amazonS3Client;

    private final AmazonS3 s3Client;

    private final ImageRepository imageRepository;

    @Value("${cloud.aws.s3.bucketName}")
    private String bucket;

    @Transactional(rollbackFor = Exception.class)
    public URL saveImage(MultipartFile image){
        // 파일명을 업로드 한 날짜로 변환하여 저장
        // S3에서 파일명이 겹치지 않게 하기 위해서
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String current_date = now.format(dateTimeFormatter);

        String originalFileExtension = "";
        String contentType = image.getContentType();

        //확장자 명이 존재하지 않을 경우 처리 X
        assert contentType != null;
        if(contentType.contains("image/jpeg") || contentType.contains("image/jpg"))
            originalFileExtension = ".jpg";
        else if(contentType.contains("image/png"))
            originalFileExtension = ".png";

        // 파일명 중복을 피하기 위해 나노초까지 얻어와 지정
        String new_file_name = current_date + System.nanoTime() + originalFileExtension;

        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(contentType);
            metadata.setContentLength(image.getSize());
            amazonS3Client.putObject(bucket,new_file_name,image.getInputStream(),metadata);
            URL url = s3Client.getUrl(bucket, new_file_name);
            imageRepository.save(Image.builder()
                    .imageName(new_file_name)
                    .imageUrl(String.valueOf(url))
                    .build());
            return url;
        } catch (IOException e) {
            throw new BusinessLogicException(ImageExceptionType.IMAGE_SAVE_ERROR);
        }
    }
}

