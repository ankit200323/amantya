package com.vrsu.subscriber.model;

import java.time.LocalDateTime;

import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.vrsu.subscriber.mqtt.MqttPublisher;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserWebSocketInfo {

	private String sessionId;
	private boolean isAlive;
	private LocalDateTime lastReceiveMessageTime;
	private String topicSubscribed;
	private String filterKey;
	private MqttPublisher mqttPublisher = new MqttPublisher();
	private MqttConnectionRequest mqttConnectionRequest;
	private SimpMessagingTemplate template;

}