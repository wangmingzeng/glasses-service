package cn.com.zach.demo.glasses.common.filter;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import cn.com.zach.demo.glasses.common.utils.TimeUtil;


@Aspect
@Component
public class WebLogAop {

    private static final Logger logger = LoggerFactory.getLogger(WebLogAop.class);
    
    private ThreadLocal<Long> local = new ThreadLocal<Long>();

    @Pointcut("execution(public * cn.com.zach.demo.glasses.controller..*.*(..))")
    public void webLog(){}

    @Before("webLog()")
    public void doBeFore(JoinPoint joinPoint){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        logger.info("REQUEST_URL : {}", request.getRequestURL().toString());
        logger.info("REQUEST_METHOD : {}", request.getMethod());
        logger.info("REQUEST_IP : {}", request.getRemoteAddr());
        logger.info("CLASS_METHOD : {}.{}", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
        logger.info("REQUEST_ARGS : {}", Arrays.toString(joinPoint.getArgs()));
        logger.info("START_TIME : {}", TimeUtil.format());
        local.set(System.currentTimeMillis());
    }

    @AfterReturning(returning = "ret",pointcut="webLog()")
    public void doAfter(Object ret){
    		//logger.info("RESPONSE :{}" , JSONObject.toJSONString(ret));
        logger.info("END_TIME : {}", TimeUtil.format());
		logger.info("EXPEND_MILLISECOND : {}", (System.currentTimeMillis() - local.get()) + " ms");
    }

}
