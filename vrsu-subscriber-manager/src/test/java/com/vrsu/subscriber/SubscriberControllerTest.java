package com.vrsu.subscriber;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.vrsu.subscriber.controller.SubscriberController;
import com.vrsu.subscriber.controller.WebSocketController;
import com.vrsu.subscriber.model.InputRequest;
import com.vrsu.subscriber.model.UserInfo;
import com.vrsu.subscriber.mqtt.MqttCredentials;
import com.vrsu.subscriber.response.GenericResponse;
import com.vrsu.subscriber.service.SubscriberService;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = VerizonApplication.class)
public class SubscriberControllerTest {

	@Autowired
	private MockMvc mockMvc;


	@InjectMocks
	SubscriberController subscriberController;
	@Autowired
	SubscriberService subscriberService;
	@InjectMocks
	WebSocketController websocketController;
	@MockBean
	MqttCredentials MqttCredentials;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}	
	@Test
	public void loginTest_Blank_Dataset() {
		UserInfo userInfo = new UserInfo();
		ResponseEntity<GenericResponse> responseEntity = subscriberController.login(userInfo, null, null);
		assertThat(responseEntity.getStatusCodeValue()).isEqualTo(500);
	}

	@Test
	public void loginTest_Incorrect_Dataset() {
		UserInfo userInfo = new UserInfo();
		userInfo.setUsername("administrator");
		userInfo.setPassword("password");
		ResponseEntity<GenericResponse> responseEntity = subscriberController.login(userInfo, null, null);
		assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
		assertThat(responseEntity.getBody().getMessage().equalsIgnoreCase("Invalid credentials")).isEqualTo(true);
	}

	@Test
	public void loginTest_Correct_Dataset() {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		UserInfo userInfo = new UserInfo();
		userInfo.setUsername("admin");
		userInfo.setPassword("admin");
		ResponseEntity<GenericResponse> responseEntity = subscriberController.login(userInfo, response, request);
		assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
		assertThat(responseEntity.getBody().getSessionId() != null);

	}
	@Test
	public void mqttConnectionCheckTest_Blank_Dataset() {
		InputRequest inpreq = new InputRequest();
		ResponseEntity<GenericResponse> responseEntity = subscriberController.mqttConnectionCheck(inpreq);
		assertThat(responseEntity.getStatusCodeValue()).isEqualTo(404);
		
	}

	@Test
	public void welcomeTest() {
		ResponseEntity<String> res = 	subscriberController.welcome();
		assertThat(res.getBody().contains("Welcome to VRSU")).isEqualTo(true);
	}
	@Test
	public void testcheckConnectivity() throws InterruptedException {
		websocketController.notify("{\"topicName\":\"REGIONAL/DYN/4/#\",\"isSubscribe\":true,\"filterName\":\"ALL\",\"sessionId\":\"3A337889130D78F14ACDC17BE5186670\"}");
		websocketController.checkConnectivity("3A337889130D78F14ACDC17BE5186670");
	}
	
}