package com.vrsu.subscriber.model;

import java.time.LocalDateTime;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Document(collection = "DecodedData")
@Component
public class DecodedData {

	@MongoId
	private String id;

	@Field
	private String publisherId;

	@Field
	private String speed;

	@Field
	private String latitude;

	@Field
	private String longitude;

	@Field
	private String messageType;

	@Field
	private String direction;

	@Field
	@DateTimeFormat(iso = ISO.DATE_TIME)
	private LocalDateTime timestamp;

	@Field
	private String topic;

}