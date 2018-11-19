package com.ww.service.auto;

import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AutoJobService {
    private static final int AUTO_JOB_RATE = 10000;

    private final AutoService autoService;

    @Scheduled(fixedRate = AUTO_JOB_RATE)
    public synchronized void perform() {
        autoService.perform();
    }
}
