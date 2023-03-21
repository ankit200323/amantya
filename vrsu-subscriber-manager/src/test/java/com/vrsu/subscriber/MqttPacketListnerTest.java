package com.vrsu.subscriber;

import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.vrsu.subscriber.model.MqttConnectionRequest;
import com.vrsu.subscriber.model.UserWebSocketInfo;
import com.vrsu.subscriber.mqtt.MqttPacketListner;
import com.vrsu.subscriber.repository.DecodedDataRepo;
import com.vrsu.subscriber.service.DecodedDataService;
import com.vrsu.subscriber.utility.Constants;
import com.vrsu.subscriber.utility.DecoderUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = VerizonApplication.class)
public class MqttPacketListnerTest {

	@Autowired
	MqttPacketListner packetlistner;

	@Autowired
	DecoderUtils utils;

	@Autowired
	DecodedDataService serv;

	@Autowired
	DecodedDataRepo repo;

	@Before
	public void setup() {
	}

	public void mqttPacketListenerTestcorrectDataSet(String payload, String topic, boolean result) throws Exception {
		MqttMessage message = new MqttMessage(payload.getBytes());
		String sessionId = "A5F483E3C0D2461B2F508BF60870CBB6";
//		packetlistner.messageArrived(topic, message);
		UserWebSocketInfo userSession = new UserWebSocketInfo();
		MessageChannel m = new MessageChannel() {

			@Override
			public boolean send(Message<?> message, long timeout) {

				return true;
			}
		};
		SimpMessagingTemplate template = new SimpMessagingTemplate(m);
		userSession.setTemplate(template);
		userSession.setTopicSubscribed(topic);
		userSession.setFilterKey(Constants.ALL);
		userSession.setSessionId(sessionId);
		boolean result1 = packetlistner.processData(userSession, topic, message, sessionId);
		assertThat(result1).isEqualTo(result);
	}

	public void mqttPacketListenerTestcorrectDataSetWithDifferentFilterKey(String payload, String topic, boolean result,
			String filterKey) throws Exception {
		MqttMessage message = new MqttMessage(payload.getBytes());
		String sessionId = "A5F483E3C0D2461B2F508BF60870CBB6";
//		packetlistner.messageArrived(topic, message);
		UserWebSocketInfo userSession = new UserWebSocketInfo();
		MessageChannel m = new MessageChannel() {

			@Override
			public boolean send(Message<?> message, long timeout) {

				return true;
			}
		};
		SimpMessagingTemplate template = new SimpMessagingTemplate(m);
		userSession.setTemplate(template);
		userSession.setTopicSubscribed(topic);
		userSession.setFilterKey(filterKey);
		userSession.setSessionId(sessionId);
		boolean result1 = packetlistner.processData(userSession, topic, message, sessionId);
		assertThat(result1).isEqualTo(result);
	}

	@Test
	public void mqttPacketListenerTestcorrectDataSetSpecificRegionalSAMP3JERPSM() throws Exception {
		mqttPacketListenerTestcorrectDataSet(
				"{\"MessageFrame\": {\"messageId\": \"32\", \"value\": {\"PersonalSafetyMessage\": {\"heading\": \"20000\", \"basicType\": {\"aPEDESTRIAN\": \"\"}, \"secMark\": \"20000\", \"msgCnt\": \"100\", \"id\": \"00 00 D8 7F\", \"position\": {\"lat\": \"582813000\", \"long\": \"-1345110400\"}, \"accuracy\": {\"semiMajor\": \"255\", \"semiMinor\": \"255\", \"orientation\": \"65535\"}, \"speed\": \"8000\"}}}}",
				"REGIONAL/SAMP/3/c/4/2/7/e/1/u/1/SW/NA/EMULATOR/JER/PSM", true);
	}

	/**
	 * In above case we are passing Topic = RegionalSAMP3JERPSM and messageFreame =
	 * PSM it retrun true;
	 */
	@Test
	public void mqttPacketListenerTestcorrectDataSetRegionalDYN4AllDemoPSM() throws Exception {
		mqttPacketListenerTestcorrectDataSet(
				"{\"MessageFrame\": {\"messageId\": \"32\", \"value\": {\"PersonalSafetyMessage\": {\"position\": {\"lat\": \"582813000\", \"long\": \"-1345110400\"}, \"accuracy\": {\"orientation\": \"65535\", \"semiMajor\": \"255\", \"semiMinor\": \"255\"}, \"speed\": \"8000\", \"heading\": \"20000\", \"basicType\": {\"aPEDESTRIAN\": \"\"}, \"secMark\": \"20000\", \"msgCnt\": \"100\", \"id\": \"00 00 D8 7F\"}}}}",
				"REGIONAL/DYN/4/#", false);
	}

	/**
	 * In above case we are passing Topic = RegionalDYN4All and messageFreame = PSM
	 * it retrun false as here we are not passing fulll topic;
	 */
	@Test
	public void mqttPacketListenerTestcorrectDataSetRegionalSpecificDYN4JERPSM() throws Exception {
		mqttPacketListenerTestcorrectDataSet(
				"{\"MessageFrame\": {\"messageId\": \"32\", \"value\": {\"PersonalSafetyMessage\": {\"position\": {\"lat\": \"582813000\", \"long\": \"-1345110400\"}, \"accuracy\": {\"orientation\": \"65535\", \"semiMajor\": \"255\", \"semiMinor\": \"255\"}, \"speed\": \"8000\", \"heading\": \"20000\", \"basicType\": {\"aPEDESTRIAN\": \"\"}, \"secMark\": \"20000\", \"msgCnt\": \"100\", \"id\": \"00 00 D8 7F\"}}}}",
				"REGIONAL/DYN/4/c/4/2/7/e/1/u/1/SW/NA/EMULATOR/JER/PSM", true);
	}

	/**
	 * In above case we are passing Topic = RegionalDYN4JERPSM and messageFreame =
	 * PSM it retrun true
	 */
	@Test
	public void mqttPacketListenerTestcorrectDataSetRegionalDYN4AllDemoBSM() throws Exception {
		mqttPacketListenerTestcorrectDataSet(
				"{\"MessageFrame\": {\"messageId\": \"20\", \"value\": {\"BasicSafetyMessage\": {\"coreData\": {\"secMark\": \"2415\", \"lat\": \"582813000\", \"speed\": \"8000\", \"heading\": \"28000\", \"angle\": \"127\", \"accelSet\": {\"long\": \"2001\", \"lat\": \"2001\", \"vert\": \"-127\", \"yaw\": \"0\"}, \"msgCnt\": \"120\", \"id\": \"00 00 D8 7F\", \"brakes\": {\"wheelBrakes\": \"00000\", \"traction\": {\"unavailable\": \"\"}, \"abs\": {\"unavailable\": \"\"}, \"scs\": {\"unavailable\": \"\"}, \"brakeBoost\": {\"unavailable\": \"\"}, \"auxBrakes\": {\"unavailable\": \"\"}}, \"size\": {\"width\": \"1000\", \"length\": \"4000\"}, \"transmission\": {\"unavailable\": \"\"}, \"long\": \"-1345110400\", \"elev\": \"-4096\", \"accuracy\": {\"orientation\": \"65535\", \"semiMajor\": \"255\", \"semiMinor\": \"255\"}}}}}}",
				"REGIONAL/DYN/4/#", false);
	}

