package com.vrsu.subscriber.mqtt;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.vrsu.subscriber.model.MqttConnectionRequest;
import com.vrsu.subscriber.model.MqttConnectionUserTopicData;
import com.vrsu.subscriber.model.UserWebSocketInfo;
import com.vrsu.subscriber.service.impl.UserDataStorage;
import com.vrsu.subscriber.utility.Constants;
import com.vrsu.subscriber.utility.DataUtils;
import com.vrsu.subscriber.utility.DecoderUtils;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Scope("prototype")
public class MqttPacketListner implements MqttCallback {

	private static Gson gson = new Gson();

	@Setter
	private MqttConnectionRequest mqttConnectionRequest;

	@Autowired
	private DecoderUtils utils;

	@Autowired
	JsonParser jsonParser;

	@Override
	public void connectionLost(Throwable cause) {
		log.debug("Connection to Mqtt broker lost!" + cause.getMessage());
	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		MqttConnectionUserTopicData userData = UserDataStorage.getUserOnMqttConnection().get(mqttConnectionRequest);
		Set<String> users = userData.getUsers();
		Iterator<String> userList = users.iterator();
		while (userList.hasNext()) {
			String sessionId = userList.next();
			log.info("No of topic used " + userData.getTopics().size());
			UserWebSocketInfo userSession = UserDataStorage.getSessions().get(sessionId);
			processData(userSession, topic, message, sessionId);
		}
	}

	public boolean processData(UserWebSocketInfo userSession, String topic, MqttMessage message, String sessionId) {
		// IMPORTANT: Do not remove this line 62 : required for testing
		System.out.println("MESSAGE" + sessionId);
		String packet = new String(message.getPayload());
		if (userSession.getTopicSubscribed() != null && DataUtils.checkDataToSend(userSession.getTopicSubscribed(), topic)) {
			log.info(sessionId + packet);
			log.info("messageArrived :: topic name-" + topic  + "-sessionId-"
					+ userSession.getSessionId());
				return filterData(topic, message, userSession);
		}
		return false;
	}

	public boolean filterData(String topic, MqttMessage message, UserWebSocketInfo userSession) {
		try {
			String packet = new String(message.getPayload());
			log.info("topic name-" + topic + "-message-" + Arrays.toString(message.getPayload()) + " and filterkey="
					+ topic.contains(Constants.BSM));
			if (topic.contains(Constants.BSM)) {
				return utils.onMessageArrived_routedMessageBSM(topic, message, userSession);
			} else if (topic.contains(Constants.PSM)) {
				return utils.onMessageArrived_routedMessagePSM(topic, message, userSession);
			} else if (topic.contains(Constants.EVA)) {
				return utils.onMessageArrived_routedMessageEVA(topic, message, userSession);
			} else if (topic.contains(Constants.SPAT)) {
				return utils.onMessageArrived_routedMessageSPAT(topic, message, userSession);
			} else if (topic.contains(Constants.MAP)) {
				return utils.onMessageArrived_routedMessageMAP(topic, message, userSession);
			}
			return false;
		} catch (Exception e) {
			log.error("filterdata Exception.." + e.getMessage());
			return false;
		}
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
		log.debug("Successfully delivery completed for MQTT packet");
	}
}