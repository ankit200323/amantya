package com.vrsu.subscriber.repository;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.vrsu.subscriber.model.DecodedData;

@Repository
public interface DecodedDataRepoMongo extends MongoRepository<DecodedData, String>{
	Page<DecodedData> findByTimestampBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);

	Page<DecodedData> findByTimestampBetweenAndMessageType(LocalDateTime start, LocalDateTime end, String messageType,
			Pageable pageable);
	
	@Query(value="{'timestamp' :{$gte : ?0 ,$lte : ?1},'topic' : {$regex : ?2}}")
	Page<DecodedData> finddata1(LocalDateTime start, LocalDateTime end, String topicHas,
			Pageable pageable);

	@Query(value = "{'messageType':?2,'topic' : {$regex : ?3},'timestamp' :{$gte : ?0 ,$lte : ?1}}")
	Page<DecodedData> finddata2(LocalDateTime start, LocalDateTime end,
			String messageType, String topicHas, Pageable pageable);
}
