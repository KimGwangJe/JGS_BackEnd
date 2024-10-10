package com.example.JustGetStartedBackEnd.API.Image.Service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.example.JustGetStartedBackEnd.API.Community.Entity.Community;
import com.example.JustGetStartedBackEnd.API.Image.Entity.Image;
import com.example.JustGetStartedBackEnd.API.Image.ExceptionType.ImageExceptionType;
import com.example.JustGetStartedBackEnd.API.Image.Repository.ImageRepository;
import com.example.JustGetStartedBackEnd.API.Common.Exception.BusinessLogicException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
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
            log.warn("Image Save Error : {}", e.getMessage());
            throw new BusinessLogicException(ImageExceptionType.IMAGE_SAVE_ERROR);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void linkImagesToCommunity(String html, Community community) {
        List<String> srcList = new ArrayList<>();
        Pattern pattern = Pattern.compile("<img[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>");
        Matcher matcher = pattern.matcher(html);
        while (matcher.find()) {
            srcList.add(matcher.group(1)); // 이미지 src 속성 값 추출
        }

        // 이미 노트와 관련된 이미지들 가져옴
        List<Image> relatedImages = imageRepository.findByCommunityId(community.getCommunityId());

        // HTML에 있는 이미지와 이미 노트와 관련된 이미지들을 비교하여 관계 맺기
        for (String src : srcList) {
            Image image = imageRepository.findByImageUrl(src);
            if (image != null && relatedImages.contains(image)) {
                // 이미 노트와 관련된 이미지이면서 HTML에도 있는 경우, 관계 유지
                continue;
            }
            // 이미 노트와 관련된 이미지가 아니거나 HTML에 없는 경우, 새로운 이미지 생성 및 관계 맺기
            assert image != null;
            image.updateCommunity(community);
        }

        // HTML에는 없지만 이미 노트와 관련된 이미지들을 찾아서 끊기
        for (Image image : relatedImages) {
            if (!srcList.contains(image.getImageUrl())) {
                image.unLinkCommunity();
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteImageCommunityByNull(){
        List<Image> images = imageRepository.findByCommunityId(null);
        for(Image image : images){
            s3Client.deleteObject(new DeleteObjectRequest(bucket, image.getImageName()));
        }
        imageRepository.deleteImagesWhereCommunityIdIsNull();
    }
}

