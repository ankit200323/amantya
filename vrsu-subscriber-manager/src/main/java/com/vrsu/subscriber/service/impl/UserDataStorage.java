package com.vrsu.subscriber.service.impl;

import java.awt.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.paho.client.mqttv3.MqttClient;

import com.vrsu.subscriber.model.MqttConnectionRequest;
import com.vrsu.subscriber.model.MqttConnectionUserTopicData;
import com.vrsu.subscriber.model.UserWebSocketInfo;
import com.vrsu.subscriber.mqtt.MqttPacketListner;

import lombok.Getter;

public class UserDataStorage {

	private UserDataStorage() {
		super();
		// Auto-generated constructor stub
	}

	/**
	 * key is session id value is user related information
	 */
	@Getter
	// session id -> user object
	private static Map<String, UserWebSocketInfo> sessions = new ConcurrentHashMap<>();

	@Getter
	private static Map<MqttConnectionRequest, MqttClient> mqttConnection = new ConcurrentHashMap<>();

	@Getter
	private static Map<MqttConnectionRequest, MqttConnectionUserTopicData> userOnMqttConnection = new ConcurrentHashMap<>();

}