	/**
	 * In above case we are passing Topic = RegionalDYN4All and messageFreame = BSM
	 * it retrun false as here we are not passing fulll topic;
	 */
	@Test
	public void mqttPacketListenerTestcorrectDataSetSpecificRegionalDYN4JERBSM() throws Exception {
		mqttPacketListenerTestcorrectDataSet(
				"{\"MessageFrame\": {\"messageId\": \"20\", \"value\": {\"BasicSafetyMessage\": {\"coreData\": {\"secMark\": \"2415\", \"lat\": \"582813000\", \"speed\": \"8000\", \"heading\": \"28000\", \"angle\": \"127\", \"accelSet\": {\"long\": \"2001\", \"lat\": \"2001\", \"vert\": \"-127\", \"yaw\": \"0\"}, \"msgCnt\": \"120\", \"id\": \"00 00 D8 7F\", \"brakes\": {\"wheelBrakes\": \"00000\", \"traction\": {\"unavailable\": \"\"}, \"abs\": {\"unavailable\": \"\"}, \"scs\": {\"unavailable\": \"\"}, \"brakeBoost\": {\"unavailable\": \"\"}, \"auxBrakes\": {\"unavailable\": \"\"}}, \"size\": {\"width\": \"1000\", \"length\": \"4000\"}, \"transmission\": {\"unavailable\": \"\"}, \"long\": \"-1345110400\", \"elev\": \"-4096\", \"accuracy\": {\"orientation\": \"65535\", \"semiMajor\": \"255\", \"semiMinor\": \"255\"}}}}}}",
				"REGIONAL/DYN/4/c/4/2/7/e/1/u/1/SW/NA/EMULATOR/JER/BSM", true);
	}

	/**
	 * In above case we are passing Topic = RegionalDYN4JERBSM and messageFreame =
	 * BSM it retrun true;
	 */
	@Test
	public void mqttPacketListenerTestcorrectDataSetRegionalDYN4AllDemoEVA() throws Exception {
		mqttPacketListenerTestcorrectDataSet(
				"{\"MessageFrame\": {\"messageId\": \"27\", \"value\": {\"RoadSideAlert\": {\"position\": {\"long\": \"-1345110400\", \"lat\": \"582813000\", \"elevation\": \"100\", \"heading\": \"30\"}, \"furtherInfoID\": \"00 7B\", \"msgCnt\": \"123\", \"timeStamp\": \"123456\", \"typeEvent\": \"535\", \"description\": {\"ITIScodes\": [\"1025\", \"12545\", \"8711\"]}, \"heading\": \"0000000000000100\", \"extent\": {\"useFor100000meters\": \"\"}}}}}",
				"REGIONAL/DYN/4/#", false);
	}

	/**
	 * In above case we are passing Topic = RegionalDYN4All and messageFreame = EVA
	 * it retrun false as here we are not passing fulll topic;
	 */
	@Test
	public void mqttPacketListenerTestcorrectDataSetSpecificRegionalDYN4JEREVA() throws Exception {
		mqttPacketListenerTestcorrectDataSet(
				"{\"MessageFrame\":{\"messageId\":\"22\",\"value\":{\"EmergencyVehicleAlert\":{\"id\":\"0000055D\",\"rsaMsg\":{\"msgCnt\":\"0\",\"typeEvent\":\"9729\",\"position\":{\"long\":\"772701595\",\"lat\":\"301392707\",\"elevation\":\"0\",\"heading\":\"267\",\"speed\":{\"transmisson\":{\"unavailable\":\"\"},\"speed\":\"0\"}}}}}}}",
				"REGIONAL/DYN/4/t/t/q/g/q/q/d/m/SW/NA/VZ/JER/EVA", true);
	}

	/**
	 *
	 * In above case we are passing Topic = RegionalDYN4JEREVA and messageFreame =
	 * EVA it retrun true;
	 */
	@Test
	public void mqttPacketListenerTestcorrectDataSetRegionalDYN3AllDemoJERPSM() throws Exception {
		mqttPacketListenerTestcorrectDataSet(
				"{\"MessageFrame\": {\"messageId\": \"32\", \"value\": {\"PersonalSafetyMessage\": {\"secMark\": \"20000\", \"msgCnt\": \"100\", \"id\": \"00 00 D8 7F\", \"position\": {\"lat\": \"582813000\", \"long\": \"-1345110400\"}, \"accuracy\": {\"semiMajor\": \"255\", \"semiMinor\": \"255\", \"orientation\": \"65535\"}, \"speed\": \"8000\", \"heading\": \"20000\", \"basicType\": {\"aPEDESTRIAN\": \"\"}}}}}",
				"REGIONAL/DYN/3/c/4/2/7/e/1/u/1/SW/NA/EMULATOR/JER/PSM", true);
	}

	/**
	 * In above case we are passing Topic = RegionalDYN3All and messageFreame = PSM
	 * it retrun false as here we are not passing fulll topic;
	 */
	@Test
	public void mqttPacketListenerTestcorrectDataSetSpecificRegionalDYN3JERPSM() throws Exception {
		mqttPacketListenerTestcorrectDataSet(
				"{\"MessageFrame\": {\"messageId\": \"32\", \"value\": {\"PersonalSafetyMessage\": {\"secMark\": \"20000\", \"msgCnt\": \"100\", \"id\": \"00 00 D8 7F\", \"position\": {\"lat\": \"582813000\", \"long\": \"-1345110400\"}, \"accuracy\": {\"semiMajor\": \"255\", \"semiMinor\": \"255\", \"orientation\": \"65535\"}, \"speed\": \"8000\", \"heading\": \"20000\", \"basicType\": {\"aPEDESTRIAN\": \"\"}}}}}",
				"REGIONAL/DYN/3/c/4/2/7/e/1/u/1/SW/NA/EMULATOR/JER/PSM", true);
	}

