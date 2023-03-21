package com.vrsu.subscriber.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.vrsu.subscriber.request.WebSocketDataReceiever;
import com.vrsu.subscriber.service.SubscriberService;
import com.vrsu.subscriber.service.impl.SubscriberServiceImpl;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class WebSocketController {
	private Gson gson = new Gson();

	@Autowired
	private SubscriberService subscriberService;

	@CrossOrigin
	@MessageMapping("/checkConnectivity")
	@SendTo("/checkConnectivity")
	public void checkConnectivity(String sessionId) throws InterruptedException {
		log.info("Check connetivity triggered " + sessionId);
			subscriberService.updateUserLastReceieveTime(sessionId);
			Thread.sleep(1000);
		
	}

	@CrossOrigin
	@MessageMapping("/notify")
	@SendTo("/notify")
	public void notify(String webSocketInputData) throws InterruptedException {
		log.info("Notification Received :: webSocketInputData :: " + webSocketInputData);
		WebSocketDataReceiever webDataReceiver = gson.fromJson(webSocketInputData, WebSocketDataReceiever.class);
			if (subscriberService == null) {
				subscriberService = new SubscriberServiceImpl();
			}
			subscriberService.notificationOperation(webDataReceiver);
			Thread.sleep(1000);
	}
}