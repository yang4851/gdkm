package com.insilicogen.gdkm.batch;

import javax.annotation.Resource;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.Schedules;
import org.springframework.stereotype.Component;

import com.insilicogen.gdkm.service.NgsFileProcessService;

@Component
public class JBrowseBatchTask {

	@Resource(name = "NgsFileProcessService")
	private NgsFileProcessService processService;
	
	@Schedules({ 
		@Scheduled(cron = "	0 0 0 * * * ") 
	})
	public void startFileDelete() {
		processService.startFileDelete();
	}
	
}