	/**
	 * In above case we are passing Topic = RegionalDYN3JERPSM and messageFreame =
	 * PSM it retrun true;
	 */
	@Test
	public void mqttPacketListenerTestcorrectDataSetRegionalDYN3DemoJERBSM() throws Exception {
		mqttPacketListenerTestcorrectDataSet(
				"{\"MessageFrame\": {\"messageId\": \"20\", \"value\": {\"BasicSafetyMessage\": {\"coreData\": {\"long\": \"-1345110400\", \"elev\": \"-4096\", \"transmission\": {\"unavailable\": \"\"}, \"id\": \"00 00 D8 7F\", \"accuracy\": {\"semiMajor\": \"255\", \"semiMinor\": \"255\", \"orientation\": \"65535\"}, \"heading\": \"28000\", \"brakes\": {\"scs\": {\"unavailable\": \"\"}, \"brakeBoost\": {\"unavailable\": \"\"}, \"auxBrakes\": {\"unavailable\": \"\"}, \"wheelBrakes\": \"00000\", \"traction\": {\"unavailable\": \"\"}, \"abs\": {\"unavailable\": \"\"}}, \"angle\": \"127\", \"size\": {\"length\": \"4000\", \"width\": \"1000\"}, \"msgCnt\": \"120\", \"secMark\": \"2415\", \"lat\": \"582813000\", \"speed\": \"8000\", \"accelSet\": {\"vert\": \"-127\", \"yaw\": \"0\", \"long\": \"2001\", \"lat\": \"2001\"}}}}}}",
				"REGIONAL/DYN/3/c/4/2/7/e/1/u/1/SW/NA/EMULATOR/JER/BSM", true);
	}

	/**
	 * In above case we are passing Topic = RegionalDYN3All and messageFreame = BSM
	 * it retrun false as here we are not passing fulll topic;
	 */
	@Test
	public void mqttPacketListenerTestcorrectDataSetSpecificRegionalDYN3JERBSM() throws Exception {
		mqttPacketListenerTestcorrectDataSet(
				"{\"MessageFrame\": {\"messageId\": \"20\", \"value\": {\"BasicSafetyMessage\": {\"coreData\": {\"long\": \"-1345110400\", \"elev\": \"-4096\", \"transmission\": {\"unavailable\": \"\"}, \"id\": \"00 00 D8 7F\", \"accuracy\": {\"semiMajor\": \"255\", \"semiMinor\": \"255\", \"orientation\": \"65535\"}, \"heading\": \"28000\", \"brakes\": {\"scs\": {\"unavailable\": \"\"}, \"brakeBoost\": {\"unavailable\": \"\"}, \"auxBrakes\": {\"unavailable\": \"\"}, \"wheelBrakes\": \"00000\", \"traction\": {\"unavailable\": \"\"}, \"abs\": {\"unavailable\": \"\"}}, \"angle\": \"127\", \"size\": {\"length\": \"4000\", \"width\": \"1000\"}, \"msgCnt\": \"120\", \"secMark\": \"2415\", \"lat\": \"582813000\", \"speed\": \"8000\", \"accelSet\": {\"vert\": \"-127\", \"yaw\": \"0\", \"long\": \"2001\", \"lat\": \"2001\"}}}}}}",
				"REGIONAL/DYN/3/c/4/2/7/e/1/u/1/SW/NA/EMULATOR/JER/BSM", true);
	}

	/**
	 * In above case we are passing Topic = RegionalDYN3JERPSM and messageFreame =
	 * BSM it retrun true;
	 */
	@Test
	public void mqttPacketListenerTestcorrectDataSetRegionalDYN3DemoJEREVA() throws Exception {
		mqttPacketListenerTestcorrectDataSet(
				"{\"MessageFrame\": {\"messageId\": \"27\", \"value\": {\"RoadSideAlert\": {\"typeEvent\": \"535\", \"description\": {\"ITIScodes\": [\"1025\", \"12545\", \"8711\"]}, \"heading\": \"0000000000000100\", \"extent\": {\"useFor100000meters\": \"\"}, \"position\": {\"lat\": \"582813000\", \"elevation\": \"100\", \"heading\": \"30\", \"long\": \"-1345110400\"}, \"furtherInfoID\": \"00 7B\", \"msgCnt\": \"123\", \"timeStamp\": \"123456\"}}}}",
				"REGIONAL/DYN/3/c/4/2/7/e/1/u/1/SW/NA/EMULATOR/JER/RSA", false);
	}

	/**
	 * In above case we are passing Topic = RegionalDYN3All and messageFreame = BSM
	 * it retrun false as here we are not passing fulll topic;
	 */
	@Test
	public void mqttPacketListenerTestcorrectDataSetSpecificRegionalDYN3JEREVA() throws Exception {
		mqttPacketListenerTestcorrectDataSet(
				"{\"MessageFrame\": {\"messageId\": \"27\", \"value\": {\"RoadSideAlert\": {\"typeEvent\": \"535\", \"description\": {\"ITIScodes\": [\"1025\", \"12545\", \"8711\"]}, \"heading\": \"0000000000000100\", \"extent\": {\"useFor100000meters\": \"\"}, \"position\": {\"lat\": \"582813000\", \"elevation\": \"100\", \"heading\": \"30\", \"long\": \"-1345110400\"}, \"furtherInfoID\": \"00 7B\", \"msgCnt\": \"123\", \"timeStamp\": \"123456\"}}}}",
				"REGIONAL/DYN/3/c/4/2/7/e/1/u/1/SW/NA/EMULATOR/JER/RSA", false);
	}

	/**
	 * In above case we are passing Topic = RegionalDYN3JEREVA and messageFreame =
	 * EVA it retrun true;
	 */
	@Test
	public void mqttPacketListenerTestcorrectDataSetRegionalSAMP3DemoJERPSM() throws Exception {
		mqttPacketListenerTestcorrectDataSet(
				"{\"MessageFrame\": {\"messageId\": \"32\", \"value\": {\"PersonalSafetyMessage\": {\"heading\": \"20000\", \"basicType\": {\"aPEDESTRIAN\": \"\"}, \"secMark\": \"20000\", \"msgCnt\": \"100\", \"id\": \"00 00 D8 7F\", \"position\": {\"lat\": \"582813000\", \"long\": \"-1345110400\"}, \"accuracy\": {\"semiMajor\": \"255\", \"semiMinor\": \"255\", \"orientation\": \"65535\"}, \"speed\": \"8000\"}}}}",
				"REGIONAL/SAMP/3/#", false);
	}

