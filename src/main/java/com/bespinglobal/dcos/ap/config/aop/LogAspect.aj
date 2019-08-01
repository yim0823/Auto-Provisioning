package com.bespinglobal.dcos.ap.config.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

/**
 * Project : Auto-Provisioning
 * Class : com.bespinglobal.dcos.ic.config.aop.LogAspect
 * Version : 
 * Created by taehyoung.yim on 2019-07-22.
 * *** 저작권 주의 ***
 */
@Aspect
@Configuration
public aspect LogAspect {

    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    // Before에 포인트컷을 작성해서 controller, service 모두 적용되게 했다.
    @Before("execution(* com..*Controller.*(..))"
            + " || execution(* com..*Service.*(..))")
    public void beforeAdvice(JoinPoint jp) {

        String type = jp.getSignature().getDeclaringTypeName();
        if (type.contains("Controller")) {
            logger.info("============\trestAPI Controller 시작\t============");
        } else if (type.contains("Service")) {
            logger.info("============\trestAPI Service 시작\t============");
        }

        logger.info(jp.getTarget() + "");
        logger.info("Method:\t" + jp.getSignature().getName());

        Object[] args = jp.getArgs();
        if (args != null) {
            for (int i = 0; i < args.length; i++) {
                logger.info(i + " 번째 값 :\t" + args[i]);
            }
        }
    }
}
