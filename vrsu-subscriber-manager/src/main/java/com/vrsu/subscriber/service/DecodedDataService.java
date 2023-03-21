package com.vrsu.subscriber.service;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.vrsu.subscriber.model.DecodedData;


public interface DecodedDataService {

	void save(DecodedData todo);

	Page<DecodedData> findByTimestampBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);

	Page<DecodedData> findByTimestampBetweenAndMessageType(LocalDateTime start, LocalDateTime end, String messageType,
			Pageable pageable);

	Page<DecodedData> finddatabyTopic(LocalDateTime start, LocalDateTime end, String topicHas, Pageable pageable);

	Page<DecodedData> finddataByTopicAndMessage(LocalDateTime start, LocalDateTime end, String messageType,
			String topicHas, Pageable pageable);

}