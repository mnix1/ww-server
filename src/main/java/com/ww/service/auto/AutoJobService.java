package com.ww.service.auto;

import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AutoJobService {
    private static final int AUTO_JOB_RATE = 2000;
    public static boolean AUTOS_INITIALIZED = false;

    private final AutoService autoService;

    @Scheduled(fixedRate = AUTO_JOB_RATE)
    public synchronized void perform() {
        if (!AUTOS_INITIALIZED) {
            return;
        }
        autoService.perform();
    }
}
