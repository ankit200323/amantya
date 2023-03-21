package com.vrsu.subscriber.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.vrsu.subscriber.model.DecodedData;


@Repository
public interface DecodedDataRepo extends ReactiveMongoRepository<DecodedData, String> {

}