	/**
	 * In above case we are passing Topic = RegionalSMAP3All and messageFreame = PSM
	 * it retrun false as here we are not passing fulll topic;
	 */
	@Test
	public void mqttPacketListenerTestcorrectDataSetSpecificRegionalSAMP3JERPSM1() throws Exception {
		mqttPacketListenerTestcorrectDataSet(
				"{\"MessageFrame\": {\"messageId\": \"32\", \"value\": {\"PersonalSafetyMessage\": {\"heading\": \"20000\", \"basicType\": {\"aPEDESTRIAN\": \"\"}, \"secMark\": \"20000\", \"msgCnt\": \"100\", \"id\": \"00 00 D8 7F\", \"position\": {\"lat\": \"582813000\", \"long\": \"-1345110400\"}, \"accuracy\": {\"semiMajor\": \"255\", \"semiMinor\": \"255\", \"orientation\": \"65535\"}, \"speed\": \"8000\"}}}}",
				"REGIONAL/SAMP/3/c/4/2/7/e/1/u/1/SW/NA/EMULATOR/JER/PSM", true);

	}

	/**
	 * In above case we are passing Topic = RegionalSAMP3DemoJERPSM and
	 * messageFreame = PSM it retrun true;
	 */
	@Test
	public void mqttPacketListenerTestcorrectDataSetRegionalSAMP3JERBSM() throws Exception {
		mqttPacketListenerTestcorrectDataSet(
				"{\"MessageFrame\": {\"value\": {\"BasicSafetyMessage\": {\"coreData\": {\"secMark\": \"2415\", \"transmission\": {\"unavailable\": \"\"}, \"elev\": \"-4096\", \"accuracy\": {\"semiMajor\": \"255\", \"semiMinor\": \"255\", \"orientation\": \"65535\"}, \"heading\": \"28000\", \"angle\": \"127\", \"msgCnt\": \"120\", \"id\": \"00 00 D8 7F\", \"long\": \"-1345110400\", \"speed\": \"8000\", \"size\": {\"length\": \"4000\", \"width\": \"1000\"}, \"lat\": \"582813000\", \"accelSet\": {\"long\": \"2001\", \"lat\": \"2001\", \"vert\": \"-127\", \"yaw\": \"0\"}, \"brakes\": {\"traction\": {\"unavailable\": \"\"}, \"abs\": {\"unavailable\": \"\"}, \"scs\": {\"unavailable\": \"\"}, \"brakeBoost\": {\"unavailable\": \"\"}, \"auxBrakes\": {\"unavailable\": \"\"}, \"wheelBrakes\": \"00000\"}}}}, \"messageId\": \"20\"}}",
				"REGIONAL/SAMP/3/#", false);
	}

	/**
	 * In above case we are passing Topic = RegionalSMAP3All and messageFrame = BSM
	 * it retrun false as here we are not passing fulll topic;
	 */
	@Test
	public void mqttPacketListenerTestcorrectDataSetSpecificRegionalSAMP3JERBSM() throws Exception {
		mqttPacketListenerTestcorrectDataSet(
				"{\"MessageFrame\": {\"value\": {\"BasicSafetyMessage\": {\"coreData\": {\"secMark\": \"2415\", \"transmission\": {\"unavailable\": \"\"}, \"elev\": \"-4096\", \"accuracy\": {\"semiMajor\": \"255\", \"semiMinor\": \"255\", \"orientation\": \"65535\"}, \"heading\": \"28000\", \"angle\": \"127\", \"msgCnt\": \"120\", \"id\": \"00 00 D8 7F\", \"long\": \"-1345110400\", \"speed\": \"8000\", \"size\": {\"length\": \"4000\", \"width\": \"1000\"}, \"lat\": \"582813000\", \"accelSet\": {\"long\": \"2001\", \"lat\": \"2001\", \"vert\": \"-127\", \"yaw\": \"0\"}, \"brakes\": {\"traction\": {\"unavailable\": \"\"}, \"abs\": {\"unavailable\": \"\"}, \"scs\": {\"unavailable\": \"\"}, \"brakeBoost\": {\"unavailable\": \"\"}, \"auxBrakes\": {\"unavailable\": \"\"}, \"wheelBrakes\": \"00000\"}}}}, \"messageId\": \"20\"}}",
				"REGIONAL/SAMP/3/c/4/2/7/e/1/u/1/SW/NA/EMULATOR/JER/BSM", true);
	}

	/**
	 * In above case we are passing Topic = RegionalSAMP3JERBSM and messageFreame =
	 * BSM it retrun true;
	 */
	@Test
	public void mqttPacketListenerTestcorrectDataSetRegionalDemoSAMP4JERPSM() throws Exception {
		mqttPacketListenerTestcorrectDataSet(
				"{\"MessageFrame\": {\"messageId\": \"32\", \"value\": {\"PersonalSafetyMessage\": {\"position\": {\"lat\": \"582813000\", \"long\": \"-1345110400\"}, \"accuracy\": {\"semiMajor\": \"255\", \"semiMinor\": \"255\", \"orientation\": \"65535\"}, \"speed\": \"8000\", \"heading\": \"20000\", \"basicType\": {\"aPEDESTRIAN\": \"\"}, \"secMark\": \"20000\", \"msgCnt\": \"100\", \"id\": \"00 00 D8 7F\"}}}}",
				"REGIONAL/SAMP/4/#", false);
	}

	/**
	 * In above case we are passing Topic = RegionalSMAP4All and messageFrame = PSM
	 * it retrun false as here we are not passing fulll topic;
	 */
	@Test
	public void mqttPacketListenerTestcorrectDataSetSpecificRegionalSAMP4JERPSM() throws Exception {
		mqttPacketListenerTestcorrectDataSet(
				"{\"MessageFrame\": {\"messageId\": \"32\", \"value\": {\"PersonalSafetyMessage\": {\"position\": {\"lat\": \"582813000\", \"long\": \"-1345110400\"}, \"accuracy\": {\"semiMajor\": \"255\", \"semiMinor\": \"255\", \"orientation\": \"65535\"}, \"speed\": \"8000\", \"heading\": \"20000\", \"basicType\": {\"aPEDESTRIAN\": \"\"}, \"secMark\": \"20000\", \"msgCnt\": \"100\", \"id\": \"00 00 D8 7F\"}}}}",
				"REGIONAL/SAMP/4/c/4/2/7/e/1/u/1/SW/NA/EMULATOR/JER/PSM", true);
	}

