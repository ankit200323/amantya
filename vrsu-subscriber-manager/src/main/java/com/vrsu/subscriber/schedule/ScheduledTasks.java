package com.vrsu.subscriber.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.vrsu.subscriber.model.DashboardSetting;
import com.vrsu.subscriber.service.DashboardSettingService;
import com.vrsu.subscriber.service.SubscriberService;
import com.vrsu.subscriber.utility.DecoderUtils;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ScheduledTasks {

	@Autowired
	DecoderUtils decoderUtils;
	@Autowired
	SubscriberService subscriberService;
	@Autowired
	DashboardSettingService dashServ;

	@Scheduled(fixedRate = 300000)
	public void checkUserAndUpdateTime() {
		log.info("ScheduledTasks :: Action triggered to check and update/delete user sessions");
		subscriberService.checkAndDeleteOldSessions();
	}

	@Scheduled(fixedRate = 15000)
	public void checkDataVaultSetting() {
		DashboardSetting dashSet = dashServ.getDataVaultSetting("data-vault");
		if (dashSet != null) {
			decoderUtils.setStatusToSaveInDB(dashSet.isStatus());
		}
	}

}