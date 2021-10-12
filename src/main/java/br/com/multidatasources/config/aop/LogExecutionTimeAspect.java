package br.com.multidatasources.config.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Aspect
@Component
@ConditionalOnProperty(
    value = "spring.aop.enabled",
    havingValue = "true",
    matchIfMissing = true
)
public class LogExecutionTimeAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogExecutionTimeAspect.class);

    @Around("@within(LogExecutionTime)")
    public Object logExecutionTimeAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();

        Object proceed = joinPoint.proceed();

        long executionTime = System.currentTimeMillis() - start;

        LOGGER.info("Method called: {} executed in {} ms", joinPoint.getSignature().toShortString(), executionTime);

        return proceed;
    }

}
