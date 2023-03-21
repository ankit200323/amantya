package com.vrsu.subscriber.service.impl;

import java.time.LocalDateTime;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.vrsu.subscriber.model.InputRequest;
import com.vrsu.subscriber.model.MqttConnectionRequest;
import com.vrsu.subscriber.model.MqttConnectionUserTopicData;
import com.vrsu.subscriber.model.TopicChange;
import com.vrsu.subscriber.model.UserWebSocketInfo;
import com.vrsu.subscriber.mqtt.MqttPacketListner;
import com.vrsu.subscriber.mqtt.MqttPublisher;
import com.vrsu.subscriber.request.WebSocketDataReceiever;
import com.vrsu.subscriber.response.GenericResponse;
import com.vrsu.subscriber.service.SubscriberService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SubscriberServiceImpl implements SubscriberService {

	@Autowired
	private SimpMessagingTemplate template;

	 @Autowired
	 private ObjectProvider<MqttPublisher> mqttPubObject;
	/**
	 * 
	 * @param sessionId - User Session ID
	 * @param topic     - MQTT topic to which User Subscribed
	 * @param filter    - User filter key
	 * @throws MqttException
	 */
	@Override
	public boolean addUserToSessions(String sessionId, String topic, String filter) throws MqttException {
		if (!StringUtils.isAnyBlank(sessionId, topic)) {
			if (UserDataStorage.getSessions().containsKey(sessionId)) {
				log.info("Update request received Session ID: " + sessionId);
				updateSession(sessionId, topic, filter);
				return true;
			} else {
				log.info("addUserToSessions :: Please login again");
				return false;
			}
		} else {
			log.debug("Input fields cannot be Empty");
			return false;
		}
	}

	@Override
	public void disconnectSession(String sessionId) {
		log.info("session Id " + sessionId);
		if (UserDataStorage.getSessions().containsKey(sessionId)) {
			UserWebSocketInfo session = UserDataStorage.getSessions().get(sessionId);
			log.info("data " + session);
			String topicName = session.getTopicSubscribed();
			log.info("topicName " + topicName);
			try {
				session.getMqttPublisher().unsubscribeTopic(topicName);
			} catch (MqttException e) {
				log.info("Error while disconnecting session " + e.getMessage());
			}
			session.setAlive(false);
			UserDataStorage.getSessions().remove(sessionId);

			checkAndRemoveTopicThatHasNoSession();
		}
	}

	private void checkAndRemoveTopicThatHasNoSession() {
	}

	private void updateSession(String sessionId, String topicName, String filterName) throws MqttException {
		log.info("Inside updateSession");
		UserWebSocketInfo sesson = UserDataStorage.getSessions().get(sessionId);
		if (Objects.nonNull(sesson)) {
			sesson.setAlive(true);
			sesson.setLastReceiveMessageTime(LocalDateTime.now());
			String previousTopic = sesson.getTopicSubscribed();
			String previousFilter = sesson.getFilterKey();
			log.info("Inside updateSession :: Previous topic " + previousTopic + "--" + previousFilter + "---");
			if (filterName.equalsIgnoreCase("Individual") || topicName.contains("#")) {
				sesson.setTopicSubscribed(topicName);
				sesson.setFilterKey(filterName);
				sesson.getMqttPublisher().subscribeTopic(sesson.getMqttConnectionRequest(), topicName);
				MqttConnectionRequest mqttConnectionRequest = sesson.getMqttConnectionRequest();
				if (UserDataStorage.getUserOnMqttConnection().containsKey(mqttConnectionRequest)) {
					UserDataStorage.getUserOnMqttConnection().get(mqttConnectionRequest).getUsers().add(sessionId);
					UserDataStorage.getUserOnMqttConnection().get(mqttConnectionRequest).getTopics().add(topicName);
				}
			} else {
				if (sesson.getTopicSubscribed() == null) {
					sesson.getMqttPublisher().subscribeTopic(sesson.getMqttConnectionRequest(), topicName);
				}
				sesson.setFilterKey(filterName);
			}

		} else {
			log.error("Inside updateSession Unable to retrieve session data " + sessionId);
		}
	}

	@Override
	public boolean checkAndDeleteOldSessions() {
		log.info("Inside checkAndDeleteOldSessions");
		LocalDateTime last6thMin = LocalDateTime.now().minusMinutes(6);
		if (UserDataStorage.getSessions() != null) {
			for (Entry<String, UserWebSocketInfo> session : UserDataStorage.getSessions().entrySet()) {
				String sessionId = session.getKey();
				boolean status = closeSession(session.getKey(), session.getValue(), last6thMin);

				if (status && StringUtils.isNotBlank(sessionId)) {
					UserDataStorage.getSessions().remove(sessionId);
				}
			}
			return true;
		}
		return false;
	}

	@Override
	public void notificationOperation(WebSocketDataReceiever data) {
		log.info("Inside notificationOperation");
		try {
			if (data.isSubscribe() && Objects.nonNull(data.getSessionId())) {
				addUserToSessions(data.getSessionId(), data.getTopicName(), data.getFilterName());
			}
		} catch (MqttException e) {
			log.info("error while notificate operation " + e.getMessage());
		}
	}

	@Override
	public void updateUserLastReceieveTime(String sessionId) {
		if (UserDataStorage.getSessions().containsKey(sessionId)) {
			UserDataStorage.getSessions().get(sessionId).setAlive(true);
			UserDataStorage.getSessions().get(sessionId).setLastReceiveMessageTime(LocalDateTime.now());
		}
	}

	@Override
	public GenericResponse checkMqttConnection(InputRequest inputRequest) {
		log.info("Inside SubscriberServiceImpl :: checkMqttConnection");
		MqttConnectionRequest mqttConnectionRequest = new MqttConnectionRequest(inputRequest.getUrl(),
				inputRequest.getUsername(), inputRequest.getPassword());
		if (UserDataStorage.getSessions().containsKey(inputRequest.getSessionId())) {
			log.info("SubscriberServiceImpl :: checkMqttConnection :: Existing User is changing the MQTT connection");
			if (checkIfNecessaryToUnload(inputRequest.getSessionId(), mqttConnectionRequest)) {
				unloadUserRequest(inputRequest.getSessionId(), mqttConnectionRequest);
				return loadUserRequest(inputRequest.getSessionId(), mqttConnectionRequest);
			} else {
				log.info("SubscriberServiceImpl :: checkMqttConnection :: Existing User is providing same information");
				return new GenericResponse("Existing User is providing same information", true);
			}
		} else {
			log.info("SubscriberServiceImpl :: checkMqttConnection :: New User request receieve");
			return loadUserRequest(inputRequest.getSessionId(), mqttConnectionRequest);
		}
	}

	private void unloadUserRequest(String sessionId, MqttConnectionRequest mqttConnectionRequest) {
		log.info("Inside SubscriberServiceImpl :: unloadUserRequest");
		UserWebSocketInfo user = UserDataStorage.getSessions().get(sessionId);

		removeUserFromPreviousMqttConnection(user.getMqttConnectionRequest(), sessionId);
		updateUserInfoAsNull(sessionId);
	}

	private void updateUserInfoAsNull(String sessionId) {
		UserWebSocketInfo user = UserDataStorage.getSessions().get(sessionId);
		user.setTopicSubscribed(null);
		user.setMqttConnectionRequest(null);
	}

	private void removeUserFromPreviousMqttConnection(MqttConnectionRequest mqttConnectionRequest, String sessionId) {
		if (UserDataStorage.getUserOnMqttConnection().containsKey(mqttConnectionRequest)) {
			log.info("RemoveUserFromPreviousMqttConnection :: Following MqttConnection Exist");
			MqttConnectionUserTopicData allData = UserDataStorage.getUserOnMqttConnection().get(mqttConnectionRequest);
			Set<String> userList = allData.getUsers();
			if (userList.contains(sessionId)) {
				userList.remove(sessionId);
				log.info("RemoveUserFromPreviousMqttConnection :: Successfully removed User ");
			}
			// check if MqttConnectionRequest Map is Empty and remove
			if (userList.isEmpty()) {
				log.info("RemoveUserFromPreviousMqttConnection :: MqttConnectionRequest can be removed");
				removeMqttConnection(mqttConnectionRequest, allData);
			} else {
				log.info("RemoveUserFromPreviousMqttConnection :: Other Session are also using Client");
			}
		} else {
			log.info("RemoveUserFromPreviousMqttConnection :: Following MqttConnection Does Not Exist");
		}
	}

	private void removeMqttConnection(MqttConnectionRequest mqttConnectionRequest,
			MqttConnectionUserTopicData allData) {
		MqttClient mqttClient = UserDataStorage.getMqttConnection().get(mqttConnectionRequest);
		for (String topic : allData.getTopics()) {
			try {
				mqttClient.unsubscribe(topic);
			} catch (MqttException e) {
				log.info("removeMqttConnection :: Failure while unsubscribe topic");
			}
		}
		UserDataStorage.getMqttConnection().remove(mqttConnectionRequest);
		UserDataStorage.getUserOnMqttConnection().remove(mqttConnectionRequest);
	}

	private GenericResponse loadUserRequest(String sessionId, MqttConnectionRequest mqttConnectionRequest) {
		boolean connectionCheck = UserDataStorage.getMqttConnection().containsKey(mqttConnectionRequest);
		if (connectionCheck) {
			log.info("SubscriberServiceImpl :: checkMqttConnection :: Utilizing previous Mqtt connection request");
			MqttClient mqttClient = UserDataStorage.getMqttConnection().get(mqttConnectionRequest);
			MqttPublisher mqtt = mqttPubObject.getObject();
			mqtt.setMqttClient(mqttClient);
			addUserToSessions(sessionId, mqttConnectionRequest, mqttClient);
			addUserToMqttConnection(mqttConnectionRequest, sessionId);
			return new GenericResponse("Successfully updated Mqtt connection", true);
		} else {
			log.info("SubscriberServiceImpl :: checkMqttConnection :: New Mqtt connection request");
			MqttPublisher mqtt = mqttPubObject.getObject();
			MqttClient mqttClient = mqtt.establishedConnection(mqttConnectionRequest);
			if (Objects.nonNull(mqttClient)) {
				addUserToSessions(sessionId, mqttConnectionRequest, mqttClient);
				UserDataStorage.getMqttConnection().put(mqttConnectionRequest, mqttClient);
				addUserToMqttConnection(mqttConnectionRequest, sessionId);
				return new GenericResponse("Successfully Added New Mqtt connection", true);
			} else {
				log.info("SubscriberServiceImpl :: checkMqttConnection :: Invalid MQTT credentials");
				return new GenericResponse("Invalid MQTT credentials.", false);
			}
		}
	}

	private void addUserToMqttConnection(MqttConnectionRequest mqttConnectionRequest, String sessionId) {
		log.info("Inside :: addUserToMqttConnection");
		if (UserDataStorage.getUserOnMqttConnection().containsKey(mqttConnectionRequest)) {
			log.info("addUserToMqttConnection :: already Exist");
			MqttConnectionUserTopicData data = UserDataStorage.getUserOnMqttConnection().get(mqttConnectionRequest);
			data.getUsers().add(sessionId);
		} else {
			MqttConnectionUserTopicData mqttConnectionUserTopicData = new MqttConnectionUserTopicData();
			UserDataStorage.getUserOnMqttConnection().put(mqttConnectionRequest, mqttConnectionUserTopicData);
			mqttConnectionUserTopicData.getUsers().add(sessionId);
		}
	}

	private boolean checkIfNecessaryToUnload(String sessionId, MqttConnectionRequest mqttConnectionRequest) {
		UserWebSocketInfo user = UserDataStorage.getSessions().get(sessionId);
		if (UserDataStorage.getMqttConnection().containsKey(user.getMqttConnectionRequest())) {
			MqttConnectionRequest previousMqttConnection = user.getMqttConnectionRequest();
			if (previousMqttConnection.equals(mqttConnectionRequest)) {
				log.info("checkIfNecessaryToUnload :: Previous and New MqttConnectionRequest is Same");
				return false;
			}
			return true;
		}
		return true;
	}

	private void addUserToSessions(String sessionId, MqttConnectionRequest mqttConnectionRequest,
			MqttClient mqttClient) {
		log.info("Adding new session " + sessionId);
		UserWebSocketInfo userWebSocketInfo = new UserWebSocketInfo();

		userWebSocketInfo.setSessionId(sessionId);
		userWebSocketInfo.setAlive(true);
		userWebSocketInfo.setLastReceiveMessageTime(LocalDateTime.now());
		userWebSocketInfo.setTemplate(template);
		userWebSocketInfo.getMqttPublisher().setMqttClient(mqttClient);
		userWebSocketInfo.setMqttConnectionRequest(mqttConnectionRequest);
		UserDataStorage.getSessions().put(sessionId, userWebSocketInfo);

	}

	@Override
	public void disconnect(String sessionId) {
		log.info("Inside disconnect");
		if (UserDataStorage.getSessions().containsKey(sessionId)) {
			UserWebSocketInfo userInfo = UserDataStorage.getSessions().get(sessionId);
			unloadUserRequest(sessionId, userInfo.getMqttConnectionRequest());

			UserDataStorage.getSessions().remove(sessionId);
		} else {
			log.info("User does not exist");
		}
	}

	private boolean closeSession(String sessionId, UserWebSocketInfo userWebSocketInfo, LocalDateTime last6thMin) {
		log.info("userWebSocketInfo.getLastReceiveMessageTime() " + userWebSocketInfo.getLastReceiveMessageTime());
		log.info("last6thMin " + last6thMin);
		if (userWebSocketInfo.getLastReceiveMessageTime().isBefore(last6thMin)) {
			log.info("Removing session ID " + sessionId);
			unloadUserRequest(userWebSocketInfo.getSessionId(), userWebSocketInfo.getMqttConnectionRequest());
			return true;
		} else {
			log.info("Session " + sessionId + " is still active!");
		}
		return false;
	}

	@Override
	public boolean updateUserTopic(TopicChange topicChange) {
		if (UserDataStorage.getSessions().containsKey(topicChange.getSessionId())) {
		UserWebSocketInfo user = UserDataStorage.getSessions().get(topicChange.getSessionId());
		
		String previousTopic = user.getTopicSubscribed();
		MqttConnectionUserTopicData userData = new MqttConnectionUserTopicData();
		user.setAlive(true);
		user.setLastReceiveMessageTime(LocalDateTime.now());
		user.setTopicSubscribed(topicChange.getTopicName());
		user.setFilterKey(topicChange.getFilterName());

		if (UserDataStorage.getUserOnMqttConnection().containsKey(user.getMqttConnectionRequest())) {
			userData = UserDataStorage.getUserOnMqttConnection().get(user.getMqttConnectionRequest());
			userData.getTopics().add(topicChange.getTopicName());
			log.info("successfully Added topic in MqttConnectionUserTopicData");
			
		}

		if (userData.getTopics().size() > 0) {
			Set<String> topics = userData.getTopics();
			String topicsStr[] = new String[topics.size()];
			int index = 0;
			for (String topic : topics) {
				topicsStr[index] = topic;
				index++;
			}
			try {
//				if (userData.getTopics().size() < 2) {
					user.getMqttPublisher().subscribeTopic(user.getMqttConnectionRequest(),
							topicChange.getTopicName());
////				} else {
//					user.getMqttPublisher().subscribeListTopic(user.getMqttConnectionRequest(), topicsStr);
////				}
			} catch (MqttException e) {
				user.setTopicSubscribed(previousTopic);
				previousTopic = null;
				log.info("SubscriberServiceImpl :: updateUserTopic :: Failure while updating topicChange" + e);
			return false;
			}
		}
		if(previousTopic != null && !previousTopic.equalsIgnoreCase(topicChange.getTopicName())
				&& user.getTopicSubscribed() != null) {
			unsubscribePreviousTopicWhenNotUsed(user.getMqttConnectionRequest(), previousTopic);
		}
	} else {
		log.info("SubscriberServiceImpl :: updateUserTopic :: User Does not exist");
		return false;
	}
	return true;}

	private void unsubscribePreviousTopicWhenNotUsed(MqttConnectionRequest mqttConnectionRequest,
			String previousTopic) {
		boolean flag = false;
		if (mqttConnectionRequest != null) {
			Set<String> users = UserDataStorage.getUserOnMqttConnection().get(mqttConnectionRequest).getUsers();
			for (String user : users) {
				if (UserDataStorage.getSessions().get(user).getTopicSubscribed().equalsIgnoreCase(previousTopic)) {
					flag = true;
				}
				if (flag) {
					break;
				}
			}
		}
		if (!flag) {
			// Now unsubscribe
			log.info("unsubscribe to topic " + previousTopic + " Since no one is using ");
			try {
				UserDataStorage.getMqttConnection().get(mqttConnectionRequest).unsubscribe(previousTopic);
				UserDataStorage.getUserOnMqttConnection().get(mqttConnectionRequest).getTopics().remove(previousTopic);
			} catch (MqttException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}