package com.insilicogen.gdkm.async;

import java.util.concurrent.Executor;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class NgsFileAsyncServiceConfig implements AsyncConfigurer {

	static final int TASK_CORE_POOL_SIZE = 20; 
	
	static final int TASK_MAX_POOL_SIZE = 20; 
	
	static final int TASK_QUEUE_CAPACITY = 100;
	
	static ThreadPoolTaskExecutor registValidator;
	
	static ThreadPoolTaskExecutor achiveValidator;
	
	static ThreadPoolTaskExecutor unzipExecutor;
	
	static ThreadPoolTaskExecutor qcExecutor;
	
	static ThreadPoolTaskExecutor clcExecutor;
	
	static ThreadPoolTaskExecutor jbrowseExecutor;
	
	@Bean(name="RegistValidator")
	@Qualifier
	@Override
	public Executor getAsyncExecutor() {
		if(registValidator == null) {
			registValidator = new ThreadPoolTaskExecutor();
			registValidator.setCorePoolSize(TASK_CORE_POOL_SIZE);
			registValidator.setMaxPoolSize(TASK_MAX_POOL_SIZE);
			registValidator.setQueueCapacity(TASK_QUEUE_CAPACITY);
			registValidator.setBeanName("RegistValidator");
			registValidator.initialize();
    	}

		return registValidator;
	}

	public static boolean isRegistValidateExcutable() {
    	return registValidator.getActiveCount() < (TASK_CORE_POOL_SIZE + TASK_QUEUE_CAPACITY);
    }

	@Bean(name="AchiveValidator")
	@Qualifier
	public Executor getAchiveValidator() {
		if(achiveValidator == null) {
			achiveValidator = new ThreadPoolTaskExecutor();
			achiveValidator.setCorePoolSize(TASK_CORE_POOL_SIZE);
			achiveValidator.setMaxPoolSize(TASK_MAX_POOL_SIZE);
			achiveValidator.setQueueCapacity(TASK_QUEUE_CAPACITY);
			achiveValidator.setBeanName("AchiveValidator");
			achiveValidator.initialize();
    	}

		return achiveValidator;
	}
	
	public static boolean isAchiveValidateExcutable() {
    	return achiveValidator.getActiveCount() < (TASK_CORE_POOL_SIZE + TASK_QUEUE_CAPACITY);
    }
	
	@Bean(name="UnzipExecutor")
	@Qualifier
	public Executor getUnzipExecutor() {
		if(unzipExecutor == null) {
	    	unzipExecutor = new ThreadPoolTaskExecutor();
			unzipExecutor.setCorePoolSize(TASK_CORE_POOL_SIZE);
			unzipExecutor.setMaxPoolSize(TASK_MAX_POOL_SIZE);
			unzipExecutor.setQueueCapacity(TASK_QUEUE_CAPACITY);
			unzipExecutor.setBeanName("UnzipExecutor");
			unzipExecutor.initialize();
    	}

		return unzipExecutor;
	}
	
	public static boolean isUnzipExcutable() {
    	return unzipExecutor.getActiveCount() < (TASK_CORE_POOL_SIZE + TASK_QUEUE_CAPACITY);
    }
	
	@Bean(name="ClcAssemblyCellExecutor")
    @Qualifier
    public Executor getClcAssemblyCellExecutor() {
    	if(clcExecutor == null) {
    		clcExecutor = new ThreadPoolTaskExecutor();
    		clcExecutor.setCorePoolSize(TASK_CORE_POOL_SIZE);
    		clcExecutor.setMaxPoolSize(TASK_MAX_POOL_SIZE);
    		clcExecutor.setQueueCapacity(TASK_QUEUE_CAPACITY);
    		clcExecutor.setBeanName("ClcAssemblyCellExecutor");
    		clcExecutor.initialize();
    	}
    	
    	return clcExecutor;
    }
	
	public static boolean isClcAssemblyCellExcutable() {
		return clcExecutor.getActiveCount() < (TASK_CORE_POOL_SIZE + TASK_QUEUE_CAPACITY);
	}
	@Bean(name="JBrowseCellExecutor")
    @Qualifier
    public Executor getJBrowseCellExecutor() {
    	if(jbrowseExecutor == null) {
    		jbrowseExecutor = new ThreadPoolTaskExecutor();
    		jbrowseExecutor.setCorePoolSize(TASK_CORE_POOL_SIZE);
    		jbrowseExecutor.setMaxPoolSize(TASK_MAX_POOL_SIZE);
    		jbrowseExecutor.setQueueCapacity(TASK_QUEUE_CAPACITY);
    		jbrowseExecutor.setBeanName("JBrowseCellExecutor");
    		jbrowseExecutor.initialize();
    	}
    	
    	return jbrowseExecutor;
    }
	
	public static boolean isJBrowseCellExcutable() {
    	return jbrowseExecutor.getActiveCount() < (TASK_CORE_POOL_SIZE + TASK_QUEUE_CAPACITY);
    }
	
	@Bean(name="FastQcExecutor")
    @Qualifier
    public Executor getFastQcExecutor() {
    	if(qcExecutor == null) {
    		qcExecutor = new ThreadPoolTaskExecutor();
    		qcExecutor.setCorePoolSize(TASK_CORE_POOL_SIZE);
    		qcExecutor.setMaxPoolSize(TASK_MAX_POOL_SIZE);
    		qcExecutor.setQueueCapacity(TASK_QUEUE_CAPACITY);
    		qcExecutor.setBeanName("FastQcExecutor");
    		qcExecutor.initialize();
    	}
    	
    	return qcExecutor;
    }
	
	public static boolean isFastQcExcutable() {
    	return qcExecutor.getActiveCount() < (TASK_CORE_POOL_SIZE + TASK_QUEUE_CAPACITY);
    }
	
	@Override
	public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
		return new NgsFileAsyncExceptionHandler();
	}

}
