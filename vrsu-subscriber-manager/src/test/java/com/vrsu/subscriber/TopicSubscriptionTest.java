package com.vrsu.subscriber;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.vrsu.subscriber.controller.SubscriberController;
import com.vrsu.subscriber.model.InputRequest;
import com.vrsu.subscriber.model.MqttConnectionRequest;
import com.vrsu.subscriber.model.TopicChange;
import com.vrsu.subscriber.mqtt.MqttPublisher;
import com.vrsu.subscriber.response.GenericResponse;
import com.vrsu.subscriber.service.SubscriberService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = VerizonApplication.class)
public class TopicSubscriptionTest {

	@Autowired
	SubscriberController subscriberController;

	@Autowired
	SubscriberService subscriberService;

	@Autowired
	MqttPublisher mqtt;

	List<String> topicList = new ArrayList<String>();

	// execute some code that invokes the method on classUnderTest
	@Before
	public void setup() {
		MqttConnectionRequest req = new MqttConnectionRequest();
		req.setUsername("User3");
		req.setPassword("dfFg7sEX52BQ");
		req.setUrl("tcp://mqtt.vzmode-br.dltdemo.io:1883");
//		topicList.add("gvtgvtvtvtgv");

		topicList.add("REGIONAL/DYN/4/#");
		topicList.add("REGIONAL/DYN/3/#");
		topicList.add("VZCV2X/3/IN/#");
		topicList.add("REGIONAL/DYN/4/+/+/+/+/+/+/+/+/+/+/+/JER/#");
		topicList.add("REGIONAL/DYN/4/+/+/+/+/+/+/+/+/+/+/EMULATOR/#");
		topicList.add("REGIONAL/DYN/4/+/+/+/+/+/+/+/+/+/+/EMULATOR/JER/#");
		topicList.add("REGIONAL/DYN/4/+/+/+/+/+/+/+/+/+/+/EMULATOR/UPER/#");

	}

	/**
	 * Subscribe to topic
	 * 
	 * @throws InterruptedException
	 */

	public void SubscribeToTopicTest(String topic) throws InterruptedException {
		InputRequest inputRequest = new InputRequest();
		inputRequest.setUsername("user3");
		inputRequest.setPassword("dfFg7sEX52BQ");
		inputRequest.setUrl("tcp://mqtt.vzmode-br.dltdemo.io:1883");
		inputRequest.setSessionId("TESTSESSIONID1234");
		ResponseEntity<GenericResponse> response = subscriberController.mqttConnectionCheck(inputRequest);
		TopicChange tc = new TopicChange();
		tc.setFilterName("ALL");
		tc.setSessionId("TESTSESSIONID1234");
		ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outContent));

		tc.setTopicName(topic);
		subscriberController.topicUpdate(tc);
		Thread.sleep(500);
		log.info("outContent.toString().contains(\"MESSAGETESTSESSIONID1234\") value is {} ",
				outContent.toString().contains("MESSAGETESTSESSIONID1234"));

		assertThat(outContent.toString().contains("MESSAGETESTSESSIONID1234")).isEqualTo(true);
	}

	@Test
	public void SubscribeToTopicTest0() throws InterruptedException {
		SubscribeToTopicTest(topicList.get(0));
	}

	@Test
	public void SubscribeToTopicTest1() throws InterruptedException {
		SubscribeToTopicTest(topicList.get(1));
	}

	@Test
	public void SubscribeToTopicTest2() throws InterruptedException {
		SubscribeToTopicTest(topicList.get(2));
	}

	@Test
	public void SubscribeToTopicTest3() throws InterruptedException {
		SubscribeToTopicTest(topicList.get(3));
	}

	@Test
	public void SubscribeToTopicTest4() throws InterruptedException {
		SubscribeToTopicTest(topicList.get(4));
	}

	@Test
	public void SubscribeToTopicTest5() throws InterruptedException {
		SubscribeToTopicTest(topicList.get(5));
	}

	@Test
	public void SubscribeToTopicTest6() throws InterruptedException {
		SubscribeToTopicTest(topicList.get(6));
	}
}
