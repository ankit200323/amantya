package com.vrsu.subscriber.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.vrsu.subscriber.service.SubscriberService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ScheduledTasks {

	@Autowired
	SubscriberService subscriberService;

	@Scheduled(fixedRate = 300000)
	public void checkUserAndUpdateTime() {
		log.info("ScheduledTasks :: Action triggered to check and update/delete user sessions");
		subscriberService.checkAndDeleteOldSessions();
	}
}