	/**
	 * In above case we are passing Topic = RegionalSAMP4JERPSM and messageFrame =
	 * PSM it retrun true;
	 */
	@Test
	public void mqttPacketListenerTestcorrectDataSetRegionalSAMP4DemoJERBSM() throws Exception {
		mqttPacketListenerTestcorrectDataSet(
				"{\"MessageFrame\": {\"messageId\": \"20\", \"value\": {\"BasicSafetyMessage\": {\"coreData\": {\"lat\": \"582813000\", \"angle\": \"127\", \"size\": {\"width\": \"1000\", \"length\": \"4000\"}, \"brakes\": {\"wheelBrakes\": \"00000\", \"traction\": {\"unavailable\": \"\"}, \"abs\": {\"unavailable\": \"\"}, \"scs\": {\"unavailable\": \"\"}, \"brakeBoost\": {\"unavailable\": \"\"}, \"auxBrakes\": {\"unavailable\": \"\"}}, \"id\": \"00 00 D8 7F\", \"transmission\": {\"unavailable\": \"\"}, \"speed\": \"8000\", \"long\": \"-1345110400\", \"accuracy\": {\"semiMajor\": \"255\", \"semiMinor\": \"255\", \"orientation\": \"65535\"}, \"heading\": \"28000\", \"accelSet\": {\"vert\": \"-127\", \"yaw\": \"0\", \"long\": \"2001\", \"lat\": \"2001\"}, \"msgCnt\": \"120\", \"secMark\": \"2415\", \"elev\": \"-4096\"}}}}}",
				"REGIONAL/SAMP/4/#", false);
	}

	/**
	 * In above case we are passing Topic = RegionalSMAP4All and messageFrame = BSM
	 * it retrun false as here we are not passing fulll topic;
	 */
	@Test
	public void mqttPacketListenerTestcorrectDataSetSpecificRegionalSAMP4JERBSM() throws Exception {
		mqttPacketListenerTestcorrectDataSet(
				"{\"MessageFrame\": {\"messageId\": \"20\", \"value\": {\"BasicSafetyMessage\": {\"coreData\": {\"lat\": \"582813000\", \"angle\": \"127\", \"size\": {\"width\": \"1000\", \"length\": \"4000\"}, \"brakes\": {\"wheelBrakes\": \"00000\", \"traction\": {\"unavailable\": \"\"}, \"abs\": {\"unavailable\": \"\"}, \"scs\": {\"unavailable\": \"\"}, \"brakeBoost\": {\"unavailable\": \"\"}, \"auxBrakes\": {\"unavailable\": \"\"}}, \"id\": \"00 00 D8 7F\", \"transmission\": {\"unavailable\": \"\"}, \"speed\": \"8000\", \"long\": \"-1345110400\", \"accuracy\": {\"semiMajor\": \"255\", \"semiMinor\": \"255\", \"orientation\": \"65535\"}, \"heading\": \"28000\", \"accelSet\": {\"vert\": \"-127\", \"yaw\": \"0\", \"long\": \"2001\", \"lat\": \"2001\"}, \"msgCnt\": \"120\", \"secMark\": \"2415\", \"elev\": \"-4096\"}}}}}",
				"REGIONAL/SAMP/4/c/4/2/7/e/1/u/1/SW/NA/EMULATOR/JER/BSM", true);
	}

	/**
	 * In above case we are passing Topic = RegionalSAMP4JERBSM and messageFrame =
	 * BSM it retrun true;
	 */
	@Test
	public void mqttPacketListenerTestcorrectDataSetVZCV2X3INEMULATORUPERBSM() throws Exception {
		mqttPacketListenerTestcorrectDataSet(
				"{\"MessageFrame\": {\"messageId\": \"20\", \"value\": {\"BasicSafetyMessage\": {\"coreData\": {\"lat\": \"582813000\", \"angle\": \"127\", \"size\": {\"width\": \"1000\", \"length\": \"4000\"}, \"brakes\": {\"wheelBrakes\": \"00000\", \"traction\": {\"unavailable\": \"\"}, \"abs\": {\"unavailable\": \"\"}, \"scs\": {\"unavailable\": \"\"}, \"brakeBoost\": {\"unavailable\": \"\"}, \"auxBrakes\": {\"unavailable\": \"\"}}, \"id\": \"00 00 D8 7F\", \"transmission\": {\"unavailable\": \"\"}, \"speed\": \"8000\", \"long\": \"-1345110400\", \"accuracy\": {\"semiMajor\": \"255\", \"semiMinor\": \"255\", \"orientation\": \"65535\"}, \"heading\": \"28000\", \"accelSet\": {\"vert\": \"-127\", \"yaw\": \"0\", \"long\": \"2001\", \"lat\": \"2001\"}, \"msgCnt\": \"120\", \"secMark\": \"2415\", \"elev\": \"-4096\"}}}}}",
				"VZCV2X/3/IN/SW/NA/EMULATOR/55423/UPER/BSM", false);
	}

	/**
	 * In above case we are passing Topic = Indiviual3UPERBSM and messageFrame = BSM
	 * it return false as here we are passing IN3 and in indiviual data is already
	 * decoded so it return false;
	 */
	@Test
	public void mqttPacketListenerTestcorrectDataSetVZCV2X3INEMULATORUPERPSM() throws Exception {
		mqttPacketListenerTestcorrectDataSet(
				"{\"MessageFrame\": {\"messageId\": \"32\", \"value\": {\"PersonalSafetyMessage\": {\"position\": {\"lat\": \"582813000\", \"long\": \"-1345110400\"}, \"accuracy\": {\"semiMajor\": \"255\", \"semiMinor\": \"255\", \"orientation\": \"65535\"}, \"speed\": \"8000\", \"heading\": \"20000\", \"basicType\": {\"aPEDESTRIAN\": \"\"}, \"secMark\": \"20000\", \"msgCnt\": \"100\", \"id\": \"00 00 D8 7F\"}}}}",
				"VZCV2X/3/IN/SW/NA/EMULATOR/55423/UPER/PSM", false);
	}

	/**
	 * In above case we are passing Topic = Indiviual3UPERPSM and messageFrame = PSM
	 * it return false as here we are passing IN3 and in indiviual data is already
	 * decoded so it return false;
	 */
	@Test
	public void mqttPacketListenerTestcorrectDataSetVZCV2X3INEMULATORUPEREVA() throws Exception {
		mqttPacketListenerTestcorrectDataSet(
				"{\"MessageFrame\": {\"messageId\": \"27\", \"value\": {\"RoadSideAlert\": {\"typeEvent\": \"535\", \"description\": {\"ITIScodes\": [\"1025\", \"12545\", \"8711\"]}, \"heading\": \"0000000000000100\", \"extent\": {\"useFor100000meters\": \"\"}, \"position\": {\"lat\": \"582813000\", \"elevation\": \"100\", \"heading\": \"30\", \"long\": \"-1345110400\"}, \"furtherInfoID\": \"00 7B\", \"msgCnt\": \"123\", \"timeStamp\": \"123456\"}}}}",
				"VZCV2X/3/IN/SW/NA/EMULATOR/55423/UPER/EVA", false);
	}

