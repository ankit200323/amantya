package com.vrsu.subscriber.service.impl;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.vrsu.subscriber.model.DecodedData;
import com.vrsu.subscriber.repository.DecodedDataRepo;
import com.vrsu.subscriber.repository.DecodedDataRepoMongo;
import com.vrsu.subscriber.service.DecodedDataService;
import com.vrsu.subscriber.utility.Constants;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DecodedDataServiceImpl implements DecodedDataService {

	@Autowired
	DecodedDataRepo repo;


	@Autowired
	DecodedDataRepoMongo repoNonReactive;


	public DecodedDataServiceImpl(DecodedDataRepo repo) {
		this.repo = repo;
	}

	@Override
	public void save(DecodedData data) {
		try {
			repo.save(data).subscribe();
		}catch (NullPointerException e) {
			log.error(Constants.EXCEPTION,e.getMessage());
		}
	}

	@Override
	public Page<DecodedData> findByTimestampBetween(LocalDateTime start, LocalDateTime end, Pageable pageable) {
		return repoNonReactive.findByTimestampBetween(start, end, pageable);
	}

	@Override
	public Page<DecodedData> findByTimestampBetweenAndMessageType(LocalDateTime start, LocalDateTime end,
			String messageType, Pageable pageable) {
		return repoNonReactive.findByTimestampBetweenAndMessageType(start, end, messageType, pageable);
	}

	@Override
	public Page<DecodedData> finddatabyTopic(LocalDateTime start, LocalDateTime end, String topicHas,
			Pageable pageable) {
		return repoNonReactive.finddata1(start, end, topicHas, pageable);
	}

	@Override
	public Page<DecodedData> finddataByTopicAndMessage(LocalDateTime start, LocalDateTime end, String messageType,
			String topicHas, Pageable pageable) {
		return repoNonReactive.finddata2(start, end, messageType, topicHas, pageable);
	}

}