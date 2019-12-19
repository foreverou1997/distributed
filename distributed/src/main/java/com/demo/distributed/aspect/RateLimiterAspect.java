package com.demo.distributed.aspect;

import com.demo.distributed.annotation.RateLimiterAnnotation;
import com.google.common.util.concurrent.RateLimiter;
import groovy.util.logging.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import static com.caucho.hessian.io.HessianInputFactory.log;

/**
 * @author oulei
 * @服务限流切面
 * @Order:控制的切面的执行顺序，越小越选执行，后结束
 * @date 2019/12/4 11:27
 */
@Component
@Aspect
@Slf4j
@Order(10)
public class RateLimiterAspect  {

    /**
     * 存放每个接口的令牌桶
     */
    private ConcurrentHashMap<String, RateLimiter> limiterMap=new ConcurrentHashMap<String, RateLimiter>();


    /**
     * 定义切入点（需要切面拦截的包）
     * 拦截com.demo.distributed.api包和所有子包里的任意类的任意方法的执行
     */
    @Pointcut("execution(* com.demo.distributed.api..*.*(..))")
    public void  pointCut() {
    }


    /**
     * 环绕通知内实现限流
     */
    @Around("pointCut()")
    public Object around (ProceedingJoinPoint proceedingJoinPoint) throws Throwable{
        log.info("进入环绕通知");
        MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
        // 使用Java反射技术获取方法上是否有@RateLimiterAnnotation注解类
        RateLimiterAnnotation rateLimiterAnnotation=signature.getMethod().getDeclaredAnnotation(RateLimiterAnnotation.class);
        if(rateLimiterAnnotation == null){
            Object proceed=proceedingJoinPoint.proceed();
            return proceed;
        }
        //如果在方法上获取到RateLimiter注解就继续执行

        //获取注解上的令牌生产速率值
        double speed=rateLimiterAnnotation.speed();
        //获取注解上获取令牌的超时时间
        long  timeOut=rateLimiterAnnotation.timeout();
        RateLimiter rateLimiter=getRateLimiter(speed);
        boolean acquireBool = rateLimiter.tryAcquire(timeOut, TimeUnit.MILLISECONDS);
        //指定时间内没有获取到令牌，走服务的降级处理
        if(!acquireBool){
           serviceDegraded();
        }
        Object proceed = proceedingJoinPoint.proceed();
        return proceed;
    }


    /**
     * 获取RateLimiter对象
     */
    public RateLimiter getRateLimiter(Double speed){
        String requestURI = getRequestUrl();
        RateLimiter rateLimiter = limiterMap.get(requestURI);
        if(rateLimiter==null){
            rateLimiter = RateLimiter.create(speed);
            limiterMap.put(requestURI, rateLimiter);
        }
        return rateLimiter;
    }


    /**
     * 服务降级处理
     */
    public void serviceDegraded() throws IOException {
        // 执行服务降级处理
        log.info("请求url为"+getRequestUrl()+"的方法执行服务的降级处理--");
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = attributes.getResponse();
        response.reset();
        response.setHeader("Content-type", "text/html;charset=UTF-8");
        OutputStream outputStream=response.getOutputStream();
        try {
            outputStream.write("服务器忙！请稍后重试!".getBytes());
        } catch (Exception e) {
            log.warning("降级处理发生异常："+e.getMessage());
        } finally {
           outputStream.close();
        }
      }


    /**
     * 获取当前请求的url
     */
    public String  getRequestUrl(){
        // 获取当前的请求url
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String requestURI = request.getRequestURI();
        return requestURI;
    }


}
