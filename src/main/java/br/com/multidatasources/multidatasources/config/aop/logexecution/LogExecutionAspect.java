package br.com.multidatasources.multidatasources.config.aop.logexecution;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogExecutionAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogExecutionAspect.class);

    @Around("@annotation(LogExecutionTime)")
    public Object logExecutionTimeAroundOnMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();

        Object proceed = joinPoint.proceed();

        long executionTime = System.currentTimeMillis() - start;

        LOGGER.info("Method called: {} executed in {} ms", joinPoint.getSignature().toShortString(), executionTime);

        return proceed;
    }

}
