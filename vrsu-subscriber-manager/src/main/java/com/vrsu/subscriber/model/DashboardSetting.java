package com.vrsu.subscriber.model;

import java.time.LocalDateTime;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Document(collection = "DashboardSetting")
@Component
public class DashboardSetting {

	@MongoId
	private String id;
	@Field
	boolean status;
	@Field
	String startTimeStamp;
	@Field
	String endTimeStamp;
	@Field
	String type;
}