	/**
	 * In above case we are passing Topic = Indiviual3UPEREVA and messageFrame = EVA
	 * it return false as here we are passing IN3 and in indiviual data is already
	 * decoded so it return false;
	 */
	@Test
	public void mqttPacketListenerTestwrongDataSet() throws Exception {
		mqttPacketListenerTestcorrectDataSet(
				"{\"MessageFrame\": {\"messageId\": \"32\", \"value\": {\"PersonalSafetyMessage\": {\"position\": {\"lat\": \"582813000\", \"long\": \"-1345110400\"}, \"accuracy\": {\"orientation\": \"65535\", \"semiMajor\": \"255\", \"semiMinor\": \"255\"}, \"speed\": \"8000\", \"heading\": \"20000\", \"basicType\": {\"aPEDESTRIAN\": \"\"}, \"secMark\": \"20000\", \"msgCnt\": \"100\", \"id\": \"00 00 D8 7F\"}}}}",
				"HELLO/DYN/5/#", false);
	}

	/**
	 * In above case we are passing Topic = Wrong data set and messageFrame = Demo
	 * it return false as here we are passing random data and return false not
	 * passing by any condition;
	 */
	@Test
	public void mqttPacketListenerTestcorrectDataSetSpecificRegionalDYN3JERPSMFilterKeyPSM() throws Exception {
		mqttPacketListenerTestcorrectDataSetWithDifferentFilterKey(
				"{\"MessageFrame\": {\"messageId\": \"32\", \"value\": {\"PersonalSafetyMessage\": {\"secMark\": \"20000\", \"msgCnt\": \"100\", \"id\": \"00 00 D8 7F\", \"position\": {\"lat\": \"582813000\", \"long\": \"-1345110400\"}, \"accuracy\": {\"semiMajor\": \"255\", \"semiMinor\": \"255\", \"orientation\": \"65535\"}, \"speed\": \"8000\", \"heading\": \"20000\", \"basicType\": {\"aPEDESTRIAN\": \"\"}}}}}",
				"REGIONAL/DYN/3/c/4/2/7/e/1/u/1/SW/NA/EMULATOR/JER/PSM", true, Constants.PSM);
	}


	/**
	 * In above case we are passing Topic = RegionalDYN3JERPSM with constant = PSM
	 * and messageFreame = PSM it retrun true;
	 */
	@Test
	public void mqttPacketListenerTestcorrectDataSetSpecificRegionalDYN3JERBSMFilterKeyBSM() throws Exception {
		mqttPacketListenerTestcorrectDataSetWithDifferentFilterKey(
				"{\"MessageFrame\": {\"messageId\": \"20\", \"value\": {\"BasicSafetyMessage\": {\"coreData\": {\"long\": \"-1345110400\", \"elev\": \"-4096\", \"transmission\": {\"unavailable\": \"\"}, \"id\": \"00 00 D8 7F\", \"accuracy\": {\"semiMajor\": \"255\", \"semiMinor\": \"255\", \"orientation\": \"65535\"}, \"heading\": \"28000\", \"brakes\": {\"scs\": {\"unavailable\": \"\"}, \"brakeBoost\": {\"unavailable\": \"\"}, \"auxBrakes\": {\"unavailable\": \"\"}, \"wheelBrakes\": \"00000\", \"traction\": {\"unavailable\": \"\"}, \"abs\": {\"unavailable\": \"\"}}, \"angle\": \"127\", \"size\": {\"length\": \"4000\", \"width\": \"1000\"}, \"msgCnt\": \"120\", \"secMark\": \"2415\", \"lat\": \"582813000\", \"speed\": \"8000\", \"accelSet\": {\"vert\": \"-127\", \"yaw\": \"0\", \"long\": \"2001\", \"lat\": \"2001\"}}}}}}",
				"REGIONAL/DYN/3/c/4/2/7/e/1/u/1/SW/NA/EMULATOR/JER/BSM", true, Constants.BSM);
	}

	/**
	 * In above case we are passing Topic = RegionalDYN3JERPSM with constant = BSM
	 * and messageFreame = BSM it retrun true;
	 */
	@Test
	public void mqttPacketListenerTestcorrectDataSetSpecificRegionalDYN3JERBSMSubTopicTRUCK() throws Exception {
		mqttPacketListenerTestcorrectDataSetWithDifferentFilterKey(
				"{\"MessageFrame\": {\"messageId\": \"20\", \"value\": {\"BasicSafetyMessage\": {\"coreData\": {\"long\": \"-1345110400\", \"elev\": \"-4096\", \"transmission\": {\"unavailable\": \"\"}, \"id\": \"00 00 D8 7F\", \"accuracy\": {\"semiMajor\": \"255\", \"semiMinor\": \"255\", \"orientation\": \"65535\"}, \"heading\": \"28000\", \"brakes\": {\"scs\": {\"unavailable\": \"\"}, \"brakeBoost\": {\"unavailable\": \"\"}, \"auxBrakes\": {\"unavailable\": \"\"}, \"wheelBrakes\": \"00000\", \"traction\": {\"unavailable\": \"\"}, \"abs\": {\"unavailable\": \"\"}}, \"angle\": \"127\", \"size\": {\"length\": \"4000\", \"width\": \"1000\"}, \"msgCnt\": \"120\", \"secMark\": \"2415\", \"lat\": \"582813000\", \"speed\": \"8000\", \"accelSet\": {\"vert\": \"-127\", \"yaw\": \"0\", \"long\": \"2001\", \"lat\": \"2001\"}}}}}}",
				"REGIONAL/DYN/3/t/t/q/s/g/4/9/y/VEH/TRUCK/VZ/JER/BSM", true, Constants.TRUCK);
	}

