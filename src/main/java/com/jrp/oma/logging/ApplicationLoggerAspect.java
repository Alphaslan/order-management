package com.jrp.oma.logging;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@Aspect
public class ApplicationLoggerAspect {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Pointcut("within(com.jrp.oma.controllers..*) ||" +
            "within(com.jrp.oma.dao..*)")
    public void definePackagePointcuts() {
    }

    @Around("definePackagePointcuts()")
    public Object aroundLogger(ProceedingJoinPoint pjp) throws Throwable {
        logger.debug("\n--- --- --- --- --- --- --- ---\n*** Before Method Execution ***\n" +
                        "-> {} \n-> {} with argument[s] -> {}",
                pjp.getSignature().getDeclaringTypeName(),
                pjp.getSignature().getName(),
                Arrays.toString(pjp.getArgs()));

        Object o = pjp.proceed();

        logger.debug("\n*** After Method Execution ***\n" +
                        "-> {} \n-> {} with argument[s] -> {}\n--- --- --- --- --- --- --- ---",
                pjp.getSignature().getDeclaringTypeName(),
                pjp.getSignature().getName(),
                Arrays.toString(pjp.getArgs()));
        return o;
    }


}
