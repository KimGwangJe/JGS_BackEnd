package com.example.JustGetStartedBackEnd.Config;

import com.example.JustGetStartedBackEnd.API.Image.Service.APIImageService;
import com.example.JustGetStartedBackEnd.API.MatchPost.Service.APIMatchPostService;
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
    private final APIMatchPostService apiMatchPostService;

    //사용되지 않는 이미지를 삭제
    @Scheduled(cron = "0 0 0 * * ?") // 매일 오후 2시 40분에 실행
    @Transactional(rollbackFor = Exception.class)
    public void deleteImage() {
        try {
            apiImageService.deleteImageCommunityByNull();
        } catch(Exception e){
            log.error("Error Delete Image Community", e);
        }
    }

    @Scheduled(cron = "0 0 0/1 * * ?") // 매 1시간마다 실행
    @Transactional(rollbackFor = Exception.class)
    public void updateMatchPostsToEnd(){
        try{
            apiMatchPostService.updateMatchPostsToEnd();
        } catch(Exception e){
            log.error("Error Update Match Post", e);
        }
    }
}
