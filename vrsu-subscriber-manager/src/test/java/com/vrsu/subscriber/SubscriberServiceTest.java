package com.vrsu.subscriber;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.websocket.CloseReason;
import javax.websocket.Extension;
import javax.websocket.MessageHandler;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import javax.websocket.MessageHandler.Partial;
import javax.websocket.MessageHandler.Whole;
import javax.websocket.RemoteEndpoint.Async;
import javax.websocket.RemoteEndpoint.Basic;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketExtension;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.google.gson.Gson;
import com.vrsu.subscriber.model.DecodedData;
import com.vrsu.subscriber.model.Filters;
import com.vrsu.subscriber.model.InputRequest;
import com.vrsu.subscriber.model.TopicChange;
import com.vrsu.subscriber.mqtt.MqttPublisher;
import com.vrsu.subscriber.response.GenericResponse;
import com.vrsu.subscriber.schedule.MyScheduledTask;
import com.vrsu.subscriber.service.SubscriberService;
import com.vrsu.subscriber.service.impl.DecodedDataServiceImpl;
import com.vrsu.subscriber.service.impl.SubscriberServiceImpl;
import com.vrsu.subscriber.socketconfig.ServerWebSocketHandler;
import com.vrsu.subscriber.socketconfig.WebSocketConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = VerizonApplication.class)
public class SubscriberServiceTest {

	@Autowired
	SubscriberService subscriberService;
	
	@Autowired
	DecodedDataServiceImpl serv;
	@Autowired
	MqttPublisher mqtt;
	
	@Autowired
	ServerWebSocketHandler serverWebSocket;
	@Autowired
	MyScheduledTask myScT;
	@Mock
	WebSocketHandlerRegistry registry;

	@Test
	public void mqttConnectionCheckTest_InCorrect_Dataset1() throws Exception {
		
		GenericResponse response = new GenericResponse("", true); 
		 Gson gson = new Gson();
		InputRequest inpreq = new InputRequest();
		inpreq.setSessionId("A5F483E3C0D2461B2F508BF60870CBB6");
		inpreq.setUsername("user13");
		inpreq.setPassword("dfFg7sEX52BQ");
		inpreq.setUrl("tcp://mqtt.vzmode-br.dltdemo.io:1883");
//		doCallRealMethod().when(subscriberService.checkMqttConnection(inpreq));
//		Mockito.when(subscriberService.checkMqttConnection(inpreq)).thenReturn(null);
		GenericResponse responseEntity = subscriberService.checkMqttConnection(inpreq);
		assertThat(responseEntity.getMessage().equalsIgnoreCase("Invalid MQTT credentials.")).isEqualTo(true);
		assertThat(responseEntity.isStatus()).isEqualTo(false);
	}




	


	@Test
	public void FetchData() {
		
		Filters filter = new Filters();Pageable pageable = PageRequest.of(1, 10);
		LocalDateTime start = LocalDateTime.parse("2023-01-01T14:02:35.00", DateTimeFormatter.ISO_DATE_TIME);
		LocalDateTime end = LocalDateTime.parse("2023-02-09T14:03:17.00", DateTimeFormatter.ISO_DATE_TIME);
		Page<DecodedData> dd =null;
		filter.setMessageType("PSM");
		filter.setTopicHas("VZ/UPER");
		dd = serv.finddataByTopicAndMessage(start,end ,filter.getMessageType(),filter.getTopicHas(),pageable);
		assertThat(dd!=null).isEqualTo(true);
		
		filter.setMessageType("");
		filter.setTopicHas("");
		dd = serv.findByTimestampBetween(start,end,pageable);
		assertThat(dd!=null).isEqualTo(true);
		
		filter.setMessageType("PSM");
		filter.setTopicHas("");
		dd = serv.findByTimestampBetweenAndMessageType(start,end ,filter.getMessageType(),pageable);
		assertThat(dd!=null).isEqualTo(true);
		
		filter.setMessageType("");
		filter.setTopicHas("VZ/UPER");
		dd = serv.finddatabyTopic(start,end ,filter.getTopicHas(),pageable);
		assertThat(dd!=null).isEqualTo(true);
	}
	@Test
	public void TestregisterWebSocketHandlers() {
		WebSocketConfig web = new WebSocketConfig();
		web.registerWebSocketHandlers(registry);
	}
	
	@Test
	public void testServerWebSocketHandler () throws Exception {
		Session sess = new Session() {
			
			@Override
			public void setMaxTextMessageBufferSize(int max) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void setMaxIdleTimeout(long timeout) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void setMaxBinaryMessageBufferSize(int max) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void removeMessageHandler(MessageHandler listener) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public boolean isSecure() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean isOpen() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public Map<String, Object> getUserProperties() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Principal getUserPrincipal() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public URI getRequestURI() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Map<String, List<String>> getRequestParameterMap() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public String getQueryString() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public String getProtocolVersion() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Map<String, String> getPathParameters() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Set<Session> getOpenSessions() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public String getNegotiatedSubprotocol() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public List<Extension> getNegotiatedExtensions() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Set<MessageHandler> getMessageHandlers() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public int getMaxTextMessageBufferSize() {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public long getMaxIdleTimeout() {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public int getMaxBinaryMessageBufferSize() {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public String getId() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public WebSocketContainer getContainer() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Basic getBasicRemote() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Async getAsyncRemote() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public void close(CloseReason closeReason) throws IOException {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void close() throws IOException {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public <T> void addMessageHandler(Class<T> clazz, Whole<T> handler) throws IllegalStateException {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public <T> void addMessageHandler(Class<T> clazz, Partial<T> handler) throws IllegalStateException {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void addMessageHandler(MessageHandler handler) throws IllegalStateException {
				// TODO Auto-generated method stub
				
			}
		};
		WebSocketSession s = new WebSocketSession() {
			
			@Override
			public void setTextMessageSizeLimit(int messageSizeLimit) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void setBinaryMessageSizeLimit(int messageSizeLimit) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void sendMessage(WebSocketMessage<?> message) throws IOException {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public boolean isOpen() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public URI getUri() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public int getTextMessageSizeLimit() {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public InetSocketAddress getRemoteAddress() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Principal getPrincipal() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public InetSocketAddress getLocalAddress() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public String getId() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public HttpHeaders getHandshakeHeaders() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public List<WebSocketExtension> getExtensions() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public int getBinaryMessageSizeLimit() {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public Map<String, Object> getAttributes() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public String getAcceptedProtocol() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public void close(CloseStatus status) throws IOException {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void close() throws IOException {
				// TODO Auto-generated method stub
				
			}
		};
		serverWebSocket.showTime(sess);
		serverWebSocket.afterConnectionEstablished(s);
		serverWebSocket.afterConnectionClosed(s,null);
		byte[] a = {'d','d'};
		TextMessage message = new TextMessage(a);  
		serverWebSocket.handleTextMessage(s, message);
		serverWebSocket.handleTransportError(null, null);
	}
	@Test
public void testschedule() {
	LocalTime now = LocalTime.now();
	LocalTime end = LocalTime.of(now.getHour(), now.getMinute()+1);
	try {
		myScT.getCronExpression(now, end);
		assertThat(true).isEqualTo(true);
	} catch (SchedulerException e) {
		assertThat(false).isEqualTo(true);
		e.printStackTrace();
	}
}
}