package com.example.JustGetStartedBackEnd.Config;

import com.example.JustGetStartedBackEnd.API.Image.Service.APIImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class SchedulerConfig {

    private final APIImageService apiImageService;

    //사용되지 않는 이미지를 삭제
    @Scheduled(cron = "0 0 0 * * ?") // 매일 오후 2시 40분에 실행
    @Transactional(rollbackFor = Exception.class)
    public void deleteImage(){
        apiImageService.deleteImageCommunityByNull();
    }
}
