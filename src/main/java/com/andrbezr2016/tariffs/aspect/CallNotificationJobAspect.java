package com.andrbezr2016.tariffs.aspect;

import com.andrbezr2016.tariffs.job.NotificationJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Aspect
@Component
@Slf4j
public class CallNotificationJobAspect {

    private final NotificationJob notificationJob;

    @Pointcut("execution(* com.andrbezr2016.tariffs.controller.TariffController.createTariff(..))")
    public void callAtCreateTariff() {
    }

    @Pointcut("execution(* com.andrbezr2016.tariffs.controller.TariffController.updateTariff(..))")
    public void callAtUpdateTariff() {
    }

    @Pointcut("execution(* com.andrbezr2016.tariffs.controller.TariffController.deleteTariff(..))")
    public void callAtDeleteTariff() {
    }

    @Pointcut("callAtCreateTariff() || callAtUpdateTariff() || callAtDeleteTariff()")
    public void callAtChangeTariff() {
    }

    @After("callAtChangeTariff()")
    public void callNotificationJob(JoinPoint jp) {
        notificationJob.execute();
    }
}
