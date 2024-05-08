package com.app.rurban.scheduler;

import org.springframework.scheduling.annotation.Scheduled;

public class CheckInScheduler {

    @Scheduled(fixedRate = 1000)
    public void scheduleFixedRateTask() {
        System.out.println(
                "Fixed rate task - " + System.currentTimeMillis() / 1000);
    }
}
