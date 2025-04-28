package com.goorm.thelastsupper.common.batch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class WaitingQueueBatchScheduler {

    private final JobLauncher jobLauncher;
    private final Job waitingQueueToHistoryJob;

    @Scheduled(cron = "0 0 0 * * *")  // 매일 자정 (00:00:00)
    public void runWaitingQueueToHistoryJob() {
        try {
            log.info("=== 웨이팅 큐 -> 히스토리 배치 실행 시작 ===");
            jobLauncher.run(waitingQueueToHistoryJob, new JobParameters());
        } catch (Exception e) {
            log.error("웨이팅 큐 -> 히스토리 배치 실행 실패", e);
        }
    }
}