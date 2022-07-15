package br.com.multidatasources.config.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Objects;

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
    public Object logExecutionTimeAround(final ProceedingJoinPoint joinPoint) throws Throwable {
        final long start = System.currentTimeMillis();

        final Object proceed = joinPoint.proceed();

        final long executionTime = System.currentTimeMillis() - start;

        final String methodName = Objects.isNull(joinPoint.getSignature()) ? "Unrecognized" : joinPoint.getSignature().toShortString();
        LOGGER.info("Method called: {} executed in {} ms", methodName, executionTime);

        return proceed;
    }

}
