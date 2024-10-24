package com.example.JustGetStartedBackEnd.API.Quartz.Job;

import com.example.JustGetStartedBackEnd.API.FCM.FCMDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.context.ApplicationEventPublisher;

@RequiredArgsConstructor
@Slf4j
public class FCMJob implements Job {

    private final ApplicationEventPublisher publisher;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        try {
            publisher.publishEvent(new FCMDTO("sendFCMNotificationForAllMember"));
        } catch(Exception e){
            log.error("Error Send FCM Notification", e);
        }
    }
}
