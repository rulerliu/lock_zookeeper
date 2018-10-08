package com.itmayiedu.lock.aop;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.itmayiedu.lock.ExtLock;
import com.itmayiedu.lock.ZookeeperDistrbuteLock;

@Aspect
@Component
public class LockAspect {
	
	private ExtLock lock = new ZookeeperDistrbuteLock();

	@Pointcut("annotation(com.itmayiedu.lock.aop.RLock)")
	public void rlAop() {
		System.out.println("rlAop()...");
	}

	/**
	 * 使用aop环绕通知判断拦截所有springmvc请求，判断方法上面是否含有@RLock注解
	 * @param proceedingJoinPoint
	 * @return
	 * @throws Throwable
	 */
	@Around("rlAop()")
	public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		lock.getLock();
    	Object obj = null;
		try {
			obj = proceedingJoinPoint.proceed();
		} catch (Throwable e) {
			e.printStackTrace();
		} finally{
			lock.releaseLock();;
		}
    	return obj;
	}
	
	/**
	 * 获取到AOP拦截的方法
	 * @return
	 */
	private Method getSignatureMethod (ProceedingJoinPoint proceedingJoinPoint) {
		MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
		Method method = signature.getMethod();
		return method;
	}
	
	private HttpServletRequest getRequest () {
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attributes.getRequest();
		return request;
	}
	
	private String getRequestURI () {
		return getRequest().getRequestURI();
	}
	
}
