package com.example.JustGetStartedBackEnd.API.Quartz;

import com.example.JustGetStartedBackEnd.API.Quartz.Job.FCMJob;
import com.example.JustGetStartedBackEnd.API.Quartz.Job.ImageJob;
import com.example.JustGetStartedBackEnd.API.Quartz.Job.MatchPostJob;
import com.example.JustGetStartedBackEnd.API.Quartz.JobListener.QuartzJobListener;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.quartz.*;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class QuartzSchedulerConfig {

    private final Scheduler scheduler;

    @PostConstruct
    private void jobProgress() throws SchedulerException {
        configureJobs();
        scheduler.start();
    }

    /**
     * Job 및 Cron 스케줄러 구성 메서드
     */
    private void configureJobs() throws SchedulerException {
        scheduleImageJob();
        scheduleFCMJob();
        scheduleMatchPostJob();
    }

    /**
     * ImageJob 스케줄링
     */
    private void scheduleImageJob() throws SchedulerException {
        JobDetail imageJob = JobBuilder
                .newJob(ImageJob.class)
                .withIdentity("imageJob", "imageGroup")
                .withDescription("매일 자정에 이미지 관련 작업 실행")
                .build();

        CronTrigger imageTrigger = TriggerBuilder
                .newTrigger()
                .withIdentity("imageTrigger", "imageGroup")
                .withDescription("매일 자정에 이미지 관련 작업 실행")
                .withSchedule(CronScheduleBuilder.cronSchedule("0 00 00 * * ?"))
                .build();

        QuartzJobListener jobListener = new QuartzJobListener();
        scheduler.getListenerManager().addJobListener(jobListener);
        scheduler.scheduleJob(imageJob, imageTrigger);
    }

    /**
     * FCMJob 스케줄링
     */
    private void scheduleFCMJob() throws SchedulerException {
        JobDetail fcmJob = JobBuilder
                .newJob(FCMJob.class)
                .withIdentity("fcmJob", "fcmGroup")
                .withDescription("매일 자정에 FCM 작업 실행")
                .build();

        CronTrigger fcmTrigger = TriggerBuilder
                .newTrigger()
                .withIdentity("fcmTrigger", "fcmGroup")
                .withDescription("매일 자정에 FCM 작업 실행")
                .withSchedule(CronScheduleBuilder.cronSchedule("0 00 00 * * ?"))
                .build();

        QuartzJobListener jobListener = new QuartzJobListener();
        scheduler.getListenerManager().addJobListener(jobListener);
        scheduler.scheduleJob(fcmJob, fcmTrigger);
    }

    /**
     * MatchPostJob 스케줄링
     */
    private void scheduleMatchPostJob() throws SchedulerException {
        JobDetail matchPostJob = JobBuilder
                .newJob(MatchPostJob.class)
                .withIdentity("matchPostJob", "matchPostGroup")
                .withDescription("매 1시간마다 정각에 매치 포스트 업데이트 작업 실행")
                .build();

        CronTrigger matchPostTrigger = TriggerBuilder
                .newTrigger()
                .withIdentity("matchPostTrigger", "matchPostGroup")
                .withDescription("매 1시간마다 정각에 매치 포스트 업데이트 작업 실행")
                .withSchedule(CronScheduleBuilder.cronSchedule("0 0 0/1 * * ?"))
                .build();

        QuartzJobListener jobListener = new QuartzJobListener();
        scheduler.getListenerManager().addJobListener(jobListener);
        scheduler.scheduleJob(matchPostJob, matchPostTrigger);
    }
}
