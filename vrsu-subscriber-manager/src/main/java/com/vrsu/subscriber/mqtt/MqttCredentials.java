package com.vrsu.subscriber.mqtt;

import org.springframework.stereotype.Component;

import com.vrsu.subscriber.model.MqttConnectionRequest;

import lombok.Getter;
import lombok.Setter;

@Component
@Getter
@Setter
public class MqttCredentials {

	private String masterBrokerUrl;
	private String secondaryBrokerUrl;
	private String clientId;
	private String userName;
	private String password;
	private String qos;
	
	public MqttCredentials() {
	}

	public MqttCredentials(MqttConnectionRequest mqttConnectionRequest) {
		this.masterBrokerUrl = mqttConnectionRequest.getUrl();
		this.userName = mqttConnectionRequest.getUsername();
		this.password = mqttConnectionRequest.getPassword();
	}
}