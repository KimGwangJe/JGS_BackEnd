package com.example.JustGetStartedBackEnd.Quartz.Job;

import com.example.JustGetStartedBackEnd.API.FCM.Service.FCMService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

@RequiredArgsConstructor
@Slf4j
public class FCMJob implements Job {

    private final FCMService fcmService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        try {
            fcmService.sendMessage();
        } catch(Exception e){
            log.error("Error Send FCM Notification", e);
        }
    }
}
