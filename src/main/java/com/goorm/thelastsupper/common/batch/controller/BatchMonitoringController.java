package com.goorm.thelastsupper.common.batch.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.*;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/batch")
@RequiredArgsConstructor
public class BatchMonitoringController {

    private final JobExplorer jobExplorer;

    @GetMapping("/waiting-queue-to-history/status")
    public BatchStatusResponse getWaitingQueueToHistoryBatchStatus() {
        List<JobInstance> instances = jobExplorer.findJobInstancesByJobName("waitingQueueToHistoryJob", 0, 1);

        if (instances.isEmpty()) {
            return new BatchStatusResponse("NO_EXECUTION", null, null, 0L, 0L, "No executions found");
        }

        JobInstance jobInstance = instances.get(0);
        List<JobExecution> executions = jobExplorer.getJobExecutions(jobInstance);

        Optional<JobExecution> latestExecution = executions.stream()
                .max(Comparator.comparing(JobExecution::getStartTime));

        if (latestExecution.isEmpty()) {
            return new BatchStatusResponse("NO_EXECUTION", null, null, 0L, 0L, "No executions found");
        }

        JobExecution execution = latestExecution.get();

        // StepExecution 중 status가 COMPLETED인 것만 합산
        long readCount = execution.getStepExecutions().stream()
                .filter(stepExecution -> stepExecution.getStatus() == BatchStatus.COMPLETED)
                .mapToLong(StepExecution::getReadCount)
                .sum();

        long writeCount = execution.getStepExecutions().stream()
                .filter(stepExecution -> stepExecution.getStatus() == BatchStatus.COMPLETED)
                .mapToLong(StepExecution::getWriteCount)
                .sum();

        return new BatchStatusResponse(
                execution.getStatus().toString(),
                execution.getStartTime(),
                execution.getEndTime(),
                readCount,
                writeCount,
                execution.getExitStatus().getExitCode()
        );
    }

    public record BatchStatusResponse(
            String status,
            LocalDateTime startTime,
            LocalDateTime endTime,
            Long readCount,
            Long writeCount,
            String exitCode
    ) {
    }
}