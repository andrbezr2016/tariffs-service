package com.andrbezr2016.tariffs.config;

import com.andrbezr2016.tariffs.job.NotificationJob;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NotificationJobConfig {

    @Bean
    public JobDetail jobDetail() {
        return JobBuilder.newJob().ofType(NotificationJob.class)
                .storeDurably()
                .withIdentity("NotificationJob")
                .withDescription("Invoke NotificationJob")
                .build();
    }

    @Bean
    public Trigger trigger(JobDetail job) {
        return TriggerBuilder.newTrigger().forJob(job)
                .withIdentity("NotificationJob_Trigger")
                .withDescription("NotificationJob trigger")
                .withSchedule(SimpleScheduleBuilder.repeatMinutelyForever())
                .build();
    }
}
