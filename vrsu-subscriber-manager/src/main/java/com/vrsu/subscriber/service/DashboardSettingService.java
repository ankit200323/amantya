package com.vrsu.subscriber.service;

import com.vrsu.subscriber.model.DashboardSetting;

public interface DashboardSettingService {

	void saveDataVaultSetting(DashboardSetting ds);
	DashboardSetting getDataVaultSetting(String type);
}
