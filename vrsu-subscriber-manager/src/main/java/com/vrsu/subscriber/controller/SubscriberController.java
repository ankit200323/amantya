package com.vrsu.subscriber.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vrsu.subscriber.model.DecodedData;
import com.vrsu.subscriber.model.Filters;
import com.vrsu.subscriber.model.InputRequest;
import com.vrsu.subscriber.model.TopicChange;
import com.vrsu.subscriber.model.UserInfo;
import com.vrsu.subscriber.mqtt.MqttCredentials;
import com.vrsu.subscriber.response.GenericResponse;
import com.vrsu.subscriber.service.DashboardSettingService;
import com.vrsu.subscriber.service.DecodedDataService;
import com.vrsu.subscriber.service.SubscriberService;
import com.vrsu.subscriber.utility.Constants;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping(value = "/api/subscriber")
public class SubscriberController extends Constants{

	@Autowired
	SubscriberService subscriberService;


	@Autowired
	MqttCredentials mqttCredentials;

	@Autowired
	DecodedDataService decodedServ;

	@Autowired
	DashboardSettingService  dashServ;
	@CrossOrigin
	@GetMapping(value = "/welcome")
	public ResponseEntity<String> welcome() {
			return new ResponseEntity<String>(WELCOME, HttpStatus.OK);
	}


	@CrossOrigin
	@PostMapping(value = "/login")
	public ResponseEntity<GenericResponse> login(@RequestBody UserInfo userInfo, HttpServletResponse response,
			HttpServletRequest request) {
		GenericResponse genericResponse = new GenericResponse();
		ResponseEntity<GenericResponse> responseEntity;
		try {
			if (userInfo.getUsername().equalsIgnoreCase("admin") && userInfo.getPassword().equalsIgnoreCase("admin")) {
				genericResponse.setMessage(SUCCESSFULL_LOGIN);
				genericResponse.setStatus(true);
				genericResponse.setSessionId(request.getSession().getId());
				responseEntity = new ResponseEntity<>(genericResponse, HttpStatus.OK);
			} else {
				genericResponse.setMessage(INVALID_CREDENTIAL);
				genericResponse.setStatus(false);
				responseEntity =  new ResponseEntity<>(genericResponse, HttpStatus.OK);
			}
		} catch (Exception e) {
			log.error(ERROR_FETCHING_LOGIN_DETAILS, e.getMessage());
			responseEntity =  new ResponseEntity<GenericResponse>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return responseEntity;
	}

	@CrossOrigin
	@PostMapping(value = "/mqttConnectionCheck")
	public ResponseEntity<GenericResponse> mqttConnectionCheck(@RequestBody InputRequest inputRequest) {
		log.info("SubscriberController :: mqttConnectionCheck :: Receieve request to check Mqtt Connection!");
		GenericResponse genericResponse = new GenericResponse();
		ResponseEntity<GenericResponse> responseEntity;
		if (StringUtils.isNotBlank(inputRequest.getSessionId())) {
			try {
				GenericResponse result = subscriberService.checkMqttConnection(inputRequest);
				responseEntity =  new ResponseEntity<GenericResponse>(result, HttpStatus.OK);
			} catch (Exception e) {
				log.error(ERROR_FETCH_DEVICES, e);
				genericResponse.setMessage(ERROR_FETCH_DEVICES);
				responseEntity =  new ResponseEntity<GenericResponse>(genericResponse, HttpStatus.BAD_REQUEST);
			}
		} else {
			log.info("SubscriberController :: mqttConnectionCheck :: Unable to fetch Session ID");
			genericResponse.setMessage(UNABLE_TO_FETCH);
			responseEntity =  new ResponseEntity<GenericResponse>(genericResponse, HttpStatus.NOT_FOUND);
		}
		return responseEntity;
	}

	@CrossOrigin
	@PutMapping(value = "/subscribeToTopic")
	public boolean topicUpdate(@RequestBody TopicChange topicChange) {
		log.info("SubscriberController :: topicUpdate :: Receieve request to update topic" + topicChange.toString());
		
		return subscriberService.updateUserTopic(topicChange);
	}
	
	@CrossOrigin
	@PostMapping(value = "/disconnect")
	public ResponseEntity disconnect(@RequestBody String sessionId) {
		log.info("SubscriberController :: disconnect :: Receieve request to disconnect Mqtt Connection" + sessionId);
		GenericResponse genericResponse = new GenericResponse();
		ResponseEntity<GenericResponse> responseEntity;
		try {
			subscriberService.disconnect(sessionId);
			genericResponse.setMessage(USER_DISCONNECTED);
			genericResponse.setStatus(true);
			responseEntity =  new ResponseEntity<>(genericResponse, HttpStatus.OK);
		} catch (Exception e) {
			log.error(ERROR_FETCH_DEVICES, e);
			genericResponse.setMessage(ERROR_WHILE_DISCONNECTING);
			responseEntity =  new ResponseEntity<>(genericResponse, HttpStatus.BAD_REQUEST);
		}
		return responseEntity;
	}
	
	@CrossOrigin
	@PostMapping("/processed-data")
	public Page<DecodedData> getData(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam(STARTDATE) String startDate,
			@RequestParam(ENDDATE) String endDate, @RequestBody Filters filters) {
		Pageable pageable = PageRequest.of(page, size);
		LocalDateTime start = LocalDateTime.parse(startDate, DateTimeFormatter.ISO_DATE_TIME);
		LocalDateTime end = LocalDateTime.parse(endDate, DateTimeFormatter.ISO_DATE_TIME);
		Page<DecodedData> response;
		if (StringUtils.isBlank(filters.getMessageType()) && StringUtils.isBlank(filters.getTopicHas())) {
			 response = decodedServ.findByTimestampBetween(start, end, pageable);
		}
		else if (StringUtils.isNoneBlank(filters.getMessageType()) && StringUtils.isBlank(filters.getTopicHas())) {
			response = decodedServ.findByTimestampBetweenAndMessageType(start, end, filters.getMessageType(), pageable);
		}
		else if (StringUtils.isBlank(filters.getMessageType()) && StringUtils.isNoneBlank(filters.getTopicHas())) {
			response = decodedServ.finddatabyTopic(start, end, filters.getTopicHas(), pageable);
		}
		else {
			response = decodedServ.finddataByTopicAndMessage(start, end, filters.getMessageType(), filters.getTopicHas(),
					pageable);
		}
		return response;
	}
}