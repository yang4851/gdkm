package com.insilicogen.gdkm.async;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

public class NgsFileAsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public void handleUncaughtException(Throwable throwable, Method method, Object... params) {
		logger.error("백엔드 프로세스 오류 : " + throwable.getMessage());
		logger.error("백엔드 프로세스 메소드 : " + method.getName());
		
		for (Object param : params) {
			logger.error("백엔드 프로세스 매개변수 : " + param);
		}
	}
}
