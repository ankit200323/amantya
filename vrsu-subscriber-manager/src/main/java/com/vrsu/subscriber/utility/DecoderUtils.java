package com.vrsu.subscriber.utility;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.time.LocalDateTime;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.oss.asn1.Coder;
import com.oss.asn1.EncodeFailedException;
import com.oss.asn1.EncodeNotSupportedException;
import com.vrsu.subscriber.model.DecodedData;
import com.vrsu.subscriber.model.UserWebSocketInfo;
import com.vrsu.subscriber.mqttdatamodel.BSMUPERDecoder;
import com.vrsu.subscriber.mqttdatamodel.EVAUPERDecoder;
import com.vrsu.subscriber.mqttdatamodel.MapDataUPERDecoder;
import com.vrsu.subscriber.mqttdatamodel.PSMUPERDecoder;
import com.vrsu.subscriber.mqttdatamodel.RoutedMsgOuterClass;
import com.vrsu.subscriber.mqttdatamodel.SPATUPERDecoder;
import com.vrsu.subscriber.service.DecodedDataService;

import j2735.J2735;
import j2735.dsrc.BasicSafetyMessage;
import j2735.dsrc.EmergencyVehicleAlert;
import j2735.dsrc.MapData;
import j2735.dsrc.PersonalSafetyMessage;
import j2735.dsrc.SPAT;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class DecoderUtils {

	@Autowired
	private DecodedDataService serv;

	@Autowired
	private BSMUPERDecoder bsmDecoder;

	@Autowired
	private JsonParser jsonParser;

	@Autowired
	private EVAUPERDecoder evaDecoder;

	@Autowired
	private PSMUPERDecoder psmDecoder;

	@Autowired
	private SPATUPERDecoder spatDecoder;

	@Autowired
	private MapDataUPERDecoder mapDecoder;

	@Setter
	@Getter
	boolean statusToSaveInDB;
	
	@Setter
	@Getter
	boolean timeBasedFlag;
	
		
	
	private static Gson gson = new Gson();

	public boolean onMessageArrived_routedMessageBSM(String topic, MqttMessage message, UserWebSocketInfo userSession) {
		try {
			if (topic.contains(VerizonEnums.UPERBSM.getValue())) {
				BasicSafetyMessage bsm = null;
				if (topic.contains(Constants.REGIONAL)) {
					RoutedMsgOuterClass.OutboundMsg outboundMsg = RoutedMsgOuterClass.OutboundMsg
							.parseFrom(message.getPayload());
					log.info("Message Topic REGIONAL: Decoding...UPER/BSM $topic   " + topic + message);
					bsm = bsmDecoder.decodeBSM(new ByteArrayInputStream(outboundMsg.getMsgBytes().toByteArray()));
					log.info("onMessageArrived_routedMessageBSM: UPER/BSM " + bsm);
				}
				if (topic.contains(Constants.IN)) {
					RoutedMsgOuterClass.RoutedMsg outboundMsg = RoutedMsgOuterClass.RoutedMsg
							.parseFrom(message.getPayload());
					log.info("Message Topic REGIONAL: Decoding...UPER/BSM $topic   " + topic + message);
					bsm = bsmDecoder.decodeBSM(new ByteArrayInputStream(outboundMsg.getMsgBytes().toByteArray()));
					log.info("onMessageArrived_routedMessageBSM: UPER/BSM " + topic);
				}
				JsonObject jsonObjectOutput = getJsonObjectAfterDecoding(bsm, Constants.BSM);
				jsonObjectOutput.addProperty(Constants.TYPE, Constants.BSM);
				jsonObjectOutput.addProperty(Constants.SUBTYPE, Constants.BSM);
				if (topic.contains(Constants.EV)) {
					jsonObjectOutput.addProperty(Constants.SUBTYPE, Constants.EV);
				} else if (topic.contains(Constants.PSGR)) {
					jsonObjectOutput.addProperty(Constants.SUBTYPE, Constants.PSGR);
				} else if (topic.contains(Constants.TRUCK)) {
					jsonObjectOutput.addProperty(Constants.SUBTYPE, Constants.TRUCK);
				}
				log.info("UPER/BSM Topic REGIONAL .................... " + jsonObjectOutput.toString());
				log.info("UPER/BSM TOPIC REGIONSL VALUE CHECKING OF GSON TO JSON  .......... " + gson.toJson(bsm));

				
				userSession.getTemplate().convertAndSend(Constants.WS_RSADATA + "/" + userSession.getSessionId(),
						gson.toJson(jsonObjectOutput));
				prepareData(jsonObjectOutput, topic, Constants.BasicSafetyMessage);
				return true;
			} else if (topic.contains(VerizonEnums.JERBSM.getValue())) {
				String str = new String(message.getPayload());
				String json = str.substring(str.indexOf("{"), str.lastIndexOf("}") + 1);
				JsonObject jsonObjectOutput = (JsonObject) jsonParser.parse(json);
				jsonObjectOutput.addProperty("type", Constants.BSM);
				jsonObjectOutput.addProperty("subtype", Constants.BSM);
				jsonObjectOutput.addProperty("topicType", Constants.JER);
				
				userSession.getTemplate().convertAndSend(Constants.WS_RSADATA + "/" + userSession.getSessionId(),
						gson.toJson(jsonObjectOutput));
				prepareData(jsonObjectOutput, topic, Constants.BasicSafetyMessage);
				return true;

			}
			return false;
		} catch (Exception e) {
			log.error("Message Topic REGIONAL: --->BSM Exception: " + e.getMessage());
			log.error("Message Topic REGIONAL: --->BSM Exception:Topic $topic");
			return false;
		}
	}

	public boolean onMessageArrived_routedMessageEVA(String topic, MqttMessage message, UserWebSocketInfo userSession) {
		try {
			EmergencyVehicleAlert eva = null;
			if (topic.contains(VerizonEnums.UPEREVA.getValue())) {
				if (topic.contains(Constants.REGIONAL)) {
					RoutedMsgOuterClass.OutboundMsg outboundMsg = RoutedMsgOuterClass.OutboundMsg
							.parseFrom(message.getPayload());
					log.info("Message Topic REGIONAL: Decoding...UPER/EVA $topic contains REGIONAL  ");
					eva = evaDecoder.decodeEVA(new ByteArrayInputStream(outboundMsg.getMsgBytes().toByteArray()));
					log.info("onMessageArrived_routedMessageEVA: UPER/EVA $topic contains REGIONAL " + eva.timeStamp);
				}
				if (topic.contains("IN")) {
					RoutedMsgOuterClass.RoutedMsg outboundMsg = RoutedMsgOuterClass.RoutedMsg
							.parseFrom(message.getPayload());
					log.info("Message Topic REGIONAL: Decoding...UPER/EVA $topic contains IN  " + topic + message);
					eva = evaDecoder.decodeEVA(new ByteArrayInputStream(outboundMsg.getMsgBytes().toByteArray()));
					log.info("onMessageArrived_routedMessageEVA: UPER/EVA $topic contains IN " + eva.timeStamp);
				}

				JsonObject jsonObjectOutput = getJsonObjectAfterDecoding(eva, Constants.EVA);
				jsonObjectOutput.addProperty(Constants.TYPE, Constants.EVA);

				
				userSession.getTemplate().convertAndSend(Constants.WS_RSADATA + "/" + userSession.getSessionId(),
						gson.toJson(jsonObjectOutput));
				prepareData(jsonObjectOutput, topic, Constants.EmergencyVehicleAlert);
				return true;
			}
			if (topic.contains(VerizonEnums.JEREVA.getValue())) {
				String str = new String(message.getPayload());
				String json = str.substring(str.indexOf("{"), str.lastIndexOf("}") + 1);
				JsonObject jsonObjectOutput = (JsonObject) jsonParser.parse(json);
				jsonObjectOutput.addProperty("type", Constants.EVA);
				jsonObjectOutput.addProperty("subtype", Constants.EVA);
				jsonObjectOutput.addProperty("topicType", Constants.JER);
				
				userSession.getTemplate().convertAndSend(Constants.WS_RSADATA + "/" + userSession.getSessionId(),
						gson.toJson(jsonObjectOutput));
				prepareData(jsonObjectOutput, topic, Constants.EmergencyVehicleAlert);
				return true;
			}
			return false;
		} catch (Exception e) {
			log.error("Message Topic REGIONAL: --->EVA Exception: " + e.getMessage());
			log.error("Message Topic REGIONAL: --->EVA Exception:Topic $topic");
			return false;
		}
	}

	public boolean onMessageArrived_routedMessagePSM(String topic, MqttMessage message, UserWebSocketInfo userSession) {
		try {
			PersonalSafetyMessage psm = null;
			if (topic.contains(Constants.UPER_PSM)) {
				if (topic.contains(Constants.REGIONAL)) {
					log.error("Message Topic in REGIONAL " + topic);
					log.error("Message Topic in Individual " + topic);
					RoutedMsgOuterClass.RoutedMsg outboundMsg = RoutedMsgOuterClass.RoutedMsg
							.parseFrom(message.getPayload());
					log.info("Message Topic REGIONAL: Decoding...UPER/PSM $topic contains IN  " + topic);
					psm = psmDecoder.decodePSM(new ByteArrayInputStream(outboundMsg.getMsgBytes().toByteArray()));
//					RoutedMsgOuterClass.OutboundMsg outboundMsg = RoutedMsgOuterClass.OutboundMsg
//							.parseFrom(message.getPayload());
					log.info("Message Topic REGIONAL: Decoding...UPER/PSM $topic contains REGIONAL  " + topic);
					psm = psmDecoder.decodePSM(new ByteArrayInputStream(outboundMsg.getMsgBytes().toByteArray()));
				}
				if (topic.contains(Constants.IN)) {
					log.error("Message Topic in Individual " + topic);
					RoutedMsgOuterClass.RoutedMsg outboundMsg = RoutedMsgOuterClass.RoutedMsg
							.parseFrom(message.getPayload());
					log.info("Message Topic REGIONAL: Decoding...UPER/PSM $topic contains IN  " + topic);
					psm = psmDecoder.decodePSM(new ByteArrayInputStream(outboundMsg.getMsgBytes().toByteArray()));
				}
				JsonObject jsonObjectOutput = getJsonObjectAfterDecoding(psm, Constants.PSM);
				jsonObjectOutput.addProperty("type", Constants.PSM);

				prepareData(jsonObjectOutput, topic, Constants.PersonalSafetyMessage);
				userSession.getTemplate().convertAndSend(Constants.WS_RSADATA + "/" + userSession.getSessionId(),
						gson.toJson(jsonObjectOutput));
				return true;
			}
			if (topic.contains(Constants.JER_PSM)) {
				String str = new String(message.getPayload());
				String json = str.substring(str.indexOf("{"), str.lastIndexOf("}") + 1);
				JsonObject jsonObjectOutput = (JsonObject) jsonParser.parse(json);
				jsonObjectOutput.addProperty("type", Constants.PSM);
				jsonObjectOutput.addProperty("subtype", Constants.PSM);
				jsonObjectOutput.addProperty("topicType", Constants.JER);
				prepareData(jsonObjectOutput, topic, Constants.PersonalSafetyMessage);
				userSession.getTemplate().convertAndSend(Constants.WS_RSADATA + "/" + userSession.getSessionId(),
						gson.toJson(jsonObjectOutput));
				return true;
			}
			return false;
		} catch (Exception e) {
			log.error("Message Topic REGIONAL: --->PSM Exception: " + e.getMessage());
			log.error("Message Topic REGIONAL: --->PSM Exception:Topic $topic");
			return false;
		}
	}

	public boolean onMessageArrived_routedMessageSPAT(String topic, MqttMessage message,
			UserWebSocketInfo userSession) {
		try {
			if (topic.contains("UPER/SPAT") && topic.contains(Constants.REGIONAL)) {
				RoutedMsgOuterClass.OutboundMsg outboundMsg = RoutedMsgOuterClass.OutboundMsg
						.parseFrom(message.getPayload());
				log.info("encoded spat" + message);
				log.info("Message Topic REGIONAL: Decoding...UPER/SPAT $topic contains REGIONAL  " + topic);
				SPAT spat = spatDecoder.decodeSPAT(new ByteArrayInputStream(outboundMsg.getMsgBytes().toByteArray()));
				Coder coder = J2735.getJSONCoder();
				coder.enableAutomaticEncoding();
				coder.enableAutomaticDecoding();
				coder.enableEncoderDebugging();
				coder.enableDecoderDebugging();
				String relax = System.getProperty(Constants.RELAXED_MODE);
				if (relax != null && relax.equalsIgnoreCase("on")) {
					coder.enableRelaxedDecoding();
				}
				ByteArrayOutputStream sink = new ByteArrayOutputStream();
				/*
				 * Encode the PDU.
				 */
				coder.encode(spat, sink);

				byte[] encoding = sink.toByteArray();
				String userDetailsBytesToStrings = new String(encoding, StandardCharsets.UTF_8);

				// JSONArray elements, we need to parse the
				// output in the form of JsonArray
				JsonObject jsonObjectOutput = (JsonObject) jsonParser.parse(userDetailsBytesToStrings);
				jsonObjectOutput.addProperty("type", Constants.SPAT);
				
				userSession.getTemplate().convertAndSend(Constants.WS_RSADATA + "/" + userSession.getSessionId(),
						gson.toJson(jsonObjectOutput));
				prepareData(jsonObjectOutput, topic, Constants.SPAT);
				return true;
			} else if (topic.contains("JER/SPAT")) {
				String str = new String(message.getPayload());
				String json = str.substring(str.indexOf("{"), str.lastIndexOf("}") + 1);
				JsonObject jsonObjectOutput = (JsonObject) jsonParser.parse(json);
				jsonObjectOutput.addProperty("type", Constants.SPAT);
				jsonObjectOutput.addProperty("topicType", Constants.JER);
				userSession.getTemplate().convertAndSend(Constants.WS_RSADATA + "/" + userSession.getSessionId(),
						gson.toJson(jsonObjectOutput));
				prepareData(jsonObjectOutput, topic, Constants.SPAT);
				return true;
			}
			return false;
		} catch (Exception e) {
			log.error("Message Topic REGIONAL: --->SPAT Exception: " + e.getMessage());
			log.error("Message Topic REGIONAL: --->SPAT Exception:Topic $topic");
			return false;
		}
	}

	public boolean onMessageArrived_routedMessageMAP(String topic, MqttMessage message, UserWebSocketInfo userSession) {

		try {
			if (topic.contains("UPER/MAP") && topic.contains(Constants.REGIONAL)) {

				log.info("encoded map" + message);
				log.info("Message Topic REGIONAL: Decoding...UPER/MAP $topic contains IN  " + topic);
				MapData mapData = mapDecoder.decodeMap(new ByteArrayInputStream(message.getPayload()));

				Coder coder = J2735.getJSONCoder();
				coder.enableAutomaticEncoding();
				coder.enableAutomaticDecoding();
				coder.enableEncoderDebugging();
				coder.enableDecoderDebugging();
				String relax = System.getProperty(Constants.RELAXED_MODE);
				if (relax != null && relax.equalsIgnoreCase("on")) {
					coder.enableRelaxedDecoding();
				}
				ByteArrayOutputStream sink = new ByteArrayOutputStream();
				coder.encode(mapData, sink);
				byte[] encoding = sink.toByteArray();
				String userDetailsBytesToStrings = new String(encoding, StandardCharsets.UTF_8);
				JsonObject jsonObjectOutput = (JsonObject) jsonParser.parse(userDetailsBytesToStrings);
				jsonObjectOutput.addProperty("type", Constants.MAP);
				
				userSession.getTemplate().convertAndSend(Constants.WS_RSADATA + "/" + userSession.getSessionId(),
						gson.toJson(jsonObjectOutput));
				prepareData(jsonObjectOutput, topic, Constants.MAP);
				return true;
			} else if (topic.contains("JER/MAP")) {
				String str = new String(message.getPayload());
				String json = str.substring(str.indexOf("{"), str.lastIndexOf("}") + 1);
				JsonObject jsonObjectOutput = (JsonObject) jsonParser.parse(json);
				jsonObjectOutput.addProperty("type", Constants.MAP);
				jsonObjectOutput.addProperty("topicType", Constants.JER);
				userSession.getTemplate().convertAndSend(Constants.WS_RSADATA + "/" + userSession.getSessionId(),
						gson.toJson(jsonObjectOutput));
				prepareData(jsonObjectOutput, topic, Constants.MAP);
				return true;
			}
			return false;
		} catch (Exception e) {
			log.error("Message Topic REGIONAL: --->MAP Exception: " + e.getMessage());
			log.error("Message Topic REGIONAL: --->MAP Exception:Topic $topic");
			return false;
		}
	}

	private JsonObject getJsonObjectAfterDecoding(Object obj, String type)
			throws EncodeFailedException, EncodeNotSupportedException {

		Coder coder = J2735.getJSONCoder();
		coder.enableAutomaticEncoding();
		coder.enableAutomaticDecoding();
		coder.enableEncoderDebugging();
		coder.enableDecoderDebugging();
		String relax = System.getProperty(Constants.RELAXED_MODE);
		if (relax != null && relax.equalsIgnoreCase("on")) {
			coder.enableRelaxedDecoding();
		}
		ByteArrayOutputStream sink = new ByteArrayOutputStream();
		if (type.contains(Constants.BSM)) {
			coder.encode((BasicSafetyMessage) obj, sink);
		}
		if (type.contains(Constants.EVA)) {
			coder.encode((EmergencyVehicleAlert) obj, sink);
		}
		if (type.contains(Constants.PSM)) {
			coder.encode((PersonalSafetyMessage) obj, sink);
		}

		byte[] encoding = sink.toByteArray();
		String userDetailsBytesToStrings = new String(encoding, StandardCharsets.UTF_8);
		return (JsonObject) jsonParser.parse(userDetailsBytesToStrings);
	}

	public void prepareData(JsonObject jsonObjectOutput, String topic, String type) throws ParseException {
	
//		if(statusToSaveInDB && isTimeBasedFlag())
//		{
			
		DecodedData dd = new DecodedData();
		if (!topic.contains(Constants.UPER)) {
			JsonObject data;
			switch (type) {
			case Constants.BasicSafetyMessage: {
				data = jsonObjectOutput.get("MessageFrame").getAsJsonObject().get("value").getAsJsonObject().get(type)
						.getAsJsonObject().get("coreData").getAsJsonObject();
				dd.setPublisherId(data.get("id").getAsString());
				dd.setSpeed(data.get("speed").getAsString());
				dd.setLatitude(data.get("lat").getAsString());
				dd.setLongitude(data.get("long").getAsString());
				dd.setTopic(topic);
				dd.setMessageType(jsonObjectOutput.get("type").getAsString());
				dd.setDirection(data.get("heading").getAsString());
				dd.setTimestamp(LocalDateTime.now());
			}
				break;
			case Constants.EmergencyVehicleAlert: {
				data = jsonObjectOutput.get("MessageFrame").getAsJsonObject().get("value").getAsJsonObject().get(type)
						.getAsJsonObject().get("rsaMsg").getAsJsonObject();
				dd.setPublisherId(jsonObjectOutput.get("MessageFrame").getAsJsonObject().get("value").getAsJsonObject()
						.get(type).getAsJsonObject().get("id").getAsString());
				dd.setSpeed(data.get("position").getAsJsonObject().get("speed").getAsJsonObject().get("speed")
						.getAsString());
				dd.setLatitude(data.get("position").getAsJsonObject().get("lat").getAsString());
				dd.setLongitude(data.get("position").getAsJsonObject().get("long").getAsString());
				dd.setTopic(topic);
				dd.setMessageType("EVA");
				dd.setDirection(data.get("position").getAsJsonObject().get("heading").getAsString());
				dd.setTimestamp(LocalDateTime.now());
			}
				break;
			case Constants.PersonalSafetyMessage: {
				data = jsonObjectOutput.get("MessageFrame").getAsJsonObject().get("value").getAsJsonObject().get(type)
						.getAsJsonObject();

				dd.setPublisherId(data.get("id").getAsString());
				dd.setSpeed(data.get("speed").getAsString());
				dd.setLatitude(data.get("position").getAsJsonObject().get("lat").getAsString());
				dd.setLongitude(data.get("position").getAsJsonObject().get("long").getAsString());
				dd.setTopic(topic);
				dd.setMessageType("PSM");
				dd.setDirection(data.get("heading").getAsString());
				dd.setTimestamp(LocalDateTime.now());
			}
				break;
			case Constants.SPAT: {
				data = jsonObjectOutput.get("MessageFrame").getAsJsonObject().get("value").getAsJsonObject().get(type)
						.getAsJsonObject();
				dd.setPublisherId(data.get("name").getAsString());
				dd.setTopic(topic);
				dd.setMessageType(Constants.SPAT);
				dd.setTimestamp(LocalDateTime.now());
			}
				break;
			case Constants.MAP: {
				data = jsonObjectOutput.get("MessageFrame").getAsJsonObject().get("value").getAsJsonObject().get("MapData")
						.getAsJsonObject().get("intersections").getAsJsonObject().get("IntersectionGeometry").getAsJsonObject();
				dd.setPublisherId(data.get("name").getAsString());
				dd.setLatitude(data.get("refPoint").getAsJsonObject().get("lat").getAsString());
				dd.setLongitude(data.get("refPoint").getAsJsonObject().get("long").getAsString());
				dd.setTopic(topic);
				dd.setMessageType(Constants.MAP);
				dd.setTimestamp(LocalDateTime.now());
			}
				break;
			}
			serv.save(dd);
			dd = null;
		} else {

			JsonObject data;
			switch (type) {
			case Constants.BasicSafetyMessage: {
				data = jsonObjectOutput.get("coreData").getAsJsonObject();
				dd.setPublisherId(data.get("id").getAsString());
				dd.setSpeed(data.get("speed").getAsString());
				dd.setLatitude(data.get("lat").getAsString());
				dd.setLongitude(data.get("long").getAsString());
				dd.setTopic(topic);
				dd.setMessageType(jsonObjectOutput.get("type").getAsString());
				dd.setDirection(data.get("heading").getAsString());
				dd.setTimestamp(LocalDateTime.now());
			}
				break;
			case Constants.EmergencyVehicleAlert: {
				data = jsonObjectOutput.get("rsaMsg").getAsJsonObject();
				dd.setPublisherId(jsonObjectOutput.get("id").getAsString());
				dd.setSpeed(data.get("position").getAsJsonObject().get("speed").getAsJsonObject().get("speed")
						.getAsString());
				dd.setLatitude(data.get("position").getAsJsonObject().get("lat").getAsString());
				dd.setLongitude(data.get("position").getAsJsonObject().get("long").getAsString());
				dd.setTopic(topic);
				dd.setMessageType("EVA");
				dd.setDirection(data.get("position").getAsJsonObject().get("heading").getAsString());
				dd.setTimestamp(LocalDateTime.now());
			}
				break;
			case Constants.PersonalSafetyMessage: {
				data = jsonObjectOutput;

				dd.setPublisherId(data.get("id").getAsString());
				dd.setSpeed(data.get("speed").getAsString());
				dd.setLatitude(data.get("position").getAsJsonObject().get("lat").getAsString());
				dd.setLongitude(data.get("position").getAsJsonObject().get("long").getAsString());
				dd.setTopic(topic);
				dd.setMessageType("PSM");
				dd.setDirection(data.get("heading").getAsString());
				dd.setTimestamp(LocalDateTime.now());
			}
			case Constants.SPAT: {
				dd.setPublisherId(jsonObjectOutput.get("name").getAsString());
				dd.setTopic(topic);
				dd.setTimestamp(LocalDateTime.now());
				dd.setMessageType(Constants.SPAT);
			}
				break;
			case Constants.MAP: {
				data = jsonObjectOutput.get("intersections").getAsJsonArray().get(0).getAsJsonObject();
				dd.setPublisherId(data.get("id").getAsJsonObject().get("id").getAsString());
				dd.setLatitude(data.get("refPoint").getAsJsonObject().get("lat").getAsString());
				dd.setLongitude(data.get("refPoint").getAsJsonObject().get("long").getAsString());
				dd.setTopic(topic);
				dd.setMessageType(Constants.MAP);
				dd.setTimestamp(LocalDateTime.now());
			}
				break;
			}
			serv.save(dd);
			dd = null;
//		}
		}
	}
}