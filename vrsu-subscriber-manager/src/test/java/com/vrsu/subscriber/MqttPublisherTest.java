package com.vrsu.subscriber;

import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.vrsu.subscriber.model.MqttConnectionRequest;
import com.vrsu.subscriber.mqtt.MqttPublisher;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = VerizonApplication.class)
public class MqttPublisherTest {

	@Autowired
	MqttPublisher mqttPub;
	
	MqttClient mqttclient;
	@Test
	public void testEstablistConnection() throws MqttException
	{
		mqttclient = mqttPub.checkConnection(new MqttConnectionRequest("tcp://mqtt.vzmode-br.dltdemo.io:1883", "user3", "dfFg7sEX52BQ"));
		mqttclient=mqttPub.getMqttClient();
		mqttPub.checkConnection(new MqttConnectionRequest("tcp://mqtt.vzmode-br.dltdemo.io:1883", "user3", "dfFg7sEX52BQ"));
		mqttclient  = mqttPub.establishedConnection(new MqttConnectionRequest("tcp://mqtt.vzmode-br.dltdemo.io:1883", "user3", "dfFg7sEX52BQ"));
		assertThat(mqttclient!=null).isEqualTo(true);
		assertThat(mqttPub.isConnect()==true).isEqualTo(true);
		mqttPub.subscribeTopic(new MqttConnectionRequest("tcp://mqtt.vzmode-br.dltdemo.io:1883", "user3", "dfFg7sEX52BQ"), "ABC");
		mqttPub.setMqttClient(null);
		assertThat(mqttPub.isConnect()==false).isEqualTo(true);
		mqttPub.subscribeTopic(new MqttConnectionRequest("tcp://mqtt.vzmode-br.dltdemo.io:1883", "user3", "dfFg7sEX52BQ"), "ABC");
		mqttclient  = mqttPub.establishedConnection(new MqttConnectionRequest("tcp://mqtt.vzmode-br.dltdemo.io:1883", "user3", "dfFg7sEX52BQ"));
		mqttPub.unsubscribeTopic("ABC");
		mqttPub.publishMessage("ABC", "TESTING");
		mqttPub.disconnect();
		
	}
}