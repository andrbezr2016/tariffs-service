package com.andrbezr2016.tariffs.config;

import com.andrbezr2016.tariffs.job.ProductServiceNotificationJob;
import lombok.RequiredArgsConstructor;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.quartz.SimpleScheduleBuilder.simpleSchedule;

@RequiredArgsConstructor
@Configuration
public class ProductServiceNotificationJobConfig {

    @Bean
    public JobDetail jobDetail() {
        return JobBuilder.newJob().ofType(ProductServiceNotificationJob.class)
                .storeDurably()
                .withIdentity("ProductServiceNotificationJob")
                .withDescription("Invoke ProductServiceNotificationJob")
                .build();
    }

    @Bean
    public Trigger trigger(JobDetail job) {
        return TriggerBuilder.newTrigger().forJob(job)
                .withIdentity("ProductServiceNotificationJob_Trigger")
                .withDescription("ProductServiceNotificationJob trigger")
                .withSchedule(simpleSchedule().repeatForever().withIntervalInSeconds(10))
                .build();
    }
}
