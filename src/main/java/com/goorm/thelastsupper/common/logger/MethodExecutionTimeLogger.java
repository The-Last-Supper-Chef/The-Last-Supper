package com.goorm.thelastsupper.common.logger;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class MethodExecutionTimeLogger {
    private final ThreadLocal<Integer> indent = ThreadLocal.withInitial(() -> 0);

    @Around("execution(* com.goorm.thelastsupper..*(..))")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        int level = indent.get();
        indent.set(level + 1); // 다음 호출로 넘어가면 들여쓰기 1 증가

        String prefix = "\t".repeat(level); // <-- 여기서 탭 추가
        String method = joinPoint.getSignature().toShortString();
        log.info("{}|--> {}", prefix, method);

        long start = System.currentTimeMillis();
        try {
            Object result = joinPoint.proceed();
            long timeTaken = System.currentTimeMillis() - start;
            log.info("{}|<-- {} time={}ms", prefix, method, timeTaken);
            return result;
        } finally {
            indent.set(level); // 깊이 복원
        }
    }
}