	/**
	 * In above case we are passing Topic = RegionalDYN3JERBSM with constant = EV
	 * and messageFreame = BSM it retrun true;
	 */
	@Test
	public void mqttPacketListenerTestcorrectDataSetSpecificRegionalDYN3JERBSMSubTopicPSGR() throws Exception {
		mqttPacketListenerTestcorrectDataSetWithDifferentFilterKey(
				"{\"MessageFrame\": {\"messageId\": \"20\", \"value\": {\"BasicSafetyMessage\": {\"coreData\": {\"long\": \"-1345110400\", \"elev\": \"-4096\", \"transmission\": {\"unavailable\": \"\"}, \"id\": \"00 00 D8 7F\", \"accuracy\": {\"semiMajor\": \"255\", \"semiMinor\": \"255\", \"orientation\": \"65535\"}, \"heading\": \"28000\", \"brakes\": {\"scs\": {\"unavailable\": \"\"}, \"brakeBoost\": {\"unavailable\": \"\"}, \"auxBrakes\": {\"unavailable\": \"\"}, \"wheelBrakes\": \"00000\", \"traction\": {\"unavailable\": \"\"}, \"abs\": {\"unavailable\": \"\"}}, \"angle\": \"127\", \"size\": {\"length\": \"4000\", \"width\": \"1000\"}, \"msgCnt\": \"120\", \"secMark\": \"2415\", \"lat\": \"582813000\", \"speed\": \"8000\", \"accelSet\": {\"vert\": \"-127\", \"yaw\": \"0\", \"long\": \"2001\", \"lat\": \"2001\"}}}}}}",
				"REGIONAL/DYN/3/t/t/q/s/g/4/9/y/VEH/PSGR/VZ/JER/BSM", true, Constants.PSGR);
	}

	/**
	 * In above case we are passing Topic = RegionalDYN3JERBSM with constant = PSGR
	 * and messageFreame = BSM it retrun true;
	 */
	@Test
	public void mqttPacketListenerTestcorrectDataSetSpecificRegionalDYN3JERBSMSubTopicEV() throws Exception {
		mqttPacketListenerTestcorrectDataSetWithDifferentFilterKey(
				"{\"MessageFrame\": {\"messageId\": \"20\", \"value\": {\"BasicSafetyMessage\": {\"coreData\": {\"long\": \"-1345110400\", \"elev\": \"-4096\", \"transmission\": {\"unavailable\": \"\"}, \"id\": \"00 00 D8 7F\", \"accuracy\": {\"semiMajor\": \"255\", \"semiMinor\": \"255\", \"orientation\": \"65535\"}, \"heading\": \"28000\", \"brakes\": {\"scs\": {\"unavailable\": \"\"}, \"brakeBoost\": {\"unavailable\": \"\"}, \"auxBrakes\": {\"unavailable\": \"\"}, \"wheelBrakes\": \"00000\", \"traction\": {\"unavailable\": \"\"}, \"abs\": {\"unavailable\": \"\"}}, \"angle\": \"127\", \"size\": {\"length\": \"4000\", \"width\": \"1000\"}, \"msgCnt\": \"120\", \"secMark\": \"2415\", \"lat\": \"582813000\", \"speed\": \"8000\", \"accelSet\": {\"vert\": \"-127\", \"yaw\": \"0\", \"long\": \"2001\", \"lat\": \"2001\"}}}}}}",
				"REGIONAL/DYN/3/t/t/q/s/g/4/9/y/VEH/EV/VZ/JER/BSM", true, Constants.EV);
	}

	/**
	 * In above case we are passing Topic = RegionalDYN3JERBSM with constant = TRUCK
	 * and messageFreame = BSM it retrun true;
	 */
	@Test
	public void mqttPacketListenerTestcorrectDataSetSpecificRegionalDYN3JEREVAFilterKeyEVA() throws Exception {
		mqttPacketListenerTestcorrectDataSetWithDifferentFilterKey(
				"{\"MessageFrame\": {\"messageId\": \"22\", \"value\": {\"EmergencyVehicleAlert\": {\"id\": \"00 00 0C 72\", \"rsaMsg\": {\"typeEvent\": \"9729\", \"position\": {\"long\": \"718288832\", \"lat\": \"215356832\", \"heading\": \"128\", \"speed\": {\"transmisson\": {\"neutral\": \"\"}, \"speed\": \"0\"}}, \"msgCnt\": \"116\"}}}}}",
				"REGIONAL/DYN/3/t/e/g/4/d/8/2/n/VEH/TRUCK/VZ/JER/EVA", true, Constants.EVA);
	}

	/**
	 * In above case we are passing Topic = RegionalDYN3JEREVA with constant = EVA
	 * and messageFreame = EVA it retrun true;
	 */
	@Test
	public void mqttPacketListenerTestcorrectDataSetRegionalSpecificDYN4JERPSMFilterKeyPSM() throws Exception {
		mqttPacketListenerTestcorrectDataSetWithDifferentFilterKey(
				"{\"MessageFrame\": {\"messageId\": \"32\", \"value\": {\"PersonalSafetyMessage\": {\"position\": {\"lat\": \"582813000\", \"long\": \"-1345110400\"}, \"accuracy\": {\"orientation\": \"65535\", \"semiMajor\": \"255\", \"semiMinor\": \"255\"}, \"speed\": \"8000\", \"heading\": \"20000\", \"basicType\": {\"aPEDESTRIAN\": \"\"}, \"secMark\": \"20000\", \"msgCnt\": \"100\", \"id\": \"00 00 D8 7F\"}}}}",
				"REGIONAL/DYN/4/c/4/2/7/e/1/u/1/SW/NA/EMULATOR/JER/PSM", true, Constants.PSM);
	}

	/**
	 * In above case we are passing Topic = RegionalDYN4JERPSM with constant = PSM
	 * and messageFreame = PSM it retrun true;
	 */
	@Test
	public void mqttPacketListenerTestcorrectDataSetSpecificRegionalDYN4JERBSMFilterKeyBSM() throws Exception {
		mqttPacketListenerTestcorrectDataSetWithDifferentFilterKey(
				"{\"MessageFrame\": {\"messageId\": \"20\", \"value\": {\"BasicSafetyMessage\": {\"coreData\": {\"secMark\": \"2415\", \"lat\": \"582813000\", \"speed\": \"8000\", \"heading\": \"28000\", \"angle\": \"127\", \"accelSet\": {\"long\": \"2001\", \"lat\": \"2001\", \"vert\": \"-127\", \"yaw\": \"0\"}, \"msgCnt\": \"120\", \"id\": \"00 00 D8 7F\", \"brakes\": {\"wheelBrakes\": \"00000\", \"traction\": {\"unavailable\": \"\"}, \"abs\": {\"unavailable\": \"\"}, \"scs\": {\"unavailable\": \"\"}, \"brakeBoost\": {\"unavailable\": \"\"}, \"auxBrakes\": {\"unavailable\": \"\"}}, \"size\": {\"width\": \"1000\", \"length\": \"4000\"}, \"transmission\": {\"unavailable\": \"\"}, \"long\": \"-1345110400\", \"elev\": \"-4096\", \"accuracy\": {\"orientation\": \"65535\", \"semiMajor\": \"255\", \"semiMinor\": \"255\"}}}}}}",
				"REGIONAL/DYN/4/c/4/2/7/e/1/u/1/SW/NA/EMULATOR/JER/BSM", true, Constants.BSM);
	}

