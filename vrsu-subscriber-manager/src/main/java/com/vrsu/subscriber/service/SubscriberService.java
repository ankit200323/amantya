package com.vrsu.subscriber.service;

import org.eclipse.paho.client.mqttv3.MqttException;

import com.vrsu.subscriber.model.InputRequest;
import com.vrsu.subscriber.model.TopicChange;
import com.vrsu.subscriber.request.WebSocketDataReceiever;
import com.vrsu.subscriber.response.GenericResponse;

public interface SubscriberService {

	public boolean addUserToSessions(String sessionId, String topicName, String filterName) throws MqttException;
	
	public boolean checkAndDeleteOldSessions();

	public void updateUserLastReceieveTime(String sessionId);
	
	public void notificationOperation(WebSocketDataReceiever data);
	
	public void disconnectSession(String sessionId);

	public GenericResponse checkMqttConnection(InputRequest mqttConnectionRequest);
	
	public void disconnect(String inputRequest);

	public boolean updateUserTopic(TopicChange topicChange);
}
