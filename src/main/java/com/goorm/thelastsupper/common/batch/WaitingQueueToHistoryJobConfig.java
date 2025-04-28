package com.goorm.thelastsupper.common.batch;

import com.goorm.thelastsupper.waiting.entity.WaitingHistory;
import com.goorm.thelastsupper.waiting.repository.WaitingQueueRepository;
import com.goorm.thelastsupper.waiting.repository.WaitingHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

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
        // step builder를 사용하여 step을 생성
        // step name, job repository 사용
        // tasklet은 안에 있는 내욜을 1회만 실행
        return new StepBuilder("moveWaitingQueueToHistoryStep", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    log.info("=== 웨이팅 큐 데이터를 웨이팅 히스토리로 이동 시작 ===");

                    // waitingQueue의 모든 내용을 waitingQueues에 저장
                    var waitingQueues = waitingQueueRepository.findAll();

                    // waitingQueues의 모든 내용을 하나하나 waitingHistory 객체로 변환
                    // Builder 패턴으로 새로 히스토리 엔티티 만들어서 리스트로 모음
                    var histories = waitingQueues.stream()
                            .map(queue -> WaitingHistory.builder()
                                    .waitingStatus(queue.getWaitingStatus())
                                    .headCount(queue.getHeadCount())
                                    .number(queue.getNumber())
                                    .account(queue.getAccount())
                                    .build())
                            .collect(Collectors.toList());

                    log.info("총 {}건의 웨이팅 큐를 히스토리로 이동했습니다.", histories.size());
                    // waitingHistory에 저장
                    waitingHistoryRepository.saveAll(histories);

                    // waitingQueue의 모든 내용을 삭제
                    waitingQueueRepository.deleteAll();

                    log.info("=== 이동 완료 및 큐 삭제 완료 ===");

                    // FINISHED 상태로 리턴하면 step이 잘 끝낸것으로 판단
                    return RepeatStatus.FINISHED;
                }, transactionManager) // 중간에 실패한다면 transactionManager로 인해 스프링배치가 자동으로 rollback
                .build();
    }
}