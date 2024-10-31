package com.example.JustGetStartedBackEnd.API.Common.AOP;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.lang.reflect.Method;

@Slf4j
@Aspect
@Component
public class SimpleLogAop {
    @Pointcut("execution(* com.example.JustGetStartedBackEnd.API..Controller.*.*(..))")
    private void cut(){}

    @Before("cut()")
    public void beforeParameterLog(JoinPoint joinPoint) {
        Method method = getMethod(joinPoint);
        log.info("======= Method Executed = {}.{}() =======",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName());

        Object[] args = joinPoint.getArgs();
        if (args.length <= 0) {
            log.info("전달값 없음");
        }
        for (Object arg : args) {
            if (arg == null) {
                log.info("전달 값 null");
            } else {
                log.info("전달 타입 = {}", arg.getClass().getSimpleName());
                log.info("전달 값 = {}", arg);
            }
        }
    }

    @AfterReturning(value = "cut()", returning = "returnObj")
    public void afterReturnLog(Object returnObj) {
        if (returnObj == null) {
            log.info("반환 값 null");
        } else {
            log.info("반환 타입 = {}", returnObj.getClass().getSimpleName());
            log.info("반환 값 = {}", returnObj);
        }
    }

    @Around("cut()")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        // 메서드 실행
        Object result = joinPoint.proceed();

        stopWatch.stop();
        log.info("메서드 실행 시간 : {} 초", stopWatch.getTotalTimeSeconds());

        return result;
    }

    private Method getMethod(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        return signature.getMethod();
    }
}
