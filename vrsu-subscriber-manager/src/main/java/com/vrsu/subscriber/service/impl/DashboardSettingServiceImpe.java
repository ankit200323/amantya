package com.vrsu.subscriber.service.impl;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vrsu.subscriber.model.DashboardSetting;
import com.vrsu.subscriber.repository.DashboardSettingRepo;
import com.vrsu.subscriber.schedule.MyScheduledTask;
import com.vrsu.subscriber.service.DashboardSettingService;

@Service
public class DashboardSettingServiceImpe implements DashboardSettingService{

	@Autowired
	DashboardSettingRepo repo;
	@Autowired
	MyScheduledTask schedule;
	@Override
	public void saveDataVaultSetting(DashboardSetting ds) {
		DashboardSetting dashSet = repo.findByType(ds.getType());
		if(dashSet==null)
		{
			
			repo.save(ds);
		}
		else {
			dashSet.setStatus(ds.isStatus());
			dashSet.setStartTimeStamp(ds.getStartTimeStamp());
			dashSet.setEndTimeStamp(ds.getEndTimeStamp());
			repo.save(dashSet);
		}
		
	}

	@Override
	public DashboardSetting getDataVaultSetting(String type) {
		return repo.findByType(type);
	}

}
