package com.example.JustGetStartedBackEnd.API.Quartz.JobListener;

import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;

@Slf4j
public class QuartzJobListener implements JobListener {
    @Override
    public String getName() {
        return "Quartz Job Listener";
    }

    /**
     * Job 실행 이전 수행
     */
    @Override
    public void jobToBeExecuted(JobExecutionContext jobExecutionContext) {
        String jobName = jobExecutionContext.getJobDetail().getKey().getName();
        log.info("JobName: {}이 실행 전입니다.", jobName);
    }

    /**
     * Job 실행 취소 시점 수행
     */
    @Override
    public void jobExecutionVetoed(JobExecutionContext jobExecutionContext) {
        String jobName = jobExecutionContext.getJobDetail().getKey().getName();
        log.warn("JobName: {}이 실행 취소되었습니다.", jobName);
    }

    /**
     * Job 실행 완료 시점 수행
     */
    @Override
    public void jobWasExecuted(JobExecutionContext jobExecutionContext, JobExecutionException e) {
        String jobName = jobExecutionContext.getJobDetail().getKey().getName();
        log.info("JobName: {}의 실행이 완료되었습니다.", jobName);
    }
}