	/**
	 * In above case we are passing Topic = RegionalDYN4JERBSM with constant = BSM
	 * and messageFreame = BSM it retrun true;
	 */
	@Test
	public void mqttPacketListenerTestcorrectDataSetSpecificRegionalDYN4JEREVAFilterKeyEVA() throws Exception {
		mqttPacketListenerTestcorrectDataSetWithDifferentFilterKey(
				"{\"MessageFrame\":{\"messageId\":\"22\",\"value\":{\"EmergencyVehicleAlert\":{\"id\":\"0000055D\",\"rsaMsg\":{\"msgCnt\":\"0\",\"typeEvent\":\"9729\",\"position\":{\"long\":\"772701595\",\"lat\":\"301392707\",\"elevation\":\"0\",\"heading\":\"267\",\"speed\":{\"transmisson\":{\"unavailable\":\"\"},\"speed\":\"0\"}}}}}}}",
				"REGIONAL/DYN/4/t/t/q/g/q/q/d/m/SW/NA/VZ/JER/EVA", true, Constants.EVA);
	}
	@Test
	public void mqttPacketListenerTestcorrectDataSetSpecificRegionalDYN4JEREVAFilterKeytruck() throws Exception {
		mqttPacketListenerTestcorrectDataSetWithDifferentFilterKey(
				"{\"MessageFrame\": {\"messageId\": \"20\", \"value\": {\"BasicSafetyMessage\": {\"coreData\": {\"long\": \"-1345110400\", \"elev\": \"-4096\", \"transmission\": {\"unavailable\": \"\"}, \"id\": \"00 00 D8 7F\", \"accuracy\": {\"semiMajor\": \"255\", \"semiMinor\": \"255\", \"orientation\": \"65535\"}, \"heading\": \"28000\", \"brakes\": {\"scs\": {\"unavailable\": \"\"}, \"brakeBoost\": {\"unavailable\": \"\"}, \"auxBrakes\": {\"unavailable\": \"\"}, \"wheelBrakes\": \"00000\", \"traction\": {\"unavailable\": \"\"}, \"abs\": {\"unavailable\": \"\"}}, \"angle\": \"127\", \"size\": {\"length\": \"4000\", \"width\": \"1000\"}, \"msgCnt\": \"120\", \"secMark\": \"2415\", \"lat\": \"582813000\", \"speed\": \"8000\", \"accelSet\": {\"vert\": \"-127\", \"yaw\": \"0\", \"long\": \"2001\", \"lat\": \"2001\"}}}}}}",
				"REGIONAL/DYN/3/t/t/q/s/g/4/9/y/VEH/TRUCK/VZ/JER/BSM", true, Constants.ALL);
	}
	@Test
	public void mqttPacketListenerTestcorrectDataSetSpecificRegionalDYN4JEREVAFilterKeypsgr() throws Exception {
		mqttPacketListenerTestcorrectDataSetWithDifferentFilterKey(
				"{\"MessageFrame\": {\"messageId\": \"20\", \"value\": {\"BasicSafetyMessage\": {\"coreData\": {\"long\": \"-1345110400\", \"elev\": \"-4096\", \"transmission\": {\"unavailable\": \"\"}, \"id\": \"00 00 D8 7F\", \"accuracy\": {\"semiMajor\": \"255\", \"semiMinor\": \"255\", \"orientation\": \"65535\"}, \"heading\": \"28000\", \"brakes\": {\"scs\": {\"unavailable\": \"\"}, \"brakeBoost\": {\"unavailable\": \"\"}, \"auxBrakes\": {\"unavailable\": \"\"}, \"wheelBrakes\": \"00000\", \"traction\": {\"unavailable\": \"\"}, \"abs\": {\"unavailable\": \"\"}}, \"angle\": \"127\", \"size\": {\"length\": \"4000\", \"width\": \"1000\"}, \"msgCnt\": \"120\", \"secMark\": \"2415\", \"lat\": \"582813000\", \"speed\": \"8000\", \"accelSet\": {\"vert\": \"-127\", \"yaw\": \"0\", \"long\": \"2001\", \"lat\": \"2001\"}}}}}}",
				"REGIONAL/DYN/3/t/t/q/s/g/4/9/y/VEH/PSGR/VZ/JER/BSM", true, Constants.ALL);
	}
	@Test
	public void mqttPacketListenerTestcorrectDataSetWithDifferentFilterKeynull() throws Exception {
		MqttMessage message = null;
		String sessionId = "A5F483E3C0D2461B2F508BF60870CBB6";
//		packetlistner.messageArrived(topic, message);
		UserWebSocketInfo userSession = new UserWebSocketInfo();
		MessageChannel m = new MessageChannel() {

			@Override
			public boolean send(Message<?> message, long timeout) {

				return true;
			}
		};
		SimpMessagingTemplate template = new SimpMessagingTemplate(m);
		userSession.setTemplate(template);
		userSession.setTopicSubscribed("REGIONAL/DYN/3/t/t/q/s/g/4/9/y/VEH/PSGR/VZ/JER/BSM");
		userSession.setSessionId(sessionId);
		boolean result1 = packetlistner.filterData("REGIONAL/DYN/3/t/t/q/s/g/4/9/y/VEH/PSGR/VZ/JER/BSM", message, userSession);
		assertThat(!result1).isEqualTo(true);
	}
@Test
public void mqttPacketListenerTestcorrectDataSet() throws Exception {
	MqttMessage message = null;
	String sessionId = "A5F483E3C0D2461B2F508BF60870CBB6";
//	packetlistner.messageArrived(topic, message);
	UserWebSocketInfo userSession = new UserWebSocketInfo();
	MessageChannel m = new MessageChannel() {

		@Override
		public boolean send(Message<?> message, long timeout) {

			return true;
		}
	};
	SimpMessagingTemplate template = new SimpMessagingTemplate(m);
	userSession.setTemplate(template);
	userSession.setTopicSubscribed(null);
	userSession.setSessionId(sessionId);
	boolean result1 = packetlistner.filterData("REGIONAL/DYN/3/t/t/q/s/g/4/9/y/VEH/PSGR/VZ/JER/BSM", message, userSession);
	assertThat(!result1).isEqualTo(true);
}

}