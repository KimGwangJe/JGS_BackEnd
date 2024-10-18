package com.example.JustGetStartedBackEnd.Quartz.Job;

import com.example.JustGetStartedBackEnd.API.Image.Service.APIImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

@RequiredArgsConstructor
@Slf4j
public class ImageJob implements Job {

    private final APIImageService apiImageService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        try {
            apiImageService.deleteImageCommunityByNull();
        } catch(Exception e){
            log.error("Error Delete Image Community", e);
        }
    }

}
