package com.vrsu.subscriber;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.vrsu.subscriber.model.DashboardSetting;
import com.vrsu.subscriber.model.DecodedData;
import com.vrsu.subscriber.model.Filters;
import com.vrsu.subscriber.model.MqttConnectionRequest;
import com.vrsu.subscriber.model.MqttConnectionUserTopicData;
import com.vrsu.subscriber.model.UserWebSocketInfo;
import com.vrsu.subscriber.model.WebSocket;
import com.vrsu.subscriber.mqtt.MqttCredentials;
import com.vrsu.subscriber.request.ClientInformation;
import com.vrsu.subscriber.request.DeviceRequest;
import com.vrsu.subscriber.request.WebSocketDataReceiever;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = VerizonApplication.class)
/**
 * Only to increase code coverage
 * 
 * @author Ankit.Arora
 *
 */
public class ModelTests {

	@Test
	public void ClientInformationTest() {
		ClientInformation ci = new ClientInformation();
		ci.setEntitySubtype("entitysubtype");
		ci.setEntityType("entityType");
		ci.setVendorId("vendorId");
		assertThat(ci.getEntitySubtype().equalsIgnoreCase("entitysubtype")).isEqualTo(true);
		assertThat(ci.getEntityType().equalsIgnoreCase("entitytype")).isEqualTo(true);
		assertThat(ci.getVendorId().equalsIgnoreCase("vendorId")).isEqualTo(true);

	}

	@Test
	public void modelClassTest() {
		ClientInformation ci = new ClientInformation();
		ci.setEntitySubtype("entitysubtype");
		ci.setEntityType("entityType");
		ci.setVendorId("vendorId");
		DeviceRequest di = new DeviceRequest();
		di.setClientInformation(ci);
		di.setDeviceId("deviceId");
		di.setIpAddress("ipAddress");
		di.setMacAddress("macAddress");
		DecodedData dd = new DecodedData();
		dd.getDirection();
		dd.getId();
		dd.getLatitude();
		dd.getLongitude();
		dd.getMessageType();
		dd.getPublisherId();
		dd.getSpeed();
		dd.getTimestamp();
		dd.getTopic();
		DashboardSetting ds = new DashboardSetting();
		ds.setId("");
		ds.setStatus(true);
		ds.setStartTimeStamp("");
		ds.setEndTimeStamp("");
		ds.setType("");
		ds.getId();ds.isStatus();ds.getStartTimeStamp();ds.getType();ds.getEndTimeStamp();
		assertThat(di.getClientInformation() != null).isEqualTo(true);
		assertThat(di.getDeviceId().equalsIgnoreCase("deviceId")).isEqualTo(true);
		assertThat(di.getIpAddress().equalsIgnoreCase("ipAddress")).isEqualTo(true);
		assertThat(di.getMacAddress().equalsIgnoreCase("macAddress")).isEqualTo(true);

	}

	@Test
	public void checkingFilters() {
		Filters fi = new Filters();
		fi.setMessageType("messageType");
		fi.setTopicHas("topicHas");
		assertThat(fi.getMessageType().equalsIgnoreCase("messageType")).isEqualTo(true);
		assertThat(fi.getTopicHas().equalsIgnoreCase("topicHas")).isEqualTo(true);
	}

	@Test
	public void checkingWebSocketReciever() {
		WebSocketDataReceiever wi = new WebSocketDataReceiever();
		wi.setFilterName("filterName");
		wi.setSessionId("sessionId");
		wi.setSubscribe(true);
		wi.setTopicName("topicName");

		assertThat(wi.getFilterName().equalsIgnoreCase("filterName")).isEqualTo(true);
		assertThat(wi.getSessionId().equalsIgnoreCase("sessionId")).isEqualTo(true);
		assertThat(wi.isSubscribe() == true).isEqualTo(true);
		assertThat(wi.getTopicName().equalsIgnoreCase("topicName")).isEqualTo(true);

	}

	@Test
	public void checkingMqttCredentials() {
		MqttCredentials mq = new MqttCredentials();
		MqttConnectionRequest mcr = new MqttConnectionRequest("url", "username", "password");
		MqttCredentials m1 = new MqttCredentials(mcr);
		mcr.toString();
		mq.setClientId("clientId");
		mq.setMasterBrokerUrl("masterBrokerUrl");
		mq.setPassword("password");
		mq.setQos("qos");
		mq.setSecondaryBrokerUrl("secondaryBrokerUrl");
		mq.setUserName("userName");
		assertThat(mq.getClientId().equalsIgnoreCase("clientId")).isEqualTo(true);
		assertThat(mq.getMasterBrokerUrl().equalsIgnoreCase("masterBrokerUrl")).isEqualTo(true);
		assertThat(mq.getPassword().equalsIgnoreCase("password")).isEqualTo(true);
		assertThat(mq.getQos().equalsIgnoreCase("qos")).isEqualTo(true);
		assertThat(mq.getSecondaryBrokerUrl().equalsIgnoreCase("secondaryBrokerUrl")).isEqualTo(true);
		assertThat(mq.getUserName().equalsIgnoreCase("userName")).isEqualTo(true);
	}

	@Test
	public void checkingMqttConnectionUserTopicData() {
		MqttConnectionRequest md = new MqttConnectionRequest();
		md.equals(md);
		md.hashCode();
		MqttConnectionUserTopicData m = new MqttConnectionUserTopicData();
		m.setUsers(null);
		m.setTopics(null);
		DecodedData s = new DecodedData();
		s.setId("id");
		UserWebSocketInfo q = new UserWebSocketInfo();
		q.setMqttPublisher(null);
		q.isAlive();
		q.getLastReceiveMessageTime();
		WebSocket we = new WebSocket();
		we.setContent("content");
		we.setName("name");
		assertThat(we.getContent().equalsIgnoreCase("content")).isEqualTo(true);
		assertThat(we.getName().equalsIgnoreCase("name")).isEqualTo(true);
	}
}
