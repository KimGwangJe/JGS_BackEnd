package com.example.JustGetStartedBackEnd.API.Quartz.Job;

import com.example.JustGetStartedBackEnd.API.MatchPost.Service.APIMatchPostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

@RequiredArgsConstructor
@Slf4j
public class MatchPostJob implements Job {

    private final APIMatchPostService apiMatchPostService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        try{
            apiMatchPostService.updateMatchPostsToEnd();
        } catch(Exception e){
            log.error("Error Update Match Post", e);
        }
    }
}
