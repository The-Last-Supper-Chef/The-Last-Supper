package com.goorm.thelastsupper.common.batch.config;

import com.goorm.thelastsupper.waiting.entity.WaitingHistory;
import com.goorm.thelastsupper.waiting.entity.WaitingQueue;
import com.goorm.thelastsupper.waiting.repository.WaitingQueueRepository;
import com.goorm.thelastsupper.waiting.repository.WaitingHistoryRepository;
import org.springframework.batch.item.support.IteratorItemReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import org.springframework.batch.core.launch.support.RunIdIncrementer;

import org.springframework.batch.core.configuration.annotation.StepScope;

import java.util.stream.Collectors;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class WaitingQueueToHistoryJobConfig {

    private final WaitingQueueRepository waitingQueueRepository;
    private final WaitingHistoryRepository waitingHistoryRepository;
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    @Bean
    public Job waitingQueueToHistoryJob() {
        // job builder를 사용하여 job을 생성
        // job name, job repository 사용
        // moveWaitingQueueToHistoryStep 이름의 step을 실행
        return new JobBuilder("waitingQueueToHistoryJob", jobRepository)
                .start(moveWaitingQueueToHistoryStep())
                .build();
    }

    @Bean
    public Step moveWaitingQueueToHistoryStep() {
        return new StepBuilder("moveWaitingQueueToHistoryStep", jobRepository)
                .<WaitingQueue, WaitingHistory>chunk(100, transactionManager)
                .reader(waitingQueueReader())
                .processor(queue -> WaitingHistory.builder()
                        .waitingStatus(queue.getWaitingStatus())
                        .headCount(queue.getHeadCount())
                        .number(queue.getNumber())
                        .account(queue.getAccount())
                        .build())
                .writer(histories -> {
                    waitingHistoryRepository.saveAll(histories);
                    waitingQueueRepository.deleteAllInBatch();
                })
                .build();
    }

    @Bean
    @StepScope
    public IteratorItemReader<WaitingQueue> waitingQueueReader() {
        return new IteratorItemReader<>(waitingQueueRepository.findAll());
    }
}