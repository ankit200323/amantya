package com.vrsu.subscriber.mqtt;

import java.util.Random;

import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.vrsu.subscriber.model.MqttConnectionRequest;
import com.vrsu.subscriber.utility.Constants;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Scope("prototype")
public class MqttPublisher {

	@Getter
	@Setter
	private MqttClient mqttClient;

	 @Autowired
	 private ObjectProvider<MqttPacketListner> mqttObject;
	public boolean isConnect() throws MqttException {
		if (mqttClient != null) {
			log.info("Mqtt isConnected");
			return mqttClient.isConnected();
		}
		log.info("Mqtt isDisconnected");
		return false;
	}

	public void disconnect() throws MqttException {
		if (mqttClient != null) {
			mqttClient.disconnect();
		}
	}

	public MqttClient checkConnection(MqttConnectionRequest mqttConnectionRequest) {
		if (mqttClient == null) {
			return establishedConnection(mqttConnectionRequest);
		}
		return mqttClient;
	}

	public MqttClient establishedConnection(MqttConnectionRequest mqttConnectionRequest) {
		String tmpDir = System.getProperty(Constants.MQTT_DIRECTORY_URL);
		MqttConnectOptions connectionOptions = new MqttConnectOptions();
		connectionOptions.setCleanSession(true);
		connectionOptions.setAutomaticReconnect(true);
		connectionOptions.setPassword(mqttConnectionRequest.getPassword().toCharArray());
		connectionOptions.setUserName(mqttConnectionRequest.getUsername());
		MqttDefaultFilePersistence dataStore = new MqttDefaultFilePersistence(tmpDir);

		try {
			int clientId = new Random().nextInt(500) + 100;
			mqttClient = new MqttClient(mqttConnectionRequest.getUrl(), "" + clientId, dataStore);
			IMqttToken token = mqttClient.connectWithResult(connectionOptions);
			token.waitForCompletion();

			log.info("MqttPublisher :: checkConnection :: Successfully establised MQTT connection...!");
			MqttPacketListner mqtt = mqttObject.getObject();
			mqttClient.setCallback(mqtt);
			mqtt.setMqttConnectionRequest(mqttConnectionRequest);
			return mqttClient;
		} catch (MqttException e) {
			log.info(
					"MqttPublisher :: checkConnection :: Error while establishing MQTT connection..!" + e.getMessage());
		}
		return null;
	}

	public void subscribeTopic(MqttConnectionRequest mqttConnectionRequest, String topic) throws MqttException {
		if (mqttClient == null) {
			log.info("Need to create New MQTT connection.!!");
			establishedConnection(mqttConnectionRequest);
		}
		if (topic != null) {
			String[] topics = new String[2];
			int[] qos = { 0, 1 };
			topics[0] = topic;
			topics[1] = Constants.MAP_DATA_TOPIC;
			mqttClient.subscribe(topics, qos);
		}
	}

	public void unsubscribeTopic(String topic) throws MqttException {
		if (mqttClient != null) {
			mqttClient.unsubscribe(topic);
		}
	}

	public void publishMessage(String topic, String msg) throws MqttPersistenceException, MqttException {
		MqttMessage message = new MqttMessage(msg.getBytes());
		message.setQos(2);
		mqttClient.publish(topic, message);

	}
}