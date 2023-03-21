package com.vrsu.subscriber.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.vrsu.subscriber.model.DashboardSetting;

@Repository
public interface DashboardSettingRepo  extends MongoRepository<DashboardSetting, String>{

	DashboardSetting findByType(String type);


}
