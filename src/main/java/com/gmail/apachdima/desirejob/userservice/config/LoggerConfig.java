package com.gmail.apachdima.desirejob.userservice.config;

import com.gmail.apachdima.desirejob.userservice.common.message.Error;
import com.gmail.apachdima.desirejob.userservice.common.message.Info;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Configuration;

import java.util.Locale;
import java.util.Objects;

@Configuration
@Aspect
@Slf4j
public class LoggerConfig {

    @Autowired
    private MessageSource messageSource;

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void withinController() {}

    @AfterThrowing(pointcut = "withinController()", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
        Object[] params = new Object[]{
            joinPoint.getSignature().getDeclaringTypeName(),
            joinPoint.getSignature().getName(),
            Objects.nonNull(e.getCause()) ? e.getCause() : e.getMessage()};
        log.error(messageSource.getMessage(Error.LOG_CONTROLLER_EXECUTE.getKey(), params, Locale.ENGLISH));
    }

    @Around("withinController()")
    public Object invoke(ProceedingJoinPoint joinPoint) throws Throwable {
        long beforeExecution = System.currentTimeMillis();

        final Object result = joinPoint.proceed();

        long afterExecution = System.currentTimeMillis();
        long executionTimeInMillis = afterExecution -beforeExecution;

        Object[] params = new Object[]{
            joinPoint.getSignature().getDeclaringTypeName(),
            joinPoint.getSignature().getName(),
            executionTimeInMillis};

        log.info(messageSource.getMessage(Info.INFO_LOG_CONTROLLER_EXECUTE.getKey(), params, Locale.ENGLISH));
        return result;
    }
}
